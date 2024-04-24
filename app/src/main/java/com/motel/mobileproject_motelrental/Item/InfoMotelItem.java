package com.motel.mobileproject_motelrental.Item;

public class InfoMotelItem {
    private String id;
    private String imageResource;
    private String title;
    private int likeCount;
    private double price;
    private String address;
    private int commentCount;
    private  boolean status;

    public InfoMotelItem(String id, String imageResource, String title, int likeCount, double price, String address, int commentCount) {
        this.id = id;
        this.imageResource = imageResource;
        this.title = title;
        this.likeCount = likeCount;
        this.price = price;
        this.address = address;
        this.commentCount = commentCount;
        this.status = status;
    }
    public String getId() {
        return id;
    }

    public String getImageResource() {
        return imageResource;
    }

    public String getTitle() {
        return title;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public double getPrice() {
        return price;
    }

    public String getAddress() {
        return address;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

