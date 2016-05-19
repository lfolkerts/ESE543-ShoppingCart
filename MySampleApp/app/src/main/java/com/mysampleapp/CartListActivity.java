package com.mysampleapp;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

import com.amazonaws.mobile.AWSMobileClient;
import java.util.*;

public class CartListActivity extends SingleFragmentActivity implements CartListFragment.OnDataPass
{
    public static final String TAG = "CART LIST ACTIVITY";
    CartItems c = new CartItems("KAKA", "1BCDEFGHIJKLM","Kakas", 0);
    ArrayList<CartItems> cart;
    //int received = getIntent().getIntExtra(SingleFragmentActivity.searchedItem, 0);
    @Override
    public void onCreate(Bundle savedInstanceState) {
       String received = getIntent().getStringExtra(SingleFragmentActivity.searchedItem);


        cart = CartContents.get(getApplicationContext()).getCart();
        super.onCreate(savedInstanceState);

    }

    @Override
    protected Fragment createFragment()
    {
        return new CartListFragment();
    }

    @Override
    public void onDataPass(String data) {
        Intent i = new Intent(this, ShoppingCartActivity.class);
        i.putExtra("ITEM_NAME", String.valueOf(data));
        startActivity(i);
    }


    public double passItemCost() {
        return (double)c.getBestPrice();
    }



}
