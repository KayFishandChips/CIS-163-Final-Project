package com.ajkayfishgmail.discount;

import android.content.Context;
import android.content.Intent;
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
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.zza;
import com.google.android.gms.common.api.zze;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class MainActivity extends ActionBarActivity  {

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
    private RecyclerView.Adapter myadapter;
    private LinearLayoutManager myManager;
    GoogleApiClient mGoogleApiClient;
    PlaceLikelihoodBuffer likelyPlaces;
    String placeId;
    //List categoryList;


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

                PlaceDetection();

            if(categoryFiller() == null || amount.getText().length() < 1 || locationName.getText().length() < 1 || adress.getText().length() < 1){
                Toast.makeText(getApplicationContext(), "Required information is missing. Please check your submission data.", Toast.LENGTH_LONG).show();
            }
            else{

                ParseObject DiscountObject = new ParseObject(categoryFiller());// create separate objects based on category
                DiscountObject.put("Discount", amount.getText().toString().toLowerCase()+"%");
                DiscountObject.put("Name", locationName.getText().toString().toLowerCase());
                DiscountObject.put("Location", adress.getText().toString().toLowerCase());
                DiscountObject.put("Point", Geo());
                DiscountObject.put("Phone", phone.getText().toString().toLowerCase());
                DiscountObject.put("Email", email.getText().toString().toLowerCase());
                DiscountObject.put("GoogleID",placeId);

                DiscountObject.saveInBackground();

                secondCategoryFiller();
            }
            }
        });
        parseArry = new ArrayList<ParseObject>();
        mGoogleApiClient = new GoogleApiClient() {
            @Override
            public <A extends Api.zza, R extends Result, T extends com.google.android.gms.common.api.zza.zza<R, A>> T zza(T t) {
                return null;
            }

            @Override
            public <A extends Api.zza, T extends com.google.android.gms.common.api.zza.zza<? extends Result, A>> T zzb(T t) {
                return null;
            }

            @Override
            public <L> zze<L> zzf(L l) {
                return null;
            }

            @Override
            public <C extends Api.zza> C zza(Api.zzc<C> czzc) {
                return null;
            }

            @Override
            public boolean zza(Api<?> api) {
                return false;
            }

            @Override
            public boolean zzb(Api<?> api) {
                return false;
            }

            @Override
            public boolean zza(Scope scope) {
                return false;
            }

            @Override
            public Context getContext() {
                return null;
            }

            @Override
            public Looper getLooper() {
                return null;
            }

            @Override
            public void connect() {

            }

            @Override
            public ConnectionResult blockingConnect() {
                return null;
            }

            @Override
            public ConnectionResult blockingConnect(long l, TimeUnit timeUnit) {
                return null;
            }

            @Override
            public void disconnect() {

            }

            @Override
            public void reconnect() {

            }

            @Override
            public PendingResult<Status> clearDefaultAccountAndReconnect() {
                return null;
            }

            @Override
            public void stopAutoManage(FragmentActivity fragmentActivity) {

            }

            @Override
            public boolean isConnected() {
                return false;
            }

            @Override
            public boolean isConnecting() {
                return false;
            }

            @Override
            public void registerConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {

            }

            @Override
            public boolean isConnectionCallbacksRegistered(ConnectionCallbacks connectionCallbacks) {
                return false;
            }

            @Override
            public void unregisterConnectionCallbacks(ConnectionCallbacks connectionCallbacks) {

            }

            @Override
            public void registerConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {

            }

            @Override
            public boolean isConnectionFailedListenerRegistered(OnConnectionFailedListener onConnectionFailedListener) {
                return false;
            }

            @Override
            public void unregisterConnectionFailedListener(OnConnectionFailedListener onConnectionFailedListener) {

            }

            @Override
            public void dump(String s, FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strings) {

            }

            @Override
            public int getSessionId() {
                return 0;
            }
        };
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


    public void Intent(List<ParseObject> ParseObjectList)
    {
        Intent intent = new Intent(this, DataPort.class);
    }

    public void GetData()
    {
        //ParseGeoPoint userLocation = (42.9633600,-85.6680860)
        ParseGeoPoint userLocation = Geo();
       // ParseQuery<ParseObject> query = ParseQuery.getQuery(cataSpinner.getItemAtPosition(cataSpinner.getSelectedItemPosition()).toString());
        ParseQuery<ParseObject> query =  new ParseQuery<ParseObject>("Discount");
        //query.whereNear("location", userLocation);
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

    public void PlaceDetection()
    {

        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                .getCurrentPlace(mGoogleApiClient, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces)
            {
                int i =0;
                for (PlaceLikelihood placeLikelihood : likelyPlaces)
                    if (i == 0)
                    {


                   placeId = placeLikelihood.getPlace().getId();
                    i++;

                }
                likelyPlaces.release();
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
