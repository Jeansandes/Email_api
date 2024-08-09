package com.sandesDev.apiConsumer.repositories;

import com.sandesDev.apiConsumer.models.EmailModels;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailRepository extends JpaRepository<EmailModels, UUID> {
    Optional<EmailModels> findByUsername(String username);
}
