package com.rodasapc.riotapitest;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;



public class MainActivity extends Activity {


    static String apiKey = "?api_key=007b0f18-e891-4b88-846a-09a64420a4df";
    static String riotPlayerName = "n3wool";

    //Link da Info
    //static String riotPlayerInfo = "https://euw.api.pvp.net/api/lol/euw/v1.4/summoner/by-name/" + riotPlayerName +"?api_key=" + apiKey;

    //Statics para Guardar a Info
    static String playerId = "default";
    static String playerName = "default";
    static String playerIcon = "default";
    static String playerLevel = "default";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Call da cena
        new MyAsyncTask().execute();

        final EditText nameEdit = (EditText) findViewById(R.id.nameEdit);
        Button button1 = (Button) findViewById(R.id.button1);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                riotPlayerName = nameEdit.getText().toString().replace(" ","");
                Log.v("player LOG:", riotPlayerName);
                new MyAsyncTask().execute();
            }
        });



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


    private class MyAsyncTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... arg0) {

            DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());

            HttpGet httpget = new HttpGet("https://euw.api.pvp.net/api/lol/euw/v1.4/summoner/by-name/" + riotPlayerName + apiKey);
            httpget.setHeader("Content-type", "application/json");

            InputStream inputStream = null;
            String result = null;

            try{

                HttpResponse response = httpclient.execute(httpget);
                HttpEntity entity = response.getEntity();
                inputStream = entity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);

                StringBuilder theStringBuilder = new StringBuilder();

                String line;

                while ((line = reader.readLine()) != null){
                    theStringBuilder.append(line + "\n");
                }

                result = theStringBuilder.toString();


            } catch (Exception e) {

                e.printStackTrace();

            } finally {

                try{
                    if(inputStream != null) inputStream.close();

                } catch (Exception e){
                    e.printStackTrace();
                }

            }


            JSONObject jsonObject;

            try{

                Log.v("object LOG:", result);

                jsonObject = new JSONObject(result);

                JSONObject idJSONObject = jsonObject.getJSONObject(riotPlayerName.toLowerCase());


                playerId = idJSONObject.getString("id");
                playerName = idJSONObject.getString("name");
                playerIcon = idJSONObject.getString("profileIconId");
                playerLevel = idJSONObject.getString("summonerLevel");


            }catch (JSONException e){
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {

            TextView line1 = (TextView) findViewById(R.id.line1);
            TextView line2 = (TextView) findViewById(R.id.line2);
            TextView line3 = (TextView) findViewById(R.id.line3);
            TextView line4 = (TextView) findViewById(R.id.line4);

            line1.setText("ID: " + playerId);
            line2.setText("Name: " + playerName);
            line3.setText("Icon: " + playerIcon);
            line4.setText("Level: " + playerLevel);

        }
    }


}

