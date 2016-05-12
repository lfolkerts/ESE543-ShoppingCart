package com.mysampleapp;
/**
 * Created by lfolkerts on 5/12/16.
 */


/**
 * A dummy item representing a piece of content.
 */
public class StoreItem
{
    private final CartItems item;
    protected float price; //in US cents
    protected final String Store; //TODO: see to change to enum
    private static double currencyConversion = 1;

    public StoreItem(CartItems item, float price, String Store)
    {
        this.item = item;
        this.price = (int)((price>0)?(price*100):ShoppingContent.INVALID_PRICE);
        this.Store = Store;
    }

    public void setPrice(float price) //in USD, conversion must take place outside of function
    {
        this.price = (price > 0) ? (price * 100) : ShoppingContent.INVALID_PRICE;
    }
    public void setCurrencyRate(double rate)
    {
        currencyConversion = rate;
    }
    public String getStore()
    {
        return this.Store;
    }
    public float getPrice()
    {
        if(price != ShoppingContent.INVALID_PRICE)
        {
            return (float)((float)this.price/100*currencyConversion);
        }
        else
        {
            return getInvalidPrice();
        }
    }
    public static float getInvalidPrice()
    {
        return (float)ShoppingContent.INVALID_PRICE/100;
    }
    public double getCurrencyRate()
    {
        return currencyConversion;
    }
}


