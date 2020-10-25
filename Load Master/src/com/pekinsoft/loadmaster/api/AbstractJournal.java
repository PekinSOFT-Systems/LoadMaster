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
 * Oct 23, 2020  Sean Carrick        Converted this class into a Generic so that
 *                                   extending classes will not need to do a lot
 *                                   of casting to access the records' member
 *                                   fields. Also, this is allowing the extending
 *                                   classes to only need to override thre (3)
 *                                   methods:
 *                                      - buildRecordLine(T)
 *                                      - createAndAddRecord(String[])
 *                                      - postTransactions()
 *                                   These changes have really cleaned up the
 *                                   code and the complexity, which should make
 *                                   maintaining the project a little easier in
 *                                   the future. Also added code folds.
 * *****************************************************************************
 */

package com.pekinsoft.loadmaster.api;

import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.err.DataStoreException;
import com.pekinsoft.loadmaster.view.LoadMaster;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.1.0
 * @param <T>
 * @since 0.1.0
 */
public abstract class AbstractJournal<T> {
    
    //<editor-fold defaultstate="collapsed" desc="Protected Fields">
    protected final File TABLE;
    
    protected ArrayList<T> records;
    protected final LogRecord entry;
    protected boolean fileJustCreated;
    protected T record;
    
    protected int row;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
    /**
     * Creates a new instance of the `AbstractJournal` class for accessing the
     * journal file for whichever account is extending this class.
     * 
     * @throws DataStoreException in the event that the database access fails.
     */
    public AbstractJournal(T obj, String pathToFile) throws DataStoreException {
        entry = new LogRecord(Level.ALL, "Logging started in the "
                + getClass().getCanonicalName());
        
        TABLE = new File(pathToFile);
        
        records = new ArrayList<>();
        record = obj;
        
        row = 0;
        fileJustCreated = createFileIfNecessary();
        
        if ( !fileJustCreated ) {
            connect();
        } else {
            entry.setMessage("Fuel Card Journal was just created.");
            entry.setSourceClassName(getClass().getCanonicalName());
            entry.setSourceMethodName(getClass().getName() + " (Constructor)");
            entry.setParameters(null);
            entry.setThrown(null);
            entry.setInstant(Instant.now());
            Starter.logger.info(entry);
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Abstract Method Declarations">
    /**
     * The `createAndAddRecord` method is the 1 of 3 methods that need to be
     * overridden for an AbstractJournal. This is due to the reason that using
     * reflection will not work within this method because there is no
     * guarantee in the order methods are provided via:
     * ```java
     * A a = new A();
     * Class<?> c = a.getClass();
     * Method methods[] = c.getDeclaredMethods();
     * ```
     * If there was a guarantee in the order the methods were returned using the
     * above code, then we could use reflection to implement this method within
     * `AbstractJournal`. However, until we figure out a way to ensure that the
     * data will be placed through the appropriate getter method, we will need
     * to keep this method abstract and require an implementing class to 
     * override it to provide for the proper storage of the data.
     * 
     * @param line from the data file, split into a `java.lang.String[]` array
     *             on the file's delimiter.
     */
    protected abstract void createAndAddRecord(String[] line);
    
    /**
     * The `buildRecordLine` method is the 1 of 3 methods that need to be
     * overridden for an AbstractJournal. This is due to the reason that using
     * reflection will not work within this method because there is no
     * guarantee in the order methods are provided via:
     * ```java
     * A a = new A();
     * Class<?> c = a.getClass();
     * Method methods[] = c.getDeclaredMethods();
     * ```
     * If there was a guarantee in the order the methods were returned using the
     * above code, then we could use reflection to implement this method within
     * `AbstractJournal`. However, until we figure out a way to ensure that the
     * data will be placed through the appropriate getter method, we will need
     * to keep this method abstract and require an implementing class to 
     * override it to provide for the proper storage of the data.
     *  
     * @param model the `T` object that contains the data being written to file.
     * @return a `java.lang.String` containing all of the data from the model,
     *         formatted into fields split by a delimiter, ready to be written
     *         out to the data file.
     */
    protected abstract String buildRecordLine(T model);
    
    /**
     * This method performs the task of posting all unposted transactions from
     * this journal to the General Ledger. 
     * <dl><dt>Developer's Note 1</dt><dd>This method should only be called when
     * the user clicks the &quot;Post&quot; task on the Accounting task pane, if
     * they have the batch method selected in the application settings dialog's
     * Accounting tab. If they do not have the batch method selected, this 
     * method should be called immediately upon any journal entries being made.
     * </dd></dl>
     * <dl><dt>Developer's Note 2</dt><dd>
     * The `postTransactions` method is the 1 of 3 methods that need to be
     * overridden for an AbstractJournal. This is due to the reason that using
     * reflection will not work within this method because there is no
     * guarantee in the order methods are provided via:
     * ```java
     * A a = new A();
     * Class<?> c = a.getClass();
     * Method methods[] = c.getDeclaredMethods();
     * ```
     * If there was a guarantee in the order the methods were returned using the
     * above code, then we could use reflection to implement this method within
     * `AbstractJournal`. However, until we figure out a way to ensure that the
     * data will be placed through the appropriate getter method, we will need
     * to keep this method abstract and require an implementing class to 
     * override it to provide for the proper storage of the data.</dd></dl>
     *  
     * @return  success: `true` if transactions posted, `false` otherwise.
     * @throws DataStoreException in the event there is an error accessing the
     *                            database.
     */
    public abstract boolean postTransactions() throws DataStoreException;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Private Methods">
    /**
     * Checks for the existence of the database file and creates it if it does
     * not yet exist.
     * 
     * @return success: `true` if successful, `false` on failure.
     */
    private boolean createFileIfNecessary() {
        boolean success = true;
        if ( !TABLE.exists() ) {
            try {
                TABLE.createNewFile();
            } catch (IOException ex) {
                entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                        + "\nThrowing DataStoreException...");
                entry.setParameters(null);
                entry.setSourceMethodName("createFileIfNecessary");
                entry.setThrown(ex);
                Starter.logger.error(entry);
                
                success = false;
            }
        }
        
        return success;
    }
    
    /**
     * Connects the AbstractJournal controller object to the database file for
     * its data.
     * 
     * @throws DataStoreException in the event that the database access fails.
     */
    private void connect() throws DataStoreException {
        entry.setMessage("Enter...");
        entry.setSourceMethodName("connect");
        entry.setParameters(new Object[]{});
        Starter.logger.enter(entry);
        
        BufferedReader in;
        
        entry.setMessage("Setting up LoadMaster.fileProgress...");
        entry.setParameters(null);
        Starter.logger.config(entry);
        
        if ( LoadMaster.fileProgress != null ) {
            LoadMaster.fileProgress.setMaximum(
                    Starter.props.getPropertyAsInt("journal.fuel.card.records", "0") 
                    + (Starter.props.getPropertyAsInt("journal.fuel.card.records", "0")));
            LoadMaster.fileProgress.setValue(0);
            LoadMaster.fileProgress.setVisible(true);
        }
        
        try {
            in = new BufferedReader(new FileReader(TABLE));
            
            String line = in.readLine();
            
            while ( line != null ) {
                String[] splitLine = line.split("~");
                
                createAndAddRecord(splitLine);
                
                line = in.readLine();

                if ( LoadMaster.fileProgress != null ) {
                    LoadMaster.fileProgress.setValue(
                            LoadMaster.fileProgress.getValue() + 1);
                }
            }
            
            in.close();
            
        } catch ( IOException ex ) {
            entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                    + "Throwing DataStoreException to calling method...");
            entry.setThrown(ex);
            entry.setSourceMethodName("connect");
            entry.setParameters(null);
            Starter.logger.error(entry);
            
            throw new DataStoreException(ex.getMessage(), ex);
        } finally {
            if ( LoadMaster.fileProgress != null ) {
                LoadMaster.fileProgress.setValue(0);
                LoadMaster.fileProgress.setVisible(false);
            }
            Starter.props.setPropertyAsInt("journal.fuel.card.records", records.size());
            Starter.props.flush();
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Protected Instance Methods">
    
    /**
     * Saves the transaction data from this model out to the database.
     * 
     * @throws DataStoreException in the event a error occurs accessing the 
     *                            database
     */
    protected void save() throws DataStoreException {
        BufferedWriter out;
        
        LoadMaster.fileProgress.setMaximum(
                Starter.props.getPropertyAsInt("journal.fuel.card.records", "0"));
        LoadMaster.fileProgress.setValue(
                Starter.props.getPropertyAsInt("journal.fuel.card.records", "0"));
        
        if ( TABLE.exists() ) {
            TABLE.delete();
            try {
                TABLE.createNewFile();
            } catch ( IOException ex ) {
                entry.setMessage("Something went wrong deleting and recreating "
                        + "the data table.");
                entry.setThrown(ex);
                entry.setSourceMethodName("save");
                entry.setParameters(null);
                Starter.logger.error(entry);
                
                throw new DataStoreException(ex.getMessage(), ex);
            }
        }
        
        try {
            out = new BufferedWriter(new FileWriter(TABLE));
            
            for ( int x = 0; x < records.size(); x++ ) {
                out.write(buildRecordLine(records.get(x)) + "\n");
                
                LoadMaster.fileProgress.setValue(
                        LoadMaster.fileProgress.getValue() - 1);
            }
            
            out.close();
        } catch ( IOException ex ) {
            entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                    + "Throwing DataStoreException to calling method...");
            entry.setThrown(ex);
            entry.setSourceMethodName("storeData");
            entry.setParameters(null);
            Starter.logger.error(entry);
            
            throw new DataStoreException(ex.getMessage(), ex);
        }
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Public Concrete Methods">
    /**
     * Closes the connection to the data store file. Prior to closing the 
     * connection, the records contained within this controller are saved back
     * to file.
     * 
     * @throws DataStoreException in the event an error occurs saving or closing
     *                            the data store.
     */
    public void close() throws DataStoreException {
        save();
    }
    
    /**
     * Provides a means of adding a new `T` object to the database.
     * 
     * @param model the new model to add.
     */
    public void addNew(T model) {
        records.add(model);
        row = getRecordCount() - 1;
    }
    
    /**
     * Moves the record pointer to the first transaction in this journal.
     * 
     * @return T The previous transaction record, if not at the first
     *                transaction in the journal, or `null` if an error occurs.
     * @throws DataStoreException in the event an error occurs while accessing
     *                       the journal
     */
    public T first() throws DataStoreException {
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
     * @return T The previous transaction record, if not at the first
     *                       transaction in the journal.
     * @throws DataStoreException in the event an error occurs while accessing
     *                       the journal
     */
    public T previous() throws DataStoreException {
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
     * @return T The previous transaction record, if not at the first
     *                       transaction in the journal.
     * @throws DataStoreException in the event an error occurs while accessing
     *                       the journal
     */
    public T next() throws DataStoreException {
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
     * @return T The previous transaction record, if not at the last
     *                       transaction in the journal.
     * @throws DataStoreException in the event an error occurs while accessing
     *                       the journal
     */
    public T last() throws DataStoreException {
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
     * Retrieves the current entry as an `T` object.
     * 
     * @return The current entry.
     */
    public T get() {
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
    public T get(int idx) {
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
     * Updates the current `T` with the one that is supplied.
     * 
     * @param model `T` with the changes made.
     */
    public void update(T model) {
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
    //</editor-fold>
}
