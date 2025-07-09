package edu.cuhk.csci3310.wordle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {
    Button start_button;
    Button credit_button;
    Button collection_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        start_button = (Button)findViewById(R.id.start_button);
        credit_button = (Button)findViewById(R.id.creditButton);
        collection_button = (Button)findViewById(R.id.collectionsButton);
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(HomeActivity.this, WordleActivity.class);
                startActivity(intent);
            }
        });
        credit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(HomeActivity.this, CreditActivity.class);
                startActivity(intent);
            }
        });
        collection_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(HomeActivity.this, CollectionActivity.class);
                startActivity(intent);
            }
        });
    }
}