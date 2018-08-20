package com.sshtukin.weatherapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;
import com.sshtukin.weatherapp.model.ClearedWeather;
import com.sshtukin.weatherapp.model.Weather;
import com.sshtukin.weatherapp.model.WeatherList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ListOfDaysFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private DayAdapter mDayAdapter;
    private GoogleApiClient mClient;
    private TextView mCityName;
    private static final int REQUEST_LOCATION_PERMISSIONS = 0;
    private static final String[] LOCATION_PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };
    private String TAG = "ListOfDaysFragment";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle bundle) {
                        getActivity().invalidateOptionsMenu();
                    }
                    @Override
                    public void onConnectionSuspended(int i) {
                    }
                })
                .build();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.location, menu);
        MenuItem searchItem = menu.findItem(R.id.action_locate);
        searchItem.setEnabled(mClient.isConnected());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_locate:
                if (hasLocationPermission()){
                    getLocation();
                }
                else {
                    requestPermissions(LOCATION_PERMISSIONS,
                            REQUEST_LOCATION_PERMISSIONS);
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSIONS:
                if (hasLocationPermission()) {
                    getLocation();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }


    private void getLocation() {
        LocationRequest request = LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setNumUpdates(1);
        request.setInterval(0);

        LocationServices.FusedLocationApi
                .requestLocationUpdates(mClient, request, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
//                        Log.i(TAG, "Got a fix: " + location);
//                        downloadWeather(String.valueOf(location.getAltitude()), String.valueOf(location.getLatitude()));
                        final RetrofitClient retrofitClient = new RetrofitClient();

                        final List<ClearedWeather> clearedWeatherList = new ArrayList<>();
                        Call<Weather> call = retrofitClient.getCall(String.valueOf(location.getAltitude()),
                                String.valueOf(location.getLatitude()));
                        call.enqueue(new Callback<Weather>() {
                            @Override
                            public void onResponse(Call<Weather> call, Response<Weather> response) {
                                Weather weather = response.body();
                                List<WeatherList> result_list = weather.getList();
                                mCityName.setText(weather.getCity().getName());
                                clearedWeatherList.addAll(retrofitClient.clearData(result_list));

                                for (ClearedWeather clearedWeather : clearedWeatherList) {
                                    Log.i(TAG, "-------------------");
                                    Log.i(TAG, clearedWeather.getDay());
                                    Log.i(TAG, String.valueOf(clearedWeather.getMaxTemp()));
                                    Log.i(TAG, String.valueOf(clearedWeather.getMinTemp()));
                                }
                                initRecyclerView(clearedWeatherList);
                            }

                            @Override
                            public void onFailure(Call<Weather> call, Throwable t) {
                                Log.e(TAG, "Called onFailure", t);

                            }
                        });
                    }
                });
    }

    private boolean hasLocationPermission() {
        int result = ContextCompat
                .checkSelfPermission(getActivity(), LOCATION_PERMISSIONS[0]);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_of_days,
                container, false);

        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
        mCityName = view.findViewById(R.id.city_name);



        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().invalidateOptionsMenu();
        mClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mClient.disconnect();
    }


    public void initRecyclerView(List<ClearedWeather> clearedWeatherList) {
        Log.w("LOFF", "HELLOOOOOOOOOOOOOOOOO");
        if (mDayAdapter == null) {
            mDayAdapter = new DayAdapter();
            mDayAdapter.setItems(clearedWeatherList);
            mRecyclerView.setAdapter(mDayAdapter);
        } else {
            mDayAdapter.setItems(clearedWeatherList);
            mDayAdapter.notifyDataSetChanged();
        }
    }

    private class DayHolder extends RecyclerView.ViewHolder{
        TextView mDayOfWeek;
        TextView mTempText;
        TextView mCityName;
        TextView mDescription;
        ImageView mImageView;


        public DayHolder(LayoutInflater layoutInflater, ViewGroup parent) {
            super(layoutInflater.inflate(R.layout.day_item, parent, false));
            mDayOfWeek = itemView.findViewById(R.id.day_of_week);
            mTempText = itemView.findViewById(R.id.temp);
            mImageView = itemView.findViewById(R.id.weather_icon);
            mDescription = itemView.findViewById(R.id.description);
        }

        public void bind(ClearedWeather clearedWeather){
            mDayOfWeek.setText(clearedWeather.getDay());
            String tempText = String.format("Max:%d  Min:%d", clearedWeather.getMaxTemp(), clearedWeather.getMinTemp());
            mTempText.setText(tempText);
            mDescription.setText(clearedWeather.getDescription());
            Picasso.get()
                    .load(clearedWeather.getImage())
                    .placeholder(R.drawable.ic_launcher_background)
                    .resize(150, 150)
                    .centerCrop()
                    .into(mImageView);
        }
    }

    private class DayAdapter extends RecyclerView.Adapter<DayHolder>{
        List<ClearedWeather> mClearedWeatherList;

        public void setItems(List<ClearedWeather> clearedWeatherList){
            mClearedWeatherList = clearedWeatherList;
        }

        @Override
        public DayHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            return new DayHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(DayHolder holder, int position) {
            holder.bind(mClearedWeatherList.get(position));
        }

        @Override
        public int getItemCount() {
            return mClearedWeatherList.size();
        }

        public int getItemViewType(int position)
        {
            return position;
        }
    }

}
