package no.nav.dagpenger.data.vedtak

import com.natpryce.konfig.getValue
import com.natpryce.konfig.stringType
import mu.KotlinLogging
import org.apache.kafka.clients.producer.KafkaProducer
import org.apache.kafka.clients.producer.ProducerRecord


val logg = KotlinLogging.logger {}

internal class VedtakDataTopic(
    private val producer: KafkaProducer<String, Vedtak>
) {
    private val data_produkt_topic by stringType

    fun publiser(vedtak: Vedtak) {
        producer.send(ProducerRecord(config[data_produkt_topic], vedtak))
        logg.info { "Sendte ut vedtak med id: ${vedtak.vedtakId} "}
    }
}