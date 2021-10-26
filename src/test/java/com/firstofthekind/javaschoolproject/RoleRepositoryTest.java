package com.firstofthekind.javaschoolproject;


import com.firstofthekind.javaschoolproject.entity.ERole;
import com.firstofthekind.javaschoolproject.entity.RoleEntity;
import com.firstofthekind.javaschoolproject.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.util.Assert;

import java.util.LinkedList;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class RoleRepositoryTest {
    @Autowired
    RoleRepository roleRepository;

    @Test
    public void createRoles(){
        RoleEntity user = new RoleEntity(ERole.ROLE_USER);
        RoleEntity admin = new RoleEntity(ERole.ROLE_ADMIN);

        roleRepository.saveAll(List.of(user, admin));
        List<RoleEntity> roleEntityList = roleRepository.findAll();

        Assert.notEmpty(roleEntityList);

    }
}
