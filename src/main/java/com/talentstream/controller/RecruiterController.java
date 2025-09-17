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

import javax.persistence.EntityNotFoundException;
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

	@PostMapping("/create")
	public ResponseEntity<?> createHackathon(@Valid @RequestBody CreateHackathonRequest r, BindingResult result) {
	    if (result.hasErrors()) {
	        StringBuilder errors = new StringBuilder();
	        result.getFieldErrors().forEach(err -> {
	            errors.append(err.getField())
	                  .append(" - ")
	                  .append(err.getDefaultMessage())
	                  .append(System.lineSeparator());
	        });
	        return ResponseEntity.badRequest().body(errors.toString());
	    }

	    try {
	        Hackathon saved = service.createHackathon(r);
	        return ResponseEntity.status(HttpStatus.CREATED)
	                .body("Hackathon created successfully with id " + saved.getId());
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.badRequest().body(e.getMessage());
	    } catch (EntityNotFoundException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	    }
	}

	
	@GetMapping("get/{recruiterId}")
	public ResponseEntity<?> getAll(@PathVariable Long recruiterId) {
	    boolean recruiterExists = recruiterRepo.existsById(recruiterId); 
	    if (!recruiterExists) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                .body("No recruiter present with the id: " + recruiterId);
	    }

	    List<Hackathon> hackathons = service.getAllByCreaterId(recruiterId);
	    if (hackathons.isEmpty()) {
	        return ResponseEntity.ok("Recruiter with id " + recruiterId + " not created any hackathons");
	    }

	    return ResponseEntity.ok(hackathons);
	}


	@PutMapping("update/{hackathonId}")
	public ResponseEntity<?> updateHackathon(@PathVariable Long hackathonId,
	                                         @Valid @RequestBody CreateHackathonRequest r) {
	    try {
	        Hackathon updated = service.updateHackathon(hackathonId, r);
	        return ResponseEntity.ok(updated);
	    } catch (EntityNotFoundException e) {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
	    } catch (IllegalArgumentException e) {
	        return ResponseEntity.badRequest().body(e.getMessage());
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("Error while updating hackathon: " + e.getMessage());
	    }
	}



	@DeleteMapping("delete/{hackathonId}")
	public ResponseEntity<?> delete(@PathVariable Long hackathonId) {
	    try {
	        boolean deleted = service.delete(hackathonId);

	        if (deleted) {
	            return ResponseEntity.ok("Hackathon deleted successfully with id: " + hackathonId);
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                                 .body("No hackathon found with id: " + hackathonId);
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("Error while deleting hackathon: " + e.getMessage());
	    }
	}

}
