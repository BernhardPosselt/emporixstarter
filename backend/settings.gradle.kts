rootProject.name = "emporixstarter"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../libs.versions.toml"))
        }
    }
}

includeBuild("../api-client")
includeBuild("../build-logic")
