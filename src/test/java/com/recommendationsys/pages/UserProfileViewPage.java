package com.recommendationsys.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class UserProfileViewPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Селекторы
    private By backButton = By.xpath("//button[contains(text(), 'Назад')]");
    private By usernameHeading = By.xpath("//h2[contains(@class, 'font-black') and contains(@class, 'text-3xl')]");
    private By userStatusBadge = By.xpath("//span[contains(text(), 'Игрок сообщества')]");
    
    // Секция отзывов
    private By reviewCards = By.xpath("//div[contains(@class, 'cursor-pointer') and .//p[contains(@class, 'italic')]]");
    private By loadMoreBtn = By.xpath("//button[contains(text(), 'Смотреть ещё отзывы')]");

    public UserProfileViewPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Открытие профиля конкретного юзера (например, ID 1)
    public void open(Long userId) {
        driver.get("http://localhost/user/" + userId);
    }

    public String getUsername() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(usernameHeading)).getText();
    }

    public boolean isUserStatusVisible() {
        return driver.findElement(userStatusBadge).isDisplayed();
    }

    public int getReviewsCount() {
        // Ждем, пока загрузится хотя бы один отзыв (если они есть)
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(reviewCards));
            return driver.findElements(reviewCards).size();
        } catch (TimeoutException e) {
            return 0; // Отзывов нет
        }
    }

    public void clickLoadMore() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(loadMoreBtn));
        btn.click();
        // Ждем завершения анимации загрузки (текст кнопки меняется)
        wait.until(ExpectedConditions.textToBePresentInElementLocated(loadMoreBtn, "Смотреть ещё отзывы"));
    }

   public void clickOnFirstReviewGame() {
    try {
        // 1. Ждем, пока хотя бы одна карточка появится в DOM
        WebElement firstReview = wait.until(ExpectedConditions.presenceOfElementLocated(reviewCards));
        
        // 2. Дополнительно ждем, чтобы она стала видимой (чтобы React закончил отрисовку)
        wait.until(ExpectedConditions.visibilityOf(firstReview));
        
        // 3. Кликаем через JavaScript
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstReview);
        
    } catch (TimeoutException e) {
        System.out.println("Карточки отзывов не загрузились вовремя");
    }
}

    public void goBack() {
        driver.findElement(backButton).click();
    }
}