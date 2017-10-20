package ru.sam.ukcrimestat.model;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import ru.sam.ukcrimestat.DataModel;
import ru.sam.ukcrimestat.filters.CrimePeriod;

import static org.junit.Assert.assertEquals;

/**
 * Created by sam on 15.10.17.
 */

public class DataModelGetSetTest {
    DataModel model;

    @Before
    public void init(){
        model = DataModel.getInstance();
    }

    @After
    public void destroy(){
        model = null;
    }

    @Test
    public void getsetDate(){

        CrimePeriod period = new CrimePeriod("2016-01");

        model.setDate(period);

        assertEquals("2016-01", model.getDate().period());
    }

    @Test
    public void getsetAttributes(){
        model.setAttribute("key1", "val1");

        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("l1");
        arrayList.add("l2");
        arrayList.add("l3");

        model.setAttribute("key1", arrayList);

        assertEquals(3, ((ArrayList<String>) model.getAttribute("key1")).size());
        assertEquals(1, model.getCountAttr());
    }
}
