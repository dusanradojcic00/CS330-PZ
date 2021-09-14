package com.metropolitan.it355;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.metropolitan.it355.databinding.FragmentProfileBinding;
import com.metropolitan.it355.databinding.FragmentReservationsBinding;
import com.metropolitan.it355.entity.Car;
import com.metropolitan.it355.entity.Reservation;
import com.metropolitan.it355.util.AppConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservationsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ReservationListAdapter reservationListAdapter;

    private static final String TAG = ReservationsFragment.class.getSimpleName();

    private ArrayList<Reservation> reservations = new ArrayList<>();

    private FragmentReservationsBinding binding;

    public ReservationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReservationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReservationsFragment newInstance(String param1, String param2) {
        ReservationsFragment fragment = new ReservationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReservationsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        reservationListAdapter = new ReservationListAdapter(getContext(), reservations);
        binding.listView.setAdapter(reservationListAdapter);


        SharedPreferences sp = getActivity().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);

        final String token = sp.getString("Bearer", "");

        final long userId = sp.getLong("userId", 0);

        String url = AppConstants.BASE_URL + "/api/reservations?user=" + userId;

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        Gson gson = gsonBuilder.create();

        JsonArrayRequest reservationsReq = new JsonArrayRequest(Request.Method.GET, url, null, response -> {
            Log.d(TAG, response.toString());
            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject obj = response.getJSONObject(i);
                    Reservation reservationObj = gson.fromJson(obj.toString(), Reservation.class);
                    reservations.add(reservationObj);
                    reservationListAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, error -> {
            VolleyLog.d(TAG, "Error: " + error.getMessage());
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", token);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(reservationsReq);

        super.onViewCreated(view, savedInstanceState);
    }
}