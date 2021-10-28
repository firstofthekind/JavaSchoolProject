package com.firstofthekind.javaschoolproject.service;

import com.firstofthekind.javaschoolproject.entity.ERole;
import com.firstofthekind.javaschoolproject.entity.RoleEntity;
import com.firstofthekind.javaschoolproject.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util. LinkedList;
import java.util.List;
import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    @Transactional
    public void create() {
        List<RoleEntity> roleEntitySet = new  LinkedList<>();
        for (ERole role : ERole.values()) {
            if (!roleRepository.existsByName(role)) {
                roleEntitySet.add(new RoleEntity(role));
            }
        }
        String roleList = new RoleEntity().getShortNames(roleEntitySet);
        roleRepository.saveAll(roleEntitySet);
        log.info("Roles " + roleList +" was added to DB");
    }
}
