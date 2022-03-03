package no.nav.dagpenger.data.vedtak.tjenester

import no.nav.dagpenger.data.vedtak.repository.InMemoryVedtakRepository
import no.nav.dagpenger.data.vedtak.repository.Vedtak
import no.nav.helse.rapids_rivers.JsonMessage
import no.nav.helse.rapids_rivers.MessageContext
import no.nav.helse.rapids_rivers.RapidsConnection
import no.nav.helse.rapids_rivers.River

internal class ArenaVedtakMottak(
    rapidsConnection: RapidsConnection,
    private val inMemoryVedtakRepository: InMemoryVedtakRepository
) : River.PacketListener {
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
            )
        )
    }
}
