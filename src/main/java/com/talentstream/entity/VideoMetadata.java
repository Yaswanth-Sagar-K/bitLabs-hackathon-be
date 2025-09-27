package com.talentstream.entity;

import javax.persistence.*;

@Entity
@Table(name = "video_metadata")
public class VideoMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "video_id")
    private Long videoId;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING) // Important: store enum as STRING
    @Column(nullable = false)
    private VideoLevel level;

    @Column(name = "s3url", nullable = false)
    private String s3Url;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    // Constructors
    public VideoMetadata() {
    }

    public VideoMetadata(String title, VideoLevel level, String s3Url, String thumbnailUrl) {
        this.title = title;
        this.level = level;
        this.s3Url = s3Url;
        this.thumbnailUrl = thumbnailUrl;
    }

    public VideoMetadata(String title, VideoLevel level, String s3Url) {
        this.title = title;
        this.level = level;
        this.s3Url = s3Url;
    }

    // Getters and Setters
    public Long getVideoId() {
        return videoId;
    }

    public void setVideoId(Long videoId) {
        this.videoId = videoId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public VideoLevel getLevel() {
        return level;
    }

    public void setLevel(VideoLevel level) {
        this.level = level;
    }

    public String getS3Url() {
        return s3Url;
    }

    public void setS3Url(String s3Url) {
        this.s3Url = s3Url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
