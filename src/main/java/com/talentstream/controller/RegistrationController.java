package com.talentstream.controller;

import com.talentstream.dto.RegisterRequest;
import com.talentstream.entity.Registration;
import com.talentstream.service.RegistrationService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/hackathons/{hackathonId}/registrations")
@CrossOrigin
public class RegistrationController {
    private final RegistrationService service;
    public RegistrationController(RegistrationService service) { this.service = service; }

    @PostMapping
    public Registration register(@PathVariable Long hackathonId, @Valid @RequestBody RegisterRequest r) {
        return service.register(hackathonId, r.getUserId(), r.getEligibility());
    }

    @GetMapping
    public List<Registration> list(@PathVariable Long hackathonId) { return service.listByHackathon(hackathonId); }
}
