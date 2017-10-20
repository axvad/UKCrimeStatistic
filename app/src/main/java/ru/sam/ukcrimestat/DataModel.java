package ru.sam.ukcrimestat;

import android.os.Build;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ru.sam.ukcrimestat.filters.CrimeCategory;
import ru.sam.ukcrimestat.filters.CrimePeriod;
import ru.sam.ukcrimestat.apipolice.CrimeEvent;


/**
 * Provide model as data storage for application
 */
public class DataModel {

    private static final DataModel ourInstance = new DataModel();

    //<<< named objects mambers
    private Map<Long, CrimeEvent> crimeEvents = null;
    private Map<String, CrimeCategory> filterCategories = null;
    private CrimePeriod period = null;

    //<<< unnamed mambers
    private Map<String, Object> attributes = null;

    static final String MAP_STATE = "CameraStateOfMap";
    static final String POS_LAST_REQUEST = "last_position";

    // Singleton constructor & cleaner
    public static DataModel getInstance() {

        return ourInstance;
    }

    private DataModel() {
        setDate(new CrimePeriod());
    }

    void destroy(){
        //clear();
    }

    private void clear(){
        if (crimeEvents != null)
            crimeEvents.clear();
    }

    //<<<<<<<<< Common methods <<<<<<<<<<<<<<<<<<

    boolean hasSomeData() {
        return filterCategories!=null &&  filterCategories.size()>0;
    }

    //<<<<<<<<< CrimeEvents <<<<<<<<<<<<<<<<<<<<<

    Collection<CrimeEvent> getCrimeEvents() {

        if (crimeEvents != null) {

            if (filterCategories == null){
                return crimeEvents.values();
            }

            List<String> shownCategories = new ArrayList<>();

            for (CrimeCategory cat:filterCategories.values()){
                if (cat.getShow()) {
                    shownCategories.add(cat.getUrl());
                }
            }

            Collection<CrimeEvent> events = new ArrayList<>();

            for (CrimeEvent ev:crimeEvents.values()) {
                if (shownCategories.contains(ev.getCategory())) {
                    events.add(ev);
                }
            }

            return events;

        } else {
            return null;
        }
    }

    void setCrimeEvents(Collection<CrimeEvent> crimeEvents) {

        if (this.crimeEvents == null) {
            this.crimeEvents = new HashMap<>();
        }

        if(crimeEvents == null) {
            return;
        }

        if (crimeEvents.size() > 10000 || !My.SAVE_RESULT_RESPONSE_ON_SAME_DATE)
            this.clear();

        for (CrimeEvent ev:crimeEvents){
            this.crimeEvents.put(ev.getId(),ev);
        }
    }

    //<<<<<<<<< Crime Category <<<<<<<<<<<<<<<<<<

    public List<CrimeCategory> getFilterCategories() {

        if (filterCategories != null) {

            Collection<CrimeCategory> res = filterCategories.values();

            List<CrimeCategory> categories = null;

            //check verion for use java8
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                categories = res.stream().collect(Collectors.toList());
                categories.sort((a,b)->a.getUrl().compareTo(b.getUrl()));
                categories.removeIf(v->v.getUrl().startsWith("all-"));

            } else {
                categories = new ArrayList<>(Arrays.asList(res.toArray(new CrimeCategory[] {})));

                Collections.sort(categories);

                for (Iterator<CrimeCategory> it = categories.iterator(); it.hasNext();) {

                    CrimeCategory crime = it.next();

                    if (crime.getUrl().startsWith("all-")) {
                        it.remove();
                        break;
                    }
                }
            }


            return categories;

        } else {
            return null;
        }
    }

    void updateCrimeCategories(List<CrimeCategory> filterCategories, boolean needForUpdateFilter) {

        if (this.filterCategories == null){
            this.filterCategories = new HashMap<>();
        }

        if(filterCategories == null)
            return;

        for (CrimeCategory category:filterCategories){

            if (!needForUpdateFilter && this.filterCategories.containsKey(category.getUrl())){
                continue;
            }

            this.filterCategories.put(category.getUrl(), category);
        }
    }

    //<<<<<<<<< Crime period <<<<<<<<<<<<<<<<<<<<

    public boolean setDate(CrimePeriod date){

        if(date == null || date.equals(period)) return false;

        period = date;

        clear();

        return true;
    }

    public CrimePeriod getDate(){
        return period;
    }

    //<<<<<<<<< Other Attributes <<<<<<<<<<<<<<<<

    public int getCountAttr() {
        return (attributes == null) ? 0 : attributes.size();
    }

    public void setAttribute(String key, Object value){

        if (attributes == null) {
            attributes = new HashMap<>();
        }

        attributes.put(key,value);
    }

    public Object getAttribute(String key) {

        if (attributes == null){
            return null;
        }

        return attributes.get(key);
    }

    Object getAttribute(String key, @NonNull Object returnByDefault){

        Object res = getAttribute(key);

        return (res != null) ? res : returnByDefault;
    }

}
