package com.recommendationsys.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By emailInput = By.xpath("//input[@placeholder='Электронная почта']");
    private By passwordInput = By.xpath("//input[@placeholder='Пароль']");
    private By loginButton = By.xpath("//button[contains(text(), 'ВОЙТИ В АККАУНТ')]");
    private By errorBlock = By.className("text-red-600");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get("http://localhost/login");
    }

     public void loginAs(String email, String password) {
        WebElement emailEl = wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput));
        emailEl.clear();
        emailEl.sendKeys(email);

        driver.findElement(passwordInput).sendKeys(password);
        
        WebElement btn = driver.findElement(loginButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);

        wait.until(webDriver -> ((JavascriptExecutor) webDriver)
            .executeScript("return localStorage.getItem('token')") != null);
    }

    public void loginWithInvalidData(String email, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput)).sendKeys(email);
        driver.findElement(passwordInput).sendKeys(password);
        
        WebElement btn = driver.findElement(loginButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);
    }

    public String getErrorMessage() {
        WebElement error = wait.until(ExpectedConditions.visibilityOfElementLocated(errorBlock));
        return error.getText();
    }
}