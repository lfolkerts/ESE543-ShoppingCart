package com.mysampleapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;

public class ShoppingCartActivity extends SingleFragmentActivity {
    public static final String TAG = "SHOPPING CART ACTIVITY ";
    CartItems c = new CartItems("KTKT", "ABCDEFGHIJKLM","Kikis", 0);

    @Override
    protected Fragment createFragment() {
        return new CartFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        String itemName = i.getStringExtra("ITEM_NAME");
        int itemQuantity = i.getIntExtra("ITEM_QUANTITY", 0);
        double itemCost = i.getDoubleExtra("ITEM_COST", 0);
        c.setQuantity(itemQuantity);
        c.setPrice("Generic", (float)itemCost);
    }

    public String passData() {     return c.getBarcode();  }

    public Double passItemCost() {
        return (double)c.getBestPrice();
    }

    public int passItemQuantity(){
        return c.getQuantity();
    }

    public CartItems passItem()
    {
        return c;
    }

    public int passItemRating(){
        return c.getRating();
    }

    public void onItemQuantityPass(int itemQuantity) {
        c.setQuantity(itemQuantity);
    }

    public void onItemRatingPass(int itemRating) {
        c.setUserRating(itemRating);
    }
}
