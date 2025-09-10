package com.talentstream.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.talentstream.entity.Score;

import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Long> {
    List<Score> findByHackathonId(Long hackathonId);
    List<Score> findBySubmissionId(Long submissionId);

    @Query("select s.submissionId as submissionId, avg(s.scoreValue) as avgScore " +
           "from Score s where s.hackathonId = :hackathonId group by s.submissionId order by avg(s.scoreValue) desc")
    List<Object[]> leaderboard(Long hackathonId);
}
