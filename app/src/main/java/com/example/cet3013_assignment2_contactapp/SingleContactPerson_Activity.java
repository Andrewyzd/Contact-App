package com.example.cet3013_assignment2_contactapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SingleContactPerson_Activity extends AppCompatActivity implements View.OnClickListener {
    public static final String TAG = "SingleContact_Activity";
    //Declare the variable
    private TextView name_text, phoneNumber_text, address_text;
    private ImageView profile_image;
    private String family_name, first_name, street, town, country, postcode, telephone_number, full_address, full_name, image_file_name;
    private FloatingActionButton back_floatingActionButton, modify_floatingActionButton, delete_floatingActionButton;
    private static long row_id, id;
    private int house_number;
    //declare and initialize the obejct of the classes
    ContactDatabase contactDatabase = new ContactDatabase(this);
    Cursor cursor;
    detailsDisplay_Fragment contactDetails;
    /**
     * Function to trigger the intent of the activity of SingleContactPerson_activity
     * @param packageName The object of the Context that is the current state of the application
     * @param id This is the row id of the database
     * @return intent This return the object of the Intent
     */
    public static Intent newIntent(Context packageName, long id) {
        Intent intent = new Intent(packageName, SingleContactPerson_Activity.class);
        //wrap the answer inside the intent, so that we can refer the answer later
        intent.putExtra("Contact_Person", id);
        row_id = id;
        return intent;
    }//end of newIntent method

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_contact_person_activity);
        //set the reference to the text
        name_text = findViewById(R.id.name_text);
        phoneNumber_text = findViewById(R.id.phoneNumber_text);
        address_text = findViewById(R.id.address_text);
        //set the reference to the floating action button
        back_floatingActionButton = findViewById(R.id.back_floatingActionButton);
        modify_floatingActionButton = findViewById(R.id.modify_floatingActionButton);
        delete_floatingActionButton = findViewById(R.id.delete_floatingActionButton);
        //set the reference to the image view
        profile_image = findViewById(R.id.profile_image);
        //create the instance to the id
        id = getIntent().getLongExtra("Contact_Person", row_id);
        //assign the row data to cursor based on row id
        cursor = contactDatabase.getContactDetails(id);
        //display the details of the contact person
        displayDetails(cursor);
        //register the click event
        back_floatingActionButton.setOnClickListener(this);
        modify_floatingActionButton.setOnClickListener(this);
        delete_floatingActionButton.setOnClickListener(this);

        Log.d(TAG, "onCreate Method Called");

    }
    /**
     * Function to display the details of a single contact person
     * @param cursor This is the object of the cursor
     */
    public void displayDetails(Cursor cursor){
        if(cursor.getCount() == 1){
            cursor.moveToFirst();
            //Retrieve the details of the contact person from cursor
            // such as family name, first name, house number, street name, town, country, postcode, telephone number, and image file name
            family_name = cursor.getString(cursor.getColumnIndex(contactDatabase.getFieldFamilyName()));
            first_name = cursor.getString(cursor.getColumnIndex(contactDatabase.getFieldFirstName()));
            house_number = cursor.getInt(cursor.getColumnIndex(contactDatabase.getFieldHouseNumber()));
            street = cursor.getString(cursor.getColumnIndex(contactDatabase.getFieldStreet()));
            town = cursor.getString(cursor.getColumnIndex(contactDatabase.getFieldTown()));
            country = cursor.getString(cursor.getColumnIndex(contactDatabase.getFieldCountry()));
            postcode = cursor.getString(cursor.getColumnIndex(contactDatabase.getFieldPostcode()));
            telephone_number = cursor.getString(cursor.getColumnIndex(contactDatabase.getFieldTelephoneNumber()));
            image_file_name = cursor.getString(cursor.getColumnIndex(contactDatabase.getFieldNameCard()));
        }
        //Set the format of the data to be displayed
        full_name = family_name +" "+ first_name;
        full_address = house_number + ", " + street + ", " + postcode + ", " + town + ", " + country;
        //Set the formatted data to the textview
        name_text.setText(full_name);
        address_text.setText(full_address);
        phoneNumber_text.setText(telephone_number);
        //set the profile image
        if(image_file_name.equals("R.drawable.person_icon")){
            profile_image.setImageResource(R.drawable.person_icon);
        }else{
            Bitmap bitmap = PictureUtils.getScaledBitmap(image_file_name, 1000, 1000);
            profile_image.setImageBitmap(bitmap);
        }
    }//end of displayDetails method

    @Override
    public void onClick(View view) {
        //if user click on delete contact floating action button
        if(view.getId() == R.id.delete_floatingActionButton){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //set the title and the message of the alert dialog block
            builder.setTitle("Delete Contact.");
            builder.setMessage("Are you sure you want to delete this contact?");
            //set the info message and the action if user decide to delete the record
            builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    contactDatabase.deleteRecord(id);
                    Toast.makeText(getApplicationContext(), "The content is deleted", Toast.LENGTH_SHORT).show();
                    Intent intent = MainActivity.newIntent(getApplication());
                    startActivity(intent);
                }
            });
            //set the info message and the action if user reject to delete the record
            builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "You reject to delete the record", Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog alertDialog = builder.create(); // create the dialog block
            alertDialog.show();//display the dialog
        }
        //if user click on modify the existing contact floating action button
        if(view.getId() == R.id.modify_floatingActionButton){
            //Create the instance of the intent and pass the id of the row to CreateNewContact_Activity class
            Intent intent = CreateNewContact_Activity.newIntent(getApplication(), id);
            startActivity(intent);//start the activity
        }
        //if user click on back to previous floating action button
        if(view.getId() == R.id.back_floatingActionButton){
            //Create the instance of the intent for MainActivity class
            Intent intent = MainActivity.newIntent(getApplication());
            startActivity(intent);//start the activity
        }
    }//end of onClick method

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
}//end of SingleContactPerson_activity
