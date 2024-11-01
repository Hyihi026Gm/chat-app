package com.example.chat_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chat_app.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByLoginId(int loginId);
}