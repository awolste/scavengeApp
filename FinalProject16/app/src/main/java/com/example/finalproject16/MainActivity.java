package com.example.finalproject16;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Camera;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MainActivity extends AppCompatActivity {

    GoogleSignInClient mGoogleSignInClient;

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
            String personEmail = acct.getEmail();
            String personId = acct.getId();

            name.setText(personName);
        }

    }

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
}