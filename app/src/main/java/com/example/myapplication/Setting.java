package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class Setting extends AppCompatActivity {

    private Button slowButton, fastButton, sensorButton, backButton;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        textView = findViewById(R.id.textView);
        slowButton = findViewById(R.id.buttonSlow);
        fastButton = findViewById(R.id.buttonFast);
        sensorButton = findViewById(R.id.buttonSensor);
        backButton = findViewById(R.id.buttonBack);

        slowButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View V) {
                slowSpeed(V);
            }
        });

        fastButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View V) {
                fastSpeed(V);
            }
        });

        sensorButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View V) {
                sensor(V);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View V) {
                backToMenu();
            }
        });
    }

    public void backToMenu(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void slowSpeed(View view){


        Intent intent = new Intent(view.getContext(), PlayScreen.class);
        intent.putExtra("speed", "slow");
        view.getContext().startActivity(intent);
        finish();
    }

    public void fastSpeed(View view){

        Intent intent = new Intent(view.getContext(), PlayScreen.class);
        intent.putExtra("speed", "fast");
        view.getContext().startActivity(intent);
        finish();
    }

    public void sensor(View view){

        Intent intent = new Intent(view.getContext(), PlayScreen.class);
        intent.putExtra("sensor", "on");
        view.getContext().startActivity(intent);
        finish();
    }
}
