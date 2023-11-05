package com.example.mhikeandroidapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mhikeandroidapp.MainActivity;
import com.example.mhikeandroidapp.R;
import com.example.mhikeandroidapp.db.entity.observation.Observation;
import com.example.mhikeandroidapp.fragment.HikeDetailFragment;

import java.util.List;

public class ObservationAdapter extends RecyclerView.Adapter<ObservationAdapter.ObservationViewHolder> {
    private Context context;
    private List<Observation> observationList;
    private MainActivity activity;
    private HikeDetailFragment fragment;
    public static class ObservationViewHolder extends RecyclerView.ViewHolder {
        private final TextView observation;
        private final TextView time;
        private final Button addBtn;


        public ObservationViewHolder(@NonNull View itemView) {
            super(itemView);
            this.observation = itemView.findViewById(R.id.observation);
            this.time = itemView.findViewById(R.id.observation_time);
            this.addBtn = itemView.findViewById(R.id.observation_add_btn);
        }

        public TextView getObservation() {
            return observation;
        }

        public TextView getTime() {
            return time;
        }
    }

    public ObservationAdapter(Context context, List<Observation> observationList, Activity activity, HikeDetailFragment fragment) {
        this.context = context;
        this.observationList = observationList;
        this.activity = (MainActivity) activity;
        this.fragment = fragment;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == observationList.size())? R.layout.btn_ic_add : R.layout.observation_card;
    }

    @NonNull
    @Override
    public ObservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        if (viewType == R.layout.observation_card) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.observation_card, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.btn_ic_add, parent, false);
        }

        return new ObservationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ObservationViewHolder holder, int position) {
        if (position == observationList.size()) {
            holder.addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.openAddObservationDialog();
                }
            });

        } else {
            Observation observation = observationList.get(position);
            holder.itemView.setId(observation.getId());

            holder.getTime().setText(activity.getString(R.string.observation_time, observation.getTime()));
            holder.getObservation().setText(activity.getString(R.string.text, observation.getObservation()));

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(context, "Button Long Clicked", Toast.LENGTH_LONG).show();
                    return true;
                }
            });

        holder.getTime().setText(activity.getString(R.string.observation_time, observation.getTime()));
        holder.getObservation().setText(activity.getString(R.string.text, observation.getObservation()));
        }

    }

    @Override
    public int getItemCount() {
        return observationList.size() + 1;
    }
}
