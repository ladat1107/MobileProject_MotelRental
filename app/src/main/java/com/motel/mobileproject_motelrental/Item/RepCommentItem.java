package com.motel.mobileproject_motelrental.Item;

public class RepCommentItem {
    private String id;
    private String avatarResource;
    private String name;
    private String repname;
    private String day;
    private String content;

    public RepCommentItem(String id, String avatarResource, String name, String repname, String day, String content) {
        this.id = id;
        this.avatarResource = avatarResource;
        this.name = name;
        this.repname = repname;
        this.day = day;
        this.content = content;
    }
    public String getId() { return id; }
    public String getAvatarResource() {
        return avatarResource;
    }
    public String getName() {
        return name;
    }
    public String getRepname() {
        return repname;
    }
    public String getDay() {
        return day;
    }
    public String getContent() {
        return content;
    }
}
