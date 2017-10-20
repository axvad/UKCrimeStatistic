package ru.sam.ukcrimestat.apipolice;

/**
 * Created by sam on 13.10.17.
 */

public class CrimeEvent {

    private long id;
    private String category;
    private String location_type;
    private CrimeLocation location;
    private String month;

    public CrimeEvent() {
        this.location = new CrimeLocation();
    }

    @Override
    public String toString() {
        return "CrimeEvent{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", location_type='" + location_type + '\'' +
                ", location=" + location +
                ", month='" + month + '\'' +
                '}';
    }

    public CrimeLocation getLocation() {
        return location;
    }

    public void setLocation(CrimeLocation location) {
        this.location = location;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation_type() {
        return location_type;
    }

    public void setLocation_type(String location_type) {
        this.location_type = location_type;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }


}
