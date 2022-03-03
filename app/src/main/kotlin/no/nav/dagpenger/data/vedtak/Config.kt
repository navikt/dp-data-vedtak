package no.nav.dagpenger.data.vedtak

import com.natpryce.konfig.Configuration
import com.natpryce.konfig.ConfigurationMap
import com.natpryce.konfig.EnvironmentVariables
import com.natpryce.konfig.overriding

private val defaultProperties = ConfigurationMap(
    "HTTP_PORT" to "8080",
    "RAPID_APP_NAME" to "dp-data-vedtak",
    "KAFKA_RAPID_TOPIC" to "teamdagpenger.rapid.v1",
    "KAFKA_RESET_POLICY" to "earliest",
)

internal val config: Configuration by lazy {
    EnvironmentVariables overriding defaultProperties
}

internal fun Configuration.asMap(): Map<String, String> = list().reversed().fold(emptyMap()) { map, pair -> map + pair.second }
