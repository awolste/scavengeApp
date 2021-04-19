package com.example.finalproject16;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class About extends AppCompatActivity {

    /**
     * @pre an intent has been started with the target activity as About
     * @post activity is set to About
     *
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        Button button = findViewById(R.id.home);
        button.setOnClickListener(mButtonClickListener);

    }

    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        /**
         * onClick is called when the learn more button is pressed
         * this function changes the activity page to the main page
         * @pre activity is set to about activity
         * @post activity is set to main page
         *
         * */
        public void onClick(View v) {
            startActivity(new Intent(About.this, MainActivity.class));
        }
    };
}
