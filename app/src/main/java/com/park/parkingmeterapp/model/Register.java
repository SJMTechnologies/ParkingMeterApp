package com.park.parkingmeterapp.model;

public class Register {

    public static final String TABLE_NAME = "park_register";
    public static final String KEY_ID = "_id";
    public static final String KEY_FIRST_NAME = "first_name";
    public static final String KEY_LAST_NAME = "last_name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_TELEPHONE = "telephone";
    public static final String KEY_ADDRESS_ONE = "address_one";
    public static final String KEY_ADDRESS_TWO = "address_two";
    public static final String KEY_CITY = "city";
    public static final String KEY_POSTAL_CODE = "postal_code";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_REGION_OR_STATE = "region_or_state";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_SUBSCRIBE = "subscribe";

    private String FirstName, LastName,Email,Telephone,
            AddressOne,City,PostalCode,Password,AddressTwo,
            ConfirmPassword,Country,RegionOrState,businessName,fax;
    private boolean isSubscribe;

    public boolean isSubscribe() {
        return isSubscribe;
    }

    public void setSubscribe(boolean subscribe) {
        isSubscribe = subscribe;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getAddressOne() {
        return AddressOne;
    }

    public void setAddressOne(String addressOne) {
        AddressOne = addressOne;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getPostalCode() {
        return PostalCode;
    }

    public void setPostalCode(String postalCode) {
        PostalCode = postalCode;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getConfirmPassword() {
        return ConfirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        ConfirmPassword = confirmPassword;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getRegionOrState() {
        return RegionOrState;
    }

    public void setRegionOrState(String regionOrState) {
        RegionOrState = regionOrState;
    }

    public String getAddressTwo() {
        return AddressTwo;
    }

    public void setAddressTwo(String strAddressTwo) {
        this.AddressTwo = strAddressTwo;
    }

    @Override
    public String toString() {
        return "Register{" +
                "FirstName='" + FirstName + '\'' +
                ", LastName='" + LastName + '\'' +
                ", Email='" + Email + '\'' +
                ", Telephone='" + Telephone + '\'' +
                ", AddressOne='" + AddressOne + '\'' +
                ", City='" + City + '\'' +
                ", PostalCode='" + PostalCode + '\'' +
                ", Password='" + Password + '\'' +
                ", AddressTwo='" + AddressTwo + '\'' +
                ", ConfirmPassword='" + ConfirmPassword + '\'' +
                ", Subscribe='" + isSubscribe + '\'' +
                ", Country='" + Country + '\'' +
                ", RegionOrState='" + RegionOrState + '\'' +
                ", businessName='" + businessName + '\'' +
                ", fax='" + fax + '\'' +
                '}';
    }
}
