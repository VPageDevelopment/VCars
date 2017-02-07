package com.vpage.vcars.pojos;

public class FacebookUserprofile {

    private String id;

    private String first_name;

    private String email;

    private String name;

    private String last_name;

    private String gender;

    private String facebookImage;

    private String token_for_business;

    public String getFacebookImage() {
        return facebookImage;
    }

    public void setFacebookImage(String facebookImage) {
        this.facebookImage = facebookImage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getToken_for_business() {
        return token_for_business;
    }

    public void setToken_for_business(String token_for_business) {
        this.token_for_business = token_for_business;
    }

    @Override
    public String toString() {
        return "FacebookUserprofile{" +
                "id='" + id + '\'' +
                ", first_name='" + first_name + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", last_name='" + last_name + '\'' +
                ", gender='" + gender + '\'' +
                ", facebookImage='" + facebookImage + '\'' +
                ", token_for_business='" + token_for_business + '\'' +
                '}';
    }
}
