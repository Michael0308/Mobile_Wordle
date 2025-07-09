package edu.cuhk.csci3310.wordle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class CollectionActivity extends AppCompatActivity {
    private ArrayList<Achievement> achievementArrayList;
    private RecyclerView recyclerView;
    private TextView winCounter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);
        getSupportActionBar().hide();


        this.recyclerView = findViewById(R.id.achievement_recycleview);
        this.achievementArrayList = new ArrayList<>();

        setScore();
        readAchievementsData();
        setAdapter();
    }

    private void setScore(){
        this.winCounter = (TextView) findViewById(R.id.winCount);
        SharedPreferences myPrefs = this.getSharedPreferences("myPrefs", MODE_PRIVATE);
        int score = myPrefs.getInt("score",0);
        winCounter.setText(String.valueOf(score));
    }
    private void setAdapter(){
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(this.achievementArrayList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        this.recyclerView.setLayoutManager(layoutManager);
        this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        this.recyclerView.setAdapter(recyclerAdapter);
    }
    private void readAchievementsData() {
        InputStream is = getResources().openRawResource(R.raw.achievement_list);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is , Charset.forName("UTF-8"))
        );
        String line = "";
        try {
            // dump header line
            String header = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(",");
                SharedPreferences myPrefs = this.getSharedPreferences("myPrefs", MODE_PRIVATE);
                Achievement ach = new Achievement(tokens[0],myPrefs.getBoolean(tokens[0],false),tokens[1]);
                this.achievementArrayList.add(ach);
            }
        }
        catch (IOException e) {
            Log.wtf("Failed to read achievements from csv in line: " + line, e);
            e.printStackTrace();
        }
    }
}