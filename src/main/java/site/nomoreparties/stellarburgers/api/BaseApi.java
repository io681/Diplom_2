package site.nomoreparties.stellarburgers.api;

import io.restassured.RestAssured;

public class BaseApi {
    public static final String BASE_URI = "https://stellarburgers.nomoreparties.site/";
    public BaseApi() {
        RestAssured.baseURI = BASE_URI;
    }
}
