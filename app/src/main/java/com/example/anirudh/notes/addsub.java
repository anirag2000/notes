package com.example.anirudh.notes;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class addsub extends AppCompatActivity {
    String finalcomb;
    DatabaseReference db;

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









    }
    public void finish() {
        super.finish();
        overridePendingTransition(R.xml.slide_in_left, R.xml.slide_out_right);
    }

}
