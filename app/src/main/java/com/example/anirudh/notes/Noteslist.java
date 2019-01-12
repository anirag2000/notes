package com.example.anirudh.notes;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Noteslist extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout;
    List<subject_mc> filenames ;
    RecyclerView myrv;
   recycleradapter_file myAdapter;
    Firebase myfirebase;
    String finalcomb1;
    String sname;


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noteslist);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent=getIntent();
        sname=intent.getStringExtra("Title");
        finalcomb1=intent.getStringExtra("finalcomb");
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.refresh);
        swipeRefreshLayout.setColorSchemeColors(R.color.colorAccent,R.color.colorPrimaryDark,R.color.colorAccent);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);

                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        finish();
                        startActivity(getIntent());

                    }
                }, 1000);




            }});


        filenames=new ArrayList<>();
        myrv= (RecyclerView) findViewById(R.id.recyclerview_id);
        myAdapter = new recycleradapter_file(this,filenames);
        myrv.setLayoutManager(new GridLayoutManager(this,3));
        Firebase.setAndroidContext(getApplicationContext());
        String url1="https://notes-d46cc.firebaseio.com/files/"+finalcomb1+"/"+sname;
        // Toast.makeText(nav.this,url1,Toast.LENGTH_LONG).show();

        myfirebase = new Firebase(url1);
        myfirebase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                        filenames.add(new subject_mc(
                                dsp.child("subject").getValue(String.class)));
                        //Toast.makeText(nav.this,dsp.child("subject").getValue(String.class),Toast.LENGTH_LONG).show();

                    }
                    int no = filenames.size();
                    if (no == 0) {
                        TextView gonetext = (TextView) findViewById(R.id.gonetext);
                        gonetext.setText("The folder is empty. Please Upload Files");
                    }

                    myrv.setAdapter(myAdapter);
                }
                catch (Exception e)
                {
                    Toast.makeText(Noteslist.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }







            }


            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(Noteslist.this,firebaseError.getMessage(),Toast.LENGTH_LONG).show();


            }
        });








    }
    public void finish() {
        super.finish();
        overridePendingTransition(R.xml.slide_in_left, R.xml.slide_out_right);
    }
}
