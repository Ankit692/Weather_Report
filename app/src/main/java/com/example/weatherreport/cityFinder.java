package com.example.weatherreport;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class cityFinder extends AppCompatActivity {
//this activity  is created to use the entered name of the city to display the
    //weather details in the mainActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_finder);
        final EditText mEditText=findViewById(R.id.searchCity);
        ImageView mImageView=findViewById(R.id.backButton);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String newCity=mEditText.getText().toString();
                Intent intent=new Intent(cityFinder.this,MainActivity.class);
                intent.putExtra("City",newCity);
                startActivity(intent);

                return false;
            }
        });
    }
}