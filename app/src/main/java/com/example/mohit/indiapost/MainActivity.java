package com.example.mohit.indiapost;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    List<String> podList;
    int LoopNumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String CommaSepratedPOD = "rm667625849in,rm667625835in,rm667625821in,rm667625818in,rm667625804in,rm667625795in,rm667625781in,rm667625778in,rm667625764in,rm667625755in,rm667625747in,rm667625733in,rm667625720in,rm667625716in,rm667625702in,rm667625693in,rm667625680in,rm667625676in,rm667625662in,rm667625659in,rm667625645in,rm667625631in,rm667625628in,rm667625614in,rm667625605in,rm667625591in,rm667625588in,rm667625574in,rm667626288in,rm667626274in,rm667626265in,rm667626257in,rm667626243in,rm667626230in,rm667626226in,rm667626212in,rm667626172in,rm667626186in,rm667626209in,rm667626190in,rm667626169in,rm667626155in,rm667626141in,rm667626138in,rm667626124in,rm667626115in,rm667626107in,rm667626098in,rm667626084in,rm667626075in";
        podList =  Arrays.asList(CommaSepratedPOD.split("\\s*,\\s*"));
        Log.i("length", String.valueOf(podList.size()));
        CreateTracking(podList);
    }

    private void CreateTracking(List<String> podList) {
        int ListSize = podList.size();

        for(int l=0; l<ListSize; l++){
            LoopNumber = l;
            Log.i("ForLoop", String.valueOf(l));
            //new sendDatatoServer(podList.get(l)).execute();

        }

    }

    public void GetResponse(View view) {
        Intent i = new Intent(this,responseActivity.class);
        startActivity(i);
    }

    public class sendDatatoServer extends AsyncTask<Void, Void, Void> {
        String ApiKey = "f4ff1c06-6434-4dc6-b8d6-f37c4c557b3c";
        String AwbNumber;

        public sendDatatoServer(String s) {
            this.AwbNumber = s;
        }


        @Override
        protected Void doInBackground(Void... voids) {
            try{
                URL url = new URL("https://api.aftership.com/v4/trackings");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("aftership-api-key", ApiKey);
                Log.i("loopNumber", AwbNumber);
                String body = "{\"tracking\":{\"tracking_number\":\""+AwbNumber+"\"}}";
                Log.i("BODY",body);
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(body);
                writer.flush();
                StringBuilder sb = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    //Read till there is something available
                    sb.append(line + "\n");     //Reading and saving line by line - not all at once
                }
                line = sb.toString();

                Log.i("orderMEssage",line);
                int responseCode = conn.getResponseCode();
                Log.i("responseCode", String.valueOf(responseCode));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }


}
