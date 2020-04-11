package com.example.elavi.predictweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    Button button;
    EditText editText;
    TextView resulttextview;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=findViewById(R.id.button);
        editText=findViewById(R.id.edittext);
        resulttextview=findViewById(R.id.resulttextview);

    }

    public void getweather(View view)
    {
       /* DownloadTask task = new DownloadTask();
        task.execute("https://openweathermap.org/data/2.5/weather?q="+ editText.getText().toString()+"&appid=439d4b804bc8187953eb36d2a8c26a02");
        InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(),0);*/
        try {
            DownloadTask task = new DownloadTask();
            String encodedCityName = URLEncoder.encode(editText.getText().toString(), "UTF-8");
            //useful to get encode the city name
            task.execute("http://openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&appid=439d4b804bc8187953eb36d2a8c26a02");

            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Could not find weather :(",Toast.LENGTH_SHORT).show();
        }
    }

    public class DownloadTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }

                return result;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        //we can touch the something from the user interface
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
              JSONObject jsonObject = new JSONObject(s) ;

                String weatherInfo = jsonObject.getString("weather");

                Log.i("Weather content", weatherInfo);

                JSONArray arr = new JSONArray(weatherInfo);
                String msg="";
                for (int i=0; i < arr.length(); i++) {
                    JSONObject jsonPart = arr.getJSONObject(i);
                    String main=jsonPart.getString("main");
                    String description=jsonPart.getString("description");
                    if(!main.equals("") && !description.equals(""))
                        msg+=main+" : "+description +"\n";
                    Log.i("main",jsonPart.getString("main"));
                    Log.i("description",jsonPart.getString("description"));
                }
              //here the game begins
               /* String  maininfo=jsonObject.getString("main");
                JSONArray maininfoarr = new JSONArray(maininfo);
                for(int i=0;i<maininfoarr.length();i++)
                {
                    JSONObject jsonPart = maininfoarr.getJSONObject(i);
                    String temp=Integer.toString(jsonPart.getInt("temp"));
                    String pressure=Integer.toString(jsonPart.getInt("pressure"));
                    String humidity=Integer.toString(jsonPart.getInt("humidity"));
                    if(!temp.equals("")&& !pressure.equals("") && !humidity.equals(""))
                    {
                        msg+=temp+"\n";
                        msg+=pressure+"\n";
                        msg+=humidity+"\n";
                    }
                }*/

                /*game over kuch met chedo*/
                if(!msg.equals(""))
                    resulttextview.setText(msg);
               Log.i("JSON Data",s);

            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Could Not find weather",Toast.LENGTH_SHORT).show();
                e.printStackTrace();

            }

        }
    }

}
