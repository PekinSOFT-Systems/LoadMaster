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
 * Class   :   PayablesModel
 * Author  :   Sean Carrick
 * Created :   Oct 25, 2020
 * Modified:   Oct 25, 2020
 * 
 * Purpose:
 *     [Provide a general purpose overview for this class]
 * 
 * WHEN          BY                  REASON
 * ------------  ------------------  -------------------------------------------
 * Oct 25, 2020  Sean Carrick        Initial creation.
 * 
 * *****************************************************************************
 */

package com.pekinsoft.loadmaster.model;

import com.pekinsoft.loadmaster.Starter;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.1.0
 * @since 0.1.0
 */
public class PayablesModel {
    //<editor-fold defaultstate="collapsed" desc="Public Static Constants">
    public static final int ACCOUNT_NUMBER = 9999;
    public static final String JOURNAL = Starter.props.getDataFolder() 
            + ACCOUNT_NUMBER;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Member Fields">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Static Initializer">
    static {
        
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
    public PayablesModel () {
        
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Static Methods">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Instance Methods">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Instance Methods">
    
    //</editor-fold>

}
