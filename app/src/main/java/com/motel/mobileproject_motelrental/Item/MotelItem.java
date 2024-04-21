package com.motel.mobileproject_motelrental.Item;

public class MotelItem {
    private String id;
    private String motelImage;
    private String title;
    private String address;
    private int likeCount;

    public MotelItem(String id, String motelImage, String title, String address, int likeCount) {
        this.id = id;
        this.motelImage = motelImage;
        this.title = title;
        this.address = address;
        this.likeCount = likeCount;
    }

    public String getId() {
        return id;
    }

    public String getMotelImage() {
        return motelImage;
    }

    public String getTitle() {
        return title;
    }

    public String getAddress() {
        return address;
    }

    public int getLikeCount() {
        return likeCount;
    }
}
