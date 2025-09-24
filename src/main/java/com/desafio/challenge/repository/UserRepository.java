package com.desafio.challenge.repository;


import com.desafio.challenge.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByLogin(String login);
    Optional<User> findByDocumento(String documento);
    boolean existsByLogin(String login);
    boolean existsByDocumento(String documento);
}
