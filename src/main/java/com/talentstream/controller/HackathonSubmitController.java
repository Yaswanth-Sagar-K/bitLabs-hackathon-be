package com.talentstream.controller;

import com.talentstream.dto.HackathonSubmitRequestDTO;
import com.talentstream.entity.HackathonSubmit;
import com.talentstream.service.HackathonSubmitService;

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
public class HackathonSubmitController {
    private final HackathonSubmitService service;
    public HackathonSubmitController(HackathonSubmitService service) { this.service = service; }

    
    @PostMapping("/submit")
    public ResponseEntity<?> submit(@PathVariable Long hackathonId, @Valid @RequestBody HackathonSubmitRequestDTO r, BindingResult result) {
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
 	        HackathonSubmit saved = service.submit(hackathonId, r);
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
    public List<HackathonSubmit> list(@PathVariable Long hackathonId) { return service.listByHackathon(hackathonId); }
}
