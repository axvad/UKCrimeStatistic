package ru.sam.ukcrimestat.apipolice;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.sam.ukcrimestat.filters.CrimeCategory;

/**
 * Created by sam on 13.10.17.
 */

public interface PoliceUKApi {

    @GET("crimes-street/all-crime")
    Observable<List<CrimeEvent>> getEventsNear(@Query("lat") double lat, @Query("lng") double lng,@Query("date") String date);

    @GET("crime-categories")
    Observable<List<CrimeCategory>> getCrimeCategories(@Query("date") String date);
}
