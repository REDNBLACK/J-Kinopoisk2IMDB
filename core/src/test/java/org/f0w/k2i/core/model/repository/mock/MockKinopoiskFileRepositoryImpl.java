package org.f0w.k2i.core.model.repository.mock;

import org.f0w.k2i.core.model.entity.KinopoiskFile;
import org.f0w.k2i.core.model.repository.KinopoiskFileRepository;

public class MockKinopoiskFileRepositoryImpl extends BaseMockRepository<KinopoiskFile>
        implements KinopoiskFileRepository
{
    @Override
    public KinopoiskFile findByHashCode(String hashCode) {
        return storage.values()
                .stream()
                .filter(kp -> kp.getHashCode().equals(hashCode))
                .findFirst()
                .orElse(null);
    }
}
