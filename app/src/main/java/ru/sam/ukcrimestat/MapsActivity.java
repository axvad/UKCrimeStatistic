package ru.sam.ukcrimestat;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Collection;
import java.util.List;


import ru.sam.ukcrimestat.apipolice.CrimeEvent;
import ru.sam.ukcrimestat.searchlocation.DialogAdderess;
import ru.sam.ukcrimestat.filters.*;
import ru.sam.ukcrimestat.view_tools.*;


/**
 * Main activity. View for MVC model.
 *
 */
public class MapsActivity extends  AppCompatActivity implements
        OnMapReadyCallback, GoogleMap.OnMapClickListener,
        CrimeEventsListener, FilterSelectListener, DialogAdderess.ResultListener {

    private CrimeEventsController policeDB;
    private DataModel model;

    private GoogleMap mMap;
    //private LatLng lastPosition;
    private MapTarget target;

    private static final String DIALOG_FILTER = "DialogFilter";
    private static final String DIALOG_DATE = "DialogDate";
    private static final String DIALOG_ADDRESS = "DialogAddress";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_application);

        policeDB = new CrimeEventsController(this);

        model = DataModel.getInstance();

        // Obtain the SupportMapFragment and period notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        target = new MapTarget();

        restoreState();
    }

    @Override
    public void onDestroy(){

        policeDB.unBind(this);

        if( isFinishing() ){
            model.destroy();
            policeDB.destroy();

        } else {
            saveState();
        }

        super.onDestroy();
    }

    //save data for change screen or sleep
    private void saveState(){
        if (mMap != null) {
            model.setAttribute(DataModel.MAP_STATE,mMap.getCameraPosition());
        }
        model.setAttribute(DataModel.POS_LAST_REQUEST, target.getLastPosition());
    }

    private void restoreState() {
        target.set((LatLng) model.getAttribute(DataModel.POS_LAST_REQUEST, new LatLng(52.4122, 0.7513)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.app_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (mMap == null) {
            Toast st = Toast.makeText(this, R.string.error_map_not_ready,Toast.LENGTH_LONG);
            st.setGravity(Gravity.CENTER,0,0);
            st.show();

            return true;
        }

        int id = item.getItemId();

        if (id == R.id.action_show) {

            onLocationSelect( (target.isHidden()) ? mMap.getCameraPosition().target : target.getLastPosition());

            return true;

        } else if (id == R.id.action_filter) {

            if (!model.hasSomeData()) {
                Toast st = Toast.makeText(this, R.string.error_data_not_filling,Toast.LENGTH_LONG);
                st.setGravity(Gravity.CENTER,0,0);
                st.show();

                return true;
            }

            FragmentManager mng = getSupportFragmentManager();
            DialogFilters dialogFilters = new DialogFilters();
            dialogFilters.show(mng,DIALOG_FILTER);


            return true;

        } else if (id == R.id.action_set_date) {

            FragmentManager mng = getSupportFragmentManager();
            DialogDate dialogDate = new DialogDate();
            dialogDate.show(mng,DIALOG_DATE);

            return true;

        } else if (id == R.id.action_search_adress) {

            FragmentManager mng = getSupportFragmentManager();
            DialogAdderess dlgAddress = new DialogAdderess();
            dlgAddress.show(mng, DIALOG_ADDRESS);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Start map
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.setOnMapClickListener(this);

        // restore markers and view after change screen orientation
        showCrimeList(model.getCrimeEvents());
        Object attr = (CameraPosition) model.getAttribute(DataModel.MAP_STATE);

        if (attr != null) {
            CameraPosition pos = (CameraPosition) attr;
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos.target, pos.zoom));
        } else {
            // move the camera to default
            LatLng thetford = new LatLng(52.4122, 0.7513);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(thetford, 12f));
        }

    }

    @Override
    public void onReciveCrimeData() {

        LatLng lastmark = showCrimeList(model.getCrimeEvents());

        //show last added marker
        /*
        if (lastmark != null) {

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastmark,12f));
        }
        */

    }

    @Override
    public void onMapClick(LatLng latLng) {
        target.set(latLng).show(mMap);
    }

    /**
     * Action request data for Police Api. Period will getting from model.
     * @param pos LatLng center point
     */
    @Override
    public void onLocationSelect(LatLng pos) {


        if (mMap == null) {
            Log.e(this.getClass().getSimpleName(), "Google map is not ready");
            return;
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos,12f));

        //show selected target
        target.set(pos).show(mMap).searching();

        // request data
        policeDB.getCrimeEvents(pos.latitude,pos.longitude);

        // request categories for choosen period
        policeDB.getCrimeCategories();
    }

    /**
     * Show crime list on map.
     * @param list
     * @return
     */
    private LatLng showCrimeList(Collection<CrimeEvent> list){

        if (mMap == null) {
            Toast.makeText(this, getResources().getText(R.string.error_map_not_ready),Toast.LENGTH_SHORT).show();
            return null;
        }

        mMap.clear();
        target.clearMarkers();

        if (list == null || list.size()==0) {
            Toast.makeText(this, getResources().getText(R.string.error_nothing_for_show),Toast.LENGTH_SHORT).show();
            return null;
        }

        LatLng mark = null;

        Log.i(this.getClass().getSimpleName(), String.format("For shown %d events\n", list.size()));

        for(CrimeEvent ev:list){

            mark = new LatLng(ev.getLocation().getLatitude(),ev.getLocation().getLongitude());
            mMap.addMarker(new MarkerOptions()
                    .position(mark)
                    .title(ev.getCategory())
                    .icon( new CategoryIcon(this,ev.getCategory()).getBitmapDescriptor())
                    .anchor(0.5f,1f)
            );

            if (My.SET_CAMERA_ON_SEARCH_RESULT)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(target.getLastPosition(),12f));
        }

        return mark;
    }

    @Override
    public void onReciveCrimeDataError(String message) {

        Toast.makeText(this,"Error reciving data: "+ message,Toast.LENGTH_LONG).show();

        showCrimeList(model.getCrimeEvents());
    }

    @Override
    public void onPeriodSelected(CrimePeriod selectedPeriod) {

        // request events for changed period
        if (model.setDate(selectedPeriod) && target.getLastPosition()!=null) {
            onLocationSelect(target.getLastPosition());
        }
    }

    @Override
    public void onFilterChoosed(List<CrimeCategory> result) {

        if(My.DEBUG) Log.i(this.getClass().getSimpleName(),My.getCrimeListInfo(result));

        model.updateCrimeCategories(result,true);

        showCrimeList(model.getCrimeEvents());
    }
}
