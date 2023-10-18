package org.example.infrostructure.repositoryies.collections;

import org.example.core.models.Action;
import org.example.core.models.Auditable;
import org.example.core.models.enums.ActionType;
import org.example.core.models.enums.AuditableStatus;
import org.example.core.repositories.AuditableRepository;
import org.example.infrostructure.repositoryies.collections.AuditableReppositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

class AuditableReppositoryImplTest {
    private AuditableRepository auditableRepository;

    @BeforeEach
    void setUd() {
        auditableRepository = new AuditableReppositoryImpl();
    }

    @Test
    void addAuditableTest() {
        Auditable action = new Action(1, 2, LocalDateTime.now(), AuditableStatus.SUCCESS, ActionType.LOGIN);
        auditableRepository.addAuditable(action);
        List<Auditable> list = auditableRepository.getAll();
        assertThat(list).contains(action);
        assertThat(list).isNotEmpty();
    }


    @Test
    void addAuditableAndFindByIdTest() {
        long id = 1;
        Auditable action = new Action(id, 2, LocalDateTime.now(), AuditableStatus.SUCCESS, ActionType.LOGIN);
        Auditable action1 = new Action(2, id, LocalDateTime.now(), AuditableStatus.SUCCESS, ActionType.LOGIN);
        auditableRepository.addAuditable(action);
        auditableRepository.addAuditable(action1);
        Auditable foundAction = auditableRepository.findById(id);
        assertThat(foundAction).isEqualTo(action);
    }

    @Test
    void findByIdNullTest() {
        long id = 1;
        Auditable action = new Action(id, 2, LocalDateTime.now(), AuditableStatus.SUCCESS, ActionType.LOGIN);
        auditableRepository.addAuditable(action);
        Auditable foundAction = auditableRepository.findById(2);
        assertThat(foundAction).isNull();
    }

    @Test
    void findAllByUserIdTest() {
        long userId = 1;
        Auditable action = new Action(2, userId, LocalDateTime.now(), AuditableStatus.SUCCESS, ActionType.LOGIN);
        Auditable action1 = new Action(3, userId, LocalDateTime.now(), AuditableStatus.SUCCESS, ActionType.LOGIN);
        Auditable action2 = new Action(4, 2, LocalDateTime.now(), AuditableStatus.SUCCESS, ActionType.LOGIN);
        auditableRepository.addAuditable(action);
        auditableRepository.addAuditable(action1);
        auditableRepository.addAuditable(action2);
        List<Auditable> found = auditableRepository.findAllByUserId(userId);
        List<Auditable> expected = new ArrayList<>();
        expected.add(action);
        expected.add(action1);
        assertThat(found).containsExactlyInAnyOrderElementsOf(expected);
    }

    @Test
    void findAllByUserIdEmptyTest() {
        long userId = 1;
        Auditable action = new Action(2, userId, LocalDateTime.now(), AuditableStatus.SUCCESS, ActionType.LOGIN);
        Auditable action1 = new Action(3, userId, LocalDateTime.now(), AuditableStatus.SUCCESS, ActionType.LOGIN);
        Auditable action2 = new Action(4, 2, LocalDateTime.now(), AuditableStatus.SUCCESS, ActionType.LOGIN);
        auditableRepository.addAuditable(action);
        auditableRepository.addAuditable(action1);
        auditableRepository.addAuditable(action2);
        List<Auditable> found = auditableRepository.findAllByUserId(5);
        assertThat(found).isEmpty();
    }
}