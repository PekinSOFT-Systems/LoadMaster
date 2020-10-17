/*
 * Copyright (C) 2020 PekinSOFT Systems
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * *****************************************************************************
 * *****************************************************************************
 *  Project    :   Load_Master
 *  Class      :   CustomerModel.java
 *  Author     :   Sean Carrick
 *  Created    :   Sep 6, 2020 @ 3:34:44 PM
 *  Modified   :   Sep 6, 2020
 *  
 *  Purpose:
 *  
 *  Revision History:
 *  
 *  WHEN          BY                  REASON
 *  ------------  ------------------- ------------------------------------------
 *  Sep 06, 2020  Sean Carrick        Initial creation.
 *  Oct 10, 2020  Sean Carrick        Added a constructor that takes a String
 *                                    array as an argument to create a new
 *                                    CustomerModel object from the provided
 *                                    data.
 * *****************************************************************************
 */

package com.pekinsoft.loadmaster.model;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.1.0
 * @since 0.1.0
 */
public class CustomerModel {
    //<editor-fold defaultstate="collapsed" desc="Public Static Constants">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Member Fields">
    private long id;            /** Unique customer identification number REQ'D*/
    private String company;     /** Company name for the customer         REQ'D*/
    private String street;      /** Street address for the customer       REQ'D*/
    private String suite;       /** Suite number for the customer      OPTIONAL*/
    private String city;        /** City for the customer                 REQ'D*/
    private String state;       /** State for the customer                REQ'D*/
    private String zip;         /** Zip Code for the customer             REQ'D*/
    private String contact;     /** Contact name for the customer      OPTIONAL*/
    private String phone;       /** Phone Number for the customer      OPTIONAL*/
    private String comments;    /** Any notes about the customer       OPTIONAL*/
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
    public CustomerModel () {
        id = System.currentTimeMillis();
        company = "";
        street = "";
        suite = "";
        city = "";
        state = "";
        zip = "";
        contact = "";
        phone = "";
        comments = "";
    }
    
    public CustomerModel(String[] data) {
        id = Long.valueOf(data[0]);
        company = data[1];
        street = data[2];
        suite = data[3];
        city = data[4];
        state = data[5];
        zip = data[6];
        contact = data[7];
        phone = data[8];
        comments = data[9];
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Static Methods">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Instance Methods">
    /**
     * Retrieves the unique identification number for this customer.
     * @return long Unique identification number
     */
    public long getId() {
        return id;
    }
    
    /**
     * Sets the unique identification number for this customer. However, this
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
     * Retrieves the name of the company for this customer.
     * @return String The company name
     */
    public String getCompany() {
        return company;
    }

    /**
     * Sets the company name for this customer.
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
     * Retrieves the street address for the customer.
     * 
     * @return String The street address, i.e., 100 E. Main Street
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets the street address for this customer.
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
     * Retrieves the suite number for this customer.
     * 
     * <dl>
     *  <dt><strong><em>NOTE</em></strong></dt>
     *  <dd>This method may return `null` if no suite number is stored in the
     *      database.</dd>
     * </dl>
     * 
     * @return String The suite number for this customer, or `null` if none.
     */
    public String getSuite() {
        return (suite == null) ? "" : suite;
    }

    /**
     * Sets the suite number for this customer.
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
     * Retrieves the city for this customer.
     * 
     * @return String The city in which the customer is located
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city in which this customer is located.
     * 
     * <dl>
     *  <dt><strong><em>NOTE</em></strong></dt>
     *  <dd>The city is a required field and is limited to thirty (30)
     *      characters.</dd>
     * </dl>
     * 
     * @param city The city in which the customer is located
     */
    public void setCity(String city) {
        if ( city.length() > 30 )
            this.city = city.substring(0, 29);
        else
            this.city = city;
    }

    /**
     * Retrieves the state in which this customer is located.
     * 
     * @return String The state in which this customer is located
     */
    public String getState() {
        return state;
    }

    /**
     * Sets the state in which this customer is located.
     * 
     * <dl>
     *  <dt><strong><em>NOTE</em></strong>
     *  <dd>The state is a required field and is limited to two (2) characters.
     *      </dd>
     * </dl>
     * 
     * @param state The state in which the customer is located
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
    
    public String getAddress() {
        String address = getStreet() + ", ";
        
        if ( suite != null && !suite.isBlank() && !suite.isEmpty() )
            address += getSuite() + ", ";
        
        address += getCity() + ", ";
        address += getState() + " ";
        address += getZip();
        
        return address;
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

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        // For parsing from file, there needs to be some data in the comments
        //+ variable, even if it is just a space.
        if ( comments.isBlank() || comments.isEmpty() )
            comments = " ";
        
        this.comments = comments;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Instance Methods">
    
    //</editor-fold>

}
