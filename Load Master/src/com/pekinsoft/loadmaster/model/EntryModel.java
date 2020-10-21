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
 *  Project    :   Load_Master
 *  Class      :   EntryModel.java
 *  Author     :   Sean Carrick
 *  Created    :   Oct 19, 2020 @ 11:14:43 AM
 *  Modified   :   Oct 19, 2020
 *  
 *  Purpose:
 *      Provides the model of the data table for a load. This class contains all
 *      data that is associated with a single load.
 *  
 *  Revision History:
 *  
 *  WHEN          BY                  REASON
 *  ------------  ------------------- ------------------------------------------
 *  Oct 19, 2020  Sean Carrick        Initial creation.
 * *****************************************************************************
 */
package com.pekinsoft.loadmaster.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The `EntryModel` class defines a General Ledger entry for the accounting 
 * system in Load Master. Each GL entry consists of specific data that is the
 * same for every GL entry. 
 * 
 * Since the Load Master accounting system is double-entry, each transaction
 * amount must be removed (debited or withdrawn) from one account and placed 
 * into (credited or deposited) into another account. The entries in the GL are
 * not very detailed, but the details of any transaction may be viewed in the
 * various accounts' associated journals. Whereas the GL maintains the "books"
 * of the accounting system, so to speak, the various journals maintain the 
 * detailed histories of the GL transactions.
 * 
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.1.0
 * @since 0.7.8 build 2549
 */
public class EntryModel {
    /** Transaction date. */
    private Date date;
    /** Transaction code. */
    private String code;
    /** Transaction description. */
    private String description;
    /** Account from which money will be removed. */
    private int fromAccount;
    /** Account to which money will be deposited. */
    private int toAccount;
    /** Transaction amount. */
    private double amount;
    /** Whether transaction is tax deductible. */
    private boolean deductible;
    /** Whether transaction has been balanced with bank statement. */
    private boolean balanced;
    
    /**
     * Creates a default instance of the `EntryModel` object, providing only the
     * current date. All other fields are set to default values.
     */
    public EntryModel() {
        this(new Date(), "", "", 0, 0, 0.00, false, false);
    }
    
    /**
     * Creates a new instance of the `EntryModel` object with the supplied field
     * values.
     * 
     * @param date          Transaction date.
     * @param code          Transaction code or check number.
     * @param description   Transaction details or description.
     * @param from          Account from which the money should be withdrawn.
     * @param to            Account to which the money should be deposited.
     * @param amount        Transaction amount.
     * @param deductible    Whether transaction is tax-deductible.
     * @param balanced      Whether transaction has been balanced with bank
     *                      statement.
     */
    public EntryModel(Date date, String code, String description, int from,
            int to, double amount, boolean deductible, boolean balanced) {
        this.date = date;
        this.code = code;
        this.description = description;
        this.fromAccount = from;
        this.toAccount = to;
        this.amount = amount;
        this.deductible = deductible;
        this.balanced = balanced;
    }

    /**
     * Retrieves the transaction date.
     * 
     * @return Transaction date
     */
    public Date getDate() {
        return date;
    }
    
    /**
     * Retrieves the transaction date as a `String` value in the format 
     * MM/dd/yyyy.
     * 
     * @return Transaction date as string.
     */
    public String getDateAsString() {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        return sdf.format(this.date);
    }
    
    /**
     * Retrieves the transaction date as a `String` value in the provided format.
     * 
     * @param format    Format for the date, such as MMM dd, yyyy.
     * @return Transaction date as string.
     */
    public String getDateAsString(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(this.date);
    }

    /**
     * Sets the transaction date for the entry.
     * 
     * @param date Transaction date.
     */
    public void setDate(Date date) {
        this.date = date;
    }
    
    /**
     * Sets the transaction date from a `String` value, formatted as MM/dd/yyyy.
     * 
     * @param date  Transaction date in MM/dd/yyyy formatted string.
     * @throws ParseException In the event the date cannot be properly parsed.
     */
    public void setDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        
        this.date = sdf.parse(date);
    }
    
    /**
     * Sets the transaction date from a `String` value formatted in the same
     * manner as the format string provided.
     * 
     * @param format    Format of the provided date, such as MMM dd, yyyy.
     * @param date      The transaction date.
     * @throws ParseException In the event the date cannot be properly parsed.
     */
    public void setDate(String format, String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        
        this.date = sdf.parse(date);
    }

    /**
     * Retrieves the transaction code or check number of the transaction.
     * 
     * @return The transaction code or check number.
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the transaction code or check number for the transaction.
     * 
     * @param code Transaction code or check number.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Retrieves the transaction details or description.
     * 
     * @return Transaction details or description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the transaction details or description.
     * 
     * @param description Transaction details or description.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Retrieves the account from which the money for the transaction was 
     * withdrawn.
     * 
     * @return Debit account.
     */
    public int getFromAccount() {
        return fromAccount;
    }

    /**
     * Sets the account from which the money for the transaction should be
     * withdrawn.
     * 
     * @param fromAccount Debit account.
     */
    public void setFromAccount(int fromAccount) {
        this.fromAccount = fromAccount;
    }

    /**
     * Retrieves the account to which the money for the transaction was 
     * deposited.
     * 
     * @return Credit account.
     */
    public int getToAccount() {
        return toAccount;
    }

    /**
     * Sets the account to which the money for the transaction should be
     * deposited.
     * 
     * @param toAccount Credit account.
     */
    public void setToAccount(int toAccount) {
        this.toAccount = toAccount;
    }

    /**
     * Retrieves the amount of the transaction.
     * 
     * @return Transaction amount.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the transaction.
     * 
     * @param amount Transaction amount.
     */
    public void setAmount(double amount) {
        this.amount = amount;
    }

    /**
     * Determines whether or not the transaction amount is tax-deductible.
     * 
     * @return `true` if deductible, `false` if not.
     */
    public boolean isDeductible() {
        return deductible;
    }

    /**
     * Sets whether or not the transaction amount is tax-deductible.
     * 
     * @param deductible `true` if deductible, `false` if not.
     */
    public void setDeductible(boolean deductible) {
        this.deductible = deductible;
    }

    /**
     * Determines whether or not this transaction has been balanced with the
     * bank statement.
     * 
     * @return `true` if balanced, `false` if not.
     */
    public boolean isBalanced() {
        return balanced;
    }

    /**
     * Sets whether or not this transaction has been balanced with the bank
     * statement.
     * 
     * @param balanced `true` if balanced, `false` if not.
     */
    public void setBalanced(boolean balanced) {
        this.balanced = balanced;
    }
}
