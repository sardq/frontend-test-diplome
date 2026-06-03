package com.recommendationsys.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class RegisterPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By emailInput = By.xpath("//input[@placeholder='Электронная почта']");
    private By usernameInput = By.xpath("//input[@placeholder='Ваш никнейм']");
    private By passwordInput = By.xpath("//input[@placeholder='Придумайте пароль']");
    private By registerButton = By.xpath("//button[contains(text(), 'ЗАРЕГИСТРИРОВАТЬСЯ')]");

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get("http://localhost/register");
    }

   public void registerAs(String email, String username, String password) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(emailInput)).sendKeys(email);
        driver.findElement(usernameInput).sendKeys(username);
        driver.findElement(passwordInput).sendKeys(password);
        
        WebElement btn = driver.findElement(registerButton);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", btn);

        wait.until(ExpectedConditions.urlToBe("http://localhost/"));
    }
}