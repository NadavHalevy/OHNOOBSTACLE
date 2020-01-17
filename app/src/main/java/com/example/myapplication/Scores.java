package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Locale;

public class Scores extends Fragment {

    private Context cntx;
    private View view = null;

    public Scores(Context cntx){
        this.cntx = cntx;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(view == null) {
            view = inflater.inflate(R.layout.activity_scores, container, false);
        }
        LinearLayout scoreLayout = view.findViewById(R.id.highScoreLayout);
        SharedPreferences preferences = cntx.getSharedPreferences("Prefs",0);
        ArrayList<Score> scores = new Gson().fromJson(preferences.getString("scores",""),
                new TypeToken<ArrayList<Score>>(){}.getType());
        for(int i = 0 ; i < scores.size() ; i++){
            TextView s = new TextView(cntx);
            s.setText(String.format(Locale.ENGLISH,"%d. %10s %d",i+1,scores.get(i).getName(),scores.get(i).getScore()));
            s.setTextSize(20);
            s.setPadding(10,2,10,2);
            scoreLayout.addView(s);
        }

        Button back = view.findViewById(R.id.backB);
        back.setOnClickListener(this::backToMenu);

        return view;
    }

    public void backToMenu(View view){
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        view.getContext().startActivity(intent);
    }
}

