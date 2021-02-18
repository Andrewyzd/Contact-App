package com.example.cet3013_assignment2_contactapp;

public class ContactPerson {
    private String family_name;
    private String first_name;
    private int house_number;
    private String street;
    private String town;
    private String country;
    private String postcode;
    private String telephone_number;
    private String name_card;

    /**
     * Constructor with parameter
     * overloaded constructor to initialize the data
     * @param family_name The family name of the contact person
     * @param first_name The first name of the contact person
     * @param house_number The house number of the contact person
     * @param street The street where the contact person lives
     * @param town The town where the contact person lives
     * @param country The country where the contact person lives
     * @param postcode The postcode where the contact person lives
     * @param telephone_number The telephone number where the contact person lives
     */
    public ContactPerson(String family_name, String first_name, int house_number, String street, String town, String country, String postcode, String telephone_number, String name_card) {
        this.family_name = family_name;
        this.first_name = first_name;
        this.house_number = house_number;
        this.street = street;
        this.town = town;
        this.country = country;
        this.postcode = postcode;
        this.telephone_number = telephone_number;
        this.name_card = name_card;
    }
    /**
     *getFamily_name() - accessor method to get the family name of the contact person
     *@return family_name This return the family name of the contact person
     */
    public String getFamily_name() {
        return this.family_name;
    }
    /**
     *getFirst_name() - accessor method to get the first name of the contact person
     *@return first_name This return the first name of the contact person
     */
    public String getFirst_name() {
        return this.first_name;
    }
    /**
     *getHouse_number() - accessor method to get the residence house number of the contact person
     *@return house_number This return the residence house number of the contact person
     */
    public int getHouse_number() {
        return this.house_number;
    }
    /**
     *getStreet() - accessor method to get the residence street of the contact person
     *@return street This return the residence street of the contact person
     */
    public String getStreet() {
        return this.street;
    }
    /**
     *getTown() - accessor method to get the residence town of the contact person
     *@return town This return the residence town of the contact person
     */
    public String getTown() {
        return this.town;
    }
    /**
     *getCountry() - accessor method to get the residence country of the contact person
     *@return country This return the residence country of the contact person
     */
    public String getCountry() {
        return this.country;
    }
    /**
     *getPostcode() - accessor method to get the residence postcode of the contact person
     *@return postcode This return the residence postcode of the contact person
     */
    public String getPostcode() {
        return this.postcode;
    }
    /**
     *getTelephone_number() - accessor method to get the telephone number of the contact person
     *@return telephone_number This return the telephone number of the contact person
     */
    public String getTelephone_number() {
        return this.telephone_number;
    }
    /**
     *getName_card() - accessor method to get the image file name of the contact person
     *@return family_name This return the image file name of the contact person
     */
    public String getName_card() {
        return name_card;
    }
}//end of ContactPerson
