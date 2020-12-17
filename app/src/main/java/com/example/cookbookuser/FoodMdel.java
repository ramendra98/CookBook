package com.example.cookbookuser;

public class FoodMdel {
    private  String itemName;
    private  String itemDescription;
    private  String itemPrices;
    private String itemImage;

    public FoodMdel() {
    }

    public FoodMdel(String itemName, String itemDescription, String itemPrices, String itemImage) {
        this.itemName = itemName;
        this.itemDescription = itemDescription;
        this.itemPrices = itemPrices;
        this.itemImage = itemImage;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getItemPrices() {
        return itemPrices;
    }

    public void setItemPrices(String itemPrices) {
        this.itemPrices = itemPrices;
    }

    public String getItemImage() {
        return itemImage;
    }

    public void setItemImage(String itemImage) {
        this.itemImage = itemImage;
    }
}
