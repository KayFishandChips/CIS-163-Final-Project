package com.ajkayfishgmail.discount;

import java.util.ArrayList;

/**
 * Created by ajkay_000 on 3/27/2015.
 */
public class Business
{
    private float latitude;
    private float longitude;
    private String name;
    private String address;
    private String phoneNum;
    private String email;
    private ArrayList<String> categories;
    private int verifications;
    private String discountDesc;


    /**
     *CONSTRUCTORS
     */
    public Business(String pName, String pDesc, ArrayList<String> pCat, String pPhone){
        name = pName;
        discountDesc = pDesc;
        phoneNum = pPhone;
        verifications = 0;
        categories = new ArrayList<String>();
        for(String i : pCat){
            categories.add(i);
        }
    }

    public Business(String pName, String pDesc, String pEmail, ArrayList<String> pCat){
        name = pName;
        discountDesc = pDesc;
        email = pEmail;
        verifications = 0;
        categories = new ArrayList<String>();
        for(String i : pCat){
            categories.add(i);
        }
    }

    public Business(String pName, String pDesc, String pEmail, ArrayList<String> pCat, String pPhone){
        name = pName;
        discountDesc = pDesc;
        phoneNum = pPhone;
        email = pEmail;
        verifications = 0;
        categories = new ArrayList<String>();
        for(String i : pCat){
            categories.add(i);
        }
    }

    public Business(String pName, String pDesc, String pPhone, ArrayList<String> pCat, float pLat, float pLong){
        name = pName;
        discountDesc = pDesc;
        phoneNum = pPhone;
        verifications = 0;
        categories = new ArrayList<String>();
        for(String i : pCat){
            categories.add(i);
        }
        latitude = pLat;
        longitude = pLong;
    }

    public Business(String pName, String pDesc, ArrayList<String> pCat, String pAddress, String pPhone){
        name = pName;
        discountDesc = pDesc;
        phoneNum = pPhone;
        verifications = 0;
        categories = new ArrayList<String>();
        for(String i : pCat){
            categories.add(i);
        }
        address = pAddress;
    }

    public Business(String pName, String pDesc, String pPhone, ArrayList<String> pCat, float pLat, float pLong, String pAddress){
        name = pName;
        discountDesc = pDesc;
        phoneNum = pPhone;
        verifications = 0;
        categories = new ArrayList<String>();
        for(String i : pCat){
            categories.add(i);
        }
        latitude = pLat;
        longitude = pLong;
        address = pAddress;
    }

    /**
     *GETTERS
     */
    public String getName(){
        return name;
    }

    public String getAddress(){
        return address;
    }

    public String getPhoneNum(){
        return phoneNum;
    }

    public String getEmail(){
        return email;
    }

    public String getVerifications(){
        return String.valueOf(verifications);// or change to int;
    }

    public String getDescription(){
        return discountDesc;
    }

    /**
     *MUTATORS
     */
    public int verifyUp(){
        if(verifications < 20)
            verifications++;
        return verifications;
    }

    public int verifyDown(){
        if(verifications > 0)
            verifications--;
        return verifications;
    }
}
