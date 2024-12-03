package com.example.weatherapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView weatherInfo;
    private final String API_KEY = "06fec289081632a9160150721c56059e";
    private final String BASE_URL = "https://api.openweathermap.org/data/2.5/weather?q=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        weatherInfo = findViewById(R.id.weatherInfo);

        Button btnNewYork = findViewById(R.id.btnNewYork);
        Button btnLondon = findViewById(R.id.btnLondon);
        Button btnTokyo = findViewById(R.id.btnTokyo);
        Button btnColombo = findViewById(R.id.btnColombo);
        Button btnDubai = findViewById(R.id.btnDubai);

        btnNewYork.setOnClickListener(v -> fetchWeather("New York"));
        btnLondon.setOnClickListener(v -> fetchWeather("London"));
        btnTokyo.setOnClickListener(v -> fetchWeather("Tokyo"));
        btnColombo.setOnClickListener(v -> fetchWeather("Colombo"));
        btnDubai.setOnClickListener(v -> fetchWeather("Dubai"));
    }

    private void fetchWeather(String city) {
        new Thread(() -> {
            try {
                URL url = new URL(BASE_URL + city + "&appid=" + API_KEY + "&units=metric");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonObject = new JSONObject(response.toString());
                String temperature = jsonObject.getJSONObject("main").getString("temp") + "°C";
                String description = jsonObject.getJSONArray("weather").getJSONObject(0).getString("description");
                String windSpeed = jsonObject.getJSONObject("wind").getString("speed") + " m/s";

                String result = "City: " + city + "\n" +
                        "Temperature: " + temperature + "\n" +
                        "Description: " + description + "\n" +
                        "Wind Speed: " + windSpeed;

                runOnUiThread(() -> weatherInfo.setText(result));

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> weatherInfo.setText("Error: " + e.getMessage()));
            }

        }).start();
    }
}
