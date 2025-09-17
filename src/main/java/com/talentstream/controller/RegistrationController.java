package com.talentstream.controller;

import com.talentstream.dto.RegisterRequest;
import com.talentstream.entity.Registration;
import com.talentstream.repository.ApplicantRepository;
import com.talentstream.repository.HackathonRepository;
import com.talentstream.service.RegistrationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/hackathons")
@CrossOrigin
public class RegistrationController {
    private final RegistrationService service;
    public RegistrationController(RegistrationService service) { this.service = service; }
    
    @Autowired
    public ApplicantRepository appRepo;
    
    @Autowired
    public HackathonRepository hackRepo;

    @PostMapping("/{hackathonId}/register")
    public ResponseEntity<?> register(@PathVariable Long hackathonId, 
                                      @Valid @RequestBody RegisterRequest r) {
    	boolean exists = appRepo.existsById(r.getUserId());
    	if(!exists) {
    		 return ResponseEntity.status(HttpStatus.NOT_FOUND)
                     .body("Applicant not found with id: " + r.getUserId());
    	}
    	if(!hackRepo.existsById(hackathonId)) {
       	 return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Hackathon not found with id: " + hackathonId);
       }
        try {
            Optional<Registration> existing = service.findByHackathonAndUser(hackathonId, r.getUserId());

            if (existing.isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                                     .body("User already registered for hackathon with id: " + hackathonId);
            }

            Registration saved = service.register(hackathonId, r.getUserId());
            return ResponseEntity.ok("User registered successfully with id: " + saved.getId());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error while registering user: " + e.getMessage());
        }
    }


    @GetMapping("/{hackathonId}/getAllRegistrations")
    public ResponseEntity<?> list(@PathVariable Long hackathonId) {
        try {
            if (!hackRepo.existsById(hackathonId)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                     .body("Hackathon not found with id: " + hackathonId);
            }

            List<Registration> registrations = service.listByHackathon(hackathonId);

            if (registrations.isEmpty()) {
                return ResponseEntity.status(HttpStatus.OK)
                                     .body("No registrations present for hackathon with id: " + hackathonId);
            }

            return ResponseEntity.ok(registrations);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error while fetching registrations: " + e.getMessage());
        }
    }

}
