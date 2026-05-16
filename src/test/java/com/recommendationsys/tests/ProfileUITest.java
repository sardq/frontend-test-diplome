package com.recommendationsys.tests;

import com.recommendationsys.pages.LoginPage;
import com.recommendationsys.pages.ProfilePage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ProfileUITest {
    private WebDriver driver;
    private ProfilePage profilePage;
    private LoginPage loginPage;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        profilePage = new ProfilePage(driver);
        loginPage = new LoginPage(driver);

        // Для тестов профиля всегда нужна авторизация
        loginPage.open();
        loginPage.loginAs("dreod@mail.ru", "12345");
    }

    @Test
    @DisplayName("Тест: Отображение данных пользователя")
    public void testProfileInfo() {
        profilePage.open();
        assertEquals("sardq", profilePage.getUsername()); // Укажите ваш тестовый ник
    }

    @Test
    @DisplayName("Тест: Изменение даты рождения")
    public void testEditBirthDate() {
        profilePage.open();
        String testDate = "15.05.1995";
        profilePage.changeBirthDate("15051995");
        
        String displayedDate = profilePage.getBirthDateValue();
        assertEquals(testDate, displayedDate, "Дата в профиле должна обновиться");
    }

    @Test
    @DisplayName("Тест: Пагинация отзывов")
    public void testReviewsPagination() {
        profilePage.open();
        int initialCount = profilePage.getReviewsCount();
        
        // Если отзывов больше 5, должна быть кнопка
        if (initialCount >= 5) {
            profilePage.clickLoadMoreReviews();
            // Ждем небольшую задержку подгрузки React
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
            
            int newCount = profilePage.getReviewsCount();
            assertTrue(newCount > initialCount, "Количество отзывов должно увеличиться");
        }
    }


    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}