plugins {
    kotlin("jvm") version "1.6.10"
    id("com.github.davidmc24.gradle.plugin.avro") version "1.3.0"
    application
}

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

application {
    mainClass.set("no.nav.dagpenger.data.vedtak.AppKt")
}

dependencies {
    implementation("com.natpryce:konfig:1.6.10.0")
    implementation("org.apache.avro:avro:1.11.0")
    implementation("com.github.navikt:rapids-and-rivers:2022.02.28-16.20.1a549dcffaae")
    implementation("io.github.microutils:kotlin-logging-jvm:2.1.21")

    testImplementation(kotlin("test"))
}

kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(JavaVersion.VERSION_17.toString()))
    }
}

tasks.test {
    useJUnitPlatform()
}
