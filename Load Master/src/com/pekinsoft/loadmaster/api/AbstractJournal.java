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
 * Class   :   AbstractJournal
 * Author  :   Sean Carrick
 * Created :   Oct 22, 2020
 * Modified:   Oct 22, 2020
 * 
 * Purpose:
 *     Provides the framework for all Journal controller classes in Load Master™.
 * 
 * WHEN          BY                  REASON
 * ------------  ------------------  -------------------------------------------
 * Oct 22, 2020  Sean Carrick        Initial creation.
 * 
 * *****************************************************************************
 */

package com.pekinsoft.loadmaster.api;

import com.pekinsoft.loadmaster.err.DataStoreException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.1.0
 * @since 0.1.0
 */
public abstract class AbstractJournal {

    protected ArrayList<Object> records;
    protected final LogRecord entry;
    protected boolean fileJustCreated;
    protected Object record;
    
    protected int row;
    
    /**
     * Creates a new instance of the `AbstractJournal` class for accessing the
     * journal file for whichever account is extending this class.
     * 
     * @throws DataStoreException in the event that the database access fails.
     */
    public AbstractJournal() throws DataStoreException {
        entry = new LogRecord(Level.ALL, "Logging started in the "
                + getClass().getCanonicalName());
        
        records = new ArrayList<>();
        record = new Object();
        
        row = 0;
        fileJustCreated = false;
        
        
    }
    
    /**
     * Checks for the existence of the database file and creates it if it does
     * not yet exist.
     * 
     * @return success: `true` if successful, `false` on failure.
     * @throws IOException 
     */
    protected abstract boolean createFileIfNecessary();
    
    /**
     * Connects the AbstractJournal controller object to the database file for
     * its data.
     * 
     * @throws DataStoreException in the event that the database access fails.
     */
    protected abstract void connect() throws DataStoreException;
    
    /**
     * Closes the connection to the data store file. Prior to closing the 
     * connection, the records contained within this controller are saved back
     * to file.
     * 
     * @throws DataStoreException in the event an error occurs saving or closing
     *                            the data store.
     */
    protected abstract void close() throws DataStoreException;
    
    /**
     * Saves the transaction data from this model out to the database.
     * 
     * @throws DataStoreException in the event a error occurs accessing the 
     *                            database
     */
    protected abstract void save() throws DataStoreException;
    
    /**
     * This method performs the task of posting all unposted transactions from
     * this journal to the General Ledger. 
     * <dl><dt>Developer's Note</dt><dd>This method should only be called when
     * the user clicks the &quot;Post&quot; task on the Accounting task pane, if
     * they have the batch method selected in the application settings dialog's
     * Accounting tab. If they do not have the batch method selected, this 
     * method should be called immediately upon any journal entries being made.
     * </dd></dl>
     * 
     * @return  success: `true` if transactions posted, `false` otherwise.
     * @throws DataStoreException in the event there is an error accessing the
     *                            database.
     */
    public abstract boolean postTransactions() throws DataStoreException;
    
    /**
     * Provides a means of adding a new `Object` object to the database.
     * 
     * @param model the new model to add.
     */
    public void addNew(Object model) {
        records.add(model);
        row = getRecordCount() - 1;
    }
    
    /**
     * Moves the record pointer to the first transaction in this journal.
     * 
     * @return Object The previous transaction record, if not at the first
     *                transaction in the journal, or `null` if an error occurs.
     * @throws DataStoreException in the event an error occurs while accessing
     *                       the journal
     */
    public Object first() throws DataStoreException {
        if ( row > 0 ) {
            row = 0;
            
            try {
                record = records.get(row);
            } catch ( IndexOutOfBoundsException ex ){
                record = null;
                throw new DataStoreException("Error occurred navigating records."
                        + "\n\n" + ex.getMessage(), ex);
            }
        }
        
        return record;
    }
    
    /**
     * Moves the record pointer to the previous transaction in this journal.
     * 
     * @return Object The previous transaction record, if not at the first
     *                       transaction in the journal.
     * @throws DataStoreException in the event an error occurs while accessing
     *                       the journal
     */
    public Object previous() throws DataStoreException {
        if ( row > 0 ) {
            row--;
            
            try {
                record = records.get(row);
            } catch (IndexOutOfBoundsException ex) {
                record = null;
                throw new DataStoreException("Error occurred navigating records."
                        + "\n\n" + ex.getMessage(), ex);
            }
        }
        
        return record;
    }
    
    /**
     * Moves the record pointer to the next transaction in this journal.
     * 
     * @return Object The previous transaction record, if not at the first
     *                       transaction in the journal.
     * @throws DataStoreException in the event an error occurs while accessing
     *                       the journal
     */
    public Object next() throws DataStoreException {
        if ( row < records.size() ) {
            row++;
            
            try {
                record = records.get(row);
            } catch (IndexOutOfBoundsException ex) {
                record = null;
                throw new DataStoreException("Error occurred navigating records."
                        + "\n\n" + ex.getMessage(), ex);
            }
        }
        
        return record;
    }
    
    /**
     * Moves the record pointer to the last transaction in this journal.
     * 
     * @return Object The previous transaction record, if not at the last
     *                       transaction in the journal.
     * @throws DataStoreException in the event an error occurs while accessing
     *                       the journal
     */
    public Object last() throws DataStoreException {
        if ( row < records.size() ) {
            row = records.size() - 1;
            
            try {
                record = records.get(row);
            } catch (IndexOutOfBoundsException ex) {
                record = null;
                throw new DataStoreException("Error occurred navigating records."
                        + "\n\n" + ex.getMessage(), ex);
            }
        }
        
        return record;
    }
    
    /**
     * Determines whether or not there are more transactions in this journal.
     * 
     * @return `true` if more transactions, `false` if not.
     */
    public boolean hasNext() {
        return row < records.size();
    }
    
    /**
     * Retrieves the current entry as an `Object` object.
     * 
     * @return The current entry.
     */
    public Object get() {
        return records.get(row);
    }
    
    /**
     * Retrieves the entry at the specified index. If the specified index is
     * invalid, returns `null`.
     * 
     * @param idx   The specified index from which to retrieve the entry.
     * @return      The entry at the specified index. If the specified index is 
     *              invalid (i.e., less than zero or greater than
     *              `getRecordCount()`), null is returned.
     */
    public Object get(int idx) {
        return records.get(idx);
    }
    
    /**
     * Retrieves the current record number of the record in this journal.
     * 
     * @return int The current record number
     */
    public int getCurrentRecordNumber() {
        return row + 1;
    }
    
    /**
     * Updates the current `Object` with the one that is supplied.
     * 
     * @param model `Object` with the changes made.
     */
    public void update(Object model) {
        record = model;
        
        records.set(row, model);
    }
    
    /**
     * Retrieves the total number of records (or rows) in this table.
     * 
     * @return int The number of records
     */
    public int getRecordCount() {
        return records.size();
    }
}
