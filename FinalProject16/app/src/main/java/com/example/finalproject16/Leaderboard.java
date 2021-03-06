/**
 * Andrew Wolstenholme (awolste) C11132861
 * Nick Provost (nprovos) C13162083
 * Leaderboard.java
 * */
package com.example.finalproject16;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;

import org.json.JSONException;

import java.util.List;

public class Leaderboard  extends AppCompatActivity {


    private InfoFetcher mInfoFetcher;
    private TableLayout board;
    private Button home;
    private Button map;
    /**
     * @pre an intent has been started with the target activity as Leaderboard
     * @post activity is set to Leaderboard
     *
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboard);


        home = findViewById(R.id.home);
        home.setOnClickListener(mButtonClickListener);
        map = findViewById(R.id.map);
        map.setOnClickListener(mButtonClickListener2);


        //TableLayout table = new TableLayout(this);
        board = findViewById(R.id.table);
        //board.addView(table);

        mInfoFetcher = new InfoFetcher(this);
        mInfoFetcher.getAll(mFetchListener);
    }

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        /**
         * onClick is called when the learn more button is pressed
         * this function changes the activity page to the bio page
         * @pre activity is set to leaderboard activity
         * @post activity is set to main page
         *
         * */
        public void onClick(View v) {
            startActivity(new Intent(Leaderboard.this, MainActivity.class));
        }
    };

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener mButtonClickListener2 = new View.OnClickListener() {
        /**
         * onClick is called when the learn more button is pressed
         * this function changes the activity page to the bio page
         * @pre activity is set to leaderboard activity
         * @post activity is set to maps page
         *
         * */
        public void onClick(View v) {
            startActivity(new Intent(Leaderboard.this, MapsActivity.class));
        }
    };


    private InfoFetcher.OnDataReceivedListener mFetchListener = new InfoFetcher.OnDataReceivedListener() {

        /**
         * @Pre
         *      fetcher is called
         * @Post
         *      data is recieved
         *      data is sorted and displayed
         *
         * Source Zybooks 5.2, 5.3
         * */
        @Override
        public void onDataReceived(List<String> info) {

            boolean sorted = false;
            String tempEmail;
            String tempScore;
            while(!sorted) {
                sorted = true;
                for (int i = 1; i < info.size() - 2; i += 2) {
                    if (Integer.parseInt(info.get(i)) < Integer.parseInt(info.get(i + 2))) {
                        tempEmail = info.get(i - 1);
                        tempScore = info.get(i);
                        info.set(i - 1, info.get(i + 1));
                        info.set(i, info.get(i + 2));
                        info.set(i + 1, tempEmail);
                        info.set(i + 2, tempScore);
                        sorted = false;
                    }
                }
            }

            int count = 0;
            for (int i = 0; i < info.size()/2; i++ ) {
                TableRow row = new TableRow(Leaderboard.this);
                row.setBackgroundColor(getResources().getColor(R.color.white));

                TextView email = new TextView(Leaderboard.this);
                TextView points = new TextView(Leaderboard.this);

                email.setText(info.get(count));
                email.setTextSize(25);
                email.setTextColor( getResources().getColor(R.color.third));
                email.setBackgroundColor(getResources().getColor(R.color.primary));
                email.setPadding(25,1,1,1);
                count++;

                points.setText(info.get(count));
                points.setTextSize(25);
                points.setTextColor( getResources().getColor(R.color.third));
                points.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                points.setBackgroundColor(getResources().getColor(R.color.primary));
                points.setPadding(1,1,25,1);
                count++;

                row.addView(email);
                row.addView(points);
                board.addView(row);
            }
        }

        /**
         * @Pre
         *      fetcher is called
         * @Post
         *      data was not found
         *
         * Source Zybooks 5.2, 5.3
         * */
        @Override
        public void onErrorResponse(VolleyError error) {
            //email.setText(error.toString());
            //points.setText("Error finding data");
        }
    };
}
