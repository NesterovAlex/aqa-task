package com.test.framework.tests.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.SimpleThreadScope;
import org.springframework.test.context.TestPropertySource;

import static com.test.framework.util.ResourceUtil.getProperty;

@Slf4j
@Configuration
@ComponentScan(basePackages = "com.test.framework")
@TestPropertySource("classpath:localhost.properties")
public class TestConfig {

    @Bean
    public CustomScopeConfigurer newCustomScopeConfigurer() {
        CustomScopeConfigurer configurer = new CustomScopeConfigurer();
        SimpleThreadScope threadScope = new SimpleThreadScope();
        configurer.addScope("thread", threadScope);
        return configurer;
    }

    @Bean
    @Scope(scopeName = "thread")
    public WebDriver getDriver() {
        WebDriverManager.chromedriver().setup();
        WebDriver webDriver = WebDriverFactory.createWebDriver(getProperty("driver"));
        return webDriver;
    }

    @Bean
    @Scope(scopeName = "prototype")
    public Actions actions(WebDriver webDriver) {
        return new Actions(webDriver);
    }

}
