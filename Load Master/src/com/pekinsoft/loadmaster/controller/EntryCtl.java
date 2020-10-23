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
 *   Module:   EntryCtl
 *   Created:  Oct 19, 2020
 *   Modified: Oct 19, 2020
 * 
 *   Purpose:
 *      Provides all data access functionality for the General Ledger.
 * 
 *   Revision History
 * 
 *   WHEN          BY                  REASON
 *   ------------  ------------------- ------------------------------------------
 *   Oct 19, 2020  Sean Carrick        Initial Creation.
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
import com.pekinsoft.loadmaster.model.EntryModel;
import com.pekinsoft.loadmaster.utils.MessageBox;
import com.pekinsoft.loadmaster.view.LoadMaster;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * The `EntryCtl` class provides the system interoperability for the General 
 * Ledger of the Load Master accounting system. This class allows for storing
 * and retrieving all transactions of the GL.
 * 
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.1.0
 * @since 0.7.8 build 2549
 */
public class EntryCtl extends AbstractJournal<EntryModel> {

    //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
    public EntryCtl () throws DataStoreException {
        super(new EntryModel(), Starter.props.getDataFolder() 
                + EntryModel.DATA_FILE);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Instance Methods">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Protected Override Methods">
    @Override
    protected String buildRecordLine(EntryModel model) {
        return model.getDateAsString() + "~" + model.getCode() + "~"
                + model.getDescription() + "~" + model.getFromAccount()
                + "~" + model.getToAccount() + "~" + model.getAmount()
                + "~" + model.isDeductible() + "~" + model.isBalanced();
    }
    
    @Override
    protected void createAndAddRecord(String[] line) {
        entry.setMessage("Entering...");
        entry.setSourceMethodName("createAndAddRecord");
        entry.setParameters(line);
        Starter.logger.enter(entry);
        
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        
        record = new EntryModel();
        
        
        try {
            record.setDate(sdf.parse(line[0]));
        } catch ( ParseException ex ) {
            entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                    + "Parsing error while parsing the dispatch date.");
            entry.setThrown(ex);
            entry.setSourceMethodName("createAndAddRecord");
            entry.setParameters(null);
            Starter.logger.error(entry);
            
            MessageBox.showError(ex, "Parsing Error");
        }
        
        record.setCode(line[1]);
        record.setDescription(line[2]);
        record.setFromAccount(Integer.parseInt(line[3]));
        record.setToAccount(Integer.parseInt(line[4]));
        record.setAmount(Double.parseDouble(line[5]));
        record.setDeductible(Boolean.parseBoolean(line[6]));
        record.setBalanced(Boolean.parseBoolean(line[7]));
        
        records.add(record);
        
        LoadMaster.fileProgress.setValue(
                LoadMaster.fileProgress.getValue() + 1);
    }

    @Override
    public boolean postTransactions() throws DataStoreException {
        throw new UnsupportedOperationException("Not necessary for this object.");
    }
    //</editor-fold>

}
