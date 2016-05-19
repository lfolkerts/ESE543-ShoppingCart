package com.mysampleapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public abstract class SingleFragmentActivity extends FragmentActivity {
    protected abstract Fragment createFragment();
    public final static String searchedItem = "com.mysampleapp";
    String item;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        final EditText userinput = (EditText) findViewById(R.id.editText2);
        Button send = (Button) findViewById(R.id.button3);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (userinput.getText() != null) {
                    item = userinput.getText().toString();

                } else {
                    item = "hi";
                }

                Intent intent1 = new Intent(getApplicationContext(), CartListActivity.class);
                intent1.putExtra(searchedItem, item);
                userinput.setText("");
                startActivity(intent1);

            }
        });

        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = createFragment();
            fm.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }


    }
}
