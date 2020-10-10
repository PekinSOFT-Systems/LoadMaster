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

import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.controller.CustomerCtl;
import com.pekinsoft.loadmaster.err.DataStoreException;
import com.pekinsoft.loadmaster.model.CustomerModel;
import java.util.logging.Level;
import java.util.logging.LogRecord;
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
    private CustomerModel cust;
    private CustomerCtl table;
    private LogRecord entry;
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
        
        entry = new LogRecord(Level.ALL, "Logging intitated for SummaryPage.");
        entry.setSourceClassName(this.getClass().getName());
        entry.setSourceMethodName("SummaryPage() - Default Constructor");
        entry.setParameters(null);
        
        map = getWizardDataMap();
        
        // We will use the `wizardData` Map to get the settings that the user
        //+ selected during this wizard and show them a summary, in HTML.
        StringBuilder summary = new StringBuilder();
        
        summary.append("<html><body><h1 style=\"text-align: center\">");
        summary.append("Book Load Summary</h1>");
        summary.append("<h2>Load Information</h2");
        summary.append("<table><thead><tr><th>Setting</th><th>Value</th></tr>");
        summary.append("</thead><tbody>");
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
        summary.append("</td></tr></tbody><table>");
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
        
        summary.append("<h2>Stop Information</h2>");
        summary.append("<table><thhead><tr><th>Stop #</th><th>Company</th>");
        summary.append("<th>Street</th><th>Suite</th><th>City</th>");
        summary.append("<th>State</th><th>Zip Code</th><th>Phone</th></tr>");
        summary.append("</thead><tbody>");
        
        // To list out the stops, we want to list only the stop number, the
        //+ company, street, suite, city, state, Zip Code, and phone number. In
        //+ order to accomplish this, we will need to loop through all keys in
        //+ the data map, appending the stop number to the word "stop". Once we
        //+ have the customer ID for the stop, we will need to get that customer
        //+ record from the database. To accomplish this, we will need to get
        //+ the data from the customers table using a CustomerCtl object and a
        //+ long value of the String of the customer ID stored in the data map.
        //+ We will use a CustomerModel object to hold the record that matches
        //+ the customer we are seeking.
        
        try {
            table = new CustomerCtl();
            
            // If we are successful in opening the data table, we can then 
            //+ search for the customer we need. However, we need to loop through
            //+ all of the stops the user entered to get the customer ID numbers.
            for ( int x = 1; x == Starter.props.getPropertyAsInt("stop.count", 
                    "0"); x++) {
                long desiredID = Long.valueOf(map.get("stop" + x).toString());
                
                // Now that we have the customer ID we need to match, we need to
                //+ loop through all of the records in the customer table until
                //+ we find that customer record.
                do {
                    if ( table.get().getId() == desiredID ) {
                        cust = table.get();
                        
                        // Exit the loop.
                        break;
                    } else
                        table.next();
                }  while ( table.hasNext() );
                
                // Now that we have gotten the appropriate customer information
                //+ for the stop, we can add the info to the summary page.
                summary.append("<tr><td>").append(x).append("</td><td>");
                summary.append(cust.getCompany()).append("</td><td>");
                summary.append(cust.getStreet()).append("</td><td>");
                summary.append(cust.getSuite()).append("</td><td>");
                summary.append(cust.getCity()).append("</td><td>");
                summary.append(cust.getState()).append("</td><td>");
                summary.append(cust.getZip()).append("</td><td>");
                summary.append(cust.getPhone()).append("</td></tr>");
            }
        } catch ( DataStoreException ex ) {
            entry.setThrown(ex);
            Starter.logger.error(entry);
        }
        
        // Once we have added the information on each of the stops, we need to
        //+ close out our table and our document.
        summary.append("</tbody></table></body></html>");
        
        // Then, we need to put our document in our summary editor.
        summaryEditor.setText(summary.toString());
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
