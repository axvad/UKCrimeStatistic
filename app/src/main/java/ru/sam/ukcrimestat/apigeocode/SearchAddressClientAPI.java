package ru.sam.ukcrimestat.apigeocode;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Request for Google MAP API. Get locations by address
 */

public interface SearchAddressClientAPI {
    @GET("geocode/json")
    Observable<LocationSearchResponse> findLacationByAddress(@Query("address") String address, @Query("key") String key);

}
