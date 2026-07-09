import at.fyayc.tasks.MyTask

//buildscript {
//	dependencies {
//		classpath(":build-logic")
//	}
//}

plugins {
	alias(libs.plugins.kotlin.spring)
	alias(libs.plugins.spring.boot)
	alias(libs.plugins.spring.boot.dependency)
	id("at.fyayc.kotlin-conventions")
}

tasks.register<MyTask>("tdf") {

}

version = "0.0.1-SNAPSHOT"

val mockitoAgent = configurations.create("mockitoAgent")
dependencies {
	implementation("at.fyayc:emporix-api-client:1.0.0-SNAPSHOT")
	implementation(libs.spring.boot.starter.actuator)
	implementation(libs.spring.boot.starter.security)
	implementation(libs.spring.boot.starter.webmvc)
	implementation(libs.kotlin.reflect)
	implementation(libs.jackson.module.kotlin)
	testImplementation(libs.spring.boot.starter.actuator.test)
	testImplementation(libs.spring.boot.starter.security.test)
	testImplementation(libs.spring.boot.starter.webmvc.test)
	testImplementation(libs.spring.boot.testcontainers)
	testImplementation(libs.kotlin.test.junit)
	testImplementation(libs.testcontainers.junit.jupiter)
	testRuntimeOnly(libs.junit.platform.launcher)
	mockitoAgent(libs.mockito) { isTransitive = false }
}

tasks.withType<Test> {
	jvmArgs.add("-javaagent:${mockitoAgent.asPath}")
}





