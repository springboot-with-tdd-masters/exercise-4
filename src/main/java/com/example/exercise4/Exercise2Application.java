package com.example.exercise4;

import com.example.exercise4.repository.RoleRepository;
import com.example.exercise4.repository.UserRepository;
import com.example.exercise4.repository.entity.RoleEntity;
import com.example.exercise4.repository.entity.RoleEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class Exercise2Application {

  public static void main(String[] args) {
    SpringApplication.run(Exercise2Application.class, args);
  }
}
