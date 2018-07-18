package com.tanishqmittal.whatstheweather;

import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView textView;

    public void setBg(String s) {

        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.constraint);

        if(s.contains("rain") || s.contains("Rain")) {
            constraintLayout.setBackgroundResource(R.drawable.rains);
        }

        else if(s.contains("clear") || s.contains("Clear")) {
            constraintLayout.setBackgroundResource(R.drawable.clear);
        }

        else if(s.contains("cloud") || s.contains("Cloud") || s.contains("clouds") || s.contains("Clouds")) {
            constraintLayout.setBackgroundResource(R.drawable.cloud);

        } else {

            constraintLayout.setBackgroundResource(R.drawable.defaulty);

        }



    }

    public class DownloadTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {

            try {

                String result = "";
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = connection.getInputStream();

                InputStreamReader reader = new InputStreamReader(inputStream);

                int data = reader.read();

                while(data != -1) {

                    char current = (char) data;

                    result += current;

                    data = reader.read();


                }

                return result;



            } catch (MalformedURLException e) {

                e.printStackTrace();
                return null;



            } catch (IOException e) {
                e.printStackTrace();
                return  null;
            }




        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {

                if(result == null) {

                    textView.setText("Please enter valid city name");
                    return;
                }

                Log.i("content", result);

                JSONObject jsonObject = new JSONObject(result);
                String weatherInfo = jsonObject.getString("weather");
                String mainInfo = jsonObject.getString("main");

                Log.i("WeatherContent", weatherInfo);
                Log.i("MainContent", mainInfo);

                JSONArray arr = new JSONArray(weatherInfo);
                JSONObject jsonObject1 = new JSONObject(mainInfo);

                String temp = jsonObject1.getString("temp");
                String min_temp = jsonObject1.getString("temp_min");
                String max_temp = jsonObject1.getString("temp_max");
                String humidity = jsonObject1.getString("humidity");

                // reduce one decimal place
                double temp_int = Double.parseDouble(temp);
                temp_int = temp_int - 273.15;

                double temp_max = Double.parseDouble(max_temp);
                temp_max = temp_max - 273.15;

                double temp_min = Double.parseDouble(min_temp);
                temp_min = temp_min - 273.15;

                DecimalFormat numberFormat = new DecimalFormat("#.00");
                temp = numberFormat.format(temp_int);
                min_temp = numberFormat.format(temp_min);
                max_temp = numberFormat.format(temp_max);



                //JSONArray brr = new JSONArray(mainInfo);

                String description = "";

                for(int i =0; i<arr.length(); i++) {

                    JSONObject jsonPart = arr.getJSONObject(i);

                    description = jsonPart.getString("description");


                }

                Log.i("temp", temp);

                String answer = "Temperature : " + temp + " C"
                        + System.getProperty("line.separator")  + "Min Temperature : " + min_temp + " C" +
                        System.getProperty("line.separator") + "Max Temperature : " + max_temp + " C" +
                        System.getProperty("line.separator") + "Humidity : " + humidity + " %" +
                        System.getProperty("line.separator") + "Description : " + description ;

               /*for(int i =0; i<brr.length(); i++) {

                    JSONObject jsonPart = brr.getJSONObject(i);

                    Log.i("temp", jsonPart.getString("temp"));


                }*/


               textView.setText(answer);
                setBg(description);


            } catch (JSONException e) {
                e.printStackTrace();
            }




        }
    }

    public void buttonPressed(View view) {

        String s = editText.getText().toString();

        DownloadTask task = new DownloadTask();

        task.execute("http://api.openweathermap.org/data/2.5/weather?q="+s+"&appid=32bbbd3ccdc9dfc15765c9805e29b553");




    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
        textView = (TextView) findViewById(R.id.textView);



        //DownloadTask task = new DownloadTask();

        //task.execute("http://api.openweathermap.org/data/2.5/weather?q=Delhi,in&appid=32bbbd3ccdc9dfc15765c9805e29b553");


    }


}
