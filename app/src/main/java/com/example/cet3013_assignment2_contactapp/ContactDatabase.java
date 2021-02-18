package com.example.cet3013_assignment2_contactapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContactDatabase extends SQLiteOpenHelper {
    //declare the table name
    private static final String DATABASE_NAME = "contact.db";
    private static final String TABLE_CONTACT = "Contact";
    //declare the column name
    private static final String FIELD_FAMILY_NAME = "Family_Name";
    private static final String FIELD_FIRST_NAME = "First_Name";
    private static final String FIELD_HOUSE_NUMBER = "House_Number";
    private static final String FIELD_STREET = "Street";
    private static final String FIELD_TOWN = "Town";
    private static final String FIELD_COUNTRY = "Country";
    private static final String FIELD_POSTCODE = "Postcode";
    private static final String FIELD_TELEPHONE_NUMBER = "Telephone_Number";
    private static final String FIELD_NAME_CARD = "Name_Card";

    private static final int DATABASE_VERSION = 1;

    /**
     * Constructor with parameter
     * overloaded constructor to initialize the data
     * @param context The object of the Context that is the current state of the application
     */
    ContactDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    /**
     * Function to create the table named contact
     * @param sqLiteDatabase This is the object of SQLiteDatabase
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //create a table named contact
        sqLiteDatabase.execSQL(" CREATE TABLE " + TABLE_CONTACT + "(_id integer PRIMARY KEY AUTOINCREMENT,"
                                + FIELD_FAMILY_NAME + " TEXT NOT NULL, "
                                + FIELD_FIRST_NAME + " TEXT NOT NULL, "
                                + FIELD_HOUSE_NUMBER + " INTEGER NOT NULL, "
                                + FIELD_STREET + " TEXT NOT NULL, "
                                + FIELD_TOWN + " TEXT NOT NULL, "
                                + FIELD_COUNTRY + " TEXT NOT NULL, "
                                + FIELD_POSTCODE + " TEXT, "
                                + FIELD_TELEPHONE_NUMBER + " TEXT, "
                                + FIELD_NAME_CARD + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    /**
     * Function to save the new or existing record
     * @param contactperson The object of the ContactPerson class
     * @param contactExist The boolean to determine whether the contact is existed or not
     * @param id The id of the row in the database
     */
    public void saveRecord(ContactPerson contactperson, boolean contactExist, long id){
        // get database instance
        SQLiteDatabase database = getWritableDatabase();
        // Use content to add the record
        ContentValues content = new ContentValues();
        //put the record into content such as
        // family name, first name, house number, street, town, country, postcode, telephone number and image file name
        content.put(FIELD_FAMILY_NAME, contactperson.getFamily_name());
        content.put(FIELD_FIRST_NAME, contactperson.getFirst_name());
        content.put(FIELD_HOUSE_NUMBER, contactperson.getHouse_number());
        content.put(FIELD_STREET, contactperson.getStreet());
        content.put(FIELD_TOWN, contactperson.getTown());
        content.put(FIELD_COUNTRY, contactperson.getCountry());
        content.put(FIELD_POSTCODE, contactperson.getPostcode());
        content.put(FIELD_TELEPHONE_NUMBER, contactperson.getTelephone_number());
        content.put(FIELD_NAME_CARD, contactperson.getName_card());
        //update the details of the particular row if the contact is existed. Otherwise add a new row
        if(contactExist) database.update(TABLE_CONTACT, content, "_id = ?", new String[] {String.valueOf(id)});
        else database.insertOrThrow(TABLE_CONTACT, null, content);
    }//end of saveRecord method
    /**
     * Function to delete the record
     * @param id The id of the row in the database
     */
    public void deleteRecord(long id){
        // get database instance
        SQLiteDatabase database = getWritableDatabase();
        //delete the record based on the selected id
        database.delete(TABLE_CONTACT, "_id = ?", new String[] {String.valueOf(id)});
    }//end of deleteRecord method
    /**
     * Function to return all the contacts under Cursor type
     * @return database.rawQuery This return all the selected record from the database
     */
    public Cursor getContactList(){
        SQLiteDatabase database = getReadableDatabase();
        String sql = " SELECT _id, " + FIELD_FAMILY_NAME + ", " + FIELD_FIRST_NAME +", " + FIELD_NAME_CARD
                + " FROM " + TABLE_CONTACT
                + " ORDER BY UPPER(" + FIELD_FAMILY_NAME + ") ASC ";
        return database.rawQuery(sql, null);
    }//end of getContactList method
    /**
     * Function to return the contact details by selected id
     * @param id The row id in the database
     * @return database.rawQuery This return all the selected record from the database
     */
    public Cursor getContactDetails(long id){
        SQLiteDatabase database = getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_CONTACT + " WHERE _id = ?";
        return database.rawQuery(sql, new String[] {String.valueOf(id)});
    }//end of getContactDetails method
    /**
     * getFieldFamilyName() - accessor method to get the column name for family name attribute
     * @return FIELD_FAMILY_NAME This will return the column name for family name attribute
     */
    public static String getFieldFamilyName() {
        return FIELD_FAMILY_NAME;
    }
    /**
     * getFieldFirstName() - accessor method to get the column name for first name attribute
     * @return FIELD_FIRST_NAME This will return the column name for first name attribute
     */
    public static String getFieldFirstName() {
        return FIELD_FIRST_NAME;
    }
    /**
     * getFieldHouseNumber() - accessor method to get the column name for house number attribute
     * @return FIELD_HOUSE_NUMBER This will return the column name for house number attribute
     */
    public static String getFieldHouseNumber() {
        return FIELD_HOUSE_NUMBER;
    }
    /**
     * getFieldStreet() - accessor method to get the column name for street attribute
     * @return FIELD_STREET This will return the column name for street attribute
     */
    public static String getFieldStreet() {
        return FIELD_STREET;
    }
    /**
     * getFieldTown() - accessor method to get the column name for town attribute
     * @return FIELD_TOWN This will return the column name for town attribute
     */
    public static String getFieldTown() {
        return FIELD_TOWN;
    }
    /**
     * getFieldCountry() - accessor method to get the column name for country attribute
     * @return FIELD_COUNTRY This will return the column name for country attribute
     */
    public static String getFieldCountry() {
        return FIELD_COUNTRY;
    }
    /**
     * getFieldPostcode() - accessor method to get the column name for postcode attribute
     * @return FIELD_POSTCODE This will return the column name for postcode attribute
     */
    public static String getFieldPostcode() {
        return FIELD_POSTCODE;
    }
    /**
     * getFieldTelephoneNumber() - accessor method to get the column name for telephone number attribute
     * @return FIELD_TELEPHONE_NUMBER This will return the column name for telephone number attribute
     */
    public static String getFieldTelephoneNumber() {
        return FIELD_TELEPHONE_NUMBER;
    }
    /**
     * getFieldNameCard() - accessor method to get the column name for image file name attribute
     * @return FIELD_NAME_CARD This will return the column name for image file name attribute
     */
    public static String getFieldNameCard() {
        return FIELD_NAME_CARD;
    }
}//end of ContactDatabase
