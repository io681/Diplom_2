package site.nomoreparties.stellarburgers.utils;

import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import site.nomoreparties.stellarburgers.models.entities.User;

import java.util.Locale;

public class UserGenerator {

    public static User randomUser() {
        User user =  new User();
        FakeValuesService faker = new FakeValuesService(new Locale("en-Us"),new RandomService());
        user.setEmail(faker.bothify("???????##@test3344.ru"));
        user.setPassword (faker.bothify("???##????##"));
        user.setName(faker.bothify("????????##"));
        return user;
    }
    public static User randomBadUser() {
        User user =  new User();
        FakeValuesService faker = new FakeValuesService(new Locale("en-Us"),new RandomService());
        user.setEmail(faker.bothify("???????##@test3344.ru"));
        user.setPassword (faker.bothify("???##????##"));
        user.setName("");
        return user;
    }
}
