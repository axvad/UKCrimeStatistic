package ru.sam.ukcrimestat.filters;

import android.support.annotation.NonNull;

/**
 * Class provide Crime categories from PoliceUK Api
 */

public class CrimeCategory implements Comparable<CrimeCategory> {
    private String url;
    private String name;
    private Boolean isShow;

    public CrimeCategory() {
        isShow = true;
    }

    @Override
    public int compareTo(@NonNull CrimeCategory o) {
        return getUrl().compareTo( o.getUrl());
    }

    public Boolean getShow() {
        return isShow;
    }

    public void setShow(Boolean show) {
        isShow = show;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc(){
        return null;
    }
}
