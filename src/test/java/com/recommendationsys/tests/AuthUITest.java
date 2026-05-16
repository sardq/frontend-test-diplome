package com.recommendationsys.tests;

import com.recommendationsys.pages.LoginPage;
import com.recommendationsys.pages.RegisterPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.*;

public class AuthUITest {
    private WebDriver driver;
    private LoginPage loginPage;
    private RegisterPage registerPage;

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        loginPage = new LoginPage(driver);
        registerPage = new RegisterPage(driver);
    }

    @Test
    @DisplayName("Тест успешной регистрации нового пользователя")
    public void testSuccessRegistration() {
        registerPage.open();
        // Генерируем случайную почту, чтобы тест не падал из-за дубликатов
        String email = "test" + System.currentTimeMillis() + "@mail.ru";
        registerPage.registerAs(email, "TesterBot", "password123");
        
        assertEquals("http://localhost/", driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Тест успешного входа")
    public void testSuccessLogin() {
        loginPage.open();
        loginPage.loginAs("dreod@mail.ru", "12345"); // используйте данные из вашей базы
        
        assertEquals("http://localhost/", driver.getCurrentUrl());
    }

    @Test
    @DisplayName("Тест ошибки при неверном пароле")
    public void testInvalidPassword() {
        loginPage.open();
        loginPage.loginWithInvalidData("dreod@mail.ru", "wrong_pass");
        
        String error = loginPage.getErrorMessage();
        assertTrue(error.contains("Неверный логин или пароль") || error.contains("⚠️"));
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}