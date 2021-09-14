package com.metropolitan.it355;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.metropolitan.it355.entity.Reservation;

import org.w3c.dom.Text;

import java.util.List;

public class ReservationListAdapter extends ArrayAdapter<Reservation> {

    private Context context;

    public ReservationListAdapter(@NonNull Context context, @NonNull List<Reservation> reservations) {
        super(context, R.layout.reservation_item, reservations);
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Reservation reservation = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reservation_item, parent, false);
        }

        TextView startDateView = convertView.findViewById(R.id.startingLabel);
        TextView endDateView = convertView.findViewById(R.id.endingLabel);
        TextView carView = convertView.findViewById(R.id.carLabel);
        TextView priceView = convertView.findViewById(R.id.priceLabel);

        startDateView.setText(reservation.getStartingDate().toString());
        endDateView.setText(reservation.getEndingDate().toString());
        carView.setText(reservation.getCar().getMaker() + " " + reservation.getCar().getModel());
        long duration = reservation.getEndingDate().toEpochDay() - reservation.getStartingDate().toEpochDay();
        double total = duration * reservation.getCar().getPrice();
        priceView.setText("$" + total);

        return convertView;
    }
}
