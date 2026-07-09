# Multiplatform Lib

First start your local nexus repository

    mkdir -p docker && sudo chown -R 200 docker
    docker compose up

Nexus is available at http://localhost:8000

Then log in as admin and the password stored in **admin.password**

    cat docker/nexus/admin.password

Then create an npm hosted repository called **npm**. Next create a role called Nexus and give it the following privileges:

* nx-repository-admin-npm-npm-*
* nx-repository-view-maven2-\*-*
* nx-repository-view-npm-\*-*

Then create a user with password and id **nexus** and add the **Nexus** role.

Finally, create an npm proxy with name **npm-proxy** and group called **npm-public** which includes the npm registry and npmjs proxy

Publish maven build:

    ./gradlew publishAllPublicationsToMavenRepository

Publish npm build:

    ./gradlew publishJsPackageToNexusRegistry

