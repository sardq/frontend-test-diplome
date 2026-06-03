package com.recommendationsys.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class GameDetailsPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By gameTitle = By.tagName("h1");
    private By localRating = By.xpath("//span[contains(text(), '💎')]");
    private By dealsBlock = By.xpath("//span[contains(., 'Лучшие цены')]");

    private By activeMediaContainer = By.xpath("//div[contains(@class, 'h-[350px]')]"); // Главный экран
    private By mediaThumbnails = By.xpath("//div[contains(@class, 'overflow-x-auto')]/div"); // Лента миниатюр

    private By newsCards = By.xpath("//a[contains(@href, 'http')]//h4");

    private By reactionButtons = By.xpath("//button[contains(., '👍') or contains(., '👎') or contains(., '😂')]");

    public GameDetailsPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(30));
    }

    public void open(Long gameId) {
        driver.get("http://localhost/games/" + gameId);
    }

    public String getTitle() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(gameTitle)).getText();
    }

    public boolean isMainMediaVisible() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(activeMediaContainer)).isDisplayed();
    }

    public void clickThumbnail(int index) {
        List<WebElement> thumbs = driver.findElements(mediaThumbnails);
        if (index < thumbs.size()) {
            thumbs.get(index).click();
        }
    }

    public boolean hasLocalRating() {
        try {
            return driver.findElement(localRating).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean areDealsVisible() {
       try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            wait.until(ExpectedConditions.visibilityOfElementLocated(dealsBlock));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public int getNewsCount() {
        return driver.findElements(newsCards).size();
    }

    public void clickLikeOnFirstReview() {
        WebElement firstLike = wait.until(ExpectedConditions.elementToBeClickable(reactionButtons));
        firstLike.click();
    }
}