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
 * Class   :   ReserveModel
 * Author  :   Sean Carrick
 * Created :   Oct 22, 2020
 * Modified:   Oct 22, 2020
 * 
 * Purpose:
 *     Provides the general definition for the reserve account data.
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
import java.time.Instant;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.1.0
 * @since 0.1.0
 */
public class ReserveModel {
    //<editor-fold defaultstate="collapsed" desc="Public Static Constants">
    public static final int ACCOUNT_NUMBER = 50060;
    public static final String DATA_FILE = ACCOUNT_NUMBER + ".jrnl";
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Member Fields">
    private long id;
    private String tripNumber;
    private Date date;
    private int fromAccount;
    private double amount;
    private boolean posted;
    
    private LogRecord entry;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
    public ReserveModel () {
        entry = new LogRecord(Level.ALL, "Logging started");
        
        id = System.currentTimeMillis();
        tripNumber = "No Active Load";
        date = new Date();
        fromAccount = 0;
        amount = 0.0;
        posted = false;
    }
    
    public ReserveModel(String[] data) {
        this();
        
        id = Long.parseLong(data[0]);
        tripNumber = data[1];
        fromAccount = Integer.parseInt(data[3]);
        amount = Double.parseDouble(data[4]);
        posted = Boolean.parseBoolean(data[5]);
        
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try {
            date = sdf.parse(data[2]);
        } catch ( ParseException ex ) {
            entry.setSourceClassName(getClass().getCanonicalName());
            entry.setSourceMethodName(getClass().getName() + " (Constructor)");
            entry.setParameters(data);
            entry.setMessage(ex.getMessage());
            entry.setThrown(ex);
            entry.setInstant(Instant.now());
            Starter.logger.error(entry);
            
            // We'll just set the date to the current date.
            date = new Date();
        }
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Instance Methods">
    /**
     * Retrieves the unique identifying number for this model.
     * 
     * @return the unique ID number.
     */
    public long getId() {
        return id;
    }
    
    public String getIdAsString() {
        return String.valueOf(id);
    }

    /**
     * Sets the unique idenifying number for this model.
     * 
     * @param id the unique ID number.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Retrieves the trip number associated with this model.
     * 
     * @return the associated trip number.
     */
    public String getTripNumber() {
        return tripNumber;
    }

    /**
     * Sets the trip number for the load associated with this model.
     * 
     * @param tripNumber the trip number to associate with this model.
     */
    public void setTripNumber(String tripNumber) {
        this.tripNumber = tripNumber;
    }

    /**
     * Retrieves the date of this model as a `java.util.Date` object.
     * 
     * @return the date of this model.
     */
    public Date getDate() {
        return date;
    }

    /**
     * Retrieves the date of this model as a `java.lang.String` object in the
     * format of MM/dd/yyyy.
     * 
     * @return the date of this model.
     */
    public String getDateAsString() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(date);
    }
    
    /**
     * Retrieves the date of this model as a `java.lang.String` object in the
     * format specified in the parameter.
     * 
     * @param format the format for the date of this model.
     * @return the date of this model in the provided format.
     */
    public String getDateAsString(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }
    
    /**
     * Sets the date for this model from a `java.util.Date` object.
     * 
     * @param date the date to set for this model.
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * Sets the date for this model from a `java.lang.String` in the format of
     * MM/dd/yyyy.
     * 
     * @param date  the date for this model as a string value.
     * @throws ParseException in the event the date will not parse.
     */
    public void setDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        this.date = sdf.parse(date);
    }
    
    /**
     * Sets the date for this model from a custom formatted `java.lang.String`
     * value.
     * 
     * @param date  the formatted date for this model as a string value.
     * @param format the format the date is currently in.
     * @throws ParseException in the event the date or the format causes an
     *                        error.
     */
    public void setDate(String date, String format) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        this.date = sdf.parse(date);
    }
    
    /**
     * Retrieves the account from which the money in this model deposited.
     * 
     * @return the account from which the money came.
     */
    public int getFromAccount() {
        return fromAccount;
    }
    
    public String getFromAccountAsString() {
        return String.valueOf(fromAccount);
    }

    /**
     * Sets the account from which the money in this model deposited.
     * 
     * @param fromAccount the account from which the money came.
     */
    public void setFromAccount(int fromAccount) {
        this.fromAccount = fromAccount;
    }

    /**
     * Retrieves the total amount of this model's value.
     * 
     * @return the amount of money in this model.
     */
    public double getAmount() {
        return amount;
    }
    
    public String getAmountAsString() {
        return String.valueOf(amount);
    }

    /**
     * Sets the total amount of this model's value.
     * 
     * @param amount the amount of money coming into this model.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Determines whether this model has been posted to the General Ledger yet.
     * 
     * @return `true` if this model has been posted to the GL, `false` otherwise.
     */
    public boolean isPosted() {
        return posted;
    }

    /**
     * Sets whether this model has been posted to the General Ledger yet.
     * 
     * @param posted `true` when it is posted, `false` otherwise.
     */
    public void setPosted(boolean posted) {
        this.posted = posted;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Instance Methods">
    
    //</editor-fold>

}
