package com.example.cet3013_assignment2_contactapp;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class detailsDisplay_Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details_display, container, false);
        return view;
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
    /**
     * Function to update the view in the contactDetails_landscape layout
     * @param full_name The full name of the contact person
     * @param full_address The full address of the contact person
     * @param phone_number The phone number of the contact person
     * @param image_file_name The profile picture of the contact person
     */
    public void updateContactDetailsView(String full_name, String full_address, String phone_number, String image_file_name){
        //set the reference to the TextView and ImageView
        TextView full_name_text = getView().findViewById(R.id.name_text);
        TextView full_address_text = getView().findViewById(R.id.address_text);
        TextView phone_number_text = getView().findViewById(R.id.phoneNumber_text);
        ImageView profile_image = getView().findViewById(R.id.profile_image);
        //set the name, address and the phone number to the text view
        full_name_text.setText(full_name);
        full_address_text.setText(full_address);
        phone_number_text.setText(phone_number);
        //set the profile image to the image view
        if(image_file_name.equals("R.drawable.person_icon")){
            profile_image.setImageResource(R.drawable.person_icon);
        }else{
            Bitmap bitmap = PictureUtils.getScaledBitmap(image_file_name, 1000, 1000);
            profile_image.setImageBitmap(bitmap);
        }
    }//end of updateContactDetailsView method
}//end of detailsDisplay_Fragment