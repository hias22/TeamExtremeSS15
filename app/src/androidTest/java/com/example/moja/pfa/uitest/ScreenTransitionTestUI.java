package com.example.moja.pfa.uitest;

import android.test.ActivityInstrumentationTestCase2;

import com.example.moja.pfa.EnteringScreen;
import com.robotium.solo.Solo;

/**
 * Created by Mathias on 02.05.2015.
 */
public class ScreenTransitionTestUI extends ActivityInstrumentationTestCase2 {

    private Solo mySolo;

    public ScreenTransitionTestUI() {
        super(EnteringScreen.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        mySolo = new Solo(getInstrumentation(), getActivity());

    }

    public void tearDown() throws Exception {

    }

    public void testEnterDataOverviewScreenAndReturnViaActionBar() {
        mySolo.clickOnActionBarItem(1);
        mySolo.sleep(5000);
        mySolo.clickOnActionBarItem(1);
    }

}