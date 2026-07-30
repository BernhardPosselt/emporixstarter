# Authentication

There are 3 types of tokens:

* Service Tokens
* Anonymous Tokens
* Customer Tokens

A service token is required when making requests to admin APIs like creating tokens and can be set up with client id and secret.

All other APIs require either anonymous or customer tokens. When a user logs in, they send their anonymous token to convert their current session into a customer session, merging carts etc.

## SSO

Login can be implemented entirely in [Emporix and the Frontend](https://developer.emporix.io/api-references/quickstart/authentication-and-authorization/customer-authentication/sso-authentication)

At the very end, Emporix issues an OAuth token, that can be used henceforth

## State

Once OAuth Tokens and their scopes are retrieved, they need to be stored somewhere. There are a couple of possibilities:

* **Store them in your session**: if you have a Redis or JDBC session already, you can easily store more information in it
* **Store them at the client**: You can either use an encrypted JWT or encrypted token. The downside of doing that is that rotating the encryption password is a massive pain. JWTs in addition are difficult to revoke, their upside only ever coming to fruition when multiple systems are accessed. Since this state needs to be sent the first time the site accesses the backend, both values need to be stored inside cookies.

There is additional state that needs to be handled: the [Emporix Session](https://developer.emporix.io/api-references/api-guides/users-and-permissions/session-context/session-context). Emporix offers a REST API to store and retrieve predefined and custom state. This state is shared across any application accessing the APIs and stores values affecting all API requests, like language. This state is difficult to store on the client due to the 4Kb limitation of cookie size.

Both APIs need to be cached, otherwise, every request to our backend facilities 2 requests to Emporix APIs: one to retrieve the current session, the other to introspect the current token and its scopes.

## Permissions

There are 2 types of permissions

* Groups
* OAuth Scopes

Groups tell an application what users are allowed to do. OAuth Scopes limit the accessibility of REST endpoints in Emporix and are bound to the token's lifetime. Adding a new scope to a token more often than not requires a new login. Therefore, groups can not be handled using OAuth Scopes. 

Emporix offers an [IAM Endpoint](https://developer.emporix.io/api-references/api-guides/users-and-permissions/iam/iam) where users not only can be put into groups, but groups can also provide additional OAuth Scopes.

These groups need to be synced regularly.