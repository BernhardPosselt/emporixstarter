# Authentication

There are 3 types of tokens:

* Service Tokens
* Anonymous Tokens
* Customer Tokens

A service token is required when making requests to admin APIs like creating tokens and can be set up with client id and secret.

All other APIs require either anonymous or customer tokens. When a user logs in, they send their anonymous token to convert their current session into a customer session, merging carts etc.

## SSO

Login can be implemented entirely in [Emporix and the Frontend](https://developer.emporix.io/api-references/quickstart/authentication-and-authorization/customer-authentication/sso-authentication)

At the very end, Emporix issues an OAuth token, that can be used henceforth. **These tokens must not be stored on the client in plain text!**

## State

Once OAuth Tokens and their scopes are retrieved, they need to be stored somewhere. There are a couple of possibilities:

* **Store them in your session**: if you have a Redis or JDBC session already, you can easily store more information in it. This also allows you to share sessions across different APIs, like for instance mobile devices.
* **Store them at the client**: You can either use an encrypted JWT or encrypted token. The downside of doing that is that rotating the encryption password is a massive pain. JWTs in addition are difficult to revoke, their upside only ever coming to fruition when a large number of systems need to verify data without contacting the authorization server. Since this state needs to be sent the first time the site accesses the backend, both values need to be stored inside cookies.

There is additional state that needs to be handled: the [Emporix Session](https://developer.emporix.io/api-references/api-guides/users-and-permissions/session-context/session-context). Emporix offers a REST API to store and retrieve predefined and custom state. This state is shared across any application accessing the APIs and stores values affecting all API requests, like language. This state is difficult to store on the client due to the 4Kb limitation of cookie size.

Both APIs need to be cached, otherwise, every request to our backend facilities 2 requests to Emporix APIs: one to retrieve the current session, the other to introspect the current token and its scopes.

### Token Refresh

Token refresh is implemented by issuing a new token if 

    (date_issued + expires_in + safety_margin) > date_now

The most important things to look out for are races and token expiration/session interaction

Races can occur if 2 requests try to refresh the same token at the same time. Usually, this issue can be ignored unless the token endpoint has restrictions that disallow using the same refresh token to be used more than once (which is not uncommon). In a similar vein, providers sometimes only allow a specific number of active Oauth tokens. Creating more tokens than required would impact the maximum amount of active tokens. **This needs to be investigated.**

In a similar fashion, the lifetime of a session can outlast the lifetime of an authentication and even refresh token. Ideally, the session has a shorter lifetime than the lifetime of the refresh token.

### Sessions

Sessions are the most common way to cache data. The user receives a new session id on login which is then usually saved inside the user's browser using an HTTPS Only cookie.

Session data is then retrieved on each request from your session storage based on the id. A logout deletes the data from the storage and usually deletes the session cookie as well.

There are 3 challenges with this approach:

* Storage
* Security
* Synchronization

#### Storage

Typically, session data is persisted in files. This does not scale well if more than one server is active at a time, so people usually reach for a database like Postgres or Redis. Usually, Redis is chosen because it supports session expiration out of the box.

There's also the possibility of using sticky sessions. In Nginx, if enabled, this sets an additional cookie that saves the server that was first accessed through the load balancer. This server is henceforth always used for this user. If the server goes down, e.g. through a deployment or through auto-scaling, then this approach runs into issues with uptime.

#### Synchronization

Since data needs to be written to a cache (and cleaned up/invalidated), there needs to be a synchronization mechanism.

Synchronization needs to ensure that 2 concurrent requests to the storage don't override each other. Spring solves this by only writing deltas to your session. This means that only changed keys are saved, so 2 servers writing distinct attributes don't override each other's data. But if 2 servers write the same data, the last one to finish will override the other request. 

The session in Spring Boot is persisted using an expiration date. The session will be deleted when an expired session is accessed.

#### Security

When using sessions and session cookies, there are a couple of things to keep in mind security wise. Since cookies are always attached to a request to a domain + path, an attacker can embed an iframe to that domain (GET) or submit a form (POST) performing actions on behalf of the user. 

The great thing is that these attacks are limited to very simple requests, so requiring an application/json header alone or a custom header will already block these attacks.

Typically, a CSRF token is required to protect against these attacks, however there are newer technologies like Sec-Fetch-* headers that do the job just as well if they fit your use case (Same-Site cookies are not a solution!).

## Permissions

There are 2 types of permissions

* Groups
* OAuth Scopes

Groups tell an application what users are allowed to do. OAuth Scopes limit the accessibility of REST endpoints in Emporix and are bound to the token's lifetime. Adding a new scope to a token more often than not requires a new login. Therefore, groups can not be handled using OAuth Scopes. 

Emporix offers an [IAM Endpoint](https://developer.emporix.io/api-references/api-guides/users-and-permissions/iam/iam) where users not only can be put into groups, but groups can also assign additional OAuth Scopes.

These groups need to be synced regularly.

## Solution Proposal

### Anon Token

1. Requests to the BFF that require a session like adding a product to a cart without session cookies will lease a new anonymous token in the BFF
2. That token is then stored in a new session
3. The client receives a session cookie

### Login

There are 2 possibilities:

#### Customer Login

1. If the user clicks on login, the FE presents them a login page
2. The login page sends the users anon token and login data to Emporix to lease a new customer token
3. The BFF uses the tokens to retrieve the users session data and groups and stores them in the user's session
4. Next a session cookie is issued that lives shorter than the refresh token

#### SSO

1. If the user clicks on login, the FE redirects them to the SSO login page
2. The login page redirects back to Emporix which issues tokens and redirects back to the FE with the tokens
3. The FE then uses the tokens to start a new session in the BFF
4. The BFF merges the customer cart with the anonymous cart
5. The BFF uses the tokens to retrieve the users session data and groups and stores them in the user's session
6. Next a session cookie is issued that lives shorter than the refresh token

### Logout

1. If the user clicks logout, the BFF deletes their session cookie and the logout customer API is called
2. The customer is then issued an anonymous token session cookie

### Sessions

* Sessions are stored in Redis or Valkey; the reason for that is that Redis supports sessions out of the box and both Spring Web and Reactive support it
* A users session is updated once they log in or when they change session attributes like preferred language
* A user session is also updated once a **customer.updated** event is fired in the OE (or other events that change users and groups); this requires Emporix to call our APIs


### CSRF

CSRF is an attack to force a user to perform an authenticated request. This works on the back of cookies, because cookies are always sent with each request to a target domain. By default, the browser only allows a small subset of requests to bypass CORS (which is an attempt to fix this issue)

This means the following things:

* GET requests must never mutate anything
* POST requests with form data or application url encoded requests should not be allowed
* Additional headers can be required

In Spring, there are 2 ways that can be combined to solve this: a special **/csrf** endpoint:

```kt
@RestController
class CsrfController {
    @GetMapping("/csrf")
    fun csrf(csrfToken: CsrfToken): CsrfToken {
        return csrfToken
    }
}
```

and a controller advice that adds the token as a header to each successful request:

```kt
@ControllerAdvice
class CsrfControllerAdvice {
	@ModelAttribute
	fun getCsrfToken(response: HttpServletResponse, csrfToken: CsrfToken) {
		response.setHeader(csrfToken.headerName, csrfToken.token)
	}
}
```

The token is then sent as **X-CSRF-TOKEN** header to the client. On the server side, the csrf token is stored inside the session and changes each time it is sent to avoid BREACH attacks (=guessing the token in encrypted requests by adding data to the website and check response size when compression is enabled; smaller response size: I guess part of the token)