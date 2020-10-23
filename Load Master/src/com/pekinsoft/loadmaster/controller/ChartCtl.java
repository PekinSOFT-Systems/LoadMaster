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
 * Class   :   ChartCtl
 * Author  :   Sean Carrick
 * Created :   Oct 22, 2020
 * Modified:   Oct 22, 2020
 * 
 * Purpose:
 *     Provides access to the Chart of Accounts table in the database.
 * 
 * WHEN          BY                  REASON
 * ------------  ------------------  -------------------------------------------
 * Oct 22, 2020  Sean Carrick        Initial creation.
 * 
 * *****************************************************************************
 */

package com.pekinsoft.loadmaster.controller;

import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.api.AbstractJournal;
import com.pekinsoft.loadmaster.err.DataStoreException;
import com.pekinsoft.loadmaster.model.ChartModel;
import com.pekinsoft.loadmaster.view.LoadMaster;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Instant;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.1.0
 * @since 0.1.0
 */
public class ChartCtl extends AbstractJournal {
    //<editor-fold defaultstate="collapsed" desc="Public Static Constants">
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Private Member Fields">
    private final File TABLE;

    //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
    public ChartCtl () throws DataStoreException {
        super();
        
        entry.setMessage("Initializing the ChartCtl object.");
        entry.setSourceClassName(getClass().getCanonicalName());
        entry.setSourceMethodName(getClass().getName() + " (Constructor)");
        entry.setParameters(null);
        entry.setThrown(null);
        entry.setInstant(Instant.now());
        Starter.logger.enter(entry);
        
        TABLE = new File(Starter.props.getDataFolder() 
                + ChartModel.ACCOUNT_NUMBER + ".tbl");
        
        record = new ChartModel();
        
        // Check to see if the table file exists:
        if ( createFileIfNecessary() )
            connect();
        else {
            entry.setMessage("Chart of Accounts table was just created.");
            entry.setSourceClassName(getClass().getCanonicalName());
            entry.setSourceMethodName(getClass().getName() + " (Constructor)");
            entry.setParameters(null);
            entry.setThrown(null);
            entry.setInstant(Instant.now());
            Starter.logger.info(entry);
        }
        
        entry.setMessage("Done creating ChartCtl object.");
        Starter.logger.exit(entry, null);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Instance Methods">
    @Override
    public ChartModel get() {
        return (ChartModel)super.get();
    }
    
    @Override
    public ChartModel get(int idx) {
        return (ChartModel)super.get(idx);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Instance Methods">
    
    private void createAndAddRecord(String[] line) {
        entry.setMessage("Entering...");
        entry.setSourceMethodName("createAndAddRecord");
        entry.setParameters(line);
        Starter.logger.enter(entry);
        
        record = new ChartModel();
        
//        record = new ReceivablesModel();
        
        ((ChartModel)record).setNumber(Long.parseLong(line[0]));
        ((ChartModel)record).setName(line[1]);
        ((ChartModel)record).setDescription(line[2]);
        
        records.add(record);
    }
    //</editor-fold>
     
    private String buildRecordLine(ChartModel model) {
        return model.getNumber()+ "~" + model.getDescription()+ "~"
                + model.getName();
    }
    
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
    protected void close() throws DataStoreException {
        save();
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
                out.write(buildRecordLine((ChartModel)records.get(x)) 
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

    @Override
    public boolean postTransactions() throws DataStoreException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
