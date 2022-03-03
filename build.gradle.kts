plugins {
    kotlin("jvm") version "1.6.10"
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
    implementation("com.github.navikt:rapids-and-rivers:2022.02.28-16.20.1a549dcffaae")

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
