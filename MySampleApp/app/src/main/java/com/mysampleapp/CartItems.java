package com.mysampleapp;


import java.util.List;
import java.util.ListIterator;

public class CartItems
{
    private final String id;
    private final String barcode;
    private String name;
    private int quantity;
    protected int rating;
    protected List<StoreItem> Stores;

    public CartItems(String id, String barcode, String name, int rating)
    {
        this.id = id;
        this.barcode = barcode;
        this.name = name;
        this.rating = (rating>=ShoppingContent.ITEM_MAX_RATING)?ShoppingContent.ITEM_UNRATED:
                (rating<ShoppingContent.ITEM_MIN_RATING)?ShoppingContent.ITEM_UNRATED:
                        rating;
    }

    private StoreItem find_store_item(String store)
    {
        ListIterator<StoreItem> List_Item;
        StoreItem StoreItem_From_List;
        for(List_Item = Stores.listIterator(); List_Item.hasNext(); )
        {
            StoreItem_From_List = List_Item.next();
            if(StoreItem_From_List.getStore().equals(store)){ return StoreItem_From_List; }
        }
        return null;
    }

    public void setUserRating(int rating) //TODO: store or fetch average ratings online
    {
        this.rating = (rating>=ShoppingContent.ITEM_MAX_RATING)?ShoppingContent.ITEM_MAX_RATING:
                (rating<ShoppingContent.ITEM_MIN_RATING)?ShoppingContent.ITEM_MIN_RATING:
                        rating;
    }
    public void setPublicRating(int rating) //TODO: store or fetch average ratings online
    {
        this.rating = (rating>=ShoppingContent.ITEM_MAX_RATING)?ShoppingContent.ITEM_MAX_RATING:
                (rating<ShoppingContent.ITEM_MIN_RATING)?ShoppingContent.ITEM_MIN_RATING:
                        rating;
    }
    public void setPrice(String store, float price) {
        ListIterator<StoreItem> List_Item;
        StoreItem StoreItem_From_List = find_store_item(store);

        if (StoreItem_From_List != null) {
            StoreItem_From_List.setPrice(price);
            return;

        } else {
            StoreItem_From_List = new StoreItem(this, price, store);
            Stores.add(StoreItem_From_List);
        }
    }
    public void setQuantity(int quantity) //TODO: store or fetch average ratings online
    {
        this.quantity = (rating<0)?0: rating;
    }


    public float getPrice(String Store)
    {
        StoreItem StoreItem_From_List = find_store_item(Store);
        if(StoreItem_From_List != null)
        {
            return StoreItem_From_List.getPrice()*quantity;
        }
        else
        {
            return StoreItem.getInvalidPrice();
        }
    }

    public StoreItem getBestPricedStore()
    {
        ListIterator<StoreItem> List_Item;
        StoreItem StoreItem_From_List;
        float min_price = Float.MAX_VALUE;
        float price;
        StoreItem Min_Price_StoreItem = null;
        for(List_Item = Stores.listIterator(); List_Item.hasNext(); )
        {
            StoreItem_From_List = List_Item.next();
            price =  StoreItem_From_List.getPrice();
            if(price != ShoppingContent.INVALID_PRICE && price < min_price )
            {
                price = min_price;
                Min_Price_StoreItem = StoreItem_From_List;
            }
        }
        return Min_Price_StoreItem;
    }

    public float getBestPrice()
    {
        ListIterator<StoreItem> List_Item;
        StoreItem StoreItem_From_List;
        float min_price = Float.MAX_VALUE;
        float price;
        StoreItem Min_Price_StoreItem = null;
        for(List_Item = Stores.listIterator(); List_Item.hasNext(); )
        {
            StoreItem_From_List = List_Item.next();
            price =  StoreItem_From_List.getPrice();
            if(price != ShoppingContent.INVALID_PRICE && price < min_price )
            {
                price = min_price;
                Min_Price_StoreItem = StoreItem_From_List;
            }
        }
        return min_price*quantity;
    }

    public int getRating()
    {
        return rating;
    }
    public String getID()
    {
        return id;
    }
    public String getBarcode()
    {
        return barcode;
    }

    public int getQuantity(){return quantity;}

}
