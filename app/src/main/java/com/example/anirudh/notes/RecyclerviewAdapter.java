package com.example.anirudh.notes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.MyViewHolder> {
    String foldername;
    String userName;

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

                Intent intent = new Intent(mContext,Noteslist.class);

                // passing data to the book activity
                intent.putExtra("finalcomb",userName);

                intent.putExtra("Title",mData.get(position).getSubject());
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
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
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.rename:
                    Toast.makeText(mContext, foldername, Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.delete:

                    DatabaseReference db=FirebaseDatabase.getInstance().getReference("Subjects");

                   db.child(userName).child(foldername).removeValue();
                    Intent i1=new Intent(mContext,nav.class);
                    mContext.startActivity(i1);















                    //Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
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
