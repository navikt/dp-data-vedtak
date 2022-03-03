package no.nav.dagpenger.data.vedtak.repository

internal class InMemoryVedtakRepository private constructor(
    private val liste: HashMap<Int, Vedtak>
) : VedtakRepository {
    constructor() : this(hashMapOf())

    override fun lagre(vedtak: Vedtak) {
        liste[vedtak.vedtakId] = vedtak
    }

    override fun size() = liste.size
}
