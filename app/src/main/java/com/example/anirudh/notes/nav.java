package com.example.anirudh.notes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;

public class nav extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    String uid;
    String finalsem;
    String finalsub;
    String finalcomb;
    TextView name_ed;
    TextView desc_ed;
    Firebase myfirebase;

    List<subject_mc> subnames ;
    RecyclerView myrv;
    RecyclerviewAdapter myAdapter;
    SwipeRefreshLayout swipeRefreshLayout;





    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav);
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





        subnames=new ArrayList<>();
        myrv= (RecyclerView) findViewById(R.id.recyclerview_id);
        myAdapter = new RecyclerviewAdapter(this,subnames);
        myrv.setLayoutManager(new GridLayoutManager(this,3));





        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        uid=currentFirebaseUser.getUid();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Firebase.setAndroidContext(getApplicationContext());
        String url="https://notes-d46cc.firebaseio.com/new/"+uid;

        myfirebase = new Firebase(url);
        myfirebase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                    try {


                        String name = dataSnapshot.child("name").getValue(String.class);
                        String sub = dataSnapshot.child("sub").getValue(String.class);
                        String sem = dataSnapshot.child("sem").getValue(String.class);
                        //Toast.makeText(nav.this, sub + sem, Toast.LENGTH_LONG).show();
                        if (sub.trim().equalsIgnoreCase("Computer Science")) {
                            finalsub = "CSE-";
                        } else if (sub.trim().equalsIgnoreCase("Electronics And Communication")) {
                            finalsub = "ECE-";
                        } else if (sub.trim().equalsIgnoreCase("Mechanical")) {
                            finalsub = "ME-";
                        }


                        if (sem.trim().equalsIgnoreCase("Physics Cycle")) {
                            finalsem = "Physics";
                        } else if (sem.trim().equalsIgnoreCase("Chemistry Cycle")) {
                            finalsem = "Chemistry";
                        } else if (sem.trim().equalsIgnoreCase("3rd Semester")) {
                            finalsem = "3rd Sem";
                        } else if (sem.trim().equalsIgnoreCase("4th Semester")) {
                            finalsem = "4th Sem";
                        } else if (sem.trim().equalsIgnoreCase("5th Semester")) {
                            finalsem = "5th Sem";
                        } else if (sem.trim().equalsIgnoreCase("6th Semester")) {
                            finalsem = "6th Sem";
                        } else if (sem.trim().equalsIgnoreCase("7th Semester")) {
                            finalsem = "7th Sem";
                        } else if (sem.trim().equalsIgnoreCase("8th Semester")) {
                            finalsem = "8th Sem";
                        }
                        if(finalsem.equals("Physics") ||finalsem.equals("Chemistry")  )
                        {
                            finalcomb=finalsem+" Cycle";

                        }
                        else {


                            finalcomb = finalsub + finalsem;
                        }
                        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(nav.this);
                        //now get Editor
                        SharedPreferences.Editor editor = sharedPref.edit();
                        //put your value
                        editor.putString("userName",finalcomb);


                        //commits your edits
                        editor.commit();
                        name_ed = (TextView) findViewById(R.id.name);
                        desc_ed = (TextView) findViewById(R.id.desc);
                        name_ed.setText(name);
                        desc_ed.setText(finalcomb);



                        Firebase.setAndroidContext(getApplicationContext());
                        String url1="https://notes-d46cc.firebaseio.com/Subjects/"+finalcomb;
                       // Toast.makeText(nav.this,url1,Toast.LENGTH_LONG).show();

                        myfirebase = new Firebase(url1);
                        myfirebase.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot dsp : dataSnapshot.getChildren()) {
                                    subnames.add(new subject_mc(
                                            dsp.child("subject").getValue(String.class)));
                                    //Toast.makeText(nav.this,dsp.child("subject").getValue(String.class),Toast.LENGTH_LONG).show();

                                }

                                myrv.setAdapter(myAdapter);






                            }


                            @Override
                            public void onCancelled(FirebaseError firebaseError) {
                                Toast.makeText(nav.this,firebaseError.getMessage(),Toast.LENGTH_LONG).show();


                            }
                        });
















                    }
                    catch (Exception e)
                    {
                        //Toast.makeText(nav.this,e.getMessage(),Toast.LENGTH_LONG).show();
                    }





                }




            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });





    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                final ProgressDialog progressDialog=new ProgressDialog(this);
                progressDialog.show();
                final Handler handler = new Handler();
                handler.postDelayed(


                        new Runnable() {
                    @Override
                    public void run() {

                        progressDialog.hide();
                    }
                }, 2000);
                this.recreate();
            }

        }
    }//



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.nav, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.mybutton) {
            this.recreate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.edit_details) {
            Intent intent=new Intent(nav.this,FirstTime.class);
            intent.putExtra("base","nav");
            startActivity(intent);
            overridePendingTransition(R.xml.slide_in_right, R.xml.slide_out_left);
            // Handle the camera action
        } else if (id == R.id.upload) {
            Intent intent=new Intent(nav.this,Upload.class);
            intent.putExtra("finalcomb",finalcomb);
            startActivityForResult(intent,1);
            overridePendingTransition(R.xml.slide_in_right, R.xml.slide_out_left);

        } else if (id == R.id.addsub) {
            Intent intent=new Intent(nav.this,addsub.class);
            intent.putExtra("comb",finalcomb);

            startActivityForResult(intent,1);
            overridePendingTransition(R.xml.slide_in_right, R.xml.slide_out_left);


        } else if (id == R.id.nav_manage) {
            finish();
            startActivity(getIntent());

        }
        else if (id == R.id.signout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        setResult(RESULT_OK, new Intent().putExtra("EXIT", true));

                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }

                }).create().show();
    }

}
