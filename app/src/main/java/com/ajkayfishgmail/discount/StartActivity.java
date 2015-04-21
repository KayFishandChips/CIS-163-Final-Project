package com.ajkayfishgmail.discount;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.Parse;


public class StartActivity extends ActionBarActivity
{
    // SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        /*prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(savedInstanceState == null)
        {
            Intent intent = new Intent(this, TermsActivity.class);
            startActivity(intent);
        }
        else // later we can use this if we update terms and they need to re-accept
        {
            Toast.makeText(
                    StartActivity.this, "Welcome Back!", Toast.LENGTH_LONG
                          ).show();
        }*/
        try {
            Parse.enableLocalDatastore(this);

            Parse.initialize(
                    this, "3Yh5EeYXEMqyf74LJd9rhQBcGJgcflLc5jrxITis",
                    "g7kKQKrxNRHMov6yANzgNYPO2LmVYtO7AngcDrGu");
        }catch(Exception e){}
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            Intent intent = new Intent(this, TermsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void launchSearch(View view)
    {
        Intent intent = new Intent(this, CategoryActivity.class);
        startActivity(intent);
    }

    public void launchSubmit(View view)
    {
        Intent intent = new Intent(this, SubmitActivity.class);
        startActivity(intent);
    }
}
