package com.example.suburbselectorfinal;

import org.junit.Test;

import static org.junit.Assert.*;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void test_location() {
        Location locationNegative = new Location(37.8136,144.9631);
        assertTrue(locationNegative.getLatitude() == -37.8136);
        assertTrue(locationNegative.getLongitude() == 144.9631);
        Location locationPositive = new Location(-37.8136,144.9631);
        assertTrue(locationPositive.getLatitude() == -37.8136);
        assertTrue(locationPositive.getLongitude() == 144.9631);
    }

    @Test
    public void test_suburb() {
        Suburb suburb = new Suburb("Melbourne", new Location(0,0), 0, 0, 0, 0);
        assertTrue(Objects.equals(suburb.getName(), "melbourne"));
    }

    @Test
    public void test_university(){
        ArrayList<Suburb> suburbs = new ArrayList<>();
        suburbs.add(new Suburb("Murrumbeena", new Location(37.8978,145.0709), 0.13, 0.45, 1.0,386));
        suburbs.add(new Suburb("Carnegie", new Location(37.8892,145.0571), 0.12, 0.42, 1.0,395));
        suburbs.add(new Suburb("Wheelers Hill", new Location(37.9068,145.1890), 0.03, 0.55, 0.9,481));
        suburbs.add(new Suburb("Mount waverley", new Location(37.8750,145.1291), 0.03, 0.43, 0.96,476));
        suburbs.add(new Suburb("Notting Hill", new Location(37.9048,145.1459), 0.16, 0.32, 0.94,371));
        suburbs.add(new Suburb("Oakleigh east", new Location(37.9078,145.1168), 0.07, 0.4, 0.94,425));
        suburbs.add(new Suburb("Burwood", new Location(37.8495,145.1090), 0.08, 0.38, 0.92,411));
        suburbs.add(new Suburb("Clayton South", new Location(37.9414,145.1262), 0.14, 0.24, 1.0,381));
        suburbs.add(new Suburb("Glen Waverley", new Location(37.8744,145.1668), 0.03, 0.36, 0.98,480));
        suburbs.add(new Suburb("Bentleigh east", new Location(37.9212,145.0590), 0.02, 0.53, 0.8,500));
        suburbs.add(new Suburb("Springvale", new Location(37.9487,145.1529), 0.18, 0.14, 0.99,357));
        suburbs.add(new Suburb("Clarinda", new Location(37.9412,145.1024), 0.12, 0.43, 0.73,394));
        suburbs.add(new Suburb("Huntingdale", new Location(37.9079,145.1107), 0.1, 0.25, 0.89,400));
        suburbs.add(new Suburb("Ashburton", new Location(37.8636,145.0811), 0.04, 0.24, 0.96,455));
        suburbs.add(new Suburb("Malvern East", new Location(37.8773,145.0593), 0.07, 0.19, 0.97,421));
        suburbs.add(new Suburb("Chadstone", new Location(37.8810,145.0960), 0.07, 0.21, 0.9,421));
        suburbs.add(new Suburb("Oakleigh", new Location(37.9017,145.0919), 0.09, 0.09, 0.99,410));
        suburbs.add(new Suburb("Ashwood", new Location(37.8683,145.1054), 0.12, 0.19, 0.83,391));
        suburbs.add(new Suburb("Scoresby", new Location(37.8925,145.2304), 0.09, 0.2, 0.79,406));
        suburbs.add(new Suburb("Mulgrave", new Location(37.9187,145.1588), 0.06, 0.24, 0.77,430));

        University university = new University("Monash University, Clayton", suburbs, new Location(-37.91281395077763, 145.1359658075086));

        ArrayList<LatLng> places = new ArrayList<>();
        // the results should only contain the top 10 suburbs
        assertTrue(university.getSuburbs(1,1,1, places).size() == 10);

        university.setCloseSuburbs((ArrayList) suburbs.clone());

        // with no priority difference, results should equal first 10 in suburb list (highest scoring 10)
        assertTrue(university.getSuburbs(1,1,1, places).equals(suburbs.subList(0, 10)));

        university.setCloseSuburbs((ArrayList) suburbs.clone());

        // test for rent as only priority
        ArrayList<Suburb> rentOrder = new ArrayList<>();
        rentOrder.add(suburbs.get(4));
        rentOrder.add(suburbs.get(7));
        rentOrder.add(suburbs.get(0));
        rentOrder.add(suburbs.get(1));
        rentOrder.add(suburbs.get(6));
        rentOrder.add(suburbs.get(5));
        rentOrder.add(suburbs.get(2));
        rentOrder.add(suburbs.get(3));
        rentOrder.add(suburbs.get(8));
        rentOrder.add(suburbs.get(9));

        assertTrue(university.getSuburbs(1,0,0, places).equals(rentOrder));

        university.setCloseSuburbs((ArrayList) suburbs.clone());

        // test for crime as only priority
        ArrayList<Suburb> crimeOrder = new ArrayList<>();
        crimeOrder.add(suburbs.get(2));
        crimeOrder.add(suburbs.get(9));
        crimeOrder.add(suburbs.get(0));
        crimeOrder.add(suburbs.get(3));
        crimeOrder.add(suburbs.get(1));
        crimeOrder.add(suburbs.get(5));
        crimeOrder.add(suburbs.get(6));
        crimeOrder.add(suburbs.get(8));
        crimeOrder.add(suburbs.get(4));
        crimeOrder.add(suburbs.get(7));

        assertTrue(university.getSuburbs(0,1,0, places).equals(crimeOrder));

        university.setCloseSuburbs((ArrayList) suburbs.clone());

        // test for transport as only priority
        ArrayList<Suburb> transportOrder = new ArrayList<>();
        transportOrder.add(suburbs.get(0));
        transportOrder.add(suburbs.get(1));
        transportOrder.add(suburbs.get(7));
        transportOrder.add(suburbs.get(8));
        transportOrder.add(suburbs.get(3));
        transportOrder.add(suburbs.get(4));
        transportOrder.add(suburbs.get(5));
        transportOrder.add(suburbs.get(6));
        transportOrder.add(suburbs.get(2));
        transportOrder.add(suburbs.get(9));

        assertTrue(university.getSuburbs(0,0,1, places).equals(transportOrder));

        university.setCloseSuburbs((ArrayList) suburbs.clone());

        // test for places, pick western most suburb and choose eastern place
        // results should not include the western most suburb
        LatLng western = new LatLng(-37.8827, 145.2776);
        places.add(western);

        assertFalse(university.getSuburbs(1,1,1, places).contains(suburbs.get(9)));

    }

    @Test
    public void test_university_factory() {
        UniversityFactory universityFactory = new UniversityFactory();
        University university = null;
        try {
            university = universityFactory.createUni("null");
        }
        catch (Exception e) {
            System.out.println(e.toString());
        }

        assertNull(university);
    }
}