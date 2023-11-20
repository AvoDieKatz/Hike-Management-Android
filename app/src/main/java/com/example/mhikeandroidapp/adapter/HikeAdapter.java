package com.example.mhikeandroidapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mhikeandroidapp.MainActivity;
import com.example.mhikeandroidapp.R;
import com.example.mhikeandroidapp.db.entity.hike.Hike;
import com.example.mhikeandroidapp.fragment.HikeDetailFragment;

import java.util.List;

public class HikeAdapter extends RecyclerView.Adapter<HikeAdapter.HikeViewHolder> {

    private Context context;
    private List<Hike> hikeList;
    private MainActivity activity;

    public static class HikeViewHolder extends RecyclerView.ViewHolder {
        private final TextView hikeTitle;
        private final TextView hikeDate;
        private final TextView hikeLocation;
        private final TextView hikeLength;

        public HikeViewHolder(@NonNull View itemView) {
            super(itemView);
            this.hikeTitle = itemView.findViewById(R.id.hike_title);
            this.hikeDate = itemView.findViewById(R.id.hikeDate);
            this.hikeLocation = itemView.findViewById(R.id.hikeLocation);
            this.hikeLength = itemView.findViewById(R.id.hikeLength);
        }

        public TextView getHikeTitle() {
            return hikeTitle;
        }

        public TextView getHikeDate() {
            return hikeDate;
        }

        public TextView getHikeLocation() {
            return hikeLocation;
        }

        public TextView getHikeLength() {
            return hikeLength;
        }
    }

    public HikeAdapter(Context context, List<Hike> hikeList, Activity activity) {
        this.context = context;
        this.hikeList = hikeList;
        this.activity = (MainActivity) activity;
    }

    // Create new views  (invoked by the layout manager)
    @NonNull
    @Override
    public HikeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Create a new view, which defines the UI of the list item
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hike_list_item,parent, false);
        return new HikeViewHolder(itemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(@NonNull HikeViewHolder holder, int position) {
        Hike hike = hikeList.get(position);
        holder.itemView.setId(hike.getId());

        holder.getHikeTitle().setText(activity.getString(R.string.hike_title, hike.getTitle()));
        holder.getHikeDate().setText(activity.getString(R.string.hike_date, hike.getDate()));
        holder.getHikeLocation().setText(activity.getString(R.string.hike_location, hike.getLocation()));
        holder.getHikeLength().setText(activity.getString(R.string.hike_length, hike.getLength()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = activity.getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();

                HikeDetailFragment detailFragment = new HikeDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelable("hikeDetail", hike);
                detailFragment.setArguments(bundle);

                ft.replace(R.id.fragment_content_view, detailFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return hikeList.size();
    }
}
