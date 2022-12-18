package com.test.framework.tests;

import com.test.framework.pages.AmazonHomePage;
import org.springframework.test.annotation.DirtiesContext;
import org.testng.annotations.Test;

public class PageUnloadErrorHandlingTest extends BaseSeleniumTest{

    @Test
    @DirtiesContext
    public void pageUnloadErrorHandlingScenario() {
        AmazonHomePage amazonHomePage = getPage(AmazonHomePage.class);
        amazonHomePage.launchURL("http://www.amazon.in");

        //The navigateToSignInPage() method visually demonstrates the processing of this scenario
        amazonHomePage.navigateToSignInPage();
    }
}
