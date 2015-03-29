package com.ajkayfishgmail.discount;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.parse.Parse;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

import org.json.JSONArray;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    EditText locationName;
    EditText adress;
    EditText amount;
    Button submit_Btn;
    EditText longitudeBox;
    EditText latitudeBox;
    EditText email;
    EditText phone;
    CheckBox cat1;
    CheckBox cat2;
    CheckBox cat3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        submit_Btn = (Button)findViewById(R.id.submit_User_Data_Btn);
        locationName = (EditText) findViewById(R.id.name_field);
        adress = (EditText)findViewById(R.id.location_field);
        amount = (EditText)findViewById(R.id.discount_field);
        longitudeBox = (EditText)findViewById(R.id.longitude_box);
        latitudeBox = (EditText)findViewById(R.id.Latitude_box);
        email = (EditText) findViewById(R.id.email_Box);
        phone = (EditText)findViewById(R.id.Phone_box);
        cat1 = (CheckBox)findViewById(R.id.cat1);
        cat2 = (CheckBox)findViewById(R.id.cat2);
        cat3 = (CheckBox)findViewById(R.id.cat3);

        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "3Yh5EeYXEMqyf74LJd9rhQBcGJgcflLc5jrxITis", "g7kKQKrxNRHMov6yANzgNYPO2LmVYtO7AngcDrGu");

        submit_Btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                //amount.getText().toString().equals("")


                    if (longitudeBox.getText().toString().equals("") || latitudeBox.getText().toString().equals(""))
                    {
                        longitudeBox.setText("0");
                        latitudeBox.setText("0");
                    }
                    float longitude = Float.valueOf(longitudeBox.getText().toString());
                    float latitude = Float.valueOf(longitudeBox.getText().toString());

                ParseGeoPoint point = new ParseGeoPoint(latitude, longitude);

                ParseObject DiscountObject = new ParseObject("Discount");
                DiscountObject.put("Discount", amount.getText().toString()+"%");
                DiscountObject.put("Name", locationName.getText().toString());
                DiscountObject.put("Location", adress.getText().toString());
                DiscountObject.put("Point", point);
                DiscountObject.put("CategoryArray", BusinessFiller());
                DiscountObject.put("Phone", phone.getText().toString());
                DiscountObject.put("Email", email.getText().toString());
                DiscountObject.saveInBackground();

                Clear();
            }
        });




    }

    public JSONArray BusinessFiller()
    {
        JSONArray categorieArray = new JSONArray();
        if (cat1.isChecked())
        {
            categorieArray.put(cat1.getText().toString());
        }
        if (cat2.isChecked())
        {
            categorieArray.put(cat2.getText().toString());
        }
        if (cat3.isChecked())
        {
            categorieArray.put(cat3.getText().toString());
        }
        return categorieArray;
    }

    public void Clear()
    {
        longitudeBox.setText("");
        latitudeBox.setText("");
        amount.setText("");
        locationName.setText("");
        adress.setText("");
        phone.setText("");
        email.setText("");

        if (cat1.isChecked())
        {
            cat1.toggle();
        }
        if (cat2.isChecked())
        {
            cat2.toggle();
        }
        if (cat3.isChecked())
        {
            cat3.toggle();
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
