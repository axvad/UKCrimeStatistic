package ru.sam.ukcrimestat.filters;

import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * Created by sam on 15.10.17.
 */

public class CrimePeriod {

    private int year;
    private int month;

    public CrimePeriod() {

        GregorianCalendar date = new GregorianCalendar();
        date.add(GregorianCalendar.MONTH, -3);
        month = date.get(GregorianCalendar.MONTH)+1;
        year = date.get(GregorianCalendar.YEAR);
    }

    public CrimePeriod(int year, int month) {
        this.year = year;
        this.month = month;
    }

    public CrimePeriod(String period) {
        set(period);
    }

    public boolean set(String period) {
        try {
            year = Integer.valueOf(period.substring(0, 4));
            month = Integer.valueOf(period.substring(5, 7));

            return true;
        } catch (NumberFormatException ex) {
            System.out.printf("Error parse period '%s' to 'YYYY-MM'\n", period);
        }
        return false;
    }

    public String period() {
        return String.format(Locale.ENGLISH,"%04d-%02d", year, month);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CrimePeriod that = (CrimePeriod) o;

        if (year != that.year) return false;
        return month == that.month;

    }

    @Override
    public int hashCode() {
        int result = year;
        result = 31 * result + month;
        return result;
    }
}
