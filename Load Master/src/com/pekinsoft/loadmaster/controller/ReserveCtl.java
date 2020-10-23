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
 * Class   :   ReserveCtl
 * Author  :   Sean Carrick
 * Created :   Oct 22, 2020
 * Modified:   Oct 22, 2020
 * 
 * Purpose:
 *     Provides the means to connect the application to the data files.
 * 
 * WHEN          BY                  REASON
 * ------------  ------------------  -------------------------------------------
 * Oct 22, 2020  Sean Carrick        Initial creation.
 * Oct 23, 2020  Sean Carrick        Altered this class to implement only the
 *                                   three (3) abstract methods in the super
 *                                   class: 
 *                                        - buildRecordLine(FuelCardModel)
 *                                        - createAndAddRecord(String[])
 *                                        - postTransactions()
 *                                   All other functionality is taken care of
 *                                   in AbstractJournal<T>.
 * 
 * *****************************************************************************
 */

package com.pekinsoft.loadmaster.controller;

import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.api.AbstractJournal;
import com.pekinsoft.loadmaster.err.DataStoreException;
import com.pekinsoft.loadmaster.model.EntryModel;
import com.pekinsoft.loadmaster.model.ReceivablesModel;
import com.pekinsoft.loadmaster.model.ReserveModel;
import com.pekinsoft.loadmaster.utils.MessageBox;
import com.pekinsoft.loadmaster.view.LoadMaster;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.1.0
 * @since 0.1.0
 */
public class ReserveCtl extends AbstractJournal<ReserveModel> {

    //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
    public ReserveCtl () throws DataStoreException {
        super(new ReserveModel(), Starter.props.getDataFolder() 
                + ReserveModel.DATA_FILE);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Instance Methods">

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Protected Override Methods">
    @Override
    protected String buildRecordLine(ReserveModel model) {
        return model.getIdAsString() + "~" + model.getTripNumber() + "~"
                + model.getDateAsString() + "~"
                + model.getFromAccountAsString() + "~" 
                + model.getAmountAsString()+ "~" + model.isPosted();
    }
    
    @Override
    protected void createAndAddRecord(String[] line) {
        entry.setMessage("Entering...");
        entry.setSourceMethodName("createAndAddRecord");
        entry.setParameters(line);
        Starter.logger.enter(entry);
        
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        
        record = new ReserveModel();
        
        
        try {
            ((ReserveModel)record).setDate(sdf.parse(line[1]));
        } catch ( ParseException ex ) {
            entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                    + "Parsing error while parsing the dispatch date.");
            entry.setThrown(ex);
            entry.setSourceMethodName("createAndAddRecord");
            entry.setParameters(null);
            Starter.logger.error(entry);
            
            MessageBox.showError(ex, "Parsing Error");
        }
        
        try {
            ((ReserveModel)record).setDate(line[1]);
        } catch ( ParseException ex ) {
            entry.setSourceClassName(getClass().getCanonicalName());
            entry.setSourceMethodName("createAndAddRecord");
            entry.setParameters(null);
            entry.setThrown(ex);
            entry.setMessage(ex.getMessage());
            entry.setInstant(Instant.now());
            Starter.logger.error(entry);
            
            // We will just create the date on the fly.
            ((ReserveModel)record).setDate(new Date());
        }
        
        ((ReserveModel)record).setId(Long.parseLong(line[0]));
        ((ReserveModel)record).setTripNumber(line[2]);
        ((ReserveModel)record).setFromAccount(Integer.parseInt(line[3]));
        ((ReserveModel)record).setAmount(Double.parseDouble(line[4]));
        ((ReserveModel)record).setPosted(Boolean.parseBoolean(line[5]));
        
        records.add(record);
        
        LoadMaster.fileProgress.setValue(
                LoadMaster.fileProgress.getValue() + 1);
    }
    
    @Override
    public boolean postTransactions() throws DataStoreException {
        // For this, we are going to need to create an EntryCtl object, as well
        //+ as an EntryModel object.
        EntryCtl gl = new EntryCtl();
        EntryModel tx = null;
        
        // We also need a return value to send back to the calling method.
        boolean success = true; // Default to a successful posting procedure.
        
        // We need to loop through all of our journal entries, so we can enter
        //+ our diesel purchase into the General Ledger.
        for ( int x = 0; x < records.size(); x++ ) {
            // Check to see if the current record has not yet been posted.
            if ( !((ReserveModel)records.get(x)).isPosted() ) {
                // Since the record has not yet been posted to the GL, we need 
                //+ to do so now.
                tx = new EntryModel();
                tx.setAmount(((ReserveModel)records.get(x)).getAmount());
                tx.setCode("RES");
                try {
                    tx.setDate(((ReserveModel)records.get(x)).getDateAsString());
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
                
                String desc = "";
                
                if ( ((ReserveModel)records.get(x)).getTripNumber()
                        .equalsIgnoreCase("No Active Load") ) {
                    desc += "Extra money put to reserves from another account.";
                } else {
                    tx.setDescription("Percentage of Trip # " 
                            + ((ReserveModel)records.get(x)).getTripNumber());
                }
                tx.setDescription(desc);
                
                tx.setFromAccount(ReceivablesModel.ACCOUNT_NUMBER);
                tx.setToAccount(ReserveModel.ACCOUNT_NUMBER);
                
                // Now that we have created the GL transaction entry, we can add
                //+ it to the GL.
                gl.addNew(tx);
                
                ReserveCtl reserves = new ReserveCtl();
                ((ReserveModel)record).setPosted(true);
                reserves.update(record);
                reserves.close();
            } // No `else`, because transactions only need to be posted once.
        } // Continue until all records have been read and posted.
        
        // Finally, we can post the transactions to the General Ledger.
        gl.close();
        
        // Leave as the last line of the method:
        return success;
    }
    //</editor-fold>
    
}
