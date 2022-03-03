buildscript { repositories { mavenCentral() } }

plugins {
    id("dagpenger.common")
    id("dagpenger.rapid-and-rivers")
}

dependencies {
    implementation(Konfig.konfig)
    testImplementation(Junit5.api)
    testImplementation(Mockk.mockk)
}

application {
    mainClass.set("no.nav.dagpenger.data.vedtak.AppKt")
}
