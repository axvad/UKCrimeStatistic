package ru.sam.ukcrimestat;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;
import org.junit.runner.RunWith;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import ru.sam.ukcrimestat.apigeocode.LocationSearchResponse;
import ru.sam.ukcrimestat.apigeocode.ApiClientGoogle;
import ru.sam.ukcrimestat.apigeocode.SearchAddressClientAPI;

import static java.lang.Thread.sleep;
import static org.junit.Assert.*;

/**
 * Created by sam on 16.10.17.
 */
@RunWith(AndroidJUnit4.class)
public class TestGoogleApiSearch {

    @Test
    public void getLocation() {
        final String TAG = "TestAPI.getLocation()";

        // input parameters
        String key = InstrumentationRegistry.
                getTargetContext().getString(R.string.apikey_google_geolocator);

        String address = "1600+Amphitheatre+Parkway,+Mountain+View,+CA";

        // initiate api
        ApiClientGoogle google = ApiClientGoogle.getInstance();

        SearchAddressClientAPI api = google.getApi();

        // observ for request
        Observable<LocationSearchResponse> observ = api.findLacationByAddress(address,key);

        // callback object
        DisposableObserver<LocationSearchResponse> disposableObserver
                = new DisposableObserver<LocationSearchResponse>() {
            @Override
            public void onNext(LocationSearchResponse value) {
                System.out.println("Observer.OnNext: " + value);

                if (value.isResultOk()) {
                    assertEquals(new LatLng(37.4216548, -122.0856374), value.getAddress().getLocation());
                } else {
                    System.out.println("Observer.OnNext: response "+value.getStatus());
                    assertTrue("Observer. Response not OK: "+value.getStatus(), false);
                }
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("Observer.OnError: " + e.getMessage());

                assertTrue(String.format("Error response %s", e.getMessage()), false);
            }

            @Override
            public void onComplete() {
                System.out.println("Observer.OnComplite");
            }
        };


        // start observ
        observ.observeOn(Schedulers.io())
                .subscribe(disposableObserver);

        // sleep main thread
        try {
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // disconnect
        disposableObserver.dispose();

        System.out.println("Test finish.");

    }
}
