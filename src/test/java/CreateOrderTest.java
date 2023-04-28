import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellarburgers.api.AuthApi;
import site.nomoreparties.stellarburgers.api.IngredientsApi;
import site.nomoreparties.stellarburgers.api.OrdersApi;
import site.nomoreparties.stellarburgers.models.bodies.RequestBodyForCreateOrder;
import site.nomoreparties.stellarburgers.models.bodies.ingredients.ResponseBodyAfterGetIngredients;
import site.nomoreparties.stellarburgers.models.entities.User;

import static org.junit.Assert.assertEquals;
import static site.nomoreparties.stellarburgers.utils.UserGenerator.randomUser;
@DisplayName("Создание заказа")
public class CreateOrderTest {
    private OrdersApi ordersApi;
    private AuthApi authApi;
    private RequestBodyForCreateOrder requestBodyForCreateOrder;
    private String bearer;
    @Before
    public void setUp() {
        authApi = new AuthApi();
        IngredientsApi ingredientsApi = new IngredientsApi();
        ordersApi = new OrdersApi();

        //получение id игредиента
        ingredientsApi.getListIngredients();
        ResponseBodyAfterGetIngredients responseBodyAfterGetIngredients = ingredientsApi.getResponseBodyAfterGetIngredients();
        String idIngredient = responseBodyAfterGetIngredients.getData().get(0).get_id();
        String idIngredientNext = responseBodyAfterGetIngredients.getData().get(1).get_id();

        //добавление ингредиента в реквест боди для создания заказа
        requestBodyForCreateOrder = new RequestBodyForCreateOrder();
        requestBodyForCreateOrder.addIngredient(idIngredient);
        requestBodyForCreateOrder.addIngredient(idIngredientNext);

        //получение токена  пользователя
        User user = randomUser();
        bearer = authApi.registerUser(user).extract().path("accessToken");
    }
    @DisplayName("Проверка создания заказа с авторизацией и c ингредиентами")
    @Test
    public void createOrderSuccessTest (){
        ValidatableResponse response = ordersApi.createOrder(bearer,requestBodyForCreateOrder);
        assertEquals("Статус кода неверный",
                HttpStatus.SC_OK, response.extract().statusCode());
        assertEquals("Успешный запрос не возвращает success:true",
                true, response.extract().path("success"));
    }
    @DisplayName("Проверка создания заказа без авторизации")
    @Description("Тест не пройдет проверку, т.к. сервер пропускает неавторизированных пользователей")
    @Test
    public void createOrderWithoutAuthorizationFailTest (){
        ValidatableResponse response = ordersApi.createOrder(requestBodyForCreateOrder);
        assertEquals("Статус кода неверный",
                HttpStatus.SC_UNAUTHORIZED, response.extract().statusCode());
    }
    @DisplayName("Проверка создания заказа без авторизации")
    @Test
    public void createOrderWithoutIngredientsFailTest (){
        requestBodyForCreateOrder = new RequestBodyForCreateOrder();
        ValidatableResponse response = ordersApi.createOrder(bearer,requestBodyForCreateOrder);
        assertEquals("Статус кода неверный",
                HttpStatus.SC_BAD_REQUEST, response.extract().statusCode());
        assertEquals("Некорректный текст ошибки",
                "Ingredient ids must be provided", response.extract().path("message"));
    }
    @DisplayName("Проверка создания заказа с неверным хешем ингредиента")
    @Test
    public void createOrderWithIngredientNotCorrectIdFailTest (){
        requestBodyForCreateOrder = new RequestBodyForCreateOrder();
        String IngredientNotCorrectId = "61x0x5x71y1f82001y5xtx6d";
        requestBodyForCreateOrder.addIngredient(IngredientNotCorrectId);
        ValidatableResponse response = ordersApi.createOrder(bearer,requestBodyForCreateOrder);
        assertEquals("Сервер пропустил некорректный хеш ингредиента,нет ошибки 500",
                HttpStatus.SC_INTERNAL_SERVER_ERROR, response.extract().statusCode());
    }
    @After
    public void cleanTestData() {
        authApi.deleteUser(bearer);
    }
}
