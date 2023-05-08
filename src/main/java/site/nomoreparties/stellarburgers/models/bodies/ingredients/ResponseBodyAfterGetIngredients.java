package site.nomoreparties.stellarburgers.models.bodies.ingredients;

import java.util.List;

public class ResponseBodyAfterGetIngredients {
    private boolean success;
    private List<Ingredient> data;
    public List<Ingredient> getData() {
        return data;
    }

    public void setData(List<Ingredient> data) {
        this.data = data;
    }
}
