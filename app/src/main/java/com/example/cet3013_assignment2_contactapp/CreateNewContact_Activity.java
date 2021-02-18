package com.example.cet3013_assignment2_contactapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.io.File;
import java.util.List;
import java.util.UUID;

public class CreateNewContact_Activity extends AppCompatActivity implements View.OnClickListener {
    private Button saveButton;
    private FloatingActionButton backButton, takePhotoButton;
    private EditText familyName_input, firstName_input, houseNumber_input, street_input, town_input, country_input, postcode_input, telephoneNumber_input;
    private ImageView profilePictureImage;
    private String text_familyName, text_firstName, text_houseNumber, text_street, text_town, text_country, text_postcode, text_telephoneNumber;
    private String image_file_name="R.drawable.person_icon";
    private static long row_id, id;
    private int houseNumber;
    private UUID uuid;
    private File photoFile;
    private Intent captureImageIntent;
    private ImageViewModel viewModel = null;
    private boolean isEditingContact = false;
    private static final int CAPTURE_PHOTO = 1;
    ContactPerson contactperson;

    /**
     * Function to trigger the intent of the activity of CreateNewContact_Activity
     * @param packageName The object of the Context that is the current state of the application
     * @param id The row id of the database
     * @return intent This return the object of the Intent
     */
    public static Intent newIntent(Context packageName, long id) {
        Intent intent = new Intent(packageName, CreateNewContact_Activity.class);
        //wrap the answer inside the intent, so that we can refer the answer later
        intent.putExtra("Contact_Person",id);
        row_id = id;
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_contact);
        //To set up the linking to the view model
        viewModel = new ViewModelProvider(this).get(ImageViewModel.class);
        //To set up the linking of the viewModel with LoveData
        final LiveData<String> live_image_file_name = viewModel.getCurrentFileName();
        //set the reference to the text view
        familyName_input = findViewById(R.id.familyName_input);
        firstName_input = findViewById(R.id.firstName_input);
        houseNumber_input = findViewById(R.id.houseNumber_input);
        street_input = findViewById(R.id.street_input);
        town_input = findViewById(R.id.town_input);
        country_input = findViewById(R.id.country_input);
        postcode_input = findViewById(R.id.postcode_input);
        telephoneNumber_input = findViewById(R.id.telephoneNumber_input);
        //set the reference to the button
        saveButton = findViewById(R.id.saveButton);
        backButton = findViewById(R.id.cancelButton);
        takePhotoButton = findViewById(R.id.takePhoto);
        //set the reference to the image view
        profilePictureImage = findViewById(R.id.profilePictureImage);
        //create the instance to the id
        id = getIntent().getLongExtra("Contact_Person", row_id);
        //display the details of the contact person if the id is more than zero
        if(id > 0) DisplayExitingContact(id);
        //create the instance of the file object
        photoFile = getPhotoFile();
        //create the camera services
        captureImageIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //register the click event
        saveButton.setOnClickListener(this);
        backButton.setOnClickListener(this);
        takePhotoButton.setOnClickListener(this);
        //Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        live_image_file_name.observe(this, new Observer<String>() {
            @Override
            public void onChanged(String imageFileName) {
                //add the image to the view only when the photo is taken
                if(viewModel.photoIsTake()) addPhotoToView(imageFileName);
            }
        });
    }
    /**
     * Function to save the new contact person
     * @return true/false This will return true if the entered details is valid to be saved otherwise return false
     */
    public boolean validateNewContact(){
        //retrieve the details from the text view respectively
        //details include family name, first name, house number, street, town, country, postcode, and telephone number
        text_familyName = familyName_input.getText().toString();
        text_firstName = firstName_input.getText().toString();
        text_houseNumber = houseNumber_input.getText().toString();
        text_street = street_input.getText().toString();
        text_town = town_input.getText().toString();
        text_country = country_input.getText().toString();
        text_postcode = postcode_input.getText().toString();
        text_telephoneNumber = telephoneNumber_input.getText().toString();
        //pass the family name, first name, street, town, and country for validation checking
        boolean validInputName = validateNameStreetTownCountry(text_familyName, text_firstName, text_street, text_town,text_country);
        //pass the house number for validation checking
        boolean validInputHouseNumber = validateHouseNumber(text_houseNumber);
        //pass the postcode for validation checking
        boolean validInputPostcode = validatePostcode(text_postcode);
        //pass the telephone number for validation checking
        boolean validInputTelephoneNumber = validateTelephoneNumber(text_telephoneNumber);
        //return true if all information are valid otherwise return false
        if(validInputName && validInputHouseNumber && validInputPostcode && validInputTelephoneNumber) return true;
        else return false;
    }//end of saveNewContact
    /**
     * Function to validate the information such as family name, first name, street, town, and country
     * @param familyName The family name of the contact person
     * @param firstName The first name of the contact person
     * @param street The residence street of the contact person
     * @param town The residence town of the contact person
     * @param country The residence country of the contact person
     * @return isValid This will return true if the entered details is valid to be saved otherwise return false
     */
    public boolean validateNameStreetTownCountry(String familyName, String firstName, String street, String town, String country){
        boolean isValid = false;//declare the variable boolean
        AlertDialog.Builder builder = new AlertDialog.Builder(this);//initialise the AlertDialog.Builder
        //return false if the character length for family name or first name or street or town or country exceed 25
        if(familyName.length() > 25 || firstName.length() > 25 || street.length() > 25 || town.length() > 25 || country.length() > 25) {
            //set the title and teh message for the alert dialog
            builder.setTitle("Text too long");
            builder.setMessage("The details such as family name, first name, street, town, country cannot be more than 25 character");
            //set the info message after user click on the button
            builder.setPositiveButton("Alright, I got it!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "Please continue the process", Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            return false;
        }
        //return false and set the error message on the text field if family name is empty
        if(familyName.isEmpty()){
            familyName_input.setError("Cannot be empty");
            familyName_input.requestFocus();
            return false;
        }
        //return false and set the error message on the text field if first name is empty
        if(firstName.isEmpty()){
            firstName_input.setError("Cannot be empty");
            firstName_input.requestFocus();
            return false;
        }
        //return false and set the error message on the text field if street is empty
        if(street.isEmpty()){
            street_input.setError("Cannot be empty");
            street_input.requestFocus();
            return false;
        }
        //return false and set the error message on the text field if town is empty
        if(town.isEmpty()){
            town_input.setError("Cannot be empty");
            town_input.requestFocus();
            return false;
        }
        //return false and set the error message on the text field if country is empty
        if(country.isEmpty()){
            country_input.setError("Cannot be empty");
            country_input.requestFocus();
            return false;
        }
        //pass the first name details to check for string validation
        for(int i = 0; i < familyName.length(); i++) {
            char character = familyName.charAt(i);
            //set isVaild to true if only alphabet is detected otherwise return false
            if ((character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z') || character == ' ') isValid = true;
            else {
                familyName_input.setError("Invalid family name! Can only be character");
                familyName_input.requestFocus();
                return false;
            }
        }
        //pass the last name details to check for string validation
        for(int i = 0; i < firstName.length(); i++) {
            char character = firstName.charAt(i);
            //set isVaild to true if only alphabet is detected otherwise return false
            if ((character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z') || character == ' ') isValid = true;
            else {
                firstName_input.setError("Invalid first name! Can only be character");
                firstName_input.requestFocus();
                return false;
            }
        }
        //pass the street details to check for string validation
        for(int i = 0; i < street.length(); i++) {
            char character = street.charAt(i);
            //set isVaild to true if only alphabet is detected otherwise return false
            if ((character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z') || character == ' ') isValid = true;
            else {
                street_input.setError("Invalid street name! Can only be character");
                street_input.requestFocus();
                return false;
            }
        }
        //pass the town details to check for string validation
        for(int i = 0; i < town.length(); i++) {
            char character = town.charAt(i);
            //set isVaild to true if only alphabet is detected otherwise return false
            if ((character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z') || character == ' ') isValid = true;
            else {
                town_input.setError("Invalid town name! Can only be character");
                town_input.requestFocus();
                return false;
            }
        }
        //pass the country details to check for string validation
        for(int i = 0; i < country.length(); i++) {
            char character = country.charAt(i);
            //set isVaild to true if only alphabet is detected otherwise return false
            if ((character >= 'a' && character <= 'z') || (character >= 'A' && character <= 'Z') || character == ' ') isValid = true;
            else {
                country_input.setError("Invalid country name! Can only be character");
                country_input.requestFocus();
                return false;
            }
        }
        return isValid;
    }//end of validateNameStreetTownCountry method
    /**
     * Function to validate the house number details
     * @param text_houseNumber The residence house number of the contact person
     * @return true/false This will return true if the entered details is valid to be saved otherwise return false
     */
    public boolean validateHouseNumber(String text_houseNumber){
        try{
            //return false and set the error message on the text field if house number is empty
            if(text_houseNumber.isEmpty()){
                houseNumber_input.setError("Cannot be empty");
                houseNumber_input.requestFocus();
                return false;
            }
            int houseNumber = Integer.parseInt(text_houseNumber);
            //return false and set the error message on the text field if house number is smaller than 1
            if (houseNumber < 1){
                houseNumber_input.setError("The house number must be greater or equal to 1");
                houseNumber_input.requestFocus();
                return false;
            }
        }catch(Exception error){
            houseNumber_input.setError("The house number can only be numerical value");
            houseNumber_input.requestFocus();
            return false;
        }
        return true;
    }//end of validateHouseNumber method
    /**
     * Function to validate the postcode details
     * @param postcode The residence postcode of teh contact person
     * @return isValid This will return true if the entered details is valid to be saved otherwise return false
     */
    public boolean validatePostcode(String postcode){
        boolean isValid = true;
        //return false and set the error message on the text field if postcode is empty
        if(postcode.isEmpty()){
            postcode_input.setError("Postcode cannot be empty!");
            postcode_input.requestFocus();
            return false;
        }
        //return false and set the error message on the text field if length of postcode is invalid
        if(postcode.length() != 7){
            postcode_input.setError("The postcode must be formatted as: two letters, one digit, a space, two digits, one letter!");
            postcode_input.requestFocus();
            return false;
        }
        //set isVaild to true if only alphabet is detected otherwise return false
        for(int i = 0; i < postcode.length(); i++){
            if(i != 2 && i != 3 && i != 4 && i != 5){
                if((postcode.charAt(i) >='a' && postcode.charAt(i) <= 'z') || (postcode.charAt(i) >='A' && postcode.charAt(i) <= 'Z')) isValid = true;
                else{
                    postcode_input.setError("The postcode must be formatted as: two letters, one digit, a space, two digits, one letter!");
                    postcode_input.requestFocus();
                    return false;
                }
            }
        }
        //set isVaild to true only if numerical alphabet is detected at third and fifth character and a space at fourth character otherwise return false
        if((postcode.charAt(2) >='0' && postcode.charAt(2) <='9') && (postcode.charAt(4) >='0' && postcode.charAt(4) <='9')
                && (postcode.charAt(5) >='0' && postcode.charAt(5) <='9')){
            if(postcode.charAt(3) == ' ') isValid = true;
            else isValid = false;
        }else isValid = false;
        //set the error message at text view if isValid is false
        if(!isValid){
            postcode_input.setError("The postcode must be formatted as: two letters, one digit, a space, two digits, one letter");
            postcode_input.requestFocus();
        }
        return isValid;
    }//end of validatePostcode method
    /**
     * Function to validate the telephone number
     * @param telephoneNumber The telephone number of the contact person
     * @return isValid This will return true if the entered details is valid to be saved otherwise return false
     */
    public boolean validateTelephoneNumber(String telephoneNumber){
        boolean isvalid = false;//declare the boolean variable
        //return false and set the error message on the text field if postcode is empty
        if(telephoneNumber.isEmpty()){
            telephoneNumber_input.setError("Cannot be empty!");
            telephoneNumber_input.requestFocus();
            return false;
        }
        if(telephoneNumber.length() < 5)//if length of telephone number is not valid
        {
            telephoneNumber_input.setError("The length of telephone number is not valid!");
            telephoneNumber_input.requestFocus();
            return false;
        }
        for(int i = 0; i < telephoneNumber.length();i++)
        {
            char character = telephoneNumber.charAt(i);
            //if telephone number start with number
            if(telephoneNumber.charAt(0) >='0' && telephoneNumber.charAt(0) <='9') isvalid = true;
            else{
                telephoneNumber_input.setError("Invalid phone number! Must start with a number.");
                telephoneNumber_input.requestFocus();
                return false;//set to true if the input is not numerical number
            }
            //if the telephone number is valid
            if((character >='0' && character <='9') || character == ' ') isvalid = true;//set to true
            else {
                telephoneNumber_input.setError("Invalid phone number!");
                telephoneNumber_input.requestFocus();
                return false;//set to true if the input is not numerical number
            }
        }
        return isvalid;
    }//end of validateTelephoneNumber method
    /**
     * Function to save the record to the database
     * @return true/false This will return true if the record is saved successfully otherwise return false
     */
    public boolean saveRecordToDatabase(){
        //save the record if all information are valid and return true otherwise return false
        if(validateNewContact()){
            houseNumber = Integer.parseInt(text_houseNumber);
            //set image file name as default person icon or keeping the previous image file name if no new nor any photo is take
            if(!viewModel.photoIsTake()){
                //keeping the previous image file name if not image is take before otherwise set to default
                if(!image_file_name.equals("R.drawable.person_icon")){
                    //pass the parameter to ContactPerson class
                    contactperson = new ContactPerson(text_familyName, text_firstName, houseNumber, text_street, text_town,
                                                      text_country, text_postcode, text_telephoneNumber, image_file_name);
                }else {
                    //pass the parameter to ContactPerson class
                    contactperson = new ContactPerson(text_familyName, text_firstName, houseNumber, text_street, text_town,
                                                      text_country, text_postcode, text_telephoneNumber,"R.drawable.person_icon");
                }
            }else{
                //pass the parameter to ContactPerson class
                contactperson = new ContactPerson(text_familyName, text_firstName, houseNumber, text_street, text_town,
                                                  text_country, text_postcode, text_telephoneNumber,viewModel.getImageFileName());
            }
            ContactDatabase database = new ContactDatabase(this);//initialise the ContactDatabase class
            if(id > 0) database.saveRecord(contactperson, true, id);//save the new contact
            else database.saveRecord(contactperson, false, id);//update the existing contact
            Intent intent = MainActivity.newIntent(getApplicationContext());//initialize the intent of MainActivity
            startActivity(intent);//return back to MainActivity
            return true;
        }else return false;
    }//end of saveRecordToDatabase method
    /**
     * Function to trigger the saveRecordToDatabase function and raise exception error if any exception is detected
     */
    public void saveRecord(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        try{
            saveRecordToDatabase();
        }catch(Exception error){
            builder.setTitle("Invalid Information!");
            builder.setMessage("The details you entered may contain error or is empty. Please verify before saving!");
        }
        AlertDialog alertDialog = builder.create(); // create the dialog block
        alertDialog.show();//display the dialog
    }//end of saveRecord method
    /**
     * Function to display the details of the existing contact person to the text view
     * @param id This is the row id of the database
     */
    public void DisplayExitingContact(long id) {
        //initialize the ContactDatabase
        ContactDatabase contactDatabase = new ContactDatabase(this);
        //initialize the cursor with the row data which retrieved based on row id
        Cursor cursor = contactDatabase.getContactDetails(id);
        //if cursor is not empty
        if (cursor.getCount() == 1) {
            cursor.moveToFirst();
            //Retrieve the details of the contact person from cursor
            // such as family name, first name, house number, street name, town, country, postcode, telephone number, and image file name
            text_familyName = cursor.getString(cursor.getColumnIndex(contactDatabase.getFieldFamilyName()));
            text_firstName = cursor.getString(cursor.getColumnIndex(contactDatabase.getFieldFirstName()));
            houseNumber = cursor.getInt(cursor.getColumnIndex(contactDatabase.getFieldHouseNumber()));
            text_street = cursor.getString(cursor.getColumnIndex(contactDatabase.getFieldStreet()));
            text_town = cursor.getString(cursor.getColumnIndex(contactDatabase.getFieldTown()));
            text_country = cursor.getString(cursor.getColumnIndex(contactDatabase.getFieldCountry()));
            text_postcode = cursor.getString(cursor.getColumnIndex(contactDatabase.getFieldPostcode()));
            text_telephoneNumber = cursor.getString(cursor.getColumnIndex(contactDatabase.getFieldTelephoneNumber()));
            image_file_name = cursor.getString(cursor.getColumnIndex(contactDatabase.getFieldNameCard()));
        }
        //set the details to th name text
        //such as family name, first name, house number, street name, town, country, postcode, telephone number, and image file name
        familyName_input.setText(text_familyName);
        firstName_input.setText(text_firstName) ;
        houseNumber_input.setText(Integer.toString(houseNumber));
        street_input.setText(text_street) ;
        town_input.setText(text_town) ;
        country_input.setText(text_country) ;
        postcode_input.setText(text_postcode);
        telephoneNumber_input.setText(text_telephoneNumber);
        //set the image view to person icon is the image file name is default otherwise set the image based on photo taken
       if(image_file_name.equals("R.drawable.person_icon")){
            profilePictureImage.setImageResource(R.drawable.person_icon);
        }else{
            Bitmap bitmap = PictureUtils.getScaledBitmap(image_file_name, this);
            profilePictureImage.setImageBitmap(bitmap);
        }
        isEditingContact = true;
    }//end of DisplayExitingContact method

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAPTURE_PHOTO){
            if(resultCode == RESULT_OK) {
                viewModel.setPhotoIsTake(true);
                //retrieve back the file from file system
                Uri uri = FileProvider.getUriForFile(this, "com.example.cet3013_assignment2_contactapp.fileprovider", photoFile);
                revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                updatePhotoView();
            }
        }
    }//end of onActivityResult method
    /**
     * Function to get file name and file location
     * @return fileName This will return the file name of the image
     */
    public String getPhotoFileName(){
        String fileName = "";
        uuid = UUID.randomUUID();

        fileName = "IMG_" + uuid.toString() +".jpg";
        Log.d("FILE", fileName);
        return fileName;
    }//end of getPhotoFileName()
    /**
     * Function to get the file path
     * @return File This will return the file path of the image
     */
    public File getPhotoFile(){
        File fileDir = getFilesDir();
        return new File(fileDir, getPhotoFileName());
    }//end of getPhotoFile method
    /**
     * Function to update the photo view
     */
    private void updatePhotoView(){
        if(photoFile == null || !photoFile.exists()){
            profilePictureImage.setImageDrawable(null);
        }
       else{
           viewModel.setImage_file_name(photoFile.getPath());
       }
    }//end of updatePhotoView method
    /**
     * Function to add the photo to the view
     * @param image_file_name The file name of the image
     */
    private void addPhotoToView(String image_file_name){
        Bitmap bitmap = PictureUtils.getScaledBitmap(image_file_name, this);
        profilePictureImage.setImageBitmap(bitmap);
    }//end of addPhotoToView method
    @Override
    public void onClick(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //if take photo button is clicked
        if(view.getId() == R.id.takePhoto){
            //set teh instance of teh file object in viewModel
            viewModel.setImage_file_name(photoFile.getPath());
            //start package manager to verify the camera service
            PackageManager pm = getPackageManager();
            //Use the resolveActivity to check the camera instance by passing package manager
            boolean canTakePhoto = photoFile != null && captureImageIntent.resolveActivity(pm) != null;
            //set the button click ability
            takePhotoButton.setEnabled(canTakePhoto);
            Uri uri = FileProvider.getUriForFile(this, "com.example.cet3013_assignment2_contactapp.fileprovider", photoFile);
            //check the file location
            Log.d("FILE-URI", uri.toString());
            //start launch the camera service with file path
            captureImageIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            //check the return value from captureImageIntent
            List<ResolveInfo> cameraActivities= getPackageManager().queryIntentActivities(captureImageIntent, PackageManager.MATCH_DEFAULT_ONLY);
            //solve each activity
            for(ResolveInfo activity : cameraActivities) {
                grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                // start the camera services
                startActivityForResult(captureImageIntent, CAPTURE_PHOTO);
            }
        }
        //if save button is clicked
        if(view.getId() == R.id.saveButton){
            //trigger the confirmation message if is editing the existing contact otherwise save directly
            if(isEditingContact){
                //set the title and the message of the alert dialog block
                builder.setTitle("Change the contact");
                builder.setMessage("Are you sure you want to change the contact?");
                //set the info message and the action if user decide to save the record
                builder.setPositiveButton("Save Change", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveRecord();
                    }
                });
                //set the info message and the action if user reject to save the record
                builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "You reject to change the content.", Toast.LENGTH_SHORT).show();
                    }
                });
            }else{
                saveRecord();//save the new contact person
            }
            AlertDialog alertDialog = builder.create(); // create the dialog block
            alertDialog.show();//display the dialog
        }
        //if cancel button is clicked
        if(view.getId() == R.id.cancelButton){
            //set the title and the message of the alert dialog block
            builder.setTitle("Back to previous page.");
            builder.setMessage("Are you sure you want to leave without saving the contact?");
            //set the info message and the action if user decide to save the record
            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(saveRecordToDatabase()) Toast.makeText(getApplicationContext(), "The new content is save", Toast.LENGTH_SHORT).show();
                }
            });
            //set the info message and the action if user reject to save the record
            builder.setNegativeButton("Discard", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getApplicationContext(), "The new content is cancelled", Toast.LENGTH_SHORT).show();
                    // move to the create new contact activity
                    Intent intent = MainActivity.newIntent(getApplicationContext());
                    startActivity(intent);
                }
            });
            AlertDialog alertDialog = builder.create(); // create the dialog block
            alertDialog.show();//display the dialog
        }
    }//end of onClick method
}//end of CreateNewContact_Activity class
