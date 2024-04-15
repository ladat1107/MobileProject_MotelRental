package com.motel.mobileproject_motelrental.Model;

public class Ward {
    private String id;
    private String name;
    private String type;
    private String displayName;

    public Ward(String id, String name, String type, String displayName) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}
