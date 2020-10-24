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
 * Created :   Oct 23, 2020
 * Modified:   Oct 23, 2020
 * 
 * Purpose:
 *     To provide a single, generic class for all data access for the various
 *     models that the program has. This is possible because we will use
 *     reflection in order to get and set data from and to the T type objects we
 *     will be working with.
 * 
 * WHEN          BY                  REASON
 * ------------  ------------------  -------------------------------------------
 * Oct 23, 2020  Sean Carrick        Initial creation.
 * 
 * *****************************************************************************
 */

package com.pekinsoft.loadmaster.api;

import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.controller.EntryCtl;
import com.pekinsoft.loadmaster.err.DataStoreException;
import com.pekinsoft.loadmaster.model.EntryModel;
import com.pekinsoft.loadmaster.view.LoadMaster;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 * `AbstractJournal` is a generic class for use in maintaining the data files
 * and objects for Load Master™. By using reflection, we will only have this one
 * class to maintain in the future for all data file access.
 * 
 * Though most people organize their data files in such a way that makes sense
 * to a person reading it in a text editor, Load Master™ will not need to do so.
 * As an example, a human would format the data file for a contact such as the
 * following, using an asterisk as a field delimiter:
 * ```
 * id*lastName*firstName*street*apartment*city*state*ZipCode*phoneNumber*email
 * ```
 * Since the computer is not human, and does not look at things the same way a
 * human does, we don't need to worry about the format, other than that the
 * system reads in the data in the same order as it was written out. In order to
 * accomplish this, `AbstractJournal` sorts the methods by name so that the
 * setter methods will be called in the same order as the getter methods.
 * Therefore, using the example above, the data file will be formatted as such:
 * ```
 * aprtment*city*email*first*id*last*phone*state*street*ZipCode
 * ```
 * Though this format is not easy for a human to read, it will work for the
 * system none-the-less.
 * 
 * <dl><dt>Developer's Note</dt><dd>By using this single class for accessing all
 * of the data files for Load Master™, we must require each model class to pass
 * the full path and file name to the data file for that model into the 
 * constructor for `AbstractJournal`. In this way, the generic, reflective class
 * will be able to open the file for input/output as needed.</dd>
 * <dl><dt>Developer's Note</dt><dd>Every model class for which you want to use
 * the `AbstractJournal` for data file access must implement the 
 * @see `JournalInterface` interface. This allows `AbstractJournal` to transfer
 * the data from the respective data file to the model, and from the model to 
 * the respective data file. By managing data transfer between the model and the
 * data file in this manner, the output of the data file will be organized in 
 * the manner directed by the developer.</dd></dl>
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.1.0
 * @since 0.1.0
 */
public class AbstractJournal {

    //<editor-fold defaultstate="collapsed" desc="Private Member Fields">
    // An ArrayList for all of the getter methods in the class type provided.
    private final ArrayList<Method> getters;
    // An ArrayList for all of the setter methods in the class type provided.
    private final ArrayList<Method> setters;
    // An ArrayList for all of the data records.
    private final ArrayList<JournalInterface> records;
    // A File object to hold the data file reference for use in this class.
    private final File dataFile;
    // A LogRecord for logging purposes.
    private final LogRecord entry;
    
    // A counter for the current row.
    private int row;
    
    // An individual record of the class type provided.
    private JournalInterface record;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
    public AbstractJournal (JournalInterface model, String filePath)
            throws DataStoreException {
        entry = new LogRecord(Level.ALL, "Initializing the AbstractJournal "
                + "class with the type: " + model.getClass().getCanonicalName());
        entry.setSourceClassName(getClass().getCanonicalName());
        entry.setSourceMethodName(getClass().getName());
        entry.setParameters(new Object[]{model, filePath});
        entry.setThrown(null);
        entry.setInstant(Instant.now());
        Starter.logger.enter(entry);
        this.record = model;
        
        getters = new ArrayList<>();
        setters = new ArrayList<>();
        records = new ArrayList<>();
        
        row = 0;
        
        dataFile = new File(filePath);
        
        connect();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Instance Methods">
    public void addNew(JournalInterface model) {
        records.add(model);
        row = getRecordCount() - 1;
        
        Starter.props.setPropertyAsInt("journal.fuel.records", getRecordCount());
    }
    
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
     * Moves the record pointer to the first transaction in this journal.
     * 
     * @return LoadModel The previous transaction record, if not at the first
     *                       transaction in the journal.
     * @throws DataStoreException in the event an error occurs while accessing
     *                       the journal
     */
    public JournalInterface first() throws DataStoreException {
        if ( row >= 0 ) {
            row = 0;
            
            try {
                record = records.get(row);
            } catch (IndexOutOfBoundsException ex) {
                record = null;
                throw new DataStoreException(ex.getMessage(), ex);
            }
        }
        
        return record;
    }
    
    /**
     * Moves the record pointer to the previous transaction in this journal.
     * 
     * @return LoadModel The previous transaction record, if not at the first
     *                       transaction in the journal.
     * @throws DataStoreException in the event an error occurs while accessing
     *                       the journal
     */
    public JournalInterface previous() throws DataStoreException {
        if ( row > 0 ) {
            row--;
            
            try {
                record = records.get(row);
            } catch (IndexOutOfBoundsException ex) {
                record = null;
                throw new DataStoreException(ex.getMessage(), ex);
            }
        }
        
        return record;
    }
    
    /**
     * Moves the record pointer to the next transaction in this journal.
     * 
     * @return LoadModel The previous transaction record, if not at the first
     *                       transaction in the journal.
     * @throws DataStoreException in the event an error occurs while accessing
     *                       the journal
     */
    public JournalInterface next() throws DataStoreException {
        if ( row < records.size() ) {
            row++;
            
            try {
                record = records.get(row);
            } catch (IndexOutOfBoundsException ex) {
                record = null;
                throw new DataStoreException(ex.getMessage(), ex);
            }
        }
        
        return record;
    }
    
    /**
     * Moves the record pointer to the last transaction in this journal.
     * 
     * @return LoadModel The previous transaction record, if not at the last
     *                       transaction in the journal.
     * @throws DataStoreException in the event an error occurs while accessing
     *                       the journal
     */
    public JournalInterface last() throws DataStoreException {
        if ( row < records.size() ) {
            row = records.size() - 1;
            
            try {
                record = records.get(row);
            } catch (IndexOutOfBoundsException ex) {
                record = null;
                throw new DataStoreException(ex.getMessage(), ex);
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
     * Retrieves the current entry as an `ReceivablesModel` object.
     * 
     * @return The current entry.
     */
    public JournalInterface get() {
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
    public JournalInterface get(int idx) {
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
     * Retrieves the total number of records (or rows) in this table.
     * 
     * @return int The number of records
     */
    public int getRecordCount() {
        return records.size();
    }
    
    public void update(JournalInterface model) {
        record = model;
        
        records.set(row, model);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Instance Methods">
    private void connect() throws DataStoreException {
        entry.setMessage("Enter...");
        entry.setSourceMethodName("connect");
        entry.setParameters(new Object[]{});
        Starter.logger.enter(entry);
        
        setUpProgressBar();
                
        try (BufferedReader in = new BufferedReader(new FileReader(dataFile))) {
            ArrayList<String> lines = new ArrayList<>();
            
            String line = in.readLine();
            
            while ( line != null ) {
                createAndAddRecord(line.split(line));
                
                line = in.readLine();

                advanceProgressBar();
            }
                    
            row = 0;    // Set our current row to the first record.
        } catch ( IOException ex ) {
            entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                    + "Throwing DataStoreException to calling method...");
            entry.setThrown(ex);
            entry.setSourceMethodName("connect");
            entry.setParameters(null);
            Starter.logger.error(entry);
            
            throw new DataStoreException(ex.getMessage(), ex);
        } finally {
            destroyProgressBar();
            Starter.props.setPropertyAsInt("journal.fuel.records", records.size());
            Starter.props.flush();
        }
    }
    
    private void save() throws DataStoreException {
        BufferedWriter out;
        
        LoadMaster.fileProgress.setMaximum(
                Starter.props.getPropertyAsInt("journal.fuel.records", "0"));
        LoadMaster.fileProgress.setValue(
                Starter.props.getPropertyAsInt("journal.fuel.records", "0"));
        
        if ( dataFile.exists() ) {
            dataFile.delete();
            try {
                dataFile.createNewFile();
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
            out = new BufferedWriter(new FileWriter(dataFile));
            
            for ( int x = 0; x < records.size(); x++ ) {
                out.write(records.get(x).buildRecordLine() + "\n");
                
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
    
    private void createAndAddRecord(String[] lines) {
        entry.setMessage("Entering...");
        entry.setSourceMethodName("createAndAddRecord");
        entry.setParameters(lines);
        Starter.logger.enter(entry);
        
        Constructor cons = null;
        try {
            cons = record.getClass().getDeclaredConstructor(String.class);
            cons.setAccessible(true);
            record = (JournalInterface)getConstructor().newInstance(lines);
        } catch ( InstantiationException | IllegalAccessException 
                | InvocationTargetException | NoSuchMethodException ex ) {
            entry.setMessage(ex.getLocalizedMessage());
            entry.setThrown(ex);
            entry.setSourceMethodName("createAndAddRecord");
            entry.setLevel(Level.WARNING);
            entry.setParameters(null);
            entry.setInstant(Instant.now());
            Starter.logger.error(entry);
        }
        
        record.load(lines);
        
        records.add(record);
        
        LoadMaster.fileProgress.setValue(
                LoadMaster.fileProgress.getValue() + 1);
        
        entry.setMessage("Completed creating and adding a record.");
        entry.setLevel(Level.FINE);
        entry.setThrown(null);
        entry.setInstant(Instant.now());
        Starter.logger.exit(entry, null);
    }

    private Constructor getConstructor() {
        Constructor cons = null;
        
        try {
            cons = record.getClass().getDeclaredConstructor(String.class);
            cons.setAccessible(true);        } catch ( Exception ex ) {
            entry.setMessage(ex.getLocalizedMessage());
            entry.setThrown(ex);
            entry.setSourceMethodName("getConstructor");
            entry.setLevel(Level.WARNING);
            entry.setParameters(null);
            entry.setInstant(Instant.now());
            Starter.logger.error(entry);
        }
        
        return cons;
    }
    
    private void setUpProgressBar() {
        if ( LoadMaster.fileProgress != null ) {
            entry.setMessage("Setting up LoadMaster.fileProgress...");
            entry.setParameters(null);
            Starter.logger.config(entry);

            LoadMaster.fileProgress.setMaximum(
                    Starter.props.getPropertyAsInt("journal.fuel.records", "0") 
                    + (Starter.props.getPropertyAsInt("journal.fuel.records", "0")));
            LoadMaster.fileProgress.setValue(0);
            LoadMaster.fileProgress.setVisible(true);
        }
    }
    
    private void advanceProgressBar() {
        if ( LoadMaster.fileProgress != null ) {
            LoadMaster.fileProgress.setValue(
                    LoadMaster.fileProgress.getValue() + 1);
        }
    }
    
    private void destroyProgressBar() {
        if ( LoadMaster.fileProgress != null ) {
            LoadMaster.fileProgress.setValue(0);
            LoadMaster.fileProgress.setVisible(false);
        }
    }
    
    private Date getDateFromString(String sDate) {
        entry.setSourceMethodName("getDateFromString");
        entry.setParameters(new Object[]{sDate});
        entry.setMessage("Parsing a java.util.Date from a java.lang.String");
        entry.setThrown(null);
        entry.setInstant(Instant.now());
        Starter.logger.enter(entry);
        
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date retDate = null;
        
        try {
            retDate = sdf.parse(sDate);
        } catch ( ParseException ex ) {
            entry.setMessage(ex.getLocalizedMessage());
            entry.setThrown(ex);
            entry.setInstant(Instant.now());
            Starter.logger.error(entry);
        }
        
        entry.setMessage("Done parsing date.");
        entry.setThrown(null);
        entry.setInstant(Instant.now());
        Starter.logger.exit(entry, retDate);
        return retDate;
    }

    public boolean postToGeneralLedger() throws DataStoreException {
        // For this, we are going to need to create an EntryCtl object, as well
        //+ as an EntryModel object.
        EntryCtl gl = new EntryCtl();

        // We also need a return value to send back to the calling method.
        boolean success = true; // Default to a successful posting procedure.
        
        // We need to loop through all of our journal entries, so we can enter
        //+ our diesel purchase into the General Ledger.
        for ( int x = 0; x < records.size(); x++ ) {
            // Check to see if the current record has not yet been posted.
            if ( !records.get(x).isPosted() ) {
                // Since the record has not yet been posted to the GL, we need 
                //+ to do so now.
                gl.addNew(records.get(x).getGeneralLedgerEntry());
            } else
                // No entries needed to be posted.
                success = false;
        } // Continue until all records have been read and posted.

        // Finally, we can post the transactions to the General Ledger.
        gl.close();

        // Leave as the last line of the method:
        return success;
    }
    //</editor-fold>

}
