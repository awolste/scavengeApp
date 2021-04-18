package com.example.finalproject16;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;
    private InfoFetcher mInfoFetcher;
    private String email;
    /**
     * @pre the app has been started
     * @post activity is set to MainActivity and shows the sign in page or home page if a user has already signed in
     *
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        Button button = findViewById(R.id.toLeaderboard);
        button.setOnClickListener(mButtonClickListener);

        Button button2 = findViewById(R.id.toMap);
        button2.setOnClickListener(mButtonClickListener2);

        TextView name = findViewById(R.id.name);
        Button signOutButton = findViewById(R.id.sign_out_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            /**
             * onClick is called when the learn more button is pressed
             * this function changes the activity page to the bio page
             * @pre activity is set to main activity
             * @post activity is set to bio page
             *
             * */
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.sign_out_button:
                        signOut();
                        break;
                }
            }
        });

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null) {
            String personName = acct.getGivenName();
            email = acct.getEmail();
            String personId = acct.getId();

            name.setText(personName);

            mInfoFetcher = new InfoFetcher(this);

            try {
                mInfoFetcher.getUser(mFetchListener, email);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * @pre the user has selected to sign out
     * @post no account is associated with the current instance of the app and the user may select to sign in
     *
     * */
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MainActivity.this, "Signed Out!", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
    }

    private void createNew() {
        try {
            mInfoFetcher.createUser(mFetchListener2, email);
            //Toast.makeText(MainActivity.this, "Thanks for joining!", Toast.LENGTH_LONG).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Create an anonymous implementation of OnClickListener
    private View.OnClickListener mButtonClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, Leaderboard.class));
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
            startActivity(new Intent(MainActivity.this, MapsActivity.class));
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
            if (info.size() == 0){
                Log.i("CREATINGNEW", email);
                createNew();
            }
            else {
                if(!info.get(0).equals(email)){
                    Log.i("CREATINGNEW", email);
                    createNew();
                }
            }
        }

        @Override
        public void onErrorResponse(VolleyError error) {

        }
    };

    private InfoFetcher.OnDataReceivedListener mFetchListener2 = new InfoFetcher.OnDataReceivedListener() {

        @Override
        public void onDataReceived(List<String> info) {
            //Toast.makeText(MainActivity.this, "Thanks for joining!", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onErrorResponse(VolleyError error) {
        }
    };
}