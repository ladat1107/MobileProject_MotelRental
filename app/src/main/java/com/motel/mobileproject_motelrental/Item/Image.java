package com.motel.mobileproject_motelrental.Item;

public class Image {
    private String image;
    private String videoUrl;
    private boolean isVideo;

    public Image(String image, String videoUrl, boolean isVideo) {
        this.image = image;
        this.videoUrl = videoUrl;
        this.isVideo = isVideo;
    }

    public String getImage() {
        return image;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public boolean isVideo() {
        return isVideo;
    }
}
