package ru.sam.ukcrimestat.apipolice;


import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.sam.ukcrimestat.My;

/**
 * Created by sam on 13.10.17.
 */

public class ApiClientPoliceUK {
    private static final ApiClientPoliceUK ourInstance = new ApiClientPoliceUK();
    private static PoliceUKApi api;

    private static final String BASE_URL = "https://data.police.uk/api/";

    public static ApiClientPoliceUK getInstance() {
        return ourInstance;
    }

    private ApiClientPoliceUK() {

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor()
                        .setLevel((My.DEBUG) ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .build();

        api = retrofit.create(PoliceUKApi.class);
    }

    public PoliceUKApi getPoliceUKApi() {
        return api;
    }
}
