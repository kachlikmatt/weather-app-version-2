package edu.noctrl.kachlik.vic.weatherapp2;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

//url for JSON object: http://craiginsdev.com/zipcodes/findzip.php?zip=60540

// XML url format:
// http://forecast.weather.gov/MapClick.php?lat={latitude}&lon={longitude}&unit=0&lg=english&FcstType=dwml

// url for XML info:
// http://forecast.weather.gov/MapClick.php?lat=41.766113999700003&lon=-88.150585000000007&unit=0&lg=english&FcstType=dwml


public class Main extends ActionBarActivity {

    LocationIO locationHolder;
    String jsonUrl;

    public void processJSON()
    {
        try
        {
            Log.i("processJSON()", locationHolder.locationObject.toString(4));
        }
        catch(Exception e){}

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationHolder = new LocationIO();
        jsonUrl = "http://craiginsdev.com/zipcodes/findzip.php?zip=60540";
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationHolder.retrieveJson(jsonUrl, this);
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
