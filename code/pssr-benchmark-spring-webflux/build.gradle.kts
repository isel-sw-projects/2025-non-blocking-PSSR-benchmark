plugins {
    kotlin("jvm")
    id("org.springframework.boot") version "3.4.4"
    id("io.spring.dependency-management") version "1.1.7"
    id("me.champeau.jmh") version "0.7.3"
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

    implementation("org.springframework.boot:spring-boot-starter-webflux")

    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-jersey")

    implementation("org.openjdk.jmh:jmh-core:1.35")
    implementation("org.openjdk.jmh:jmh-core-benchmarks:1.35")
    implementation("org.openjdk.jmh:jmh-generator-annprocess:1.35")
    implementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation(kotlin("test"))
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")

    testImplementation(project(":pssr-benchmark-repository-mem"))
}

configurations.all {
    exclude(group = "org.springframework.boot", module = "spring-boot-starter-tomcat")
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(21)
}
