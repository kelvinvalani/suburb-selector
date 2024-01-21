package com.example.suburbselectorfinal;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class University {

    private String name;
    private ArrayList<Suburb> closeSuburbs;
    private Location location;

    public University(String name, ArrayList<Suburb> closeSuburbs, Location location) {
        this.name = name.toLowerCase();
        this.closeSuburbs = closeSuburbs;
        this.location = location;
    }

    public ArrayList<Suburb> getSuburbs(int priorityRent, int priorityCrime, int priorityTransport, ArrayList<LatLng> places) {
        // set suburb scores based on priorities
        for (Suburb suburb: closeSuburbs) {
            double rentScore = priorityRent * suburb.getRent();
            double crimeScore = priorityCrime * suburb.getCrime();
            double transportScore = priorityTransport * suburb.getTransport();
            double totalScore = rentScore + crimeScore + transportScore;
            suburb.setScore(totalScore);

            double distance = 0;
            LatLng suburbLatLng = new LatLng(suburb.getLocation().getLatitude(), suburb.getLocation().getLongitude());

            for (LatLng latLng: places) {
                distance += SphericalUtil.computeDistanceBetween(suburbLatLng, latLng) / 1000;
            }
            suburb.setDistanceScore(distance);
        }

        // order suburb list based on distances and take top 10
        Collections.sort(closeSuburbs, new SortByDistanceScore());
        ArrayList<Suburb> closeSuburbsSorted =  new ArrayList<>(closeSuburbs.subList(0,10));

        // order suburb list based on scores
        Collections.sort(closeSuburbsSorted, new SortByScore());
        return closeSuburbsSorted;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public void setCloseSuburbs(ArrayList<Suburb> closeSuburbs) {
        this.closeSuburbs = closeSuburbs;
    }
}

class SortByScore implements Comparator<Suburb> {

    @Override
    public int compare(Suburb o1, Suburb o2) {
        return Double.compare(o2.getScore(), o1.getScore());
    }
}

class SortByDistanceScore implements Comparator<Suburb> {

    @Override
    public int compare(Suburb o1, Suburb o2) {
        return Double.compare(o1.getDistanceScore(), o2.getDistanceScore());
    }
}
