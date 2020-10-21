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
 *   Module:   CustomerCtl
 *   Created:  Sep 06, 2020
 *   Modified: Sep 06, 2020
 * 
 *   Purpose:
 *      Provides all data access functionality for the customers table.
 * 
 *   Revision History
 * 
 *   WHEN          BY                  REASON
 *   ------------  ------------------- ------------------------------------------
 *   Sep 19, 2020  Sean Carrick        Initial Creation.
 *  ******************************************************************************
 */

package com.pekinsoft.loadmaster.controller;

import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.err.DataStoreException;
import com.pekinsoft.loadmaster.model.CustomerModel;
import com.pekinsoft.loadmaster.view.LoadMaster;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
public class CustomerCtl {
    //<editor-fold defaultstate="collapsed" desc="Public Static Constants">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Member Fields">
    // Table Data:
    private final File TABLE;
    
    // Table Information:
    private CustomerModel customer;
    private final ArrayList<CustomerModel> records;
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
    public CustomerCtl () throws DataStoreException {
        customer = new CustomerModel();
        TABLE = new File(Starter.DB_URL + "customers.tbl");
        
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
     * Retrieves the current customer as a `CustomerModel` object.
     * 
     * @return The current customer.
     */
    public CustomerModel get() {
        return records.get(row);
    }
    
    /**
     * Retrieves the customer at the specified index. If the provided index is
     * invalid, returns `null`.
     * 
     * @param idx   The index of the customer to retrieve.
     * @return      The customer at the specified index. If the specified index
     *              is invalid (i.e., less than zero or greater than
     *              `getRecordCount()`), then `null` is returned.
     */
    public CustomerModel get(int idx) {
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
    
    
    public boolean hasNext() {
        return row < records.size();
    }
    
    /**
     * Moves the record pointer to the first record in this table.
     * 
     * @return CustomerModel The previous customer record, if not at the first
     *                       record in the table.
     * @throws DataStoreException in the event an error occurs while accessing
     *                       the table
     */
    public CustomerModel first() throws DataStoreException {
        if ( row >= 0 ) {
            row = 0;
            
            try {
                customer = records.get(row);
            } catch (IndexOutOfBoundsException ex) {
                customer = null;
                throw new DataStoreException(ex.getMessage(), ex);
            }
        }
        
        return customer;
    }
    
    /**
     * Moves the record pointer to the last record in this table.
     * 
     * @return CustomerModel The previous customer record, if not at the first
     *                       record in the table.
     * @throws DataStoreException in the event an error occurs while accessing
     *                       the table
     */
    public CustomerModel last() throws DataStoreException {
        if ( row < getRecordCount() ) {
            row = getRecordCount() - 1;
            
            try {
                customer = records.get(row);
            } catch (IndexOutOfBoundsException ex) {
                customer = null;
                throw new DataStoreException(ex.getMessage(), ex);
            }
        }
        
        return customer;
    }
    
    /**
     * Moves the record pointer to the next record in this table.
     * 
     * @return CustomerModel The previous customer record, if not at the first
     *                       record in the table.
     * @throws DataStoreException in the event an error occurs while accessing
     *                       the table
     */
    public CustomerModel next() throws DataStoreException {
        if ( row < getRecordCount() ) {
            row++;
            
            try {
                customer = records.get(row);
            } catch (IndexOutOfBoundsException ex) {
                customer = null;
                throw new DataStoreException(ex.getMessage(), ex);
            }
        }
        
        return customer;
    }
    
    /**
     * Moves the record pointer to the previous record in this table.
     * 
     * @return CustomerModel The previous customer record, if not at the first
     *                       record in the table.
     * @throws DataStoreException in the event an error occurs while accessing
     *                       the table
     */
    public CustomerModel previous() throws DataStoreException {
        if ( row > 0 ) {
            row--;
            
            try {
                customer = records.get(row);
            } catch (IndexOutOfBoundsException ex) {
                customer = null;
                throw new DataStoreException(ex.getMessage(), ex);
            }
        }
        
        return customer;
    }
    
    /**
     * Allows for the updating of an existing customer record.
     * 
     * @param cust The new data model to use to update the record.
     */
    public void update(CustomerModel cust) {
            customer = cust;
            
            records.set(row, customer);
    }
    
    /**
     * Allows for the addition of new customers into the table.
     * 
     * @param cust The new customer record to add to the table
     */
    public void addNew(CustomerModel cust) {
        records.add(cust);
        row = getRecordCount() - 1;
        
        Starter.props.setPropertyAsInt("table.customers.records",
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
                Starter.props.getPropertyAsInt("table.customers.records", "0"));
        LoadMaster.loadProgress.setValue(
                Starter.props.getPropertyAsInt("table.customers.records", "0"));
        
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
                Starter.props.getPropertyAsInt("table.customers.records", "0"));
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
        customer = new CustomerModel();
        customer.setId(Long.parseLong(record[0]));
        customer.setCompany(record[1]);
        customer.setStreet(record[2]);
        customer.setSuite(record[3]);
        customer.setCity(record[4]);
        customer.setState(record[5]);
        customer.setZip(record[6]);
        customer.setContact(record[7]);
        customer.setPhone(record[8]);
        customer.setComments(record[9]);
        
        records.add(customer);
    }
    
    private String buildRecordLine(CustomerModel model) {
        return model.getId() + "~" + model.getCompany() + "~"
                + model.getStreet() + "~" + model.getSuite() + "~"
                + model.getCity() + "~" + model.getState() + "~"
                + model.getZip() + "~" + model.getContact() + "~"
                + model.getPhone() + "~" + model.getComments();
    }
    //</editor-fold>


}
