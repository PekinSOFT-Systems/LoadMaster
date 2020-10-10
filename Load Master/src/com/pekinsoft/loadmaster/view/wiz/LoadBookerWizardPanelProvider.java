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
 *  Class      :   LoadBookerWizardPanelProvider.java
 *  Author     :   Sean Carrick
 *  Created    :   Sep 7, 2020 @ 11:08:59 AM
 *  Modified   :   Sep 7, 2020
 *  
 *  Purpose:
 *  
 *  Revision History:
 *  
 *  WHEN          BY                  REASON
 *  ------------  ------------------- ------------------------------------------
 *  Sep 7, 2020  Sean Carrick        Initial creation.
 * *****************************************************************************
 */
package com.pekinsoft.loadmaster.view.wiz;

import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.controller.LoadCtl;
import com.pekinsoft.loadmaster.controller.StopCtl;
import com.pekinsoft.loadmaster.err.DataStoreException;
import com.pekinsoft.loadmaster.model.LoadModel;
import com.pekinsoft.loadmaster.model.StopModel;
import com.pekinsoft.loadmaster.utils.MessageBox;
import com.pekinsoft.loadmaster.view.wiz.book.BrokerPage;
import com.pekinsoft.loadmaster.view.wiz.book.LoadPage;
import com.pekinsoft.loadmaster.view.wiz.book.StopsPage;
import com.pekinsoft.loadmaster.view.wiz.book.SummaryPage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.Map;
import javax.swing.JComponent;
import org.netbeans.spi.wizard.WizardController;
import org.netbeans.spi.wizard.WizardPanelProvider;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 *
 * @version 0.1.0
 * @since 0.1.0
 */
public class LoadBookerWizardPanelProvider extends WizardPanelProvider
        implements ActionListener {
    //<editor-fold defaultstate="collapsed" desc="Public Static Constants">

    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Private Member Fields">
    private Map settings;
    private WizardController controller;

    private LoadCtl loads;
    private LoadModel load;
    private StopCtl stops;
    private StopModel stop;
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
    public LoadBookerWizardPanelProvider() {
        super(new String[]{"load", "broker", "stops", "summary"}, 
                new String[]{"Load Information", "Broker Information",
            "StopsPage", "Summary"});
    }

    public LoadBookerWizardPanelProvider(String[] steps, String[] descriptions) {
        super(steps, descriptions);

        entry = new LogRecord(Level.ALL, "Configuring Book Load Wizard...");
        entry.setSourceClassName(this.getClass().getName());
        entry.setSourceMethodName("LoadBookerWizardPanelProvider");
        entry.setMessage("super() called. Configuring other objects...");
        Starter.logger.enter(entry);

        try {
            loads = new LoadCtl();
        } catch (DataStoreException ex) {
            entry.setMessage("An error occurred accessing the loads database.");
            entry.setThrown(ex);
            Starter.logger.error(entry);

            MessageBox.showError(ex, "Loads Database Error");
        }

        try {
            stops = new StopCtl();
        } catch (DataStoreException ex) {
            entry.setMessage("An error occurred accessing the stops database.");
            entry.setThrown(ex);
            Starter.logger.error(entry);

            MessageBox.showError(ex, "Stops Database Error");
        }

        load = new LoadModel();
        stop = new StopModel();
    }

    public LoadBookerWizardPanelProvider(String title, String[] steps,
            String[] descriptions) {
        super(title, steps, descriptions);

        entry = new LogRecord(Level.ALL, "Configuring Book Load Wizard...");
        entry.setSourceClassName(this.getClass().getName());
        entry.setSourceMethodName("LoadBookerWizardPanelProvider");
        entry.setMessage("super() called. Configuring other objects...");
        Starter.logger.enter(entry);

        try {
            loads = new LoadCtl();
        } catch (DataStoreException ex) {
            entry.setMessage("An error occurred accessing the loads database.");
            entry.setThrown(ex);
            Starter.logger.error(entry);

            MessageBox.showError(ex, "Loads Database Error");
        }

        try {
            stops = new StopCtl();
        } catch (DataStoreException ex) {
            entry.setMessage("An error occurred accessing the stops database.");
            entry.setThrown(ex);
            Starter.logger.error(entry);

            MessageBox.showError(ex, "Stops Database Error");
        }

        load = new LoadModel();
        stop = new StopModel();
    }

    public LoadBookerWizardPanelProvider(String title, String singleStep,
            String singleDescription) {
        super(title, singleStep, singleDescription);

        entry = new LogRecord(Level.ALL, "Configuring Book Load Wizard...");
        entry.setSourceClassName(this.getClass().getName());
        entry.setSourceMethodName("LoadBookerWizardPanelProvider");
        entry.setMessage("super() called. Configuring other objects...");
        Starter.logger.enter(entry);

        try {
            loads = new LoadCtl();
        } catch (DataStoreException ex) {
            entry.setMessage("An error occurred accessing the loads database.");
            entry.setThrown(ex);
            Starter.logger.error(entry);

            MessageBox.showError(ex, "Loads Database Error");
        }

        try {
            stops = new StopCtl();
        } catch (DataStoreException ex) {
            entry.setMessage("An error occurred accessing the stops database.");
            entry.setThrown(ex);
            Starter.logger.error(entry);

            MessageBox.showError(ex, "Stops Database Error");
        }

        load = new LoadModel();
        stop = new StopModel();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Static Methods">
    //</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="Public Instance Methods">
    @Override
    public void actionPerformed(ActionEvent e) {
        String name;
        Object value;
        // Here, we need to store the entered load data to our objects.
        //+ In order to do this, we need to determine what type of JComponent
        //+ the event source is.
        if (e.getSource() instanceof javax.swing.JTextField) {
            // In this block, we will handle storing the data from our text
            //+ fields. Store the data to the appropriate
            name = ((javax.swing.JTextField) e.getSource()).getName();
            value = ((javax.swing.JTextField) e.getSource()).getText();

            settings.put(name, value);
        } else if (e.getSource() instanceof javax.swing.JFormattedTextField) {
            name = ((javax.swing.JFormattedTextField) e.getSource()).getName();
            value = ((javax.swing.JFormattedTextField) e.getSource()).getText();

            settings.put(name, value);
        } else if (e.getSource() instanceof javax.swing.JCheckBox) {
            name = ((javax.swing.JCheckBox) e.getSource()).getName();
            value = ((javax.swing.JCheckBox) e.getSource()).isSelected()
                    ? Boolean.TRUE : Boolean.FALSE;

            settings.put(name, value);
        }

    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Instance Methods">
    //</editor-fold>
    @Override
    protected JComponent createPanel(WizardController wc, String string, Map map) {
        this.settings = map;
        this.controller = wc;

        if (string.equalsIgnoreCase("load")) {
            // Cannot finish the wizard from this screen.
            controller.setForwardNavigationMode(
                    WizardController.MODE_CAN_CONTINUE);

            // We are on the load page.
            return new LoadPage();
        } else if (string.equalsIgnoreCase("broker")) {
            // Cannot finish the wizard from this screen.
            controller.setForwardNavigationMode(
                    WizardController.MODE_CAN_CONTINUE);

            // We are on the broker page.
            return new BrokerPage();
        } else if (string.equalsIgnoreCase("stops")) {
            // Cannot finish from this page.
            controller.setForwardNavigationMode(
                    WizardController.MODE_CAN_CONTINUE);

            // We are on the first stop page, which is the first of two pages
            //+ for adding stops in the wizard.
            return new StopsPage();
        } else if (string.equals("summary")) {
            // Can finish from this page.
            controller.setForwardNavigationMode(
                    WizardController.MODE_CAN_FINISH);
            
            // We are on the summary page, which is the final page of the wizard,
            //+ there is nowhere else to go from here.
            return new SummaryPage(map);
        } else {
            // An unknown page ID has been discovered. Typo?
            throw new Error("Unknown ID " + string + "\nPlease check the "
                    + "spelling\nand try again.");
        }
    }

    @Override
    protected Object finish(Map settings) {
        // Once the user clicks finish, we need to create the load record in the
        //+ loads table, as well as the stop record(s) in the stops table.
        MessageBox.showInfo("User completed wizard steps.", "Wizard Complete");

        // We are not returning anything from this method, so just return a null
        //+ object, as everything that needed to be done was done here.
        System.out.println(settings.get("brokerList"));
        return null;
    }

    @Override
    public boolean cancel(Map settings) {
        // In this method, we need to verify that the user truly wants to cancel
        //+ the wizard. In order to do this, we need to determine if any data
        //+ has been stored in the settings map. If not, just let the cancel
        //+ take place. However, if data has been entered into the map, then we
        //+ want to query the user as to whether or not they truly wish to 
        //+ cancel booking the load.

        boolean result = true;

        return result;
    }

}
