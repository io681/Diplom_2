import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.api.AuthApi;
import site.nomoreparties.stellarburgers.models.entities.User;

import static org.junit.Assert.assertEquals;
import static site.nomoreparties.stellarburgers.utils.UserGenerator.randomBadUser;
import static site.nomoreparties.stellarburgers.utils.UserGenerator.randomUser;
@DisplayName("Создание пользователя")
public class RegisterTest {
    private AuthApi authApi;
    private User user;
    @Before
    public void setUp() {
        authApi = new AuthApi();
        user = randomUser();
    }
    @DisplayName("Создание уникального пользователя")
    @Test
    public void registerUserSuccessTest() {
        ValidatableResponse response = authApi.registerUser(user);
        assertEquals("Статус кода неверный",
                HttpStatus.SC_OK, response.extract().statusCode());
        assertEquals("Успешный запрос не возвращает success:true",
                true, response.extract().path("success"));
    }
    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Test
    public void registerNotUniqueUserFailTest() {
        authApi.registerUser(user);
        ValidatableResponse response = authApi.registerUser(user);
        assertEquals("Статус кода неверный",
                HttpStatus.SC_FORBIDDEN, response.extract().statusCode());
        assertEquals("Неверный текст ошибки",
                "User already exists", response.extract().path("message"));
    }
    @DisplayName("При создании пользователя не заполнить одно из обязательных полей")
    @Test
    public void checkRequiredFieldsRegisterUserTest () {
        user = randomBadUser();
        ValidatableResponse response = authApi.registerUser(user);
        assertEquals("Статус кода неверный",
                HttpStatus.SC_FORBIDDEN, response.extract().statusCode());
        assertEquals("Неверный текст ошибки",
                "Email, password and name are required fields", response.extract().path("message"));
    }
    @After
    public void cleanTestData() {
        String login = user.getEmail();
        String password = user.getPassword();
        String bearer = authApi.login(login,password).extract().path("accessToken");
        if (bearer != null) {
            authApi.deleteUser(bearer);
        }
    }
}
