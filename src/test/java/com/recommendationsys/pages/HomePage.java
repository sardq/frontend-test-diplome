package com.recommendationsys.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class HomePage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By searchInput = By.xpath("//input[contains(@placeholder, 'Поиск')]");
    private By tagFilterButton = By.xpath("//button[text()='Жанры и теги']");
    private By recalculateButton = By.xpath("//button[contains(text(), 'Обновить подборку')]");
    private By recalculateButtonActive = By.xpath("//button[contains(text(), 'Анализируем данные...')]");
    private By surveyBanner = By.xpath("//h2[text()='Персонализируй это!']");
    private By recommendationsSection = By.xpath("//h2[contains(text(), 'Специально для тебя')]");
    private By gameCards = By.cssSelector(".group"); 
        private By gameCardTitle = By.xpath("//div[contains(@class, 'group')]//h3");


    public HomePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get("http://localhost/");
    }

    public void searchText(String text) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        input.clear();
        input.sendKeys(text);
        try { Thread.sleep(1000); }
        catch (InterruptedException e){
         e.printStackTrace();}
    }

    public void openTagFilter() {
        driver.findElement(tagFilterButton).click();
    }

    public void clickRecalculate() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(recalculateButton));
        btn.click();
    }

    public boolean isRecalculating() {
        try {
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        
        shortWait.until(ExpectedConditions.attributeContains(recalculateButtonActive, "textContent", "Анализируем"));
        return true;
        
    } catch (TimeoutException e) {
        return false;
    }
    }
    public void waitForGamesToLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(gameCards));
    }
    public boolean isRecommendationsVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(recommendationsSection)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSurveyBannerVisible() {
        try {
            return driver.findElement(surveyBanner).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public int getGameCardsCount() {
        try {
            wait.until(ExpectedConditions.presenceOfElementLocated(gameCards));
        } catch (TimeoutException e) {
            return 0;
        }
        return driver.findElements(gameCards).size();
    }

    public void clickFirstGame() {
        WebElement firstCard = wait.until(ExpectedConditions.presenceOfElementLocated(gameCardTitle));
    
    wait.until(ExpectedConditions.visibilityOf(firstCard));
    
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstCard);
    }
}
