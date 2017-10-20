package ru.sam.ukcrimestat.view_tools;

import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import ru.sam.ukcrimestat.R;

/**
 * Visual target for search
 */

public class MapTarget {
    private Circle area = null;
    private Marker target = null;
    private LatLng position = null;
    private State state;

    enum State {HIDDEN, SHOWN, SEARCHING}

    // internal constants
    private static double AREA_SIZE = 1610.0;
    private static int TARGET_ICON_ID = R.drawable.target_dark_green;
    private static int AREA_COLOR_FILL = Color.argb(100,0,255,0);
    private static int AREA_COLOR_STROKE = Color.argb(200,0,255,0);
    private static int AREA_SEARCH_COLOR_FILL = Color.argb(100,255,64,129);
    private static int AREA_SEARCH_COLOR_STROKE = Color.argb(200,255,64,129);

    public MapTarget() {
        state = State.HIDDEN;
    }

    public void clearMarkers () {
        if (area != null) {
            area.remove();
            area = null;
        }

        if (target != null) {
            target.remove();
            target = null;
        }

        state = State.HIDDEN;
    }

    public State getState() {
        return this.state;
    }

    public boolean isHidden() {return this.state == State.HIDDEN;}
    public boolean isVisible() {return this.state != State.HIDDEN; }

    public MapTarget set(LatLng position) {
        this.position = position;

        return this;
    }

    public MapTarget show(GoogleMap map) {
        if (this.position == null) {
            Log.e(this.getClass().getSimpleName(), "Error show target at not defined position");
            return this;
        }

        updateArea(map, this.position);
        updateTarget(map, this.position);

        state = State.SHOWN;

        return this;
    }

    public MapTarget searching() {
        if (area != null) {
            area.setFillColor(AREA_SEARCH_COLOR_FILL);
            area.setStrokeColor(AREA_SEARCH_COLOR_STROKE);
            area.setVisible(true);
        }
        if (target != null) {
            target.setVisible(false);
        }

        state = State.SEARCHING;
        return this;
    }

    //hide target
    public void hide() {

        state = State.HIDDEN;

        if (area != null) area.setVisible(false);
        if (target!=null) target.setVisible(false);
    }

    //get target position
    public LatLng getLastPosition() {
        return this.position;
    }

    private void updateArea(GoogleMap mMap, LatLng pos) {
        if (area != null) {
            //move existing area
            area.setVisible(true);
            area.setCenter(pos);

        } else {
            area = mMap.addCircle(new CircleOptions()
                    .center(pos)
                    .radius(AREA_SIZE)
                    .strokeColor(AREA_COLOR_STROKE)
                    .fillColor(AREA_COLOR_FILL)
            );
        }

    }

    private void updateTarget(GoogleMap mMap, LatLng pos) {
        if (target != null) {
            //move and show
            target.setVisible(true);
            target.setPosition(pos);
        } else {
            target = mMap.addMarker(new MarkerOptions()
                    .position(pos)
                    .anchor(0.5f,0.5f)
                    .icon( BitmapDescriptorFactory
                                .fromResource(TARGET_ICON_ID)));
        }
    }
}
