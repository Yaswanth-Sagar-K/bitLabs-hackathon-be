package com.talentstream.controller;

import com.talentstream.dto.SubmitProjectRequest;
import com.talentstream.entity.Submission;
import com.talentstream.service.SubmissionService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/hackathons/{hackathonId}")
@CrossOrigin
public class SubmissionController {
    private final SubmissionService service;
    public SubmissionController(SubmissionService service) { this.service = service; }

    
    @PostMapping("/submit")
    public ResponseEntity<?> submit(@PathVariable Long hackathonId, @Valid @RequestBody SubmitProjectRequest r, BindingResult result) {
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
 	        Submission saved = service.submit(hackathonId, r);
 	        return ResponseEntity.status(HttpStatus.CREATED)
 	                .body("submitted resonse successfully with id " + saved.getId());
 	    } catch (IllegalArgumentException e) {
 	        return ResponseEntity.badRequest().body(e.getMessage());
 	    } catch (EntityNotFoundException e) {
 	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
 	    }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error while submitting the response: " + e.getMessage());
}
    }

    @GetMapping
    public List<Submission> list(@PathVariable Long hackathonId) { return service.listByHackathon(hackathonId); }
}
