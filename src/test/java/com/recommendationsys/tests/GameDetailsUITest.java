package com.recommendationsys.tests;

import com.recommendationsys.pages.GameDetailsPage;
import com.recommendationsys.pages.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.*;

public class GameDetailsUITest {
    private WebDriver driver;
    private GameDetailsPage gamePage;
    private LoginPage loginPage;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        gamePage = new GameDetailsPage(driver);
        loginPage = new LoginPage(driver);
    }

    @Test
    public void testGalleryAdaptivity() {
        gamePage.open(658L);
        
        assertTrue(gamePage.isMainMediaVisible());

        driver.manage().window().setSize(new Dimension(375, 812));
        assertTrue(gamePage.isMainMediaVisible(), "Галерея должна быть видна на мобильных устройствах");
    }

    @Test
    public void testRecSysDataDisplay() {
        gamePage.open(658L);
        
        assertTrue(gamePage.areDealsVisible(), "Блок мониторинга цен должен быть загружен");
        
        System.out.println("Наличие локального рейтинга: " + gamePage.hasLocalRating());
    }

    @Test
    public void testGalleryNavigation() {
        gamePage.open(658L);
        
        gamePage.clickThumbnail(1);
        
        assertTrue(gamePage.isMainMediaVisible());
    }

    @Test
    public void testReviewReactions() {
        loginPage.open();
        loginPage.loginAs("dreod@mail.ru", "12345");

        gamePage.open(658L);
        
        try {
            gamePage.clickLikeOnFirstReview();
        } catch (Exception e) {
            System.out.println("Отзывы отсутствуют, тест лайка пропущен");
        }
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}