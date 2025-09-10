package com.talentstream.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.talentstream.entity.Hackathon;

public interface HackathonRepository extends JpaRepository<Hackathon, Long> {}
