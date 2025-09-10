package com.talentstream.controller;

import com.talentstream.dto.SubmitProjectRequest;
import com.talentstream.entity.Submission;
import com.talentstream.service.SubmissionService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/hackathons/{hackathonId}/submissions")
@CrossOrigin
public class SubmissionController {
    private final SubmissionService service;
    public SubmissionController(SubmissionService service) { this.service = service; }

    @PostMapping
    public Submission submit(@PathVariable Long hackathonId, @Valid @RequestBody SubmitProjectRequest r) {
        return service.submit(hackathonId, r);
    }

    @GetMapping
    public List<Submission> list(@PathVariable Long hackathonId) { return service.listByHackathon(hackathonId); }
}
