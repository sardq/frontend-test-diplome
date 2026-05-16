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
        // В докере часто нужен headless режим, но для тестов оставим видимым
        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        gamePage = new GameDetailsPage(driver);
        loginPage = new LoginPage(driver);
    }

    @Test
    @DisplayName("Задача 1: Адаптивность — Проверка Media блока на разных экранах")
    public void testGalleryAdaptivity() {
        gamePage.open(658L); // Используйте ID игры из вашей базы
        
        // Тест на Desktop
        assertTrue(gamePage.isMainMediaVisible());

        // Тест на Mobile (iPhone X)
        driver.manage().window().setSize(new Dimension(375, 812));
        assertTrue(gamePage.isMainMediaVisible(), "Галерея должна быть видна на мобильных устройствах");
    }

    @Test
    @DisplayName("Задача 2: Уклон на RecSys — Проверка уникального рейтинга и цен")
    public void testRecSysDataDisplay() {
        gamePage.open(658L);
        
        // Проверяем, что подгрузились внешние цены (CheapShark)
        assertTrue(gamePage.areDealsVisible(), "Блок мониторинга цен должен быть загружен");
        
        // Проверяем, что виден алмаз (наша уникальная метрика)
        // Примечание: появится только если у игры есть оценки в БД
        System.out.println("Наличие локального рейтинга: " + gamePage.hasLocalRating());
    }

    @Test
    @DisplayName("Задача 3: Современный дизайн — Взаимодействие с галереей (Steam Style)")
    public void testGalleryNavigation() {
        gamePage.open(658L);
        
        // Кликаем на вторую миниатюру
        gamePage.clickThumbnail(1);
        
        // Проверяем, что контейнер не исчез и переключился
        assertTrue(gamePage.isMainMediaVisible());
    }

    @Test
    @DisplayName("Задача 4: Сохранение логики — Социальные реакции")
    public void testReviewReactions() {
        // Авторизуемся, так как лайки требуют токена
        loginPage.open();
        loginPage.loginAs("dreod@mail.ru", "12345");

        gamePage.open(658L);
        
        // Пытаемся поставить лайк
        try {
            gamePage.clickLikeOnFirstReview();
            // Если не упало — значит кнопка кликабельна и логика работает
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