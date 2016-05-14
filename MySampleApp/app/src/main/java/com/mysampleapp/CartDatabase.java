package com.mysampleapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class CartDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "cart.db";
    private static final int DB_VERSION = 1;
    public static final String SHOPPING_CART_TABLE = "cart";
    public static final String ID_COLUMN = "id";
    public static final String CART_ITEM_BARCODE = "barcode";
    public static final String CART_ITEM_NAME = "name";
    public static final String CART_ITEM_QUANTITY = "quantity";
    public static final String CART_ITEM_MARKET = "market";
    public static final String CART_ITEM_COST = "cost";

    public CartDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertCartItem(String name, int quantity, int market, double cost)
    {
       return insertCartItem(name, quantity, "Generic",  "0000000000000", cost);
    }

    public boolean insertCartItem(String name, int quantity, String market, String barcode, double cost)
    {
        return true;
    }

    public Cursor getAllData()
    {
        return getAllData();
    }

    public void deleteData() {

    }

}
