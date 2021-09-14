package com.metropolitan.it355;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.metropolitan.it355.entity.Car;
import com.metropolitan.it355.util.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CarListAdapter extends ArrayAdapter<Car> {

    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    private Context context;


    public CarListAdapter(@NonNull Context context, @NonNull List<Car> cars) {
        super(context, R.layout.car_item, cars);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        Car car = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.car_item, parent, false);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        NetworkImageView imageView = (NetworkImageView) convertView.findViewById(R.id.carImageView);

        TextView carMakerText = convertView.findViewById(R.id.carMakerText);
        TextView carModelText = convertView.findViewById(R.id.carModelText);
        TextView carSeatsText = convertView.findViewById(R.id.carSeatsText);
        TextView carPriceText = convertView.findViewById(R.id.carPriceText);
        TextView carTransText = convertView.findViewById(R.id.carTransText);

        imageView.setImageUrl(car.getImage(), imageLoader);

        Button reserveButton = convertView.findViewById(R.id.reserveButton);

        carMakerText.setText(car.getMaker());
        carModelText.setText(car.getModel());
        carSeatsText.setText(Integer.toString(car.getSeats()));
        carPriceText.setText(car.getPrice().toString());
        carTransText.setText(car.getTransmission());

        reserveButton.setOnClickListener(view -> {
            Toast.makeText(context, car.getId().toString(), Toast.LENGTH_SHORT).show();

            Intent intent = ((Activity) context).getIntent();

            String startDate = intent.getStringExtra("startDate");
            String endDate = intent.getStringExtra("endDate");

            SharedPreferences sp = ((Activity) context).getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
            Long userId = sp.getLong("userId", -1);
            final String token = sp.getString("Bearer", "");
            JSONObject body = null;
            try {
                body = new JSONObject();
                body.put("startingDate", startDate);
                body.put("endingDate", endDate);

                JSONObject jsonCar = new JSONObject();
                jsonCar.put("id", car.getId());
                body.put("car", jsonCar);

                JSONObject user = new JSONObject();
                user.put("id", userId);
                body.put("user", user);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String url = AppConstants.BASE_URL + "/api/reservations";

            JsonObjectRequest reservationReq = new JsonObjectRequest(Request.Method.POST, url, body,
                    response -> {
                        Log.d("SUCEES", response.toString());
                        Intent i = new Intent(getContext(), MainActivity.class);
                        getContext().startActivity(i);
                    }, error -> {
                Log.d("ERROR", error.toString());
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Authorization", token);
                    return params;
                }
            };
            AppController.getInstance().addToRequestQueue(reservationReq);
        });

        return convertView;
    }
}
