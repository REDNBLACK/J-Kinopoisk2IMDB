package org.f0w.k2i.core.model.service;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import org.f0w.k2i.core.model.entity.KinopoiskFile;
import org.f0w.k2i.core.model.repository.KinopoiskFileRepository;

import java.nio.file.Path;

import static org.f0w.k2i.core.util.IOUtils.getFileHashCode;

public class KinopoiskFileServiceImpl implements KinopoiskFileService {
    private final KinopoiskFileRepository kinopoiskFileRepository;

    @Inject
    public KinopoiskFileServiceImpl(KinopoiskFileRepository kinopoiskFileRepository) {
        this.kinopoiskFileRepository = kinopoiskFileRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public KinopoiskFile find(Path filePath) {
        return kinopoiskFileRepository.findByHashCode(getFileHashCode(filePath));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public KinopoiskFile save(Path filePath) {
        return kinopoiskFileRepository.save(new KinopoiskFile(getFileHashCode(filePath)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void delete(Path filePath) {
        KinopoiskFile existingFile = find(filePath);

        if (existingFile == null) {
            return;
        }

        kinopoiskFileRepository.delete(existingFile);
    }
}
