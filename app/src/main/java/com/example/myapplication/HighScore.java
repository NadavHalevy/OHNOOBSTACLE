package com.example.myapplication;

import androidx.fragment.app.FragmentActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
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
}