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

@DisplayName("Получение заказов конкретного пользователя")
public class GetOrdersByUserTest {
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
    @DisplayName("Получение заказов конкретного пользователя с авторизацией")
    @Test
    public void getOrdersByUserSuccessTest (){
        //создание заказа и получение id заказа в респонзе
        ValidatableResponse responseAfterCreatedOrder = ordersApi.createOrder(bearer, requestBodyForCreateOrder);
        String orderIdExpected = responseAfterCreatedOrder.extract().path("order._id");

        //запрос получения заказа по пользователю и проверки
        ValidatableResponse response = ordersApi.getOrderByUser(bearer);
        assertEquals("Статус кода неверный",
                HttpStatus.SC_OK, response.extract().statusCode());
        String orderIdActual = response.extract().path("orders[0]._id");
        assertEquals("Id заказов не соответствуют",
                orderIdExpected, orderIdActual);
    }
    @DisplayName("Получение заказов конкретного пользователя без авторизации")
    @Test
    public void getOrdersByUserWithoutAuthorizationFailTest (){
        ValidatableResponse response = ordersApi.getOrderByUser();
        assertEquals("Статус кода неверный",
                HttpStatus.SC_UNAUTHORIZED, response.extract().statusCode());
        assertEquals("Некорректный текст ошибки",
                "You should be authorised", response.extract().path("message"));
    }
    @After
    public void cleanTestData() {
        authApi.deleteUser(bearer);
    }
}
