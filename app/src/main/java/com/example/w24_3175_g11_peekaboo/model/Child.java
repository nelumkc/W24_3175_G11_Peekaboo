package com.example.w24_3175_g11_peekaboo.model;

public class Child {
    private String name;
    private String dob;
    private String imagePath;

    public Child(String name, String dob, String imagePath) {
        this.name = name;
        this.dob = dob;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
