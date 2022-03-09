package no.nav.dagpenger.data.vedtak

import io.confluent.kafka.serializers.KafkaAvroSerializer
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig
import no.nav.dagpenger.data.vedtak.tjenester.ArenaVedtakMottak
import no.nav.helse.rapids_rivers.RapidApplication
import no.nav.helse.rapids_rivers.RapidsConnection
import no.nav.helse.rapids_rivers.RapidsConnection.StatusListener
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import java.util.Properties

internal class ApplicationBuilder(config: Map<String, String>) : StatusListener {
    private val rapidsConnection = RapidApplication.Builder(
        RapidApplication.RapidApplicationConfig.fromEnv(config)
    ).build { _, rapid ->
        // Les fra start hver gang
        rapid.seekToBeginning()
    }

    init {
        rapidsConnection.register(this)
    }

    fun start() = rapidsConnection.start()

    override fun onStartup(rapidsConnection: RapidsConnection) {
        val dataProduktProducer: KafkaProducer<String, Vedtak> =
            createProducer(AivenConfig.default.producerConfig(avroProducerConfig))
        ArenaVedtakMottak(rapidsConnection, VedtakDataTopic(dataProduktProducer))
    }

    private fun <K, V> createProducer(producerConfig: Properties = Properties()) =
        KafkaProducer<K, V>(producerConfig).also {
            Runtime.getRuntime().addShutdownHook(
                Thread {
                    it.close()
                }
            )
        }

    private val avroProducerConfig = Properties().apply {
        val schemaRegistryUser =
            requireNotNull(System.getenv("KAFKA_SCHEMA_REGISTRY_USER")) { "Expected KAFKA_SCHEMA_REGISTRY_USER" }
        val schemaRegistryPassword =
            requireNotNull(System.getenv("KAFKA_SCHEMA_REGISTRY_PASSWORD")) { "Expected KAFKA_SCHEMA_REGISTRY_PASSWORD" }

        put(
            KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG,
            requireNotNull(System.getenv("KAFKA_SCHEMA_REGISTRY")) { "Expected KAFKA_SCHEMA_REGISTRY" }
        )
        put(KafkaAvroSerializerConfig.USER_INFO_CONFIG, "$schemaRegistryUser:$schemaRegistryPassword")
        put(KafkaAvroSerializerConfig.BASIC_AUTH_CREDENTIALS_SOURCE, "USER_INFO")
        put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer::class.java)
        put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer::class.java)
    }
}
