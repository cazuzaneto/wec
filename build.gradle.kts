import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.23"
    kotlin("plugin.spring") version "1.9.23"
}

group = "tech.jaya"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
}

// Organize dependencies into groups
val springBootVersion = "3.2.4"
val kotlinVersion = "1.9.23"
val flywayVersion = "10.10.0"
val junitVersion = "5.10.2"
val mockkVersion = "1.13.10"
val podam = "8.0.1.RELEASE"

dependencies {
    // Spring Boot & Kotlin
    implementation("org.springframework.boot:spring-boot-starter:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")

    // Database & Migration
    implementation("org.springframework.boot:spring-boot-starter-jdbc:$springBootVersion")
    runtimeOnly("org.postgresql:postgresql")
    implementation("org.flywaydb:flyway-core:$flywayVersion")

    // Development tools
    developmentOnly("org.springframework.boot:spring-boot-devtools:$springBootVersion")

    // Testing frameworks
    testImplementation("uk.co.jemos.podam:podam:$podam")
    testImplementation("org.flywaydb:flyway-core:$flywayVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testRuntimeOnly("org.junit.platform:junit-platform-launcher") {
        because("Only needed to run tests in a version of IntelliJ IDEA that bundles older versions")
    }
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine")
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("com.h2database:h2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}