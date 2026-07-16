package at.fyayc

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.kotlin.dsl.withType

group = "at.fyayc"

// Workaround for https://github.com/gradle/gradle/issues/15383
val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

plugins {
    jacoco
    id("com.github.ben-manes.versions")
}

jacoco {
    toolVersion = "0.8.14"
}

repositories {
    mavenCentral()
}

// do not recommend unstable versions to upgrade to
tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        val version = candidate.version
        val stableKeyword = listOf("RELEASE", "FINAL", "GA").any { version.uppercase().contains(it) }
        val regex = "^[0-9,.v-]+(-r)?$".toRegex()
        val isStable = stableKeyword || regex.matches(version)
        isStable.not()
    }
}
