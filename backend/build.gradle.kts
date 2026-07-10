import at.fyayc.tasks.CreateDevProfile

plugins {
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.boot.dependency)
    alias(libs.plugins.kotlin.serialization)
    id("at.fyayc.kotlin-conventions")
}

version = "0.0.1-SNAPSHOT"

val mockitoAgent = configurations.register("mockitoAgent")
dependencies {
    implementation("at.fyayc:emporix-api-client")
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.webmvc)
    implementation(libs.spring.boot.starter.kotlinx.serialization.json)
    implementation(libs.springdoc)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.coroutines)
    testImplementation(testFixtures("at.fyayc:common"))
    testImplementation(libs.spring.boot.starter.actuator.test)
    testImplementation(libs.spring.boot.starter.security.test)
    testImplementation(libs.spring.boot.starter.webmvc.test)
    testImplementation(libs.spring.boot.starter.restclient.test)
    testImplementation(libs.spring.boot.testcontainers)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.testcontainers.junit.jupiter)
    testRuntimeOnly(libs.junit.platform.launcher)
    mockitoAgent(libs.mockito) { isTransitive = false }
}

tasks.withType<Test>().configureEach {
    jvmArgs.add("-javaagent:${mockitoAgent.get().asPath}")
}

tasks.register<CreateDevProfile>("createDevProfile") {
    description = "Creates an application-dev.yml inside your resources folder"
    file = layout.projectDirectory.file("src/main/resources/application-dev.yml")
}