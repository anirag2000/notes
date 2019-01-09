package com.example.anirudh.notes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import java.util.List;

public class recycleradapter_file extends RecyclerView.Adapter<recycleradapter_file.MyViewHolder> {
    String foldername;

    private Context mContext ;
    private List<subject_mc> mData ;
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_book_title;
        ImageView foldertype;
        CardView cardView ;
        public ImageView imageView;


        public MyViewHolder(View itemView) {
            super(itemView);

            tv_book_title = (TextView) itemView.findViewById(R.id.textView8f) ;

            cardView = (CardView) itemView.findViewById(R.id.cardviewf);
            foldertype=(ImageView)itemView.findViewById(R.id.imageView8f);




        }
    }



    public recycleradapter_file(Context mContext, List<subject_mc> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view ;
        LayoutInflater mInflater = LayoutInflater.from(mContext);
        view = mInflater.inflate(R.layout.cardview_file,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {




        String type= (mData.get(position).getSubject()).substring(mData.get(position).getSubject().indexOf("."),mData.get(position).getSubject().length());

if(mData.get(position).getSubject().length()<13) {
    holder.tv_book_title.setText(mData.get(position).getSubject());
}
else
{
    holder.tv_book_title.setText((mData.get(position).getSubject()).substring(0,5)+"..."+type);

}
        if(type.equals(".pdf"))
        {
            holder.foldertype.setImageResource(R.drawable.pdf);

        }
        else if(type.equals(".doc") || type.equals(".docx"))
        {
            holder.foldertype.setImageResource(R.drawable.word);

        }
        else if(type.equals(".ppt") || type.equals(".pptx"))
        {
            holder.foldertype.setImageResource(R.drawable.ppt);

        }
        else if(type.equals(".jpg") || type.equals(".jpeg")|| type.equals(".png"))
        {
            holder.foldertype.setImageResource(R.drawable.imageicon);

        }
        else
        {
            holder.foldertype.setImageResource(R.drawable.others);
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext,Downloadurl.class);

                // passing data to the book activity
               intent.putExtra("filename",mData.get(position).getSubject());



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
                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
                    String userName = sharedPref.getString("userName", "Not Available");
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




