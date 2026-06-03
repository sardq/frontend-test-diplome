package com.recommendationsys.tests;

import com.recommendationsys.pages.LoginPage;
import com.recommendationsys.pages.PreferencesPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PreferencesUITest {
    private WebDriver driver;
    private PreferencesPage prefsPage;
    private LoginPage loginPage;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        prefsPage = new PreferencesPage(driver);
        loginPage = new LoginPage(driver);

        loginPage.open();
        loginPage.loginAs("dreod@mail.ru", "12345");
    }

    @Test
    @DisplayName("Тест: Поиск и выбор жанра")
    public void testSearchAndSelectPreference() {
        prefsPage.open();
        
        prefsPage.searchTag("Космос");
        
        int initialCount = prefsPage.getSelectedTagsCount();
        prefsPage.rateFirstTag(5);
        assertEquals(initialCount, prefsPage.getSelectedTagsCount(), 
            "Количество выбранных тегов должно увеличиться");
    }

    @Test
    @DisplayName("Тест: Удаление выбранного предпочтения")
    public void testRemovePreference() {
        prefsPage.open();
        
        prefsPage.searchTag("Action");
        prefsPage.rateFirstTag(4);
        int countAfterAdd = prefsPage.getSelectedTagsCount();
        
        prefsPage.removeFirstSelectedTag();
        
        assertEquals(countAfterAdd - 1, prefsPage.getSelectedTagsCount(), 
            "Тег должен быть удален из списка после нажатия на крестик");
    }

    @Test
    @DisplayName("Тест: Сохранение и переход на главную")
    public void testSaveAndRedirect() {
        prefsPage.open();
        
        prefsPage.searchTag("Кооператив");
        prefsPage.rateFirstTag(5);
        
        assertTrue(prefsPage.isSaveButtonEnabled(), "Кнопка сохранения должна быть активна");
        
        prefsPage.clickSave();
        
        new org.openqa.selenium.support.ui.WebDriverWait(driver, Duration.ofSeconds(5))
            .until(org.openqa.selenium.support.ui.ExpectedConditions.urlToBe("http://localhost/"));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}