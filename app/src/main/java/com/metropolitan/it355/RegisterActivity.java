package com.metropolitan.it355;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.metropolitan.it355.databinding.ActivityRegisterBinding;
import com.metropolitan.it355.util.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.registerButton.setOnClickListener(v -> {

            v.setEnabled(false);
            String url = AppConstants.BASE_URL + "/api/auth/register";

            JSONObject jsonBody = new JSONObject();

            try {
                jsonBody.put("username", binding.inputEmail.getText().toString());
                jsonBody.put("password", binding.inputPassword.getText().toString());

                JsonObjectRequest loginRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                        response -> {
                            Log.d("JSON", response.toString());
                            Intent intent = new Intent(this, LoginActivity.class);
                            startActivity(intent);
                            v.setEnabled(true);
                        },
                        error -> {
                            Toast.makeText(this, "Unable to register", Toast.LENGTH_SHORT).show();
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