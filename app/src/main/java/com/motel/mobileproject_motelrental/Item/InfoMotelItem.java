package com.motel.mobileproject_motelrental.Item;

public class InfoMotelItem {
    private int imageResource;
    private String title;
    private String likeCount;
    private String price;
    private String address;
    private String commentCount;

    public InfoMotelItem(int imageResource, String title, String likeCount, String price, String address, String commentCount) {
        this.imageResource = imageResource;
        this.title = title;
        this.likeCount = likeCount;
        this.price = price;
        this.address = address;
        this.commentCount = commentCount;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getTitle() {
        return title;
    }

    public String getLikeCount() {
        return likeCount;
    }

    public String getPrice() {
        return price;
    }

    public String getAddress() {
        return address;
    }

    public String getCommentCount() {
        return commentCount;
    }
}

