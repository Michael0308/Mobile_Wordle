package edu.cuhk.csci3310.wordle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.FragmentTransaction;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Locale;

public class WordleActivity extends AppCompatActivity {

    private View viewGrid0_0, viewGrid0_1, viewGrid0_2, viewGrid0_3, viewGrid0_4, viewGrid1_0, viewGrid1_1,
            viewGrid1_2, viewGrid1_3, viewGrid1_4, viewGrid2_0, viewGrid2_1, viewGrid2_2, viewGrid2_3,
            viewGrid2_4, viewGrid3_0, viewGrid3_1, viewGrid3_2, viewGrid3_3, viewGrid3_4, viewGrid4_0,
            viewGrid4_1, viewGrid4_2, viewGrid4_3, viewGrid4_4, viewGrid5_0, viewGrid5_1, viewGrid5_2,
            viewGrid5_3, viewGrid5_4, currentView;

    private TextView textGrid0_0, textGrid0_1, textGrid0_2, textGrid0_3, textGrid0_4, textGrid1_0, textGrid1_1,
            textGrid1_2, textGrid1_3, textGrid1_4, textGrid2_0, textGrid2_1, textGrid2_2, textGrid2_3,
            textGrid2_4, textGrid3_0, textGrid3_1, textGrid3_2, textGrid3_3, textGrid3_4, textGrid4_0,
            textGrid4_1, textGrid4_2, textGrid4_3, textGrid4_4, textGrid5_0, textGrid5_1, textGrid5_2,
            textGrid5_3, textGrid5_4, currentTextView;

    boolean isRowFull = false;

    String[] keys = {"q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "a", "s", "d", "f", "g", "h",
            "j", "k", "l", "z", "x", "c", "v", "b", "n", "m"};
    
    int currentColumn = 0;

    int currentRow = 0;

    int guessCount = 0;

    boolean isCompleted = false;

    boolean isEndless = false;

    boolean guessed = false;
    String answer;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        currentColumn = 0;
        currentRow = 0;
        guessCount = 0;
        int guessMade = myPrefs.getInt("guessCount",0);
        //      TextGrid Input
        for (int i = 0; i <6; i++) {
            for (int j = 0; j <5; j++) {
                String key = "textGrid" + i + "_" + j;
                String value = myPrefs.getString(key,"");
                if (value.equals("") || value.equals(null)) return;
                generalKeyClick(value);
            }
            if (i< guessMade) onUserGuess();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();

//      TextGrid Input
        for (int i = 0; i <6; i++) {
            for (int j = 0; j <5; j++) {
                String key = "textGrid" + i + "_" + j;
                int id = getResources().getIdentifier(key,"id",getPackageName());
                TextView textView = findViewById(id);
                editor.putString(key,textView.getText().toString());
            }
        }

//      Game variables
        editor.putInt("currentColumn",currentColumn);
        editor.putInt("currentRow",currentRow);
        editor.putInt("guessCount",guessCount);
        editor.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_wordle);
        selectAnswerWord();
        Log.d("Ans",this.answer);
        toggleSwitchInit();
// ------------------------- ENTER AND DELETE BUTTONS ------------------------------------------------------

        AppCompatButton enterButton = findViewById(R.id.enterButton);
        AppCompatButton deleteButton = findViewById(R.id.deleteButton);

// ------------------------- GRID VIEWS ------------------------------------------------------

        viewGrid0_0 = findViewById(R.id.viewGrid0_0); viewGrid0_1 = findViewById(R.id.viewGrid0_1);
        viewGrid0_2 = findViewById(R.id.viewGrid0_2); viewGrid0_3 = findViewById(R.id.viewGrid0_3);
        viewGrid0_4 = findViewById(R.id.viewGrid0_4); viewGrid1_0 = findViewById(R.id.viewGrid1_0);
        viewGrid1_1 = findViewById(R.id.viewGrid1_1); viewGrid1_2 = findViewById(R.id.viewGrid1_2);
        viewGrid1_3 = findViewById(R.id.viewGrid1_3); viewGrid1_4 = findViewById(R.id.viewGrid1_4);
        viewGrid2_0 = findViewById(R.id.viewGrid2_0); viewGrid2_1 = findViewById(R.id.viewGrid2_1);
        viewGrid2_2 = findViewById(R.id.viewGrid2_2); viewGrid2_3 = findViewById(R.id.viewGrid2_3);
        viewGrid2_4 = findViewById(R.id.viewGrid2_4); viewGrid3_0 = findViewById(R.id.viewGrid3_0);
        viewGrid3_1 = findViewById(R.id.viewGrid3_1); viewGrid3_2 = findViewById(R.id.viewGrid3_2);
        viewGrid3_3 = findViewById(R.id.viewGrid3_3); viewGrid3_4 = findViewById(R.id.viewGrid3_4);
        viewGrid4_0 = findViewById(R.id.viewGrid4_0); viewGrid4_1 = findViewById(R.id.viewGrid4_1);
        viewGrid4_2 = findViewById(R.id.viewGrid4_2); viewGrid4_3 = findViewById(R.id.viewGrid4_3);
        viewGrid4_4 = findViewById(R.id.viewGrid4_4); viewGrid5_0 = findViewById(R.id.viewGrid5_0);
        viewGrid5_1 = findViewById(R.id.viewGrid5_1); viewGrid5_2 = findViewById(R.id.viewGrid5_2);
        viewGrid5_3 = findViewById(R.id.viewGrid5_3); viewGrid5_4 = findViewById(R.id.viewGrid5_4);

// ------------------------- GRID TEXT VIEWS ------------------------------------------------------

        textGrid0_0 = findViewById(R.id.textGrid0_0); textGrid0_1 = findViewById(R.id.textGrid0_1);
        textGrid0_2 = findViewById(R.id.textGrid0_2); textGrid0_3 = findViewById(R.id.textGrid0_3);
        textGrid0_4 = findViewById(R.id.textGrid0_4); textGrid1_0 = findViewById(R.id.textGrid1_0);
        textGrid1_1 = findViewById(R.id.textGrid1_1); textGrid1_2 = findViewById(R.id.textGrid1_2);
        textGrid1_3 = findViewById(R.id.textGrid1_3); textGrid1_4 = findViewById(R.id.textGrid1_4);
        textGrid2_0 = findViewById(R.id.textGrid2_0); textGrid2_1 = findViewById(R.id.textGrid2_1);
        textGrid2_2 = findViewById(R.id.textGrid2_2); textGrid2_3 = findViewById(R.id.textGrid2_3);
        textGrid2_4 = findViewById(R.id.textGrid2_4); textGrid3_0 = findViewById(R.id.textGrid3_0);
        textGrid3_1 = findViewById(R.id.textGrid3_1); textGrid3_2 = findViewById(R.id.textGrid3_2);
        textGrid3_3 = findViewById(R.id.textGrid3_3); textGrid3_4 = findViewById(R.id.textGrid3_4);
        textGrid4_0 = findViewById(R.id.textGrid4_0); textGrid4_1 = findViewById(R.id.textGrid4_1);
        textGrid4_2 = findViewById(R.id.textGrid4_2); textGrid4_3 = findViewById(R.id.textGrid4_3);
        textGrid4_4 = findViewById(R.id.textGrid4_4); textGrid5_0 = findViewById(R.id.textGrid5_0);
        textGrid5_1 = findViewById(R.id.textGrid5_1); textGrid5_2 = findViewById(R.id.textGrid5_2);
        textGrid5_3 = findViewById(R.id.textGrid5_3); textGrid5_4 = findViewById(R.id.textGrid5_4);


// ------------------------- BUTTONS LISTENERS ------------------------------------------------------

        deleteButton.setOnClickListener(view -> {
            isRowFull = false;
            if (currentColumn != 0){
                currentColumn--;
            }
            removeLetter(currentColumn, currentRow);
        });

        enterButton.setOnClickListener(view -> onUserGuess());

//      listener for all alphabet
        for (String key : keys){
            String buttonID = key + "Button";
            AppCompatButton currentButton = findViewById(getResources().getIdentifier(buttonID, "id", getPackageName()));
            currentButton.setOnClickListener(view -> generalKeyClick(key.toUpperCase(Locale.ROOT)));
        }

    }

    private void toggleSwitchInit() {
        SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        this.isEndless = myPrefs.getBoolean("endless",false);
        Switch toggle = findViewById(R.id.modeToggle);
        toggle.setChecked(isEndless? true:false);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                SharedPreferences.Editor editor = myPrefs.edit();
                editor.putBoolean("endless",isChecked?true:false);
                editor.apply();
                isEndless = isChecked?true:false;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void onUserGuess() {
        if (currentColumn != 5){
            Toast.makeText(WordleActivity.this, "Please enter a five letter word", Toast.LENGTH_SHORT).show();
        } else{
            guessCount++;
            String[] guessedWord = getGuessedWord(currentRow);
            CompareWords compare = new CompareWords(this.answer);

            Integer[] colors = compare.compareWords(guessedWord);
            displayColoursOnViews(colors, currentRow);

            if (Arrays.equals(colors, new Integer[] {1, 1, 1, 1, 1})) { // <- User has guessed the word
//                TODO: Adding action for completing a level
//                e.g. lock the keyboard, next level, show messages...

                MediaPlayer mp = MediaPlayer.create(this,R.raw.win_music);
                mp.start();
                Toast.makeText(WordleActivity.this, "You guessed the word!", Toast.LENGTH_SHORT).show();
                isCompleted = true;
                lockKeyboard();
                showCompleteDialog();
                return;
            }
            if (guessCount >= 6) {
//                TODO: Adding action for used up all tries
//                e.g. lock the keyboard, next level, show messages...
                Toast.makeText(WordleActivity.this, "You have no remaining tries!", Toast.LENGTH_SHORT).show();
                lockKeyboard();
                showCompleteDialog();
                return;
            }
            currentRow++;
            currentColumn = 0;
            isRowFull = false;
        }
    }

//  update achievement status after game completion
    public void updateAchievement(){
        SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();
        int winScore = myPrefs.getInt("score",0);
        if (winScore >0) editor.putBoolean("First Win!",true);
        if (winScore >=3) editor.putBoolean("Streak!",true);
        if (winScore >=5) editor.putBoolean("On fire!",true);
        if (winScore >=10) editor.putBoolean("Keen learner",true);
        if (winScore >=20) editor.putBoolean("Master",true);
        if (winScore >=50) editor.putBoolean("Too EZ",true);
        if (guessCount == 1) editor.putBoolean("Lucky guess",true);
        editor.apply();
    }
//  increment win score after a game
    public void addScore(){
        SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();
        int oldScore = myPrefs.getInt("score",0);
        int newScore = oldScore + 1;
        editor.putInt("score",newScore);
        editor.apply();
    }
//  clear data before preference save by onStop()
    public void clearPreference(){
        SharedPreferences myPrefs = getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = myPrefs.edit();

//      TextGrid Input
        for (int i = 0; i <6; i++) {
            for (int j = 0; j <5; j++) {
                String key = "textGrid" + i + "_" + j;
                int id = getResources().getIdentifier(key,"id",getPackageName());
                TextView textView = findViewById(id);

                textView.setText("");
                editor.putString(key,"");
            }
        }
        editor.apply();
    }
//  trigger pop up window
    private void showCompleteDialog(){
        GameCompleteFragment newFragment = new GameCompleteFragment();
        newFragment = newFragment.newInstance(isCompleted?"Good job!" : "Better next time!", this,isEndless);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.wordle_container, newFragment, "GameCompleteDialog");
        transaction.commit();
    }
//  lock the keyboard from giving any inputs
    private void lockKeyboard(){
//      all letter keys
        for (String key : keys){
            String buttonID = key + "Button";
            AppCompatButton currentButton = findViewById(getResources().getIdentifier(buttonID, "id", getPackageName()));
            currentButton.setEnabled(false);
        }
//      enter and delete button
        findViewById(R.id.enterButton).setEnabled(false);
        findViewById(R.id.deleteButton).setEnabled(false);
    }
//  display color of buttons
    private void displayColoursOnViews(Integer[] colors, int currentRow) {
        String currentViewID, currentTextViewID, buttonID;
        View currentView;
        TextView currentTextView;
        AppCompatButton currentButton;

        for (int i = 0; i < colors.length; i++){
            currentViewID = "viewGrid" + currentRow + "_" + i;
            currentTextViewID = "textGrid" + currentRow + "_" + i;
            currentView = findViewById(getResources().getIdentifier(currentViewID, "id", getPackageName()));
            currentTextView = findViewById(getResources().getIdentifier(currentTextViewID, "id", getPackageName()));

            buttonID = currentTextView.getText().toString().toLowerCase(Locale.ROOT) + "Button";
            currentButton = findViewById(getResources().getIdentifier(buttonID, "id", getPackageName()));

//          The card flipping animation
            Animation anim = AnimationUtils.loadAnimation(WordleActivity.this, R.anim.flip_out);
            anim.setStartOffset(anim.getDuration() * i);
            currentView.startAnimation(anim);
            currentTextView.startAnimation(anim);
            if (colors[i] == null){
                currentView.setBackgroundColor(getResources().getColor(R.color.key_black));
                currentButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.key_black)));
            } else if (colors[i] == 1){
                currentView.setBackgroundColor(getResources().getColor(R.color.key_green));
                currentButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.key_green)));
            } else {
                currentView.setBackgroundColor(getResources().getColor(R.color.key_yellow));
                currentButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.key_yellow)));
            }
        }
    }
//  retrieve input words from textviews
    private String[] getGuessedWord(int currentRow){
        String[] guessedWord = new String[5];
        for (int i = 0; i < 5; i++){
            String currentTextViewID = "textGrid" + currentRow + "_" + i;
            TextView currentTextView = findViewById(getResources().getIdentifier(currentTextViewID, "id", getPackageName()));
            guessedWord[i] = (String) currentTextView.getText();
        }

        return guessedWord;
    }
//  handle keyboard clicks
    private void generalKeyClick(String letter){
        if (!isRowFull){
            if (currentColumn != 5){
                displayLetter(currentColumn, currentRow, letter);
                currentColumn++;
            } else{
                isRowFull = true;
            }
        }
    }
//  display alphabet on text grid
    private void displayLetter(int currentColumn, int currentRow, String letter){
        String currentViewID = "viewGrid" + currentRow + "_" + currentColumn;
        String currentTextViewID = "textGrid" + currentRow + "_" + currentColumn;

        currentView = findViewById(getResources().getIdentifier(currentViewID, "id", getPackageName()));
        currentTextView = findViewById(getResources().getIdentifier(currentTextViewID, "id", getPackageName()));

        currentView.setAlpha((float) 1);
        currentTextView.setText(letter);

    }
//  remove input when delete pressed
    private void removeLetter(int currentColumn, int currentRow){
        String currentViewID = "viewGrid" + currentRow + "_" + currentColumn;
        String currentTextViewID = "textGrid" + currentRow + "_" + currentColumn;

        currentView = findViewById(getResources().getIdentifier(currentViewID, "id", getPackageName()));
        currentTextView = findViewById(getResources().getIdentifier(currentTextViewID, "id", getPackageName()));

        currentView.setAlpha((float) 0.2);
        currentTextView.setText("");

    }
//  read answer word from csv
    private void selectAnswerWord(){
        InputStream is = getResources().openRawResource(R.raw.answer);
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is , Charset.forName("UTF-8"))
        );
        SharedPreferences myPrefs = this.getSharedPreferences("myPrefs", MODE_PRIVATE);
        String line = "";
        int counter = 0;
        int target = myPrefs.getInt("score",0);
        try {
            // dump header line
            while ((line = reader.readLine()) != null) {
                if (counter >= target) {
                    this.answer = line;
                    return;
                }
                counter++;
            }
        }
        catch (IOException e) {
            Log.wtf("Failed to read achievements from csv in line: " + line, e);
            e.printStackTrace();
        }
    }
}