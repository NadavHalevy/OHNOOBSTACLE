package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import java.util.List;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import android.hardware.SensorEvent;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;


public class PlayScreen extends AppCompatActivity implements View.OnClickListener,SensorEventListener  {

    private FusedLocationProviderClient fusedLocationClient;

    final int ZERO = 0, LIFE = 3, UP = 1500;
    private int END = 780, STEP = (END / 4);
    private Random random;
    Vibrator vib;
    List<Integer> divideScreenWidth = new ArrayList<>();
    private BroadcastReceiver broadcastReceiver;

    //image
    private ImageView car, spike1, spike2, spike3, spike4, fuel, heart1, heart2, heart3;
    private ImageButton left,right;

    //position
    private int spike1X, spike2X, spike3X, spike4X, fuelX;
    private float carX, spike1Y;
    //score && life
    private int meter, score, lifes;
    private TextView showScore, showMeter;
    // time
    private Timer timer;
    private Handler handler = new Handler();
    //status
    private boolean playingNow = false, actionLeftFlag = false, actionRightFlag = false, sensorM = false;
    //mode play
    private String speed, senModeStr;
    private SensorManager sensorManager;
    private double latitude = 0,longitude = 0;
    private int moveSpeed;
    Sensor sensor;
    boolean isHit = false;
    //sound
    //private SoundClass sound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_screen);
//        Point p = new Point();
//        this.getWindowManager().getDefaultDisplay().getSize(p);
//        END = p.x;
//        STEP = END/4;
        // Call object
        car = findViewById(R.id.car);
        spike1 = findViewById(R.id.spike1);
        spike2 = findViewById(R.id.spike2);
        spike3 = findViewById(R.id.spike3);
        spike4 = findViewById(R.id.spike4);
        fuel = findViewById(R.id.fuel);
        showScore = findViewById(R.id.showScore);
        showMeter = findViewById(R.id.showMeter);
        heart1 = findViewById(R.id.heart1);
        heart2 = findViewById(R.id.heart2);
        heart3 = findViewById(R.id.heart3);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        left.setOnClickListener(this);
        right.setOnClickListener(this);
        random = new Random();
        timer = new Timer();
        //sound = new SoundClass(this);
        sensorManager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);

        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener((SensorEventListener) PlayScreen.this,sensor,SensorManager.SENSOR_DELAY_NORMAL);
        //getGpsInfo();
        //mode play
        String slowSpeed = "slow";
        Intent modeIntent = getIntent();
        speed = modeIntent.getStringExtra("speed");
        if(speed != null && speed.equals(slowSpeed))
            moveSpeed = 18;
        else
            moveSpeed = 26;

        String senMode = "on";
        Intent sensorIntent = getIntent();
        senModeStr = sensorIntent.getStringExtra("sensor");
        if(senModeStr != null && senModeStr.equals(senMode)) {
            sensorM = true;
            left.setVisibility(View.GONE);
            right.setVisibility(View.GONE);
        }
        else sensorM = false;


        //Initial variables
        for (int k = 0; k < 5; k++)
            divideScreenWidth.add(k * STEP);
        vib = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        playingNow = true;
        carX = divideScreenWidth.get(divideScreenWidth.size() / 2);
        //car.setX(carX);
        spike1Y = spike1.getY();

        lifes = LIFE;
        score = ZERO;
        meter = ZERO;
        showScore.setText("SCORE: " + score);
        showMeter.setText("METER: " + meter);
        //Car Move
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (playingNow)
                    handler.post(() -> changePos());
            }
        }, 0, 100);

        //Play Movement
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (playingNow)
                    handler.post(() -> playMovement());
            }
        }, 1, 20);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    protected void onPause() {
        playingNow = false;
        super.onPause();
    }

    @Override
    protected void onResume() {
        playingNow = true;

        super.onResume();
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {this.longitude = location.getLongitude(); this.latitude = location.getLatitude();});
    }

    public void changePos() {

        if (actionRightFlag) {
            carX += STEP;
            actionRightFlag = false;
        }
        if (actionLeftFlag) {
            carX -= STEP;
            actionLeftFlag = false;
        }

        // Check car position
        if (carX < ZERO) {
            car.setX(ZERO);
            carX = ZERO;
        }
        if (carX > END) {
            car.setX(END);
            carX = END;
        }
        car.setX(carX);
    }

    private void setLife() {
        switch (lifes) {
            case 2:
                heart2.setVisibility(View.INVISIBLE);
                break;
            case 1:
                heart3.setVisibility(View.INVISIBLE);
                break;
            case ZERO:
                heart1.setVisibility(View.INVISIBLE);
                endGame();
                break;
        }
    }

    public void playMovement() {

        spike1Y += moveSpeed;
        //Check zombie position
        if (spike1Y > UP) {
            spike1Y = ZERO;
            spike1X = divideScreenWidth.remove(random.nextInt(divideScreenWidth.size() - 1));
            spike2X = divideScreenWidth.remove(random.nextInt(divideScreenWidth.size() - 1));
            spike3X = divideScreenWidth.remove(random.nextInt(divideScreenWidth.size() - 1));
            spike4X = divideScreenWidth.remove(random.nextInt(divideScreenWidth.size() - 1));
            fuelX = 30 + divideScreenWidth.get(ZERO);
            divideScreenWidth.add(spike1X);
            divideScreenWidth.add(spike2X);
            divideScreenWidth.add(spike3X);
            divideScreenWidth.add(spike4X);
        }

        spike1.setX(spike1X);
        spike1.setY(spike1Y);
        spike2.setX(spike2X);
        spike2.setY(spike1Y);
        spike3.setX(spike3X);
        spike3.setY(spike1Y);
        spike4.setX(spike4X);
        spike4.setY(spike1Y);
        fuel.setX(fuelX);
        fuel.setY(spike1Y + 40);
        if (hitCheck(fuelX - 30 , spike1Y)) {
            //if(spike1Y%20 == 0)
            score += 5;
            showScore.setText("SCORE: " + score);
        }

        if (hitCheck(spike1X, spike1Y) || hitCheck(spike2X, spike1Y) || hitCheck(spike3X, spike1Y) || hitCheck(spike4X, spike1Y)) {
            if(isHit)
                return;
            isHit = true;
            lifes--;
            vibrationPhone();
            //sound.playHit();
            setLife();
        } else {
            isHit = false;
            for (int i = 0; i < 100; i++) ;
            meter += 1;
            showMeter.setText("METER: " + meter);
        }

    }

    public boolean hitCheck(float x, float y) {
        return carX == x && car.getY() + 100 <= y;
    }

    public void endGame(){
            Intent endGameIntent = new Intent(PlayScreen.this, EndGame.class);
            endGameIntent.putExtra("score",new Gson().toJson(new Score("eled",score,longitude,latitude)));
            startActivity(endGameIntent);
            finish();
        }

    public void vibrationPhone() {
        if (Build.VERSION.SDK_INT >= 26) {
            vib.vibrate((VibrationEffect.createOneShot(150, VibrationEffect.DEFAULT_AMPLITUDE)));
            if (lifes == ZERO)
                vib.vibrate((VibrationEffect.createOneShot(350, VibrationEffect.DEFAULT_AMPLITUDE)));
        } else {
            vib.vibrate(150);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.left:
                actionLeftFlag = true;
                break;
            case R.id.right:
                actionRightFlag = true;
                break;
            default:
                actionLeftFlag = false;
                actionRightFlag = false;
                break;
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (sensorM) {
            if (event.values[0] > 3)
                actionLeftFlag = true;
            else if (event.values[0] < -3)
                actionRightFlag = true;
            else {
                actionLeftFlag = false;
                actionRightFlag = false;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private void startGpsService() {
        if (!runtime_permissions()) {
            Intent i = new Intent(getApplicationContext(), GPS_Service.class);
            startService(i);
        }

    }

    private boolean runtime_permissions() {
        if (Build.VERSION.SDK_INT >= 28 && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);

            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//                startGpsService();
//            } else {
//                runtime_permissions();
//            }
        }
    }

    public void getGpsInfo(){
        startGpsService();
        if (broadcastReceiver == null) {
            broadcastReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {

                    latitude = (double) intent.getExtras().get("latitude");
                    longitude = (double) intent.getExtras().get("longitude");

                }
            };
        }
        registerReceiver(broadcastReceiver, new IntentFilter("update_location"));

    }


}

