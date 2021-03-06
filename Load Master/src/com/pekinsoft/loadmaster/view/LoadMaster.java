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
 *  Class      :   LoadMaster.java
 *  Author     :   Sean Carrick
 *  Created    :   Sep 13, 2020 @ 3:49:17 PM
 *  Modified   :   Sep 13, 2020
 *  
 *  Purpose:
 *  
 *  Revision History:
 *  
 *  WHEN          BY                  REASON
 *  ------------  ------------------- ------------------------------------------
 *  Sep 13, 2020  Sean Carrick        Initial creation.
 *  Oct 09, 2020  Sean Carrick        Added this header and removed the main() 
 *                                    method from the class.
 *  Oct 10, 2020  Sean Carrick        Modified the width of the Book Load Wizard
 *                                    from 600 to 700 to better fit the Summary
 *                                    Page report. Changed the titlebar text to 
 *                                    read the Project Name from the properties 
 *                                    file.
 * *****************************************************************************
 */
package com.pekinsoft.loadmaster.view;

import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.controller.EntryCtl;
import com.pekinsoft.loadmaster.controller.LoadCtl;
import com.pekinsoft.loadmaster.controller.StopCtl;
import com.pekinsoft.loadmaster.enums.SysExits;
import com.pekinsoft.loadmaster.err.DataStoreException;
import com.pekinsoft.loadmaster.err.InvalidTimeException;
import com.pekinsoft.loadmaster.model.LoadModel;
import com.pekinsoft.loadmaster.model.StopModel;
import com.pekinsoft.loadmaster.utils.MessageBox;
import com.pekinsoft.loadmaster.view.wiz.LoadBookerWizardPanelProvider;
import com.pekinsoft.loadmaster.view.wiz.book.BrokerPage;
import com.pekinsoft.loadmaster.view.wiz.book.LoadPage;
import com.pekinsoft.loadmaster.view.wiz.book.StopsPage;
import com.pekinsoft.loadmaster.view.wiz.book.SummaryPage;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import org.netbeans.api.wizard.WizardDisplayer;
import org.netbeans.spi.wizard.Wizard;

/**
 *
 * @author Sean Carrick
 */
public class LoadMaster extends javax.swing.JFrame {
    public static EntryCtl batch;
    private final LogRecord record = new LogRecord(Level.ALL, 
            "Logging started for com.pekinsoft.loadmaster.view.LoadMaster");
    
    /**
     * Creates new form LoadMaster
     */
    public LoadMaster() {
        record.setSourceClassName(LoadMaster.class.getName());
        record.setSourceMethodName("LoadMaster");
        record.setMessage("Initializing the LoadMaster window...");
        Starter.logger.enter(record);
        initComponents();
        
        setTitle(Starter.props.getProjectName() + " - Current Trip: " 
                + Starter.props.getProperty("load.current", "No Active Load"));        
        
        versionLabel.setText("Version " + Starter.props.getVersion());
        
        fileProgress.setVisible(false);
        
        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource(
                "/com/pekinsoft/loadmaster/res/Northwind16.png"));
        setIconImage(icon);
        
        record.setMessage("LoadMaster window initialization completed.");
        Starter.logger.exit(record, null);
        
        // Set up the load information.
        loadProgress.setMinimum(0);
        loadProgress.setMaximum(Starter.props.getPropertyAsInt("load.stops", "4"));
        loadProgress.setValue(Starter.props.getPropertyAsInt("load.stop", "1"));
        if ( Starter.props.getProperty("load.current", "No Active Load")
                .equalsIgnoreCase("No Active Load") )
            loadProgress.setVisible(false);
        
        if ( Starter.props.getPropertyAsBoolean("acct.batch", "false") ) {
            try {
                batch = new EntryCtl();
            } catch ( DataStoreException ex ) {
                record.setMessage(ex.getMessage());
                record.setThrown(ex);
                Starter.logger.error(record);
            }
        } else {
            batch = null;
        }
    }
    
    public void setWindowTitle(String title) {
        setTitle(title);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lrSplit = new javax.swing.JSplitPane();
        mainDesktop = new javax.swing.JDesktopPane();
        tbSplit = new javax.swing.JSplitPane();
        tasksContainer = new org.jdesktop.swingx.JXTaskPaneContainer();
        systemTasks = new org.jdesktop.swingx.JXTaskPane();
        loadTasks = new org.jdesktop.swingx.JXTaskPane();
        accountingTasks = new org.jdesktop.swingx.JXTaskPane();
        miscTasks = new org.jdesktop.swingx.JXTaskPane();
        overviewPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        overView = new javax.swing.JTextArea();
        mainStatusBar = new org.jdesktop.swingx.JXStatusBar();
        tipsLabel = new javax.swing.JLabel();
        loadProgress = new javax.swing.JProgressBar();
        versionLabel = new javax.swing.JLabel();
        fileProgress = new javax.swing.JProgressBar();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Load Master");
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                formComponentResized(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        lrSplit.setDividerLocation(250);

        javax.swing.GroupLayout mainDesktopLayout = new javax.swing.GroupLayout(mainDesktop);
        mainDesktop.setLayout(mainDesktopLayout);
        mainDesktopLayout.setHorizontalGroup(
            mainDesktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1160, Short.MAX_VALUE)
        );
        mainDesktopLayout.setVerticalGroup(
            mainDesktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 607, Short.MAX_VALUE)
        );

        lrSplit.setRightComponent(mainDesktop);

        tbSplit.setDividerLocation(525);
        tbSplit.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        org.jdesktop.swingx.VerticalLayout verticalLayout2 = new org.jdesktop.swingx.VerticalLayout();
        verticalLayout2.setGap(14);
        tasksContainer.setLayout(verticalLayout2);

        systemTasks.setTitle("Load Master System");
        createSystemTasks();
        systemTasks.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                systemTasksComponentResized(evt);
            }
        });
        tasksContainer.add(systemTasks);

        loadTasks.setTitle("Loads");
        createLoadTasks();
        tasksContainer.add(loadTasks);

        accountingTasks.setTitle("Accounting");
        createAccountingTasks();
        tasksContainer.add(accountingTasks);

        miscTasks.setTitle("Miscellaneous");
        createMiscTasks();
        tasksContainer.add(miscTasks);

        tbSplit.setTopComponent(tasksContainer);

        overView.setColumns(20);
        overView.setRows(5);
        jScrollPane1.setViewportView(overView);

        javax.swing.GroupLayout overviewPanelLayout = new javax.swing.GroupLayout(overviewPanel);
        overviewPanel.setLayout(overviewPanelLayout);
        overviewPanelLayout.setHorizontalGroup(
            overviewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
        );
        overviewPanelLayout.setVerticalGroup(
            overviewPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 76, Short.MAX_VALUE)
        );

        tbSplit.setRightComponent(overviewPanel);

        lrSplit.setLeftComponent(tbSplit);

        tipsLabel.setText("Watch here for helpful tips...");
        tipsLabel.setMaximumSize(new java.awt.Dimension(1000, 16));
        tipsLabel.setMinimumSize(new java.awt.Dimension(1000, 16));
        tipsLabel.setPreferredSize(new java.awt.Dimension(1000, 16));
        mainStatusBar.add(tipsLabel);

        loadProgress.setToolTipText("Load progress");
        mainStatusBar.add(loadProgress);

        versionLabel.setText("Version 0.0.0");
        mainStatusBar.add(versionLabel);
        mainStatusBar.add(fileProgress);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lrSplit)
            .addComponent(mainStatusBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(lrSplit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainStatusBar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void setTipWidth() {
        int tipWidth = getWidth() - ( loadProgress.getWidth() 
                + fileProgress.getWidth() + versionLabel.getWidth() + 45 );
        tipsLabel.setSize(tipWidth - 30, 16);
        
        Point load = loadProgress.getLocation();
        Point version = versionLabel.getLocation();
        Point file = fileProgress.getLocation();
        
        load.x = tipsLabel.getWidth() + 15;
        version.x = load.x + loadProgress.getWidth() + 10;
        file.x = version.x + versionLabel.getWidth() + 10;
        
        loadProgress.setLocation(load);
        versionLabel.setLocation(version);
        fileProgress.setLocation(file);
    }
    
    private void createLoadTasks() {
        loadTasks.add(new AbstractAction() {
        {
            putValue(Action.NAME, "Book New Load...");
            putValue(Action.SHORT_DESCRIPTION, "Displays the load booking dialog");
            putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass()
                    .getResource("/com/pekinsoft/loadmaster/res/add.png")));
        }

        public void actionPerformed(ActionEvent e) {
            doBookLoad();
        }
        });

        loadTasks.add(new JSeparator());

        loadTasks.add(new AbstractAction() {
        {
            if ( Starter.props.getProperty("load.status", 
                    "arrive").equals("depart") ){
                putValue(Action.NAME, "Depart from Stop...");
                putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass()
                    .getResource("/com/pekinsoft/loadmaster/res/Depart.png")));
            } else {
                putValue(Action.NAME, "Arrive at Stop...");
                putValue(Action.SHORT_DESCRIPTION, "Displays stop arrival dialog");
                putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass()
                        .getResource("/com/pekinsoft/loadmaster/res/Arrive.png")));
            }
        }

        public void actionPerformed(ActionEvent e) {                
            if ( getValue(Action.NAME).toString().equalsIgnoreCase("Arrive at Stop...") ) {
                putValue(Action.NAME, "Depart from Stop...");
                putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass()
                    .getResource("/com/pekinsoft/loadmaster/res/Depart.png")));
                doArrival();
            } else if ( getValue(Action.NAME).toString().equalsIgnoreCase("Depart from Stop...") ) {
                putValue(Action.NAME, "Arrive at Stop...");
                putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass()
                    .getResource("/com/pekinsoft/loadmaster/res/Arrive.png")));
                doShowDeparture();
            }
        }
        });

        loadTasks.add(new JSeparator());

        loadTasks.add(new AbstractAction() {
        {
            putValue(Action.NAME, "Take Cash Advance...");
            putValue(Action.SHORT_DESCRIPTION, "Displays cash advance dialog");
            putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass()
                    .getResource("/com/pekinsoft/loadmaster/res/money3D.png")));
        }

        public void actionPerformed(ActionEvent e) {
//            CashAdvanceDialog dlg = new CashAdvanceDialog(null, false);
//            dlg.pack();
//            dlg.setAlwaysOnTop(true);
//            dlg.setVisible(true);
        }
        });

        loadTasks.add(new JSeparator());

        loadTasks.add(new AbstractAction() {
        {
            putValue(Action.NAME, "View Loads Queue...");
            putValue(Action.SHORT_DESCRIPTION, "Displays Loads Queue dialog");
            putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass()
                    .getResource("/com/pekinsoft/loadmaster/res/freight.png")));
        }

        public void actionPerformed(ActionEvent e) {
            doShowLoadsQueue();
        }
        });

        loadTasks.add(new JSeparator());

        loadTasks.add(new AbstractAction() {
        {
            putValue(Action.NAME, "Close Load");
            putValue(Action.SHORT_DESCRIPTION, "Closes out the current load.");
            putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass()
                    .getResource("/com/pekinsoft/loadmaster/res/mark.png")));
            
        }

        public void actionPerformed(ActionEvent e) {
            if ( loadProgress.getValue() == loadProgress.getMaximum() ) {
                // Close out the load. Remember, if the company has their own
                //+ operating authority, we need to generate an invoice for the
                //+ load we are closing.
                doCloseLoad();
            } else 
                MessageBox.showWarning("Load is not yet complete.\n\nPlease "
                        + "complete all stops before closing the load.", 
                        "Load Incomplete");
        }
        });
    }
    
    private void createSystemTasks() {
        systemTasks.add(new AbstractAction() {
        {
            putValue(Action.NAME, "Load Master Settings...");
            putValue(Action.SHORT_DESCRIPTION, "Displays the settings dialog.");
            putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass()
                    .getResource("/com/pekinsoft/loadmaster/res/configuration.png")));
        }

        public void actionPerformed(ActionEvent e) {
            doShowSettings();
        }
        });

        systemTasks.add(new AbstractAction() {
        {
            putValue(Action.NAME, "About Load Master...");
            putValue(Action.SHORT_DESCRIPTION, "Displays the about dialog.");
            putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass()
                    .getResource("/com/pekinsoft/loadmaster/res/Northwind16.png")));
        }

        public void actionPerformed(ActionEvent e) {
            doShowAbout();
        }
        });

        systemTasks.add(new JSeparator());

        systemTasks.add(new AbstractAction() {
        {
            putValue(Action.NAME, "Add Customer...");
            putValue(Action.SHORT_DESCRIPTION, "Displays the New Customer dialog.");
            putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass()
                    .getResource("/com/pekinsoft/loadmaster/res/people.png")));
        }

        public void actionPerformed(ActionEvent e) {
            Customers dlg = new Customers();
            mainDesktop.add(dlg);
            dlg.pack();
            dlg.setVisible(true);
        }
        });


        systemTasks.add(new AbstractAction() {
        {
            putValue(Action.NAME, "Add Broker/Agent...");
            putValue(Action.SHORT_DESCRIPTION, "Displays the New Broker/Agent dialog.");
            putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass()
                    .getResource("/com/pekinsoft/loadmaster/res/users.png")));
        }

        public void actionPerformed(ActionEvent e) {
            Brokers dlg = new Brokers();
            mainDesktop.add(dlg);
            dlg.pack();
            dlg.setVisible(true);
        }
        });

        systemTasks.add(new JSeparator());

        systemTasks.add(new AbstractAction() {
        {
            putValue(Action.NAME, "Exit Load Master...");
            putValue(Action.SHORT_DESCRIPTION, "Exits Load Master cleanly.");
            putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass()
                    .getResource("/com/pekinsoft/loadmaster/res/Turnoff.png")));
        }

        public void actionPerformed(ActionEvent e) {
            doClose();
        }
        });
    }
    
    private void createAccountingTasks() {
        accountingTasks.add(new AbstractAction() {
        {
            putValue(Action.NAME, "Chart of Accounts...");
            putValue(Action.SHORT_DESCRIPTION, "Displays the Chart of Accounts "
                    + "Viewer.");
            putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass()
                    .getResource("/com/pekinsoft/loadmaster/res/open-list.png")));
        }

        public void actionPerformed(ActionEvent e) {
            doShowChartOfAccounts();
        }
        });
    }
    
    private void createMiscTasks() {
        miscTasks.add(new AbstractAction() {
        {
            putValue(Action.NAME, "Fuel Purchase Entry...");
            putValue(Action.SHORT_DESCRIPTION, "Displays the Fuel Purchase Entry"
                    + " dialog.");
            putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass()
                    .getResource("/com/pekinsoft/loadmaster/res/GasPump.png")));
        }

        public void actionPerformed(ActionEvent e) {
            doAddFuelPurchase();
        }
        });
    }
    
    private void doAddFuelPurchase() {
        FuelPurchaseDialog dlg = new FuelPurchaseDialog(this, true);
        dlg.pack();
        dlg.setVisible(true);
    }
    
    private void doClose() {Starter.props.setPropertyAsInt("windows.main.tb", 
                tbSplit.getDividerLocation());
        Starter.props.setPropertyAsInt("windows.main.lr", 
                lrSplit.getDividerLocation());

        Starter.props.setPropertyAsInt("windows.main.state", getExtendedState());
        
        if ( getExtendedState() != JFrame.MAXIMIZED_BOTH ) {
            Starter.props.setPropertyAsInt("windows.main.top", getLocation().y);
            Starter.props.setPropertyAsInt("windows.main.left", getLocation().x);
            Starter.props.setPropertyAsInt("windows.main.height", getSize().height);
            Starter.props.setPropertyAsInt("windows.main.width", getSize().width);
        }
                
        if ( Boolean.parseBoolean(Starter.props.getProperty(
                "acct.batch", "false")) & Starter.batch != null) {
            int unprocessed = Starter.batch.getRecordCount();
            if ( unprocessed > 0 ) {
                confirmExit(unprocessed);
            } else {
                exit(SysExits.EX_OK);
            }
        
        } else {
            exit(SysExits.EX_OK);
        }
    }
    
    private void confirmExit(int count) {
        int response = MessageBox.askQuestion("There are " + count + 
                " unprocessed transactions.\n\nWould you like to process these "
                        + "before exiting", "Unposted Transactions", true);
        
        if ( response == MessageBox.YES_OPTION ) {
            // Process batch to General Ledger.
        } else if ( response == MessageBox.NO_OPTION ) {
            exit(SysExits.EX_OK);
        } // if the response is MessageBox.CANCEL_OPTION, we do nothing to let
          //+ the application continue. Maybe the user is going to process the
          //+ batch manually.
    }
    
    private void exit(SysExits status) {
        Starter.exit(status);
    }
    
    private void doCloseLoad() {
        
        
        if ( Starter.props.getPropertyAsBoolean("authority", "false") ) {
            doGenerateInvoice("");
        }
    }
    
    private void doBookLoad() {
        Class[] pages = new Class[] {
            LoadPage.class,
            BrokerPage.class,
            StopsPage.class,
            SummaryPage.class
        };
        
        // We will want to change the WizardResultProducer.NO_OP to a different
        //+ object once we make sure that the wizard is going as expected.
        Wizard wiz = new LoadBookerWizardPanelProvider().createWizard();
        
        int top = (Toolkit.getDefaultToolkit().getScreenSize().height - 400) / 2;
        int left = (Toolkit.getDefaultToolkit().getScreenSize().width - 700) / 2;
        WizardDisplayer.showWizard(wiz, new Rectangle(left, top, 700, 400));
        
        setTitle(Starter.props.getProjectName() + " - Current Trip: " 
                + Starter.props.getProperty("load.current", "No Active Load"));
    }
    
    private void doShowAbout() {
        About dlg = new About(this, true);
        dlg.pack();
        dlg.setVisible(true);
    }
    
    private void doShowSettings() {
        SettingsDialog dlg = new SettingsDialog(this, true);
        dlg.pack();
        dlg.setVisible(true);
    }
    
    private void doArrival() {
        // Update the stop number in the settings file.
        Starter.props.setPropertyAsInt("load.stop", 
                Starter.props.getPropertyAsInt("load.stop", "0") + 1);
        
//        // Update the progress bar for the load.
//        loadProgress.setValue(Starter.props.getPropertyAsInt("load.stop", "0"));
        
        
        // Retrieve the current stop from the table.
        StopModel current = null;
        StopCtl stops = null;
        
        try {
            stops = new StopCtl();
        } catch ( DataStoreException ex ) {
            record.setSourceMethodName("doArrival");
            record.setMessage("An error occurred while creating a StopCtl "
                    + "object.");
            record.setThrown(ex);
            Starter.logger.error(record);
            
            MessageBox.showError(ex, "Data Access Error");
        } finally {
            if ( stops != null ) {
                if ( stops.getRecordCount() > 0 ) {
                    current = stops.get();
                    
                    if ( !current.getTripNumber().equalsIgnoreCase(
                            Starter.props.getProperty("load.current"))) {
                        while ( stops.hasNext() ) {
                            try {
                                current = stops.next();
                            } catch ( DataStoreException ex ) {
                                record.setMessage("An error occurred while "
                                        + "trying to move to the next stop.");
                                Starter.logger.error(record);
                                
                                MessageBox.showError(ex, "Record Navigation "
                                        + "Error");
                            }
                            
                            if ( current.getTripNumber().equalsIgnoreCase(
                                    Starter.props.getProperty("load.current")))
                                break;
                        }
                    }
                    
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    current.setArrDate(new Date());
                    try {
                        current.setArrTime(sdf.format(new Date()));
                    } catch ( InvalidTimeException | ParseException ex ) {
                        record.setMessage("Time not properly parsed or invalid.");
                        record.setThrown(ex);
                        Starter.logger.error(record);
                        
                        MessageBox.showError(ex, "Parsing Error");
                    }
                    
                    stops.update(current);
                    
                    try {
                        stops.close();
                    } catch ( DataStoreException ex ) {
                        record.setMessage("An error occurred while attempting "
                                + "to access the stops table file.");
                        record.setThrown(ex);
                        Starter.logger.error(record);
                        
                        MessageBox.showError(ex, "Data Access Error");
                    }
                }
            }
        }
        
        updateLoadProgress();
    }
    
    private void doShowDeparture() {
        // Update the stop number in the settings file.
        Starter.props.setPropertyAsInt("load.stop", 
                Starter.props.getPropertyAsInt("load.stop", "0") + 1);
        
        // Update the progress bar for the load.
        loadProgress.setValue(Starter.props.getPropertyAsInt("load.stop", "0"));
        
        // Declare a JDialog object to use for our departure dialogs.
        JDialog dlg = null;
        
        int result = JOptionPane.showConfirmDialog(this, "Is this a delivery?");
        boolean pickingUp = true;
        
        if ( result == JOptionPane.NO_OPTION ) {
            pickingUp = false;
            dlg = new DepartPickupDialog(this, true);
            dlg.pack();
            dlg.setVisible(true);
        } else {
            dlg = new DepartPickupDialog(this, true);
            dlg.pack();
            dlg.setVisible(true);
        }        
        
        // Retrieve the current stop from the table.
        StopModel current = null;
        StopCtl stops = null;
        
        try {
            stops = new StopCtl();
        } catch ( DataStoreException ex ) {
            record.setSourceMethodName("doDeparture");
            record.setMessage("An error occurred while creating a StopCtl "
                    + "object.");
            record.setThrown(ex);
            Starter.logger.error(record);
            
            MessageBox.showError(ex, "Data Access Error");
        } finally {
            if ( stops != null ) {
                if ( stops.getRecordCount() > 0 ) {
                    current = stops.get();
                    
                    if ( !current.getTripNumber().equalsIgnoreCase(
                            Starter.props.getProperty("load.current"))) {
                        while ( stops.hasNext() ) {
                            try {
                                current = stops.next();
                            } catch ( DataStoreException ex ) {
                                record.setMessage("An error occurred while "
                                        + "trying to move to the next stop.");
                                Starter.logger.error(record);
                                
                                MessageBox.showError(ex, "Record Navigation "
                                        + "Error");
                            }
                            
                            if ( current.getTripNumber().equalsIgnoreCase(
                                    Starter.props.getProperty("load.current")))
                                break;
                        }
                    }
                    
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                    Date dep = new Date();
                    current.setDepDate(dep);
                    try {
                        current.setDepTime(sdf.format(dep));
                    } catch ( InvalidTimeException | ParseException ex ) {
                        record.setMessage("Time not properly parsed or invalid.");
                        record.setThrown(ex);
                        Starter.logger.error(record);
                        
                        MessageBox.showError(ex, "Parsing Error");
                    }
                    
                    if ( !pickingUp ) {
                        current.setSignedBy(
                                ((DepartDeliveryDialog)dlg).getSignedBy());
                    }
                    
                    stops.update(current);
                    
                    try {
                        stops.close();
                    } catch ( DataStoreException ex ) {
                        record.setMessage("An error occurred while attempting "
                                + "to access the stops table file.");
                        record.setThrown(ex);
                        Starter.logger.error(record);
                        
                        MessageBox.showError(ex, "Data Access Error");
                    }
                }
            }
        }
        
        // Now, we need to update the load with the information provided if this
        //+ stop is a pickup.
        if ( pickingUp ) {
            // To store the pickup information, we need to create and use two (2)
            //+ objects: LoadModel and LoadCtl. First, we will create a LoadModel.
            LoadModel load = new LoadModel();
            String currentLoad = Starter.props.getProperty("load.current", 
                    "No Active Load");
            
            // Now, we need to create our LoadCtl object.
            try {
                LoadCtl loads = new LoadCtl();
                
                // Next, check to be sure there are records available.
                if ( loads.getRecordCount() > 0 ) {
                    // We need to get the first record into our local load object.
                    load = loads.get();
                    
                    if ( !currentLoad.equals("No Active Load") ) {
                        if ( !load.getOrder().equalsIgnoreCase(currentLoad) ) {
                            // We need to loop through all of the loads until we
                            //+ find the record for the active load.
                            while ( loads.hasNext() ) {
                                load = loads.next();
                                
                                if ( load.getOrder().equalsIgnoreCase(
                                        currentLoad) ) {
                                    // Now, since we found our current load, we
                                    //+ can update the information in the load...
                                    load.setBol(((DepartPickupDialog)dlg)
                                            .getBillOfLadingNumber());
                                    load.setPieces(((DepartPickupDialog)dlg)
                                            .getPieceCount());
                                    load.setWeight(((DepartPickupDialog)dlg)
                                            .getWeight());
                                    
                                    //+ Then, break out of the loop.
                                    break;
                                } // end if in the while
                            } // end while loop
                        } else {
                            // We got lucky and our load was the first load in
                            //+ the table, so store the pickup information.
                            load.setBol(((DepartPickupDialog)dlg)
                                    .getBillOfLadingNumber());
                            load.setPieces(((DepartPickupDialog)dlg)
                                    .getPieceCount());
                            load.setWeight(((DepartPickupDialog)dlg)
                                    .getWeight());
                        } // end check of first load record
                    } // end no active load check
                } // end records exist check
                
                // Update the record to include the new data we just received.
                loads.update(load);
                
                // Close the loads table.
                loads.close();
            } catch ( DataStoreException ex ) {
                record.setMessage("An error occurred trying to access the loads"
                        + " table.");
                record.setSourceMethodName("doShowDeparture");
                record.setSourceClassName(getClass().getName());
                record.setThrown(ex);
                Starter.logger.error(record);
                
                MessageBox.showError(ex, "Database Access Error");
            }
        }
        
        updateLoadProgress();
    }
    
    private void doShowChartOfAccounts() {
        ChartOfAccountsViewer dlg = new ChartOfAccountsViewer(this, true);
        dlg.pack();
        dlg.setVisible(true);
    }
    
    private void updateLoadProgress() {
        if ( loadProgress.getValue() == loadProgress.getMaximum() ) {
            String msg = "Trip " + Starter.props.getProperty("load.current", 
                    "No Load") + " complete!";
            MessageBox.showInfo(msg, "Load Complete");

            Starter.props.setPropertyAsInt("load.stops", 0);
            Starter.props.setPropertyAsInt("load.stop", 0);
            Starter.props.setProperty("load.current", "No Active Load");

            loadProgress.setMaximum(Starter.props.getPropertyAsInt("load.stops", 
                    "0"));
            loadProgress.setValue(Starter.props.getPropertyAsInt("load.stop", 
                    "0"));
            
            setTitle("Load Master - Current Trip: " 
                    + Starter.props.getProperty("load.current", "No Active Load"));
        } else {
            loadProgress.setValue(loadProgress.getValue() + 1);
        }
    }
    
    private void doShowLoadsQueue() {
        
    }
    
    private void doGenerateInvoice(String orderNumber) {
        
    }
    
    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // Make sure that the user truly wants to exit the application.
        doClose();
    }//GEN-LAST:event_formWindowClosing

    private void formComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentResized
        // set the width of the tips label.
        setTipWidth();
    }//GEN-LAST:event_formComponentResized

    private void systemTasksComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_systemTasksComponentResized
        systemTasks.setCollapsed(false);
    }//GEN-LAST:event_systemTasksComponentResized

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        tbSplit.setDividerLocation(Starter.props.getPropertyAsInt(
                "windows.main.tb", "525"));
        lrSplit.setDividerLocation(Starter.props.getPropertyAsInt(
                "windows.main.lr", "250"));
    }//GEN-LAST:event_formWindowOpened

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.JXTaskPane accountingTasks;
    public static javax.swing.JProgressBar fileProgress;
    private javax.swing.JScrollPane jScrollPane1;
    public static javax.swing.JProgressBar loadProgress;
    private org.jdesktop.swingx.JXTaskPane loadTasks;
    private javax.swing.JSplitPane lrSplit;
    private javax.swing.JDesktopPane mainDesktop;
    private org.jdesktop.swingx.JXStatusBar mainStatusBar;
    private org.jdesktop.swingx.JXTaskPane miscTasks;
    private javax.swing.JTextArea overView;
    private javax.swing.JPanel overviewPanel;
    private org.jdesktop.swingx.JXTaskPane systemTasks;
    private org.jdesktop.swingx.JXTaskPaneContainer tasksContainer;
    private javax.swing.JSplitPane tbSplit;
    public static javax.swing.JLabel tipsLabel;
    private javax.swing.JLabel versionLabel;
    // End of variables declaration//GEN-END:variables
}
