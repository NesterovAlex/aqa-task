package com.test.framework.tests;

import com.test.framework.pages.AmazonHomePage;
import com.test.framework.pages.GoogleHomePage;
import org.openqa.selenium.StaleElementReferenceException;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.annotations.Test;

import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.fail;

public class StaleElementReferenceExceptionTest extends BaseSeleniumTest {

    @Test
    @DirtiesContext
    public void testGoogleSample() {
        GoogleHomePage googleHomePage = getPage(GoogleHomePage.class);
        googleHomePage.launchURL("https://www.google.com.ua/");
        googleHomePage.enterGoogleSearch();
    }

    /*
     * One possible way to handle StaleElementReferenceException is manage the Stale Element Reference Exception
     * by implementing methods from the ExpectedConditions class. The chain of method calls
     * clickByLinkText() -> DriverHelper.clickDisplayedElement() -> DriverHelper.isPresenceElementLocated()
     * clearly demonstrates the use of this approach
     */
    @Test
    @DirtiesContext
    void expectedConditions_presenceOfElementLocatedScenario_thenExpectedResultReturned() {
        GoogleHomePage googleHomePage = getPage(GoogleHomePage.class);
        googleHomePage.launchURL("https://www.google.com.ua/");
        String linkText = "Setup Selenium with Python and Chrome on Ubuntu & Debian";
        String keyWord = "selenium install ubuntu python";
        googleHomePage.sendKeysAndSearch(keyWord);
        String expected = "selenium install ubuntu python - Пошук Google";
        String actual = googleHomePage.getWebDriver().getTitle();

        assertEquals(expected, actual);

        googleHomePage.clickByLinkText(linkText);
    }


    /*
     * Another way is to mix the implementation of methods from the ExpectedConditions class and
     * a try-catch block in a loop that searches for the desired element, checking if the expected one appeared after a successful click.
     */

    @Test
    void retryUsingLoopScenario_thenExpectedResultReturned() {
        GoogleHomePage googleHomePage = getPage(GoogleHomePage.class);
        googleHomePage.launchURL("https://www.google.com.ua/");
        String clickOn = "Setup Selenium with Python and Chrome on Ubuntu & Debian";
        String waitFor = "Tutorials";
        String keyWord = "selenium install ubuntu python";
        googleHomePage.sendKeysAndSearch(keyWord);
        String expected = "selenium install ubuntu python - Пошук Google";
        String actual = googleHomePage.getWebDriver().getTitle();

        assertEquals(expected, actual);

        googleHomePage.clickByLinkTextConfirmed(clickOn, waitFor);
    }

    /*
     * Another way to avoid a stale reference is to create a decorator class for the WebElement object that overrides all
     * of its methods wrapped in the retry() method. The retry() itself contains the waitForElement() method, which
     * contains processing using explicit waits. enterAmazonSearchWithWebElementDecorator() demonstrates the implementation of this approach.
     */

    @Test
    @DirtiesContext
    public void retryUsingWebElementDecoratorLoopScenario() {
        AmazonHomePage amazonHomePage = getPage(AmazonHomePage.class);
        amazonHomePage.launchURL("http://www.amazon.in");
        try {
            amazonHomePage.enterAmazonSearch();
            fail();
        } catch (StaleElementReferenceException e) {
            amazonHomePage.enterAmazonSearchWithWebElementDecorator();
        }
    }
}
