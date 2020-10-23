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
 *  ******************************************************************************
 *   Project:  Load_Master
 *   Module:   ReceivablesModel
 *   Created:  Oct 19, 2020
 *   Modified: Oct 19, 2020
 * 
 *   Purpose:
 *      Provides the definition of and Accounts Receivable object.
 * 
 *   Revision History
 * 
 *   WHEN          BY                  REASON
 *   ------------  ------------------- ------------------------------------------
 *   Oct 19, 2020  Sean Carrick        Initial Creation.
 *  ******************************************************************************
 */

package com.pekinsoft.loadmaster.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The `ReceivableModel` class provides the definition of an Accounts Receivable
 * object. An Accounts Receivable object, in the scope of Load Master, is the
 * total revenue that will be paid to the truck at the completion of a load. 
 * This value will be entered at the time the load is booked and will be stored
 * in the AR Journal until such time as the driver can settle the load, moving
 * money from the AR account to the various accounts for insurance, maintenance,
 * fuel, wages, etc.
 * 
 * When settling the load, all money from the AR account will need to be moved
 * to other accounts, either asset/income or liability/expense accounts. Once 
 * all loads have been settled, the AR account should have a total balance of 
 * zero (0.00).
 * 
 * <dl><dt>Note:</dt><dd>Entries into the Accounts Receivable Journal are
 * <strong><em>never</em></strong> entered into the General Ledger, in and of
 * themselves. Rather, the entries are made in the GL at the time the load is
 * settled, with money being withdrawn/debited from the AR account and portions
 * of that money being deposited/credited to various other accounts.</dd></dl>
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.1.0
 * @since 0.7.8 build 2549
 */
public class ReceivablesModel {
    //<editor-fold defaultstate="collapsed" desc="Public Static Constants">
    public static final int ACCOUNT_NUMBER = 50500;
    public static final String DATA_FILE = ACCOUNT_NUMBER + ".jrnl";
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Member Fields">
    private long id;
    private String tripNumber;
    private String orderNumber;
    private Date date;
    private double amount;
    private boolean settled;
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
    public ReceivablesModel () {
        this(new Date(), "[NO TRIP]", "[NO ORDER]", 0.00);
    }
    
    public ReceivablesModel(Date date, String tripNumber, String orderNumber,
            double amount) {
        this.id = System.currentTimeMillis();
        this.date = date;
        this.tripNumber = tripNumber;
        this.orderNumber = orderNumber;
        this.amount = amount;
        this.settled = false;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Static Methods">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Instance Methods">
    /**
     * Retrieves the trip number for this receivable.
     * 
     * @return the trip number.
     */
    public String getTripNumber() {
        return tripNumber;
    }

    /**
     * Sets the trip number for this receivable.
     * 
     * @param tripNumber the trip number.
     */
    public void setTripNumber(String tripNumber) {
        this.tripNumber = tripNumber;
    }

    /**
     * Retrieves the order number for this receivable.
     * 
     * @return the order number.
     */
    public String getOrderNumber() {
        return orderNumber;
    }

    /**
     * Sets the order number for this receivable.
     * 
     * @param orderNumber the order number.
     */
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * Retrieves the date this receivable was acquired as a `java.util.Date`
     * object.
     * 
     * @return receivable acquisition date.
     */
    public Date getDate() {
        return date;
    }
    
    /**
     * Retrieves the date this receivable was acquired as a `java.lang.String`
     * in the format MM/dd/yyyy.
     * 
     * @return receivable acquisition date.
     */
    public String getDateAsString() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(this.date);
    }
    
    /**
     * Retrieves the date this receivable was acquired as a `java.lang.String`
     * in the provided format, such as MMM dd, yyyy.
     * 
     * @param format    The desired format for the date.
     * @return          receivable acquisition date in the desired format.
     */
    public String getDateAsString(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(this.date);
    }
    /**
     * Sets the date this receivable was acquired using a `java.util.Date` 
     * object.
     * 
     * @param date receivable acquisition date.
     */
    public void setDate(Date date) {
        this.date = date;
    }
    
    /**
     * Sets the date this receivable was acquired using a `java.lang.String` in
     * the format MM/dd/yyyy.
     * 
     * @param date      The date the receivable was acquired.
     * @throws ParseException in the event the date cannot be parsed.
     */
    public void setDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        this.date = sdf.parse(date);
    }
    
    /**
     * Sets the date this receivable was acquired using a `java.lang.String` in
     * the specified format.
     * 
     * @param date      The date the receivable was acquired.
     * @param format    The format the date is in.
     * @throws ParseException in the event the date cannot be parsed.
     */
    public void setDate(String date, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        this.date = sdf.parse(date);
    }

    /**
     * Retrieves the amount of this receivable.
     * 
     * @return receivable amount/value.
     */
    public double getAmount() {
        return amount;
    }
    
    /**
     * Retrieves the amount of this receivable as a `java.lang.String`.
     * 
     * @return receivable amount/value.
     */
    public String getAmountAsString() {
        return String.valueOf(this.amount);
    }

    /**
     * Sets the amount of this receivable from a `java.lang.double` value.
     * 
     * @param amount the total value of this receivable.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }
    
    /**
     * Sets the amount of this receivable from a `java.lang.String` value.
     * 
     * @param amount the total value of this receivable.
     */
    public void setAmount(String amount) {
        this.amount = Double.parseDouble(amount);
    }

    /**
     * Retrieves the unique identifying number of this receivable.
     * 
     * @return the unique ID
     */
    public long getId() {
        return id;
    }
    
    /**
     * Retrieves the unique identifying number of this receivable as a
     * `java.lang.String` value.
     * 
     * @return the unique ID.
     */
    public String getIdAsString() {
        return String.valueOf(this.id);
    }
    
    /**
     * Sets the unique identification number for this receivable from the 
     * provided `java.lang.long` value. This method should only be used when 
     * reading in the journal from file.
     * 
     * @param id unique id number.
     */
    public void setId(long id) {
        this.id = id;
    }
    
    /**
     * Sets the unique identification number for this receivable from the
     * provided `java.lang.String` value. This method should only be used when
     * reading in the journal from file.
     * 
     * @param id unique id number.
     */
    public void setId(String id) {
        this.id = Long.valueOf(id);
    }
    
    /**
     * Determines whether or not this journal entry has been settled to the
     * General Ledger.
     * 
     * @return `true` if posted to the GL, `false` otherwise.
     */
    public boolean isSettled() {
        return this.settled;
    }
    
    /**
     * Sets whether this journal entry has been settled to the General Ledger.
     * 
     * @param settled `true` if posted. Default is `false` and needs to be 
     *               updated once the journal entry is posted to the GL.
     */
    public void setSettled(boolean settled) {
        this.settled = settled;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Instance Methods">
    
    //</editor-fold>

}
