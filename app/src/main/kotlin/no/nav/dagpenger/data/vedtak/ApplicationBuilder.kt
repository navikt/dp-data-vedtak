package no.nav.dagpenger.data.vedtak

import no.nav.dagpenger.data.vedtak.repository.InMemoryVedtakRepository
import no.nav.dagpenger.data.vedtak.tjenester.ArenaVedtakMottak
import no.nav.helse.rapids_rivers.RapidApplication
import no.nav.helse.rapids_rivers.RapidsConnection
import no.nav.helse.rapids_rivers.RapidsConnection.StatusListener

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
        ArenaVedtakMottak(rapidsConnection, InMemoryVedtakRepository())
    }
}
