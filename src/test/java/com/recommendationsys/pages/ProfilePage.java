package com.recommendationsys.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ProfilePage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Селекторы
    private By usernameText = By.xpath("//h2[contains(@class, 'text-3xl')]");
    
    // Блок даты рождения
    private By editBirthDateBtn = By.xpath("//button[text()='Изменить']");
    private By dateInput = By.xpath("//input[@type='date']");
    private By saveDateBtn = By.xpath("//button[text()='ОК']");
    private By birthDateDisplay = By.xpath("//span[text()='Дата рождения']/following-sibling::div/p");

    // Аватар
    private By avatarInput = By.id("avatarInput");

    // Списки
    private By reviewCards = By.xpath("//span[contains(@class, 'text-purple-600')]/ancestor::div[contains(@class, 'bg-white')]");
    private By loadMoreReviewsBtn = By.xpath("//button[text()='Загрузить ещё']");

    public ProfilePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get("http://localhost/profile");
    }

    public String getUsername() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(usernameText)).getText();
    }

    // Изменение даты рождения
    public void changeBirthDate(String date) {
        wait.until(ExpectedConditions.elementToBeClickable(editBirthDateBtn)).click();
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(dateInput));
        input.sendKeys(date); // Формат зависит от браузера, обычно "ddmmyyyy"
        driver.findElement(saveDateBtn).click();
        // Ждем, пока исчезнет инпут (значит сохранение прошло)
        wait.until(ExpectedConditions.invisibilityOfElementLocated(dateInput));
    }

    public String getBirthDateValue() {
        return driver.findElement(birthDateDisplay).getText();
    }

    // Загрузка аватара
    public void uploadAvatar(String absolutePath) {
        // В Selenium для загрузки файлов просто шлем путь в невидимый input[type='file']
        WebElement input = driver.findElement(avatarInput);
        ((JavascriptExecutor) driver).executeScript("arguments[0].style.display='block';", input);
        input.sendKeys(absolutePath);
        // Ждем появления alert об успешной загрузке
        wait.until(ExpectedConditions.alertIsPresent()).accept();
    }

    public int getReviewsCount() {
        return driver.findElements(reviewCards).size();
    }

    public void clickLoadMoreReviews() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(loadMoreReviewsBtn));
        btn.click();
    }
}