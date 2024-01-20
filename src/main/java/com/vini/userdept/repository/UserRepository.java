package com.vini.userdept.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vini.userdept.model.UserModel;

public interface UserRepository extends JpaRepository<UserModel, Long> {

    UserModel findByEmail(String email);
}
