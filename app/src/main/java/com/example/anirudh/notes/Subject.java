package com.example.anirudh.notes;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Subject extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
String sub;
    ImageButton img;
    String name;
    String srn;
    String sem;
DatabaseReference db;
String uid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);
        img=(ImageButton)findViewById(R.id.imageButton) ;
        img.setVisibility(View.INVISIBLE);
        db=FirebaseDatabase.getInstance().getReference("new");

        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
         uid=currentFirebaseUser.getUid();

        Spinner spinner=(Spinner)findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.subject,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
        Intent intent=getIntent();
        name=intent.getStringExtra("name");
        srn=intent.getStringExtra("srn");
        sem=intent.getStringExtra("sem");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        sub = parent.getItemAtPosition(position).toString();
        img=(ImageButton)findViewById(R.id.imageButton) ;
        img.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        img=(ImageButton)findViewById(R.id.imageButton) ;
        img.setVisibility(View.INVISIBLE);

    }
public void onclickfinal(View view)
{
    student_mc mc=new student_mc(name,sem,sub,srn,"no");
    db.child(uid).setValue(mc);

    Intent mStartActivity = new Intent(Subject.this, nav.class);
    Toast.makeText(Subject.this,"please restart app to viiew changes",Toast.LENGTH_LONG).show();
    startActivity(mStartActivity);
    overridePendingTransition(R.xml.slide_in_right, R.xml.slide_out_left);

}

}
