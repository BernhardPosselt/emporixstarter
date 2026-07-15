package at.fyayc

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import gradle.kotlin.dsl.accessors._9c509d200d0c750b9a5d0c45e796dc09.main
import gradle.kotlin.dsl.accessors._9c509d200d0c750b9a5d0c45e796dc09.sourceSets
import org.gradle.kotlin.dsl.withType

group = "at.fyayc"

plugins {
    jacoco
    id("com.github.ben-manes.versions")
    id("dev.detekt")
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

detekt {
    //config.setFrom(resources.text.fromFile("detekt.yml"))
}