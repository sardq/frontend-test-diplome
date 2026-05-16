package com.recommendationsys.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class GameDetailsPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // --- Селекторы Header ---
    private By gameTitle = By.tagName("h1");
    private By localRating = By.xpath("//span[contains(text(), '💎')]");
    private By dealsBlock = By.xpath("//span[contains(., 'Лучшие цены')]");

    // --- Селекторы Media (Steam Style) ---
    private By activeMediaContainer = By.xpath("//div[contains(@class, 'h-[350px]')]"); // Главный экран
    private By mediaThumbnails = By.xpath("//div[contains(@class, 'overflow-x-auto')]/div"); // Лента миниатюр

    // --- Селекторы News ---
    private By newsCards = By.xpath("//a[contains(@href, 'http')]//h4");

    // --- Селекторы Reviews ---
    private By reactionButtons = By.xpath("//button[contains(., '👍') or contains(., '👎') or contains(., '😂')]");

    public GameDetailsPage(WebDriver driver) {
        this.driver = driver;
        // Увеличиваем ожидание до 15 секунд для Docker-среды
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void open(Long gameId) {
        driver.get("http://localhost/games/" + gameId);
    }

    public String getTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(gameTitle)).getText();
    }

    // --- Работа с Галереей ---
    public boolean isMainMediaVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(activeMediaContainer)).isDisplayed();
    }

    public void clickThumbnail(int index) {
        List<WebElement> thumbs = driver.findElements(mediaThumbnails);
        if (index < thumbs.size()) {
            thumbs.get(index).click();
        }
    }

    // --- Проверки RecSys и Данных ---
    public boolean hasLocalRating() {
        try {
            return driver.findElement(localRating).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean areDealsVisible() {
       try {
            // Ждем 3-5 секунд, пока блок цен появится и станет видимым на экране
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            
            // visibilityOfElementLocated — самый надежный способ проверить, что данные прогрузились
            wait.until(ExpectedConditions.visibilityOfElementLocated(dealsBlock));
            return true;
        } catch (TimeoutException e) {
            // Если за 5 секунд блок не появился — значит цен нет или бэкенд не ответил
            return false;
        }
    }

    public int getNewsCount() {
        return driver.findElements(newsCards).size();
    }

    // --- Социальные действия ---
    public void clickLikeOnFirstReview() {
        WebElement firstLike = wait.until(ExpectedConditions.elementToBeClickable(reactionButtons));
        firstLike.click();
    }
}