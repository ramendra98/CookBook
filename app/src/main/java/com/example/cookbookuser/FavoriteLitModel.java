package com.example.cookbookuser;

public class FavoriteLitModel {

    String Decription,RecipeName;

    public FavoriteLitModel() {
    }

    public FavoriteLitModel(String decription, String recipeName) {
        Decription = decription;
        RecipeName = recipeName;
    }

    public String getDecription() {
        return Decription;
    }

    public void setDecription(String decription) {
        Decription = decription;
    }

    public String getRecipeName() {
        return RecipeName;
    }

    public void setRecipeName(String recipeName) {
        RecipeName = recipeName;
    }
}
