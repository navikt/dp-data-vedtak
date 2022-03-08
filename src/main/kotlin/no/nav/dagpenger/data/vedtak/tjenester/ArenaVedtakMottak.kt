package no.nav.dagpenger.data.vedtak.tjenester

import mu.KotlinLogging
import no.nav.dagpenger.data.vedtak.repository.InMemoryVedtakRepository
import no.nav.dagpenger.data.vedtak.repository.Vedtak
import no.nav.helse.rapids_rivers.JsonMessage
import no.nav.helse.rapids_rivers.MessageContext
import no.nav.helse.rapids_rivers.RapidsConnection
import no.nav.helse.rapids_rivers.River

private val logg = KotlinLogging.logger {}

internal class ArenaVedtakMottak(
    rapidsConnection: RapidsConnection,
    private val inMemoryVedtakRepository: InMemoryVedtakRepository
) : River.PacketListener, River.PacketValidationSuccessListener {
    init {
        River(rapidsConnection).validate {
            it.demandValue("@event_name", "vedtak")
            it.demandValue("@kilde", "arena")
            it.requireValue("status", "Iverksatt")
            it.requireKey(
                "vedtakId",
                "rettighet",
                "type",
                "utfall",
                "saknummer"
            )
        }.register(this)
    }

    override fun onPacket(packet: JsonMessage, context: MessageContext) {
        inMemoryVedtakRepository.lagre(
            Vedtak(
                vedtakId = packet["vedtakId"].asInt(),
                rettighet = packet["rettighet"].asText(),
                type = packet["type"].asText(),
                status = packet["status"].asText(),
                utfall = packet["utfall"].asText(),
                saknummer = packet["saknummer"].asText(),
            ).also {
                logg.info { "leste inn vedtak: ${it.vedtakId}" }
            }
        )
    }
}
