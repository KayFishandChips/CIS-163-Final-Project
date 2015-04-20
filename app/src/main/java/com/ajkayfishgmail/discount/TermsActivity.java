package com.ajkayfishgmail.discount;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;


public class TermsActivity extends ActionBarActivity
{
    // SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms);



        /*prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor ped = prefs.edit();
        ped.putInt("versionAccepted", 1); // put version # here
        ped.apply();*/
    }
}
