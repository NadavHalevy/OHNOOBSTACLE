package com.example.myapplication;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;


public class HighScore extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap gMap;

//    private Fragment ranks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        getSupportFragmentManager().beginTransaction().replace(R.id.highScoreFragment, new Scores(this)).commit();


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        SharedPreferences sp = getSharedPreferences("Prefs", 0);
        gMap = googleMap;
        Gson gson = new Gson();
        ArrayList<Score> scores = gson.fromJson(sp.getString("scores",""),
                new TypeToken<ArrayList<Score>>(){}.getType());
        LatLng pos = null;
        for (int i = 0; i < scores.size(); i++) {
            Score score = scores.get(i);
            pos = new LatLng(score.getLat(),score.getLon());
            gMap.addMarker(new MarkerOptions().position(pos).title((i + 1) + " Place: " + score.getName()).icon(BitmapDescriptorFactory.defaultMarker()));
        }
        if(pos != null)
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 18.0f));

    }

//            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(locations[position], 18.0f));


    double getDouble(final SharedPreferences prefs, final String key, final double defaultValue)
    { return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));


  }
}








/*
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HighScore extends Fragment {
    private View view = null;
    private int lastScore;
    private String lastName;
    private double latitude,longitude;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null)
            view = inflater.inflate(R.layout.activity_high_score, container, false);
        SharedPreferences preferences = getActivity().getSharedPreferences("Prefs", 0);
        getValues(preferences);
        
        return view;
    }


    private void getValues(SharedPreferences preferences) {
        lastScore = preferences.getInt("lastScore", 0);
        lastName = preferences.getString("nameLastScore", "");
        latitude = getDouble(preferences, "lastLat", 0.0f);
        longitude = getDouble(preferences, "lastLon", 0.0f);
    }


    double getDouble(final SharedPreferences prefs, final String key, final double defaultValue)
    { return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));}

}*/
