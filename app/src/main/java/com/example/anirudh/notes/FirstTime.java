package com.example.anirudh.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class FirstTime extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String sem;
ImageButton img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time);


        ImageView imgv=(ImageView)findViewById(R.id.imageView);
        TextView tv=(TextView)findViewById(R.id.textView3);
        TextView tv1=(TextView)findViewById(R.id.textView);

        Intent intent=getIntent();
        String c=intent.getStringExtra("base");
        if(c.equals("nav"))

        {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            imgv.setVisibility(View.INVISIBLE);
            tv.setVisibility(View.INVISIBLE);
            tv1.setVisibility(View.VISIBLE);
        }
         else
        {
           tv1.setVisibility(View.INVISIBLE); }
        img=(ImageButton)findViewById(R.id.imageButton) ;
        img.setVisibility(View.INVISIBLE);
        Spinner spinner=(Spinner)findViewById(R.id.spinner);
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this,R.array.semester,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);



    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
img=(ImageButton)findViewById(R.id.imageButton) ;
img.setVisibility(View.VISIBLE);

            sem = parent.getItemAtPosition(position).toString();




    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        img=(ImageButton)findViewById(R.id.imageButton) ;
        img.setVisibility(View.INVISIBLE);
    }
    public void onclicksub(View view)
    {
        EditText name_ed=(EditText)findViewById(R.id.name);
        EditText srn_ed=(EditText)findViewById(R.id.srn);
        String name=name_ed.getText().toString();
        String srn =srn_ed.getText().toString();
        Intent intent=new Intent(FirstTime.this,Subject.class);
        intent.putExtra("name",name);
        intent.putExtra("srn",srn);
        intent.putExtra("sem",sem);
        startActivity(intent);
        overridePendingTransition(R.xml.slide_in_right, R.xml.slide_out_left);
    }
    public void finish() {
        super.finish();
        overridePendingTransition(R.xml.slide_in_left, R.xml.slide_out_right);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:


                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
