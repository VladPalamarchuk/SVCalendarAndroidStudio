package com.example.calendar;

public class OneFriend {


    public static final int VK_TYPE = 1;
    public static final int FACEBOOK_TYPE = 2;
    public static final int PHONE_TYPE = 3;
    public String first_name;
    public String last_name;
    public String date_birthday;
    public String contact;
    public int type;
    private int id;
    private String location;
    private String photo;

    public OneFriend(int id, String first_name, String last_name, String date_birthday,
                     String contact, int type, String location, String photo) {
        super();
        this.setId(id);
        this.first_name = first_name;
        this.last_name = last_name;
        this.date_birthday = date_birthday;
        this.contact = contact;
        this.type = type;
        this.setLocation(location);
        this.setPhoto(photo);
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getDate_birthday() {
        return date_birthday;
    }

    public void setDate_birthday(String date_birthday) {
        this.date_birthday = date_birthday;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    int getId() {
        return id;
    }

    void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
