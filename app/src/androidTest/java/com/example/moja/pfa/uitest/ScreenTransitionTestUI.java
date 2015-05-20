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

    public void testEnteringDataAndSearchForThemInOverviewScreen() {
        mySolo.waitForActivity("EnteringScreen");
        String amount = "12.12";
        String description = "unique";
        mySolo.enterText(1, amount);
        mySolo.enterText(3, description);
        mySolo.clickOnButton(0);
        mySolo.clickOnActionBarItem(1);
        mySolo.waitForActivity("DataOverviewScreen");
        boolean amountBool = mySolo.searchText(amount);
        boolean descriptionBool = mySolo.searchText(description);
        assert(amountBool && descriptionBool);

    }

    public void testEnteringDataAndDeleteThemInOverviewScreen() {
        mySolo.waitForActivity("EnteringScreen");
        String amount = "1992.12";
        String description = "unique22";
        mySolo.enterText(1, amount);
        mySolo.enterText(3, description);
        mySolo.clickOnButton(0);
        mySolo.clickOnActionBarItem(1);
        mySolo.waitForActivity("DataOverviewScreen");
        boolean amountBool = mySolo.searchText(amount);
        boolean descriptionBool = mySolo.searchText(description);
        mySolo.clickInList(1);
        mySolo.waitForActivity("EnteringScreen");
        mySolo.clickOnButton(1);
        mySolo.waitForActivity("DataOverviewScreen");
        boolean amountBool2 = mySolo.searchText(amount);
        boolean descriptionBool2 = mySolo.searchText(description);
        assert(amountBool && descriptionBool && !amountBool2 && !descriptionBool2);

    }


}