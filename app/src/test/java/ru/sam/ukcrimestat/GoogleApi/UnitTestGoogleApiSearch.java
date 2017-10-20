package ru.sam.ukcrimestat.GoogleApi;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.internal.schedulers.NewThreadScheduler;
import io.reactivex.observers.DisposableObserver;
import ru.sam.ukcrimestat.apigeocode.ApiClientGoogle;
import ru.sam.ukcrimestat.apigeocode.LocationSearchResponse;
import ru.sam.ukcrimestat.apigeocode.SearchAddressClientAPI;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by sam on 16.10.17.
 */
public class UnitTestGoogleApiSearch {

    @Test
    public void getLocation() {

        final String TAG = "GoogleAPI.getLocation(): ";

        System.out.println(TAG + "Start test. Try create Api");

        // input parameters
        String key = "AIzaSyBuuMMH7gHuR0JdztQLHxC1dpW2JZUHPHQ";
        String address = "1600+Amphitheatre+Parkway,+Mountain+View,+CA";

        // initiate api
        ApiClientGoogle google = ApiClientGoogle.getInstance();

        SearchAddressClientAPI api = google.getApi();

        System.out.println(TAG + "API created. Try observe");

        // observ for request
        Observable<LocationSearchResponse> observ = api.findLacationByAddress(address, key);

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
        System.out.println(TAG + "Try observeOn().");

        Scheduler sch = NewThreadScheduler.instance();

        observ.observeOn(sch)
                .subscribe(disposableObserver);

        // sleep main thread
        try {
            System.out.println(TAG + "sleep for 3 sec");
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // disconnect
        System.out.println(TAG+"Try dispose.");
        disposableObserver.dispose();


        System.out.println(TAG+"Test finish");

    }
}
