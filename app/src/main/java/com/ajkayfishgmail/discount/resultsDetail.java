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

import org.w3c.dom.Text;


public class resultsDetail extends ActionBarActivity {

    private Button callButton;
    private Button nav;

    private TextView phone;
    private TextView warning;
    private TextView name;
    private TextView email;
    private TextView discount;

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



        if(true)//Replace true with Parse value for verified 20+
            warning.setVisibility(View.INVISIBLE);

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
                        "&body=" + Uri.encode("I have heard you offer the following student discount:\n\n" + discount.getText() + "\n\nCan you please confirm if this is accurate?\nThank you!");

                Uri emailURI = Uri.parse(uriText);
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(emailURI);
                startActivity(Intent.createChooser(emailIntent, "Send email"));
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_results_detail, menu);
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
