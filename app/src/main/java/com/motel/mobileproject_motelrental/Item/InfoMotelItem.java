package com.motel.mobileproject_motelrental.Item;

public class InfoMotelItem {
    private String id;
    private int imageResource;
    private String title;
    private int likeCount;
    private double price;
    private String address;
    private int commentCount;

    public InfoMotelItem(String id, int imageResource, String title, int likeCount, double price, String address, int commentCount) {
        this.id = id;
        this.imageResource = imageResource;
        this.title = title;
        this.likeCount = likeCount;
        this.price = price;
        this.address = address;
        this.commentCount = commentCount;
    }
    public String getId() {
        return id;
    }

    public int getImageResource() {
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
}

