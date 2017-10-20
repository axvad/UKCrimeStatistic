package ru.sam.ukcrimestat.view;

import java.util.UUID;
import org.junit.Test;

import ru.sam.ukcrimestat.BuildConfig;
import ru.sam.ukcrimestat.view_tools.CategoryIcon;

import static org.junit.Assert.assertTrue;


/**
 * Testing CategoryIcon class
 */

public class TestCategoryIcon {

    @Test
    public void getIndexTest() throws Exception{

        for (int r = 0; r < 100; r++) {
            String key = UUID.randomUUID().toString();

            CategoryIcon icon = new CategoryIcon(null, key); //InstrumentationRegistry.getContext()

            if (BuildConfig.DEBUG) {
                System.out.printf("Key '%s' return index %d\n", key, icon.getIndex(key));
            }

            assertTrue(icon.getIndex(key)>=0 && icon.getIndex(key)<icon.getCountIcons());
        }
    }
}
