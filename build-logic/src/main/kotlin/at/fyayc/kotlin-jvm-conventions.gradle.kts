package at.fyayc

import org.gradle.api.tasks.testing.logging.TestExceptionFormat

group = "at.fyayc"

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("at.fyayc.base-conventions")
    `java-test-fixtures`
    `jvm-test-suite`
}

kotlin {
    jvmToolchain(25)
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
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
        named<JvmTestSuite>("test").configure {
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