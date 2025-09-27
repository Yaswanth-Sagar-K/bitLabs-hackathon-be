package com.talentstream.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.talentstream.dto.VideoMetadataDto;
import com.talentstream.entity.Applicant;
import com.talentstream.exception.CustomException;
import com.talentstream.repository.RegisterRepository;
import com.talentstream.repository.VideoMetadataRepository;

@Service
public class VideoService {

	@Autowired
	private VideoMetadataRepository videoMetadataRepository;
	
	@Autowired
    private RegisterRepository applicantRepository;

	public List<VideoMetadataDto> getRecommendedVideos(Long applicantId) {
        Applicant applicant = applicantRepository.getApplicantById(applicantId);
        if (applicant == null) {
            throw new CustomException("Applicant not found for ID: " + applicantId, HttpStatus.NOT_FOUND);
	    }


		List<Object[]> rawData = videoMetadataRepository.fetchRecommendedVideos(applicantId);

		return rawData.stream().map(obj -> new VideoMetadataDto(obj[0] != null ? Long.valueOf(obj[0].toString()) : null, // video_id
				obj[1] != null ? obj[1].toString() : "", // s3_url
				obj[2] != null ? obj[2].toString() : "", // tags
				obj[3] != null ? obj[3].toString() : "", // title
				obj[4] != null ? obj[4].toString() : "" // thumbnail_url
		)).toList();
	}

}
