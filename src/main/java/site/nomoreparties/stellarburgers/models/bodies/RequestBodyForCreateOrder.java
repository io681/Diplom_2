package site.nomoreparties.stellarburgers.models.bodies;

import java.util.ArrayList;
import java.util.List;

public class RequestBodyForCreateOrder {
    private List<String> ingredients =new ArrayList<>();
    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
    public void addIngredient(String idIngredient) {
        this.ingredients.add(idIngredient);
    }
}
