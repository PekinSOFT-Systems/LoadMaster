/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pekinsoft.loadmaster.model;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.1.0
 * @since 0.1.0
 */
public class BrokerModel {
    //<editor-fold defaultstate="collapsed" desc="Public Static Constants">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Member Fields">
    private long id;            /** Unique broker identification number REQ'D*/
    private String company;     /** Company name for the broker         REQ'D*/
    private String street;      /** Street address for the broker       REQ'D*/
    private String suite;       /** Suite number for the broker      OPTIONAL*/
    private String city;        /** City for the broker                 REQ'D*/
    private String state;       /** State for the broker                REQ'D*/
    private String zip;         /** Zip Code for the broker             REQ'D*/
    private String contact;     /** Contact name for the broker      OPTIONAL*/
    private String phone;       /** Phone Number for the broker      OPTIONAL*/
    private String fax;         /** Fax Number for the broker          OPTIONAL*/
    private String email;       /** Email address for the broker       OPTIONAL*/
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Static Initializer">
    static {
        
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Intstance Initializer">
    {
        
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
    public BrokerModel () {
        id = System.currentTimeMillis();
        company = "";
        street = "";
        suite = "";
        city = "";
        state = "";
        zip = "";
        contact = "";
        phone = "";
        fax = "";
        email = "";
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Static Methods">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Instance Methods">
    /**
     * Retrieves the unique identification number for this broker.
     * @return long Unique identification number
     */
    public long getId() {
        return id;
    }
    
    /**
     * Sets the unique identification number for this broker. However, this
     * value should never be changed from what is created when the object is
     * created, this is necessary for creating an object from the data in the
     * table.
     * 
     * @param id Unique identification number
     */
    public void setId(long id) {
        this.id = id;
    }
    
    /**
     * Retrieves the name of the company for this broker.
     * @return String The company name
     */
    public String getCompany() {
        return company;
    }

    /**
     * Sets the company name for this broker.
     * 
     * <dl>
     *  <dt><strong><em>NOTE</em></strong></dt>
     *  <dd>The company name is required and will only store fifty (50) 
     *      characters of the name, if it is longer than 50.</dd>
     * </dl>
     * @param company The new company name
     */
    public void setCompany(String company) {
        if ( company.length() > 50 )
            this.company = company.substring(0, 49);
        else
            this.company = company;
    }

    /**
     * Retrieves the street address for the broker.
     * 
     * @return String The street address, i.e., 100 E. Main Street
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets the street address for this broker.
     * 
     * <dl>
     *  <dt><strong><em>NOTE</em></strong></dt>
     *  <dd>The street address is required and is limited to forty (40) 
     *      characters.</dd>
     * </dl>
     * 
     * @param street The street address, i.e., 100 E. Main Street
     */
    public void setStreet(String street) {
        if ( street.length() > 40 )
            this.street = street.substring(0, 39);
        else
            this.street = street;
    }

    /**
     * Retrieves the suite number for this broker.
     * 
     * <dl>
     *  <dt><strong><em>NOTE</em></strong></dt>
     *  <dd>This method may return `null` if no suite number is stored in the
     *      database.</dd>
     * </dl>
     * 
     * @return String The suite number for this broker, or `null` if none.
     */
    public String getSuite() {
        return (suite == null) ? "" : suite;
    }

    /**
     * Sets the suite number for this broker.
     * 
     * <dl>
     *  <dt><strong><em>NOTE</em></strong></dt>
     *  <dd>The suite number is <strong>not</strong> required and may store a 
     *      `null` value. The maximum length of this field is fifteen (15) 
     *      characters.</dd>
     * </dl>
     * 
     * @param suite The suite number, if any.
     */
    public void setSuite(String suite) {
        // For parsing from file, there needs to be some data in the suite
        //+ variable, even if it is just a space.
        if ( suite.isBlank() || suite.isEmpty() )
            suite = " ";
        
        if ( suite.length() > 15 )
            this.suite = suite.substring(0, 14);
        else
            this.suite = suite;
    }

    /**
     * Retrieves the city for this broker.
     * 
     * @return String The city in which the broker is located
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city in which this broker is located.
     * 
     * <dl>
     *  <dt><strong><em>NOTE</em></strong></dt>
     *  <dd>The city is a required field and is limited to thirty (30)
     *      characters.</dd>
     * </dl>
     * 
     * @param city The city in which the broker is located
     */
    public void setCity(String city) {
        if ( city.length() > 30 )
            this.city = city.substring(0, 29);
        else
            this.city = city;
    }

    /**
     * Retrieves the state in which this broker is located.
     * 
     * @return String The state in which this broker is located
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the state in which this broker is located.
     * 
     * <dl>
     *  <dt><strong><em>NOTE</em></strong>
     *  <dd>The state is a required field and is limited to two (2) characters.
     *      </dd>
     * </dl>
     * 
     * @param state The state in which the broker is located
     */
    public void setState(String state) {
        this.state = state;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        // For parsing from file, there needs to be some data in the contact
        //+ variable, even if it is just a space.
        if ( contact.isBlank() || contact.isEmpty() )
            contact = " ";
        
        if ( contact.length() > 30 )
            this.contact = contact.substring(0, 29);
        else
            this.contact = contact;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        // For parsing from file, there needs to be some data in the phone
        //+ variable, even if it is just a space.
        if ( phone.isBlank() || phone.isEmpty() )
            phone = " ";
        
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        // For parsing from file, there needs to be some data in the comments
        //+ variable, even if it is just a space.
        if ( email.isBlank() || email.isEmpty() )
            email = " ";
        
        this.email = email;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Instance Methods">
    
    //</editor-fold>

}
