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
 * *****************************************************************************
 *   Project:  Load_Master
 *   Module:   FuelCardCtl
 *   Created:  Oct 19, 2020
 *   Modified: Oct 19, 2020
 * 
 *   Purpose:
 *      Provides all data access for the Fuel Purchases journal.
 * 
 *   Revision History
 * 
 * WHEN          BY                  REASON
 * ------------  ------------------- -----------------------------------------
 * Oct 21, 2020  Sean Carrick        Initial Creation.
 * Oct 22, 2020  Sean Carrick        Modified FuelCardControl to extend the
 *                                   AbstractJournal class. This modification
 *                                   will save countless time on class creation
 *                                   and maintenance moving into the future.
 * Oct 23, 2020  Sean Carrick        Altered this class to implement only the
 *                                   three (3) abstract methods in the super
 *                                   class: 
 *                                        - buildRecordLine(FuelCardModel)
 *                                        - createAndAddRecord(String[])
 *                                        - postTransactions()
 *                                   All other functionality is taken care of
 *                                   in AbstractJournal<T>.
 * Oct 25, 2020  Sean Carrick        Removed unnecessary casting of the record.
 * 
 * *****************************************************************************
 */

package com.pekinsoft.loadmaster.controller;

import com.pekinsoft.loadmaster.api.AbstractJournal;
import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.err.DataStoreException;
import com.pekinsoft.loadmaster.model.EntryModel;
import com.pekinsoft.loadmaster.model.FuelCardModel;
import com.pekinsoft.loadmaster.utils.MessageBox;
import com.pekinsoft.loadmaster.view.LoadMaster;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.2.67
 * @since 0.7.8 build 2549
 */
public class FuelCardCtl extends AbstractJournal<FuelCardModel> {

    //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
    public FuelCardCtl () throws DataStoreException {
        super(new FuelCardModel(), Starter.props.getDataFolder() 
                + FuelCardModel.ACCOUNT_NUMBER + ".jrnl");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Instance Methods">
    public ArrayList<FuelCardModel> getByTripNumber(String tripNumber) {
        ArrayList<FuelCardModel> list = new ArrayList<>();
        
            for ( int x = 0; x < records.size(); x++ ) {
                if ( records.get(x).getTripNumber()
                        .contains(tripNumber) ) {
                    list.add(records.get(x));
                }
            }
        
        return list;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Protected Override Methods">
    @Override
    public boolean postTransactions() throws DataStoreException {
        // For this, we are going to need to create an EntryCtl object, as well
        //+ as an EntryModel object.
        EntryCtl gl = new EntryCtl();
        EntryModel tx = null;
        
        // We also need a return value to send back to the calling method.
        boolean success = true; // Default to a successful posting procedure.
        
        // We need to loop through all of our journal entries.
        for ( int x = 0; x < records.size(); x++ ) {
            // Check to see if the current record has not yet been posted.
            if ( !records.get(x).isPosted() ) {
                // Since the record has not yet been posted to the GL, we need 
                //+ to do so now.
                tx = new EntryModel();
                tx.setAmount(records.get(x).getAmount());
                tx.setBalanced(false);
                tx.setCode("Xfer");
                try {
                    tx.setDate(records.get(x).getDateAsString());
                } catch ( ParseException ex ) {
                    // If we get a ParseException setting the date, we will log
                    //+ it...
                    entry.setSourceClassName(getClass().getCanonicalName());
                    entry.setSourceMethodName("postTransactions");
                    entry.setParameters(null);
                    entry.setThrown(ex);
                    entry.setMessage(ex.getMessage());
                    entry.setInstant(Instant.now());
                    Starter.logger.error(entry);
                    
                    // ...but we will still set a date for the transaction, we
                    //+ will just make it the current date.
                    tx.setDate(new Date());
                }
                tx.setDeductible(false);
                if ( records.get(x).getTripNumber().equalsIgnoreCase(
                        "No Active Load") ) {
                    tx.setDescription("Transferred while not under a load.");
                } else {
                    tx.setDescription("Transferred under Trip # " 
                            + records.get(x).getTripNumber());
                }
                tx.setFromAccount(records.get(x).getFromAcct());
                tx.setToAccount(FuelCardModel.ACCOUNT_NUMBER);
                
                // Now that we have created the GL transaction entry, we can add
                //+ it to the GL.
                gl.addNew(tx);
            } // No `else`, because transactions only need to be posted once.
        } // Continue until all records have been read and posted.
        
        // Finally, we can post the transactions to the General Ledger.
        gl.close();
        
        // Leave as the last line of the method:
        return success;
    }

    @Override
    protected String buildRecordLine(FuelCardModel model) {
        return model.getId() + "~" + model.getDateAsString() + "~"
                + model.getFromAcct()+ "~" + model.getTripNumber()+ "~"
                + model.getAmount()+ "~" + model.isPosted();
    }
    
    @Override
    protected void createAndAddRecord(String[] line) {
        entry.setMessage("Entering...");
        entry.setSourceMethodName("createAndAddRecord");
        entry.setParameters(line);
        Starter.logger.enter(entry);
        
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        
        record = new FuelCardModel();
        
        
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
        
        record.setId(Long.parseLong(line[0]));
        record.setFromAcct(Integer.parseInt(line[2]));
        record.setTripNumber(line[3]);
        record.setAmount(Double.parseDouble(line[4]));
        record.setPosted(Boolean.parseBoolean(line[5]));
        
        records.add(record);
        
        LoadMaster.fileProgress.setValue(
                LoadMaster.fileProgress.getValue() + 1);
    }
    //</editor-fold>

}
