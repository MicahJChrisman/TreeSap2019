package com.example.treesapv2new.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Simple parsing and formatting of GPS coordinates.
 *
 * @author Richard Mihalovič
 * @url http://en.wikipedia.org/wiki/Geographic_coordinate_conversion#Putting_it_all_together
 * @url https://github.com/richard-mihalovic/GpsCoordinatesParser
 */
public class GPSCoordinates {
    private Double latitude = 0.;
    private Double longitude = 0.;

    public GPSCoordinates(){
    }

    public GPSCoordinates(Double lat, Double lon){
        this.latitude = lat;
        this.longitude = lon;
    }

    public void setLatitude(Double latitude){
        this.latitude = latitude;
    }

    public Double getLatitude(){
        return this.latitude;
    }

    public void setLongitude(Double longitude){
        this.longitude = longitude;
    }

    public Double getLongitude(){
        return this.longitude;
    }

    public Boolean parse(String input){
        String myInput = input;

        myInput = myInput.replaceAll("^\\s+", ""); // trim starting spaces
        myInput = myInput.replaceAll("\\s+$", ""); // trim ending spaces

        Pattern p = Pattern.compile(
                "([NSEW+-]{1})?\\s?([0-9\\.]{1,})\\s?([0-9\\.]{1,})?\\s?([0-9\\.]{1,})?([NSEW+-]{1})?"
                        +
                        "\\s?[\\,\\s]{1}\\s?"
                        +
                        "([NSEW+-]{1})?\\s?([0-9\\.]{1,})\\s?([0-9\\.]{1,})?\\s?([0-9\\.]{1,})?([NSEW+-]{1})?",
                Pattern.CASE_INSENSITIVE
        );
        Matcher m = p.matcher(myInput);

        Double latDeg = null;
        Double latMin = null;
        Double latSec = null;
        String latDir = "N";

        Double lonDeg = null;
        Double lonMin = null;
        Double lonSec = null;
        String lonDir = "E";

        if(m.matches()){
            if(m.group(1) != null)
                latDir = m.group(1);
            if(m.group(2) != null)
                latDeg = parseDouble(m.group(2));
            if(m.group(3) != null)
                latMin = parseDouble(m.group(3));
            if(m.group(4) != null)
                latSec = parseDouble(m.group(4));
            if(m.group(5) != null)
                if(m.group(1) != null && m.group(5) != null)
                    lonDir = m.group(5);
                else
                    latDir = m.group(5);

            if(m.group(6) != null)
                lonDir = m.group(6);
            if(m.group(7) != null)
                lonDeg = parseDouble(m.group(7));
            if(m.group(8) != null)
                lonMin = parseDouble(m.group(8));
            if(m.group(9) != null)
                lonSec = parseDouble(m.group(9));
            if(m.group(10) != null)
                lonDir = m.group(10);

            if(latDeg != null && latMin != null && latSec != null) {
                latitude = convertFromDMS2Decimal(latDeg, latMin, latSec, latDir);
                longitude = convertFromDMS2Decimal(lonDeg, lonMin, lonSec, lonDir);

                return true;
            } else if(latDeg != null && latMin != null && latSec == null) {
                latitude = convertFromDM2Decimal(latDeg, latMin, latDir);
                longitude = convertFromDM2Decimal(lonDeg, lonMin, lonDir);

                return true;
            } else if (latDeg != null && latMin == null && latSec == null){

                latitude = latDeg;
                if(
                        latDir.equalsIgnoreCase("S")
                                ||
                                latDir.equalsIgnoreCase("-")
                        ) latitude = -latitude;

                longitude = lonDeg;
                if(
                        lonDir.equalsIgnoreCase("W")
                                ||
                                lonDir.equalsIgnoreCase("-")
                        ) longitude = -longitude;

                return true;
            }

        } else  {
            // bad input format
        }

        return false;
    }

    private Double parseDouble(String numberStr){
        try {
            return Double.parseDouble(numberStr);
        } catch (Exception e){
            return 0.0;
        }
    }

    private Double convertFromDMS2Decimal(Double d, Double m, Double s, String dir){
        Double _d; Double _m; Double _s;

        _d = d == null ? 0. : d;
        _m = m == null ? 0. : m;
        _s = s == null ? 0. : s;

        Double result = _d + _m / 60.0 + _s / 3600;
        if(
                dir.equalsIgnoreCase("S")
                        ||
                        dir.equalsIgnoreCase("W")
                        ||
                        dir.equalsIgnoreCase("-")
                ) result = -result;

        return result;
    }

    private Double convertFromDM2Decimal(Double d, Double m, String dir){
        Double _d; Double _m;

        _d = d == null ? 0. : d;
        _m = m == null ? 0. : m;

        Double result = _d + _m / 60.0;

        if(
                dir.equalsIgnoreCase("S")
                        ||
                        dir.equalsIgnoreCase("W")
                        ||
                        dir.equalsIgnoreCase("-")
                ) result = -result;

        return result;
    }

    @Override
    public boolean equals(Object o){
        if(o.getClass() != this.getClass()) return false;

        GPSCoordinates c = (GPSCoordinates) o;

        if(c.getLatitude().equals(getLatitude()) && c.getLongitude().equals(getLongitude()))
            return true;
        else
            return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + (this.latitude != null ? this.latitude.hashCode() : 0);
        hash = 59 * hash + (this.longitude != null ? this.longitude.hashCode() : 0);
        return hash;
    }

    public String toString() {
        return "("+latitude+", "+longitude+")";
    }
 }