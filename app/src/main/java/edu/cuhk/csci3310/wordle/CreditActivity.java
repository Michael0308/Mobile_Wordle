package edu.cuhk.csci3310.wordle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class CreditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit);
        getSupportActionBar().hide();
    }
}