package io.github.pawel_bogdan.model;

public class Event {
    protected String name;
    protected String date;
    protected String url;

    public Event(String name, String date, String url) {
        this.name = name;
        this.date = date;
        this.url = url;
    }
    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }
}
