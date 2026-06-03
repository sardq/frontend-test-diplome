package com.recommendationsys.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class FavoritesPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By searchInput = By.xpath("//input[@placeholder='Поиск по названию...']");
    private By tagSelect = By.xpath("//select");
    private By gameCards = By.cssSelector(".grid > div.cursor-pointer");
    private By emptyState = By.xpath("//h3[text()='Ничего не нашли...']");
    private By loadMoreBtn = By.xpath("//button[contains(text(), 'Показать больше')]");
    private By backToProfileBtn = By.xpath("//span[text()='В профиль']/..");

    public FavoritesPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get("http://localhost/favorites");
    }

    public void searchFor(String text) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        input.clear();
        input.sendKeys(text);
        try { Thread.sleep(800); } catch (InterruptedException e) {}
    }

    public void selectTag(String tagName) {
        wait.until(ExpectedConditions.textToBePresentInElementLocated(tagSelect, tagName));
        WebElement selectElement = driver.findElement(tagSelect);
        Select select = new Select(selectElement);
        select.selectByVisibleText(tagName);
        try { Thread.sleep(800); } catch (InterruptedException e) {}
    }

    public int getVisibleGamesCount() {
        return driver.findElements(gameCards).size();
    }

    public boolean isEmptyStateVisible() {
        try {
            return driver.findElement(emptyState).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public void clickLoadMore() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(loadMoreBtn));
        btn.click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(loadMoreBtn, "Показать больше"));
    }

    public void clickFirstGame() {
        wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(gameCards)).get(0).click();
    }

    public void goBackToProfile() {
        driver.findElement(backToProfileBtn).click();
    }
}