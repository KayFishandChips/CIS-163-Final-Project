package com.ajkayfishgmail.discount;

 import android.content.Intent;
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


public class CategoryActivity extends ActionBarActivity
{
    public static final String ALLCAPSTHING = "yes";
    ArrayList<ParseObject> parseArry;
    RecyclerView r_view;
    Spinner cataSpinner;
    private LinearLayoutManager myManager;
    private RecyclerView.Adapter myadapter;
    Button gobtn;
    private LocationManager locationManager;
    private LocationListener mylistener;
    Button mapButton;

    //This is a change cuz GIT is picky
    String provider;
    double latitude;
    double longitude;
    private Criteria criteria;
    ArrayList<Double> LongArray;
    ArrayList<Double> LatArray;
    double[] doubleLongArray;
    double[] doubleLatArray;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        LongArray = new ArrayList<Double>();
        LatArray = new ArrayList<Double>();

        parseArry = new ArrayList<ParseObject>();

        cataSpinner = (Spinner)findViewById(R.id.Cata_spinner);
        gobtn = (Button)findViewById(R.id.btnGo);
        mapButton = (Button)findViewById(R.id.showResults);
        String[] catagories = new String[]{"Categories","Food","Clothing", "Groceries", "Automotive", "Pets", "Entertainment"};
        ArrayAdapter<String> spinAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, catagories);
        cataSpinner.setAdapter(spinAdapter);
        r_view = (RecyclerView) findViewById(R.id.word_recycler);
        myManager = new LinearLayoutManager(this);
        r_view.setLayoutManager(myManager);
        /*
        Parse.enableLocalDatastore(this);

        Parse.initialize(this, "3Yh5EeYXEMqyf74LJd9rhQBcGJgcflLc5jrxITis", "g7kKQKrxNRHMov6yANzgNYPO2LmVYtO7AngcDrGu");
        */

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


        myadapter = new ParseObjectAdapter(parseArry);
        r_view.setAdapter(myadapter);
        gobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetData();


            }
        });


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
        doubleLongArray = new double[parseArry.size()];
        doubleLatArray = new double[parseArry.size()];
        for(int i = 0; i < parseArry.size(); i++)
        {
           ParseGeoPoint newGeo =
                   (ParseGeoPoint)parseArry.get(i).get("Point");
            LatArray.add(i,newGeo.getLatitude());
            LongArray.add(i,newGeo.getLongitude());
           ;
            doubleLongArray[i] = LongArray.get(i);

            doubleLatArray[i] = LongArray.get(i);

        }

        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("LongData", doubleLongArray);
        intent.putExtra("LatData", doubleLatArray);
        startActivity(intent);
    }
}