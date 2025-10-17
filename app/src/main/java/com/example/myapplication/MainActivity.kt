package com.example.myapplication

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.color.DynamicColors
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import android.util.Log
import android.widget.TextView
class MainActivity : AppCompatActivity() {
    private lateinit var temperatureTextView: TextView
    private lateinit var windTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        DynamicColors.applyToActivityIfAvailable(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets

        }
        temperatureTextView = findViewById(R.id.temperatureTextView)
        windTextView = findViewById(R.id.windTextView)
        val url = "https://api.open-meteo.com/v1/forecast?latitude=52.52&longitude=13.405&hourly=temperature_2m,relative_humidity_2m,precipitation,weather_code,wind_speed_10m&current=temperature_2m,relative_humidity_2m,wind_speed_10m,weather_code&timezone=auto"
        val queue = Volley.newRequestQueue(this)
        val request = StringRequest(Request.Method.GET, url,
            { response ->
                val jsonObject = JSONObject(response)
                val hourly = jsonObject.getJSONObject("hourly")
                val temperatures = hourly.getJSONArray("temperature_2m")
                val wind = hourly.getJSONArray("wind_speed_10m")
                val firstTemp = temperatures.getDouble(0)
                val firstWind = wind.getDouble(0)

                temperatureTextView.text = "$firstTemp"
                windTextView.text = "$firstWind m/s"

                },
                {error ->
                    Log.e("błąd" , "blaad${error.message}")
                }
        )
        queue.add(request)
    }
}

