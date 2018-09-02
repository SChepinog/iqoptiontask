package api;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.notNullValue;

@DisplayName("API логин")
class APILoginTest {
    private static String baseUrl = "https://auth.iqoption.com/api/v1.0/login";

    @Test
    @DisplayName("Данные не заполнены")
    void notAllData() {
        when()
            .post(baseUrl).
            then()
            .statusCode(400)
            .body("errors.code", hasItems(1, 2))
            .body("errors.title", hasItems("Invalid email", "Invalid password"));

        LoginData invalidUser = new LoginData()
            .setPassword("123qwerty");
        given()
            .contentType(ContentType.JSON)
            .body(invalidUser).
            when()
            .post(baseUrl)
            .then()
            .statusCode(400)
            .body("errors.code", hasItems(1))
            .body("errors.title", hasItems("Invalid email"));

        invalidUser = new LoginData()
            .setEmail("brasenia239@gmail.com");
        given()
            .contentType(ContentType.JSON)
            .body(invalidUser).
            when()
            .post(baseUrl)
            .then()
            .statusCode(400)
            .body("errors.code", hasItems(2))
            .body("errors.title", hasItems("Invalid password"));
    }

    @Test
    @DisplayName("Валидация email на бэке")
    void emailValidation() {
        LoginData invalidUser = new LoginData()
            .setEmail("brasenia239gmail.com")
            .setPassword("123qwerty");
        given()
            .contentType(ContentType.JSON)
            .body(invalidUser).
            when()
            .post(baseUrl)
            .then()
            .statusCode(400)
            .body("errors.code", hasItems(1))
            .body("errors.title", hasItems("Invalid email"));
    }

    @Test
    @DisplayName("Заблокированный пользователь")
    void blockedUser() {
        LoginData blockedUser = new LoginData()
            .setEmail("user@mail.ru")
            .setPassword("password");
        given()
            .contentType(ContentType.JSON)
            .body(blockedUser).
            when()
            .post(baseUrl).
            then()
            .statusCode(403)
            .body("errors.code", hasItems(200))
            .body("errors.title", hasItems("Account is blocked"));
    }

    @Test
    @DisplayName("Успешный логин")
    void successfulLogin() {
        LoginData user = new LoginData()
            .setEmail("brasenia239@gmail.com")
            .setPassword("123qwerty");
        given()
            .contentType(ContentType.JSON)
            .body(user).
            when()
            .post(baseUrl)
            .then()
            .statusCode(200)
            .body("data.ssid", notNullValue());
    }

    @Test
    @DisplayName("Несуществующий пользователь")
    void UnsuccessfulLogin() {
        LoginData user = new LoginData()
            .setEmail("brasenia239@mail.ru")
            .setPassword("123qwerty");
        given()
            .contentType(ContentType.JSON)
            .body(user).
            when()
            .post(baseUrl)
            .then()
            .statusCode(403)
            .body("errors.code", hasItems(202))
            .body("errors.title", hasItems("Invalid credentials"));
    }
}
