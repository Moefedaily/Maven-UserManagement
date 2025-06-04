package com.example.usermanagement.model;

import java.time.LocalDate;
import java.util.Objects;

public class User {

    private int id;
    private String name;
    private String email;
    private String phone;
    private LocalDate dateNaissance;

    public User() {
    }

    public User(String name, String email, String phone, LocalDate dateNaissance) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.dateNaissance = dateNaissance;
    }

    public User(int id, String name, String email, String phone, LocalDate dateNaissance) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.dateNaissance = dateNaissance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getDateNaissance() {
        return dateNaissance;
    }

    public void setDateNaissance(LocalDate dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", dateNaissance=" + dateNaissance +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return id == user.id &&
                Objects.equals(name, user.name) &&
                Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, email);
    }
}