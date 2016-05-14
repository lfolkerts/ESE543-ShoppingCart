package com.mysampleapp;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobile.util.ThreadUtils;
import com.mysampleapp.CartItems;
import com.mysampleapp.demo.nosql.DemoNoSQLTableBase;
import com.mysampleapp.demo.nosql.DemoNoSQLTableFactory;
import com.mysampleapp.demo.nosql.DemoNoSQLTableRatedItemsTest;
import com.mysampleapp.demo.nosql.DynamoDBUtils;

public class CartFragment extends Fragment{

    private CartItems c = new CartItems("Id", "WXYZWXYZWXYZZ", "IDK", 0);
    private static final String DIALOG_QUANTITY = "quantity";
    private static final String DIALOG_MARKET = "store";
    private Button mQuantityButton;
    private Button mMarketButton;
    private Button mBackendButton;
    private Button mAddCartItemButton;
    private Button mViewCartButton;
    private Button mDeleteCartItemButton;
    /** The NoSQL Table demo operations will be run against. */
    private DemoNoSQLTableRatedItemsTest demoTable;
    double itemCost;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        demoTable = DemoNoSQLTableFactory.instance(getContext().getApplicationContext())
                .getNoSQLTableByTableName("ESE543 TEST");
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

                ShoppingCartActivity a = (ShoppingCartActivity) getActivity();
                CartItems itemB = null;
                CartItems itemA = a.passItem();
                if(itemA == null){ itemB = new CartItems("id", "00000000000000","test",0);}
                final CartItems item = (itemA==null)?itemB:itemA;
                item.setQuantity(a.passItemQuantity());
                item.setPrice("Generic", (float) 3.99);
                String itemMarket = "Generic";

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            demoTable.insertData(item.getID(),item.getBarcode(),  item.getBestPrice(), item.getQuantity());
                        } catch (final AmazonClientException ex) {
                            // The insertSampleData call already logs the error, so we only need to
                            // show the error dialog to the user at this point.
                          return;
                        }
                        ThreadUtils.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
                                dialogBuilder.setTitle(R.string.nosql_dialog_title_added_sample_data_text);
                                dialogBuilder.setMessage(R.string.nosql_dialog_message_added_sample_data_text);
                                dialogBuilder.setNegativeButton(R.string.nosql_dialog_ok_text, null);
                                dialogBuilder.show();
                                     }
                        });
                    }
                }).start();

                Toast.makeText(getActivity(), "ITEMS INSERTED", Toast.LENGTH_LONG).show();

                //TODO delete duplicates LARS
            }
        });

        mViewCartButton = (Button)v.findViewById(R.id.view_cart_button);
        mViewCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //TODO show cart here
                showMessage("Data", "Items");
            }
        });

        mDeleteCartItemButton = (Button)v.findViewById(R.id.delete_cart_item_button);
        mDeleteCartItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                demoTable.removeSampleData();
                Toast.makeText(getActivity(), "DATA DELETED", Toast.LENGTH_LONG).show();
            }
        });

        mBackendButton = (Button)v.findViewById(R.id.settings_button);
        mBackendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShoppingCartActivity a = (ShoppingCartActivity) getActivity();
                Intent backendIntent = new Intent(a, BackendActivity.class);
                a.startActivity(backendIntent);
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
