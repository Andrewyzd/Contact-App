package com.example.cet3013_assignment2_contactapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements  View.OnClickListener{
    public static final String TAG = "MainActivity";
    //Declaring the variable and the object name of the classes
    private FloatingActionButton createNewContact_floatingActionButton,  modify_floatingActionButton, delete_floatingActionButton;
    private long row_id;
    ContactAdapter adapter;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layout;
    detailsDisplay_Fragment contactDetails;
    ContactDatabase contactDatabase = new ContactDatabase(this);
    /**
     * Function to trigger the intent of the activity of MainActivity
     * @param packageName The object of the Context that is the current state of the application
     * @return intent This return the object of the Intent
     */
    public static Intent newIntent(Context packageName) {
        Intent intent = new Intent(packageName, MainActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //set the reference to the floating action button
        createNewContact_floatingActionButton = findViewById(R.id.createNewContact_floatingActionButton);
        //set the reference to the Recycler view
        recyclerView = findViewById(R.id.contactList_recycleView2);
        //assign all the contact information to the interface
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Obtain the contact details from the database
        adapter = new ContactAdapter(this, contactDatabase.getContactList());
        recyclerView.setAdapter(adapter);
        //update cursor
        adapter.swapCursor(contactDatabase.getContactList());
        //register the click event
        createNewContact_floatingActionButton.setOnClickListener(this);
        //set the action when user click on a contact on the contact list view
        adapter.setContactClickListener(new ContactAdapter.contactClickListener() {
            @Override
            public void contactClick(int position) {
                //pass the current position of the selected contact on the contact list view
                row_id = adapter.getID(position);
                contactDetails  = (detailsDisplay_Fragment) getSupportFragmentManager().findFragmentById(R.id.contactDetails_landscape);
                //if the layout if the MainActivity is in landscape mode
                if(contactDetails != null && contactDetails.isVisible()){
                    //set the action to the modify contact and delete floating action button
                    modify_floatingActionButton = findViewById(R.id.modify_floatingActionButton);
                    delete_floatingActionButton = findViewById(R.id.delete_floatingActionButton);
                    displayDetails(position);//display the details based on the selected contact on the list view
                    modifyANDdeleteContact(row_id);//modify or delete the contact based on the selected contact on the list view
                }else {
                    //Otherwise trigger the SingleContactPerson_activity class by passing the id of the contact
                    Intent intent = SingleContactPerson_Activity.newIntent(getApplication(), row_id);
                    startActivity(intent);//start the activity
                }
            }
        });
        // touch helper instance to delete the item by swiping to left or to right
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //delete the contact with confirmation dialog
                deleteSwipedContact((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);
        Log.d(TAG, "onCreate Method Called");
    }//End of onCreate
    /**
     * Function to display the details of the selected contact
     * @param position This is the position of the selected contact on the contact list view
     */
    public void  displayDetails(int position) {
        row_id = adapter.getID(position);//pass the current position of the selected contact on the contact list view
        Cursor cursor =  contactDatabase.getContactDetails(row_id);//initialise the Cursor object with the details of selected contact
        String full_name, full_address, telephone_number;//declare the variables
        //if the row of the data exist in database
        if(cursor.getCount() == 1){
            cursor.moveToFirst();
            //Retrieve the details of the contact person from cursor
            // such as family name, first name, house number, street name, town, country, postcode, telephone number, and image file name
            String family_name = cursor.getString(cursor.getColumnIndex(contactDatabase.getFieldFamilyName()));
            String first_name = cursor.getString(cursor.getColumnIndex(contactDatabase.getFieldFirstName()));
            int house_number = cursor.getInt(cursor.getColumnIndex(contactDatabase.getFieldHouseNumber()));
            String street = cursor.getString(cursor.getColumnIndex(contactDatabase.getFieldStreet()));
            String town = cursor.getString(cursor.getColumnIndex(contactDatabase.getFieldTown()));
            String country = cursor.getString(cursor.getColumnIndex(contactDatabase.getFieldCountry()));
            String postcode = cursor.getString(cursor.getColumnIndex(contactDatabase.getFieldPostcode()));
            telephone_number = cursor.getString(cursor.getColumnIndex(contactDatabase.getFieldTelephoneNumber()));
            String image_file_name = cursor.getString(cursor.getColumnIndex(contactDatabase.getFieldNameCard()));
            //set the format of the name to be displayed
            full_name = family_name +" "+ first_name;
            //set the format of the address to be displayed
            full_address = house_number + ", " + street + ", " + postcode + ", " + town + ", " + country;
            //set the reference to extract the layout to be displayed in landscape resolution
            contactDetails  = (detailsDisplay_Fragment) getSupportFragmentManager().findFragmentById(R.id.contactDetails_landscape);
            //pass the name and the address data to the layout
            contactDetails.updateContactDetailsView(full_name, full_address, telephone_number, image_file_name);
        }
    }//end of displayDetails method

    /**
     * Function to display the confirmation message when user swaps to delete the contact person
     * @param swipe_id The row id of the contact person as in database
     */
    public void deleteSwipedContact(final Long swipe_id){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //set the title and the message of the alert dialog block
        builder.setTitle("Delete Contact.");
        builder.setMessage("Are you sure you want to delete this contact?");
        //set the info message and the action if user decide to delete the record
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //delete the swapped item from ArrayList
                adapter.removeID(swipe_id);
                //delete the swapped item from Database
                contactDatabase.deleteRecord(swipe_id);
                //update cursor
                adapter.swapCursor(contactDatabase.getContactList());
                //display the message to inform user that the contact person has been deleted
                Toast.makeText(getApplicationContext(), "The content is deleted", Toast.LENGTH_SHORT).show();
            }
        });
        //set the info message and the action if user reject to delete the record
        builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //update cursor
                adapter.swapCursor(contactDatabase.getContactList());
                //display the message to inform user that the delete action has been denied
                Toast.makeText(getApplicationContext(), "You reject to delete the record", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = builder.create(); // create the dialog block
        alertDialog.show();//display the dialog
    }//end of deleteSwappedContact method
    /**
     * Function to modify and delete the selected contact
     * @param id This is the id of the row of the selected contact in database
     */
    public void modifyANDdeleteContact(long id){
        final long selectedID = id;//declare the variable
        delete_floatingActionButton.setOnClickListener(this);//register the click event
        //if user click on modify the existing contact floating action button
        modify_floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pass the id of the row to CreateNewContact_Activity class
                Intent intent = CreateNewContact_Activity.newIntent(getApplication(), selectedID);
                startActivity(intent);//start the activity
            }
        });
    }//end of modifyANDdeleteContact method
    @Override
    public void onClick(View view) {
        //if user click on create new contact floating action button
        if(view.getId() == R.id.createNewContact_floatingActionButton){
            // move to the create new contact activity
            Intent intent = CreateNewContact_Activity.newIntent(getApplicationContext(), 0);
            startActivity(intent);
        }
        //if user click on delete contact floating action button
        if(view.getId() == R.id.delete_floatingActionButton){
            AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(this);
            //set the title and the message of the alert dialog block
            builder.setTitle("Delete Contact.");
            builder.setMessage("Are you sure you want to delete this contact?");
            //set the info message and the action if user decide to delete the record
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            contactDatabase.deleteRecord(row_id);//passing the id of the row to database
                            //display the message to inform user that the contact person has been deleted
                            Toast.makeText(getApplicationContext(), "The content is deleted", Toast.LENGTH_SHORT).show();
                            //return to the contact list view layout in MainActivity
                            Intent intent = MainActivity.newIntent(getApplication());
                            startActivity(intent);//start the activity
                        }
            });
            //set the info message and the action if user reject to delete the record
            builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //display the message to inform user that the delete action has been denied
                            Toast.makeText(getApplicationContext(), "You reject to delete the record", Toast.LENGTH_SHORT).show();
                        }
            });
            AlertDialog alertDialog = builder.create(); // create the dialog block
            alertDialog.show();//display the dialog
        }
    }//end of onClick

    @Override
    protected void onStart(){
        super.onStart();//super class constructor
        Log.d(TAG, "onStart Method Called");
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG, "onResume Method Called");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.d(TAG, "onPause Method Called");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.d(TAG, "onStop Method Called");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.d(TAG, "onDestroy Method Called");
    }


}//end of MainActivity