package com.test.framework.pages;

import com.test.framework.exceptions.FrameworkRuntimeException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.test.framework.constants.BasePageConstants.DELAY;
import static com.test.framework.constants.BasePageConstants.TIMEOUT;
import static com.test.framework.constants.ExceptionConstants.ERR_ELM_NOT_FOUND;
import static com.test.framework.util.ResourceUtil.getProperty;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import static java.lang.String.format;
import static java.time.Duration.ofSeconds;
import static org.openqa.selenium.By.*;
import static org.openqa.selenium.Keys.DELETE;
import static org.openqa.selenium.Keys.ENTER;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfElementLocated;
import static org.testng.Assert.assertTrue;


@Slf4j
public class BasePage implements IWebDriverAware {
    @Autowired
    protected WebDriver webDriver;

    @Autowired
    private Actions actions;

    private JavascriptExecutor js;

    public void launchURL(String url) {
        webDriver.get(url);
        webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        webDriver.manage().window().maximize();
    }

    protected void hardWait() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public WebDriver getWebDriver() {
        return webDriver;
    }

    public WebElement findElement(String path, By by) {
        try {
            return webDriver.findElement(determineBy(by, path));
        } catch (Exception e) {
            throw new FrameworkRuntimeException(format(ERR_ELM_NOT_FOUND, path));
        }
    }

    public void setTextField(By by, String path, String value) {
        try {
            WebElement el = findElement(path, by);
            el.clear();
            if (StringUtils.isNotEmpty(value)) {
                el.sendKeys(value);
            } else {
                el.sendKeys(DELETE);
            }
        } catch (WebDriverException ignored) {
        } catch (FrameworkRuntimeException e) {
            log.info(format("Unable to set value '%s'", value), e);
        }
    }

    public void pressEnter(By by, String path) {
        try {
            WebElement el = findElement(path, by);
            el.sendKeys(ENTER);
        } catch (WebDriverException ignored) {
        }
    }

    public void clickElement(By by, String path) {
        clickElement(by, path, parseDouble(getProperty(DELAY)));
    }

    public void clickElement(By by, String path, double delay) {
        List<WebElement> els = findElements(by, path);
        if (els.isEmpty()) {
            throw new FrameworkRuntimeException(String.format(ERR_ELM_NOT_FOUND, path));
        }
        for (WebElement el : els) {
            try {
                el.click();
                delay(delay);
                break;
            } catch (WebDriverException ignored) {
            } catch (Exception e) {
                log.warn("Unable to click element", e);
            }
        }
    }

    public boolean clickDisplayedElement(By by, String path) {
        if (!isPresenceElementLocated(by, path)) {
            return false;
        }
        clickElement(by, getVisibleElement(by, path), 1);
        return true;
    }

    public boolean clickDisplayedElement(By by, String path, int wait) {
        waitElementPresent(by, path, wait);
        waitElementClickable(by, path);
        return clickDisplayedElement(by, path);
    }

    // Waits for presence of specified element as confirmation of successful click
    // retries 3 times before fail
    public void clickConfirmed(By by, String clickOn, String waitFor) {
        clickConfirmed(by, clickOn, waitFor, 3);
    }

    private void clickConfirmed(By by, String clickOn, String waitFor, int i) {
        try {
            actions.moveToElement(findElement(clickOn, by)).click().build().perform();
            new WebDriverWait(webDriver, ofSeconds(parseInt(getProperty(TIMEOUT))))
                    .until(presenceOfElementLocated(determineBy(by, waitFor)));
        } catch (StaleElementReferenceException e) {
            if (i > 0) {
                clickConfirmed(by, clickOn, waitFor, i--);
            } else {
                assertTrue(false, "Click failed - " + clickOn);
            }
        }
    }

    public void clickVisibleElement(By by, String xpath) {
        String xp = getVisibleElement(by, xpath);
        if (!xpath.equals(xp)) {
            clickElement(by, xp);
        } else {
            throw new FrameworkRuntimeException(String.format(ERR_ELM_NOT_FOUND, xpath));
        }
    }

    public List<WebElement> findElements(By by, String path) {
        if (webDriver == null) {
            return Collections.emptyList();
        }
        return webDriver.findElements(determineBy(by, path));
    }

    public static void delay(double sec) {
        try {
            Thread.sleep((long) (sec * 1000));
        } catch (Exception e) {
            log.error("Unexpected error", e);
        }
    }

    public boolean isPresenceElementLocated(By by, String path) {
        try {
            new WebDriverWait(webDriver, ofSeconds(5)).until(presenceOfElementLocated(determineBy(by, path)));
            return true;
        } catch (StaleElementReferenceException e) {
            log.error(String.format("Element %s='%s' not present", by, path), e);
        }
        return false;
    }

    public boolean waitElementPresent(By by, String path, int sec) {
        try {
            new WebDriverWait(webDriver, ofSeconds(sec))
                    .until(presenceOfElementLocated(determineBy(by, path)));
            return true;
        } catch (StaleElementReferenceException e) {
            log.debug("Element not displayed", e);
        }
        return false;
    }

    public void waitElementClickable(By by, String path) {
        try {
            new WebDriverWait(webDriver, ofSeconds(parseInt(getProperty(TIMEOUT))))
                    .until(elementToBeClickable(determineBy(by, path)));
        } catch (StaleElementReferenceException e) {
            log.debug("Element not clickable", e);
        }
    }

    public String getVisibleElement(By by, String path) {
        int cnt = elementsCount(by, path);
        for (int i = cnt; i >= 1; i--) {
            String xp = String.format("(%s)[%s]", path, i);
            if (isDisplayed(by, xp)) {
                return xp;
            }
        }
        return path;       // original will be returned
    }

    public int elementsCount(By by, String path) {
        return findElements(by, path).size();
    }

    public boolean setElementVisible(By by, String path) {
        js = (JavascriptExecutor) webDriver;
        try {
            waitElementPresent(by, path, 5);
            js.executeScript("arguments[0].scrollIntoView(true);", findElement(path, by));
            return true;
        } catch (Exception e) {
            log.debug("Unable to set element visible", e);
        }
        return false;
    }

    public boolean isDisplayed(By by, String path) {
        try {
            return findElement(path, by).isDisplayed();
        } catch (StaleElementReferenceException e) {
            log.debug("Element not displayed", e);
        }
        return false;
    }

    public void refresh() {
        try {
            closeAlert();
        } catch (Exception ignored) {
        }
        try {
            webDriver.navigate().refresh();
            acceptAlert();
            waitPageReady();
        } catch (Exception e) {
            log.warn("Unable to refresh Browser", e);
        }
    }

    public void waitPageReady() {
        js = (JavascriptExecutor) webDriver;
        if (webDriver == null) {
            return;
        }

        for (int i = 0; i < parseInt(getProperty(TIMEOUT)); i++) {
            try {
                if ("complete".equals(js.executeScript("return document.readyState"))) {
                    break;
                }
            } catch (Exception e) {
                log.warn("Unable to get page status", e);
                if (e.getMessage().toLowerCase().contains("timeout")) {
                    break;
                }
            }
            delay(1);
        }
    }

    public void acceptAlert() {
        try {
            webDriver.switchTo().alert().accept();
        } catch (Exception ignored) {
        }
    }

    public void switchToNextTab() {
        // First, we need to get the list of all tabs that are currently open in the browser
        List<String> browserTabs = new ArrayList<>(webDriver.getWindowHandles());

        // Then, we can get the handle of the current tab using the getWindowHandle() method
        String currentTab = webDriver.getWindowHandle();
        waitPageReady();

        // Then, we can find the index of the current tab by searching for its handle in the list of tabs
        int currentTabIndex = browserTabs.indexOf(currentTab);

        try {
            // Finally we are finished with the second tab, we can switch  to the next tab using the same technique
            webDriver.switchTo().window(browserTabs.get(currentTabIndex + 1));
        }catch (IndexOutOfBoundsException e){
            log.warn(String.format("No next open tab. Current tab index = %s", currentTabIndex), e);
        }
    }

    public void switchToPreviousTab() {
        // First, we need to get the list of all tabs that are currently open in the browser
        List<String> browserTabs = new ArrayList<>(webDriver.getWindowHandles());

        // Then, we can get the handle of the current tab using the getWindowHandle() method
        String currentTab = webDriver.getWindowHandle();
        waitPageReady();

        // Then, we can find the index of the current tab by searching for its handle in the list of tabs
        int currentTabIndex = browserTabs.indexOf(currentTab);

        try {
            // Finally we are finished with the second tab, we can switch back to the first tab using the same technique
            webDriver.switchTo().window(browserTabs.get(currentTabIndex - 1));
        }catch (IndexOutOfBoundsException e){
            log.warn(String.format("No previous open tab. Current tab index = %s", currentTabIndex), e);
        }
    }

    public void openNewTabByUrl(String url) {
        // First, we need to get the list of all tabs that are currently open in the browser
        List<String> browserTabs = new ArrayList<>(webDriver.getWindowHandles());

        // Then, we need to cast the WebDriver object to a JavascriptExecutor
         js = (JavascriptExecutor) webDriver;

        // Then, we can use the executeScript() method to execute JavaScript code that will open a new tab and navigate to the desired URL
        js.executeScript("window.open('" + url + "', '_blank');");

        // Then, we can get the handle of the current tab using the getWindowHandle() method
        String currentTab = webDriver.getWindowHandle();

        // Then, we can find the index of the current tab by searching for its handle in the list of tabs
        int currentTabIndex = browserTabs.indexOf(currentTab);

        try {
            // Finally we are finished with the second tab, we can switch  to the next tab using the same technique
            webDriver.switchTo().window(browserTabs.get(currentTabIndex + 1));
        }catch (IndexOutOfBoundsException e){
            log.warn(String.format("No next open tab. Current tab index = %s", currentTabIndex), e);
        }
    }

    public void closeAlert() {
        webDriver.switchTo().alert().dismiss();
    }

    private org.openqa.selenium.By determineBy(By by, String path) {
        switch (by) {
            case BY_XPATH:
                return xpath(path);
            case BY_ID:
                return id(path);
            case BY_CLASS_NAME:
                return className(path);
            case BY_NAME:
                return name(path);
            case BY_CSS_SELECTOR:
                return cssSelector(path);
            case BY_TAG_NAME:
                return tagName(path);
            case BY_LINK_TEXT:
                return linkText(path);
            case BY_PARTIAL_LINK_TEXT:
                return partialLinkText(path);
            default:
                throw new FrameworkRuntimeException("Somthing went wrong");
        }
    }

}
