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
import com.pekinsoft.loadmaster.model.ChartModel;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.1.0
 * @since 0.1.0
 */
public class ChartCtl extends AbstractJournal<ChartModel> {

    //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
    public ChartCtl () throws DataStoreException {
        super(new ChartModel(), Starter.props.getDataFolder() + "coa.tbl");
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
        
        record = new ChartModel();
        
//        record = new ReceivablesModel();
        
        ((ChartModel)record).setNumber(Long.parseLong(line[0]));
        ((ChartModel)record).setName(line[1]);
        ((ChartModel)record).setDescription(line[2]);
        
        records.add(record);
    }
     
    @Override
    protected String buildRecordLine(ChartModel model) {
        return model.getNumber()+ "~" + model.getDescription()+ "~"
                + model.getName();
    }

    @Override
    public boolean postTransactions() throws DataStoreException {
        throw new UnsupportedOperationException("Not necessary for this object.");
    }
    //</editor-fold>

}
