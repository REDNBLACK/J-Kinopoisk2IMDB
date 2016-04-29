package org.f0w.k2i.gui;

import com.google.common.eventbus.Subscribe;
import com.typesafe.config.Config;
import org.f0w.k2i.core.Client;
import org.f0w.k2i.core.event.ImportFinishedEvent;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

class ClientExecutor {
    private final AtomicBoolean running = new AtomicBoolean(false);
    private Client client;
    private ExecutorService executor;

    public void init(Path filePath, Config config, boolean cleanRun, List<?> listeners) {
        List<Object> listenersExtended = new ArrayList<>(listeners);
        listenersExtended.add(new ExecutionCompleteListener());

        client = new Client(filePath, config, cleanRun, listenersExtended);
    }

    public void run() {
        if (running.get() || client == null) {
            return;
        }

        running.set(true);
        executor = Executors.newSingleThreadExecutor();
        executor.submit(client);
        executor.shutdown();
    }

    public boolean isRunning() {
        return running.get();
    }

    public void terminate() {
        if (!running.get() || executor == null) {
            return;
        }

        executor.shutdownNow();
        running.set(false);
    }

    private class ExecutionCompleteListener {
        @Subscribe
        public void handleEnd(ImportFinishedEvent event) {
            running.set(false);
        }
    }
}
