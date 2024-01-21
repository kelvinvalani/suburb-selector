package com.example.suburbselectorfinal;

import java.util.Locale;

public class Suburb {

    private final String name;
    private final Location location;
    private final double rent;
    private final double crime;
    private final double transport;
    private final double medianWeeklyRent;

    private double score;

    private double distanceScore;

    public Suburb(String name, Location location, double rent, double crime, double transport, double medianWeeklyRent) {
        this.name = name.toLowerCase();
        this.location = location;
        this.rent = rent;
        this.crime = crime;
        this.transport = transport;
        this.medianWeeklyRent  = medianWeeklyRent;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public double getRent() {
        return rent;
    }

    public double getCrime() {
        return crime;
    }

    public double getTransport() {
        return transport;
    }

    public double getMedianWeeklyRent() {
        return medianWeeklyRent;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getDistanceScore() {
        return distanceScore;
    }

    public void setDistanceScore(double distanceScore) {
        this.distanceScore = distanceScore;
    }
}
