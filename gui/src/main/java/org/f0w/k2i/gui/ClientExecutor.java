package org.f0w.k2i.gui;

import com.google.common.eventbus.Subscribe;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.f0w.k2i.core.Client;
import org.f0w.k2i.core.event.ImportFinishedEvent;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

class ClientExecutor {
    private final AtomicBoolean running = new AtomicBoolean(false);
    private Client client;
    private ExecutorService executor;

    private Path filePath;
    private Config config;
    private boolean cleanRun = false;
    private final List<Object> listeners = new ArrayList<>(Collections.singletonList(new ExecutionCompleteListener()));

    public void setFilePath(Path filePath) {
        this.filePath = filePath;
    }

    public void setConfig(Config config) {
        this.config = config;
    }

    public void setConfig(Map<String, Object> configMap) {
        setConfig(ConfigFactory.parseMap(configMap));
    }

    public void setCleanRun(boolean cleanRun) {
        this.cleanRun = cleanRun;
    }

    public void setListeners(List<Object> listeners) {
        this.listeners.addAll(listeners);
    }

    private void init() {
        client = new Client(filePath, config, cleanRun, listeners);
    }

    public void run() {
        init();

        if (running.get()) {
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
