package ru.sam.ukcrimestat.model;

import org.junit.Test;

import ru.sam.ukcrimestat.filters.CrimePeriod;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by sam on 15.10.17.
 */

public class CrimePeriodTest {
    @Test
    public void parsing(){
        String period_str = "2017-08";

        CrimePeriod p = new CrimePeriod();

        assertTrue(p.set(period_str));
    }

    @Test
    public void get_as_string(){
        CrimePeriod p = new CrimePeriod();
        p.setYear(2017);
        p.setMonth(8);

        assertEquals("2017-08",p.period());
    }
}
