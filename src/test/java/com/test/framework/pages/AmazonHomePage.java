package com.test.framework.pages;

import com.test.framework.annotations.PageObject;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import static com.test.framework.constants.AmazonHomePageConstants.SIGN_IN_BUTTON_PATH;
import static java.time.Duration.ofSeconds;

@Slf4j
@PageObject
public class AmazonHomePage extends BasePage {
    private WebElement amazonSearchTextbox;

    public void enterAmazonSearch() {
        hardWait();
        WebDriver webDriver = getWebDriver();
        amazonSearchTextbox = webDriver.findElement(By.id("twotabsearchtextbox"));

        // make the search ref stale
        webDriver.navigate().refresh();
        amazonSearchTextbox.sendKeys("Computer");
        amazonSearchTextbox.sendKeys(Keys.RETURN);
    }

    public void enterAmazonSearchWithWebElementDecorator() {
        hardWait();
        WebDriver webDriver = getWebDriver();
        amazonSearchTextbox = webDriver.findElement(By.id("twotabsearchtextbox"));

        amazonSearchTextbox = new WebElementDecorator(amazonSearchTextbox, webDriver, By.id("twotabsearchtextbox"));
        // make the search ref stale
        webDriver.navigate().refresh();
        amazonSearchTextbox.sendKeys("Computer");
        amazonSearchTextbox.sendKeys(Keys.RETURN);
    }

    public void navigateToSignInPage() {
        // Get the WebDriver object and navigate to the desired page
        WebDriver webDriver = getWebDriver();

        // Find the button that will redirect to a new page and click it
        WebElement button = webDriver.findElement(By.xpath(SIGN_IN_BUTTON_PATH));

        try {
            // Define a WebDriverWait object and specify the maximum amount of time to wait for the element to become stale
            WebDriverWait wait = new WebDriverWait(webDriver, ofSeconds(10));
            button.click();
            // Wait until the element with the ID "redirect-button" is no longer attached to the DOM
            wait.until(ExpectedConditions.stalenessOf(button));
        } catch (TimeoutException e) {
            // If the element is still attached to the DOM after the specified amount of time, a TimeoutException will be thrown
            // In this case, we can gracefully handle the error and take appropriate action
            // For example, you could log the error or display an error message to the user
            log.warn("The page did not unload within the specified time");
        }
        // At this point, the page has either finished unloading or a TimeoutException was thrown
    }

}
