package com.kmmi.notekas.model;

public class SlidePage {
    private int image;

    public SlidePage(int image, String slogan) {
        this.image = image;
        this.slogan = slogan;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    private String slogan;

}
