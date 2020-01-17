package com.example.myapplication;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Collections;

public class EndGame extends AppCompatActivity {

    private String name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_game);

        TextView scoreView = findViewById(R.id.scoreText);
        Button backMenu = findViewById(R.id.backMenu);
        Button highScore = findViewById(R.id.highScore);

        Gson gson = new Gson();
        Intent playScreen = getIntent();
        Score score = gson.fromJson(playScreen.getExtras().getString("score"),Score.class);
        final SharedPreferences sp = getSharedPreferences("Prefs", 0);
        ArrayList<Score> scores = gson.fromJson(sp.getString("scores",""),
                new TypeToken<ArrayList<Score>>(){}.getType());
        if(scores == null) {
            scores = new ArrayList<>();
        }
        if (scores.size() >= 10){
            Score lastScore = scores.get(scores.size() - 1);
            if (lastScore.getScore() < score.getScore()) {
                saveScore(score, scores,sp);
            }
        }else{
            saveScore(score,scores,sp);
        }
        scoreView.setText("Score : " + score);

        backMenu.setOnClickListener(view -> sendToMenu(view));
        highScore.setOnClickListener(view -> sendToHighScore(view));

    }

    public void saveScore(Score score, ArrayList<Score> scores, SharedPreferences sp){
        Gson gson = new Gson();
        //AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MyActivity.this, R.style.MyDialogTheme);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.promt_screen, null);
        // Set up the input
        final EditText inputName = promptsView.findViewById(R.id.editTextDialogUserInput);
        // Specify the type of input expected;
        inputName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        builder.setView(promptsView);
        builder.setCancelable(false).setPositiveButton(android.R.string.ok,(dialog, which) -> {
            dialog.dismiss();
            name = inputName.getText().toString().replace(",","").replace(":","");
            score.setName(name);
            scores.add(score);
            Collections.sort(scores);
            if(scores.size() > 10)
                scores.remove(10);
            sp.edit().putString("scores",gson.toJson(scores)).apply();
        }).create().show();
    }
    public void sendToHighScore(View view){
        Intent intent = new Intent(view.getContext(), HighScore.class);
        view.getContext().startActivity(intent);
        finish();
    }

    public void sendToMenu(View view){
        Intent intent = new Intent(view.getContext(), MainActivity.class);
        view.getContext().startActivity(intent);
        finish();
    }
}
