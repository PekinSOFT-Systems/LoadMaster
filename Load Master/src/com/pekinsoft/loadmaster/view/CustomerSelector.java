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
 *  Class      :   CustomerSelector.java
 *  Author     :   Sean Carrick
 *  Created    :   Aug 30, 2020 @ 10:14:29 AM
 *  Modified   :   Aug 30, 2020
 *  
 *  Purpose:
 *      To provide a method of visually selecting the customers associated with
 *      the load (i.e., stops). This dialog allows the o/o to select their
 *      pickup and delivery customers to be shown in the booking window and 
 *      windows associated with the load tasks.
 *  
 *  Revision History:
 *  
 *  WHEN          BY                  REASON
 *  ------------  ------------------- ------------------------------------------
 *  Aug 30, 2020  Sean Carrick        Initial creation.
 * *****************************************************************************
 */
package com.pekinsoft.loadmaster.view;

import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.controller.CustomerCtl;
import com.pekinsoft.loadmaster.err.DataStoreException;
import com.pekinsoft.loadmaster.model.CustomerModel;
import com.pekinsoft.loadmaster.utils.MessageBox;
import com.pekinsoft.loadmaster.utils.ScreenUtils;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 *
 * @author Sean Carrick
 */
public class CustomerSelector extends javax.swing.JDialog {

    private CustomerModel customer;
    private CustomerCtl records;
    private LogRecord lr = new LogRecord(Level.ALL, 
            "Logging started in CustomerSelector.");
    
    /**
     * Creates new form CustomerSelector
     */
    public CustomerSelector(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        lr.setSourceClassName(CustomerSelector.class.getName());
        Starter.logger.info(lr);
        
        lr.setMessage("Creating an instance of the CustomerSelector dialog.");
        lr.setSourceMethodName("CustomerSelector");
        lr.setParameters(new Object[] {parent, modal});
        Starter.logger.enter(lr);
        
        initComponents();
        
        lr.setMessage("Attempting to access the brokers database...");
        Starter.logger.debug(lr);
        try {
            records = new CustomerCtl();
            lr.setMessage("Customers database accessed successfully!");
            Starter.logger.info(lr);
        } catch ( DataStoreException ex ) {
            lr.setMessage("Something went wrong accessing the customers database.");
            lr.setThrown(ex);
            Starter.logger.error(lr);
            
            MessageBox.showError(ex, "Database Access");
            
            records = null;
        }
        
        loadList();
        
        setLocation(ScreenUtils.centerDialog(this));
        
        lr.setMessage("CustomerSelector creation complete.");
        Starter.logger.exit(lr, null);
    }
    
    private void loadList() {
        customerList.removeAllItems();
        customerList.addItem("Select customer...");
        
        try {
            records.first();
        } catch ( DataStoreException ex ) {
            lr.setMessage("Something went wrong moving to the next record.");
            lr.setThrown(ex);
            Starter.logger.error(lr);

            MessageBox.showError(ex, "Database Access");
        }
        
        for ( int x = 0; x < records.getRecordCount(); x++ ) {
            CustomerModel c = records.get();
            
            customerList.addItem(c.getCompany() + ": " + c.getCity() + ", " 
                    + c.getState() + " [" + c.getId() + "]");
            
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
    }
    
    public CustomerModel getSelectedCustomer() {
        return customer;
    }
    
    public Date getEarlyDate() {
        return earlyDate.getDate();
    }
    
    public String getEarlyTime() {
        return earlyTime.getText();
    }
    
    public Date getLateDate() {
        return lateDate.getDate();
    }
    
    public String getLateTime() {
        return lateTime.getText();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        customerList = new javax.swing.JComboBox<>();
        selectButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        earlyDate = new org.jdesktop.swingx.JXDatePicker();
        earlyTime = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        lateDate = new org.jdesktop.swingx.JXDatePicker();
        lateTime = new javax.swing.JFormattedTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Customer:");

        customerList.setEditable(true);

        selectButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pekinsoft/loadmaster/res/ok.png"))); // NOI18N
        selectButton.setMnemonic('S');
        selectButton.setText("Select Customer");
        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("Early:");

        earlyDate.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                earlyDateFocusLost(evt);
            }
        });
        earlyDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                earlyDateActionPerformed(evt);
            }
        });

        earlyTime.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm"))));
        earlyTime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                earlyTimeKeyTyped(evt);
            }
        });

        jLabel3.setText("Late:");

        lateTime.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("HH:mm"))));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(selectButton))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(customerList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(earlyDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(earlyTime, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(lateDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lateTime)))
                                .addGap(0, 105, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(customerList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(earlyDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(earlyTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lateDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lateTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(selectButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void selectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectButtonActionPerformed
        // In order to get the selected broker, we need to loop through the
        //+ records to find which record has the selected ID number.
        if ( !customerList.getSelectedItem().toString().equalsIgnoreCase(
                "select customer...") ) {
            String selectedBroker = customerList.getSelectedItem().toString();
            long brokerID = Long.valueOf(selectedBroker.substring(
                    selectedBroker.indexOf("[") + 1,    // Start after (
                    selectedBroker.indexOf("]")));  // End before )
        
            try {
                records.first();

                for ( int x = 0; x < records.getRecordCount(); x++ ) {
                    CustomerModel c = records.get();

                    if ( brokerID == c.getId() ) {
                        customer = c;
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
    }//GEN-LAST:event_selectButtonActionPerformed

    private void earlyDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_earlyDateFocusLost
        
    }//GEN-LAST:event_earlyDateFocusLost

    private void earlyTimeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_earlyTimeKeyTyped
        lateTime.setText(earlyTime.getText());
    }//GEN-LAST:event_earlyTimeKeyTyped

    private void earlyDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_earlyDateActionPerformed
        lateDate.setDate(earlyDate.getDate());
        lateDate.getEditor().setText(earlyDate.getEditor().getText());
    }//GEN-LAST:event_earlyDateActionPerformed

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
            java.util.logging.Logger.getLogger(CustomerSelector.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CustomerSelector.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CustomerSelector.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CustomerSelector.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CustomerSelector dialog = new CustomerSelector(new javax.swing.JFrame(), true);
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
    private javax.swing.JComboBox<String> customerList;
    private org.jdesktop.swingx.JXDatePicker earlyDate;
    private javax.swing.JFormattedTextField earlyTime;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private org.jdesktop.swingx.JXDatePicker lateDate;
    private javax.swing.JFormattedTextField lateTime;
    private javax.swing.JButton selectButton;
    // End of variables declaration//GEN-END:variables
}
