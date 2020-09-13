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
 *  Sep 01, 2020  Sean Carrick        Added team and pieces fields. Modified the
 *                                    getAvgRPM() method to calculate the 
 *                                    average rate per mile based on the number
 *                                    of miles actually driven and the gross pay
 *                                    for the load. Added JavaDoc documentation
 *                                    to the public methods.
 * *****************************************************************************
 */

package com.pekinsoft.loadmaster.model;

import java.util.ArrayList;
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
    private int pieces;
    private String commodity;
    private boolean hazMat;
    private boolean tarped;
    private boolean team;
    private boolean twic;
    private boolean topCust;
    private boolean ltl;
    private boolean cbd;
    private boolean ramps;
    private long broker;
    private ArrayList<StopModel> stops;
    private String bol;
    private double avgRPM;
    private boolean completed;
    private boolean cancelled;
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
    /**
     * Determines whether load has been completed in its entirety.
     * 
     * @return 
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * Sets whether load is complete.
     * 
     * @param completed 
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    /**
     * Determines if the load has been cancelled.
     * 
     * @return 
     */
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * Sets the load cancelled flag.
     * 
     * @param cancelled 
     */
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    /**
     * Retrieves the order number of the load.
     * 
     * @return 
     */
    public String getOrder() {
        return order;
    }

    /**
     * Sets the order number of the load.
     * 
     * @param order 
     */
    public void setOrder(String order) {
        this.order = order;
    }

    /**
     * Retrieves the trip number for the load.
     * 
     * @return 
     */
    public String getTrip() {
        return trip;
    }

    /**
     * Sets the trip number for the load.
     * 
     * @param trip 
     */
    public void setTrip(String trip) {
        this.trip = trip;
    }

    /**
     * Retrieves the odometer reading at the start of the load.
     * 
     * @return 
     */
    public int getStartOdo() {
        return startOdo;
    }

    /**
     * Sets the odometer reading at the start of the load.
     * 
     * @param startOdo 
     */
    public void setStartOdo(int startOdo) {
        this.startOdo = startOdo;
    }

    /**
     * Retrieves the odometer reading at the end of the load.
     * 
     * @return 
     */
    public int getEndOdo() {
        return endOdo;
    }

    /**
     * Sets the odometer reading at the end of the load.
     * 
     * @param endOdo 
     */
    public void setEndOdo(int endOdo) {
        this.endOdo = endOdo;
    }

    /**
     * Retrieves the date the load was dispatched.
     * 
     * @return 
     */
    public Date getDispatch() {
        return dispatch;
    }

    /**
     * Sets the date the load was dispatched.
     * 
     * @param dispatch 
     */
    public void setDispatch(Date dispatch) {
        this.dispatch = dispatch;
    }

    /**
     * Retrieves the gross pay for the load.
     * 
     * @return 
     */
    public double getRate() {
        return rate;
    }

    /**
     * Sets the gross pay for the load.
     * 
     * @param rate 
     */
    public void setRate(double rate) {
        this.rate = rate;
    }

    /**
     * Retrieves the number of miles dispatched for the load.
     * 
     * @return 
     */
    public int getMiles() {
        return miles;
    }

    /**
     * Sets the number of miles dispatched for the load.
     * 
     * @param miles 
     */
    public void setMiles(int miles) {
        this.miles = miles;
    }

    /**
     * Retrieves the gross weight of the product on the load.
     * 
     * @return 
     */
    public int getWeight() {
        return weight;
    }

    /**
     * Sets the gross weight of the product on the load.
     * 
     * @param weight 
     */
    public void setWeight(int weight) {
        this.weight = weight;
    }

    /**
     * Retrieves the product being hauled on the load.
     * 
     * @return 
     */
    public String getCommodity() {
        return commodity;
    }

    /**
     * Sets the product being hauled on the load.
     * 
     * @param commodity 
     */
    public void setCommodity(String commodity) {
        if ( commodity.isBlank() || commodity.isEmpty() )
            commodity = " ";
        
        this.commodity = commodity;
    }

    /**
     * Determines if the load involves hauling hazardous materials.
     * 
     * @return 
     */
    public boolean isHazMat() {
        return hazMat;
    }

    /**
     * Sets the hazardous material flag for the load.
     * 
     * @param hazMat 
     */
    public void setHazMat(boolean hazMat) {
        this.hazMat = hazMat;
    }

    /**
     * Determines if the load requires to be tarped.
     * 
     * @return 
     */
    public boolean isTarped() {
        return tarped;
    }

    /**
     * Sets the tarp required flag for the load.
     * 
     * @param tarped 
     */
    public void setTarped(boolean tarped) {
        this.tarped = tarped;
    }

    /**
     * Deterimines if a Transportation Worker Identification Credential (TWIC) 
     * is required to haul the load.
     * 
     * @return 
     */
    public boolean isTwic() {
        return twic;
    }

    /**
     * Sets the Transportation Worker Identification Credential (TWIC) 
     * requirement flag for the load.
     * @param twic 
     */
    public void setTwic(boolean twic) {
        this.twic = twic;
    }

    /**
     * Determines if any of the customers on the load are top customers for the
     * company.
     * 
     * @return 
     */
    public boolean isTopCust() {
        return topCust;
    }

    /**
     * Sets the top customer flag for the load if any one of the customers on 
     * the load are top customers for the company.
     * 
     * @param topCust 
     */
    public void setTopCust(boolean topCust) {
        this.topCust = topCust;
    }

    /**
     * Determines if the load is Less Than Truckload (LTL)
     * 
     * @return 
     */
    public boolean isLtl() {
        return ltl;
    }

    /**
     * Sets the Less Than Truckload (LTL) flag for the load.
     * 
     * @param ltl 
     */
    public void setLtl(boolean ltl) {
        this.ltl = ltl;
    }

    /**
     * Determines if the load is Count By Driver (CBD) or Signature and Talley.
     * 
     * @return 
     */
    public boolean isCbd() {
        return cbd;
    }

    /**
     * Sets the Count By Driver (CBD) or Signature and Talley flag for the load.
     * 
     * @param cbd 
     */
    public void setCbd(boolean cbd) {
        this.cbd = cbd;
    }

    /**
     * Determines if the load requires ramps to be provided.
     * 
     * @return 
     */
    public boolean isRamps() {
        return ramps;
    }

    /**
     * Sets the ramps required flag for the load.
     * 
     * @param ramps 
     */
    public void setRamps(boolean ramps) {
        this.ramps = ramps;
    }

    /**
     * Retrieves the broker or agent ID number who booked the freight.
     * 
     * @return 
     */
    public long getBroker() {
        return broker;
    }

    /**
     * Sets the broker or agent ID number who booked the freight.
     * 
     * @param broker 
     */
    public void setBroker(long broker) {
        this.broker = broker;
    }

    /**
     * Retrieves all of the stops for the load.
     * 
     * @return 
     */
    public ArrayList<StopModel> getStops() {
        return stops;
    }

    /**
     * Sets all of the stops for the load.
     * 
     * @param stops 
     */
    public void addStop(StopModel stop) {
        this.stops.add(stop);
    }
    
    /**
     * Retrieves the number of stops on this load.
     * 
     * @return 
     */
    public int getStopCount() {
        return this.stops.size();
    }

    /**
     * Retrieves the Bill Of Lading (BOL) number for the load.
     * 
     * @return 
     */
    public String getBol() {
        return bol;
    }

    /**
     * Sets the Bill Of Lading (BOL) number for the load.
     * 
     * @param bol 
     */
    public void setBol(String bol) {
        if ( bol.isBlank() || bol.isEmpty() )
            bol = " ";
        
        this.bol = bol;
    }

    /**
     * Retrieves the average Rate Per Mile (RPM) that is paid on the load.
     * 
     * <dl>
     *  <dt><strong><em>Note</em></strong></dt>
     *  <dd>The average rate per mile (RPM) will only be calculated at the end
     *      of a load, once the ending odometer is entered. This field value
     *      does not get entered manually on any dialog. It is purely a 
     *      calculated field.</dd>
     * </dl>
     * 
     * @return 
     */
    public double getAvgRPM() {
        return rate / (endOdo - startOdo);
    }
    
    /**
     * Retrieves the number of pieces contained on the load.
     * 
     * <dl>
     *  <dt><strong><em>Note</em></strong></dt>
     *  <dd>This number may be either the actual number of pieces or the number
     *      of lifts/pallets/bundles.</dd>
     * </dl>
     * 
     * @return 
     */
    public int getPieces() {
        return pieces;
    }

    /**
     * Sets the number of pieces contained on the load.
     * 
     * <dl>
     *  <dt><strong><em>Note</em></strong></dt>
     *  <dd>This number may be either the actual number of pieces or the number
     *      of lifts/pallets/bundles.</dd>
     * </dl>
     * 
     * @param pieces 
     */
    public void setPieces(int pieces) {
        this.pieces = pieces;
    }

    /**
     * Determines if this load requires a team to drive it to its destination.
     * 
     * @return 
     */
    public boolean isTeam() {
        return team;
    }

    /**
     * Sets the team flag for this load if it requires a team to drive it to its
     * destination.
     * 
     * @param team 
     */
    public void setTeam(boolean team) {
        this.team = team;
    }

    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Instance Methods">

    //</editor-fold>

}
