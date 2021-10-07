package com.firstofthekind.javaschoolproject.repository;

import com.firstofthekind.javaschoolproject.entity.SupplementEntity;
import com.firstofthekind.javaschoolproject.entity.TariffEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SupplementRepository extends JpaRepository<SupplementEntity, Long> {
        List<SupplementEntity> findAll();
}
