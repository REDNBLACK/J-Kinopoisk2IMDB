package org.f0w.k2i.core;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.jpa.JpaPersistModule;
import org.f0w.k2i.core.model.entity.KinopoiskFile;
import org.f0w.k2i.core.model.repository.KinopoiskFileRepository;
import org.f0w.k2i.core.model.repository.KinopoiskFileRepositoryImpl;

public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ServiceProvider(), new JpaPersistModule("K2IDB"));

        KinopoiskFileRepository repository = injector.getInstance(KinopoiskFileRepositoryImpl.class);

        System.out.println(repository.findOrCreate(new KinopoiskFile("dAtAsS")));

        System.out.println(repository.findOrCreate(new KinopoiskFile("tasdasd")));
    }
}
