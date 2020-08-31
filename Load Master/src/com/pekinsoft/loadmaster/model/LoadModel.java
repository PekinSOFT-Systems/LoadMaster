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
 *  Class      :   LoadModel.java
 *  Author     :   Sean Carrick
 *  Created    :   Aug 30, 2020 @ 4:43:43 PM
 *  Modified   :   Aug 30, 2020
 *  
 *  Purpose:
 *      Provides the model of the data table for a load. This class contains all
 *      data that is associated with a single load.
 *  
 *  Revision History:
 *  
 *  WHEN          BY                  REASON
 *  ------------  ------------------- ------------------------------------------
 *  Aug 30, 2020  Sean Carrick        Initial creation.
 * *****************************************************************************
 */

package com.pekinsoft.loadmaster.model;

import java.util.Date;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.1.0
 * @since 0.1.0
 */
public class LoadModel {
    //<editor-fold defaultstate="collapsed" desc="Public Static Constants">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Member Fields">
    private String order;
    private String trip;
    private int startOdo;
    private int endOdo;
    private Date dispatch;
    private double rate;
    private int miles;
    private int weight;
    private String commodity;
    private boolean hazMat;
    private boolean tarped;
    private boolean twic;
    private boolean topCust;
    private boolean ltl;
    private boolean cbd;
    private boolean ramps;
    private long broker;
    private StopModel[] stops;
    private String bol;
    private double avgRPM;
    private boolean completed;
    private boolean canelled;
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
    public LoadModel () {
        
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Static Methods">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Instance Methods">
    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public boolean isCanelled() {
        return canelled;
    }

    public void setCanelled(boolean canelled) {
        this.canelled = canelled;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getTrip() {
        return trip;
    }

    public void setTrip(String trip) {
        this.trip = trip;
    }

    public int getStartOdo() {
        return startOdo;
    }

    public void setStartOdo(int startOdo) {
        this.startOdo = startOdo;
    }

    public int getEndOdo() {
        return endOdo;
    }

    public void setEndOdo(int endOdo) {
        this.endOdo = endOdo;
    }

    public Date getDispatch() {
        return dispatch;
    }

    public void setDispatch(Date dispatch) {
        this.dispatch = dispatch;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getMiles() {
        return miles;
    }

    public void setMiles(int miles) {
        this.miles = miles;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        if ( commodity.isBlank() || commodity.isEmpty() )
            commodity = " ";
        
        this.commodity = commodity;
    }

    public boolean isHazMat() {
        return hazMat;
    }

    public void setHazMat(boolean hazMat) {
        this.hazMat = hazMat;
    }

    public boolean isTarped() {
        return tarped;
    }

    public void setTarped(boolean tarped) {
        this.tarped = tarped;
    }

    public boolean isTwic() {
        return twic;
    }

    public void setTwic(boolean twic) {
        this.twic = twic;
    }

    public boolean isTopCust() {
        return topCust;
    }

    public void setTopCust(boolean topCust) {
        this.topCust = topCust;
    }

    public boolean isLtl() {
        return ltl;
    }

    public void setLtl(boolean ltl) {
        this.ltl = ltl;
    }

    public boolean isCbd() {
        return cbd;
    }

    public void setCbd(boolean cbd) {
        this.cbd = cbd;
    }

    public boolean isRamps() {
        return ramps;
    }

    public void setRamps(boolean ramps) {
        this.ramps = ramps;
    }

    public long getBroker() {
        return broker;
    }

    public void setBroker(long broker) {
        this.broker = broker;
    }

    public StopModel[] getStops() {
        return stops;
    }

    public void setStops(StopModel[] stops) {
        this.stops = stops;
    }

    public String getBol() {
        return bol;
    }

    public void setBol(String bol) {
        if ( bol.isBlank() || bol.isEmpty() )
            bol = " ";
        
        this.bol = bol;
    }

    public double getAvgRPM() {
        return avgRPM;
    }

    public void setAvgRPM(double avgRPM) {
        this.avgRPM = avgRPM;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Instance Methods">

    //</editor-fold>

}
