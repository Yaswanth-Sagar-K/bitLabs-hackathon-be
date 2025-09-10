package com.talentstream.service;

import com.talentstream.dto.ScoreRequest;
import com.talentstream.entity.Score;
import com.talentstream.repository.ScoreRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ScoringService {
    private final ScoreRepository repo;

    public ScoringService(ScoreRepository repo) { this.repo = repo; }

    public Score addScore(Long hackathonId, Long submissionId, ScoreRequest r) {
        Score s = new Score();
        s.setHackathonId(hackathonId);
        s.setSubmissionId(submissionId);
        s.setJudgeId(r.getJudgeId());
        s.setScoreValue(r.getScore());
        s.setComment(r.getComment());
        return repo.save(s);
    }

    public List<Map<String,Object>> leaderboard(Long hackathonId) {
        List<Object[]> rows = repo.leaderboard(hackathonId);
        List<Map<String,Object>> out = new ArrayList<>();
        for (Object[] row : rows) {
            Map<String,Object> m = new HashMap<>();
            m.put("submissionId", ((Number)row[0]).longValue());
            m.put("avgScore", ((Number)row[1]).doubleValue());
            out.add(m);
        }
        return out;
    }
}
