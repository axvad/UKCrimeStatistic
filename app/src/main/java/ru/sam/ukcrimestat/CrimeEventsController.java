package ru.sam.ukcrimestat;

import android.util.Log;

import java.util.List;
import java.util.Stack;

import io.reactivex.android.schedulers.AndroidSchedulers;

import io.reactivex.observers.DisposableObserver;
import ru.sam.ukcrimestat.filters.CrimeCategory;
import ru.sam.ukcrimestat.apipolice.*;

/**
 * Created by sam on 13.10.17.
 */

public class CrimeEventsController {

    private CrimeEventsListener mapView;

    private PoliceUKApi api = ApiClientPoliceUK.getInstance().getPoliceUKApi();
    private DataModel model = DataModel.getInstance();

    private Stack<DisposableObserver> observers;

    public CrimeEventsController(CrimeEventsListener mapView) {
        this.mapView = mapView;
        observers = new Stack<>();

    }

    public void unBind(CrimeEventsListener mapView) {

        if (mapView != null){
            ;//todo unsubscribe
        }

        while (!observers.empty()){
            DisposableObserver ob = observers.pop();
            if (!ob.isDisposed()) {
                System.out.printf("Warning! Dispose observer onDestroy: %s\n", ob);
                ob.dispose();
            }
        }
    }

    public void destroy() {
        unBind(mapView);
    }

    public void getCrimeEvents(double lat, double lng) {

        Log.i(this.getClass().getSimpleName(),"start: getCrimeEventsList");

        /*
        lat = 52.629729;
        lng = -1.131592;
        date ="2017-08" ;
        */
        String date = model.getDate().period();

        try {

            DisposableObserver<List<CrimeEvent>> disposableObserver
                    = new DisposableObserver<List<CrimeEvent>>() {

                @Override
                public void onNext(List<CrimeEvent> value) {
                    System.out.println("onNext: "+value.toString());

                    model.setCrimeEvents(value);
                    mapView.onReciveCrimeData();
                }


                @Override
                public void onError(Throwable e) {
                    System.out.println("onError");

                    mapView.onReciveCrimeDataError(e.getMessage());
                }

                @Override
                public void onComplete() {
                    System.out.println("onComplite");
                }
            };

            api.getEventsNear(lat,lng,date)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(disposableObserver);

            observers.push(disposableObserver);

        } catch (Exception e) {
            e.printStackTrace();
            mapView.onReciveCrimeDataError(e.getMessage());
        }
    }

    public void getCrimeCategories(){
        Log.i(this.getClass().getSimpleName(),"getCrimeCategories");

        String date = model.getDate().period();

        if (date == null || date.length()==0) {
            System.out.println("Error: date not set");
            return;
        }

        DisposableObserver<List<CrimeCategory>> disposableObserver
                = new DisposableObserver<List<CrimeCategory>>() {
            @Override
            public void onNext(List<CrimeCategory> value) {
                System.out.println("getCrimeCategories: OnNext "+ value);

                model.updateCrimeCategories(value,false);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("getCrimeCategories: OnError: "+ e.getMessage());

            }

            @Override
            public void onComplete() {
                System.out.println("getCrimeCategories: OnComplite ");
            }
        };

        api.getCrimeCategories(date)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(disposableObserver);

        observers.push(disposableObserver);
    }
}
