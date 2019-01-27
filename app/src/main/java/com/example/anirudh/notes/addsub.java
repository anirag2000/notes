package com.example.anirudh.notes;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addsub extends AppCompatActivity {
    String finalcomb;
    DatabaseReference db;
    Firebase myfirebase;
    String uid;
    String admin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addsub);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        finalcomb=intent.getStringExtra("comb");
        db=FirebaseDatabase.getInstance().getReference("Subjects");



    }
    public  void onclicksu(View vew)
    {

       FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;



        uid=currentFirebaseUser.getUid();



        Firebase.setAndroidContext(getApplicationContext());
        String url1="https://notes-d46cc.firebaseio.com/new/"+uid;

        myfirebase = new Firebase(url1);
        myfirebase.addListenerForSingleValueEvent((new com.firebase.client.ValueEventListener() {
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                admin= dataSnapshot.child("admin").getValue(String.class);
                //Toast.makeText(Upload.this, admin, Toast.LENGTH_LONG).show();
                try {



                    if (admin!=null && admin.equalsIgnoreCase("yes")) {
                        EditText ed=(EditText)findViewById(R.id.editText);
                        String sub=ed.getText().toString();
                        if(sub.length()<15) {
                            try {
                                subject_mc subjectMc = new subject_mc(sub);
                                db.child(finalcomb).child(sub).setValue(subjectMc);
                                Intent intent = new Intent();
                                setResult(Activity.RESULT_OK, intent);
                                finish();

                            } catch (Exception e
                                    ) {
                                Toast.makeText(addsub.this, e.getMessage().toString(), Toast.LENGTH_LONG).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(addsub.this,"Enter a name within 15 characters",Toast.LENGTH_LONG).show();
                        }







                    } else {
                        Toast.makeText(addsub.this, "You are not a moderator,Please Contact Administrator ", Toast.LENGTH_LONG).show();


                    }

                }
                catch (Exception e)

                {
                    Toast.makeText(addsub.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }


            }




            @Override
            public void onCancelled(FirebaseError firebaseError) {
                admin="no";

            }
        }));































        //////










    }
    public void finish() {
        super.finish();
        overridePendingTransition(R.xml.slide_in_left, R.xml.slide_out_right);
    }

}
