package com.talentstream.controller;

import com.talentstream.dto.ScoreRequest;
import com.talentstream.entity.Score;
import com.talentstream.service.ScoringService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class HackathonScoreController {
    private final ScoringService service;
    public HackathonScoreController(ScoringService service) { this.service = service; }

    @PostMapping("/submissions/{submissionId}/score")
    public Score score(@PathVariable Long submissionId, @Valid @RequestBody ScoreRequest r, @RequestParam Long hackathonId) {
        return service.addScore(hackathonId, submissionId, r);
    }

    @GetMapping("/hackathons/{hackathonId}/leaderboard")
    public List<Map<String,Object>> leaderboard(@PathVariable Long hackathonId) { return service.leaderboard(hackathonId); }
}
