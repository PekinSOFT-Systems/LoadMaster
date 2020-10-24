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
 * Class   :   JournalInterface
 * Author  :   Sean Carrick
 * Created :   Oct 23, 2020
 * Modified:   Oct 23, 2020
 * 
 * Purpose:
 *     An interface to be used by all models that require access to data files.
 * 
 * WHEN          BY                  REASON
 * ------------  ------------------  -------------------------------------------
 * Oct 23, 2020  Sean Carrick        Initial creation.
 * 
 * *****************************************************************************
 */

package com.pekinsoft.loadmaster.api;

import com.pekinsoft.loadmaster.model.EntryModel;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.1.0
 * @since 0.1.0
 */
public interface JournalInterface {

    /**
     * Loads data from the journal into the model.
     * 
     * @param data the journal data to load.
     */
    public void load(String[] data);
    
    /**
     * Prepares the model data to be written to the journal.
     * 
     * @return data formatted for file.
     */
    public String buildRecordLine();
    
    /**
     * Creates a General Ledger entry, formatted to be written to the General
     * Ledger file.
     * 
     * @return data formatted for the General Ledger file.
     */
    public EntryModel getGeneralLedgerEntry();
    
    /**
     * Determines whether the journal entry has been posted to the General 
     * Ledger.
     * 
     * @return `true` if already posted to the General Ledger, `false` otherwise.
     */
    public boolean isPosted();
    
}
