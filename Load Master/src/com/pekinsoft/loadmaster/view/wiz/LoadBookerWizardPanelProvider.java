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
 *  Sep 07, 2020  Sean Carrick        Initial creation.
 *  Oct 10, 2020  Sean Carrick        Modified constructors to call each 
 *                                    successive constructor until such time as
 *                                    the super() needed to be called to prevent
 *                                    repetition of the same code for 
 *                                    initialization.
 *  Oct 12, 2020  Jiří Kovalský       Improved parsing of the truck.pay value
 *                                    which will work in all countries not only
 *                                    in USA, if Load Master plans to target
 *                                    markets around the world.
 * *****************************************************************************
 */
package com.pekinsoft.loadmaster.view.wiz;

import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.controller.LoadCtl;
import com.pekinsoft.loadmaster.controller.ReceivablesCtl;
import com.pekinsoft.loadmaster.controller.StopCtl;
import com.pekinsoft.loadmaster.err.DataStoreException;
import com.pekinsoft.loadmaster.model.LoadModel;
import com.pekinsoft.loadmaster.model.ReceivablesModel;
import com.pekinsoft.loadmaster.model.StopModel;
import com.pekinsoft.loadmaster.utils.MessageBox;
import com.pekinsoft.loadmaster.view.StartTripDialog;
import com.pekinsoft.loadmaster.view.wiz.book.BrokerPage;
import com.pekinsoft.loadmaster.view.wiz.book.LoadPage;
import com.pekinsoft.loadmaster.view.wiz.book.StopsPage;
import com.pekinsoft.loadmaster.view.wiz.book.SummaryPage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogRecord;
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
        this(new String[]{"load", "broker", "stops", "summary"},
                new String[]{"Load Information", "Broker Information",
                    "StopsPage", "Summary"});
    }

    public LoadBookerWizardPanelProvider(String[] steps, String[] descriptions) {
        this("Book New Load Wizard", steps, descriptions);
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
        load = new LoadModel();
        load.setMiles(Integer.valueOf(settings.get("miles").toString()));
        load.setCbd(Boolean.parseBoolean(settings.get("cbd").toString()));
        load.setCommodity(settings.get("commodity").toString());
        load.setHazMat(Boolean.parseBoolean(settings.get("hazmat").toString()));
        load.setLtl(Boolean.parseBoolean(settings.get("ltl").toString()));
        load.setOrder(settings.get("order").toString());
        load.setRamps(Boolean.parseBoolean(settings.get("ramps").toString()));
        try {
            NumberFormat nf = new java.text.DecimalFormat("#,##0.00");
            Number pay = nf.parse(settings.get("truck.pay").toString());
            load.setRate(pay.doubleValue());
        } catch (ParseException ex) {
            entry.setMessage("Couldn't parse truck.pay value.");
            entry.setThrown(ex);
            Starter.logger.error(entry);
        }
        load.setTarped(Boolean.parseBoolean(settings.get("tarped").toString()));
        load.setTeam(Boolean.parseBoolean(settings.get("team").toString()));
        load.setTrip(settings.get("trip").toString());
        load.setTwic(Boolean.parseBoolean(settings.get("twic").toString()));
        load.setTopCust(Boolean.parseBoolean(settings.get(
                "top.customer").toString()));

        load.setDispatch(new Date());

        String broker = settings.get("brokerList").toString();
        broker = broker.substring(broker.indexOf("(") + 1);
        broker = broker.substring(0, broker.length() - 1);
        load.setBroker(Long.valueOf(broker));

        int stopCount = Starter.props.getPropertyAsInt("stop.count", "0");
        int x = 0;
        do {
            stop = new StopModel();
            stop = (StopModel) settings.get("stop" + ++x);
            stops.addNew(stop);
            load.addStop(stop);
        } while (x < stopCount);

        try {
            stops.close();
        } catch (DataStoreException ex) {
            entry.setMessage("An error occurred accessing the stops database.");
            entry.setThrown(ex);
            Starter.logger.error(entry);

            MessageBox.showError(ex, "Stops Database Error");
        }

        // The last thing to do is to check to see if the driver is on an active
        //+ trip.
        if (Starter.props.getProperty("load.current", "No Active Load")
                .equalsIgnoreCase("No Active Load")) {
            // If not, see if the driver wants to start this trip.
            int response = MessageBox.askQuestion("Would you like to start this "
                    + "trip?", "Start Trip", false);

            if (response == MessageBox.YES_OPTION) {
                StartTripDialog dlg = new StartTripDialog(null, true);
                dlg.pack();
                dlg.setOrderNumber(load.getOrder());
                dlg.setTripNumber(load.getTrip());
                dlg.setVisible(true);

                if (!dlg.isCancelled()) {
                    load.setStartOdo(dlg.getOdometer());

                    Starter.props.setProperty("load.current", load.getTrip());
                    Starter.props.setPropertyAsInt("stop.count",
                            load.getStopCount() * 2);
                    Starter.props.setPropertyAsInt("load.stop", 0);
                    Starter.props.flush();

                    ReceivablesCtl ctl;

                    try {
                        ctl = new ReceivablesCtl();
//                        ReceivablesModel ar = ;
                        ctl.addNew(new ReceivablesModel(
                                load.getDispatch(),
                                load.getTrip(),
                                load.getOrder(),
                                load.getRate()));
                        ctl.close();
                    } catch (DataStoreException ex) {
                        entry.setMessage("An error occurred accessing the stops "
                                + "database.");
                        entry.setThrown(ex);
                        Starter.logger.error(entry);

                        MessageBox.showError(ex, "Receivables Journal Error");
                    }
                }
            }
        }

        loads.addNew(load);

        try {
            loads.close();
        } catch (DataStoreException ex) {
            entry.setMessage("An error occurred accessing the loads database.");
            entry.setThrown(ex);
            Starter.logger.error(entry);

            MessageBox.showError(ex, "Stops Database Error");
        }

        // Now, we need to see if the user is currently on
        // We are not returning anything from this method, so just return a null
        //+ object, as everything that needed to be done was done here.
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

        if (MessageBox.askQuestion(
                "Are you sure you want to cancel the Wizard?",
                "Confirm Cancellation", false) == MessageBox.YES_OPTION) {
            return true;
        } else {
            return false;
        }
    }

}
