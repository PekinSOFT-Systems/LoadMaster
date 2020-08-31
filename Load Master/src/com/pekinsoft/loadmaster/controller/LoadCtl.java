/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pekinsoft.loadmaster.controller;

import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.err.DataStoreException;
import com.pekinsoft.loadmaster.model.LoadModel;
import com.pekinsoft.loadmaster.utils.MessageBox;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class LoadCtl {
    //<editor-fold defaultstate="collapsed" desc="Public Static Constants">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Member Fields">
    // Table Data:
    private final File TABLE;
    
    // Table Information:
    private LoadModel load;
    private final ArrayList<LoadModel> records;
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
    public LoadCtl () throws DataStoreException {
        load = new LoadModel();
        TABLE = new File(Starter.DB_URL + "loads.tbl");
        
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
    
    public LoadModel get() {
        return records.get(row);
    }
    
    public LoadModel get(int idx) {
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
    
    
    public boolean hasNext() {
        return row < records.size();
    }
    
    /**
     * Moves the record pointer to the first record in this table.
     * 
     * @return LoadModel The previous customer record, if not at the first
     *                       record in the table.
     * @throws DataStoreException in the event an error occurs while accessing
     *                       the table
     */
    public LoadModel first() throws DataStoreException {
        if ( row >= 0 ) {
            row = 0;
            
            try {
                load = records.get(row);
            } catch (IndexOutOfBoundsException ex) {
                load = null;
                throw new DataStoreException(ex.getMessage(), ex);
            }
        }
        
        return load;
    }
    
    /**
     * Moves the record pointer to the last record in this table.
     * 
     * @return LoadModel The previous customer record, if not at the first
     *                       record in the table.
     * @throws DataStoreException in the event an error occurs while accessing
     *                       the table
     */
    public LoadModel last() throws DataStoreException {
        if ( row < getRecordCount() ) {
            row = getRecordCount() - 1;
            
            try {
                load = records.get(row);
            } catch (IndexOutOfBoundsException ex) {
                load = null;
                throw new DataStoreException(ex.getMessage(), ex);
            }
        }
        
        return load;
    }
    
    /**
     * Moves the record pointer to the next record in this table.
     * 
     * @return LoadModel The previous customer record, if not at the first
     *                       record in the table.
     * @throws DataStoreException in the event an error occurs while accessing
     *                       the table
     */
    public LoadModel next() throws DataStoreException {
        if ( row < getRecordCount() ) {
            row++;
            
            try {
                load = records.get(row);
            } catch (IndexOutOfBoundsException ex) {
                load = null;
                throw new DataStoreException(ex.getMessage(), ex);
            }
        }
        
        return load;
    }
    
    /**
     * Moves the record pointer to the previous record in this table.
     * 
     * @return LoadModel The previous customer record, if not at the first
     *                       record in the table.
     * @throws DataStoreException in the event an error occurs while accessing
     *                       the table
     */
    public LoadModel previous() throws DataStoreException {
        if ( row > 0 ) {
            row--;
            
            try {
                load = records.get(row);
            } catch (IndexOutOfBoundsException ex) {
                load = null;
                throw new DataStoreException(ex.getMessage(), ex);
            }
        }
        
        return load;
    }
    
    /**
     * Allows for the updating of an existing customer record.
     * 
     * @param cust The new data model to use to update the record.
     */
    private void update(LoadModel cust) {
            load = cust;
            
            records.set(row, load);
    }
    
    /**
     * Allows for the addition of new customers into the table.
     * 
     * @param cust The new customer record to add to the table
     */
    public void addNew(LoadModel cust) {
        records.add(cust);
        row = getRecordCount() - 1;
    }
    
    public void storeData() throws DataStoreException {
        BufferedWriter out;
        
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
        
        try {
            in = new BufferedReader(new FileReader(TABLE));
            
            String line = in.readLine();
            
            while ( line != null ) {
                String[] record = line.split("~");
                
                createAndAddRecord(record);
                
                line = in.readLine();
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
        }
    }
    
    private void createAndAddRecord(String[] record) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        
        load = new LoadModel();
        
        load.setOrder(record[0]);
        load.setTrip(record[1]);
        load.setStartOdo(Integer.valueOf(record[2]));
        load.setEndOdo(Integer.valueOf(record[3]));
        
        try {
            load.setDispatch(sdf.parse(record[4]));
        } catch ( ParseException ex ) {
            entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                    + "Parsing error while parsing the dispatch date.");
            entry.setThrown(ex);
            entry.setSourceMethodName("createAndAddRecord");
            entry.setParameters(null);
            Starter.logger.error(entry);
            
            MessageBox.showError(ex, "Parsing Error");
        }
        
        load.setRate(Double.valueOf(record[5]));
        load.setMiles(Integer.valueOf(record[6]));
        load.setWeight(Integer.valueOf(record[7]));
        load.setCommodity(record[8]);
        load.setHazMat(Boolean.parseBoolean(record[9]));
        load.setTarped(Boolean.parseBoolean(record[10]));
        load.setTwic(Boolean.parseBoolean(record[11]));
        load.setTopCust(Boolean.parseBoolean(record[12]));
        load.setLtl(Boolean.parseBoolean(record[13]));
        load.setCbd(Boolean.parseBoolean(record[14]));
        load.setRamps(Boolean.parseBoolean(record[15]));
        load.setBroker(Long.valueOf(record[16]));
        load.setBol(record[17]);
        load.setAvgRPM(Double.valueOf(record[18]));
        load.setCompleted(Boolean.parseBoolean(record[19]));
        load.setCanelled(Boolean.parseBoolean(record[20]));
        
        records.add(load);
    }
    
    private String buildRecordLine(LoadModel model) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        
        return model.getOrder() + "~" + model.getTrip() + "~" 
                + model.getStartOdo() + "~" + model.getEndOdo() + "~" 
                + sdf.format(model.getDispatch()) + "~" + model.getRate()
                + "~" + model.getMiles() + "~" + model.getWeight() + "~" 
                + "~" + model.getCommodity() + "~" + model.isHazMat() + "~" 
                + model.isTarped()+ "~" + model.isTwic()+ "~" + model.isTopCust()
                + "~" + model.isLtl()+ "~" + model.isCbd()+ "~" + model.isRamps()
                + "~" + model.getBroker() + "~" + model.getBol() + "~" 
                + model.getAvgRPM() + "~" + model.isCompleted() + "~" 
                + model.isCanelled();
    }
    //</editor-fold>


}