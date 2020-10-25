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
 * *****************************************************************************
 * Project:  Load_Master
 * Module:   FuelPurchaseModel
 * Created:  Oct 19, 2020
 * Modified: Oct 19, 2020
 * 
 * Purpose:
 *      Defines a fuel entry object for the database and accounting system.
 * 
 * Revision History
 * 
 * WHEN          BY                  REASON
 * ------------  ------------------- ------------------------------------------
 * Oct 19, 2020  Sean Carrick        Initial Creation.
 * Oct 25, 2020  Sean Carrick        Added the ability to make a fuel purchase
 *                                   from an account other than the Fuel Card
 *                                   account by adding the field `fromAccount`,
 *                                   as well as the setter and getter for it.
 * 
 * *****************************************************************************
 */

package com.pekinsoft.loadmaster.model;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The `FuelEntryModel` defines the data required and stored for a fuel purchase
 * made through Load Master.
 * 
 * This class provides a means for a driver to store critical information about
 * each fuel purchase as a means of maintaining a history of fuel purchases and
 * understanding how driving conditions, speeds, etc., affect fuel economy. By
 * having all of this information stored in the Fuel Account Journal, 
 * `10040.jrnl`, the driver will be able to make informed decisions regarding
 * his/her driving practices in order to reduce their fuel expense over time.
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.1.0
 * @since 0.1.0
 */
public class FuelPurchaseModel {
    //<editor-fold defaultstate="collapsed" desc="Public Static Constants">
    /**
     * The account number in the Chart of Accounts for the Fuel Purchase Account
     */
    public static final int ACCOUNT_NUMBER = 10040;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Member Fields">
    private long id;
    private Date date;
    private String tripNumber;
    private int odometer;
    private String location;
    private double gallonsOfDiesel;
    private double pricePerGallonDiesel;
    private boolean defPurchased;
    private double gallonsOfDef;
    private double pricePerGallonDef;
    private String notes;
    private int fromAccount;
    private boolean posted;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
    public FuelPurchaseModel () {
        this.id = System.currentTimeMillis();
        this.date = new Date();
        this.tripNumber = "No Active Load";
        this.odometer = 0;
        this.location = "[No Location Provided]";
        this.gallonsOfDef = 0.0;
        this.gallonsOfDiesel = 0.0;
        this.notes = "";
        this.pricePerGallonDef = 0.0;
        this.pricePerGallonDiesel = 0.0;
        this.fromAccount = FuelCardModel.ACCOUNT_NUMBER; // Default, can be chgd
        this.posted = false;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Instance Methods">
    /**
     * Retrieves the unique identifying number for this fuel purchase.
     * 
     * @return unique id number.
     */
    public long getId() {
        return id;
    }
    
    /**
     * Retrieves the unique identifying number for this fuel purchase as a
     * `java.lang.String` value.
     * 
     * @return unique id number.
     */
    public String getIdAsString() {
        return String.valueOf(id);
    }
    
    /**
     * Sets the unique identifying number for this fuel purchase. 
     * <dl><dt>Note</dt><dd>This setter should only be used when reading data in 
     * from a data file, as the unique ID number is automatically created when
     * creating a new `FuelPurchaseModel` object.</dd></dl>
     * 
     * @param id unique id number.
     */
    public void setId(long id) {
        this.id = id;
    }
    
    /**
     * Sets the unique identifying number for this fuel purchase, from a 
     * `java.lang.String` value. 
     * <dl><dt>Note</dt><dd>This setter should only be used when reading data in 
     * from a data file, as the unique ID number is automatically created when
     * creating a new `FuelPurchaseModel` object.</dd></dl>
     * 
     * @param id unique id number.
     */
    public void setId(String id) {
        this.id = Long.parseLong(id);
    }

    /**
     * Retrieves the purchase date for this fuel entry as a `java.util.Date`
     * object.
     * 
     * @return the purchase date.
     */
    public Date getDate() {
        return date;
    }
    
    /**
     * Retrieves the purchase date for this fuel entry as a `java.lang.String`
     * value, formatted as MM/dd/yyyy.
     * 
     * @return the purchase date.
     */
    public String getDateAsString() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(date);
    }
    
    /**
     * Retrieves the purchase date for this fuel entry as a `java.lang.String`
     * value, formatted with the provided date format, such as MMM dd, yyyy.
     * 
     * @param format The format to apply to the purchase date.
     * @return the purchase date.
     */
    public String getDateAsString(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * Sets the purchase date for this fuel entry as a `java.util.Date` object.
     * <dl><dt>Note</dt><dd>The date is created as the current date by default
     * when a new `FuelPurchaseModel` is created. This setter may be used for 
     * adding the date to the object when reading records from a data file, or 
     * when entering a fuel purchase at a date later than when it was 
     * purchased.</dd></dl>
     * 
     * @param date the purchase date.
     */
    public void setDate(Date date) {
        this.date = date;
    }
    
    /**
     * The trip number with which this model is associated. The value returned
     * will either be the trip that was active at the time this transaction
     * occurred, or the phrase &quot;No Active Load&quot;.
     *
     * @return the trip number associated with this model.
     */
    public String getTripNumber() {
        return tripNumber;
    }
    
    /**
     * Sets the trip number with which this model is associated.
     * <dl><dt>Developer's Note</dt><dd>The best thing to do to provide the data
     * for this field is to pull the current load from the application settings
     * file. There will either be a trip number or &quot;No Active Load&quot;.
     * </dd></dl>
     *
     * @param tripNumber the trip number associated with this model.
     */
    public void setTripNumber(String tripNumber) {
        this.tripNumber = tripNumber;
    }

    /**
     * Retrieves the odometer reading at the time of fuel purchase.
     * 
     * @return odometer reading at time of fuel purchase.
     */
    public int getOdometer() {
        return odometer;
    }
    
    /**
     * Retrieves the odometer reading at the time of fuel purchase as a
     * `java.lang.String` value.
     * 
     * @return odometer reading at time of fuel purchase.
     */
    public String getOdometerAsString() {
        return String.valueOf(odometer);
    }

    /**
     * Sets the odometer reading at the time of fuel purchase.
     * 
     * @param odometer odometer reading at time of fuel purchase.
     */
    public void setOdometer(int odometer) {
        this.odometer = odometer;
    }
    
    /**
     * Sets the odometer reading at the time of fuel purchase, from a
     * `java.lang.String` value.
     * 
     * @param odometer odometer reading at time of fuel purchase.
     */
    public void setOdometer(String odometer) {
        this.odometer = Integer.parseInt(odometer);
    }

    /**
     * Retrieves the location the fuel was purchased.
     * 
     * @return location of fuel purchase.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the location of the fuel purchase.
     * <dl><dt>Note</dt><dd>This is a free-form field with no limits on its 
     * length. Therefore, the truck stop name, city, state, and other data may
     * be stored in this field.</dd></dl>
     * 
     * @param location location of fuel purchase.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Retrieves the total number of gallons of diesel fuel purchased.
     * 
     * @return gallons purchased.
     */
    public double getGallonsOfDiesel() {
        return gallonsOfDiesel;
    }
    
    /**
     * Retrieves the total number of gallons of diesel fuel purchased as a
     * `java.lang.String` value.
     * 
     * @return gallons purchased.
     */
    public String getGallonsOfDieselAsString() {
        return String.valueOf(gallonsOfDiesel);
    }
    
    /**
     * Retrieves the total number of gallons of diesel fuel purchased as a 
     * `java.lang.double` value, in the supplied format.
     * 
     * @param format    The format to apply to the number.
     * @return          gallons purchased.
     * @throws ParseException in the event the number cannot be parsed.
     */
    public double getGallonsOfDieselAsString(String format) 
            throws ParseException {
        NumberFormat nf = new DecimalFormat("#,##0.00");
        Number gallons = nf.parse(getGallonsOfDieselAsString());
        return gallons.doubleValue();
    }

    /**
     * Sets the total number of gallons of diesel fuel purchased.
     * 
     * @param gallonsOfDiesel gallons purchased.
     */
    public void setGallonsOfDiesel(double gallonsOfDiesel) {
        this.gallonsOfDiesel = gallonsOfDiesel;
    }
    
    /**
     * Sets the total number of gallons of diesel fuel purchased, from a
     * `java.lang.String` value.
     * 
     * @param gallonsOfDiesel gallons purchased.
     */
    public void setGallonsOfDiesel(String gallonsOfDiesel) {
        this.gallonsOfDiesel = Double.parseDouble(gallonsOfDiesel);
    }

    /**
     * Retrieves the price per gallon of diesel fuel when purchased.
     * 
     * @return price per gallon at time of purchase.
     */
    public double getPricePerGallonDiesel() {
        return pricePerGallonDiesel;
    }
    
    /**
     * Retrieves the price per gallon of diesel fuel purchased as a
     * `java.lang.String` value.
     * 
     * @return price per gallon.
     */
    public String getPricePerGallonDieselAsString() {
        return String.valueOf(gallonsOfDiesel);
    }
    
    /**
     * Retrieves the price per gallon of diesel fuel purchased as a
     * `java.lang.double` value, in the supplied format.
     * 
     * @param format    The format to apply to the number.
     * @return          price per gallon.
     * @throws ParseException in the event the number cannot be parsed.
     */
    public double getPricePerGallonDieselAsString(String format) 
            throws ParseException {
        NumberFormat nf = new DecimalFormat("#,##0.00");
        Number gallons = nf.parse(getGallonsOfDieselAsString());
        return gallons.doubleValue();
    }

    /**
     * Sets the price per gallon of diesel fuel when purchased.
     * 
     * @param pricePerGallonDiesel price per gallon at time of purchase.
     */
    public void setPricePerGallonDiesel(double pricePerGallonDiesel) {
        this.pricePerGallonDiesel = pricePerGallonDiesel;
    }
    
    /**
     * Sets the price per gallon of diesel fuel when purchased, from a
     * `java.lang.String` value.
     * 
     * @param pricePerGallon price per gallon at time of purchase.
     */
    public void setPricePerGallonOfDiesel(String pricePerGallon) {
        this.pricePerGallonDiesel = Double.valueOf(pricePerGallon);
    }

    /**
     * Determines whether diesel exhaust fluid (DEF) was purchased during this
     * fuel stop.
     * 
     * @return `true` if purchasing DEF at the same time, `false` otherwise.
     */
    public boolean isDefPurchased() {
        return defPurchased;
    }

    /**
     * Sets whether diesel exhaust fluid (DEF) was purchased at the same time.
     * 
     * @param defPurchased 
     */
    public void setDefPurchased(boolean defPurchased) {
        this.defPurchased = defPurchased;
    }

    /**
     * Retrieves the number of gallons of DEF purchased.
     * 
     * @return gallons of DEF purchased.
     */
    public double getGallonsOfDef() {
        return gallonsOfDef;
    }
    
    /**
     * Retrieves the total number of gallons of diesel fuel purchased as a
     * `java.lang.String` value.
     * 
     * @return gallons purchased.
     */
    public String getGallonsOfDefAsString() {
        return String.valueOf(gallonsOfDef);
    }
    
    /**
     * Retrieves the total number of gallons of diesel fuel purchased as a 
     * `java.lang.double` value, in the supplied format.
     * 
     * @param format    The format to apply to the number.
     * @return          gallons purchased.
     * @throws ParseException in the event the number cannot be parsed.
     */
    public double getGallonsOfDefAsString(String format) 
            throws ParseException {
        NumberFormat nf = new DecimalFormat("#,##0.00");
        Number gallons = nf.parse(getGallonsOfDefAsString());
        return gallons.doubleValue();
    }

    /**
     * Sets the number of gallons of DEF purchased.
     * 
     * @param gallonsOfDef gallons of DEF purchased.
     */
    public void setGallonsOfDef(double gallonsOfDef) {
        this.gallonsOfDef = gallonsOfDef;
    }
    
    /**
     * Sets the number of gallons of DEF purchased from a `java.lang.String`
     * value.
     * 
     * @param gallonsOfDef gallons of DEF purchased.
     */
    public void setGallonsOfDef(String gallonsOfDef) {
        this.gallonsOfDef = Double.parseDouble(gallonsOfDef);
    }

    /**
     * Retrieves the price per gallon of DEF at the time of the purchase.
     * 
     * @return price per gallon of DEF at time of purchase.
     */
    public double getPricePerGallonDef() {
        return pricePerGallonDef;
    }
    
    /**
     * Retrieves the price per gallon of DEF fuel purchased as a
     * `java.lang.String` value.
     * 
     * @return price per gallon.
     */
    public String getPricePerGallonDefAsString() {
        return String.valueOf(gallonsOfDef);
    }
    
    /**
     * Retrieves the price per gallon of DEF fuel purchased as a
     * `java.lang.double` value, in the supplied format.
     * 
     * @param format    The format to apply to the number.
     * @return          price per gallon.
     * @throws ParseException in the event the number cannot be parsed.
     */
    public double getPricePerGallonDefAsString(String format) 
            throws ParseException {
        NumberFormat nf = new DecimalFormat("#,##0.00");
        Number gallons = nf.parse(getGallonsOfDefAsString());
        return gallons.doubleValue();
    }

    /**
     * Sets the price per gallon of DEF at the time of purchase.
     * 
     * @param pricePerGallonDef price per gallon of DEF at the time of purchase.
     */
    public void setPricePerGallonDef(double pricePerGallonDef) {
        this.pricePerGallonDef = pricePerGallonDef;
    }
    
    /**
     * Sets the price per gallon of DEF at the time of purchase, from a
     * `java.lang.String` value.
     * 
     * @param pricePerGallon price per gallon of DEF at the time of purchase.
     */
    public void setPricePerGallonDef(String pricePerGallon) {
        this.pricePerGallonDef = Double.parseDouble(pricePerGallon);
    }

    /**
     * Retrieves the notes stored regarding this fuel stop.
     * 
     * @return notes.
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets the notes regarding this fuel stop.
     * <dl><dt>Note</dt><dd>This field may be used for whatever notes the driver
     * wants to keep regarding the fuel stop. Some suggestions are:<ul><li>
     * weather</li><li>average speed</li><li>road conditions</li><li>mountains
     * or plains</li><li>etc.</li></ul></dd></dl>
     * 
     * @param notes notes regarding the fuel stop.
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    /**
     * Retrieves the credit account from which this Fuel Purchase is to be 
     * withdrawn.
     * 
     * @return the credit account associated with this Fuel Purchase.
     */
    public int getFromAccount() {
        return fromAccount;
    }
    
    /**
     * Sets the credit account from which this Fuel Purchase is to be withdrawn.
     * 
     * @param fromAccount the credit account from which to withdraw the Fuel 
     *                    Purchase price.
     */
    public void setFromAccount(int fromAccount) {
        this.fromAccount = fromAccount;
    }
    
    /**
     * Determines whether or not this transaction has been posted to the General
     * Ledger.
     * 
     * @return `true` if already posted to the General Ledger, `false` otherwise.
     */
    public boolean isPosted() {
        return posted;
    }
    
    /**
     * Sets the posted flag. This should only be set to `true` when the 
     * transaction this model represents has been posted to the General Ledger.
     * 
     * @param posted `true` when posted to the General Ledger, `false` otherwise.
     */
    public void setPosted(boolean posted) {
        this.posted = posted;
    }
    //</editor-fold>

}
