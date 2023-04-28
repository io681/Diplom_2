package site.nomoreparties.stellarburgers.api;

import site.nomoreparties.stellarburgers.models.bodies.ingredients.ResponseBodyAfterGetIngredients;

import static io.restassured.RestAssured.given;

public class IngredientsApi extends BaseApi {
    private static final String PATH_INGREDIENTS_API = "api/ingredients";
    private ResponseBodyAfterGetIngredients responseBodyAfterGetIngredients;

    public ResponseBodyAfterGetIngredients getResponseBodyAfterGetIngredients() {
        return responseBodyAfterGetIngredients;
    }
    public IngredientsApi () {
        super();
    }

    //получение списка ингредиентов
    public void getListIngredients(){
        this.responseBodyAfterGetIngredients = given()
                .header("Content-type", "application/json")
                .when()
                .get(PATH_INGREDIENTS_API)
                .body().as(ResponseBodyAfterGetIngredients.class);
    }
}
