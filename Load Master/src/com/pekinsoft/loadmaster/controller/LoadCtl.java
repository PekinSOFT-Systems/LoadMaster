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
 *   Module:   LoadCtl
 *   Created:  Sep 06, 2020
 *   Modified: Sep 06, 2020
 * 
 *   Purpose:
 *      Provides all data access functionality for the records.
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
import com.pekinsoft.loadmaster.model.LoadModel;
import com.pekinsoft.loadmaster.model.StopModel;
import com.pekinsoft.loadmaster.utils.MessageBox;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.1.0
 * @since 0.1.0
 */
public class LoadCtl extends AbstractJournal<LoadModel> {

    //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
    public LoadCtl () throws DataStoreException {
        super(new LoadModel(), Starter.props.getDataFolder() + "loads.tbl");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Instance Methods">

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Protected Override Methods">
    @Override
    protected void createAndAddRecord(String[] line) {
        entry.setMessage("Entering...");
        entry.setSourceMethodName("createAndAddRecord");
        entry.setParameters(line);
        Starter.logger.enter(entry);
        
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        
        record = new LoadModel();
        
        record.setOrder(line[0]);
        record.setTrip(line[1]);
        record.setStartOdo(Integer.valueOf(line[2]));
        record.setEndOdo(Integer.valueOf(line[3]));
        
        try {
            record.setDispatch(sdf.parse(line[4]));
        } catch ( ParseException ex ) {
            entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                    + "Parsing error while parsing the dispatch date.");
            entry.setThrown(ex);
            entry.setSourceMethodName("createAndAddRecord");
            entry.setParameters(null);
            Starter.logger.error(entry);
            
            MessageBox.showError(ex, "Parsing Error");
        }
        
        record.setRate(Double.valueOf(line[5]));
        record.setMiles(Integer.valueOf(line[6]));
        record.setWeight(Integer.valueOf(line[7]));
        record.setPieces(Integer.valueOf(line[8]));
        record.setCommodity(line[9]);
        record.setHazMat(Boolean.parseBoolean(line[10]));
        record.setTarped(Boolean.parseBoolean(line[11]));
        record.setTeam(Boolean.parseBoolean(line[12]));
        record.setTwic(Boolean.parseBoolean(line[13]));
        record.setTopCust(Boolean.parseBoolean(line[14]));
        record.setLtl(Boolean.parseBoolean(line[15]));
        record.setCbd(Boolean.parseBoolean(line[16]));
        record.setRamps(Boolean.parseBoolean(line[17]));
        record.setBroker(Long.valueOf(line[18]));
        record.setBol(line[19]);
        record.setCompleted(Boolean.parseBoolean(line[20]));
        record.setCancelled(Boolean.parseBoolean(line[21]));
        
        records.add(record);
        
        StopCtl stops = null;
        
        try {
            stops = new StopCtl();
        } catch ( DataStoreException ex ) {
            entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                    + "Throwing DataStoreException to calling method...");
            entry.setThrown(ex);
            entry.setSourceMethodName("connect");
            entry.setParameters(null);
            Starter.logger.error(entry);
        }
        
        if ( stops != null ) {
            for ( StopModel stop : stops.getList() ) {
                if ( stop.getTripNumber().equals(record.getTrip()) ) {
                    record.addStop(stop);
                }
//                LoadMaster.fileProgress.setValue(
//                        LoadMaster.fileProgress.getValue() + 1);
            }
        }
    }
    
    protected String buildRecordLine(LoadModel model) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        
        return model.getOrder() + "~" + model.getTrip() + "~" 
                + model.getStartOdo() + "~" + model.getEndOdo() + "~" 
                + sdf.format(model.getDispatch()) + "~" + model.getRate()
                + "~" + model.getMiles() + "~" + model.getWeight() + "~" 
                + model.getPieces() + "~" + model.getCommodity() + "~" 
                + model.isHazMat() + "~" + model.isTarped()+ "~" 
                + model.isTeam() + "~" + model.isTwic() + "~" 
                + model.isTopCust() + "~" + model.isLtl()+ "~" + model.isCbd()
                + "~" + model.isRamps() + "~" + model.getBroker() + "~" 
                + model.getBol() + "~" + "~" + model.isCompleted() + "~" 
                + model.isCancelled();
    }

    @Override
    public boolean postTransactions() throws DataStoreException {
        throw new UnsupportedOperationException("Not necessary for this object.");
    }
    //</editor-fold>


}
