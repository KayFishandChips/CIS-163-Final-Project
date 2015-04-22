package com.ajkayfishgmail.discount;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;


public class SubmitActivity extends FragmentActivity
{
    EditText locationName;
    EditText adress;
    EditText amount;
    Button submit_Btn;
    EditText email;
    EditText phone;
    Button getInfo;
    Spinner cataSpinner;
    Spinner cata2;
    double latitude;
    double longitude;
    private LinearLayoutManager myManager;

    LocationManager locationManager;
    private LocationListener mylistener;
    Criteria criteria;
    String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit);



        cataSpinner = (Spinner)findViewById(R.id.Cata_spinner);
        String[] catagories = new String[]{"Categories","Food","Clothing", "Groceries", "Automotive", "Pets", "Entertainment"};
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, catagories);
        cataSpinner.setAdapter(spinAdapter);
        cata2 = (Spinner)findViewById(R.id.cata2);
        cata2.setAdapter(spinAdapter);
        submit_Btn = (Button) findViewById(R.id.submit_User_Data_Btn);
        myManager = new LinearLayoutManager(this);
        submit_Btn = (Button)findViewById(R.id.submit_User_Data_Btn);
        locationName = (EditText) findViewById(R.id.name_field);
        //adress = (EditText)findViewById(R.id.location_field);
        amount = (EditText)findViewById(R.id.discount_field);
        email = (EditText) findViewById(R.id.email_Box);
        phone = (EditText)findViewById(R.id.Phone_box);
        getInfo = (Button)findViewById(R.id.retrieve_Btn);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
        criteria.setCostAllowed(false);
        provider = locationManager.getBestProvider(criteria,false);
        Location location = locationManager.getLastKnownLocation(provider);
        mylistener = new MyLocationListener();
        if(location !=null)
        {
            mylistener.onLocationChanged(location);
        }

        locationManager.requestLocationUpdates(provider,3000,5,mylistener);

        submit_Btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(categoryFiller() == null || amount.getText().length() < 1 || locationName.getText().length() < 1/*|| adress.getText().length() < 1*/){
                    Toast.makeText(getApplicationContext(), "Required information is missing. Please check your submission data.", Toast.LENGTH_LONG).show();
                }
                else{

                    ParseGeoPoint userLocation = getLocation();

                    ParseObject DiscountObject = new ParseObject(categoryFiller());// create separate objects based on category
                    DiscountObject.put("Discount", amount.getText().toString().toLowerCase());
                    DiscountObject.put("Name", locationName.getText().toString().toLowerCase());
                    //DiscountObject.put("Location", adress.getText().toString().toLowerCase());
                    DiscountObject.put("Point", getLocation());
                    DiscountObject.put("Phone", phone.getText().toString().toLowerCase());
                    DiscountObject.put("Email", email.getText().toString().toLowerCase());
                    //DiscountObject.put("Details")
                    //DiscountObject.put("VoteValue");


                    DiscountObject.saveInBackground();

                    secondCategoryFiller();
                }
            }
        });
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
            DiscountObject.put("Discount", amount.getText().toString().toLowerCase());
            DiscountObject.put("Name", locationName.getText().toString().toLowerCase());
            DiscountObject.put("Location", adress.getText().toString().toLowerCase());
            DiscountObject.put("Point", getLocation());
            DiscountObject.put("Phone", phone.getText().toString().toLowerCase());
            DiscountObject.put("Email", email.getText().toString().toLowerCase());
            DiscountObject.saveInBackground();

            Clear();
        }
        Clear();
    }

    public void Clear()
    {

        amount.setText("");
        locationName.setText("");
        //adress.setText("");
        phone.setText("");
        email.setText("");
        cataSpinner.setSelection(0);
        cata2.setSelection(0);
    }

    public ParseGeoPoint getLocation()
    {

        ParseGeoPoint point = new ParseGeoPoint(latitude, longitude);
        return point;

    }

    private class MyLocationListener implements LocationListener
    {

        public void onConnected()
        {

        }

        @Override
        public void onLocationChanged(Location location)
        {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

}