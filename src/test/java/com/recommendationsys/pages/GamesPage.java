package com.recommendationsys.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class GamesPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Селекторы фильтров
    private By tagSearchInput = By.xpath("//input[@placeholder='Быстрый поиск по тегам...']");
    private By popularPill = By.xpath("//span[contains(text(), 'ПОПУЛЯРНОЕ')]");
    private By showAllTagsBtn = By.xpath("//button[contains(text(), 'Показать все категории')]");
    
    // Селекторы списка игр
    private By pageHeader = By.tagName("h2");
    private By gameCards = By.xpath("//div[contains(@class, 'group') and contains(@class, 'cursor-pointer')]");
    private By gameCardTitle = By.xpath("//div[contains(@class, 'group')]//h3");
    private By gameTitles = By.xpath("//h3");
    private By loadMoreBtn = By.xpath("//button[text()='Загрузить ещё']");
    private By gameTitleLocator = By.xpath("//h3");
    private By headerLocator = By.xpath("//h2");
    public GamesPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get("http://localhost/games");
    }
    public void waitForGamesToLoad() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(gameTitleLocator));
    }
     public void waitForHeaderToContain(String expectedText) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(headerLocator, expectedText));
    }

    // Поиск тега в фильтре
    public void searchTag(String text) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(tagSearchInput));
        input.clear();
        input.sendKeys(text);
        // Задержка 300мс из кода (debounce) + время на запрос
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
    }

    // Выбор тега по его названию
    public void clickTagByName(String tagName) {
        By specificTag = By.xpath("//span[contains(text(), '" + tagName + "')]");
        wait.until(ExpectedConditions.elementToBeClickable(specificTag)).click();
    }

    public void clickPopular() {
        driver.findElement(popularPill).click();
    }

    public void toggleAllTags() {
        driver.findElement(showAllTagsBtn).click();
    }

    public String getHeaderText() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(pageHeader)).getText();
    }

    public int getGamesCount() {
        return driver.findElements(gameCards).size();
    }

    public List<String> getAllVisibleTitles() {
        return driver.findElements(gameTitles).stream()
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    public void clickLoadMore() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(loadMoreBtn));
        btn.click();
        // Ждем, пока индикатор "Загрузка..." сменится обратно
        wait.until(ExpectedConditions.textToBePresentInElementLocated(loadMoreBtn, "Загрузить ещё"));
    }

    public void clickFirstGame() {
        WebElement firstGame = wait.until(ExpectedConditions.elementToBeClickable(gameCardTitle));
    
    // 2. Кликаем по заголовку
    firstGame.click();
    }
}