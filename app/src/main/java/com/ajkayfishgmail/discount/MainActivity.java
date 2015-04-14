package com.ajkayfishgmail.discount;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class MainActivity extends ActionBarActivity  {

    EditText locationName;
    EditText adress;
    EditText amount;
    Button submit_Btn;
    EditText longitudeBox;
    EditText latitudeBox;
    EditText email;
    EditText phone;
    Button getInfo;
    Spinner cataSpinner;
    Spinner cata2;
    RecyclerView r_view;
    List<ParseObject> parseList;
    private RecyclerView.Adapter myadapter;
    private LinearLayoutManager myManager;
    //List categoryList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cataSpinner = (Spinner)findViewById(R.id.Cata_spinner);
        //Dropdown menu
        String[] catagories = new String[]{"Categories","Food",
                "Clothing", "Groceries", "Automotive", "Pets",
                "Entertainment"};//Category Name
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                catagories);//Pass cetegories to drop down
        cataSpinner.setAdapter(spinAdapter);
        cata2 = (Spinner)findViewById(R.id.cata2);
        cata2.setAdapter(spinAdapter);
        myManager = new LinearLayoutManager(this); //Recycler layout
        // manager
        myadapter = new ParseObjectAdapter(parseList);//Recyclerview
        // Adapter
        r_view = (RecyclerView) findViewById(R.id.word_recycler);
        r_view.setLayoutManager(myManager);
        r_view.setAdapter(myadapter);

        submit_Btn = (Button)findViewById(R.id.submit_User_Data_Btn);
        locationName = (EditText) findViewById(R.id.name_field);
        adress = (EditText)findViewById(R.id.location_field);
        amount = (EditText)findViewById(R.id.discount_field);
        longitudeBox = (EditText)findViewById(R.id.longitude_box);
        latitudeBox = (EditText)findViewById(R.id.Latitude_box);
        email = (EditText) findViewById(R.id.email_Box);
        phone = (EditText)findViewById(R.id.Phone_box);
        getInfo = (Button)findViewById(R.id.retrieve_Btn);

        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "3Yh5EeYXEMqyf74LJd9rhQBcGJgcflLc5jrxITis", "g7kKQKrxNRHMov6yANzgNYPO2LmVYtO7AngcDrGu");

        submit_Btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                /*Check for required data. If missing, Toast to user*/
                if(categoryFiller() == null || amount.getText().equals
                        ("")||locationName.getText().equals("")
                        ||adress.getText().equals("")){
                    Toast.makeText(getApplicationContext(),
                            "Required information has been omitted. " +
                                    "Please check the information " +
                                    "submitted.",Toast.LENGTH_LONG)
                            .show();
                }
                else{/*Required data provided, submit data to Parse*/
                    ParseObject DiscountObject = new ParseObject(categoryFiller());// create separate objects based on category
                    DiscountObject.put("Discount", amount.getText().toString().toLowerCase()+"%");
                    DiscountObject.put("Name", locationName.getText().toString().toLowerCase());
                    DiscountObject.put("Location", adress.getText().toString().toLowerCase());
                    DiscountObject.put("Point", Geo());
                    DiscountObject.put("Phone", phone.getText().toString().toLowerCase());
                    DiscountObject.put("Email", email.getText().toString().toLowerCase());

                    DiscountObject.saveInBackground();

                    secondCategoryFiller();
                }
            }
        });

       getInfo.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v)
           {
           GetData();
           }
       });
    }


    public void Intent(List<ParseObject> ParseObjectList)
    {
        Intent intent = new Intent(this, DataPort.class);
    }

    public void GetData()
    {
        //ParseGeoPoint userLocation = (42.9633600,-85.6680860)
        ParseGeoPoint userLocation = Geo();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(cataSpinner.getItemAtPosition(cataSpinner.getSelectedItemPosition()).toString());
        query.whereNear("location", userLocation);
        query.setLimit(10);
        query.findInBackground(new FindCallback<ParseObject>()
        {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e)
            {
                myadapter.notifyDataSetChanged();
            }
        });
    }


    public ParseGeoPoint Geo()
    {
        if (longitudeBox.getText().toString().equals("") || latitudeBox.getText().toString().equals(""))
        {
            longitudeBox.setText("0");
            latitudeBox.setText("0");
        }
        float longitude = Float.valueOf(longitudeBox.getText().toString());
        float latitude = Float.valueOf(longitudeBox.getText().toString());

        ParseGeoPoint point = new ParseGeoPoint(latitude, longitude);

        return  point;
    }


    public String categoryFiller()
    {
        String n;
        if( cataSpinner.getItemAtPosition(cataSpinner.getSelectedItemPosition()).toString().equals("Categories"))
        {
            n = null;
        }
        else
        {
            n = cataSpinner.getItemAtPosition(cataSpinner.getSelectedItemPosition()).toString();
        }
        return n;
    }

    public void secondCategoryFiller()
    {
        String n;
        if( cata2.getItemAtPosition(cata2.getSelectedItemPosition()).toString().equals("Categories"))
        {
             n = null;
        }
        else
        {

           n = cata2.getItemAtPosition(cata2.getSelectedItemPosition()).toString();

        }
        if(n != null)
        {
            ParseObject DiscountObject = new ParseObject(n);// create separate objects based on category
            DiscountObject.put("Discount", amount.getText().toString().toLowerCase()+"%");
            DiscountObject.put("Name", locationName.getText().toString().toLowerCase());
            DiscountObject.put("Location", adress.getText().toString().toLowerCase());
            DiscountObject.put("Point", Geo());
            DiscountObject.put("Phone", phone.getText().toString().toLowerCase());
            DiscountObject.put("Email", email.getText().toString().toLowerCase());
            DiscountObject.saveInBackground();

            Clear();
        }
        Clear();
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
        cataSpinner.setSelection(0);
        cata2.setSelection(0);
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
