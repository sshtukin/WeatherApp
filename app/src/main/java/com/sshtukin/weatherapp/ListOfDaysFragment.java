package com.sshtukin.weatherapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ListOfDaysFragment extends Fragment {
    RecyclerView mRecyclerView;
    DayAdapter mDayAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_of_days,
                container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        initRecyclerView();
        return view;
    }


    public void initRecyclerView() {
        if (mDayAdapter == null) {
            mDayAdapter = new DayAdapter();
            mRecyclerView.setAdapter(mDayAdapter);
        } else {
            mDayAdapter.notifyDataSetChanged();
        }
    }

    private class DayHolder extends RecyclerView.ViewHolder{
        TextView mDayOfWeek;
        TextView mTempText;
        TextView mDescription;
        ImageView mImageView;


        public DayHolder(LayoutInflater layoutInflater, ViewGroup parent) {
            super(layoutInflater.inflate(R.layout.day_item, parent, false));
            mDayOfWeek = itemView.findViewById(R.id.day_of_week);
            mTempText = itemView.findViewById(R.id.temp);
            mImageView = itemView.findViewById(R.id.weather_icon);
            mDescription = itemView.findViewById(R.id.description);
        }

        public void bind(){
        }
    }

    private class DayAdapter extends RecyclerView.Adapter<DayHolder>{

        @Override
        public DayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new DayHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(DayHolder holder, int position) {
            holder.bind();
        }

        @Override
        public int getItemCount() {
            return 5;
        }

        public int getItemViewType(int position)
        {
            return position;
        }
    }

}
