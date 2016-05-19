package com.mysampleapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by lfolkerts on 5/12/16.
 */


/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ShoppingContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<CartItems> ITEMS = new ArrayList<CartItems>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, CartItems> ITEM_MAP = new HashMap<String, CartItems>();

    public static final int DUMMY_ITEM_COUNT = 25;
    public static final int DUMMY_STORE_COUNT = 4;
    public static final int ITEM_MAX_RATING = 5;
    public static final int ITEM_MIN_RATING = 1;
    public static final int ITEM_UNRATED = -1;
    public static final int INVALID_PRICE = -1;
    private static Random rand;


    public ShoppingContent() {
        // Add some sample items.
        for (int i = 1; i <= DUMMY_ITEM_COUNT; i++) {
            addItem(createDummyItem(i));
        }
        rand = new Random(System.currentTimeMillis());
    }

    private static void addItem(CartItems item) {
        ITEMS.add(item);
        ITEM_MAP.put(""+item.getID(), item);
    }

    private static CartItems createDummyItem(int position) {
        CartItems Item = new CartItems(
                String.valueOf(position),
                "01234567890" + String.valueOf((position * 11) % 900 + 100),
                "Lindt " + position,
                rand.nextInt() % (ITEM_MAX_RATING + 1)
        );
        for (int i = 1; i < DUMMY_STORE_COUNT; i++) {
            Item.setPrice("Store" + i, (rand.nextFloat() + 1) * 5);
        }
        return Item;
    }

}

