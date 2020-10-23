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
 *   Module:   ReceivablesCtl
 *   Created:  Oct 19, 2020
 *   Modified: Oct 19, 2020
 * 
 *   Purpose:
 *      Provides all data access for the Accounts Receivables journal.
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
import com.pekinsoft.loadmaster.model.ReceivablesModel;
import com.pekinsoft.loadmaster.utils.MessageBox;
import com.pekinsoft.loadmaster.view.LoadMaster;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.1.0
 * @since 0.7.8 build 2549
 */
public class ReceivablesCtl extends AbstractJournal<ReceivablesModel> {

    //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
    public ReceivablesCtl () throws DataStoreException {
        super(new ReceivablesModel(), Starter.props.getDataFolder() 
                + ReceivablesModel.ACCOUNT_NUMBER + ".jrnl");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Instance Methods">
    /**
     * Provides a means of selecting a specific AR entry based upon the load 
     * from which it was earned. This method searches through the entries in the
     * Accounts Receivable Journal to find the record with the specified trip
     * number.
     * 
     * @param tripNumber    the desired trip number.
     * @return              the requested journal entry, or `null` if not found.
     * @throws DataStoreException in the event an error occurs accessing the
     *                            date file.
     */
    public ReceivablesModel getByTripNumber(String tripNumber) 
            throws DataStoreException {
        int lastRecord = getCurrentRecordNumber();
        first();    // move to the first record.
        
        ReceivablesModel m = null;
        
        for ( int x = 0; x < getRecordCount(); x++ ) {
            m = records.get(x);
            
            if ( m.getTripNumber().equalsIgnoreCase(tripNumber) )
                break;
            else
                m = null;
        }
        
        row = lastRecord; // move back to the record we were on before the search.
        
        return m;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Protected Override Methods">
    @Override
    protected String buildRecordLine(ReceivablesModel model) {
        return model.getIdAsString() + "~" + model.getDateAsString() + "~"
                + model.getTripNumber() + "~" + model.getOrderNumber() + "~"
                + model.getAmountAsString() + "~" + model.isSettled();
    }
    
    @Override
    protected void createAndAddRecord(String[] line) {
        entry.setMessage("Entering...");
        entry.setSourceMethodName("createAndAddRecord");
        entry.setParameters(line);
        Starter.logger.enter(entry);
        
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        
        try {
            
            record = new ReceivablesModel(sdf.parse(line[1]), line[2], line[3], 
                    Double.valueOf(line[4]));
        } catch ( ParseException ex ) {
            entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                    + "Parsing error while parsing the dispatch date.");
            entry.setThrown(ex);
            entry.setSourceMethodName("createAndAddRecord");
            entry.setParameters(null);
            Starter.logger.error(entry);
            
            MessageBox.showError(ex, "Parsing Error");
        }
        
        record.setId(line[0]);
        record.setTripNumber(line[2]);
        record.setOrderNumber(line[3]);
        record.setAmount(line[4]);
        record.setSettled(Boolean.parseBoolean(line[5]));
        
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
