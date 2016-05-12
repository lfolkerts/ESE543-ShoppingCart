package com.mysampleapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;


public class QuantityFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_quantity, null);
        NumberPicker np = (NumberPicker) v.findViewById(R.id.numberPicker);
        np.setMaxValue(20);
        np.setMinValue(0);
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                ShoppingCartActivity a = (ShoppingCartActivity) getActivity();
                int itemQuantity = newVal;
                a.onItemQuantityPass(itemQuantity);
            }
        });
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.item_quantity_title)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }
}