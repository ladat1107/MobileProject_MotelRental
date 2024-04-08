package com.motel.mobileproject_motelrental.Item;

public class MotelItem {
    private int motelImage;
    private String title;
    private String address;
    private int likeCount;

    public MotelItem(int motelImage, String title, String address, int likeCount) {
        this.motelImage = motelImage;
        this.title = title;
        this.address = address;
        this.likeCount = likeCount;
    }

    public int getMotelImage() {
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
