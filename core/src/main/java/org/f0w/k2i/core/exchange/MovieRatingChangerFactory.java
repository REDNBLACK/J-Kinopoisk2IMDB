package org.f0w.k2i.core.exchange;

/**
 * Factory for {@link MovieRatingChanger}
 */
public interface MovieRatingChangerFactory {
    /**
     * Create instance of {@link MovieRatingChanger}.
     *
     * @param authString Auth string
     * @return {@link MovieRatingChanger} instance
     */
    MovieRatingChanger create(final String authString);
}
