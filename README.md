# Project Setup

What you need to run this project:

* JDK 25 (TODO: devcontainers)
* docker or podman

## Gradle Setup

There are 2 different kinds of projects that we build:

* [Composite Builds](https://docs.gradle.org/current/userguide/composite_builds.html)
* [Multi-Project Builds](https://docs.gradle.org/current/userguide/multi_project_builds.html)

Multi-Project Builds allow you to split up your project into separate modules (think atscore, atsfacdes, etc.). These all  are built and released together.

Composite Builds are separate builds that can be built and released independently of each other. They also include shared build logic.

Each Project has its own wrapper. While it's technically not required, IntelliJ does not seem to work with having just a parent wrapper directory. This also means that you need to manually update all projects using:

    ./gradlew wrapper --gradle-version=x.x.x

each time there's a Gradle update.


## Project Structure

### libs.versions.toml

This is where all library versions across all projects are persisted. That way, you only need to update dependencies once and ensure consistent versions across all projects.

This needs the following block in each project's **settings.gradle.kts**

```kt
dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../libs.versions.toml"))
        }
    }
}
```

### build-logic

This project includes Convention Plugins (= included **build.gradle.kts** code) and tasks, that are the same across many projects. To make use of this folder in your project, you need to add this to your **settings.gradle.kts**:

```kt
includeBuild("../build-logic")
```

If you are only using Convention Plugins, no further changes are required. 

If you only want to make use of tasks defined inside this folder and no convention plugin, your **build.gradle.kts** needs to add this block at the very top to add files from this directory to your classpath:

```kt
buildscript {
    dependencies {
        classpath(":build-logic")
    }
}
```

### TODO

* Containerization
* Devcontainers
* CI
* Spring Boot Setup
* Spring Security
* Spring Integration Test Template
* Spring Unit Test Template
* Spring Controller MVC Test Template
* Emporix API Client extension
* Push to Repo
* Static Analysis
* Formatter
* Github Actions CI Pipeline
* K8s setup
* Think about authorization/OAuth