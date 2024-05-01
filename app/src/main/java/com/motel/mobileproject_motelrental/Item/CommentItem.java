package com.motel.mobileproject_motelrental.Item;

public class CommentItem {
    private String id;
    private String avatarResource;
    private String avatarRep;
    private String name;
    private String nameRep;
    private String day;
    private String content;

    public CommentItem(String id, String avatarResource, String avatarRep, String name, String nameRep, String day, String content) {
        this.id = id;
        this.avatarResource = avatarResource;
        this.avatarRep = avatarRep;
        this.name = name;
        this.nameRep = nameRep;
        this.day = day;
        this.content = content;
    }
    public String getId() { return id; }
    public String getAvatarResource() {
        return avatarResource;
    }
    public String getAvatarRep() {
        return avatarRep;
    }
    public String getName() {
        return name;
    }
    public String getNameRep() {
        return nameRep;
    }
    public String getDay() {
        return day;
    }
    public String getContent() {
        return content;
    }
}
