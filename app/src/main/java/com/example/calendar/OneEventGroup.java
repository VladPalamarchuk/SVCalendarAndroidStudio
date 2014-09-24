package com.example.calendar;

public class OneEventGroup {

    public int id;
    public int color;
    public int start_date_year;
    public int start_date_mounth;
    public int start_date_day;
    public int start_date_hour;
    public int start_date_minute;
    public int end_date_year;
    public int end_date_mounth;
    public int end_date_day;
    public int end_date_hour;
    public int end_date_minute;
    public String category;
    public String location;
    public String info;
    public int status;
    private String file_path;
    private String title;
    private String push_time;
    private String sound;
    private int isDone;
    private String friends;
    private String administrators;

    public OneEventGroup(int id, String title, int color, int start_date_year,
                         int start_date_mounth, int start_date_day, int start_date_hour,
                         int start_date_minute, int end_date_year, int end_date_mounth,
                         int end_date_day, int end_date_hour, int end_date_minute,
                         String category, String location, String info, int status,
                         String file_path, String push_time, String sound, int isDone,
                         String friends, String administrators) {
        super();
        this.id = id;
        this.setFile_path(file_path);
        this.color = color;
        this.setTitle(title);
        this.setFriends(friends);
        this.setAdministrators(administrators);
        this.setSound(sound);
        this.start_date_year = start_date_year;
        this.start_date_mounth = start_date_mounth;
        this.start_date_day = start_date_day;
        this.setPush_time(push_time);
        this.start_date_hour = start_date_hour;
        this.start_date_minute = start_date_minute;
        this.end_date_year = end_date_year;
        this.end_date_mounth = end_date_mounth;
        this.end_date_day = end_date_day;
        this.end_date_hour = end_date_hour;
        this.end_date_minute = end_date_minute;
        this.category = category;
        this.location = location;
        this.info = info;
        this.status = status;
        this.setIsDone(isDone);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getStart_date_year() {
        return start_date_year;
    }

    public void setStart_date_year(int start_date_year) {
        this.start_date_year = start_date_year;
    }

    public int getStart_date_mounth() {
        return start_date_mounth;
    }

    public void setStart_date_mounth(int start_date_mounth) {
        this.start_date_mounth = start_date_mounth;
    }

    public int getStart_date_day() {
        return start_date_day;
    }

    public void setStart_date_day(int start_date_day) {
        this.start_date_day = start_date_day;
    }

    public int getStart_date_hour() {
        return start_date_hour;
    }

    public void setStart_date_hour(int start_date_hour) {
        this.start_date_hour = start_date_hour;
    }

    public int getStart_date_minute() {
        return start_date_minute;
    }

    public void setStart_date_minute(int start_date_minute) {
        this.start_date_minute = start_date_minute;
    }

    public int getEnd_date_year() {
        return end_date_year;
    }

    public void setEnd_date_year(int end_date_year) {
        this.end_date_year = end_date_year;
    }

    public int getEnd_date_mounth() {
        return end_date_mounth;
    }

    public void setEnd_date_mounth(int end_date_mounth) {
        this.end_date_mounth = end_date_mounth;
    }

    public int getEnd_date_day() {
        return end_date_day;
    }

    public void setEnd_date_day(int end_date_day) {
        this.end_date_day = end_date_day;
    }

    public int getEnd_date_hour() {
        return end_date_hour;
    }

    public void setEnd_date_hour(int end_date_hour) {
        this.end_date_hour = end_date_hour;
    }

    public int getEnd_date_minute() {
        return end_date_minute;
    }

    public void setEnd_date_minute(int end_date_minute) {
        this.end_date_minute = end_date_minute;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    String getTitle() {
        return title;
    }

    void setTitle(String title) {
        this.title = title;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getPush_time() {
        return push_time;
    }

    public void setPush_time(String push_time) {
        this.push_time = push_time;
    }

    String getSound() {
        return sound;
    }

    void setSound(String sound) {
        this.sound = sound;
    }

    int getIsDone() {
        return isDone;
    }

    void setIsDone(int isDone) {
        this.isDone = isDone;
    }

    public String getFriends() {
        return friends;
    }

    public void setFriends(String friends) {
        this.friends = friends;
    }

    public String getAdministrators() {
        return administrators;
    }

    public void setAdministrators(String administrators) {
        this.administrators = administrators;
    }

}
