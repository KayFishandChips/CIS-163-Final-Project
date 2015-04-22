package com.ajkayfishgmail.discount;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.w3c.dom.Text;


public class resultsDetail extends ActionBarActivity {

    private Button callButton;
    private Button nav;
    private Button verifyYes;
    private Button verifyNo;

    private TextView phone;
    private TextView warning;
    private TextView name;
    private TextView email;
    private TextView discount;
    private TextView verifyTxt;

    private double lat, longitude;
    private int verification;
    private String objectId;
    private ParseObject parseObject;
    private String className;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results_detail);

        name = (TextView) findViewById(R.id.bus_name_txt);
        warning = (TextView) findViewById(R.id.warning_txt);
        callButton = (Button) findViewById(R.id.call_button);
        nav = (Button) findViewById(R.id.nav_button);
        phone = (TextView) findViewById(R.id.phone_txt);
        email = (TextView) findViewById(R.id.email_txt);
        discount = (TextView) findViewById(R.id.discount_details);
        verifyTxt = (TextView) findViewById(R.id.verify_txt);
        verifyYes = (Button) findViewById(R.id.verify_yes);
        verifyNo = (Button) findViewById(R.id.verify_no);

        Intent intent = getIntent();
        objectId = intent.getStringExtra("objectId");
        className = intent.getStringExtra("Class");
        setParseObject();
        name.setText(intent.getStringExtra("Name"));
        discount.setText(intent.getStringExtra("Discount"));
        phone.setText(intent.getStringExtra("Phone"));
        lat = intent.getDoubleExtra("Latitude", 0.0);
        longitude = intent.getDoubleExtra("Longitude", 0.0);
        verification = intent.getIntExtra("Verification", 15);
        SpannableString content = new SpannableString(intent.getStringExtra("Email"));
        content.setSpan(new UnderlineSpan(), 0, intent.getStringExtra("Email").length(), 0);
        content.setSpan(new ForegroundColorSpan(Color.BLUE), 0, intent.getStringExtra("Email").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        email.setText(content);


        if(verification >=20) {
            warning.setVisibility(View.INVISIBLE);
            verifyTxt.setText("This discount has been verified. If this discount is no longer valid, please report it!");
            verifyYes.setVisibility(View.INVISIBLE);
        }

        verifyNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                verification -= 3;
                try{
                    parseObject.increment("Verification", -3);
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"This discount is no longer in the database.", Toast.LENGTH_SHORT).show();
                }

                if(verification < 1){
                    try{
                        parseObject.deleteInBackground();
                    }catch (Exception e){
                        Toast.makeText(getApplicationContext(),"This discount is no longer in the database.", Toast.LENGTH_SHORT).show();
                    }

                }
                else
                try{
                    parseObject.saveInBackground();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(),"This discount is no longer in the database.", Toast.LENGTH_SHORT).show();
                }
                verifyNo.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Thank you for reporting. If enough people confirm this the listing will be removed.", Toast.LENGTH_LONG).show();
            }
        });

        verifyYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verification += 1;
                try{
                    parseObject.increment("Verification");
                    parseObject.saveInBackground();
                    verifyYes.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"Thank you for confirming this discount!", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    verifyYes.setVisibility(View.INVISIBLE);
                    Toast.makeText(getApplicationContext(),"This discount is no longer in the database.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "tel:" + phone.getText().toString().trim();
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                startActivity(intent);
            }
        });

        nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("geo:0,0?q=" + lat + "," + longitude + "(" + name.getText() + ")");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });

        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uriText =
                "mailto:" + email.getText() + "?subject=" + Uri.encode("Student Discount Verification") +
                        "&body=" + Uri.encode("I have heard you offer the following student discount:\n\n" + discount.getText() + "\n\nCan you please confirm if this is accurate?\n\nThank you!");

                Uri emailURI = Uri.parse(uriText);
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(emailURI);
                startActivity(Intent.createChooser(emailIntent, "Send email"));
            }
        });

    }


    private void setParseObject() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery(className);
        query.getInBackground(objectId, new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    parseObject = object;
                } else {

                }
            }
        });
    }

}