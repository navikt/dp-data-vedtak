package no.nav.dagpenger.data.vedtak.repository

interface VedtakRepository {
    fun lagre(vedtak: Vedtak)
    fun size(): Int
}

data class Vedtak(
    val vedtakId: Int,
    val rettighet: String,
    val type: String,
    val status: String,
    val utfall: String,
    val saknummer: String
)
