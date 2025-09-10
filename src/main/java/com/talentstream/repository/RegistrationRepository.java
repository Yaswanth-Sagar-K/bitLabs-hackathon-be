package com.talentstream.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talentstream.entity.Registration;

import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    List<Registration> findByHackathonId(Long hackathonId);
    Optional<Registration> findByHackathonIdAndUserId(Long hackathonId, Long userId);
}
