package edu.noctrl.kachlik.vic.weatherapp2;

import android.content.Context;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Vic on 5/10/2015.
 *
 * Parses JSON data retrieved from an inputstream and returns
 * a JSON object to the calling Activity.
 */
public class LocationIO implements Downloader.DownloadListener<JSONObject> {

    JSONObject locationObject;
    Main callingActivity;

    public void retrieveJson(String url, Context caller)
    {
        callingActivity = (Main) caller;
        Downloader<JSONObject> downloadInfo = new Downloader<>(this);
        downloadInfo.execute(url);
    }

    @Override
    public JSONObject parseResponse(InputStream in)
    {
        BufferedReader br = new BufferedReader( new InputStreamReader(in));
        String json = "";
        String line = "";
        try
        {
            while ((line = br.readLine()) != null)
                json += line;

            return new JSONObject(json);
        }
        catch(Exception e) { return null; }
    }

    @Override
    public void handleResult(JSONObject result)
    {
        locationObject = result;
        callingActivity.processJSON();
    }
}
