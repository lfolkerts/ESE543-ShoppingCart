package com.mysampleapp;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class CartListFragment extends ListFragment {
    private ArrayList<CartItems> mShoppingCart;
    public static final String TAG = "CartListFragment";

    CartDatabase mDbHelper = new CartDatabase(getContext());
    OnDataPass dataPasser;

    public interface OnDataPass {
        public void onDataPass(String data);
    }

    public void passData(String data) {
        dataPasser.onDataPass(data);
    }

    @Override
    public void onAttach(Activity a) {
        super.onAttach(a);
        dataPasser = (CartListFragment.OnDataPass) a;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getActivity().setTitle(R.string.cart_title);
        mShoppingCart = CartContents.get(getActivity()).getCart();
        ShoppingCartAdapter adapter = new ShoppingCartAdapter(mShoppingCart);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        CartItems c = ((ShoppingCartAdapter)getListAdapter()).getItem(position);
        CartListActivity a = new CartListActivity();
        passData(c.getID());

    }

    private class ShoppingCartAdapter extends ArrayAdapter<CartItems> {
        public ShoppingCartAdapter(ArrayList<CartItems> cart) {
            super(getActivity(), 0, cart);
        }
        //----------------------------------------------------
        // Inflates the view of the initial screen
        //----------------------------------------------------
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_cart, null);
            }
            CartItems c = getItem(position);
            TextView titleTextView = (TextView) convertView.findViewById(R.id.cart_list_item_titleTextView);
            titleTextView.setText(c.getID());

            return convertView;
        }
    }
}
