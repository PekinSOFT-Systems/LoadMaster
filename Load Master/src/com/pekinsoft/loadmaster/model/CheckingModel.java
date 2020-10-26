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
 * Class   :   CheckingModel
 * Author  :   Sean Carrick
 * Created :   Oct 25, 2020
 * Modified:   Oct 25, 2020
 * 
 * Purpose:
 *     [Provide a general purpose overview for this class]
 * 
 * WHEN          BY                  REASON
 * ------------  ------------------  -------------------------------------------
 * Oct 25, 2020  Sean Carrick        Initial creation.
 * 
 * *****************************************************************************
 */

package com.pekinsoft.loadmaster.model;

import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.err.ValidationException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
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
public class CheckingModel {
    //<editor-fold defaultstate="collapsed" desc="Public Static Constants">
    public static final int ACCOUNT_NUMBER = 50010;
    public static final String JOURNAL = Starter.props.getDataFolder()
            + ACCOUNT_NUMBER;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Member Fields">
    private long id;
    private Date date;
    private String code;
    private String description;
    private double withdrawal;
    private boolean deposit;
    private boolean balanced;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
    public CheckingModel () {
        this(new Date(), "", "", 0.0, false, false);
    }
    
    public CheckingModel(Date date, String code, String description, 
            double withdrawal, boolean deposit, boolean balanced) {
        this.id = System.currentTimeMillis();
        this.date = date;
        this.code = code;
        this.description = description;
        this.withdrawal = withdrawal;
        this.deposit = deposit;
        this.balanced = balanced;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Instance Methods">
    /**
     * Retrieves the unique ID for this checking entry.
     * 
     * @return the unique ID
     */
    public long getId() {
        return id;
    }
    
    /**
     * Convenience method for retrieving the unique ID as a `java.lang.String`
     * value for displaying as text, without needing to do the conversion
     * explicitly.
     * 
     * @return a `java.lang.String` representation of this unique ID
     */
    public String getIdAsString() {
        return String.valueOf(id);
    }

    /**
     * Sets the unique ID for this checking entry.
     * 
     * <dl><dt>Developer's Note</dt><dd>This method should only be used when
     * reading the data in from a file or database, as the unique ID is an 
     * `autonumber` field, just like in a database system. Therefore, it is best
     * for developers using this data model to not create their own ID numbers 
     * for this field, though it is possible to do so provided the custom ID is
     * a `long` value.</dd></dl>
     * 
     * @param id the unique ID for this entry
     */
    public void setId(long id) {
        this.id = id;
    }
    
    /**
     * Convenience method for setting the unique ID from a `java.lang.String`,
     * in the event the value is retrieved from a `javax.swing.JTextField`
     * object. This method allows for *implicit* conversion.
     * 
     * @param id the ID as a `java.lang.String`
     */
    public void setIdFromString(String id) {
        this.id = Long.parseLong(id);
    }

    /**
     * Retrieves the date of this transaction.
     * 
     * @return the transaction date.
     */
    public Date getDate() {
        return date;
    }
    
    /**
     * Retrieves the date of this transaction as a formatted `java.lang.String`
     * representation of the underlying `java.util.Date` object. This convenience
     * method allows you to implicitly get the date as a string for display in
     * a text field or other textual scenario. The format of the date returned
     * by this method is "MM/dd/yyyy".
     * 
     * @return the date formatted as MM/dd/yyyy"
     */
    public String getDateAsString() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(date);
    }
    
    /**
     * Retrieves the date of this transaction as a formatted `java.lang.String`
     * representation of the underlying `java.util.Date` object. This convenience
     * method allows you to implicitly get the date as a string for display and
     * have the custom format that you provide applied to the value.
     * 
     * @param format the format to use, i.e., "MMM dd, yyyy"
     * @return the date formatted by the provided format string.
     */
    public String getDateAsString(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    /**
     * Sets the date of this transaction.
     * 
     * @param date the transaction date.
     */
    public void setDate(Date date) {
        this.date = date;
    }
    
    /**
     * Convenience method to allow you to set the date for this transaction from
     * a `java.lang.String` value. This default version of `setDateFromString`
     * uses the format "MM/dd/yyyy" as the format of the date. If you have a
     * custom date format you need to use, call the overridden method
     * `setDateFromString(String date, String format)` and provide your custom
     * date format string.
     * 
     * @param date the date of this transaction as a `java.lang.String` object
     * @throws ValidationException in the event the date is not valid and 
     *                             parsable
     */
    public void setDateFromString(String date) throws ValidationException {
        setDateFromString(date, "MM/dd/yyyy");
    }
    
    /**
     * Convenience method to allow you to set the date for this transaction from
     * a `java.lang.String` value. This overridden method of `setDateFromString`
     * requires a custom format string to be provided. If you only need the date
     * string formatted as "MM/dd/yyyy", call the default method that does not
     * require a formatting string to be provided.
     * 
     * @param date  the date of this transaction as a `java.lang.String` object
     * @param format the custom format string to use, i.e., "MMM dd, yyyy`
     * @throws ValidationException in the event the date or format string are
     *                             not valid, causing the date to not parse.
     */
    public void setDateFromString(String date, String format) throws 
            ValidationException {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            this.date = sdf.parse(date);
        } catch ( ParseException ex ) {
            throw new ValidationException("Provided date (" + date
                    + ") is not valid with the provided format (" 
                    + format + "). Checking Account transaction date not set.",
                    ex);
        }
    }

    /**
     * Retrieves the check number or code for this transaction.
     * 
     * @return the transaction code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the check number or code for this transaction.
     * 
     * @param code the transaction code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Retrieves the description of this transaction.
     * 
     * @return the transaction description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description for this transaction.
     * 
     * @param description the transaction description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the withdrawal amount of this transaction.
     * 
     * @return how much was withdrawn
     */
    public double getWithdrawal() {
        return withdrawal;
    }
    
    /**
     * Convenience method to retrieve the value of the withdrawal as currency in
     * the locale-specific format, using the system default locale on which the
     * application is running.
     * 
     * @return the withdrawn amount as a system default locale fomatted currency
     *         `java.lang.String`
     * @throws com.pekinsoft.loadmaster.err.ValidationException in the event 
     *          this method is invoked on a transaction that contains a deposit.
     */
    public String getWithdrawalAsFormattedString() throws ValidationException {
        if ( withdrawal == 0.0 )
            throw new ValidationException("Withdrawal value is 0.0");
        
        NumberFormat number = NumberFormat.getCurrencyInstance();
        return ((DecimalFormat)number).format(withdrawal);
    }

    /**
     * Set the withdrawal amount of this transaction.
     * 
     * @param withdrawal the amount withdrawn
     */
    public void setWithdrawal(double withdrawal) {
        this.withdrawal = withdrawal;
    }

    /**
     * Determines if this transaction was a deposit or withdrawal.
     * 
     * @return `true` if deposited, `false` if withdrawn
     */
    public boolean isDeposit() {
        return deposit;
    }

    /**
     * Sets whether or not this transaction is a deposit.
     * 
     * @param deposit `true` if a deposit, `false` if a withdrawal
     */
    public void setDeposit(boolean deposit) {
        this.deposit = deposit;
    }

    /**
     * Determines if the transaction has been balanced with the account statement
     * from the financial institution.
     * 
     * @return `true` if already balanced, `false` otherwise
     */
    public boolean isBalanced() {
        return balanced;
    }

    /**
     * Sets this flag to tell whether or not this transaction has been balanced
     * with the account statement from the financial institution.
     * 
     * @param balanced set `true` once balanced, `false` otherwise and by default
     */
    public void setBalanced(boolean balanced) {
        this.balanced = balanced;
    }
    //</editor-fold>
    
}
