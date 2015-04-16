package com.ajkayfishgmail.discount;

 import android.content.Intent;
 import android.support.v7.app.ActionBarActivity;
 import android.os.Bundle;
 import android.view.View;
 import android.widget.ArrayAdapter;
 import android.widget.Spinner;


public class CategoryActivity extends ActionBarActivity
{
    public static final String ALLCAPSTHING = "yes";

    // declare spinner components
    private Spinner catSpinner1, catSpinner2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // instantiate spinners
        String[] categories = new String[]
                {
                        "Categories","Food","Clothing", "Groceries",
                        "Automotive", "Pets", "Entertainment"
                };
        ArrayAdapter<String> spinAdapter = new ArrayAdapter <String> (
                this, android.R.layout.simple_spinner_item, categories);
        catSpinner1 = (Spinner)findViewById(R.id.spinCat1);
        catSpinner1.setAdapter(spinAdapter);
        catSpinner2 = (Spinner)findViewById(R.id.spinCat2);
        catSpinner2.setAdapter(spinAdapter);
    }

    public void launchMap(View view)
    {
        Intent intent = new Intent(this, ResultsMap.class);
        String thing = "yes";
        intent.putExtra(ALLCAPSTHING, thing);
        startActivity(intent);
    }
}