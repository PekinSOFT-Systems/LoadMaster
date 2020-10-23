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
 *   Module:   BrokerCtl
 *   Created:  Sep 06, 2020
 *   Modified: Sep 06, 2020
 * 
 *   Purpose:
 *      Provides all data access functionality for the General Ledger.
 * 
 *   Revision History
 * 
 *   WHEN          BY                  REASON
 *   ------------  ------------------- ------------------------------------------
 *   Sep 06, 2020  Sean Carrick        Initial Creation.
 *   Oct 21, 2020  Sean Carrick        Modifying the class to allow for filtering
 *                                     the data to a specific broker and to make
 *                                     sure that a new broker does not already
 *                                     exist in the table.
 *   Oct 23, 2020  Sean Carrick        Altered this class to implement only the
 *                                     three (3) abstract methods in the super
 *                                     class: 
 *                                          - buildRecordLine(FuelCardModel)
 *                                          - createAndAddRecord(String[])
 *                                          - postTransactions()
 *                                     All other functionality is taken care of
 *                                     in AbstractJournal<T>.
 *
 *  ******************************************************************************
 */

package com.pekinsoft.loadmaster.controller;

import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.api.AbstractJournal;
import com.pekinsoft.loadmaster.err.DataStoreException;
import com.pekinsoft.loadmaster.model.BrokerModel;
import java.util.ArrayList;

/**
 * The `BrokerCtl` object provides all functionality related to accessing the
 * `brokers.tbl` database file. This object provides the ability to access all
 * of the records in the table, navigate between those records, as well as to
 * search those records.
 * 
 * When creating an object of type `BrokerCtl`, the developer 
 * <strong>must</strong> enclose the call to the constructor within a 
 * `try...catch()` block, as the constructor could throw a `DataStoreException`
 * while the object is being created. Calls to any of the methods that access
 * the database, likewise, need to be enclosed in `try...catch()` blocks, as all
 * database access methods could throw the `DataStoreException` if there are any
 * errors accessing the data file.
 * 
 * <dl><dt>Developer's Note</dt><dd>PekinSOFT™ Sytems <em>does not</em> handle
 * any `DataStoreException` trapping within this object. The reason for this is
 * that we believed that it should be up to the individual class developer to 
 * determine how those errors should be handled. PekinSOFT™ could not possibly
 * determine the various different ways that this class could possibly be used,
 * and so also couldn't make a quality decision on how to handle errors if they
 * were to arise. For these reasons, we have left the error trapping and handling
 * up to the developers who choose to use our controller objects in their 
 * applications and projects.</dd></dl>
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.4.0
 * @since 0.1.0
 */
public class BrokerCtl extends AbstractJournal<BrokerModel> {

    //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
    public BrokerCtl () throws DataStoreException {
        super(new BrokerModel(), Starter.props.getDataFolder() + "brokers.tbl");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Instance Methods">
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
    public BrokerModel getByCompany(String company, javax.swing.JProgressBar bar) 
            throws DataStoreException {
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
        BrokerModel tmp = null;
        
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
        
        // Return the record we found, or null if there was no match.
        return tmp;
    }
    
    /**
     * This method returns an `java.util.ArrayList<BrokerModel>` of all matching
     * broker records.This method is useful for allowing the user to filter a 
     * long list ofrecords down to narrow his/her search for a broker, such as 
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
     * @param state The state in which the broker(s) must be located.
     * @param bar a `javax.swing.JProgressBar` object to visually display the
     *            search progress to the user. May be null.
     * @return an `ArrayList` of all matching brokers, or `null` if none found.
     * @throws DataStoreException in the event a database access error occurs.
     */
    public ArrayList<BrokerModel> getCompaniesByState(String state,
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
        ArrayList<BrokerModel> tmp = new ArrayList<>();
        
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
     * This method returns an `java.util.ArrayList<BrokerModel>` of all matching
     * broker records.
     * 
     * This method is useful for allowing the user to filter a long list of 
     * records down to narrow his/her search for a broker, such as in the Book
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
     * @param city The city in which the broker(s) must be located.
     * @return an `ArrayList` of all matching brokers, or `null` if none found.
     * @throws DataStoreException in the event a database access error occurs.
     */
    public ArrayList<BrokerModel> getCompaniesByCity(String city,
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
        ArrayList<BrokerModel> tmp = new ArrayList<>();
        
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Protected Override Methods">
    @Override
    protected void createAndAddRecord(String[] line) {
        record = new BrokerModel();
        record.setId(Long.parseLong(line[0]));
        record.setCompany(line[1]);
        record.setStreet(line[2]);
        record = new BrokerModel();
        record.setSuite(line[3]);
        record.setCity(line[4]);
        record.setState(line[5]);
        record.setZip(line[6]);
        record.setContact(line[7]);
        record.setPhone(line[8]);
        record.setFax(line[9]);
        record.setEmail(line[10]);

        records.add(record);
    }
    
    @Override
    protected String buildRecordLine(BrokerModel model) {
        return model.getId() + "~" + model.getCompany() + "~"
                + model.getStreet() + "~" + model.getSuite() + "~"
                + model.getCity() + "~" + model.getState() + "~"
                + model.getZip() + "~" + model.getContact() + "~"
                + model.getPhone() + "~" + model.getFax() + "~"
                + model.getEmail();
    }

    @Override
    public boolean postTransactions() throws DataStoreException {
        throw new UnsupportedOperationException("Not necessary for this object.");
    }
    //</editor-fold>


}
