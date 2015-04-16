package com.ajkayfishgmail.discount;

 import android.content.Intent;
 import android.support.v7.app.ActionBarActivity;
 import android.os.Bundle;
 import android.view.View;


public class CategoryActivity extends ActionBarActivity
{
    public static final String ALLCAPSTHING = "yes";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
    }

    public void launchMap(View view)
    {
        Intent intent = new Intent(this, ResultsMap.class);
        String thing = "yes";
        intent.putExtra(ALLCAPSTHING, thing);
        startActivity(intent);
    }
}