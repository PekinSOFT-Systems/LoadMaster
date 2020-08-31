/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pekinsoft.loadmaster.controller;

import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.err.DataStoreException;
import com.pekinsoft.loadmaster.model.BrokerModel;
import com.pekinsoft.loadmaster.view.LoadMaster;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.LogRecord;
import java.util.logging.Level;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.1.0
 * @since 0.1.0
 */
public class BrokerCtl {
    //<editor-fold defaultstate="collapsed" desc="Public Static Constants">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Member Fields">
    // Table Data:
    private final File TABLE;
    
    // Table Information:
    private BrokerModel broker;
    private final ArrayList<BrokerModel> records;
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
    public BrokerCtl () throws DataStoreException {
        broker = new BrokerModel();
        TABLE = new File(Starter.DB_URL + "brokers.tbl");
        
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
                entry.setSourceMethodName("BrokerCtl");
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
     * Retrieves the current broker as a `BrokerModel` object.
     * 
     * @return The current broker record.
     */
    public BrokerModel get() {
        return records.get(row);
    }
    
    /**
     * Retrieves the broker at the specified index as a `BrokerModel` object. If
     * the index provided is not valid, returns `null`.
     * 
     * @param idx   The index of the broker to retrieve.
     * @return      The specified broker. If the specified index is invalid
     *              (i.e., less than zero or greater than `getRecordCount()`,
     *              `null` is returned.
     */
    public BrokerModel get(int idx) {
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
     * Provides a means of determining whether the data table contains at least
     * one more record after the current record.
     * 
     * @return  `true` if there is at least one more record in the table, 
     *          `false` otherwise.
     */
    public boolean hasNext() {
        return row < records.size();
    }
    
    /**
     * Moves the record pointer to the first record in this table.
     * 
     * @return BrokerModel The previous customer record, if not at the first
     *                       record in the table.
     * @throws DataStoreException in the event an error occurs while accessing
     *                       the table
     */
    public BrokerModel first() throws DataStoreException {
        if ( row >= 0 ) {
            row = 0;
            
            try {
                broker = records.get(row);
            } catch (IndexOutOfBoundsException ex) {
                broker = null;
                throw new DataStoreException(ex.getMessage(), ex);
            }
        }
        
        return broker;
    }
    
    /**
     * Moves the record pointer to the last record in this table.
     * 
     * @return BrokerModel The previous customer record, if not at the first
     *                       record in the table.
     * @throws DataStoreException in the event an error occurs while accessing
     *                       the table
     */
    public BrokerModel last() throws DataStoreException {
        if ( row < getRecordCount() ) {
            row = getRecordCount() - 1;
            
            try {
                broker = records.get(row);
            } catch (IndexOutOfBoundsException ex) {
                broker = null;
                throw new DataStoreException(ex.getMessage(), ex);
            }
        }
        
        return broker;
    }
    
    /**
     * Moves the record pointer to the next record in this table.
     * 
     * @return BrokerModel The previous customer record, if not at the first
     *                       record in the table.
     * @throws DataStoreException in the event an error occurs while accessing
     *                       the table
     */
    public BrokerModel next() throws DataStoreException {
        if ( row < getRecordCount() ) {
            row++;
            
            try {
                broker = records.get(row);
            } catch (IndexOutOfBoundsException ex) {
                broker = null;
                throw new DataStoreException(ex.getMessage(), ex);
            }
        }
        
        return broker;
    }
    
    /**
     * Moves the record pointer to the previous record in this table.
     * 
     * @return BrokerModel The previous customer record, if not at the first
     *                       record in the table.
     * @throws DataStoreException in the event an error occurs while accessing
     *                       the table
     */
    public BrokerModel previous() throws DataStoreException {
        if ( row > 0 ) {
            row--;
            
            try {
                broker = records.get(row);
            } catch (IndexOutOfBoundsException ex) {
                broker = null;
                throw new DataStoreException(ex.getMessage(), ex);
            }
        }
        
        return broker;
    }
    
    /**
     * Allows for the updating of an existing customer record.
     * 
     * @param cust The new data model to use to update the record.
     */
    private void update(BrokerModel cust) {
            broker = cust;
            
            records.set(row, broker);
    }
    
    /**
     * Allows for the addition of new customers into the table.
     * 
     * @param cust The new customer record to add to the table
     */
    public void addNew(BrokerModel cust) {
        records.add(cust);
        row = getRecordCount() - 1;
        
        Starter.props.setPropertyAsInt("table.brokers.records",
                getRecordCount());
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
                entry.setMessage("Something went wrong deleting and recreating the data table.");
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Instance Methods">
    private void connect() throws DataStoreException {
        BufferedReader in;
        LoadMaster.fileProgress.setMinimum(0);
        LoadMaster.fileProgress.setMaximum(
                Starter.props.getPropertyAsInt("table.brokers.records", "0"));
//        LoadMaster.fileProgress.setVisible(true);
        
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
        broker = new BrokerModel();
        broker.setId(Long.parseLong(record[0]));
        broker.setCompany(record[1]);
        broker.setStreet(record[2]);
        broker.setSuite(record[3]);
        broker.setCity(record[4]);
        broker.setState(record[5]);
        broker.setZip(record[6]);
        broker.setContact(record[7]);
        broker.setPhone(record[8]);
        broker.setFax(record[9]);
        broker.setEmail(record[10]);
        
        records.add(broker);
    }
    
    private String buildRecordLine(BrokerModel model) {
        return model.getId() + "~" + model.getCompany() + "~"
                + model.getStreet() + "~" + model.getSuite() + "~"
                + model.getCity() + "~" + model.getState() + "~"
                + model.getZip() + "~" + model.getContact() + "~"
                + model.getPhone() + "~" + model.getFax() + "~"
                + model.getEmail();
    }
    //</editor-fold>


}
