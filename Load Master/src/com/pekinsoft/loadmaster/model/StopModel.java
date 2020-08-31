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
    private Date earlyTime;
    private Date lateDate;
    private Date lateTime;
    private Date arrDate;
    private Date arrTime;
    private Date depDate;
    private Date depTime;
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

    public Date getEarlyTime() {
        return earlyTime;
    }

    public void setEarlyTime(String earlyTime) throws InvalidTimeException,
            ParseException {
        if ( earlyTime.isBlank() || earlyTime.isEmpty() ) 
            throw new InvalidTimeException("Time must be provided.");
        
        int hour = Integer.valueOf(earlyTime.substring(0, earlyTime.indexOf(":")));
        if ( hour < 0 || hour > 23 )
            throw new InvalidTimeException(earlyTime + " is not between 0 and 23.");
        
        int min = Integer.valueOf(earlyTime.substring(earlyTime.indexOf(":") + 1));
        if ( min < 0 || min > 59 )
            throw new InvalidTimeException(earlyTime + " has more than 59 minutes"
                    + " or less than 0 minutes contained within it.");
        
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        this.earlyTime = sdf.parse(earlyTime);
    }

    public Date getLateDate() {
        return lateDate;
    }

    public void setLateDate(Date lateDate) {
        this.lateDate = lateDate;
    }

    public Date getLateTime() {
        return lateTime;
    }

    public void setLateTime(String lateTime) throws InvalidTimeException,
            ParseException {
        if ( lateTime.isBlank() || lateTime.isEmpty() ) 
            throw new InvalidTimeException("Time must be provided.");
        
        int hour = Integer.valueOf(lateTime.substring(0, lateTime.indexOf(":")));
        if ( hour < 0 || hour > 23 )
            throw new InvalidTimeException(lateTime + " is not between 0 and 23.");
        
        int min = Integer.valueOf(lateTime.substring(lateTime.indexOf(":") + 1));
        if ( min < 0 || min > 59 )
            throw new InvalidTimeException(lateTime + " has more than 59 minutes"
                    + " or less than 0 minutes contained within it.");
        
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        this.lateTime = sdf.parse(lateTime);
    }
    
    public Date getArrDate() {
        return arrDate;
    }
    
    public void setArrDate(Date arrDate) {
        this.arrDate = arrDate;
    }
    
    public Date getArrTime() {
        return arrTime;
    }
    
    public void setArrTime(String arrTime) throws InvalidTimeException, 
            ParseException {
        if ( arrTime.isBlank() || arrTime.isEmpty() ) 
            throw new InvalidTimeException("Time must be provided.");
        
        int hour = Integer.valueOf(arrTime.substring(0, arrTime.indexOf(":")));
        if ( hour < 0 || hour > 23 )
            throw new InvalidTimeException(arrTime + " is not between 0 and 23.");
        
        int min = Integer.valueOf(arrTime.substring(arrTime.indexOf(":") + 1));
        if ( min < 0 || min > 59 )
            throw new InvalidTimeException(arrTime + " has more than 59 minutes"
                    + " or less than 0 minutes contained within it.");
        
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        this.arrTime = sdf.parse(arrTime);
    }
    
    public Date getDepDate() {
        return this.depDate;
    }
    
    public void setDepDate(Date depDate) {
        this.depDate = depDate;
    }
    
    public Date getDepTime() {
        return depTime;
    }
    
    public void setDepTime(String depTime) throws InvalidTimeException,
            ParseException {
        if ( depTime.isBlank() || depTime.isEmpty() ) 
            throw new InvalidTimeException("Time must be provided.");
        
        int hour = Integer.valueOf(depTime.substring(0, depTime.indexOf(":")));
        if ( hour < 0 || hour > 23 )
            throw new InvalidTimeException(depTime + " is not between 0 and 23.");
        
        int min = Integer.valueOf(depTime.substring(depTime.indexOf(":") + 1));
        if ( min < 0 || min > 59 )
            throw new InvalidTimeException(depTime + " has more than 59 minutes"
                    + " or less than 0 minutes contained within it.");
        
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        this.depTime = sdf.parse(depTime);
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
