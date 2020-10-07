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
 *  Class      :   SummaryPage.java
 *  Author     :   Sean Carrick
 *  Created    :   Oct 5, 2020 @ 7:21:34 PM
 *  Modified   :   Oct 5, 2020
 *  
 *  Purpose:
 *  
 *  Revision History:
 *  
 *  WHEN          BY                  REASON
 *  ------------  ------------------- ------------------------------------------
 *  Oct 5, 2020  Sean Carrick        Initial creation.
 * *****************************************************************************
 */

package com.pekinsoft.loadmaster.view.wiz.book;

import java.util.Map;
import org.netbeans.spi.wizard.WizardPage;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.1.0
 * @since 0.1.0
 */
public class SummaryPage extends WizardPage {
    //<editor-fold defaultstate="collapsed" desc="Public Static Constants">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Member Fields">
    private Map map;
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
    public SummaryPage () {
        super();
        
        map = super.getWizardDataMap();
        
        // We will use the `wizardData` Map to get the settings that the user
        //+ selected during this wizard and show them a summary, in HTML.
        StringBuilder summary = new StringBuilder();
        
        summary.append("<html><body><h1 style=\"text-align: center\">");
        summary.append("Book Load Summary</h1>");
        summary.append("<h2>Load Information</h2");
        summary.append("<table><th><td>Setting</td><td>Value</td></th>");
        summary.append("<tr><td>Order #</td><td>");
        summary.append(map.get("order").toString());
        summary.append("</td></tr><tr><td>Trip #");
        summary.append(map.get("trip").toString());
        summary.append("</td></tr><tr><td>Gross Pay</td><td>");
        summary.append(map.get("truck.pay").toString());
        summary.append("</td></tr><tr><td>Loaded Miles</td><td>");
        summary.append(map.get("miles").toString());
        summary.append("</td></tr><tr><td>Commodity</td><td>");
        summary.append(map.get("commodity").toString());
        summary.append("</td></tr><table>");
        summary.append("<p>Other Load Information:<br><br>");
        
        // For the checkboxes on the LoadPage, we will use if statements to 
        //+ print "human-readable" information, such as HazMat Load
        if ( Boolean.valueOf(map.get("hazmat").toString()) == true ) 
            summary.append("HazMat Load<br>");
        if ( Boolean.valueOf(map.get("tarped").toString()) )
            summary.append("Load Must be Tarped<br>");
        if ( Boolean.valueOf(map.get("team").toString())) 
            summary.append("Team Drivers Required");
        if ( Boolean.valueOf(map.get("ltl").toString())) {
            summary.append("Less Than Truckload<br>");
            summary.append("<div style=\"float: right; background-color:");
            summary.append(" CornflowerBlue;>To add additional stops, simply ");
            summary.append("run the Add Stop Wizard for each additional stop.");
            summary.append("</div><br>");
        }
        if ( Boolean.valueOf(map.get("top.customer").toString()) )
            summary.append("This is a Top Customer: Do Great Work<br>");
        if ( Boolean.valueOf(map.get("twic").toString()) )
            summary.append("TWIC Card is <strong>Required</strong><br>");
        if ( Boolean.valueOf(map.get("cbd").toString()) )
            summary.append("Count By Driver (Signature & Tally)<br>");
        if ( Boolean.valueOf(map.get("ramps").toString()) )
            summary.append("Ramps are <strong>Required</strong><br>");
        summary.append("</p>");
        
        summary.append("<h2>Broker/Agent Information</h2><p>");
        summary.append("Broker: ");
        summary.append(map.get("Broker"));
        summary.append("<br>");
        
        // Again, only report if the data is present, similar to the checkboxes.
        if ( !map.get("Phone").toString().equals("(   )    -    ") ) {
            summary.append("Phone #: ");
            summary.append(map.get("Phone").toString());
            summary.append("<br");
        }
        if ( !map.get("Fax").toString().equals("(   )    -    ") ) {
            summary.append("Fax #: ");
            summary.append(map.get("Fax").toString());
            summary.append("<br>");
        }
        if ( map.get("Email").toString().length() > 0 ) {
            summary.append("Email: ");
            summary.append(map.get("Email").toString());
            summary.append("<br>");
        }
        summary.append("</p>");
        
        summary.append("<h2>Shipper's Information</h2><p>");
        summary.append("Customer ID: ").append(map.get("shipCustomerID").toString());
        summary.append("<br>Company").append(map.get("shipCompany").toString());
        summary.append("<br>Address: ");
        summary.append(map.get("shipStreet").toString());
        if ( map.get("shipSuite").toString().length() > 0 )
            summary.append(", ").append(map.get("shipSuite").toString());
        summary.append(", ").append(map.get("shipCity").toString());
        summary.append(", ").append(map.get("shipState").toString());
        summary.append("  ").append(map.get("shipZipCode").toString());
        summary.append("<br>");
        if ( map.get("shipContact").toString().length() > 0 )
            summary.append(map.get("shipContact").toString()).append("<br>");
        if ( !map.get("shipPhone").toString().equals("(   )    -    ") )
            summary.append(map.get("shipPhone").toString()).append("<br>");
        summary.append("Early Arrival: ").append(map.get("shipEarlyDate").toString());
        summary.append(" @ ").append(map.get("shipEarlyTime").toString());
        summary.append("<br>");
        summary.append("Late Arrival: ").append(map.get("shipLateDate").toString());
        summary.append(" @ ").append(map.get("shipLateTime").toString());
        summary.append("</p>");
        
        summary.append("<h2>Final Delivery Information</h2><p>");
        summary.append("Customer ID: ").append(map.get("recCustomerID").toString());
        summary.append("<br>Company").append(map.get("recCompany").toString());
        summary.append("<br>Address: ");
        summary.append(map.get("recStreet").toString());
        if ( map.get("recSuite").toString().length() > 0 )
            summary.append(", ").append(map.get("recSuite").toString());
        summary.append(", ").append(map.get("recCity").toString());
        summary.append(", ").append(map.get("recState").toString());
        summary.append("  ").append(map.get("recZipCode").toString());
        summary.append("<br>");
        if ( map.get("recContact").toString().length() > 0 )
            summary.append(map.get("recContact").toString()).append("<br>");
        if ( !map.get("recPhone").toString().equals("(   )    -    ") )
            summary.append(map.get("recPhone").toString()).append("<br>");
        summary.append("Early Arrival: ").append(map.get("recEarlyDate").toString());
        summary.append(" @ ").append(map.get("recEarlyTime").toString());
        summary.append("<br>");
        summary.append("Late Arrival: ").append(map.get("recLateDate").toString());
        summary.append(" @ ").append(map.get("recLateTime").toString());
        summary.append("</p>");
    }
    //</editor-fold>

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        summaryEditor = new javax.swing.JEditorPane();


        jScrollPane1.setViewportView(summaryEditor);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 624, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 391, Short.MAX_VALUE)
        );
    }// </editor-fold>                        

    // Variables declaration - do not modify                     
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JEditorPane summaryEditor;
    // End of variables declaration                   

}
