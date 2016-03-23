package org.f0w.k2i.core;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.typesafe.config.Config;
import org.f0w.k2i.core.model.entity.KinopoiskFile;
import org.f0w.k2i.core.model.repository.ImportProgressRepository;
import org.f0w.k2i.core.model.repository.KinopoiskFileRepository;
import org.f0w.k2i.core.providers.ConfigurationProvider;
import org.f0w.k2i.core.providers.JpaRepositoryProvider;

public class Client {
    private final Injector injector;

    public Client(Config config) {
        injector = Guice.createInjector(
                new ConfigurationProvider(config),
                new JpaPersistModule("K2IDB"),
                new JpaRepositoryProvider()
        );
    }

    public void init() {
        injector.getInstance(PersistService.class).start();
    }

    public void run() {
        KinopoiskFileRepository repository = injector.getInstance(KinopoiskFileRepository.class);

        System.out.println(repository.findOrCreate(new KinopoiskFile("dAtAsS")));

        ImportProgressRepository ipRep = injector.getInstance(ImportProgressRepository.class);

        System.out.println(ipRep.findNotImportedByFileId(1).get(0).getMovie());
        System.out.println(ipRep.findNotRatedByFileId(1).get(0).getMovie());
    }
}
