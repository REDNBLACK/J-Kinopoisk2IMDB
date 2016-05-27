package org.f0w.k2i.core.handler;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.util.HttpUtils;

import java.util.List;

public final class ConnectionCheckHandler extends MovieHandler {
    private static final String URL = "www.imdb.com";
    private static final int PORT = 80;

    private final int timeout;

    @Inject
    public ConnectionCheckHandler(Config config) {
        this.timeout = config.getInt("timeout");
    }

    /**
     * Repeatably sleeps the thread for specific timeout, until connection to IMDB site is available.
     *
     * @param importProgress Entity to handle
     * @param errors         List which fill with errors if occured
     */
    @Override
    protected void handleMovie(ImportProgress importProgress, List<Error> errors) {
        while (!HttpUtils.isReachable(URL, PORT, timeout)) {
            LOG.info("IMDB URL is not reachable, retrying...");

            try {
                Thread.sleep(timeout);
            } catch (InterruptedException ignore) {
                return;
            }
        }

        LOG.info("IMDB URL is successfully reached!");
    }
}
