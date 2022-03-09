package no.nav.dagpenger.data.vedtak.tjenester

import io.mockk.mockk
import io.mockk.verify
import no.nav.dagpenger.data.vedtak.VedtakDataTopic
import no.nav.helse.rapids_rivers.testsupport.TestRapid
import org.intellij.lang.annotations.Language
import org.junit.jupiter.api.Test

internal class ArenaVedtakMottakTest {
    private val vedtakDataTopic = mockk<VedtakDataTopic>(relaxed = true)
    private val rapid = TestRapid().also {
        ArenaVedtakMottak(it, vedtakDataTopic)
    }

    @Test
    fun `Henter vedtak fra rapid, gjør de om til avro, og publiserer de på data produkt topic`() {
        rapid.sendTestMessage(testJSON)

        verify {
            vedtakDataTopic.publiser(any())
        }
    }
}

@Language("JSON")
private const val testJSON = """{
  "@event_name": "vedtak",
  "@kilde": "arena",
  "@opprettet": "2022-03-03T20:40:41.929538",
  "@meldingId": "00f21686-7aef-4b3e-a537-eb05f93ea8fd",
  "vedtakId": 1,
  "sakId": 2,
  "personId": 4795335,
  "rettighet": "Permittering",
  "type": "Ny rettighet",
  "status": "Iverksatt",
  "utfall": "Ja",
  "saknummer": "2019580493",
  "løpenummer": "1",
  "opprettet": "2019-09-27T04:04:26",
  "oppdatert": "2019-09-27T04:04:26"
}
"""
