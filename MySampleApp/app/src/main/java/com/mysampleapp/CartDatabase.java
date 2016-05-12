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
        db.execSQL("DROP TABLE IF EXISTS " + SHOPPING_CART_TABLE);
        onCreate(db);
    }

    public boolean insertCartItem(String name, int quantity, int market, double cost) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CART_ITEM_NAME, name);
        values.put(CART_ITEM_QUANTITY, quantity);
        values.put(CART_ITEM_MARKET, market);
        values.put(CART_ITEM_COST, cost);
        long result = db.insertWithOnConflict(SHOPPING_CART_TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);

        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.query(SHOPPING_CART_TABLE, null, null,
        null, null, null, CART_ITEM_MARKET + " ASC", null);
        return result;
    }

    public void deleteData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ SHOPPING_CART_TABLE);
    }

    public void deleteDuplicates(){
        getWritableDatabase().execSQL("DELETE FROM " + SHOPPING_CART_TABLE + " WHERE " +  ID_COLUMN +
                " not in (SELECT MIN(" + ID_COLUMN + ") FROM " + SHOPPING_CART_TABLE + " GROUP BY " +  CART_ITEM_NAME + ")");
    }
}
