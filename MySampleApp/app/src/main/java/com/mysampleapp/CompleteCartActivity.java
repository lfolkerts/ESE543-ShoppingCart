package com.mysampleapp;

import android.app.Fragment;


public class CompleteCartActivity extends SingleFragmentActivity {

    protected Fragment createFragment() {
        return new CompleteCartFragment();
    }

}
