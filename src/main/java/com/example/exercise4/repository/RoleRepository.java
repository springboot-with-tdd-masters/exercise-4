package com.example.exercise4.repository;

import com.example.exercise4.repository.entity.RoleEntity;
import com.example.exercise4.repository.entity.RoleEnum;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
  Optional<RoleEntity> findByName(RoleEnum name);
}
