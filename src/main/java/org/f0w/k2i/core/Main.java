package org.f0w.k2i.core;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import org.f0w.k2i.core.model.entity.KinopoiskFile;
import org.f0w.k2i.core.model.repository.ImportProgressRepository;
import org.f0w.k2i.core.model.repository.KinopoiskFileRepository;

public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new ServiceProvider(), new JpaPersistModule("K2IDB"));
        injector.getInstance(PersistService.class).start();

        KinopoiskFileRepository repository = injector.getInstance(KinopoiskFileRepository.class);

        System.out.println(repository.findOrCreate(new KinopoiskFile("dAtAsS")));

        ImportProgressRepository ipRep = injector.getInstance(ImportProgressRepository.class);

        System.out.println(ipRep.findNotImportedByFileId(1).get(0).getMovie());
        System.out.println(ipRep.findNotRatedByFileId(1).get(0).getMovie());
    }
}
