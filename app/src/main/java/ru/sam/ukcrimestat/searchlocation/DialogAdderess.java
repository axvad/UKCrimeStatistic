package ru.sam.ukcrimestat.searchlocation;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

import com.google.android.gms.maps.model.LatLng;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.internal.util.AppendOnlyLinkedArrayList;
import io.reactivex.observers.DisposableObserver;
import ru.sam.ukcrimestat.My;
import ru.sam.ukcrimestat.R;
import ru.sam.ukcrimestat.apigeocode.LocationAddress;

/**
 * Created by sam on 16.10.17.
 */

public class DialogAdderess extends AppCompatDialogFragment implements SearchLocations.ResultListener {

    private EditText cityInput;
    private Disposable textDisposable;

    SearchLocations locator;
    private ListView listView;

    private int selectedPosition = -1;

    @Override
    public void onDestroyView() {

        if(textDisposable !=null && !textDisposable.isDisposed()) {
            textDisposable.dispose();
        }

        locator.disposeAll();

        super.onDestroyView();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View dlgView = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_address, null);

        cityInput = (EditText) dlgView.findViewById(R.id.inputCity);

        listView = (ListView) dlgView.findViewById(R.id.dlg_address_list_result);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            listView.setOnItemClickListener(
                    (parent, view, position, id) -> {
                        selectedPosition = position;
                        dismiss();
                    });
        } else {
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectedPosition = position;
                    dismiss();
                }
            });
        }


        locator = new SearchLocations(this);

        return new AlertDialog.Builder(getActivity())
                .setView(dlgView)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (listView.getAdapter().getCount()>0) selectedPosition =0;
                            }
                        }
                        //(dialog, witch) ->  {if (listView.getAdapter().getCount()>0) selectedPosition =0;}
                )
                .create();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        locationSelected(selectedPosition);

        super.onDismiss(dialog);
    }

    // send result to listener
    void locationSelected(int position) {

        //check for cancel (nothing selected)
        if(position == -1) return;

        LatLng point = ((LocationAddress) listView.getAdapter().getItem(position)).getLocation();

        ((DialogAdderess.ResultListener) getActivity()).onLocationSelect(point);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        textDisposable = RxTextView.textChangeEvents(cityInput)
                .debounce(400, TimeUnit.MILLISECONDS)
                //.filter( changes -> isNotNullOrEmpty(changes.text().toString())
                .filter( new AppendOnlyLinkedArrayList.NonThrowingPredicate<TextViewTextChangeEvent>() {
                            @Override
                            public boolean test(TextViewTextChangeEvent textViewTextChangeEvent) {
                                return isNotNullOrEmpty(textViewTextChangeEvent.text().toString());
                            }
                        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(getNewObserver());

    }

    private static boolean isNotNullOrEmpty(String in) {
        return (in!=null && in.length()>0);
    }

    private DisposableObserver<TextViewTextChangeEvent> getNewObserver() {

        return new DisposableObserver<TextViewTextChangeEvent>() {
            @Override
            public void onNext(TextViewTextChangeEvent value) {
                if (My.DEBUG) Log.i(this.getClass().getSimpleName(), "OnNext: "+ value.text());

                locator.searchLocation(value.text().toString()+",+UK");
            }

            @Override
            public void onError(Throwable e) {
                if (My.DEBUG) Log.i(this.getClass().getSimpleName(), "OnError: "+ e.getMessage());


            }

            @Override
            public void onComplete() {
                if (My.DEBUG) Log.i(this.getClass().getSimpleName(), "OnComplite: ");

            }
        };
    }

    @Override
    public void onSearchResult(List<LocationAddress> addressList) {

        //List<String> list = addressList.stream().map(LocationAddress::getAddress).collect(Collectors.toList());

        if (addressList == null) return;
        ArrayAdapter<LocationAddress> adapter
                = new ArrayAdapter<LocationAddress>(getContext(),
                android.R.layout.simple_list_item_1, addressList);

        listView.setAdapter(adapter);
    }

    @Override
    public void onSearchError(String message) {
        if (My.DEBUG) Log.e(this.getClass().getSimpleName(), "searching error: "+message);
    }

    /**
     * Interface callback result from dialog to application
     */

    public static interface ResultListener {
        void onLocationSelect(LatLng point);
    }
}
