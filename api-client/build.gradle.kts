import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import java.net.URI

repositories {
    mavenCentral()
}

plugins {
    id("maven-publish")
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.versions)
    alias(libs.plugins.npm.publish)
}

group = "at.fyayc"
version = "1.0.0-SNAPSHOT"
description = "Emporix API Client"

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(
            "-opt-in=kotlinx.serialization.ExperimentalSerializationApi",
            "-opt-in=kotlin.js.ExperimentalJsExport",
            "-opt-in=kotlin.time.ExperimentalTime",
        )
    }

    js {
        compilerOptions {
            freeCompilerArgs.add(
                "-Xenable-suspend-function-exporting",
            )
        }
        useEsModules()
        nodejs {
        }
        compilerOptions {
            target = "es2015"
        }
        binaries.library()
        generateTypeScriptDefinitions()
    }

    jvm {
        javaToolchains {
            version = JavaLanguageVersion.of(25)
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.ktor.client.logging)
            implementation(libs.ktor.client.json)
            implementation(libs.ktor.client.negotiation)
            implementation(libs.ktor.client.core)
            implementation(libs.kotlinx.serialization.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.coroutines)
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }

        jsMain.dependencies {
            implementation(libs.kotlin.plain.objects)
            implementation(libs.kotlinx.coroutines.js)
            implementation(libs.ktor.client.js)
            implementation(project.dependencies.enforcedPlatform(libs.kotlin.wrappers))
            implementation(libs.kotlin.wrappers.js)
            implementation(libs.kotlin.wrappers.web)
        }

        jsTest.dependencies {
            implementation(libs.kotlin.test.js)
        }

        jvmMain.dependencies {
            implementation(libs.ktor.client.java)
        }

        jvmTest.dependencies {
            implementation(libs.junit.kotlin)
        }
    }
}

// do not show beta and milestone versions as upgrades
tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        val version = candidate.version
        val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
        val regex = "^[0-9,.v-]+(-r)?$".toRegex()
        val isStable = stableKeyword || regex.matches(version)
        isStable.not()
    }
}

npmPublish {
    registries {
        register("nexus") {
            uri.set("http://localhost:8000/repository/npm")
            username.set("nexus")
            password.set("nexus")
        }
    }
}

publishing {
    repositories {
        maven {
            url = URI("http://localhost:8000/repository/maven-releases")
            isAllowInsecureProtocol = true
            credentials {
                username = "nexus"
                password = "nexus"
            }
        }
    }
}