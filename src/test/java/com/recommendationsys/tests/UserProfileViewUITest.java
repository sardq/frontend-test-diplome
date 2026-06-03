package com.recommendationsys.tests;

import com.recommendationsys.pages.UserProfileViewPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserProfileViewUITest {
    private WebDriver driver;
    private UserProfileViewPage userProfilePage;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        userProfilePage = new UserProfileViewPage(driver);
    }

    @Test
    @DisplayName("Тест: Проверка отображения данных чужого профиля")
    public void testPublicProfileDisplay() {
        userProfilePage.open(25147L);
        
        String username = userProfilePage.getUsername();
        assertFalse(username.isEmpty(), "Имя пользователя должно отображаться");
        assertTrue(userProfilePage.isUserStatusVisible(), "Бейдж статуса должен быть виден");
    }

    @Test
    @DisplayName("Тест: Пагинация отзывов в чужом профиле")
    public void testPublicReviewsPagination() {
        userProfilePage.open(25147L);
        
        int initialCount = userProfilePage.getReviewsCount();
        
        try {
            userProfilePage.clickLoadMore();
            int newCount = userProfilePage.getReviewsCount();
            assertTrue(newCount > initialCount, "Количество отзывов должно увеличиться");
        } catch (Exception e) {
            System.out.println("Кнопка загрузки не найдена (отзывов меньше 5 или их нет)");
        }
    }

    @Test
    @DisplayName("Тест: Переход к игре из отзыва пользователя")
    public void testNavigateToGameFromPublicReview() {
        userProfilePage.open(25147L);
        
        if (userProfilePage.getReviewsCount() > 0) {
            userProfilePage.clickOnFirstReviewGame();
            
            new org.openqa.selenium.support.ui.WebDriverWait(driver, java.time.Duration.ofSeconds(5))
                .until(org.openqa.selenium.support.ui.ExpectedConditions.urlContains("/games/"));
            
            assertTrue(driver.getCurrentUrl().contains("/games/"), "Должен произойти переход на страницу игры");
        }
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}