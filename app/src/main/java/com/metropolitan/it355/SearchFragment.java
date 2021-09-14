package com.metropolitan.it355;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.metropolitan.it355.databinding.FragmentSearchBinding;
import com.metropolitan.it355.util.AppConstants;

import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG = SearchFragment.class.getName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private LocalDate start;
    private LocalDate end;

    private FragmentSearchBinding binding;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        MaterialDatePicker<Pair<Long, Long>> datePicker = MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Odaberite datume")
                .setCalendarConstraints(getConstraints())
                .setSelection(
                        new Pair<>
                                (MaterialDatePicker.todayInUtcMilliseconds(),
                                        MaterialDatePicker.todayInUtcMilliseconds() + 86400000L)
                )
                .build();

        binding.dateButton.setOnClickListener(v -> {
            datePicker.show(getActivity().getSupportFragmentManager(), "DatePicker");
        });

        datePicker.addOnPositiveButtonClickListener(selection -> {
            Pair<Long, Long> selectedDates = (Pair<Long, Long>) datePicker.getSelection();
            if (selectedDates != null) {
                LocalDate startDate = Instant.ofEpochMilli(selectedDates.first)
                        .atZone(ZoneId.systemDefault()).toLocalDate();
                LocalDate endDate = Instant.ofEpochMilli(selectedDates.second)
                        .atZone(ZoneId.systemDefault()).toLocalDate();
                String text = "From: " + startDate.toString() + " to: " + endDate.toString();

                binding.dateButton.setText(text);
                Log.d(TAG, "Start " + startDate.toString());
                Log.d(TAG, "End " + endDate.toString());

                this.start = startDate;
                this.end = endDate;
            }
        });

        binding.searchButton.setOnClickListener(v -> {
            if (start != null && end != null) {
                Intent i = new Intent(getContext(), CarListActivity.class);
                i.putExtra("startDate", start.toString());
                i.putExtra("endDate", end.toString());
                startActivity(i);
            } else {
                Toast.makeText(getContext(), "You haven't selected dates", Toast.LENGTH_SHORT).show();
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private CalendarConstraints getConstraints() {
        CalendarConstraints.Builder constraintsBuilderRange = new CalendarConstraints.Builder();
        constraintsBuilderRange.setValidator(DateValidatorPointForward.now());
        return constraintsBuilderRange.build();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}