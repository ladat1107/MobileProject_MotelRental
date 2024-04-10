package com.motel.mobileproject_motelrental.Item;

public class CommentItem {
    private int avatarResource;
    private String name;
    private String day;
    private String content;

    public CommentItem(int avatarResource, String name, String day, String content) {
        this.avatarResource = avatarResource;
        this.name = name;
        this.day = day;
        this.content = content;
    }

    public int getAvatarResource() {
        return avatarResource;
    }

    public String getName() {
        return name;
    }

    public String getDay() {
        return day;
    }

    public String getContent() {
        return content;
    }
}
