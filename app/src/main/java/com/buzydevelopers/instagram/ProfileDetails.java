package com.buzydevelopers.instagram;

public class ProfileDetails {

    String url,name,username,bio;

    public ProfileDetails() {
    }

    public ProfileDetails(String url, String name, String username, String bio) {
        this.url = url;
        this.name = name;
        this.username = username;
        this.bio = bio;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
