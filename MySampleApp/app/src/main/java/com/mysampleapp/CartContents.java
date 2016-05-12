package com.mysampleapp;

import android.content.Context;

import java.util.ArrayList;


public class CartContents {
    private ArrayList<CartItems> mCart;

    private static CartContents sCartContents;
    private Context mAppContext;

    private CartContents(Context appContext) {
        mAppContext = appContext;
        mCart = new ArrayList<CartItems>();
        CartItems c1 = new CartItems("Bens_Rice", "0123456789012", "Rice", 0);
        CartItems c2 = new CartItems("Cereal_LC", "0123456789013", "Lucky Charms", 0);
        CartItems c3 = new CartItems("Cereal_Rice", "0123456789014", "Rice Crispies", 0);
        CartItems c4 = new CartItems("Marker_Sharpie", "0123456789015", "Sharpie Red Markers", 0);
        CartItems c5 = new CartItems("Toothpast_CR", "0123456789016", "Crest Toothpaste", 0);
        CartItems c6 = new CartItems("Apple_IPhone", "0123456789017", "Iphone 4S", 0);
        CartItems c7 = new CartItems("Greeting_Card", "0123456789018", "Hallmark Greeting Card", 0);

        //Adding titles to list to display in adapter
        mCart.add(c1);
        mCart.add(c2);
        mCart.add(c3);
        mCart.add(c4);
        mCart.add(c5);
        mCart.add(c6);
        mCart.add(c7);

    }

    public static CartContents get(Context c){
        if (sCartContents == null){
            sCartContents = new CartContents(c.getApplicationContext());
        }
        return sCartContents;
    }

    public ArrayList<CartItems> getCart(){
        return mCart;
    }

}
