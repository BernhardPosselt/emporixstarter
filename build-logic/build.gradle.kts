import org.gradle.kotlin.dsl.`kotlin-dsl`

plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation(libs.plugins.versions.toDependency())
    implementation(libs.plugins.kotlin.jvm.toDependency())
    implementation(libs.plugins.detekt.toDependency())
}

kotlin {
    jvmToolchain(25)
}

// see https://github.com/gradle/gradle/issues/17963#issuecomment-939207895
private fun Provider<PluginDependency>.toDependency(): String {
    val t = get()
    val id = t.pluginId
    val version = t.version
    return "$id:$id.gradle.plugin:$version"
}