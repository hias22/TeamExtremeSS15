package com.example.moja.pfa.uitest;

import android.test.ActivityInstrumentationTestCase2;

import com.example.moja.pfa.EnteringScreen;
import com.robotium.solo.Solo;

import junit.framework.TestCase;

/**
 * Created by Mathias on 02.05.2015.
 */
public class EnteringScreenTestUI extends ActivityInstrumentationTestCase2 {

    private Solo mySolo;

    public EnteringScreenTestUI() {
        super(EnteringScreen.class);
    }

    public void setUp() throws Exception {
        super.setUp();
        mySolo = new Solo(getInstrumentation(), getActivity());

    }

    public void tearDown() throws Exception {

    }

    public void testenterValidInputInTextfields() {
        mySolo.enterText(1, "12.12");
        mySolo.enterText(3, "Spar");
    }

}