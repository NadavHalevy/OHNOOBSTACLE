package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
    TextView First,Second,Third;
    String firstName, secondName, thirdName, name, password;
    int firstScore, secondScore, thirdScore, score;
    double latitude, longitude, firstLatitude, firstLongitude, secondLatitude, secondLongitude, thirdLatitude, thirdLongitude;

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
//        getValuesFromGame(preferences);
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
    public void updateScores(int newScore,String newName, SharedPreferences preferences){
        SharedPreferences.Editor editor = preferences.edit();
        String tempName;
        int tempScore;
        double tempLa,tempLo;
        if(firstScore > newScore){
            if (secondScore > newScore){
                thirdScore = newScore;
                thirdName = newName;
                thirdLatitude = latitude;
                thirdLongitude = longitude;

                editor.putLong("tLat",Double.doubleToLongBits(thirdLatitude));
                editor.putLong("tLon",Double.doubleToLongBits(thirdLongitude));
                editor.putString("thirdName",thirdName);
                editor.putInt("thirdScore", thirdScore);

            }
            else{
                thirdScore = secondScore;
                thirdName = secondName;
                thirdLatitude = secondLatitude;
                thirdLongitude = secondLongitude;

                secondScore = newScore;
                secondName = newName;
                secondLongitude = longitude;
                secondLatitude = latitude;


                editor.putLong("tLat",Double.doubleToLongBits(thirdLatitude));
                editor.putLong("tLon",Double.doubleToLongBits(thirdLongitude));
                editor.putLong("sLat",Double.doubleToLongBits(secondLatitude));
                editor.putLong("sLon",Double.doubleToLongBits(secondLongitude));

                editor.putInt("secondScore", secondScore);
                editor.putString("secondName", secondName);
                editor.putInt("thirdScore", thirdScore);
                editor.putString("thirdName",thirdName);

            }
        }
        else{
            tempName = firstName;
            tempScore = firstScore;
            tempLa = firstLatitude;
            tempLo = firstLongitude;

            firstScore = newScore;
            firstName = newName;
            firstLongitude = longitude;
            firstLatitude = latitude;

            thirdScore = secondScore;
            thirdName = secondName;
            thirdLongitude = secondLongitude;
            thirdLatitude = secondLatitude;

            secondName = tempName;
            secondScore = tempScore;
            secondLatitude = tempLa;
            secondLongitude = tempLo;

            editor.putLong("tLat",Double.doubleToLongBits(thirdLatitude));
            editor.putLong("tLon",Double.doubleToLongBits(thirdLongitude));
            editor.putLong("sLat",Double.doubleToLongBits(secondLatitude));
            editor.putLong("sLon",Double.doubleToLongBits(secondLongitude));
            editor.putLong("fLat",Double.doubleToLongBits(firstLatitude));
            editor.putLong("fLon",Double.doubleToLongBits(firstLongitude));

            editor.putString("firstName",firstName);
            editor.putInt("firstScore", firstScore);
            editor.putString("secondName",secondName);
            editor.putInt("secondScore", secondScore);
            editor.putString("thirdName",thirdName);
            editor.putInt("thirdScore", thirdScore);

        }

        First.setText("1. " + firstName + " - " + firstScore);
        Second.setText("2. " + secondName + " - " + secondScore);
        Third.setText("3. " + thirdName + " - " + thirdScore);
        editor.putInt("lastScore", 0);
        editor.putString("lastScoreName","");
        editor.putLong("lastLat", Double.doubleToLongBits(0.0f));
        editor.putLong("lastLon", Double.doubleToLongBits(0.0f));
        editor.apply();

    }



    private void getValuesFromGame(SharedPreferences preferences){
        score  = preferences.getInt("lastScore", 0);
        name  = preferences.getString("nameLastScore", "");
        latitude = getDouble(preferences,"lastLat", 0.0f);
        longitude = getDouble(preferences,"lastLon", 0.0f);
        //players name
        firstName  = preferences.getString("firstPlaceName", "");
        secondName  = preferences.getString("secondPlaceName", "");
        thirdName  = preferences.getString("thirdPlaceName","");
        //players score
        firstScore  = preferences.getInt("firstPlace", 0);
        secondScore  = preferences.getInt("secondPlace", 0);
        thirdScore  = preferences.getInt("thirdPlace", 0);
        //players location
        firstLatitude = getDouble(preferences,"fLat",0.0f);
        firstLongitude = getDouble(preferences,"fLon",0.0f);
        secondLatitude = getDouble(preferences,"sLat",0.0f);
        secondLongitude = getDouble(preferences,"sLon",0.0f);
        thirdLatitude = getDouble(preferences,"tLat",0.0f);
        thirdLongitude = getDouble(preferences,"tLon",0.0f);


    }

    double getDouble(final SharedPreferences prefs, final String key, final double defaultValue)
    { return Double.longBitsToDouble(prefs.getLong(key, Double.doubleToLongBits(defaultValue)));}

    public void backToMenu(View view){
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        view.getContext().startActivity(intent);
    }
}

