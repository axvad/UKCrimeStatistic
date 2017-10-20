package ru.sam.ukcrimestat.filters;

import java.util.List;

/**
 * Created by sam on 16.10.17.
 */

public interface FilterSelectListener {
    void onPeriodSelected(CrimePeriod selectedPeriod);
    void onFilterChoosed(List<CrimeCategory> result);

}
