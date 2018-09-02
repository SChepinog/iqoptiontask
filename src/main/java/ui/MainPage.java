package ui;

import org.junit.jupiter.api.Assertions;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selectors.byName;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class MainPage {
    public static MainPage openMainPage() {
        Selenide.open("https://iqoption.com/ru");
        return new MainPage();
    }

    private SelenideElement getLoginTabButton() {
        return $(".SidebarTab__text").$(byText("Войти"));
    }

    public void openLoginForm() {
        getLoginTabButton().click();
    }

    public SelenideElement getEmailField() {
        return $(byName("email"));
    }

    public SelenideElement getPasswordField() {
        return $(byName("password"));
    }

    public SelenideElement getLoginButton() {
        return $("button.SidebarLogin__submit");
    }

    public void loginAs(User user) {
        openLoginForm();
        getEmailField().setValue(user.getEmail());
        getPasswordField().setValue(user.getPassword());
        getLoginButton().click();
    }

    public void checkLoggedInAs(User user) {
        Assertions.assertAll(
            () -> $(".SidebarProfile").should(Condition.exist),
            () -> $(".SidebarProfile__UserName").shouldHave(Condition.exactText(user.getFullName())),
            () -> $(".SidebarProfile__UserEmail").shouldHave(Condition.exactText(user.getEmail()))
        );
    }

    public SelenideElement getLoginErrorElement() {
        return $(".SidebarLogin__error");
    }

    public void checkLoginFailed() {
        getLoginErrorElement().shouldHave(Condition.exactText("Неправильный логин или пароль"));
    }

    public void checkAccountBlockedMessage() {
        getLoginErrorElement().shouldHave(Condition.exactText("Ваш аккаунт заблокирован"));
    }

    private SelenideElement getEmailErrorElement() {
        return getEmailField().parent().parent().$(".iqInput__error");
    }

    private void checkEmailMessage(String message) {
        getEmailErrorElement().shouldHave(Condition.cssClass("active"), Condition.exactText(message));
    }

    public void checkEmailEmptyMessage() {
        checkEmailMessage("Поле не заполнено");
    }

    public void checkWrongEmailMessage() {
        checkEmailMessage("Неверный e-mail");
    }

    private SelenideElement getPasswordErrorElement() {
        return getPasswordField().parent().parent().$(".iqInput__error");
    }

    private void checkPasswordMessage(String message) {
        getPasswordErrorElement().shouldHave(Condition.cssClass("active"), Condition.exactText(message));
    }

    public void checkPasswordEmptyMessage() {
        checkPasswordMessage("Поле не заполнено");
    }

    public void tryToLogin(String email, String password) {
        openLoginForm();
        if (email != null) {
            getEmailField().setValue(email);
        }
        if (password != null) {
            getPasswordField().setValue(password);
        }
        getLoginButton().click();
    }
}
