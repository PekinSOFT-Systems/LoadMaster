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
 *   Module:   StopCtl
 *   Created:  Sep 06, 2020
 *   Modified: Sep 06, 2020
 * 
 *   Purpose:
 *      Provides all data access functionality for the load stops.
 * 
 *   Revision History
 * 
 *   WHEN          BY                  REASON
 *   ------------  ------------------- ------------------------------------------
 *   Sep 06, 2020  Sean Carrick        Initial Creation.
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
import com.pekinsoft.loadmaster.err.InvalidTimeException;
import com.pekinsoft.loadmaster.model.StopModel;
import com.pekinsoft.loadmaster.utils.MessageBox;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.1.0
 * @since 0.1.0
 */
public class StopCtl extends AbstractJournal<StopModel> {

    //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
    public StopCtl () throws DataStoreException {
        super(new StopModel(), Starter.props.getDataFolder() + "stops.tbl");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Instance Methods">
    /**
     * This method returns an `java.util.ArrayList<StopModel>` of all stops with
     * an association to the supplied Trip Number. This method is useful for 
     * allowing the user to retrieve all stops for a given load.
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
     * "Stop Search Progress", so the developer will also need to reset the
     * tooltip on the progress bar once this method returns.</dd></dl>
     * 
     * @param tripNumber    the Trip or Order Number of the load of interest.
     * @return an `ArrayList` of all matching customers, or `null` if none found.
     */
    public ArrayList<StopModel> getStopsByTripNumber(String tripNumber) {
        throw new UnsupportedOperationException("Not yet implemented");
        /*
        ArrayList<StopModel> stops = null;
        
        return stops;*/
    }
    
    /**
     * Retrieves a list of all of the stops in the database as an
     * `ArrayList<StopModel>`.
     * 
     * @return a list of all of the stops in the database.
     */
    public ArrayList<StopModel> getList() {
        return records;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Protected Override Methods">
    @Override
    protected void createAndAddRecord(String[] line) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        
        record = new StopModel();
        
        record.setTripNumber(line[0]);
        record.setStopNumber(Integer.parseInt(line[1]));
        record.setCustomer(Long.parseLong(line[2]));
        
        try {
            if ( line[3].length() > 1 )
                record.setEarlyDate(sdf.parse(line[3]));
        } catch ( ParseException ex ) {
            entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                    + "Parsing error while parsing the early date.");
            entry.setThrown(ex);
            entry.setSourceMethodName("createAndAddRecord");
            entry.setParameters(null);
            Starter.logger.error(entry);
            
            MessageBox.showError(ex, "Data Retrieval Error");
        }
        
        try {
            record.setEarlyTime(line[4]);
        } catch ( InvalidTimeException | ParseException ex ) {
            entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                    + "Parsing error while parsing the late date.");
            entry.setThrown(ex);
            entry.setSourceMethodName("createAndAddRecord");
            entry.setParameters(null);
            Starter.logger.error(entry);
            
            MessageBox.showError(ex, "Data Retrieval Error");            
        }
        
        try {
            if ( line[5].length() > 1 )
                record.setLateDate(sdf.parse(line[5]));
        } catch ( ParseException ex ) {
            entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                    + "Parsing error while parsing the late date.");
            entry.setThrown(ex);
            entry.setSourceMethodName("createAndAddRecord");
            entry.setParameters(null);
            Starter.logger.error(entry);
            
            MessageBox.showError(ex, "Data Retrieval Error");
        }
        
        try {
            record.setLateTime(line[6]);
        } catch ( InvalidTimeException | ParseException ex ) {
            entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                    + "Parsing error while parsing the late date.");
            entry.setThrown(ex);
            entry.setSourceMethodName("createAndAddRecord");
            entry.setParameters(null);
            Starter.logger.error(entry);
            
            MessageBox.showError(ex, "Data Retrieval Error");            
        }
        
        try {
            if ( line[7].length() > 1 )
                record.setArrDate(sdf.parse(line[7]));
        } catch ( ParseException ex ) {
            entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                    + "Parsing error while parsing the late date.");
            entry.setThrown(ex);
            entry.setSourceMethodName("createAndAddRecord");
            entry.setParameters(null);
            Starter.logger.error(entry);
            
            MessageBox.showError(ex, "Data Retrieval Error");
        }
        
        try {
            record.setArrTime(line[8]);
        } catch ( InvalidTimeException | ParseException ex ) {
            entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                    + "Parsing error while parsing the late date.");
            entry.setThrown(ex);
            entry.setSourceMethodName("createAndAddRecord");
            entry.setParameters(null);
            Starter.logger.error(entry);
            
            MessageBox.showError(ex, "Data Retrieval Error");            
        }
        
        try {
            if ( line[9].length() > 1 )
                record.setDepDate(sdf.parse(line[9]));
        } catch ( ParseException ex ) {
            entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                    + "Parsing error while parsing the late date.");
            entry.setThrown(ex);
            entry.setSourceMethodName("createAndAddRecord");
            entry.setParameters(null);
            Starter.logger.error(entry);
            
            MessageBox.showError(ex, "Data Retrieval Error");
        }
        
        try {
            record.setDepTime(line[10]);
        } catch ( InvalidTimeException | ParseException ex ) {
            entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                    + "Parsing error while parsing the late date.");
            entry.setThrown(ex);
            entry.setSourceMethodName("createAndAddRecord");
            entry.setParameters(null);
            Starter.logger.error(entry);
            
            MessageBox.showError(ex, "Data Retrieval Error");            
        }
        
//        record.setSignedBy(record[11]);
        
        records.add(record);
    }
    
    @Override
    protected String buildRecordLine(StopModel model) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        
        return model.getTripNumber() + "~" + model.getStopNumber() + "~"
                + model.getCustomer() + "~" + sdf.format(model.getEarlyDate())
                 + "~" + model.getEarlyTime() + "~" 
                + sdf.format(model.getLateDate()) + "~" + model.getLateTime()
                + "~ ~ ~ ~ ~ ";
    }

    @Override
    public boolean postTransactions() throws DataStoreException {
        throw new UnsupportedOperationException("Not necessary for this object.");
    }
    //</editor-fold>

}
