/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pekinsoft.loadmaster.view;

import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.enums.SysExits;
import com.pekinsoft.loadmaster.utils.MessageBox;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JSeparator;

/**
 *
 * @author Sean Carrick
 */
public class LoadMaster extends javax.swing.JFrame {
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
        
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        
        versionLabel.setText("Version " + Starter.props.getVersion());
        
        fileProgress.setVisible(false);
        
        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource(
                "/com/pekinsoft/loadmaster/res/Northwind16.png"));
        setIconImage(icon);
        
        record.setMessage("LoadMaster window initialization completed.");
        Starter.logger.exit(record, null);
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
        jXTaskPane1 = new org.jdesktop.swingx.JXTaskPane();
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
        });

        lrSplit.setDividerLocation(250);
        lrSplit.setDividerSize(5);

        javax.swing.GroupLayout mainDesktopLayout = new javax.swing.GroupLayout(mainDesktop);
        mainDesktop.setLayout(mainDesktopLayout);
        mainDesktopLayout.setHorizontalGroup(
            mainDesktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1143, Short.MAX_VALUE)
        );
        mainDesktopLayout.setVerticalGroup(
            mainDesktopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 566, Short.MAX_VALUE)
        );

        lrSplit.setRightComponent(mainDesktop);

        tbSplit.setDividerLocation(525);
        tbSplit.setDividerSize(5);
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
        tasksContainer.add(accountingTasks);

        jXTaskPane1.setTitle("Miscellaneous");
        tasksContainer.add(jXTaskPane1);

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
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 35, Short.MAX_VALUE)
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
            doNewLoad();
        }
        });

        loadTasks.add(new JSeparator());

        loadTasks.add(new AbstractAction() {
        {
            putValue(Action.NAME, "Arrive at Stop...");
            putValue(Action.SHORT_DESCRIPTION, "Displays stop arrival dialog");
            putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass()
                    .getResource("/com/pekinsoft/loadmaster/res/Arrive.png")));
        }

        public void actionPerformed(ActionEvent e) {
            if ( getValue(Action.NAME).toString().equalsIgnoreCase("Arrive at Stop...") ) {
                putValue(Action.NAME, "Depart from Stop...");
                putValue(Action.SMALL_ICON, new javax.swing.ImageIcon(getClass()
                    .getResource("/com/pekinsoft/loadmaster/res/Depart.png")));
                doShowArrival();
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

        systemTasks.add(new JSeparator());

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
        
    }
    
    private void createMiscTasks() {
        
    }
    
    private void doClose() {
        String msg = "Exit Load Master?";
        int response = MessageBox.askQuestion(msg, "Confirm Close", false);
        
        if ( response == MessageBox.YES_OPTION ) {
            Starter.exit(SysExits.EX_OK);
        }
    }
    
    private void doCloseLoad() {
        
        
        if ( Starter.props.getPropertyAsBoolean("authority", "false") ) {
            doGenerateInvoice("");
        }
    }
    
    private void doNewLoad() {
        Booker dlg = new Booker();
        
        dlg.pack();
        mainDesktop.add(dlg);
        dlg.setVisible(true);
    }
    
    private void doShowSettings() {
        SettingsDialog dlg = new SettingsDialog(this, true);
        dlg.pack();
        dlg.setVisible(true);
    }
    
    private void doShowArrival() {
        
    }
    
    private void doShowDeparture() {
        
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

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LoadMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoadMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoadMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoadMaster.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoadMaster().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private org.jdesktop.swingx.JXTaskPane accountingTasks;
    public static javax.swing.JProgressBar fileProgress;
    private javax.swing.JScrollPane jScrollPane1;
    private org.jdesktop.swingx.JXTaskPane jXTaskPane1;
    public static javax.swing.JProgressBar loadProgress;
    private org.jdesktop.swingx.JXTaskPane loadTasks;
    private javax.swing.JSplitPane lrSplit;
    private javax.swing.JDesktopPane mainDesktop;
    private org.jdesktop.swingx.JXStatusBar mainStatusBar;
    private javax.swing.JTextArea overView;
    private javax.swing.JPanel overviewPanel;
    private org.jdesktop.swingx.JXTaskPane systemTasks;
    private org.jdesktop.swingx.JXTaskPaneContainer tasksContainer;
    private javax.swing.JSplitPane tbSplit;
    public static javax.swing.JLabel tipsLabel;
    private javax.swing.JLabel versionLabel;
    // End of variables declaration//GEN-END:variables
}
