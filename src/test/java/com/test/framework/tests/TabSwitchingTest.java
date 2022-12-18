package com.test.framework.tests;

import com.test.framework.pages.GoogleHomePage;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;

public class TabSwitchingTest extends BaseSeleniumTest {


    @Test
    @DirtiesContext
    public void testTabSwitching() {
        GoogleHomePage googleHomePage = getPage(GoogleHomePage.class);
        googleHomePage.launchURL("https://www.google.com.ua/");
        //Open a new tab by url
        googleHomePage.openNewTabByUrl("https://www.google.com.ua/");
        //Switching to a new tab
        googleHomePage.switchToNextTab();
        //Performing actions on the tab you have switched to
        googleHomePage.enterGoogleSearch();
        //Checking the expected result
        assertEquals("Selenium - Пошук Google", googleHomePage.getWebDriver().getTitle());
    }

    @Test
    @DirtiesContext
    public void testTabSwitchingExtended() {
        GoogleHomePage googleHomePage = getPage(GoogleHomePage.class);
        googleHomePage.launchURL("https://www.google.com.ua/");
        //Open a new tab by url
        googleHomePage.openNewTabByUrl("https://www.google.com.ua/");
        //Switch to the previous tab (Detailed switch code description in switchToPreviousTab())
        googleHomePage.switchToNextTab();
        //Performing actions on the tab you have switched to
        googleHomePage.enterGoogleSearch();
        //Checking the expected result
        String expected = "Selenium - Пошук Google";
        String actual = googleHomePage.getWebDriver().getTitle();
        assertEquals(expected, actual);
        //Switch to the previous tab (Detailed switch code description in switchToPreviousTab())
        googleHomePage.switchToPreviousTab();
        //Performing actions on the tab you have switched to
        googleHomePage.enterGoogleSearch();
        //Checking the expected result
        assertEquals(expected, actual);
    }
}
