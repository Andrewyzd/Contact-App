package com.example.cet3013_assignment2_contactapp;

import java.io.File;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.MutableLiveData;

public class ImageViewModel extends ViewModel {
    private String image_file_name;
    private boolean photoIsTake;
    private File file_name;
    private MutableLiveData<String> live_image_file_name = new MutableLiveData<>();

    /**
     * Constructor with parameter
     * overloaded constructor to initialize the data
     * @param image_file_name The image file name of the contact person
     */
    public void setImage_file_name(String image_file_name){
        this.image_file_name = image_file_name;
        live_image_file_name.setValue(this.getImageFileName());
    }
    public void setFile_name(File file_name){
        this.file_name = file_name;
    }
    public File getFileName(){
        return this.file_name;
    }
    /**
     *setPhotoIsTake() - mutator method to change the boolean identifier to true or false
     * @param photoIsTake The identifier which is either true or false
     */
    public void setPhotoIsTake(boolean photoIsTake){
        this.photoIsTake = photoIsTake;
    }
    /**
     * getImageFileName() - accessor method to get the image file name of the contact person
     * @return image_file_name This return the image file name of the contact person
     */
    public String getImageFileName(){
        return this.image_file_name;
    }
    /**
     * photoIsTake() - accessor method to determine whether the photo is taken or not
     * @return photoIsTake This return true or false to indicate whether the photo is take or not
     */
    public boolean photoIsTake(){
        return this.photoIsTake;
    }
    /**
     *Function to get the object of LiveData
     *@return LiveData object This return the object of the LiveData
     */
    public MutableLiveData<String> getCurrentFileName(){
        return this.live_image_file_name;
    }
}
