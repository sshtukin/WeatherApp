package com.sshtukin.weatherapp.model;

public class ClearedWeather {
    private String day;
    private int maxTemp;
    private int minTemp;
    private Double presure;
    private String description;
    private String image;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public Double getPresure() {
        return presure;
    }

    public void setPresure(Double presure) {
        this.presure = presure;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
