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

@DisplayName("Изменение данных пользователя")
public class UpdateUserTest {
    private AuthApi authApi;
    private String bearer;
    @Before
    public void setUp() {
        authApi = new AuthApi();
        User user = randomUser();
        bearer = authApi.registerUser(user).extract().path("accessToken");
    }
    @DisplayName("Изменение данных пользователя с авторизацией")
    @Test
    public void updateUserSuccessTest() {
        //Подготовка данных и изменение
        User userForUpdate = randomUser();
        ValidatableResponse response = authApi.updateUser(bearer,userForUpdate);
        assertEquals("Статус кода неверный",
                HttpStatus.SC_OK, response.extract().statusCode());
        assertEquals("Успешный запрос не возвращает success:true",
                true, response.extract().path("success"));

        //Проверка полей email и email
        ValidatableResponse responseAfterUpdate = authApi.updateUser(bearer,userForUpdate);
        String emailAfterUpdate = responseAfterUpdate.extract().path("user.email");
        String nameAfterUpdate = responseAfterUpdate.extract().path("user.name");
        assertEquals("Email не обновился",
                userForUpdate.getEmail(), emailAfterUpdate);
        assertEquals("Name не обновился",
                userForUpdate.getName(), nameAfterUpdate);

        //проверка поля password через авторизацию
        ValidatableResponse responseLoginAfterUpdate = authApi.login(emailAfterUpdate,userForUpdate.getPassword());
        assertEquals("Статус кода неверный при авторизации с измененными данными",
                HttpStatus.SC_OK, responseLoginAfterUpdate.extract().statusCode());
        assertEquals("Успешный запрос авторизации не возвращает success:true",
                true, responseLoginAfterUpdate.extract().path("success"));
    }
    @DisplayName("Изменение данных пользователя без авторизации")
    @Test
    public void updateUserWithoutAuthorizationFailTest() {
        ValidatableResponse response = authApi.updateUser();
        assertEquals("Статус кода неверный",
                HttpStatus.SC_UNAUTHORIZED, response.extract().statusCode());
        assertEquals("Неправильный текст ошибки",
                "You should be authorised", response.extract().path("message"));
    }
    @After
    public void cleanTestData() {
        authApi.deleteUser(bearer);
    }
}
