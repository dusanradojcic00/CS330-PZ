package com.metropolitan.it355;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.metropolitan.it355.databinding.ActivityEditProfileBinding;
import com.metropolitan.it355.entity.Address;
import com.metropolitan.it355.entity.User;
import com.metropolitan.it355.util.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private User user = null;

    private static final String TAG = EditProfileActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        SharedPreferences sp = getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        final String token = sp.getString("Bearer", "");

        String getUrl = AppConstants.BASE_URL + "/api/users/me";

        JsonObjectRequest getReq = new JsonObjectRequest(Request.Method.GET, getUrl, null, response -> {
            Gson gson = new Gson();
            user = gson.fromJson(response.toString(), User.class);
            Log.d(TAG, response.toString());
            binding.nameInputText.setText(user.getFirstName());
            binding.surnameInputText.setText(user.getLastName());
            binding.phoneInputText.setText(user.getPhone());
            Address address = user.getAddress();
            if (address != null) {
                binding.cityInputText.setText(address.getCity());
                binding.adressInputText.setText(address.getStreet());
                binding.zipInputText.setText(address.getZipcode());
                binding.apartmentInputText.setText(address.getApartment());
            }
        }, error -> {

        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", token);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(getReq);

        binding.saveButton.setOnClickListener(v -> {
            String putUrl = AppConstants.BASE_URL + "/api/users";

            user.setFirstName(binding.nameInputText.getText().toString());
            user.setLastName(binding.surnameInputText.getText().toString());
            user.setPhone(binding.phoneInputText.getText().toString());

            Address address = user.getAddress();
            if (address == null) {
                user.setAddress(new Address());
            }
            address = user.getAddress();
            address.setApartment(binding.apartmentInputText.getText().toString());
            address.setCity(binding.cityInputText.getText().toString());
            address.setZipcode(binding.zipInputText.getText().toString());
            address.setStreet(binding.adressInputText.getText().toString());

            Gson gson = new Gson();

            String userString = gson.toJson(user);
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(userString);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            JsonObjectRequest updateReq = new JsonObjectRequest(Request.Method.PUT, putUrl, jsonObject,
                    response -> {
                        Log.d(TAG, response.toString());
                        finish();
                    },
                    error -> {
                        Log.d(TAG, "There was an error");
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", token);
                    return params;
                }
            };

            AppController.getInstance().addToRequestQueue(updateReq);
        });
    }
}