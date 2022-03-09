package no.nav.dagpenger.data.vedtak.tjenester

import com.fasterxml.jackson.databind.JsonNode
import mu.KotlinLogging
import no.nav.dagpenger.data.vedtak.Vedtak
import no.nav.dagpenger.data.vedtak.VedtakDataTopic
import no.nav.helse.rapids_rivers.JsonMessage
import no.nav.helse.rapids_rivers.MessageContext
import no.nav.helse.rapids_rivers.RapidsConnection
import no.nav.helse.rapids_rivers.River
import no.nav.helse.rapids_rivers.asLocalDate
import no.nav.helse.rapids_rivers.asLocalDateTime

private val logg = KotlinLogging.logger {}

internal class ArenaVedtakMottak(
    rapidsConnection: RapidsConnection,
    private val vedtakDataTopic: VedtakDataTopic
) : River.PacketListener, River.PacketValidationSuccessListener {
    init {
        River(rapidsConnection).validate {
            it.demandValue("@event_name", "vedtak")
            it.demandValue("@kilde", "arena")
            it.requireKey(
                "vedtakId",
                "sakId",
                "personId",
                "rettighet",
                "type",
                "status",
                "utfall",
                "saknummer",
                "løpenummer"
            )
            it.require("oppdatert", JsonNode::asLocalDateTime)
            it.require("opprettet", JsonNode::asLocalDateTime)
        }.register(this)
    }

    override fun onPacket(packet: JsonMessage, context: MessageContext) {
        Vedtak.newBuilder().apply {
            vedtakId = packet["vedtakId"].asInt()
            sakId = packet["sakId"].asInt()
            personId = packet["personId"].asInt()
            rettighet = packet["rettighet"].asText()
            type = packet["type"].asText()
            status = packet["status"].asText()
            utfall = packet["utfall"].asText()
            saknummer = packet["saknummer"].asText()
            lopenummer = packet["løpenummer"].asInt()
            opprettet = packet["opprettet"].asLocalDateTime()
            oppdatert = packet["oppdatert"].asLocalDateTime()
        }.build().also {
            vedtakDataTopic.publiser(it)
        }
    }
}
