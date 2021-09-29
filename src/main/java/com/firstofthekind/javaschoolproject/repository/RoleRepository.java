package com.firstofthekind.javaschoolproject.repository;


import com.firstofthekind.javaschoolproject.entity.ERole;
import com.firstofthekind.javaschoolproject.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(ERole name);
    boolean existsByName(ERole name);
}
