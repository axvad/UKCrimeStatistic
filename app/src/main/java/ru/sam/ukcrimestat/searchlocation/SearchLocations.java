package ru.sam.ukcrimestat.searchlocation;

import android.content.Context;
import android.os.Build;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import ru.sam.ukcrimestat.R;
import ru.sam.ukcrimestat.apigeocode.ApiClientGoogle;
import ru.sam.ukcrimestat.apigeocode.LocationAddress;
import ru.sam.ukcrimestat.apigeocode.LocationSearchResponse;
import ru.sam.ukcrimestat.apigeocode.SearchAddressClientAPI;

/**
 * Search controller
 */

public class SearchLocations {

    SearchAddressClientAPI apiSearch = ApiClientGoogle.getInstance().getApi();

    ResultListener searchListener;

    List<Disposable> disposables = null;

    SearchLocations(ResultListener listener) {
        this.searchListener = listener;

        disposables = new ArrayList<>();
    }

    void disposeAll() {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            disposables.removeIf(Disposable::isDisposed);

            disposables.forEach(dis -> {
                if (!dis.isDisposed()) dis.dispose();
            });
        } else {
            Iterator<Disposable> it = disposables.iterator();

            while (it.hasNext()) {
                Disposable dis = it.next();

                if (dis.isDisposed()) it.remove();
                else dis.dispose();
            }
        }

    }

    void unBind(ResultListener listener) {

        disposeAll();

        this.searchListener = null;
    }

    String getKey() {
        if (this.searchListener == null) return null;

        return searchListener.getContext().getApplicationContext()
                .getResources().getString(R.string.apikey_google_geolocator);
    }

    void searchLocation(String address) {

        Disposable disp;

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            disp = apiSearch.findLacationByAddress(address, getKey())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(response -> searchListener.onSearchResult(response.getAllAddresses()),
                            error -> searchListener.onSearchError(error.getMessage()));
        } else {
            DisposableObserver<LocationSearchResponse> dispObservList = new DisposableObserver<LocationSearchResponse>() {
                @Override
                public void onNext(LocationSearchResponse value) {
                    searchListener.onSearchResult(value.getAllAddresses());
                }

                @Override
                public void onError(Throwable e) {
                    searchListener.onSearchError(e.getMessage());
                }

                @Override
                public void onComplete() {

                }
            };

            apiSearch.findLacationByAddress(address, getKey())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(dispObservList);

            disp = dispObservList;
        }

        disposables.add(disp);
    }

    /**
     * Result callback listener
     */

    public static interface ResultListener {
        Context getContext();

        void onSearchResult(List<LocationAddress> addressList);

        void onSearchError(String message);
    }
}

