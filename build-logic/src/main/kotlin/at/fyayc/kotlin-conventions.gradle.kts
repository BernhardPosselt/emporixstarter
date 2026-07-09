package at.fyayc

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.kotlin.dsl.withType

group = "at.fyayc"

plugins {
    jacoco
    id("org.jetbrains.kotlin.jvm")
    id("com.github.ben-manes.versions")
    `java-test-fixtures`
    `jvm-test-suite`
}

jacoco {
    toolVersion = "0.8.14"
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
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

/**
 * This configures each project to have 3 directories:
 * * test/
 * * integrationTest/
 * * migrationTest/
 *
 * All tests of these directories can be run using the tasks with the same names, e.g. ./gradlew integrationTest
 *
 * integrationTest and migrationTest will not be executed during the check phase, meaning they won't
 * be run on ./gradlew build or ./gradlew check. That's because we don't want a test database
 * accessible when running ./gradlew build, but we do want unit tests in test/ to execute
 */
val junitTags = listOf("integration", "migration")
testing {
    suites {
        val test = named<JvmTestSuite>("test").configure {
            useJUnitJupiter()
            targets.all {
                testTask.configure {
                    options {
                        testLogging {
                            showStandardStreams = true
                            exceptionFormat = TestExceptionFormat.FULL
                        }
                    }
                }
            }
        }
        junitTags.forEach { tag ->
            register<JvmTestSuite>("${tag}Test") {
                description = "Runs $tag tests"
                dependencies {
                    implementation(project())
                }
                targets {
                    all {
                        testTask.configure {
                            options {
                                testLogging {
                                    showStandardStreams = true
                                    exceptionFormat = TestExceptionFormat.FULL
                                }
                            }
                        }
                    }
                }
            }
            configurations.named("${tag}TestImplementation") {
                extendsFrom(configurations.testImplementation)
            }
            configurations.named("${tag}TestApi") {
                extendsFrom(configurations.testApi)
            }
            configurations.named("${tag}TestCompileOnly") {
                extendsFrom(configurations.testCompileOnly)
            }
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
