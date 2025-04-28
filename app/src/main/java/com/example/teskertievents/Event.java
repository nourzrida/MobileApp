package com.example.teskertievents;

public class Event {
    private int id;
    private String title;
    private String date;
    private String venue;
    private String price;
    private int imageResourceId;

    public Event(int id, String title, String date, String venue, String price, int imageResourceId) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.venue = venue;
        this.price = price;
        this.imageResourceId = imageResourceId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDate() {
        return date;
    }

    public String getVenue() {
        return venue;
    }

    public String getPrice() {
        return price;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }
}