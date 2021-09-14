package com.metropolitan.it355;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.metropolitan.it355.entity.Car;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class CarListActivity extends AppCompatActivity {

    CarListAdapter carListAdapter;
    ListView listView;
    ArrayList<Car> cars = new ArrayList<>();
    private final String BASE_URL = "http://10.0.2.2:8080";

    private static final String TAG = CarListActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);


        listView = (ListView) findViewById(R.id.listView);
        carListAdapter = new CarListAdapter(this, cars);
        listView.setAdapter(carListAdapter);

        Intent intent = getIntent();

        String startDate = intent.getStringExtra("startDate");
        String endDate = intent.getStringExtra("endDate");

        String url = BASE_URL + "/api/cars/available?start=" + startDate + "&end=" + endDate;

        SharedPreferences sp = getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        final String token = sp.getString("Bearer", "");

        JsonArrayRequest carReq = new JsonArrayRequest(Request.Method.GET, url, null,
                response -> {
                    Log.d(TAG, response.toString());
                    Gson gson = new Gson();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject obj = response.getJSONObject(i);
                            Car carObj = gson.fromJson(obj.toString(), Car.class);
                            cars.add(carObj);
                            carListAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> {
                    VolleyLog.d(TAG, "Error: " + error.getMessage());
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", token);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(carReq);
    }

}
