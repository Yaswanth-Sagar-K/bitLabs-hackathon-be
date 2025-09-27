package com.talentstream.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.talentstream.dto.VideoMetadataDto;
import com.talentstream.dto.VideoUploadRequestDTO;
import com.talentstream.entity.VideoLevel;
import com.talentstream.entity.VideoMetadata;
import com.talentstream.repository.VideoMetadataRepository;
import com.talentstream.service.S3Service;
import com.talentstream.service.VideoService;

@RestController
@RequestMapping("/videos")
public class VideoUploadController {

	@Autowired
	private S3Service s3Service;

	@Autowired
	private VideoMetadataRepository repo;

	@Autowired
	private VideoService videoservice;

	@PostMapping("/uploadVideo")
	public ResponseEntity<?> uploadVideo(@Valid VideoUploadRequestDTO request, BindingResult result) {

		if (result.hasErrors()) {
			StringBuilder errors = new StringBuilder();
			result.getFieldErrors().forEach(err -> errors.append(err.getField()).append(" - ")
					.append(err.getDefaultMessage()).append(System.lineSeparator()));
			return ResponseEntity.badRequest().body(errors.toString());
		}
		VideoLevel levelEnum;
		try {
			levelEnum = VideoLevel.valueOf(request.getLevel().toUpperCase());
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(
					"Invalid level '" + request.getLevel() + "'. Allowed values: BEGINNER, INTERMEDIATE, ADVANCED");
		}

		// Validate video and thumbnail manually
		ResponseEntity<?> validationResponse = validations(request.getFile(), request.getThumbnail());
		if (validationResponse != null)
			return validationResponse;

		try {
			String s3VideoUrl = s3Service.uploadVideo(request.getFile());
			String s3ThumbnailUrl = s3Service.uploadThumbnail(request.getThumbnail());

			VideoMetadata video = new VideoMetadata(request.getTitle(), levelEnum, s3VideoUrl, s3ThumbnailUrl);
			repo.save(video);

			Map<String, String> response = new HashMap<>();
			response.put("message", "Video and thumbnail uploaded successfully");
			return ResponseEntity.ok(response);

		} catch (Exception e) {
			return ResponseEntity.status(500).body(Map.of("error", e.getMessage()));
		}
	}

	private ResponseEntity<?> validations(MultipartFile file, MultipartFile thumbnail) {
		List<String> errors = new ArrayList<>();
		// File type validation
		if (!"video/mp4".equalsIgnoreCase(file.getContentType())) {
			errors.add("Only MP4 videos are allowed");
		}
		if (!("image/jpeg".equalsIgnoreCase(thumbnail.getContentType())
				|| "image/png".equalsIgnoreCase(thumbnail.getContentType()))) {
			errors.add("Thumbnail must be JPG or PNG");
		}

		if (!errors.isEmpty()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
		}

		return null;
	}

	@GetMapping("/recommended/{applicantId}")
	public ResponseEntity<?> getRecommended(@PathVariable String applicantId) {
	    try {
	        Long id = Long.parseLong(applicantId); 
	        List<VideoMetadataDto> videos = videoservice.getRecommendedVideos(id);
	        return ResponseEntity.ok(videos);
	    } catch (NumberFormatException e) {
	        return ResponseEntity.badRequest().body("400 - Invalid Applicant Id :"+e.getMessage()); 
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()); 
	    }
	}
	
	 @PutMapping("updateThumbnail/{videoId}")
	    public ResponseEntity<String> uploadThumbnailForExistingVideo(@PathVariable Long videoId,
	            @RequestPart("thumbnail") MultipartFile thumbnail) {
	        if (thumbnail == null || thumbnail.isEmpty()) {
	            return ResponseEntity.badRequest().body("Thumbnail is required.");
	        }

	        try {
	            VideoMetadata video = repo.findById(videoId)
	                    .orElseThrow(() -> new RuntimeException("Video not found with ID: " + videoId));

	            String oldThumbnailUrl = video.getThumbnailUrl();
	            System.out.println("oldThumbnailUrl"+ oldThumbnailUrl);

	            String s3ThumbnailUrl = s3Service.uploadThumbnail(thumbnail);

	            video.setThumbnailUrl(s3ThumbnailUrl);
	            repo.save(video);

	            if (oldThumbnailUrl != null && !oldThumbnailUrl.isEmpty()) {
	            	System.out.println("inside delete ");
	                s3Service.deleteFile(oldThumbnailUrl);
	            }

	            return ResponseEntity.ok("Thumbnail uploaded successfully. Thumbnail URL: " + s3ThumbnailUrl);
	        } catch (Exception e) {
	            return ResponseEntity.status(500).body("Error: " + e.getMessage());
	        }
	    }

}
