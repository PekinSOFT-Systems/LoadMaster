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
 * 
 * *****************************************************************************
 * *****************************************************************************
 *  Project    :   Load_Master
 *  Class      :   StopModel.java
 *  Author     :   Sean Carrick
 *  Created    :   Aug 30, 2020 @ 4:48:40 PM
 *  Modified   :   Aug 30, 2020
 *  
 *  Purpose:
 *      Model for the stops data table. This class contains all stop-related 
 *      information.
 *  
 *  Revision History:
 *  
 *  WHEN          BY                  REASON
 *  ------------  ------------------- ------------------------------------------
 *  Aug 30, 2020  Sean Carrick        Initial creation.
 *  Oct 10, 2020  Sean Carrick        Modified our class to store all times as
 *                                    String objects and to attempt a parsing of
 *                                    the provided time to a Date object in the
 *                                    setter methods to verify that valid times
 *                                    were provided.
 * *****************************************************************************
 */

package com.pekinsoft.loadmaster.model;

import com.pekinsoft.loadmaster.err.InvalidTimeException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.1.0
 * @since 0.1.0
 */
public class StopModel {
    //<editor-fold defaultstate="collapsed" desc="Public Static Constants">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Member Fields">
    private String tripNumber;
    private int stopNumber;
    private long customer;
    private Date earlyDate;
    private String earlyTime;
    private Date lateDate;
    private String lateTime;
    private Date arrDate;
    private String arrTime;
    private Date depDate;
    private String depTime;
    private String signedBy;
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Static Initializer">
    static {
        
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Intstance Initializer">
    {
        
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
    public StopModel () {
        
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Static Methods">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Instance Methods">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Instance Methods">
    public int getStopNumber() {
        return stopNumber;
    }

    public void setStopNumber(int stopNumber) {
        this.stopNumber = stopNumber;
    }

    public long getCustomer() {
        return customer;
    }

    public void setCustomer(long customer) {
        this.customer = customer;
    }

    public Date getEarlyDate() {
        return earlyDate;
    }

    public void setEarlyDate(Date earlyDate) {
        this.earlyDate = earlyDate;
    }

    public String getEarlyTime() {
        return earlyTime;
    }

    public void setEarlyTime(String earlyTime) throws InvalidTimeException,
            ParseException {
//        if ( earlyTime.isBlank() || earlyTime.isEmpty() ) 
//            throw new InvalidTimeException("Time must be provided.");
        
        try {
            if ( earlyTime.length() > 1 ) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                Date time = sdf.parse(earlyTime);
            }
        } catch ( ParseException ex ) {
            throw new InvalidTimeException("The provided time, " + depTime 
                    + ", is invalid.");
        }

        this.earlyTime = earlyTime;
    }

    public Date getLateDate() {
        return lateDate;
    }

    public void setLateDate(Date lateDate) {
        this.lateDate = lateDate;
    }

    public String getLateTime() {
        return lateTime;
    }

    public void setLateTime(String lateTime) throws InvalidTimeException,
            ParseException {
//        if ( lateTime.isBlank() || lateTime.isEmpty() ) 
//            throw new InvalidTimeException("Time must be provided.");
        
        try {
            if ( lateTime.length() > 1 ) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                Date time = sdf.parse(lateTime);
            }
        } catch ( ParseException ex ) {
            throw new InvalidTimeException("The provided time, " + depTime 
                    + ", is invalid.");
        }
        
        this.lateTime = lateTime;
    }
    
    public Date getArrDate() {
        return arrDate;
    }
    
    public void setArrDate(Date arrDate) {
        this.arrDate = arrDate;
    }
    
    public String getArrTime() {
        return arrTime;
    }
    
    public void setArrTime(String arrTime) throws InvalidTimeException, 
            ParseException {
//        if ( arrTime.isBlank() || arrTime.isEmpty() ) 
//            throw new InvalidTimeException("Time must be provided.");
        
        try {
            if ( arrTime.length() > 1 ) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                Date time = sdf.parse(arrTime);
            }
        } catch ( ParseException ex ) {
            throw new InvalidTimeException("The provided time, " + depTime 
                    + ", is invalid.");
        }
        
        this.arrTime = arrTime;
    }
    
    public Date getDepDate() {
        return this.depDate;
    }
    
    public void setDepDate(Date depDate) {
        this.depDate = depDate;
    }
    
    public String getDepTime() {
        return depTime;
    }
    
    public void setDepTime(String depTime) throws InvalidTimeException,
            ParseException {
//        if ( depTime.isBlank() || depTime.isEmpty() ) 
//            throw new InvalidTimeException("Time must be provided.");
        
        try {
            if ( depTime.length() > 1 ) {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                Date time = sdf.parse(depTime);
            }
        } catch ( ParseException ex ) {
            throw new InvalidTimeException("The provided time, " + depTime 
                    + ", is invalid.");
        }
            
        this.depTime = depTime;
    }

    public String getSignedBy() {
        return signedBy;
    }
    
    public void setSignedBy(String signedBy) {
        this.signedBy = signedBy;
    }

    public String getTripNumber() {
        return tripNumber;
    }

    public void setTripNumber(String tripNumber) {
        this.tripNumber = tripNumber;
    }
    //</editor-fold>

}
