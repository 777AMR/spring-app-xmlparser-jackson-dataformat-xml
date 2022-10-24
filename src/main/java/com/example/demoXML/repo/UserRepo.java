package com.example.demoXML.repo;

import com.example.demoXML.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {

}