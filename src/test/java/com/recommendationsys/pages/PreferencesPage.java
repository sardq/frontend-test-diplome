package com.recommendationsys.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class PreferencesPage {
    private WebDriver driver;
    private WebDriverWait wait;

    private By searchInput = By.xpath("//input[contains(@placeholder, 'Начните вводить')]");
    private By tagCards = By.xpath("//div[contains(@class, 'rounded-[2rem]') and contains(., 'Категория')]");
    private By starRatingContainer = By.xpath(".//div[contains(@class, 'cursor-pointer') and contains(., '★')]");
    private By selectedTagPills = By.xpath("//div[contains(@class, 'rounded-full') and contains(., '⭐')]");
    private By saveButton = By.xpath("//button[contains(text(), 'СОХРАНИТЬ')]");
    private By removeTagBtn = By.xpath(".//button[text()='×']");

    public PreferencesPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void open() {
        driver.get("http://localhost/preferences");
    }

    public void searchTag(String text) {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        input.clear();
        input.sendKeys(text);
        try { Thread.sleep(1000); } catch (InterruptedException e) {}
    }

    public void rateFirstTag(int stars) {
        WebElement firstCard = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(tagCards)).get(0);
        WebElement starsElement = firstCard.findElement(starRatingContainer);
        
        int width = starsElement.getSize().getWidth();
        int offset = (int) (width * (stars / 5.0));

        new Actions(driver)
            .moveToElement(starsElement, -width/2, 0) 
            .moveByOffset(offset, 0)                  
            .click()
            .perform();
    }

    public int getSelectedTagsCount() {
        return driver.findElements(selectedTagPills).size();
    }

    public void removeFirstSelectedTag() {
        WebElement firstPill = driver.findElements(selectedTagPills).get(0);
        firstPill.findElement(removeTagBtn).click();
    }

    public void clickSave() {
        driver.findElement(saveButton).click();
    }

    public boolean isSaveButtonEnabled() {
        return driver.findElement(saveButton).isEnabled();
    }
}