package org.f0w.k2i.core.parser;

abstract class AbstractJSONMovieParser<T> extends AbstractMovieParser<T> {
    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isDataValid(final String data) {
        return super.isDataValid(data) && !"".equals(data);
    }
}
