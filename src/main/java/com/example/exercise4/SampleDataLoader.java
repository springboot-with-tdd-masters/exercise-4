package com.example.exercise4;

import com.example.exercise4.repository.RoleRepository;
import com.example.exercise4.repository.UserRepository;
import com.example.exercise4.repository.entity.RoleEntity;
import com.example.exercise4.repository.entity.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class SampleDataLoader implements ApplicationRunner {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  public void run(ApplicationArguments args) {
    roleRepository.save(new RoleEntity(RoleEnum.ROLE_USER));
    roleRepository.save(new RoleEntity(RoleEnum.ROLE_ADMIN));
  }
}
