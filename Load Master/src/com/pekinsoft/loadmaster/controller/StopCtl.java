/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pekinsoft.loadmaster.controller;

import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.err.DataStoreException;
import com.pekinsoft.loadmaster.err.InvalidTimeException;
import com.pekinsoft.loadmaster.model.StopModel;
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
import java.util.Iterator;
import java.util.logging.LogRecord;
import java.util.logging.Level;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.1.0
 * @since 0.1.0
 */
public class StopCtl {
    //<editor-fold defaultstate="collapsed" desc="Public Static Constants">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Member Fields">
    // Table Data:
    private final File TABLE;
    
    // Table Information:
    private StopModel stop;
    private final ArrayList<StopModel> records;
    private int row;
    
    // System:
    private LogRecord entry;
    
    // Flags:
    private boolean fileJustCreated = false;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Static Initializer">
    static {
        
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Intstance Initializer">
    {
        records = new ArrayList<>();
        row = 0;
        entry = new LogRecord(Level.FINEST, "");
        entry.setSourceClassName(this.getClass().getCanonicalName());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
    public StopCtl () throws DataStoreException {
        stop = new StopModel();
        TABLE = new File(Starter.DB_URL + "stops.tbl");
        
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
                entry.setSourceMethodName("Customer");
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
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Static Methods">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Instance Methods">
    /**
     * Retrieves the current stop as a `StopModel` object.
     * 
     * @return Current stop.
     */
    public StopModel get() {
        return records.get(row);
    }
    
    /**
     * Retrieves the specified stop as a `StopModel` object.
     * 
     * @param idx   The index of the stop to retrieve. If the index is invalid
     *              (i.e., below zero (0) or greater than `getRecordCount()`),
     *              `null` is returned.
     * @return      The specified stop at the index provided or `null`.
     */
    public StopModel get(int idx) {
        if ( idx < 0 || idx >= getRecordCount() ) 
            return null;
        
        return records.get(idx);
    }
    
    /**
     * Retrieves the total number of records (or rows) in this table.
     * 
     * @return int The number of records
     */
    public int getRecordCount() {
        return records.size();
    }
    
    /**
     * Retrieves the current record number of the record in this table.
     * 
     * @return int The current record number
     */
    public int getCurrentRecordNumber() {
        return row + 1;
    }
    
    /**
     * Provides a means of determining if this table has another record beyond
     * the current record.
     * 
     * @return `true` if there is at least one more record, `false` otherwise.
     */
    public boolean hasNext() {
        return row < records.size();
    }
    
    /**
     * Moves the record pointer to the first record in this table.
     * 
     * @return StopModel The previous stop record, if not at the first
     *                       record in the table.
     * @throws DataStoreException in the event an error occurs while accessing
     *                       the table
     */
    public StopModel first() throws DataStoreException {
        if ( row >= 0 ) {
            row = 0;
            
            try {
                stop = records.get(row);
            } catch (IndexOutOfBoundsException ex) {
                stop = null;
                throw new DataStoreException(ex.getMessage(), ex);
            }
        }
        
        return stop;
    }
    
    /**
     * Moves the record pointer to the last record in this table.
     * 
     * @return StopModel The previous stop record, if not at the first
     *                       record in the table.
     * @throws DataStoreException in the event an error occurs while accessing
     *                       the table
     */
    public StopModel last() throws DataStoreException {
        if ( row < getRecordCount() ) {
            row = getRecordCount() - 1;
            
            try {
                stop = records.get(row);
            } catch (IndexOutOfBoundsException ex) {
                stop = null;
                throw new DataStoreException(ex.getMessage(), ex);
            }
        }
        
        return stop;
    }
    
    /**
     * Moves the record pointer to the next record in this table.
     * 
     * @return StopModel The previous stop record, if not at the first
     *                       record in the table.
     * @throws DataStoreException in the event an error occurs while accessing
     *                       the table
     */
    public StopModel next() throws DataStoreException {
        if ( row < getRecordCount() ) {
            row++;
            
            try {
                stop = records.get(row);
            } catch (IndexOutOfBoundsException ex) {
                stop = null;
                throw new DataStoreException(ex.getMessage(), ex);
            }
        }
        
        return stop;
    }
    
    /**
     * Moves the record pointer to the previous record in this table.
     * 
     * @return StopModel The previous stop record, if not at the first
     *                       record in the table.
     * @throws DataStoreException in the event an error occurs while accessing
     *                       the table
     */
    public StopModel previous() throws DataStoreException {
        if ( row > 0 ) {
            row--;
            
            try {
                stop = records.get(row);
            } catch (IndexOutOfBoundsException ex) {
                stop = null;
                throw new DataStoreException(ex.getMessage(), ex);
            }
        }
        
        return stop;
    }
    
    /**
     * Allows for the updating of an existing stop record.
     * 
     * @param cust The new data model to use to update the record.
     */
    private void update(StopModel cust) {
            stop = cust;
            
            records.set(row, stop);
    }
    
    /**
     * Allows for the addition of new stops into the table.
     * 
     * @param cust The new stop record to add to the table
     */
    public void addNew(StopModel cust) {
        records.add(cust);
        row = getRecordCount() - 1;
        
        Starter.props.setPropertyAsInt("table.stops.records", getRecordCount());
    }
    
    /**
     * Writes the data out to the table data file.
     * 
     * @throws DataStoreException In the event there is an error writing the
     *                            data.
     */
    public void storeData() throws DataStoreException {
        BufferedWriter out;
        
        LoadMaster.loadProgress.setMaximum(
                Starter.props.getPropertyAsInt("table.stops.records", "0"));
        LoadMaster.loadProgress.setValue(
                Starter.props.getPropertyAsInt("table.stops.records", "0"));
        
        if ( TABLE.exists() ) {
            TABLE.delete();
            try {
                TABLE.createNewFile();
            } catch ( IOException ex ) {
                entry.setMessage("Something went wrong deleting and recreating "
                        + "the data table.");
                entry.setThrown(ex);
                entry.setSourceMethodName("storeData");
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
    
    
    public ArrayList<StopModel> getList() {
        return records;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Instance Methods">
    private void connect() throws DataStoreException {
        BufferedReader in;
        
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
//            LoadMaster.fileProgress.setValue(0);
        }
    }
    
    private void createAndAddRecord(String[] record) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        
        stop = new StopModel();
        
        stop.setTripNumber(record[0]);
        stop.setStopNumber(Integer.parseInt(record[1]));
        stop.setCustomer(Long.parseLong(record[2]));
        
        try {
            stop.setEarlyDate(sdf.parse(record[3]));
        } catch ( ParseException ex ) {
            entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                    + "Parsing error while parsing the early date.");
            entry.setThrown(ex);
            entry.setSourceMethodName("createAndAddRecord");
            entry.setParameters(null);
            Starter.logger.error(entry);
            
            MessageBox.showError(ex, "Data Retrieval Error");
        }
        
        try {
            stop.setEarlyTime(record[4]);
        } catch ( InvalidTimeException | ParseException ex ) {
            entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                    + "Parsing error while parsing the late date.");
            entry.setThrown(ex);
            entry.setSourceMethodName("createAndAddRecord");
            entry.setParameters(null);
            Starter.logger.error(entry);
            
            MessageBox.showError(ex, "Data Retrieval Error");            
        }
        
        try {
            stop.setLateDate(sdf.parse(record[5]));
        } catch ( ParseException ex ) {
            entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                    + "Parsing error while parsing the late date.");
            entry.setThrown(ex);
            entry.setSourceMethodName("createAndAddRecord");
            entry.setParameters(null);
            Starter.logger.error(entry);
            
            MessageBox.showError(ex, "Data Retrieval Error");
        }
        
        try {
            stop.setLateTime(record[6]);
        } catch ( InvalidTimeException | ParseException ex ) {
            entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                    + "Parsing error while parsing the late date.");
            entry.setThrown(ex);
            entry.setSourceMethodName("createAndAddRecord");
            entry.setParameters(null);
            Starter.logger.error(entry);
            
            MessageBox.showError(ex, "Data Retrieval Error");            
        }
        
        try {
            stop.setArrDate(sdf.parse(record[7]));
        } catch ( ParseException ex ) {
            entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                    + "Parsing error while parsing the late date.");
            entry.setThrown(ex);
            entry.setSourceMethodName("createAndAddRecord");
            entry.setParameters(null);
            Starter.logger.error(entry);
            
            MessageBox.showError(ex, "Data Retrieval Error");
        }
        
        try {
            stop.setArrTime(record[8]);
        } catch ( InvalidTimeException | ParseException ex ) {
            entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                    + "Parsing error while parsing the late date.");
            entry.setThrown(ex);
            entry.setSourceMethodName("createAndAddRecord");
            entry.setParameters(null);
            Starter.logger.error(entry);
            
            MessageBox.showError(ex, "Data Retrieval Error");            
        }
        
        try {
            stop.setDepDate(sdf.parse(record[9]));
        } catch ( ParseException ex ) {
            entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                    + "Parsing error while parsing the late date.");
            entry.setThrown(ex);
            entry.setSourceMethodName("createAndAddRecord");
            entry.setParameters(null);
            Starter.logger.error(entry);
            
            MessageBox.showError(ex, "Data Retrieval Error");
        }
        
        try {
            stop.setDepTime(record[10]);
        } catch ( InvalidTimeException | ParseException ex ) {
            entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                    + "Parsing error while parsing the late date.");
            entry.setThrown(ex);
            entry.setSourceMethodName("createAndAddRecord");
            entry.setParameters(null);
            Starter.logger.error(entry);
            
            MessageBox.showError(ex, "Data Retrieval Error");            
        }
        
        stop.setSignedBy(record[11]);
        
        records.add(stop);
    }
    
    private String buildRecordLine(StopModel model) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        
        return model.getTripNumber() + "~" + model.getStopNumber() + "~"
                + model.getCustomer() + "~" + sdf.format(model.getEarlyDate())
                 + "~" + model.getEarlyTime() + "~" 
                + sdf.format(model.getLateDate()) + "~" + model.getLateTime();
    }
    //</editor-fold>


}
