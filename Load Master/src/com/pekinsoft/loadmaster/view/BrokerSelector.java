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
 *  Class      :   BrokerSelector.java
 *  Author     :   Sean Carrick
 *  Created    :   Aug 30, 2020 @ 9:00:42 AM
 *  Modified   :   Aug 30, 2020
 *  
 *  Purpose:
 *      Provides a method of visually selecting the broker/agent who booked the
 *      load, so that their contact information will be available on the load
 *      window and from the other load task windows.
 *  
 *  Revision History:
 *  
 *  WHEN          BY                  REASON
 *  ------------  ------------------- ------------------------------------------
 *  Aug 30, 2020  Sean Carrick        Initial creation.
 *  Sep 01, 2020  Sean Carrick        Added data validation to the dialog to 
 *                                    ensure that required fields are completed
 *                                    and that the date entered is valid.
 *  Oct 09, 2020  Sean Carrick        Removed the main() method from the class.
 * *****************************************************************************
 */
package com.pekinsoft.loadmaster.view;

import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.controller.BrokerCtl;
import com.pekinsoft.loadmaster.err.DataStoreException;
import com.pekinsoft.loadmaster.model.BrokerModel;
import com.pekinsoft.loadmaster.utils.MessageBox;
import com.pekinsoft.loadmaster.utils.ScreenUtils;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 *
 * @author Sean Carrick
 */
public class BrokerSelector extends javax.swing.JDialog {
    
    private BrokerModel broker;
    private BrokerCtl records;
    private LogRecord lr = new LogRecord(Level.ALL, "Logging initialized for "
            + "BrokerSelector.");
    
    private boolean filtering;
    
    /**
     * Creates new form BrokerSelector
     */
    public BrokerSelector(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        
        Starter.logger.config(lr);
        lr = new LogRecord(Level.ALL, "Logging initialized for BrokerSelector.");
        lr.setSourceClassName(Brokers.class.getName());
        lr.setSourceMethodName("BrokerSelector");
        Starter.logger.enter(lr);
        
        filtering = false;
        
        lr.setMessage("Attempting to access the brokers database...");
        Starter.logger.debug(lr);
        try {
            records = new BrokerCtl();
            lr.setMessage("Brokers database accessed successfully!");
            Starter.logger.info(lr);
        } catch ( DataStoreException ex ) {
            lr.setMessage("Something went wrong accessing the brokers database.");
            lr.setThrown(ex);
            Starter.logger.error(lr);
            
            MessageBox.showError(ex, "Database Access");
            
            records = null;
        }
        
        initComponents();
        
        loadBrokerList();
        
        setLocation(ScreenUtils.centerDialog(this));
    }
    
    private void loadBrokerList() {
        brokerList.removeAllItems();
        brokerList.addItem("Select Broker/Agent...");
        
        ArrayList<BrokerModel> filtered = new ArrayList<>();
        
        if ( allFilterOption.isSelected() ) {
            try {
                records.first();
            } catch ( DataStoreException ex ) {
                lr.setMessage("Something went wrong moving to the next record.");
                lr.setThrown(ex);
                Starter.logger.error(lr);

                MessageBox.showError(ex, "Database Access");
            }

            for ( int x = 0; x < records.getRecordCount(); x++ ) {
                BrokerModel b = records.get();

                brokerList.addItem(b.getContact() + " (" + b.getId() + ")");

                try {
                    if (records.hasNext() ) 
                        records.next();
                } catch ( DataStoreException ex ) {
                    lr.setMessage("Something went wrong moving to the next record.");
                    lr.setThrown(ex);
                    Starter.logger.error(lr);

    //                MessageBox.showError(ex, "Database Access");
                }
            }
        } else if ( cityFilterOption.isSelected() ) {
            try {
                filtered = records.getCompaniesByCity(criteriaField.getText(), 
                        LoadMaster.fileProgress);
            } catch ( DataStoreException ex ) {
                lr.setMessage("Something went wrong moving to the next record.");
                lr.setThrown(ex);
                Starter.logger.error(lr);
            }
            
            if ( filtered != null && filtered.size() > 0 ) {
                for ( int x = 0; x < filtered.size(); x++ ) {
                    brokerList.addItem(filtered.get(x).getContact() + " (" 
                            + filtered.get(x).getId() + ")");
                }
            } else
                MessageBox.showInfo("No matching records found!", "No Records");
        } else if ( stateFilterOption.isSelected() ) {
            try {
                filtered = records.getCompaniesByState(criteriaField.getText(), 
                        LoadMaster.fileProgress);
            } catch ( DataStoreException ex ) {
                lr.setMessage("Something went wrong moving to the next record.");
                lr.setThrown(ex);
                Starter.logger.error(lr);
            }
            
            if ( filtered != null && filtered.size() > 0 ) {
                for ( int x = 0; x < filtered.size(); x++ ) {
                    brokerList.addItem(filtered.get(x).getContact() + " (" 
                            + filtered.get(x).getId() + ")");
                }
            } else
                MessageBox.showInfo("No matching records found!", "No Records");
        }
        
        filtering = false;
    }
    
    public BrokerModel getSelectedBroker() {
        return broker;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        filterButtonGroup = new javax.swing.ButtonGroup();
        brokerLabel = new javax.swing.JLabel();
        brokerList = new javax.swing.JComboBox<>();
        selectBroker = new javax.swing.JButton();
        filterPanel = new javax.swing.JPanel();
        allFilterOption = new javax.swing.JRadioButton();
        cityFilterOption = new javax.swing.JRadioButton();
        stateFilterOption = new javax.swing.JRadioButton();
        criteriaLabel = new javax.swing.JLabel();
        criteriaField = new javax.swing.JTextField();
        filterButton = new javax.swing.JButton();

        setTitle("Select Load Broker/Agent");
        setAlwaysOnTop(true);
        setModal(true);
        setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
        setResizable(false);

        brokerLabel.setText("Broker/Agent:");

        brokerList.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                brokerListItemStateChanged(evt);
            }
        });

        selectBroker.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pekinsoft/loadmaster/res/ok.png"))); // NOI18N
        selectBroker.setMnemonic('S');
        selectBroker.setText("Select Broker/Agent");
        selectBroker.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectBrokerActionPerformed(evt);
            }
        });

        filterPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Filter List"));

        filterButtonGroup.add(allFilterOption);
        allFilterOption.setMnemonic('A');
        allFilterOption.setSelected(true);
        allFilterOption.setText("All");

        filterButtonGroup.add(cityFilterOption);
        cityFilterOption.setMnemonic('C');
        cityFilterOption.setText("By City");

        filterButtonGroup.add(stateFilterOption);
        stateFilterOption.setMnemonic('S');
        stateFilterOption.setText("By State");

        criteriaLabel.setDisplayedMnemonic('V');
        criteriaLabel.setText("Value to Search:");

        criteriaField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                criteriaFieldKeyTyped(evt);
            }
        });

        filterButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pekinsoft/loadmaster/res/filter.png"))); // NOI18N
        filterButton.setEnabled(false);
        filterButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                filterButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout filterPanelLayout = new javax.swing.GroupLayout(filterPanel);
        filterPanel.setLayout(filterPanelLayout);
        filterPanelLayout.setHorizontalGroup(
            filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(filterPanelLayout.createSequentialGroup()
                        .addComponent(allFilterOption)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cityFilterOption)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(stateFilterOption)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(filterPanelLayout.createSequentialGroup()
                        .addComponent(criteriaLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(criteriaField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(filterButton)))
                .addContainerGap())
        );
        filterPanelLayout.setVerticalGroup(
            filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(allFilterOption)
                    .addComponent(cityFilterOption)
                    .addComponent(stateFilterOption))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(criteriaLabel)
                    .addComponent(criteriaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filterButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(filterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(brokerLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(brokerList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 232, Short.MAX_VALUE)
                        .addComponent(selectBroker)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(filterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(brokerLabel)
                    .addComponent(brokerList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selectBroker)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void selectBrokerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectBrokerActionPerformed
        // In order to get the selected broker, we need to loop through the
        //+ records to find which record has the selected ID number.
        if ( !brokerList.getSelectedItem().toString().equalsIgnoreCase(
                "select broker/agent...") ) {
            String selectedBroker = brokerList.getSelectedItem().toString();
            long brokerID = Long.valueOf(selectedBroker.substring(
                    selectedBroker.indexOf("(") + 1,    // Start after (
                    selectedBroker.indexOf(")")));  // End before )
        
            try {
                records.first();

                for ( int x = 0; x < records.getRecordCount(); x++ ) {
                    BrokerModel b = records.get();

                    if ( brokerID == b.getId() ) {
                        broker = b;
                        break;
                    } else {
                        if ( records.hasNext() ) 
                            records.next();
                    }
                }
        
                setVisible(false); 
            } catch ( DataStoreException ex ) {
                lr.setMessage("Something went wrong moving to the next record.");
                lr.setThrown(ex);
                Starter.logger.error(lr);

    //            MessageBox.showError(ex, "Database Access");

                dispose();
            }
        }       
    }//GEN-LAST:event_selectBrokerActionPerformed

    private void brokerListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_brokerListItemStateChanged
        if ( !filtering )
            selectBroker.setEnabled(!brokerList.getSelectedItem().toString()
                    .equals("Select Broker/Agent..."));
    }//GEN-LAST:event_brokerListItemStateChanged

    private void filterButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_filterButtonActionPerformed
        filtering = true;
        loadBrokerList();
    }//GEN-LAST:event_filterButtonActionPerformed

    private void criteriaFieldKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_criteriaFieldKeyTyped
        filterButton.setEnabled(criteriaField.getText().length() > 0);
            
    }//GEN-LAST:event_criteriaFieldKeyTyped

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
            java.util.logging.Logger.getLogger(FuelPurchaseDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FuelPurchaseDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FuelPurchaseDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FuelPurchaseDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                BrokerSelector dialog = new BrokerSelector(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JRadioButton allFilterOption;
    private javax.swing.JLabel brokerLabel;
    private javax.swing.JComboBox<String> brokerList;
    private javax.swing.JRadioButton cityFilterOption;
    private javax.swing.JTextField criteriaField;
    private javax.swing.JLabel criteriaLabel;
    private javax.swing.JButton filterButton;
    private javax.swing.ButtonGroup filterButtonGroup;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JButton selectBroker;
    private javax.swing.JRadioButton stateFilterOption;
    // End of variables declaration//GEN-END:variables
}
