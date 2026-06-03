package com.recommendationsys.tests;

import com.recommendationsys.pages.GamesPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

public class GamesUITest {
    private WebDriver driver;
    private GamesPage gamesPage;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        gamesPage = new GamesPage(driver);
    }

    @Test
    @DisplayName("Тест: Поиск и выбор тега в каталоге")
    public void testTagSearchAndSelect() {
        gamesPage.open();
        
        gamesPage.searchTag("Хоррор");
        
        gamesPage.clickTagByName("Хоррор");
        gamesPage.waitForHeaderToContain("Хоррор");

        assertTrue(gamesPage.getHeaderText().contains("Хоррор"), 
            "Заголовок должен соответствовать выбранному тегу");
        
        assertTrue(driver.getCurrentUrl().contains("tag="));
    }

    @Test
    @DisplayName("Те ст: Пагинация списка игр")
    public void testInfiniteScroll() {
        gamesPage.open();
        int initialCount = gamesPage.getGamesCount();
        
        if (initialCount >= 10) {
            gamesPage.clickLoadMore();
            
            int newCount = gamesPage.getGamesCount();
            assertTrue(newCount > initialCount, 
                "Количество игр должно увеличиться после нажатия 'Загрузить ещё'");
        }
    }

    @Test
    @DisplayName("Тест: Отображение рейтингов в карточке")
    public void testGameCardData() {
        gamesPage.open();
         gamesPage.waitForGamesToLoad();
        String firstTitle = gamesPage.getAllVisibleTitles().get(0);
        assertNotNull(firstTitle, "Название игры должно отображаться");
        
        assertTrue(driver.getPageSource().contains("⭐"), "Иконка рейтинга должна присутствовать");
    }

    @Test
    @DisplayName("Тест: Переход к деталям игры")
    public void testNavigationToDetails() {
        gamesPage.open();
        gamesPage.waitForGamesToLoad();
        gamesPage.clickFirstGame();
        
        waitUrlContains("/games/");
        assertTrue(driver.getCurrentUrl().matches(".*/games/\\d+"), "URL должен вести на страницу конкретной игры");
    }

    private void waitUrlContains(String part) {
        new org.openqa.selenium.support.ui.WebDriverWait(driver, Duration.ofSeconds(5))
            .until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains(part));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}