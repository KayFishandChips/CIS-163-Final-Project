package com.ajkayfishgmail.discount;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;


public class SubmitActivity extends ActionBarActivity
{
    private EditText name, location, discount, email, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);

        name = (EditText) findViewById(R.id.name_field);
        location = (EditText) findViewById(R.id.location_field);
        discount = (EditText) findViewById(R.id.discount_field);
        email = (EditText) findViewById(R.id.name_field);
        phone = (EditText) findViewById(R.id.name_field);
    }

    public void submitData(View view)
    {
        // send stuff to parse
        // clear fields
        name.setText("");
        location.setText("");
        discount.setText("");
        email.setText("");
        phone.setText("");
        // progress dialog

        // launch results-map
    }

}