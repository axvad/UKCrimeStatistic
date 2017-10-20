package ru.sam.ukcrimestat.apigeocode;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sam on 16.10.17.
 */

public class LocationSearchResponse {

    @SerializedName("results")
    private List<LocationAddress> addresses = null;

    private String status;

    @Override
    public String toString() {
        return "LocationSearchResponse{" +
                "result=" + addresses +
                ", status='" + status + '\'' +
                '}';
    }

    public @Nullable LocationAddress getAddress(){
        if (addresses!=null && isResultOk()) {
            return addresses.get(0);
        }

        return null;
    }


    public @Nullable List<LocationAddress> getAllAddresses() {
        if (isResultOk()) return addresses;

        return null;
    }

    public boolean isResultOk() {
        return "OK".equals(status);
    }

    public String getStatus() {
        return status;
    }
}
