package com.example.calendar;

public class User {

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String login;
    public String password;
    public int id;
    public String src;
    public String email;
    String name;
    String surname;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    String phone;

    public User(String login, String password, int id, String src,
                String email, String name, String surname, String phone) {
        super();
        this.login = login;
        this.password = password;
        this.id = id;
        this.src = src;
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.phone = phone;
    }

}
