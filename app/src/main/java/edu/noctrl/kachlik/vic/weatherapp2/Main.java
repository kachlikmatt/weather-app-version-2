package edu.noctrl.kachlik.vic.weatherapp2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

//url for JSON object: http://craiginsdev.com/zipcodes/findzip.php?zip=60540

// XML url format:
// http://forecast.weather.gov/MapClick.php?lat={latitude}&lon={longitude}&unit=0&lg=english&FcstType=dwml

// url for XML info:
// http://forecast.weather.gov/MapClick.php?lat=41.766113999700003&lon=-88.150585000000007&unit=0&lg=english&FcstType=dwml


public class Main extends ActionBarActivity {

    LocationIO locationHolder;
    String jsonUrl;
    String latitude, longitude;
    WeatherInfo weatherInfo;
    WeatherInfoIO weatherIOHelper;
    String weatherUrl;
    WeatherInfoIO.WeatherListener weatherDownloaded;
    ArrayList<String> recentZipcodes;
    final int MAX_LIST_SIZE = 5;
    Boolean imperialPreferred;

    String[] zipStringArray;

    public void processJSON()
    {
        try
        {
            latitude = locationHolder.locationObject.getString("latitude");
            longitude = locationHolder.locationObject.getString("longitude");

            //store zipcode for later sessions

            /*
            //manually build list for testing
            recentZipcodes.clear();
            recentZipcodes.add("60540");
            recentZipcodes.add("60640");
            recentZipcodes.add("90210");
            recentZipcodes.add("50862");
            recentZipcodes.add("70943");*/

            if(zipStringArray != null)
            {
                String zipToAdd = locationHolder.locationObject.getString("zip").trim();
                Boolean shouldAdd = true;

                for(int i = 0; i < zipStringArray.length; i++)
                {
                    //zip code already exists within the list
                    if(zipStringArray[i].trim().equals(zipToAdd))
                    {
                        shouldAdd = false;
                        break;
                    }
                }

                if(shouldAdd) {
                    if(recentZipcodes.size() >= MAX_LIST_SIZE)
                        recentZipcodes.remove(0);
                    recentZipcodes.add(zipToAdd);
                }
            }
            Log.i("processJSON", "recent zip codes: " + recentZipcodes.toString());

        } catch(Exception e){}

        processWeatherInfo();
    }

    public void retrieveWeatherInfo()
    {
        weatherUrl = "http://forecast.weather.gov/MapClick.php?lat="
                     + latitude
                     + "&lon="
                     + longitude
                     + "&unit=0&lg=english&FcstType=dwml";

        weatherDownloaded = new WeatherInfoIO.WeatherListener(){
                                    @Override
                                    public void handleResult(WeatherInfo result) {
                                        weatherInfo = result;
                                        processWeatherInfo();
                                    }
                                };

        weatherIOHelper.loadFromUrl(weatherUrl, weatherDownloaded);
    }

    public void processWeatherInfo()
    {
        //this is where we handle the information retrieved from the XML parser

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        locationHolder = new LocationIO();
        latitude = "";
        longitude = "";
        jsonUrl = "http://craiginsdev.com/zipcodes/findzip.php?zip=60540";
        weatherIOHelper = new WeatherInfoIO();
        recentZipcodes = new ArrayList<>(MAX_LIST_SIZE);

        // get stored zips and measurement preference from previous sessions
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        String zipString = sharedPref.getString("zipcodes","");
        if(zipString.length() > 1)
        {
            zipString = zipString.substring(1, zipString.length()-1);
            zipStringArray = zipString.split(",");
            for(int i = 0; i < zipStringArray.length; i++)
                recentZipcodes.add(zipStringArray[i].trim());
        }

        imperialPreferred = sharedPref.getBoolean("imperial", true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationHolder.retrieveJson(jsonUrl, this);
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        if(recentZipcodes.size() > 0)
            editor.putString("zipcodes", recentZipcodes.toString());

        editor.putBoolean("imperial", imperialPreferred);

        // Commit the edits!
        editor.commit();
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

        if(id == R.id.action_7day)
        {

        }
        else if(id == R.id.action_about)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("about").setMessage("Weather information from forecast.weather.gov")
                    .setNeutralButton("okay", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int id) {
                            // FIRE ZE MISSILES!
                        }
                    });

            builder.show();
        }
        else if(id == R.id.action_units)
        {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Unit")
                    .setItems(R.array.units, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), ""+which,
                                    Toast.LENGTH_LONG).show();
                        }
                    });
            builder.show();
        }
        else if(id == R.id.action_currentWeather)
        {

        }
        else if(id == R.id.action_recentZip)
        {

        }
        else
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);

            alert.setTitle("Title");
            alert.setMessage("Message");

// Set an EditText view to get user input
            final EditText input = new EditText(this);
            alert.setView(input);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int whichButton)
                {
                    String value = input.getText().toString();
                    Toast.makeText(getApplicationContext(), value,
                            Toast.LENGTH_LONG).show();
                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });

            alert.show();
        }

        //String url = ((EditText)findViewById(R.id.URLText)).getText().toString();
        //Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        // startActivity(i);



        return super.onOptionsItemSelected(item);

    }
}
