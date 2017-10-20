package ru.sam.ukcrimestat.apigeocode;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sam on 17.10.17.
 */

public class LocationAddress {
    @SerializedName("formatted_address")
    private String  address;

    @SerializedName("geometry")
    private Geometry geometry;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LocationAddress that = (LocationAddress) o;

        if (!address.equals(that.address)) return false;
        return geometry.equals(that.geometry);

    }

    @Override
    public int hashCode() {
        int result = address.hashCode();
        result = 31 * result + geometry.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return getAddress();
    }

    public String getAddress(){
        return address;
    }

    public LatLng getLocation(){
        return new LatLng(geometry.location.lat, geometry.location.lng);
    }

    public String getLocationType(){
        return geometry.location_type;
    }

    private class Geometry{

        private Point location;

        //ROOFTOP,RANGE_INTERPOLATED,GEOMETRIC_CENTER,APPROXIMATE
        private String location_type;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Geometry geometry = (Geometry) o;

            if (!location.equals(geometry.location)) return false;
            return location_type != null ? location_type.equals(geometry.location_type) : geometry.location_type == null;

        }

        @Override
        public int hashCode() {
            int result = location.hashCode();
            result = 31 * result + (location_type != null ? location_type.hashCode() : 0);
            return result;
        }

        private class Point {

            private double lat;

            private double lng;

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;

                Point point = (Point) o;

                if (Double.compare(point.lat, lat) != 0) return false;
                return Double.compare(point.lng, lng) == 0;

            }

            @Override
            public int hashCode() {
                int result;
                long temp;
                temp = Double.doubleToLongBits(lat);
                result = (int) (temp ^ (temp >>> 32));
                temp = Double.doubleToLongBits(lng);
                result = 31 * result + (int) (temp ^ (temp >>> 32));
                return result;
            }
        }
    }
}
