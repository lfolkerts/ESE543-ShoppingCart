package com.mysampleapp;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


public class CartFragment extends Fragment{

    private CartItems c = new CartItems("Id", "WXYZWXYZWXYZZ", "IDK", 0);
    private static final String DIALOG_QUANTITY = "quantity";
    private static final String DIALOG_MARKET = "store";
    private Button mQuantityButton;
    private Button mMarketButton;
    private Button mAddCartItemButton;
    private Button mViewCartButton;
    private Button mDeleteCartItemButton;
    CartDatabase db;
    double itemCost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_shopping, parent, false);
        mQuantityButton = (Button)v.findViewById(R.id.cart_quantity_button);
        mQuantityButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getActivity().getFragmentManager();
                QuantityFragment dialog = new QuantityFragment();
                dialog.show(fm, DIALOG_QUANTITY);
            }
        });

        mMarketButton = (Button)v.findViewById(R.id.cart_market_button);
        mMarketButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fm = getActivity().getFragmentManager();
                MarketFragment dialog = new MarketFragment();
                dialog.show(fm, DIALOG_MARKET);
            }
        });

        mAddCartItemButton = (Button)v.findViewById(R.id.add_cart_item_button);
        mAddCartItemButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                db = new CartDatabase(getActivity());
                ShoppingCartActivity a = (ShoppingCartActivity) getActivity();
                String itemName = a.passData();
                int itemQuantity = a.passItemQuantity();
                int itemMarket = a.passItemRating(); //TODO

                //Todo: add cost here
                itemCost = itemCost+1;


                boolean isInserted = db.insertCartItem(itemName, itemQuantity,
                        itemMarket, itemCost);
                if (isInserted = true) {
                    Toast.makeText(getActivity(), "ITEMS INSERTED", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "ITEMS NOT INSERTED", Toast.LENGTH_LONG).show();
                }
                db.deleteDuplicates();
            }
        });

        mViewCartButton = (Button)v.findViewById(R.id.view_cart_button);
        mViewCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor result = db.getAllData();
                if (result.getCount() == 0) {
                    showMessage("Error", "No Data Found");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while (result.moveToNext()) {
                    buffer.append("Id " + result.getString(0) + "\n");
                    buffer.append("Item name " + result.getString(1) + "\n");
                    buffer.append("Quantity " + result.getString(2) + "\n");
                    buffer.append("Priority " + result.getString(3) + "\n");
                    buffer.append("Cost " + result.getString(4) + "\n\n");
                }
                showMessage("Data", buffer.toString());
            }
        });

        mDeleteCartItemButton = (Button)v.findViewById(R.id.delete_cart_item_button);
        mDeleteCartItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteData();
                Toast.makeText(getActivity(), "DATA DELETED", Toast.LENGTH_LONG).show();
            }
        });
        return v;
    }

    public void showMessage (String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}
