import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("io.quarkus") version "3.21.0"
    id("org.kordamp.gradle.jandex") version "1.0.0"
    kotlin("jvm")
}

group = "pt.isel.pfc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":pssr-benchmark-controller"))
    implementation(project(":pssr-benchmark-view"))
    implementation(project(":pssr-benchmark-repository"))
    implementation(project(":pssr-benchmark-repository-mem"))

    implementation(enforcedPlatform("io.quarkus.platform:quarkus-bom:3.21.0"))
    implementation("io.quarkus:quarkus-resteasy")
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-kotlin")

    implementation(kotlin("stdlib-jdk8"))
}

tasks.named("quarkusDependenciesBuild") {
    dependsOn(tasks.named("jandex"))
}

kotlin {
    jvmToolchain(21)
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
    }
}
