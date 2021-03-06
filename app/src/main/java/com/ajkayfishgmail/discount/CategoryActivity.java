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
 import java.util.Date;
 import java.util.List;
 import java.util.concurrent.TimeUnit;


public class CategoryActivity extends ActionBarActivity implements ParseObjectAdapter.OnItemSelectListener
{
    private ArrayList<ParseObject> parseArry;
    private RecyclerView r_view;
    private Spinner cataSpinner;
    private LinearLayoutManager myManager;
    private RecyclerView.Adapter myadapter;
    private Button gobtn;
    private LocationManager locationManager;
    private LocationListener mylistener;
    private Button mapButton;
    private String provider;
    private double latitude;
    private double longitude;
    private Criteria criteria;
    private ArrayList<Double> LongArray;
    private ArrayList<Double> LatArray;
    private double[] doubleLongArray;
    private double[] doubleLatArray;
    private ArrayList<String> nameArray;
    private ArrayList<String> discArray;

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


        myadapter = new ParseObjectAdapter(parseArry, this);
        r_view.setAdapter(myadapter);
        gobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetData();
            }
        });


    }

        public void GetData(){
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
                    parseArry.clear();
                    for(int i = 0; i < parseObjects.size();i++)
                    {

                        if (!dateCheck(parseObjects.get(i)))
                        {
                            parseArry.add(parseObjects.get(i));
                        }

                    }
                    myadapter.notifyDataSetChanged();
                }
                else
                {

                }
            }
        });
    }

    public Boolean dateCheck(ParseObject i)
    {
        Date date1;
        Date dateNow = new Date();
        date1 = i.getDate("updatedAt");
        long differnce = dateNow.getTime() - date1.getTime();
        long diffencedays = TimeUnit.MILLISECONDS.toDays(differnce);
        if (diffencedays >= 90)
        {
            i.put("Verification", i.getNumber("Verification").intValue() -1);
            i.saveInBackground();

        }
        if(i.getNumber("Verification").intValue() >= -15)//just bit a random value here
        {
            i.deleteInBackground();
            return true;
        }
        return false;
    }

    @Override
    public void onItemSelect(int i) {
        ParseGeoPoint location =
                (ParseGeoPoint)parseArry.get(i).get("Point");
        Intent intent = new Intent(this, resultsDetail.class);
        intent.putExtra("Discount", (String) parseArry.get(i).get("Discount"));
        intent.putExtra("Name", (String) parseArry.get(i).get("Name"));
        intent.putExtra("Latitude", location.getLatitude());
        intent.putExtra("Longitude", location.getLongitude());
        intent.putExtra("Phone", (String) parseArry.get(i).get("Phone"));
        intent.putExtra("Email", (String) parseArry.get(i).get("Email"));
        intent.putExtra("Verification", (Integer) parseArry.get(i).get("Verification"));
        intent.putExtra("objectId", (String) parseArry.get(i).getObjectId());
        intent.putExtra("Class", (String) parseArry.get(i).getClassName());
        startActivity(intent);
    }

    private class MyLocationListener implements LocationListener{

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

    public ParseGeoPoint getLocation(){
        ParseGeoPoint point = new ParseGeoPoint(latitude, longitude);
        return point;
    }

    public void sendMapData(View view){
        doubleLongArray = new double[parseArry.size()];
        doubleLatArray = new double[parseArry.size()];
        nameArray = new ArrayList<String>(parseArry.size());
        discArray = new ArrayList<String>(parseArry.size());
        for(int i = 0; i < parseArry.size(); i++)
        {
           ParseGeoPoint newGeo =
                   (ParseGeoPoint)parseArry.get(i).get("Point");
            LatArray.add(i,newGeo.getLatitude());
            LongArray.add(i,newGeo.getLongitude());

            doubleLongArray[i] = LongArray.get(i);
            doubleLatArray[i] = LatArray.get(i);

            nameArray.add((String)parseArry.get(i).get("Name"));
            discArray.add((String)parseArry.get(i).get("Discount"));
        }

        Intent intent = new Intent(this, MapsActivity.class);
        intent.putExtra("LongData", doubleLongArray);
        intent.putExtra("LatData", doubleLatArray);
        intent.putStringArrayListExtra("Names", nameArray);
        intent.putStringArrayListExtra("Discounts", discArray);
        startActivity(intent);
    }
}