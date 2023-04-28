import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.api.AuthApi;
import site.nomoreparties.stellarburgers.models.entities.User;

import static org.junit.Assert.assertEquals;
import static site.nomoreparties.stellarburgers.utils.UserGenerator.randomUser;
@DisplayName("Логин пользователя")
public class LoginTest {
    private AuthApi authApi;
    private User user;
    private String bearer;
    @Before
    public void setUp() {
        authApi = new AuthApi();
        user = randomUser();
        bearer = authApi.registerUser(user).extract().path("accessToken");
    }
    @DisplayName("Логин под существующим пользователем")
    @Test
    public void loginRegisteredUserSuccessTest() {
        String login = user.getEmail();
        String password = user.getPassword();
        ValidatableResponse response = authApi.login(login,password);
        assertEquals("Статус кода неверный",
                HttpStatus.SC_OK, response.extract().statusCode());
        assertEquals("Успешный запрос не возвращает success:true",
                true, response.extract().path("success"));
    }
    @DisplayName("Логин с неверным логином и паролем")
    @Test
    public void loginNotRegisteredUserFailTest() {
        User notRegisteredUser = randomUser();
        String login = notRegisteredUser.getEmail();
        String password = notRegisteredUser.getPassword();
        ValidatableResponse response = authApi.login(login,password);
        assertEquals("Статус кода неверный",
                HttpStatus.SC_UNAUTHORIZED, response.extract().statusCode());
        assertEquals("Некорректный текст ошибки",
                "email or password are incorrect", response.extract().path("message"));
    }
    @After
    public void cleanTestData() {
    authApi.deleteUser(bearer);
    }
}
