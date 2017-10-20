package ru.sam.ukcrimestat.view_tools;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.sam.ukcrimestat.My;
import ru.sam.ukcrimestat.R;
import ru.sam.ukcrimestat.filters.CrimeCategory;


/**
 * Created by sam on 16.10.17.
 */

public class CategoryIcon {

    public static int SIZE = 100;

    private List<Integer> icons_set;
    private Context context;
    private String crimeCategoryUrl;

    private static Map<String, Bitmap> cache = new HashMap<>();

    //// constructors
    public CategoryIcon(@NonNull Context context, @NonNull String crimeCategoryUrl) {
        init(context,crimeCategoryUrl);
    }

    public CategoryIcon(@NonNull Context context, @NonNull CrimeCategory crimeCategory) {
        init(context,crimeCategory.getUrl());

    }

    private void init(@NonNull Context context, @NonNull String crimeCategoryUrl){

        this.context = context;
        this.crimeCategoryUrl = crimeCategoryUrl;

        icons_set = new ArrayList<>();

        icons_set.add(R.drawable.marker_blue);
        icons_set.add(R.drawable.marker_blue_darker );
        icons_set.add(R.drawable.marker_dark );
        icons_set.add(R.drawable.marker_green);
        icons_set.add(R.drawable.marker_green_darker);
        icons_set.add(R.drawable.marker_orange);
        icons_set.add(R.drawable.marker_pink);
        icons_set.add(R.drawable.marker_red);
    }

    //// resource index
    public int getIndex(String crimeCategoryUrl) {
        return Math.abs(crimeCategoryUrl.hashCode()) % (icons_set.size());
    }

    private int getResource(){
        return icons_set.get(getIndex(this.crimeCategoryUrl));
    }

    public int getCountIcons() {
        return icons_set.size();
    }

    //// create Bitmaps
    public BitmapDescriptor getBitmapDescriptor() {

        return BitmapDescriptorFactory
                .fromBitmap(getBitmap(true));
    }

    /**
     * Create icon for CrimeCategiry by preset icon array
     * @param addLetter true - for add first letter off CrimeCategory into icon
     * @return marker as Bitmap
     */
    public Bitmap getBitmap(boolean addLetter) {

        Bitmap bitmap_cached = isCached(this.crimeCategoryUrl);

        if (bitmap_cached != null) {
            return bitmap_cached;
        }


        int height = SIZE;
        int width = SIZE;

        BitmapDrawable bitmapdraw=(BitmapDrawable) context.getApplicationContext()
                .getResources().getDrawable(this.getResource(), null);

        Bitmap b=bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);

        if (addLetter) {

            smallMarker = printText(smallMarker, SIZE, crimeCategoryUrl.substring(0,1));

        }

        toCache(this.crimeCategoryUrl,smallMarker);

        return smallMarker;
    }

    private Bitmap printText(Bitmap input, int bit_size, String gText) {

        gText = gText.toUpperCase();

        Bitmap bitmap = input;
        // BitmapFactory.decodeResource(resources, gResId);

        Resources resources = context.getResources();

        float scale = resources.getDisplayMetrics().density;

        android.graphics.Bitmap.Config bitmapConfig =
                bitmap.getConfig();

        // set default bitmap config if none
        if(bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }

        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);

        Canvas canvas = new Canvas(bitmap);

        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        // text color - #3D3D3D
        paint.setColor(Color.rgb(61, 61, 61));

        // text size in pixels
        paint.setTextSize((int) (16 * scale));

        // text shadow
        paint.setShadowLayer(1f, 0f, 1f, Color.BLUE);

        // draw text to the Canvas center
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = (bitmap.getWidth() - bounds.width())/2;
        int y = Math.round((bitmap.getHeight() + bounds.height())*2f/5);

        canvas.drawText(gText, x, y, paint);

        return bitmap;

    }

    //// Cache
    private Bitmap toCache(String key, Bitmap bitmap) {
        if (My.DEBUG){
            System.out.printf("Count cached bitmap %d\n", cache.size()+1);
        }
        return cache.put(key,bitmap);
    }

    @Nullable
    private Bitmap isCached(String key){
        if (cache.containsKey(key))
            return cache.get(key);
        else
            return null;
    }
}
