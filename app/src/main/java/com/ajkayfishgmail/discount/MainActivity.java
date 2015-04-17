package com.ajkayfishgmail.discount;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
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

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;


import java.sql.Array;
import java.util.ArrayList;
import java.util.List;



public class MainActivity extends FragmentActivity  {

    ArrayList<ParseObject> parseArry;
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
    private LocationManager locationManager;
    private Criteria criteria;
    private RecyclerView.Adapter myadapter;
    private LinearLayoutManager myManager;
    private LocationListener mylistener;
    String placeId;
    String provider;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cataSpinner = (Spinner)findViewById(R.id.Cata_spinner);
        String[] catagories = new String[]{"Categories","Food","Clothing", "Groceries", "Automotive", "Pets", "Entertainment"};
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, catagories);
        cataSpinner.setAdapter(spinAdapter);
        cata2 = (Spinner)findViewById(R.id.cata2);
        cata2.setAdapter(spinAdapter);
        myManager = new LinearLayoutManager(this);

        r_view = (RecyclerView) findViewById(R.id.word_recycler);
        r_view.setLayoutManager(myManager);

        submit_Btn = (Button)findViewById(R.id.submit_User_Data_Btn);
        locationName = (EditText) findViewById(R.id.name_field);
        adress = (EditText)findViewById(R.id.location_field);
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



        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "3Yh5EeYXEMqyf74LJd9rhQBcGJgcflLc5jrxITis", "g7kKQKrxNRHMov6yANzgNYPO2LmVYtO7AngcDrGu");
//        mGoogleApiClient = new GoogleApiClient.Builder();

        MapsActivity fragment = new MapsActivity();


        submit_Btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            if(categoryFiller() == null || amount.getText().length() < 1 || locationName.getText().length() < 1 || adress.getText().length() < 1){
                Toast.makeText(getApplicationContext(), "Required information is missing. Please check your submission data.", Toast.LENGTH_LONG).show();
            }
            else{

                ParseObject DiscountObject = new ParseObject(categoryFiller());// create separate objects based on category
                DiscountObject.put("Discount", amount.getText().toString().toLowerCase());
                DiscountObject.put("Name", locationName.getText().toString().toLowerCase());
                DiscountObject.put("Location", adress.getText().toString().toLowerCase());
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
        parseArry = new ArrayList<ParseObject>();


        myadapter = new ParseObjectAdapter(parseArry);
        r_view.setAdapter(myadapter);
       getInfo.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v)
           {
           GetData();


           }
       });
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

    public ParseGeoPoint getLocation()
    {

        ParseGeoPoint point = new ParseGeoPoint(latitude, longitude);
        return point;

    }

    public void sendMapData(View view)
    {

        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("ParseData",parseArry);
    }



    public void GetData()
    {
        //ParseGeoPoint userLocation = (42.9633600,-85.6680860)
        ParseGeoPoint userLocation = getLocation();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(cataSpinner.getItemAtPosition(cataSpinner.getSelectedItemPosition()).toString());
        //ParseQuery<ParseObject> query =  new ParseQuery<ParseObject>("Discount");
        query.whereNear("Point", userLocation);
        query.setLimit(10);
        query.findInBackground(new FindCallback<ParseObject>()
        {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e)
            {
             if (e==null)
             {
                for(int i = 0; i < parseObjects.size();i++)
                {
                 parseArry.add(parseObjects.get(i));

                }
                 myadapter.notifyDataSetChanged();
             }
                else
             {

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
    public ArrayList<ParseObject> getObjects(){
        return parseArry;
    }
}
