package org.example.infrostructure.repositoryies.collections;

import org.example.core.models.Auditable;
import org.example.core.models.Transaction;
import org.example.core.repositories.AuditableRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


/**
 * Реализация интерфейса {@link AuditableRepository},
 * предоставляющая функциональность для работы с
 * наследниками {@link Auditable}
 * в системе.
 */
public class AuditableReppositoryImpl implements AuditableRepository {
    private final List<Auditable> audits = new ArrayList<>();

    @Override
    public void addAuditable(Auditable auditable) {
        audits.add(auditable);
    }

    @Override
    public Auditable findById(long id) {
        Optional<Auditable> audit = audits.stream().filter(a -> a.getId() == id).findFirst();
        return audit.orElse(null);
    }

    @Override
    public List<Auditable> findAllByUserId(long userId) {
        List<Auditable> userAudits = new ArrayList<>();

        for (Auditable audit : audits) {
            if (audit.getUserId() == userId) {
                userAudits.add(audit);
            }
        }

        return userAudits;
    }

    @Override
    public List<Auditable> getAll() {
        return audits;
    }
}
