package org.f0w.k2i.core.exchange;

/**
 * Factory for {@link MovieRatingChanger}
 */
public interface MovieRatingChangerFactory {
    /**
     * Create instance of {@link MovieRatingChanger}.
     *
     * @return {@link MovieRatingChanger} instance
     */
    MovieRatingChanger create();
}
