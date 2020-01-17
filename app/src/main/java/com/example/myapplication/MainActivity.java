package com.example.myapplication;

    import androidx.annotation.NonNull;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.core.app.ActivityCompat;
    import androidx.core.content.ContextCompat;
    import android.Manifest;
    import android.content.Intent;
    import android.content.pm.PackageManager;
    import android.os.Bundle;
    import android.widget.Button;
    import android.widget.Toast;
    import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Button buttonNewGame;
    private Button buttonSetting;
    private Button buttonHighScore;

    private final int REQUEST_CODE_LOCARION_PERMISSION = 15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonNewGame = findViewById(R.id.buttonNewGame);
        buttonSetting = findViewById(R.id.buttonSetting);
        buttonHighScore = findViewById(R.id.button3);
        buttonNewGame.setOnClickListener(v -> openPlayScreen());
        buttonSetting.setOnClickListener(v -> openSetting());
        buttonHighScore.setOnClickListener(v -> openHighScore());
        getPermissions();

    }

    public void openPlayScreen(){
        Intent intent = new Intent(this, PlayScreen.class);
        startActivity(intent);
        finish();
    }

    public void openSetting(){
        Intent intent = new Intent(this, Setting.class);
        startActivity(intent);
        finish();
    }

    public void openHighScore(){
        Intent intent = new Intent(this, HighScore.class);
        startActivity(intent);
        finish();
    }

    public void getPermissions(){
        // Check whether this app has write external storage permission or not.
        int writeLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        ArrayList<String> permissions = new ArrayList<>();
        if(writeLocationPermission != PackageManager.PERMISSION_GRANTED)
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        if(permissions.size() > 0)
            ActivityCompat.requestPermissions(this,
                    permissions.toArray(new String[0]),REQUEST_CODE_LOCARION_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE_LOCARION_PERMISSION) {
            int grantResultsLength = grantResults.length;
            if (!(grantResultsLength > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                Toast.makeText(this, "Please Grant Permissions!",Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}
