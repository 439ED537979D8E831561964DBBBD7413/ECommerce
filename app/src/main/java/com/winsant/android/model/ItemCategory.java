package com.winsant.android.model;

public class ItemCategory {
    private int RecipeId;
    private String CategoryNameGU;
    private String CategoryNameEN;
    private String ImageUrl;

    public ItemCategory(int RecipeId, String CategoryNameGU, String CategoryNameEN, String ImageUrl) {
        this.RecipeId = RecipeId;
        this.CategoryNameGU = CategoryNameGU;
        this.CategoryNameEN = CategoryNameEN;
        this.ImageUrl = ImageUrl;
    }

    public String getCategoryNameGU() {
        return this.CategoryNameGU;
    }

    public String getCategoryNameEN() {
        return this.CategoryNameEN;
    }

    public int getRecipeId() {
        return this.RecipeId;
    }

    public String getImageurl() {
        return this.ImageUrl;
    }

}
