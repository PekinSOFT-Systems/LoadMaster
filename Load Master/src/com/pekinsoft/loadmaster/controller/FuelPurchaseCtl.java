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
 *   Module:   FuelPurchaseCtl
 *   Created:  Oct 19, 2020
 *   Modified: Oct 19, 2020
 * 
 *   Purpose:
 *      Provides all data access for the Fuel Purchases journal.
 * 
 *   Revision History
 * 
 *   WHEN          BY                  REASON
 *   ------------  ------------------- ------------------------------------------
 *   Oct 21, 2020    Sean Carrick Initial Creation.
 *  ******************************************************************************
 */

package com.pekinsoft.loadmaster.controller;

import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.err.DataStoreException;
import com.pekinsoft.loadmaster.model.FuelPurchaseModel;
import com.pekinsoft.loadmaster.utils.MessageBox;
import com.pekinsoft.loadmaster.view.LoadMaster;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.1.0
 * @since 0.7.8 build 2549
 */
public class FuelPurchaseCtl {
    //<editor-fold defaultstate="collapsed" desc="Public Static Constants">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Member Fields">
    private final ArrayList<FuelPurchaseModel> records;
    private final File TABLE;
    
    private final LogRecord entry;
    
    private FuelPurchaseModel record;
    private int row;
    
    private boolean fileJustCreated;
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
    public FuelPurchaseCtl () throws DataStoreException {
        entry = new LogRecord(Level.ALL, "Logging initiated for ReceivablesCtl "
                + "class.");
        entry.setSourceClassName(this.getClass().getName());
        entry.setSourceMethodName("FuelPurchaseCtl (Constructor)");
        entry.setParameters(null);
        Starter.logger.enter(entry);
        
        fileJustCreated = false;
        
        records = new ArrayList<>();
        row = 0;
        
        TABLE = new File(Starter.props.getDataFolder() + "10040.jrnl");

        // Check to see if the table file exists:
        if ( !TABLE.exists() ) {
            try {
                TABLE.createNewFile();
                
                // Set our flag:
                fileJustCreated = true;
            } catch (IOException ex) {
                entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                        + "\nThrowing DataStoreException...");
                entry.setParameters(null);
                entry.setSourceMethodName("FuelPurchaseCtl (Constructor)");
                entry.setThrown(ex);
                Starter.logger.error(entry);
                
                throw new DataStoreException(ex.getMessage(), ex);
            }
        }
        
        if ( !fileJustCreated )
            connect();
//        else
//            MessageBox.showInfo("Data file was just now created.\n"
//                    + "Add records to it, then save, in order\n"
//                    + "to not see this message in the future.", 
//                    "New Data File Created");
        
        entry.setMessage("Done creating FuelPurchaseCtl object.");
        Starter.logger.exit(entry, null);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Static Methods">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Instance Methods">
    public void addNew(FuelPurchaseModel model) {
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
    public FuelPurchaseModel first() throws DataStoreException {
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
    public FuelPurchaseModel previous() throws DataStoreException {
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
    public FuelPurchaseModel next() throws DataStoreException {
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
    public FuelPurchaseModel last() throws DataStoreException {
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
    public FuelPurchaseModel get() {
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
    public FuelPurchaseModel get(int idx) {
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
    
    public void update(FuelPurchaseModel model) {
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
        
        BufferedReader in;
        
        entry.setMessage("Setting up LoadMaster.fileProgress...");
        entry.setParameters(null);
        Starter.logger.config(entry);
        
        LoadMaster.fileProgress.setMaximum(
                Starter.props.getPropertyAsInt("journal.fuel.records", "0") 
                + (Starter.props.getPropertyAsInt("journal.fuel.records", "0")));
        LoadMaster.fileProgress.setValue(0);
        LoadMaster.fileProgress.setVisible(true);
        
        try {
            in = new BufferedReader(new FileReader(TABLE));
            
            String line = in.readLine();
            
            while ( line != null ) {
                String[] record = line.split("~");
                
                createAndAddRecord(record);
                
                line = in.readLine();

                LoadMaster.fileProgress.setValue(
                        LoadMaster.fileProgress.getValue() + 1);
            }
            
            row = 0;    // Set our current row to the first record.
            
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
            LoadMaster.fileProgress.setValue(0);
            LoadMaster.fileProgress.setVisible(false);
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
    
    private String buildRecordLine(FuelPurchaseModel model) {
        return model.getIdAsString() + "~" + model.getDateAsString() + "~"
                + model.getOdometer() + "~" + model.getLocation()+ "~"
                + model.getGallonsOfDieselAsString()+ "~" 
                + String.valueOf(model.getPricePerGallonDiesel()) + "~" 
                + model.isDefPurchased() + "~" + model.getGallonsOfDefAsString()
                + "~" + String.valueOf(model.getPricePerGallonDef()) + "~"
                + model.getNotes();
    }
    
    private void createAndAddRecord(String[] line) {
        entry.setMessage("Entering...");
        entry.setSourceMethodName("createAndAddRecord");
        entry.setParameters(line);
        Starter.logger.enter(entry);
        
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        
//        record = new ReceivablesModel();
        
        
        try {
            record.setDate(sdf.parse(line[1]));
        } catch ( ParseException ex ) {
            entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                    + "Parsing error while parsing the dispatch date.");
            entry.setThrown(ex);
            entry.setSourceMethodName("createAndAddRecord");
            entry.setParameters(null);
            Starter.logger.error(entry);
            
            MessageBox.showError(ex, "Parsing Error");
        }
        
        record.setId(line[0]);
        record.setOdometer(line[2]);
        record.setLocation(line[3]);
        record.setGallonsOfDiesel(line[4]);
        record.setPricePerGallonOfDiesel(line[5]);
        record.setDefPurchased(Boolean.parseBoolean(line[6]));
        record.setGallonsOfDef(line[7]);
        record.setPricePerGallonDef(line[8]);
        record.setNotes(line[9]);
        
        records.add(record);
        
        LoadMaster.fileProgress.setValue(
                LoadMaster.fileProgress.getValue() + 1);
    }
    //</editor-fold>

}
