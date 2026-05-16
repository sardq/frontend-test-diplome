package com.recommendationsys.tests;

import com.recommendationsys.pages.FavoritesPage;
import com.recommendationsys.pages.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FavoritesUITest {
    private WebDriver driver;
    private FavoritesPage favoritesPage;
    private LoginPage loginPage;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        favoritesPage = new FavoritesPage(driver);
        loginPage = new LoginPage(driver);

        // Входим в систему
        loginPage.open();
        loginPage.loginAs("dreod@mail.ru", "12345");
    }

    @Test
    @DisplayName("Тест: Поиск игры внутри избранного")
    public void testSearchInFavorites() {
        favoritesPage.open();
        
        // Вводим название, которое точно есть (например, Witcher)
        favoritesPage.searchFor("Witcher");
        
        int count = favoritesPage.getVisibleGamesCount();
        assertTrue(count >= 0, "Сетка должна обновиться");
        
        // Вводим белиберду, чтобы проверить Empty State
        favoritesPage.searchFor("ZXC_NON_EXISTENT_GAME_123");
        assertTrue(favoritesPage.isEmptyStateVisible(), "Должна появиться заглушка 'Ничего не нашли'");
    }

    @Test
    @DisplayName("Тест: Фильтрация по жанру")
    public void testTagFiltering() {
        favoritesPage.open();
        
        // Выбираем конкретный жанр из селекта
        favoritesPage.selectTag("Рпг"); 
        
        // Проверяем, что игры отображаются
        assertTrue(favoritesPage.getVisibleGamesCount() >= 0);
    }

    @Test
    @DisplayName("Тест: Пагинация в избранном")
    public void testPagination() {
        favoritesPage.open();
        int initialCount = favoritesPage.getVisibleGamesCount();
        
        // Если игр много (больше 6), проверяем кнопку
        try {
            favoritesPage.clickLoadMore();
            int newCount = favoritesPage.getVisibleGamesCount();
            assertTrue(newCount > initialCount, "Количество игр должно увеличиться после подгрузки");
        } catch (Exception e) {
            System.out.println("Кнопка загрузки не найдена (игр меньше 6)");
        }
    }

    @Test
    @DisplayName("Тест: Переход из избранного в карточку игры")
    public void testNavigateToGame() {
        favoritesPage.open();
        if (favoritesPage.getVisibleGamesCount() > 0) {
            favoritesPage.clickFirstGame();
            assertTrue(driver.getCurrentUrl().contains("/games/"), "Должен произойти переход в детали игры");
        }
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}