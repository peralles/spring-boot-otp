package com.peralles.authenticator.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.peralles.authenticator.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

}
