/*
 * Copyright (C) 2020 PekinSOFT™ Systems
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
 * 
 * Project :   Load Master™
 * Class   :   FuelCardModel
 * Author  :   Sean Carrick
 * Created :   Oct 22, 2020
 * Modified:   Oct 22, 2020
 * 
 * Purpose:
 *     Provides the definition for the data to be stored for the Fuel Card 
 *      Journal.
 * 
 * WHEN          BY                  REASON
 * ------------  ------------------  -------------------------------------------
 * Oct 22, 2020  Sean Carrick        Initial creation.
 * 
 * *****************************************************************************
 */
package com.pekinsoft.loadmaster.model;

import com.pekinsoft.loadmaster.Starter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 *
 * @version 0.1.0
 * @since 0.1.0
 */
public class FuelCardModel {

    //<editor-fold defaultstate="collapsed" desc="Public Static Constants">
    /**
     * The account number in the Chart of Accounts for the Fuel Card Account
     */
    public static final int ACCOUNT_NUMBER = 50040;
    public static final String JOURNAL = Starter.props.getDataFolder() 
            + ACCOUNT_NUMBER;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Member Fields">
    private long id;
    private Date date;
    private int fromAcct;
    private String tripNumber;
    private double amount;
    private boolean posted;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
    /**
     * Creates a new instance of the `FuelCardModel` fuel card account
     * definition.
     */
    public FuelCardModel() {
        this(System.currentTimeMillis(), 0, "No Active Load", 0.0);
    }

    /**
     * Creates a new instance of the `FuelCardModel` with the provided data.
     *
     * @param id unique transaction ID number.
     * @param fromAcct account from which the money came.
     * @param tripNumber the trip number associated with this transaction.
     * @param amount the amount of money transferred in.
     */
    public FuelCardModel(long id, int fromAcct, String tripNumber,
            double amount) {
        this.id = id;
        this.fromAcct = fromAcct;
        this.tripNumber = tripNumber;
        this.amount = amount;
        this.posted = false;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Static Methods">
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Public Instance Methods">
    /**
     * Retrieves the unique identification number for this model.
     *
     * @return the unique ID number.
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the unique identification number for this model.
     *
     * @param id the unique ID to use.
     */
    public void setId(long id) {
        this.id = id;
    }
    
    /**
     * Retrieves the date of the transaction for this model.
     * 
     * @return the transaction date as a `java.util.Date` object.
     */
    public Date getDate() {
        return date;
    }
    
    /**
     * Retrieves the date of the transaction for this model as a 
     * `java.lang.String`, in the MM/dd/yyyy format.
     * 
     * @return the transaction date as a `java.lang.String` object.
     */
    public String getDateAsString() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(date);
    }
    
    /**
     * Retrieves the date of the transaction for this model as a
     * `java.lang.String`, in the specified format, such as MMM dd, yyyy.
     * 
     * @param format the format for the date string.
     * @return the date as a `java.lang.String` in the specified format.
     */
    public String getDateAsString(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
    
    /**
     * Sets the date for the transaction associated with this model.
     * 
     * @param date a `java.util.Date` object for this model.
     */
    public void setDate(Date date) {
        this.date = date;
    }
    
    /**
     * Sets the date for the transaction associated with this model from a 
     * `java.lang.String` in the format MM/dd/yyyy.
     * 
     * @param date a `java.lang.String` formatted date.
     * @throws ParseException in the event the date string is in an invalid
     *                        format.
     */
    public void setDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        this.date = sdf.parse(date);
    }
    
    /**
     * Sets the date for the transaction associated with this model from a 
     * `java.lang.String` in the provided format.
     * 
     * @param date  a `java.lang.String` formatted date.
     * @param format    the format the date string is in.
     * @throws ParseException in the event the date string or the format string
     *                        is invalid.
     */
    public void setDate(String date, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        this.date = sdf.parse(date);
    }

    /**
     * Retrieves the transfer account from which the money originated for this
     * model.
     *
     * @return the account from which the money was transferred.
     */
    public int getFromAcct() {
        return fromAcct;
    }

    /**
     * Sets the transfer account from which the money originated for this model.
     *
     * @param fromAcct the account from which the money was transferred.
     */
    public void setFromAcct(int fromAcct) {
        this.fromAcct = fromAcct;
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
     * Retrieves the amount of money for this transaction.
     * 
     * @return the amount of money associated with this model.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the amount of money associated with this transaction.
     * 
     * @param amount the amount of money associated with this model.
     */
    public void setAmount(double amount) {
        this.amount = amount;
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
    
    //<editor-fold defaultstate="collapsed" desc="Private Instance Methods">
    //</editor-fold>

}
