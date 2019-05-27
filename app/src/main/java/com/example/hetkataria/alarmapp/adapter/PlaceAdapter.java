package com.example.hetkataria.alarmapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hetkataria.alarmapp.PlaceNameInterface;
import com.example.hetkataria.alarmapp.R;
import com.example.hetkataria.alarmapp.model.Place;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.MyViewHolder> {

    private List<Place> placeList;
    private PlaceNameInterface placeNameInterface;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView placeName;


        public MyViewHolder(View view) {
            super(view);
            placeName = (TextView) view.findViewById(R.id.placetext);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    placeNameInterface.onRowClick(placeList.get(getAdapterPosition()).getPlaceName());
                }
            });
        }
    }

    public PlaceAdapter(List<Place> placeList, PlaceNameInterface placeNameInterface) {
        this.placeList = placeList;
        this.placeNameInterface = placeNameInterface;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_place, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Place place = placeList.get(position);
        holder.placeName.setText(place.getPlaceName());
    }

    @Override
    public int getItemCount() {
        return placeList.size();
    }
}
