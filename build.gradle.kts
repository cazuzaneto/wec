import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "3.2.4"
    id("io.spring.dependency-management") version "1.1.4"
    id("org.jlleitschuh.gradle.ktlint") version "10.0.0"
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
val ninja = "4.0.2"

dependencies {
    // Spring Boot & Kotlin
    implementation("org.springframework.boot:spring-boot-starter:$springBootVersion")
    implementation("org.springframework.boot:spring-boot-starter-web:$springBootVersion")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion")

    // Database & Migration
    implementation("org.springframework.boot:spring-boot-starter-jdbc:$springBootVersion")
    implementation("org.flywaydb:flyway-core:$flywayVersion")
    runtimeOnly("org.postgresql:postgresql")
    runtimeOnly("org.flywaydb:flyway-database-postgresql:$flywayVersion")

    // Development tools
    developmentOnly("org.springframework.boot:spring-boot-devtools:$springBootVersion")

    // Testing frameworks
    testImplementation("uk.co.jemos.podam:podam:$podam")
    testImplementation("org.flywaydb:flyway-core:$flywayVersion")
    testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
    testImplementation(platform("org.junit:junit-bom:$junitVersion"))
    testImplementation("io.mockk:mockk:$mockkVersion")
    testImplementation("com.h2database:h2")
    testImplementation("com.ninja-squad:springmockk:$ninja")

    testRuntimeOnly("org.junit.platform:junit-platform-launcher") {
        because("Only needed to run tests in a version of IntelliJ IDEA that bundles older versions")
    }
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testRuntimeOnly("org.junit.vintage:junit-vintage-engine")
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
