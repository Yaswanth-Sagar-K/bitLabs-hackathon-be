package com.talentstream.controller;

import com.talentstream.dto.CreateHackathonRequest;
import com.talentstream.entity.Hackathon;
import com.talentstream.repository.JobRecruiterRepository;
import com.talentstream.service.HackathonService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/recruiter/hackathons")
@CrossOrigin
public class RecruiterController {
	private final HackathonService service;
	
	@Autowired
	private JobRecruiterRepository recruiterRepo;

	public RecruiterController(HackathonService service) {
		this.service = service;
	}

	@PostMapping
	public ResponseEntity<?> createHackathon(@Valid @RequestBody CreateHackathonRequest r, BindingResult result) {
		if (result.hasErrors()) {
	        StringBuilder errors = new StringBuilder();
	        result.getFieldErrors().forEach(err -> {
	            errors.append(err.getField())
	                  .append(" should not be null; ")
	                  .append(System.lineSeparator());
	        });
	        return ResponseEntity.badRequest().body(errors.toString());
	    }
		
		if(!recruiterRepo.existsById(r.getCreatorId())) {
	       	 return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body("Recruiter not found with id: " + r.getCreatorId());
	       }

	    Hackathon h = new Hackathon();
	    h.setCreatorId(r.getCreatorId());
	    h.setTitle(r.getTitle());
	    h.setDescription(r.getDescription());
	    h.setBannerUrl(r.getBannerUrl());
	    h.setStartAt(r.getStartAt());
	    h.setEndAt(r.getEndAt());
	    h.setInstructions(r.getInstructions());
	    h.setEligibility(r.getEligibility());
	    h.setAllowedTechnologies(r.getAllowedTechnologies());
	    h.setStatus(r.getStatus());

	    Hackathon saved = service.create(h);
	    return ResponseEntity.ok("Hackathon created successfully with id " + saved.getId());

	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> getAll(@PathVariable Long id) {
	    boolean recruiterExists = recruiterRepo.existsById(id); 
	    if (!recruiterExists) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("No recruiter present with the id: " + id);
	    }

	    List<Hackathon> hackathons = service.getAllByCreaterId(id);
	    if (hackathons.isEmpty()) {
	        return ResponseEntity.ok("Recruiter with id " + id + " not created any hackathons");
	    }

	    return ResponseEntity.ok(hackathons);
	}


	@PutMapping("/{id}")
	public ResponseEntity<?> update(@PathVariable Long id, @RequestBody CreateHackathonRequest r) {
	    try {
	        Hackathon updated = service.update(id, r);

	        if (updated == null) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                                 .body("No hackathon found with id: " + id);
	        }

	        return ResponseEntity.ok(updated);
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("Error while updating hackathon: " + e.getMessage());
	    }
	}


	@DeleteMapping("/{id}")
	public ResponseEntity<?> delete(@PathVariable Long id) {
	    try {
	        boolean deleted = service.delete(id);

	        if (deleted) {
	            return ResponseEntity.ok("Hackathon deleted successfully with id: " + id);
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                                 .body("No hackathon found with id: " + id);
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("Error while deleting hackathon: " + e.getMessage());
	    }
	}

}
