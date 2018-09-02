package ui;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.codeborne.selenide.Selenide;

@DisplayName("Логин")
class LoginTest {
    @BeforeEach
    void clearCookies() {
        Selenide.clearBrowserCookies();
    }

    @Test
    @DisplayName("Успешный логин пользователя")
    void successfulLogin() {
        User existingUser = new User()
            .setEmail("brasenia239@gmail.com")
            .setPassword("123qwerty")
            .setName("Сергей")
            .setSurname("Тестировщик");

        MainPage mainPage = MainPage.openMainPage();
        mainPage.loginAs(existingUser);
        mainPage.checkLoggedInAs(existingUser);
    }

    @Test
    @DisplayName("Неуспешный логин пользователя")
    void failedLogin() {
        User notExistingUser = new User()
            .setEmail("user@gmail.com")
            .setPassword("password")
            .setName("Сергей")
            .setSurname("Тестировщик");

        MainPage mainPage = MainPage.openMainPage();
        mainPage.loginAs(notExistingUser);
        mainPage.checkLoginFailed();
    }

    @Test
    @DisplayName("Вход заблокированного пользователя")
    void blockedUserLogin() {
        User blockedUser = new User()
            .setEmail("user@mail.ru")
            .setPassword("password");
        MainPage mainPage = MainPage.openMainPage();
        mainPage.loginAs(blockedUser);
        mainPage.checkAccountBlockedMessage();
    }

    @Test
    @DisplayName("Валидация входных данных на фронте")
    void wrongDataFiltering() {
        MainPage mainPage = MainPage.openMainPage();
        mainPage.tryToLogin("brasenia239@gmail.com", null);
        mainPage.checkPasswordEmptyMessage();

        mainPage = MainPage.openMainPage();
        mainPage.tryToLogin(null, "123");
        mainPage.checkEmailEmptyMessage();

        mainPage = MainPage.openMainPage();
        mainPage.tryToLogin(null, null);
        mainPage.checkEmailEmptyMessage();
        mainPage.checkPasswordEmptyMessage();

        mainPage = MainPage.openMainPage();
        mainPage.tryToLogin("brasenia239gmail.com", "123qwerty");
        mainPage.checkWrongEmailMessage();
    }
}
