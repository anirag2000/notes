package com.example.anirudh.notes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public  void onclick(View view)
    {
        Intent intent=new Intent(MainActivity.this,Main2Activity.class);
        EditText ed=(EditText)findViewById(R.id.code) ;
        String no=ed.getText().toString();

        intent.putExtra("code",no);




        startActivity(intent);
        overridePendingTransition(R.xml.slide_in_right, R.xml.slide_out_left);

    }


}
