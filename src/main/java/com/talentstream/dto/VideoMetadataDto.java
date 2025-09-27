package com.talentstream.dto;


public class VideoMetadataDto {
    private Long videoId;
    private String s3url;
    private String tags;
    private String title;
    private String thumbnail_url;

    public VideoMetadataDto(Long videoId, String s3url, String tags, String title,String thumbnail_url) {
        this.videoId = videoId;
        this.s3url = s3url;
        this.tags = tags;
        this.title = title;
        this.setThumbnail_url(thumbnail_url);
    }

	public Long getVideoId() {
		return videoId;
	}

	public void setVideoId(Long videoId) {
		this.videoId = videoId;
	}

	public String getS3url() {
		return s3url;
	}

	public void setS3url(String s3url) {
		this.s3url = s3url;
	}

	public String getTags() {
		return tags;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getThumbnail_url() {
		return thumbnail_url;
	}

	public void setThumbnail_url(String thumbnail_url) {
		this.thumbnail_url = thumbnail_url;
	}

	
}
