package com.test.framework.pages;

import com.test.framework.annotations.PageObject;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.test.framework.constants.GoogleHomePageConstants.*;
import static com.test.framework.pages.By.BY_XPATH;
import static java.lang.String.format;

@PageObject
public class GoogleHomePage extends BasePage {
    @FindBy(name = "q")
    private WebElement searchTextbox;

    public void enterGoogleSearch() {
        hardWait();
        searchTextbox.sendKeys("Selenium");
        searchTextbox.sendKeys(Keys.RETURN);
    }

    public void sendKeysAndSearch(String keysToSend) {
        setTextField(BY_XPATH, SEARCH_INPUT, keysToSend);
        pressEnter(BY_XPATH, SEARCH_INPUT);
    }

    public void clickByLinkText(String linkText) {
        clickDisplayedElement(BY_XPATH, format(LINK_PATH, linkText));
    }

    public void clickByLinkTextConfirmed(String clickOn, String waitFor) {
        clickConfirmed(BY_XPATH, format(LINK_PATH, clickOn), format(BASIC_LINK_PATH, waitFor));
    }


}
