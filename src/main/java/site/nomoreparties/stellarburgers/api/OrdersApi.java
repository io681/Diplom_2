package site.nomoreparties.stellarburgers.api;

import io.restassured.response.ValidatableResponse;
import site.nomoreparties.stellarburgers.models.bodies.RequestBodyForCreateOrder;

import static io.restassured.RestAssured.given;

public class OrdersApi {

    private static final String PATH_ORDERS_API = "api/orders/";
    private RequestBodyForCreateOrder requestBodyForCreateOrder;

    public OrdersApi () {
        super();
    }

    //создание заказа
    public ValidatableResponse createOrder (String bearer, RequestBodyForCreateOrder requestBodyForCreateOrder) {
        String[] splitBearer = bearer.split(" ");
        return given()
                .auth().oauth2(splitBearer[1])
                .header("Content-type", "application/json")
                .and()
                .body(requestBodyForCreateOrder)
                .when()
                .post(PATH_ORDERS_API)
                .then();
    }
    //создание заказа без авторизации
    public ValidatableResponse createOrder (RequestBodyForCreateOrder requestBodyForCreateOrder) {
        return given()
                .auth().oauth2("")
                .header("Content-type", "application/json")
                .and()
                .body(requestBodyForCreateOrder)
                .when()
                .post(PATH_ORDERS_API)
                .then();
    }
    //получить заказы пользователя
    public ValidatableResponse getOrderByUser (String bearer) {
        String[] splitBearer = bearer.split(" ");
        return given()
                .auth().oauth2(splitBearer[1])
                .header("Content-type", "application/json")
                .when()
                .get(PATH_ORDERS_API)
                .then();
    }
    //получить заказы пользователя без авторизации
    public ValidatableResponse getOrderByUser () {
        return given()
                .auth().oauth2("")
                .header("Content-type", "application/json")
                .when()
                .get(PATH_ORDERS_API)
                .then();
    }
}
