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
         * @pre activity is set to main activity
         * @post activity is set to bio page
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
         * @pre activity is set to main activity
         * @post activity is set to bio page
         *
         * */
        public void onClick(View v) {
            startActivity(new Intent(Leaderboard.this, MapsActivity.class));
        }
    };

    /**
     * @Pre
     *      fetcher is called
     * @Post
     *      data is recieved
     *      temptextview is set
     *      condtextview is set
     *      windtextview is set
     *      detailstextview is set
     *
     * Source Zybooks 5.2, 5.3
     * */
    private InfoFetcher.OnDataReceivedListener mFetchListener = new InfoFetcher.OnDataReceivedListener() {

        @Override
        public void onDataReceived(List<String> info) {
            //email.setText(info.get(0));
            //points.setText(info.get(0) + " PTS");
            int count = 0;
            for (int i = 0; i < info.size()/2; i++ ) {
                TableRow row = new TableRow(Leaderboard.this);
                row.setBackgroundColor(getResources().getColor(R.color.white));

                TextView email = new TextView(Leaderboard.this);
                TextView points = new TextView(Leaderboard.this);

                email.setText(info.get(count));
                email.setTextSize(20);
                email.setTextColor( getResources().getColor(R.color.third));
                email.setBackgroundColor(getResources().getColor(R.color.primary));
                email.setPadding(1,1,1,1);
                count++;

                points.setText(info.get(count));
                points.setTextSize(20);
                points.setTextColor( getResources().getColor(R.color.third));
                points.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
                points.setBackgroundColor(getResources().getColor(R.color.primary));
                points.setPadding(1,1,1,1);
                count++;

                row.addView(email);
                row.addView(points);
                board.addView(row);
            }
        }

        @Override
        public void onErrorResponse(VolleyError error) {
            //email.setText(error.toString());
            //points.setText("Error finding data");
        }
    };
}
