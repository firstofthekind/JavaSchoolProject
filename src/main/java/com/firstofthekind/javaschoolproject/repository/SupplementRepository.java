package com.firstofthekind.javaschoolproject.repository;

import com.firstofthekind.javaschoolproject.entity.SupplementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.LinkedList;

@Repository
public interface SupplementRepository extends JpaRepository<SupplementEntity, Long> {
    LinkedList<SupplementEntity> findAll();

    @Override
    SupplementEntity getById(Long id);
}
