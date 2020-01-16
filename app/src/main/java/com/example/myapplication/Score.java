package com.example.myapplication;

public class Score implements Comparable<Score>{
    private String name;
    private int score;
    private double lon,lat;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public Score(String name, int score, double lon, double lat) {
        this.name = name;
        this.score = score;
        this.lon = lon;
        this.lat = lat;
    }

    @Override
    public int compareTo(Score o) {
        return o.score - score;
    }
}
