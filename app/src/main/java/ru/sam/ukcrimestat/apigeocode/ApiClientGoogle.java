package ru.sam.ukcrimestat.apigeocode;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.sam.ukcrimestat.My;

/**
 * Access for google api address search engine
 */

public class ApiClientGoogle {
    private static final ApiClientGoogle ourInstance = new ApiClientGoogle();

    private static final String BASE_URL = "https://maps.googleapis.com/maps/api/";

    private SearchAddressClientAPI api;

    public static ApiClientGoogle getInstance() {return ourInstance;}

    private ApiClientGoogle(){

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor()
                .setLevel((My.DEBUG) ? HttpLoggingInterceptor.Level.BODY: HttpLoggingInterceptor.Level.NONE))
                .build();

        Retrofit retofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();

        api = retofit.create(SearchAddressClientAPI.class);
    }

    public SearchAddressClientAPI getApi() {
        return api;
    }
}
