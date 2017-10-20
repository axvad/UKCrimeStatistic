package ru.sam.ukcrimestat.apipolice;

import java.util.List;

/**
 * Created by sam on 13.10.17.
 */

public class AvailableForceID {
    private String date;
    private List<String> stop_and_search;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<String> getStop_and_search() {
        return stop_and_search;
    }

    public void setStop_and_search(List<String> stop_and_search) {
        this.stop_and_search = stop_and_search;
    }
}
