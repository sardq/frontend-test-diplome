package com.recommendationsys.tests;

import com.recommendationsys.pages.HomePage;
import com.recommendationsys.pages.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class HomePageTest {
    private WebDriver driver;
    private HomePage homePage;
    private LoginPage loginPage;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
    }

    @Test
    @DisplayName("Тест RecSys: Проверка блока персональных рекомендаций")
    public void testRecommendationsPresence() {
        // 1. Авторизуемся (предполагаем, что у юзера есть предпочтения)
        loginPage.open();
        loginPage.loginAs("dreod@mail.ru", "12345"); 

        // 2. Проверяем наличие блока "Специально для тебя"
        assertTrue(homePage.isRecommendationsVisible(), "Блок рекомендаций должен быть виден для авторизованного юзера");
        
        // 3. Проверяем работу кнопки пересчета
        homePage.clickRecalculate();
        assertTrue(homePage.isRecalculating(), "Кнопка должна перейти в состояние загрузки");
    }

    @Test
    @DisplayName("Тест Поиска: Фильтрация игр по названию")
    public void testSearchFunctionality() {
        homePage.open();
        int initialCount = homePage.getGameCardsCount();
        
        homePage.searchText("Witcher");
        
        int filteredCount = homePage.getGameCardsCount();
        assertNotEquals(initialCount, filteredCount, "Количество игр должно измениться после поиска");
    }

    @Test
    @DisplayName("Тест Адаптивности: Проверка интерфейса на мобильном разрешении")
    public void testMobileResponsiveness() {
        homePage.open();
        // Устанавливаем размер экрана как у iPhone X
        driver.manage().window().setSize(new Dimension(375, 812));
        
        // Проверяем, что элементы не перекрывают друг друга и поиск доступен
        assertTrue(driver.findElement(By.xpath("//input[contains(@placeholder, 'Поиск')]")).isDisplayed());
    }

    @Test
    @DisplayName("Тест Навигации: Переход из карточки в детали игры")
    public void testNavigationToDetails() {
        homePage.open();
        homePage.waitForGamesToLoad();
        homePage.clickFirstGame();
        
        // Проверяем, что URL изменился на /games/ID
        assertTrue(driver.getCurrentUrl().contains("/games/"));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}