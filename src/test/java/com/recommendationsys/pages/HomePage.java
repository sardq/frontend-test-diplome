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
        // Ждем 300мс (как в вашем коде useEffect) + время на запрос
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
        // Создаем быстрый таймер (например, ждем максимум 2 секунды)
        WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        
        // Ждем, пока внутри HTML-атрибута 'textContent' появится слово 'Анализируем'
        // Это заставит Selenium ждать те самые миллисекунды, пока React не обновит кнопку
        shortWait.until(ExpectedConditions.attributeContains(recalculateButtonActive, "textContent", "Анализируем"));
        return true;
        
    } catch (TimeoutException e) {
        // Если за 2 секунды текст так и не поменялся — возвращаем false
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
            // Ждем появления хотя бы одной карточки (до 10 секунд)
            wait.until(ExpectedConditions.presenceOfElementLocated(gameCards));
        } catch (TimeoutException e) {
            // Если за 10 секунд ничего не появилось — возвращаем 0
            return 0;
        }
        // Когда карточки прогрузились, считаем их количество
        return driver.findElements(gameCards).size();
    }

    public void clickFirstGame() {
        WebElement firstCard = wait.until(ExpectedConditions.presenceOfElementLocated(gameCardTitle));
    
    // 2. Ждем, пока он станет видимым (чтобы убедиться, что React закончил рендер)
    wait.until(ExpectedConditions.visibilityOf(firstCard));
    
    // 3. Делаем клик через JavaScript прямо по элементу, игнорируя любые CSS-анимации
    ((JavascriptExecutor) driver).executeScript("arguments[0].click();", firstCard);
    }
}
