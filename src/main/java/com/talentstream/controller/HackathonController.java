package com.talentstream.controller;

import com.talentstream.entity.Hackathon;
import com.talentstream.service.HackathonService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/hackathons")
@CrossOrigin
public class HackathonController {
	private final HackathonService service;

	public HackathonController(HackathonService service) {
		this.service = service;
	}

	@GetMapping("/getAll")
	public ResponseEntity<?> list() {
	    List<Hackathon> hackathons = service.getAll();

	    if (hackathons.isEmpty()) {
	        return ResponseEntity.ok("There are no hackathons to show");
	    }

	    return ResponseEntity.ok(hackathons);
	}


	@GetMapping("/{hackathonId}")
	public ResponseEntity<?> get(@PathVariable Long hackathonId) {
	    try {
	        Optional<Hackathon> hackathonOpt = service.get(hackathonId);

	        if (hackathonOpt.isPresent()) {
	            return ResponseEntity.ok(hackathonOpt.get());
	        } else {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                                 .body("No hackathon found with id: " + hackathonId);
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                             .body("Error while fetching hackathon: " + e.getMessage());
	    }
	}


}
