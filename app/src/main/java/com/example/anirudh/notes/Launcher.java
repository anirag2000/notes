package com.example.anirudh.notes;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Launcher extends AppCompatActivity {
    FirebaseAuth auth;
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);


        ProgressBar pb=(ProgressBar)findViewById(R.id.progressBar2);
        Thread timer= new Thread()
        {
            public void run()
            {
                try
                {
                    //Display for 3 seconds
                    sleep(3000);
                }
                catch (InterruptedException e)
                {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                finally {
                    mFirebaseAuth=FirebaseAuth.getInstance();
                    mFirebaseUser=mFirebaseAuth.getCurrentUser();

                    //Goes to Activity  StartingPoint.java(STARTINGPOINT)
                    if (mFirebaseUser == null){
                        //Not signed in, launch the Sign In Activity
                        startActivity(new Intent(Launcher.this, MainActivity.class));
                        finish();

                    }
                    else if (mFirebaseUser != null){
                        //Not signed in, launch the Sign In Activity
                        startActivity(new Intent(Launcher.this, nav.class));
                        finish();
                     
                    }

                }
            }
        };
        timer.start();

    }
}
