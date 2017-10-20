package ru.sam.ukcrimestat;

import java.util.List;

import ru.sam.ukcrimestat.filters.CrimeCategory;


/**
 * Internal tools and settings application.
 * Some of this will be move to SharedPreferences
 */

public class My {
    //internal state for show logs
    public final static boolean DEBUG = false;

    //save previus request markers
    public static boolean SAVE_RESULT_RESPONSE_ON_SAME_DATE = true;

    //
    public static boolean SET_CAMERA_ON_SEARCH_RESULT = true;

    //get crimmecategory info
    public static String getCrimeListInfo(List<CrimeCategory> list) {

        int positive = 0;

        for (CrimeCategory ct:list) {
            positive += ct.getShow() ? 1:0;
        }

        return String.format("CrimeCategory: total=%d,shown=%d {%s}", list.size(), positive, list);
    }

}
