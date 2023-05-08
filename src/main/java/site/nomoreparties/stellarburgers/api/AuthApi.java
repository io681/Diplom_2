package site.nomoreparties.stellarburgers.api;

import io.restassured.response.ValidatableResponse;
import site.nomoreparties.stellarburgers.models.bodies.RequestBodyForLogin;
import site.nomoreparties.stellarburgers.models.entities.User;

import static io.restassured.RestAssured.given;

public class AuthApi extends BaseApi {

    private static final String PATH_AUTH_API = "api/auth/";
    private User user;
    private RequestBodyForLogin requestBodyForLogin;

    public AuthApi () {
        super();
    }

    //Регистрация пользователя
    public ValidatableResponse registerUser(User user) {
        this.user = user;
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .post(PATH_AUTH_API + "register")
                .then();
    }
    // Авторизация
    public ValidatableResponse login (String login,String password) {
        requestBodyForLogin = new RequestBodyForLogin(login,password);
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBodyForLogin)
                .when()
                .post(PATH_AUTH_API + "login")
                .then();
    }
    // Удаление пользователя
    public void deleteUser (String bearer) {
        String[] splitBearer = bearer.split(" ");
        given()
                .auth().oauth2(splitBearer[1])
                .header("Content-type", "application/json")
                .when()
                .delete(PATH_AUTH_API + "user")
                .then();
    }
    //Изменение данных пользователя
    public ValidatableResponse updateUser (String bearer, User user) {
        String[] splitBearer = bearer.split(" ");
        return given()
                .auth().oauth2(splitBearer[1])
                .header("Content-type", "application/json")
                .and()
                .body(user)
                .when()
                .patch(PATH_AUTH_API + "user")
                .then();
    }
    // Изменение данных пользователя без авторизации
    public ValidatableResponse updateUser () {
        return given()
                .auth().oauth2("")
                .header("Content-type", "application/json")
                .when()
                .patch(PATH_AUTH_API + "user")
                .then();
    }
}
