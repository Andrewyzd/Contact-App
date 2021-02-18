package com.example.cet3013_assignment2_contactapp;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {
    //Declaring the variable and the object name of the classes
    private Context context;
    private Cursor cursor;
    ContactDatabase contactDatabase;
    contactClickListener clickListener = null;
    ArrayList<Long> idList = new ArrayList<Long>();

    /**
     * Constructor with parameter
     * overloaded constructor to initialize the data
     * @param context The object of the Context that is the current state of the application
     * @param cursor The object of the Cursor the used to process data information
     */
    public ContactAdapter(Context context, Cursor cursor){
        this.context = context;
        this.cursor = cursor;
    }
    /**
     * Interface to send the position of the selected contact
     */
    public interface contactClickListener{
        void contactClick(int position);
    }
    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_person_list, parent, false);
        ViewHolder holder = new ViewHolder(v, clickListener);
        return holder;
    }//end of onCreateViewHolder method

    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, int position) {
        //Ensure that the cursor can move to the position
        if(!cursor.moveToPosition(position)){
            return;
        }
        //Declare the variable to retrive the data from database
        //such as row id, famil name, first name, image file name
        long id = cursor.getLong(cursor.getColumnIndex("_id"));
        String family_name = cursor.getString(cursor.getColumnIndex(contactDatabase.getFieldFamilyName()));
        String first_name = cursor.getString(cursor.getColumnIndex(contactDatabase.getFieldFirstName()));
        String image_file_name = cursor.getString(cursor.getColumnIndex(contactDatabase.getFieldNameCard()));
        idList.add(id);//add the row id to the arraylist
        holder.itemView.setTag(id);//set the reference to the ViewHolder by using row id
        holder.name_text.setText(family_name +" "+first_name);//set the name to the view holder
        //if the user does not take any picture
        if(image_file_name.equals("R.drawable.person_icon")){
            //set the person icon as default
            holder.profileImage.setImageResource(R.drawable.person_icon);
        }else{
            //set the picture that is taken before
            Bitmap bitmap = PictureUtils.getScaledBitmap(image_file_name, 100, 100);
            holder.profileImage.setImageBitmap(bitmap);
        }
    }//end of onBindViewHolder method
    /**
     * getItemCount() - accessor method to get the number of row in cursor
     * @return cursor.getCount() This return the number of row in cursor
     */
    @Override
    public int getItemCount() {
        return cursor.getCount();
    }//end of getItemCount method
    /**
     * Function to update the dataset in cursor
     * @param newCursor The object of the Cursor
     */
    public void swapCursor(Cursor newCursor){
        //close the cursor if the cursor is not empty
        if(cursor != null){
            cursor.close();
        }
        cursor = newCursor;//assign the new cursor to old cursor
        //update the data in the cursor if the new cursor is not empty
        if(newCursor != null){
            notifyDataSetChanged();
        }
    }//end of swapCursor method
    /**
     * Function to set the click listener
     * @param clickListener The object of contactClickListener
     */
    public void setContactClickListener(contactClickListener clickListener){
        this.clickListener = clickListener;
    }
    /**
     * getID() - accessor method to get the row id based on selected contact on list view from ArrayList
     * @param position The position of the selected contact on the contact list view
     * @return idList This return the row id of the select contact in database from ArrayList
     */
    public Long getID(int position){
        return idList.get(position);
    }//end of getID method
    /**
     * Function to remove the id from
     * @param id The row id of the select contact in database
     */
    public void removeID(Long id){
        idList.remove(id);
    }//end of removeID method
    /**
     * Create ViewHolder as inner class
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //Declare the object for ImageView, TextView, and contactClickListener
        public ImageView profileImage;
        public TextView name_text;
        contactClickListener clickListener;

        public ViewHolder(@NonNull View itemView, contactClickListener clickListener) {
            super(itemView);
            //set the reference to the image view and name text
            profileImage = itemView.findViewById(R.id.profile_image);
            name_text = itemView.findViewById(R.id.fullName_text);
            this.clickListener = clickListener;
            itemView.setOnClickListener(this);//set the action when the view holder is clicked
        }//end of ViewHolder method

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(clickListener != null) clickListener.contactClick(position);
        }//end of onClick method
    }
}

