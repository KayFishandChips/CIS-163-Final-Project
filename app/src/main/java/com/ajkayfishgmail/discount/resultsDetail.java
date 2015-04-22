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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
        name.setText(intent.getStringExtra("Name"));
        discount.setText(intent.getStringExtra("Discount"));
        phone.setText("tel: " + intent.getStringExtra("Phone"));
        lat = intent.getDoubleExtra("Latitude", 0.0);
        longitude = intent.getDoubleExtra("Longitude", 0.0);
        SpannableString content = new SpannableString(intent.getStringExtra("Email"));
        content.setSpan(new UnderlineSpan(), 0, intent.getStringExtra("Email").length(), 0);
        content.setSpan(new ForegroundColorSpan(Color.BLUE), 0, intent.getStringExtra("Email").length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        email.setText(content);

        if(true) {//Replace true with Parse value for verified 15+
            warning.setVisibility(View.INVISIBLE);
            verifyTxt.setText("If this discount is no longer valid, please report it!");
            verifyYes.setVisibility(View.INVISIBLE);
        }

        verifyNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //parseverifyvalue -= 3;
                //if(parseverifyvalue < 1) deleteparseentry
                verifyNo.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Thank you for reporting. If enough people confirm this the listing will be removed.", Toast.LENGTH_LONG).show();
            }
        });

        verifyYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //parseverifyvalue += 1;
                verifyYes.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(),"Thank you for confirming this discount!", Toast.LENGTH_SHORT).show();
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
}