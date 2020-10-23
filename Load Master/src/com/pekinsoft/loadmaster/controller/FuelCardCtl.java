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
 *   Module:   FuelCardCtl
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
 *   Oct 21, 2020  Sean Carrick        Initial Creation.
 *   Oct 22, 2020  Sean Carrick        Modified FuelCardControl to extend the
 *                                     AbstractJournal class. This modification
 *                                     will save countless time on class creation
 *                                     and maintenance moving into the future.
 *  ******************************************************************************
 */

package com.pekinsoft.loadmaster.controller;

import com.pekinsoft.loadmaster.api.AbstractJournal;
import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.err.DataStoreException;
import com.pekinsoft.loadmaster.model.EntryModel;
import com.pekinsoft.loadmaster.model.FuelCardModel;
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
public class FuelCardCtl extends AbstractJournal {
    //<editor-fold defaultstate="collapsed" desc="Public Static Constants">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Member Fields">
    private final File TABLE;
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
    public FuelCardCtl () throws DataStoreException {
        super();
        
        entry.setSourceClassName(getClass().getCanonicalName());
        entry.setSourceMethodName(getClass().getName() + " (Constructor)");
        entry.setParameters(null);
        entry.setMessage("Initializing a FuelCardCtl journal object.");
        Starter.logger.enter(entry);
        
        records = new ArrayList<>();
        
        TABLE = new File(Starter.props.getDataFolder() 
                + FuelCardModel.ACCOUNT_NUMBER + ".jrnl");

        // Check to see if the table file exists:
        
        if ( createFileIfNecessary() )
            connect();
        else {
            entry.setMessage("Fuel Card Journal was just created.");
            entry.setSourceClassName(getClass().getCanonicalName());
            entry.setSourceMethodName(getClass().getName() + " (Constructor)");
            entry.setParameters(null);
            entry.setThrown(null);
            entry.setInstant(Instant.now());
            Starter.logger.info(entry);
        }
        
        entry.setMessage("Done creating FuelCardCtl object.");
        Starter.logger.exit(entry, null);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Static Methods">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Instance Methods">
    public void addNew(FuelCardModel model) {
        super.addNew(model);
        
        Starter.props.setPropertyAsInt("journal.fuel.card.records", 
                getRecordCount());
    }
    
    @Override
    public FuelCardModel first() throws DataStoreException {
        return (FuelCardModel)super.first();
    }
    
    @Override
    public FuelCardModel previous() throws DataStoreException {
        return (FuelCardModel)super.previous();
    }
    
    @Override
    public FuelCardModel next() throws DataStoreException {
        return (FuelCardModel)super.next();
    }
    
    @Override
    public FuelCardModel last() throws DataStoreException {
        return (FuelCardModel)super.last();
    }
    
    public FuelCardModel get() {
        return (FuelCardModel)super.get();
    }
    
    public FuelCardModel get(int idx) {
        return (FuelCardModel)super.get(idx);
    }
    
    public ArrayList<FuelCardModel> getByTripNumber(String tripNumber) {
        ArrayList<FuelCardModel> list = new ArrayList<>();
        
            for ( int x = 0; x < records.size(); x++ ) {
                if ( ((FuelCardModel)records.get(x)).getTripNumber()
                        .contains(tripNumber) ) {
                    list.add(((FuelCardModel)records.get(x)));
                }
            }
        
        return list;
    }
    
    @Override
    public void close() throws DataStoreException {
        save();
    }
    
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
            if ( !((FuelCardModel)records.get(x)).isPosted() ) {
                // Since the record has not yet been posted to the GL, we need 
                //+ to do so now.
                tx = new EntryModel();
                tx.setAmount(((FuelCardModel)records.get(x)).getAmount());
                tx.setBalanced(false);
                tx.setCode("Xfer");
                try {
                    tx.setDate(((FuelCardModel)records.get(x)).getDateAsString());
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
                if ( ((FuelCardModel)records.get(x)).getTripNumber().equalsIgnoreCase(
                        "No Active Load") ) {
                    tx.setDescription("Transferred while not under a load.");
                } else {
                    tx.setDescription("Transferred under Trip # " 
                            + ((FuelCardModel)records.get(x)).getTripNumber());
                }
                tx.setFromAccount(((FuelCardModel)records.get(x)).getFromAcct());
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
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Instance Methods">
    @Override
    protected void connect() throws DataStoreException {
        entry.setMessage("Enter...");
        entry.setSourceMethodName("connect");
        entry.setParameters(new Object[]{});
        Starter.logger.enter(entry);
        
        BufferedReader in;
        
        entry.setMessage("Setting up LoadMaster.fileProgress...");
        entry.setParameters(null);
        Starter.logger.config(entry);
        
        if ( LoadMaster.fileProgress != null ) {
            LoadMaster.fileProgress.setMaximum(
                    Starter.props.getPropertyAsInt("journal.fuel.card.records", "0") 
                    + (Starter.props.getPropertyAsInt("journal.fuel.card.records", "0")));
            LoadMaster.fileProgress.setValue(0);
            LoadMaster.fileProgress.setVisible(true);
        }
        
        try {
            in = new BufferedReader(new FileReader(TABLE));
            
            String line = in.readLine();
            
            while ( line != null ) {
                String[] splitLine = line.split("~");
                
                createAndAddRecord(splitLine);
                
                line = in.readLine();

                if ( LoadMaster.fileProgress != null ) {
                    LoadMaster.fileProgress.setValue(
                            LoadMaster.fileProgress.getValue() + 1);
                }
            }
            
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
            Starter.props.setPropertyAsInt("journal.fuel.card.records", records.size());
            Starter.props.flush();
        }
    }
    
    @Override
    protected void save() throws DataStoreException {
        BufferedWriter out;
        
        LoadMaster.fileProgress.setMaximum(
                Starter.props.getPropertyAsInt("journal.fuel.card.records", "0"));
        LoadMaster.fileProgress.setValue(
                Starter.props.getPropertyAsInt("journal.fuel.card.records", "0"));
        
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
                out.write(buildRecordLine((FuelCardModel)records.get(x)) + "\n");
                
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
    
    private String buildRecordLine(FuelCardModel model) {
        return model.getId() + "~" + model.getDateAsString() + "~"
                + model.getFromAcct()+ "~" + model.getTripNumber()+ "~"
                + model.getAmount()+ "~" + model.isPosted();
    }
    
    private void createAndAddRecord(String[] line) {
        entry.setMessage("Entering...");
        entry.setSourceMethodName("createAndAddRecord");
        entry.setParameters(line);
        Starter.logger.enter(entry);
        
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        
        record = new FuelCardModel();
        
        
        try {
            ((FuelCardModel)record).setDate(sdf.parse(line[1]));
        } catch ( ParseException ex ) {
            entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                    + "Parsing error while parsing the dispatch date.");
            entry.setThrown(ex);
            entry.setSourceMethodName("createAndAddRecord");
            entry.setParameters(null);
            Starter.logger.error(entry);
            
            MessageBox.showError(ex, "Parsing Error");
        }
        
        ((FuelCardModel)record).setId(Long.parseLong(line[0]));
        ((FuelCardModel)record).setFromAcct(Integer.parseInt(line[2]));
        ((FuelCardModel)record).setTripNumber(line[3]);
        ((FuelCardModel)record).setAmount(Double.parseDouble(line[4]));
        ((FuelCardModel)record).setPosted(Boolean.parseBoolean(line[5]));
        
        records.add(record);
        
        LoadMaster.fileProgress.setValue(
                LoadMaster.fileProgress.getValue() + 1);
    }
    //</editor-fold>

    @Override
    protected boolean createFileIfNecessary() {
        boolean success = true;
        if ( !TABLE.exists() ) {
            try {
                TABLE.createNewFile();
            } catch (IOException ex) {
                entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                        + "\nThrowing DataStoreException...");
                entry.setParameters(null);
                entry.setSourceMethodName("createFileIfNecessary");
                entry.setThrown(ex);
                Starter.logger.error(entry);
                
                success = false;
            }
        }
        
        return success;
    }

}
