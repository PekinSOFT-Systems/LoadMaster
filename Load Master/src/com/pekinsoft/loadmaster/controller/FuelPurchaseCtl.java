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
 *   Oct 21, 2020  Sean Carrick        Initial Creation.
 *   Oct 22, 2020  Sean Carrick        Modified FuelCardControl to extend the
 *                                     AbstractJournal class. This modification
 *                                     will save countless time on class creation
 *                                     and maintenance moving into the future.
 *  ******************************************************************************
 */

package com.pekinsoft.loadmaster.controller;

import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.api.AbstractJournal;
import com.pekinsoft.loadmaster.err.DataStoreException;
import com.pekinsoft.loadmaster.model.EntryModel;
import com.pekinsoft.loadmaster.model.FuelCardModel;
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
import java.time.Instant;
import java.util.Date;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.2.67
 * @since 0.7.8 build 2549
 */
public class FuelPurchaseCtl extends AbstractJournal {
    //<editor-fold defaultstate="collapsed" desc="Private Member Fields">
    private final File TABLE;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
    public FuelPurchaseCtl () throws DataStoreException {
        super();
        
        entry.setSourceClassName(getClass().getCanonicalName());
        entry.setSourceMethodName(getClass().getName() + " (Constructor)");
        entry.setParameters(null);
        entry.setMessage("Initializing a FuelPurchaseCtl journal object.");
        Starter.logger.enter(entry);
        
        TABLE = new File(Starter.props.getDataFolder() 
                + FuelPurchaseModel.ACCOUNT_NUMBER + ".jrnl");

        // Check to see if the table file exists:
        if ( createFileIfNecessary() )
            connect();
        else {
            entry.setMessage("Fuel Expense Journal was just created.");
            entry.setSourceClassName(getClass().getCanonicalName());
            entry.setSourceMethodName(getClass().getName() + " (Constructor)");
            entry.setParameters(null);
            entry.setThrown(null);
            entry.setInstant(Instant.now());
            Starter.logger.info(entry);
        }
        
        entry.setMessage("Done creating FuelPurchaseCtl object.");
        Starter.logger.exit(entry, null);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Instance Methods">
    public void addNew(FuelPurchaseModel model) {
        super.addNew(model);
        
        Starter.props.setPropertyAsInt("journal.fuel.records", getRecordCount());
    }
    
    @Override
    public FuelPurchaseModel first() throws DataStoreException {
        return (FuelPurchaseModel)super.first();
    }
    
    @Override
    public FuelPurchaseModel previous() throws DataStoreException {
        return (FuelPurchaseModel)super.previous();
    }
    
    @Override
    public FuelPurchaseModel next() throws DataStoreException {
        return (FuelPurchaseModel)super.next();
    }
    
    @Override
    public FuelPurchaseModel last() throws DataStoreException {
        return (FuelPurchaseModel)super.last();
    }
    
    @Override
    public FuelPurchaseModel get() {
        return (FuelPurchaseModel)super.get();
    }
    
    @Override
    public FuelPurchaseModel get(int idx) {
        return (FuelPurchaseModel)super.get(idx);
    }
    
    @Override
    public void close() throws DataStoreException {
        save();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Instance Methods">
    @Override
    protected void connect() throws DataStoreException {
        entry.setMessage("Enter...");
        entry.setSourceClassName(getClass().getCanonicalName());
        entry.setSourceMethodName("connect");
        entry.setParameters(new Object[]{});
        entry.setThrown(null);
        entry.setInstant(Instant.now());
        Starter.logger.enter(entry);
        
        entry.setMessage("Setting up LoadMaster.fileProgress...");
        entry.setParameters(null);
        Starter.logger.config(entry);
        
        if ( LoadMaster.fileProgress != null ) {
            LoadMaster.fileProgress.setMaximum(
                    Starter.props.getPropertyAsInt("journal.fuel.records", "0") 
                    + (Starter.props.getPropertyAsInt("journal.fuel.records", "0")));
            LoadMaster.fileProgress.setValue(0);
            LoadMaster.fileProgress.setVisible(true);
        }
        
        try (BufferedReader in = new BufferedReader(new FileReader(TABLE))) {
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
            Starter.props.setPropertyAsInt("journal.fuel.records", records.size());
            Starter.props.flush();
        }
    }
    
    @Override
    protected void save() throws DataStoreException {
        
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
        
        try (BufferedWriter out = new BufferedWriter(new FileWriter(TABLE))) {
            
            for ( int x = 0; x < records.size(); x++ ) {
                out.write(buildRecordLine((FuelPurchaseModel)records.get(x)) 
                        + "\n");
                
                LoadMaster.fileProgress.setValue(
                        LoadMaster.fileProgress.getValue() - 1);
            }
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
                + model.getNotes() + "~" + model.getTripNumber() + "~"
                + model.isPosted();
    }
    
    private void createAndAddRecord(String[] line) {
        entry.setMessage("Entering...");
        entry.setSourceMethodName("createAndAddRecord");
        entry.setParameters(line);
        Starter.logger.enter(entry);
        
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        
        record = new FuelPurchaseModel();
        
        
        try {
            ((FuelPurchaseModel)record).setDate(sdf.parse(line[1]));
        } catch ( ParseException ex ) {
            entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                    + "Parsing error while parsing the dispatch date.");
            entry.setThrown(ex);
            entry.setSourceMethodName("createAndAddRecord");
            entry.setParameters(null);
            Starter.logger.error(entry);
            
            MessageBox.showError(ex, "Parsing Error");
        }
        
        ((FuelPurchaseModel)record).setId(line[0]);
        ((FuelPurchaseModel)record).setOdometer(line[2]);
        ((FuelPurchaseModel)record).setLocation(line[3]);
        ((FuelPurchaseModel)record).setGallonsOfDiesel(line[4]);
        ((FuelPurchaseModel)record).setPricePerGallonOfDiesel(line[5]);
        ((FuelPurchaseModel)record).setDefPurchased(Boolean.parseBoolean(line[6]));
        ((FuelPurchaseModel)record).setGallonsOfDef(line[7]);
        ((FuelPurchaseModel)record).setPricePerGallonDef(line[8]);
        ((FuelPurchaseModel)record).setNotes(line[9]);
        ((FuelPurchaseModel)record).setTripNumber(line[10]);
        ((FuelPurchaseModel)record).setPosted(Boolean.parseBoolean(line[11]));
        
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
            if ( !((FuelPurchaseModel)records.get(x)).isPosted() ) {
                // Since the record has not yet been posted to the GL, we need 
                //+ to do so now.
                tx = new EntryModel();
                tx.setAmount(((FuelPurchaseModel)records.get(x)).getGallonsOfDiesel()
                        * ((FuelPurchaseModel)records.get(x)).getPricePerGallonDiesel());
                tx.setBalanced(false);
                tx.setCode("DieselPurch");
                try {
                    tx.setDate(((FuelPurchaseModel)records.get(x)).getDateAsString());
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
                tx.setDeductible(true);
                
                String desc = "Purchased " + ((FuelPurchaseModel)records.get(x))
                        .getGallonsOfDieselAsString();
                desc += " gallons at ";
                desc += ((FuelPurchaseModel)records.get(x)).getLocation();
                
                if ( ((FuelPurchaseModel)records.get(x)).getTripNumber()
                        .equalsIgnoreCase("No Active Load") ) {
                    desc += " while not under an active load.";
                } else {
                    tx.setDescription(" while on Trip # " 
                            + ((FuelPurchaseModel)records.get(x)).getTripNumber());
                }
                tx.setDescription(desc);
                
                tx.setFromAccount(FuelCardModel.ACCOUNT_NUMBER);
                tx.setToAccount(FuelPurchaseModel.ACCOUNT_NUMBER);
                
                // Now that we have created the GL transaction entry, we can add
                //+ it to the GL.
                gl.addNew(tx);
                
                // Now we have to do it all again for the DEF that may have been
                //+ purchased at this same fuel stop.
                if ( ((FuelPurchaseModel)records.get(x)).isDefPurchased() ) {
                    tx.setAmount(((FuelPurchaseModel)records.get(x)).getGallonsOfDef()
                            * ((FuelPurchaseModel)records.get(x)).getPricePerGallonDef());
                    tx.setBalanced(false);
                    tx.setCode("DEFPurch");
                    tx.setDate(((FuelPurchaseModel)records.get(x)).getDate());
                    tx.setDeductible(true);

                    desc = "Purchased " + ((FuelPurchaseModel)records.get(x))
                            .getGallonsOfDefAsString();
                    desc += " gallons at ";
                    desc += ((FuelPurchaseModel)records.get(x)).getLocation();

                    if ( ((FuelPurchaseModel)records.get(x)).getTripNumber()
                            .equalsIgnoreCase("No Active Load") ) {
                        desc += " while not under an active load.";
                    } else {
                        tx.setDescription(" while on Trip # " 
                                + ((FuelPurchaseModel)records.get(x)).getTripNumber());
                    }
                    tx.setDescription(desc);

                    tx.setFromAccount(FuelCardModel.ACCOUNT_NUMBER);
                    tx.setToAccount(FuelPurchaseModel.ACCOUNT_NUMBER);

                    // Now that we have created the GL transaction entry, we can add
                    //+ it to the GL.
                    gl.addNew(tx);
                }
            } // No `else`, because transactions only need to be posted once.
        } // Continue until all records have been read and posted.
        
        // Finally, we can post the transactions to the General Ledger.
        gl.close();
        
        // Leave as the last line of the method:
        return success;
    }

}
