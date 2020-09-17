package com.kml;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.kml.aGlobalUses.FileFactory;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;


@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        FileFactory fileFactory = new FileFactory(appContext);
        assertNotEquals("",fileFactory.readFromFile(FileFactory.PROFILE_KEEP_DATA_TXT));
    }
}
