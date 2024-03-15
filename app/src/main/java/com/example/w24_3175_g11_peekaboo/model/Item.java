package com.example.w24_3175_g11_peekaboo.model;

public class Item {
    private String text;
    private String imageResource;

    public Item(String text, String imageResource) {
        this.text = text;
        this.imageResource = imageResource;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }
}
