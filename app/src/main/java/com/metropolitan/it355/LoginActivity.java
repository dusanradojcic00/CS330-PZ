package com.metropolitan.it355;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.metropolitan.it355.databinding.ActivityLoginBinding;
import com.metropolitan.it355.util.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        if (!sp.getString("Bearer", "").equals("")) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        //Initial data
        binding.inputEmail.setText(R.string.email);
        binding.inputPassword.setText(R.string.password);

        binding.loginButton.setOnClickListener(v -> {
            v.setEnabled(false);


            String url = AppConstants.BASE_URL + "/api/auth/login";

            JSONObject jsonBody = new JSONObject();

            try {
                jsonBody.put("username", binding.inputEmail.getText().toString());
                jsonBody.put("password", binding.inputPassword.getText().toString());

                final String requestBody = jsonBody.toString();

                JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                        response -> {
                            Log.d("JSON", response.toString());
                            SharedPreferences.Editor editor = sp.edit();
                            try {
                                editor.putString("Bearer", "Bearer " + response.getString("bearer"));

                                JSONObject user = response.getJSONObject("user");
                                editor.putLong("userId", user.getLong("id"));

                                editor.apply();
                                Intent intent = new Intent(this, MainActivity.class);
                                startActivity(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            v.setEnabled(true);
                        },
                        error -> {
                            Toast.makeText(this, "Unable to login", Toast.LENGTH_SHORT).show();
                            Log.d("ERROR JSON", "Something went wrong \n" + error.toString());
                            v.setEnabled(true);
                        });
                AppController.getInstance().addToRequestQueue(loginRequest);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        });
    }
}