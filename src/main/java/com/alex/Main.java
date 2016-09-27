package com.alex;

import com.google.maps.ElevationApi;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.ElevationResult;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    public static Scanner stringScanner = new Scanner(System.in);
    public static Scanner numberScanner = new Scanner(System.in);

    public static void main(String[] args) throws Exception {

        //todo: add your key files
        // store the geolocation key in a string
        String geolocationKey = null;
        try (BufferedReader reader = new BufferedReader(new FileReader("geolocation_key.txt"))){
            geolocationKey = reader.readLine();
            reader.close();
        } catch (Exception e){
            System.out.println("No key file found, or could not read key. Please check geolocation_key.txt");
            System.exit(-1);
        }


        //todo: add your key files
        // store the elevation key in a string
        String elevationKey = null;
        try (BufferedReader reader = new BufferedReader(new FileReader("elevation_key.txt"))){
            elevationKey = reader.readLine();
            reader.close();
        } catch (Exception e){
            System.out.println("No key file found, or could not read key. Please check elevation_key.txt");
            System.exit(-1);
        }

        //ask the user to input a place to find the elevation of
        System.out.println("What place do you want to know the elevation for?");
        String location = stringScanner.nextLine();


        //get the lat/long of the place the user input
        GeoApiContext context = new GeoApiContext().setApiKey(geolocationKey);

        GeocodingResult[] geoResult = GeocodingApi.geocode(context, location).await();



        if (geoResult.length >= 1) {
            for (int i = 0; i < geoResult.length; i++){
                System.out.println(i + " " + geoResult[i].formattedAddress);
            }

            System.out.println("Press the number next to the specfic location you wanted.");
            int locationOption = numberScanner.nextInt();


            LatLng locationLatLng = geoResult[locationOption].geometry.location;

            //get the elevation for the location the user input using the lat/long you got from geolocation
            context = new GeoApiContext().setApiKey(elevationKey);

            ElevationResult[] results = ElevationApi.getByPoints(context, locationLatLng).await();

            if (results.length >= 1){
                ElevationResult mctcElevation = results[0];
                System.out.println(String.format("The elevation of " + geoResult[locationOption].formattedAddress +" above sea level is %.2f meters.", mctcElevation.elevation));
            }
        } else {
            System.out.println("No matches for " + location + " were found.");
        }






    }
}
