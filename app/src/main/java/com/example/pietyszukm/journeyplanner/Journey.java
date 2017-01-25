package com.example.pietyszukm.journeyplanner;

/**
 * Created by pietyszukm on 14.01.2017.
 */

public class Journey {
    private Integer id;
    private String name;
    private String description;
    private String country;
    private int cost;
    private String uri;
    private byte[] thumbnail;

    public Journey(){};
    public Journey(String name, String description, int cost, String country, String uri, byte[] imageId) {
        this.name = name;
        this.description = description;
        this.cost = cost;
        this.country = country;
        this.uri = uri;
        this.thumbnail = imageId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public byte[] getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(byte[] thumbnail) {
        this.thumbnail = thumbnail;
    }
}
