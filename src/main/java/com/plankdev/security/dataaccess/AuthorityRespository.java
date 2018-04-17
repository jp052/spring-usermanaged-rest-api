package com.plankdev.security.dataaccess;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRespository extends JpaRepository<Authority, Long> {
    String ROLE_USER_DB_NAME = "ROLE_USER";
    String ROLE_USER_APP_NAME = "USER";

    Optional<Authority> findByName(String name);

}
