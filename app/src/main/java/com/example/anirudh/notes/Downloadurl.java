package com.example.anirudh.notes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class Downloadurl extends AppCompatActivity {
    Firebase myfirebase;
    String userName;
    String sname;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_downloadurl);
        Intent intent = getIntent();

        String filename = intent.getStringExtra("filename");

        Firebase.setAndroidContext(getApplicationContext());
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        userName = sharedPref.getString("userName", "Not Available");
        sname = sharedPref.getString("sname", "Not Available");
        Log.w("warning", "https://notes-d46cc.firebaseio.com/Subject/" + userName + "/" + sname + "/" +filename.substring(0,filename.indexOf(".")));
        String url = "https://notes-d46cc.firebaseio.com/Subject/" + userName + "/" + sname + "/" + filename.substring(0,filename.indexOf("."));

        myfirebase = new Firebase(url);
        myfirebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               String url = dataSnapshot.child("url").getValue(String.class);

                Intent i = new Intent();
                i.setPackage("com.android.chrome");
                i.setAction(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                finish();



            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(Downloadurl.this, firebaseError.getMessage(), Toast.LENGTH_LONG).show();


            }


        });
    }
    public void finish() {
        super.finish();
        overridePendingTransition(R.xml.slide_in_left, R.xml.slide_out_right);
    }
}
