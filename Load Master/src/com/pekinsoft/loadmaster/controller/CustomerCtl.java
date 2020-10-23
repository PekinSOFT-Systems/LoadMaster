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
     * Provides a method of retrieving the record for the specified company.
     * This method may be used to find that specific company for multiple 
     * purposes, including to make sure the user is not attempting to enter the 
     * same company record multiple times. 
     * <dl><dt>Note</dt><dd>It is a best practice to call this method prior to
     * calling the `addNew(BrokerModel)` method as a means of ensuring each 
     * broker is only entered into the database a single time.</dd></dl>
     * <dl><dt>Developer's Note</dt><dd>If a `javax.swing.JProgressBar` is 
     * provided to this method, the developer will need to reset the progress
     * bar once this method has returned from their own code, as we only set it
     * up for use and use it on each record while the search proceeds. This 
     * method does not zero the progress bar out once completed, nor do we 
     * change its visibility within this method. Therefore, prior to providing
     * the progress bar, the developer needs to make sure that it is visible to
     * the user and, once this method returns, the developer needs to handle the
     * progress bar how they desire.
     * 
     * Furthermore, this method changes the tooltip text of the progress bar to
     * "Broker Search Progress", so the developer will also need to reset the
     * tooltip on the progress bar once this method returns.</dd></dl>
     * 
     * @param company The company whose record is to be located.
     * @param bar a `javax.swing.JProgressBar` object to visually display the
     *            search progress to the user. May be null.
     * @return  `BrokerModel` object containing the company record, if it exists.
     *          `null` otherwise.
     * @throws DataStoreException in the event a database access error occurs.
     */
    public CustomerModel getByCompany(String company, 
            javax.swing.JProgressBar bar) throws DataStoreException {
        // Check to see if we were given a JProgressBar to work with.
        if ( bar == null ) {
            // If not, create one, even though it will not be displayed, just so
            //+ we do not have to constantly repeat this check.
            bar = new javax.swing.JProgressBar();
        }
        
        bar.setMaximum(getRecordCount());
        bar.setMinimum(0);
        bar.setToolTipText("Broker Search Progress");
        bar.setValue(0);
        
        // Store the current record number.
        int lastRecord = getCurrentRecordNumber();
        
        // Prepare a record object.
        CustomerModel tmp = null;
        
        if ( getRecordCount() > 0 ) {
            // Move to the first record.
            first();

            // Now, loop through all of the records in the table.
            for ( int x = 0; x < records.size(); x++ ) {
                bar.setValue(x + 1);
                // Check to see if the current record's company matches the company
                //+ being sought by the user.
                if ( records.get(x).getCompany().equalsIgnoreCase(company) ) {
                    // Grab the record.
                    tmp = records.get(x);

                    // Break out of the loop because we do not need to continue.
                    break;
                }
            }
        }
        
        row = lastRecord;
        
        // Return the record we found, or null if there was no match.
        return tmp;
    }
    
    /**
     * This method returns an `java.util.ArrayList<CustomerModel>` of all matching
     * customer records.This method is useful for allowing the user to filter a 
     * long list ofrecords down to narrow his/her search for a customer, such as 
     * in the Book Load Wizard.
     * <dl><dt>Developer's Note</dt><dd>If a `javax.swing.JProgressBar` is 
     * provided to this method, the developer will need to reset the progress
     * bar once this method has returned from their own code, as we only set it
     * up for use and use it on each record while the search proceeds. This 
     * method does not zero the progress bar out once completed, nor do we 
     * change its visibility within this method. Therefore, prior to providing
     * the progress bar, the developer needs to make sure that it is visible to
     * the user and, once this method returns, the developer needs to handle the
     * progress bar how they desire.
     * 
     * Furthermore, this method changes the tooltip text of the progress bar to
     * "Broker Search Progress", so the developer will also need to reset the
     * tooltip on the progress bar once this method returns.</dd></dl>
     * 
     * @param state The state in which the customer(s) must be located.
     * @param bar a `javax.swing.JProgressBar` object to visually display the
     *            search progress to the user. May be null.
     * @return an `ArrayList` of all matching customers, or `null` if none found.
     * @throws DataStoreException in the event a database access error occurs.
     */
    public ArrayList<CustomerModel> getCompaniesByState(String state,
            javax.swing.JProgressBar bar) throws DataStoreException {
        // Check to see if we were given a JProgressBar to work with.
        if ( bar == null ) {
            // If not, create one, even though it will not be displayed, just so
            //+ we do not have to constantly repeat this check.
            bar = new javax.swing.JProgressBar();
        }
        
        bar.setMaximum(getRecordCount());
        bar.setMinimum(0);
        bar.setToolTipText("Broker Search Progress");
        bar.setValue(0);
        
        // Initialize our ArrayList for returning to the requesting method.
        ArrayList<CustomerModel> tmp = new ArrayList<>();
        
        if ( getRecordCount() > 0 ) {
            // Loop through all of our records.
            for ( int x = 0; x < records.size(); x++ ) {
                bar.setValue(x + 1);
                // Check to see if the current record contains a broker from the 
                //+ requested state.
                if ( records.get(x).getState().equalsIgnoreCase(state) ) {
                    // Add the record to our returnable ArrayList.
                    tmp.add(records.get(x));
                }
            }
        }
        
        // Return either the list of located brokers or null.
        return tmp.size() > 0 ? tmp : null;
    }
        
    /**
     * This method returns an `java.util.ArrayList<CustomerModel>` of all matching
     * customer records.
     * 
     * This method is useful for allowing the user to filter a long list of 
     * records down to narrow his/her search for a customer, such as in the Book
     * Load Wizard.
     * <dl><dt>Developer's Note</dt><dd>If a `javax.swing.JProgressBar` is 
     * provided to this method, the developer will need to reset the progress
     * bar once this method has returned from their own code, as we only set it
     * up for use and use it on each record while the search proceeds. This 
     * method does not zero the progress bar out once completed, nor do we 
     * change its visibility within this method. Therefore, prior to providing
     * the progress bar, the developer needs to make sure that it is visible to
     * the user and, once this method returns, the developer needs to handle the
     * progress bar how they desire.
     * 
     * Furthermore, this method changes the tooltip text of the progress bar to
     * "Broker Search Progress", so the developer will also need to reset the
     * tooltip on the progress bar once this method returns.</dd></dl>
     * 
     * @param city The city in which the customer(s) must be located.
     * @return an `ArrayList` of all matching customers, or `null` if none found.
     * @throws DataStoreException in the event a database access error occurs.
     */
    public ArrayList<CustomerModel> getCompaniesByCity(String city,
            javax.swing.JProgressBar bar) throws DataStoreException {
        // Check to see if we were given a JProgressBar to work with.
        if ( bar == null ) {
            // If not, create one, even though it will not be displayed, just so
            //+ we do not have to constantly repeat this check.
            bar = new javax.swing.JProgressBar();
        }
        
        bar.setMaximum(getRecordCount());
        bar.setMinimum(0);
        bar.setToolTipText("Broker Search Progress");
        bar.setValue(0);
        
        // Initialize our ArrayList for returning to the requesting method.
        ArrayList<CustomerModel> tmp = new ArrayList<>();
        
        if ( getRecordCount() > 0 ) {
            // Loop through all of our records.
            for ( int x = 0; x < records.size(); x++ ) {
                bar.setValue(x + 1);
                // Check to see if the current record contains a broker from the 
                //+ requested state.
                if ( records.get(x).getCity().equalsIgnoreCase(city) ) {
                    // Add the record to our returnable ArrayList.
                    tmp.add(records.get(x));
                }
            }
        }
        
        // Return either the list of located brokers or null.
        return tmp.size() > 0 ? tmp : null;
    }
    
    /**
     * Provides a method of retrieving the record for the specified company.
     * This method may be used to find that specific company for multiple 
     * purposes, including to make sure the user is not attempting to enter the 
     * same company record multiple times. 
     * <dl><dt>Note</dt><dd>It is a best practice to call this method prior to
     * calling the `addNew(BrokerModel)` method as a means of ensuring each 
     * broker is only entered into the database a single time.</dd></dl>
     * <dl><dt>Developer's Note</dt><dd>If a `javax.swing.JProgressBar` is 
     * provided to this method, the developer will need to reset the progress
     * bar once this method has returned from their own code, as we only set it
     * up for use and use it on each record while the search proceeds. This 
     * method does not zero the progress bar out once completed, nor do we 
     * change its visibility within this method. Therefore, prior to providing
     * the progress bar, the developer needs to make sure that it is visible to
     * the user and, once this method returns, the developer needs to handle the
     * progress bar how they desire.
     * 
     * Furthermore, this method changes the tooltip text of the progress bar to
     * "Broker Search Progress", so the developer will also need to reset the
     * tooltip on the progress bar once this method returns.</dd></dl>
     * 
     * @param company The company whose record is to be located.
     * @param bar a `javax.swing.JProgressBar` object to visually display the
     *            search progress to the user. May be null.
     * @return  `ArrayList<BrokerModel>` object containing the company records, 
     *          if any exist. `null` otherwise.
     * @throws DataStoreException in the event a database access error occurs.
     */
    public ArrayList<CustomerModel> getCustomersByCompany(String company, 
            javax.swing.JProgressBar bar) throws DataStoreException {
        // Check to see if we were given a JProgressBar to work with.
        if ( bar == null ) {
            // If not, create one, even though it will not be displayed, just so
            //+ we do not have to constantly repeat this check.
            bar = new javax.swing.JProgressBar();
        }
        
        bar.setMaximum(getRecordCount());
        bar.setMinimum(0);
        bar.setToolTipText("Broker Search Progress");
        bar.setValue(0);
        
        // Store the current record number.
        int lastRecord = getCurrentRecordNumber();
        
        // Prepare a record object.
        ArrayList<CustomerModel> tmp = new ArrayList<>();
        
        if ( getRecordCount() > 0 ) {
            // Move to the first record.
            first();

            // Now, loop through all of the records in the table.
            for ( int x = 0; x < records.size(); x++ ) {
                bar.setValue(x + 1);
                // Check to see if the current record's company matches the company
                //+ being sought by the user.
                if ( records.get(x).getCompany().equalsIgnoreCase(company) ) {
                    // Grab the record.
                    tmp.add(records.get(x));

                    // Break out of the loop because we do not need to continue.
                    break;
                }
            }
        }
        
        row = lastRecord;
        
        // Return the record we found, or null if there was no match.
        return tmp.size() > 0 ? tmp : null;
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
    public void close() throws DataStoreException {
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
        if ( LoadMaster.fileProgress != null ) {
            LoadMaster.fileProgress.setMinimum(0);
            LoadMaster.fileProgress.setMaximum(
                    Starter.props.getPropertyAsInt("table.customers.records", "0"));
            LoadMaster.fileProgress.setVisible(true);
        }
        
        try {
            in = new BufferedReader(new FileReader(TABLE));
            
            String line = in.readLine();
            
            while ( line != null ) {
                String[] record = line.split("~");
                
                createAndAddRecord(record);
                
                line = in.readLine();

                if ( LoadMaster.fileProgress != null ) {
                    LoadMaster.fileProgress.setValue(
                            LoadMaster.fileProgress.getValue() + 1);
                }
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
            if ( LoadMaster.fileProgress != null ) {
                LoadMaster.fileProgress.setValue(0);
                LoadMaster.fileProgress.setVisible(false);
            }
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
