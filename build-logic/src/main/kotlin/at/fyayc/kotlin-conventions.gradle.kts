package at.fyayc

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.kotlin.dsl.withType

group = "at.fyayc"

plugins {
    jacoco
    id("org.jetbrains.kotlin.jvm")
    id("com.github.ben-manes.versions")
}

jacoco {
    toolVersion = "0.8.14"
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

repositories {
    mavenCentral()
}

// all of these tags can be used in tests using one of these, e.g. @Tag("integration")
val junitTags  = listOf("unit", "integegration", "migration")

tasks.withType<Test>().named("test").configure {
    useJUnitPlatform {
        includeTags(*junitTags.toTypedArray())
    }
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
    }
}

junitTags.forEach { tag ->
    tasks.register<Test>("${tag}Test") {
        description = "Runs $tag tests"
        useJUnitPlatform {
            includeTags(tag)
        }
        testLogging {
            exceptionFormat = TestExceptionFormat.FULL
        }
    }
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
