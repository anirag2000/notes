package com.example.anirudh.notes;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.MyViewHolder> {
    String foldername;
    String userName;
    String user;
    String admin;
    String uid;
    Firebase myfirebase;

    private Context mContext ;
    private List<subject_mc> mData ;
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_book_title;
        ImageView img_book_thumbnail;
        CardView cardView ;
        public ImageView imageView;


        public MyViewHolder(View itemView) {
            super(itemView);

            tv_book_title = (TextView) itemView.findViewById(R.id.textView8) ;

            cardView = (CardView) itemView.findViewById(R.id.cardview);




        }
    }



    public RecyclerviewAdapter(Context mContext, List<subject_mc> mData) {
        this.mContext = mContext;
        this.mData = mData;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        userName = sharedPref.getString("userName", "Not Available");

        //now get Editor
        SharedPreferences.Editor editor = sharedPref.edit();
        //put your value
        editor.putString("foldername",foldername);


        //commits your edits
        editor.commit();

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);

        view = mInflater.inflate(R.layout.cardview_folder,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        holder.tv_book_title.setText(mData.get(position).getSubject());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);

                Intent intent = new Intent(mContext,Noteslist.class);

                // passing data to the book activity
                intent.putExtra("finalcomb",userName);

                intent.putExtra("Title",mData.get(position).getSubject());
                SharedPreferences sharedPref1 = PreferenceManager.getDefaultSharedPreferences(mContext);
                //now get Editor
                SharedPreferences.Editor editor = sharedPref.edit();
                //put your value
                editor.putString("sname",mData.get(position).getSubject());


                //commits your edits
                editor.commit();


                // start the activity
                mContext.startActivity(intent);



                //Toast.makeText(mContext, foldername, Toast.LENGTH_SHORT).show();




            }
        });
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                showPopupMenu(holder.cardView);
                foldername=mData.get(position).getSubject();



                return true;
            }
        });




    }
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.folder_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(final MenuItem menuItem) {








            switch (menuItem.getItemId()) {
                case R.id.rename:
                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
                    uid=currentFirebaseUser.getUid();



                    Firebase.setAndroidContext(mContext);
                    String url="https://notes-d46cc.firebaseio.com/new/"+uid;

                    myfirebase = new Firebase(url);
                    myfirebase.addListenerForSingleValueEvent((new com.firebase.client.ValueEventListener() {
                        @Override
                        public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                            admin= dataSnapshot.child("admin").getValue(String.class);
                            //Toast.makeText(Upload.this, admin, Toast.LENGTH_LONG).show();
                            try {



                                if (admin!=null && admin.equalsIgnoreCase("yes")) {
                                    ///////

                                    LayoutInflater inflater = LayoutInflater.from(mContext);

                                    View alertLayout = inflater.inflate(R.layout.layout_custom_dialog, null);
                                    final EditText etUsername = alertLayout.findViewById(R.id.et_username);
                                    AlertDialog.Builder alert = new AlertDialog.Builder(mContext);

                                    alert.setTitle("Enter a name to Rename the folder");
                                    // this is set the view from XML inside AlertDialog
                                    alert.setView(alertLayout);
                                    // disallow cancel of AlertDialog on click of back button and outside touch
                                    alert.setCancelable(false);
                                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Toast.makeText(mContext, "Cancel clicked", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            user = etUsername.getText().toString();
                                            DatabaseReference db=FirebaseDatabase.getInstance().getReference("Subjects");

                                            db.child(userName).child(foldername).removeValue();
                                            db.child(userName).child(user).setValue(new subject_mc(user));
                                            FirebaseDatabase.getInstance().getReference("files").child(userName).child(foldername)
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            FirebaseDatabase.getInstance().getReference("files").child(userName).child(user).setValue(dataSnapshot.getValue());
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });
                                            FirebaseDatabase.getInstance().getReference("Subject").child(userName).child(foldername)
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            FirebaseDatabase.getInstance().getReference("Subject").child(userName).child(user).setValue(dataSnapshot.getValue());
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                                        }
                                                    });

                                            Log.w("Warning",foldername);

                                            new Handler().postDelayed(new Runnable() {
                                                public void run() {
                                                    DatabaseReference db21=FirebaseDatabase.getInstance().getReference("Subject");

                                                    db21.child(userName).child(foldername).removeValue();
                                                    DatabaseReference db14=FirebaseDatabase.getInstance().getReference("files");

                                                    db14.child(userName).child(foldername).removeValue();



                                                }
                                            }, 3000);







                                        }
                                    });
                                    AlertDialog dialog = alert.create();
                                    dialog.show();



                                } else {
                                    Toast.makeText(mContext, "You are not a moderator,Please Contact Administrator ", Toast.LENGTH_LONG).show();


                                }

                            }
                            catch (Exception e)

                            {
                                Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                            }


                        }




                        @Override
                        public void onCancelled(FirebaseError firebaseError) {
                            admin="no";

                        }
                    }));










































                    return true;
                case R.id.delete:





























                  currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;



                uid=currentFirebaseUser.getUid();



                Firebase.setAndroidContext(mContext);
                String url1="https://notes-d46cc.firebaseio.com/new/"+uid;

                myfirebase = new Firebase(url1);
                myfirebase.addListenerForSingleValueEvent((new com.firebase.client.ValueEventListener() {
                    @Override
                    public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                        admin= dataSnapshot.child("admin").getValue(String.class);
                        //Toast.makeText(Upload.this, admin, Toast.LENGTH_LONG).show();
                        try {



                            if (admin!=null && admin.equalsIgnoreCase("yes")) {
                                ///////


                                DatabaseReference db12=FirebaseDatabase.getInstance().getReference("files");
                                db12.child(userName).child(foldername).removeValue();
                                DatabaseReference db2=FirebaseDatabase.getInstance().getReference("Subjects");
                                db2.child(userName).child(foldername).removeValue();
                                Toast.makeText(mContext,"PLEASE REFRESH....",Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(mContext, "You are not a moderator,Please Contact Administrator ", Toast.LENGTH_LONG).show();


                            }

                        }
                        catch (Exception e)

                        {
                            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_LONG).show();
                        }


                    }




                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        admin="no";

                    }
                }));





































                    return true;










                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }




}
