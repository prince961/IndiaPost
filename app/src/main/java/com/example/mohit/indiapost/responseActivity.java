package com.example.mohit.indiapost;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class responseActivity extends AppCompatActivity {

    ArrayList<String> trackingUpdateList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);
        new getAllResponse().execute();

    }

    public class getAllResponse extends AsyncTask<Void, Void, Void> {
        String ApiKey = "f4ff1c06-6434-4dc6-b8d6-f37c4c557b3c";

        @Override
        protected Void doInBackground(Void... voids) {
            try{
                URL url = new URL("https://api.aftership.com/v4/trackings");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("GET");
                //conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("aftership-api-key", ApiKey);
                conn.connect();


                //OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                //writer.write(body);
                //writer.flush();
                int responseCode = conn.getResponseCode();
                Log.i("responseCode", String.valueOf(responseCode));
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    //Read till there is something available
                    sb.append(line + "\n");     //Reading and saving line by line - not all at once
                }
                line = sb.toString();

                Log.i("TrackingResponse",line);
                ArrayList<String> sa = new ArrayList<>();
                sa = parseResponseJson(line);
                String arrayAsString = "";
                for (String s : sa)
                {
                    arrayAsString += s + "\t";
                }
                Log.i("ArrayAsString",arrayAsString);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private ArrayList<String> parseResponseJson(String line) {

        try{
            JSONObject firstJsonObj = new JSONObject(line);
            JSONObject data = firstJsonObj.getJSONObject("data");
            JSONArray trackingsArray = data.getJSONArray("trackings");
            int i = trackingsArray.length();
            Log.i("size", String.valueOf(i));

            for(int l=0; l<i; l++){
                JSONObject specificAwbDetails = trackingsArray.getJSONObject(l);
                Log.i("AWB", String.valueOf(specificAwbDetails));
                JSONArray checkPointsArray = specificAwbDetails.getJSONArray("checkpoints");
                int totalCheckpoints = checkPointsArray.length();
                JSONObject lastCheckpoint = checkPointsArray.getJSONObject(totalCheckpoints-1);
                Log.i("lastObject", String.valueOf(lastCheckpoint));
                String date = lastCheckpoint.getString("checkpoint_time");
                String tag = lastCheckpoint.getString("tag");
                String AwbNumber = specificAwbDetails.getString("tracking_number");

                Tracking tracking = new Tracking(AwbNumber,date,tag);
                trackingUpdateList.add(AwbNumber+"|"+tag+"|"+date);



            }

        }catch (Exception e) {
            e.printStackTrace();}

        return trackingUpdateList;
    }
}
