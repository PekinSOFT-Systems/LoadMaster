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
 *  Sep 01, 2020  Sean Carrick        Added data validation to the dialog to 
 *                                    ensure all required data is provided and 
 *                                    that the data entered is valid.
 * *****************************************************************************
 */
package com.pekinsoft.loadmaster.view;

import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.controller.CustomerCtl;
import com.pekinsoft.loadmaster.err.DataStoreException;
import com.pekinsoft.loadmaster.model.CustomerModel;
import com.pekinsoft.loadmaster.utils.MessageBox;
import com.pekinsoft.loadmaster.utils.ScreenUtils;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        
        getRootPane().setDefaultButton(selectButton);
        
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
    
    private void doSave() {
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
    }
    
    private boolean isDataValid() {
        lr.setSourceMethodName("isDateValid");
        lr.setMessage("Validating the input data.");
        Starter.logger.enter(lr);
        
        // Before testing any of the dates, we need to make sure that the user
        //+ has selected a customer from the list.
        if ( customerList.getSelectedItem().toString().equals("Select "
                + "customer...") ) {
            // No customer has been chosen, so no sense in moving forward with
            //+ the data validation.
            lr.setMessage("Validation completed. Returning findings.");
            Starter.logger.exit(lr, new Object[] {false});
            return false;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        
        // For the stop to be valid, the late date *MUST* be greater than or
        //+ equal to the early date. 
        if ( lateDate.getDate() != null && earlyDate.getDate() != null ) {
            if ( lateDate.getDate().compareTo(earlyDate.getDate()) < 0 ) {
                // Less than zero (0) means that lateDate is BEFORE earlyDate.
                lr.setMessage("Validation completed. Returning findings.");
                Starter.logger.exit(lr, new Object[] {false});
                return false;
            }
        }
        
        // Before we compare the early and late times, we need to make sure that
        //+ they are valid times. In order for them to be valid, they must pass
        //+ these two tests:
        //+
        //+     Test 1: The hour must NOT be less than zero nor greater than 23.
        //+     Test 2: The minute must NOT be less than zero nor greater than
        //+             59.
        String[] et;
        String[] lt;
        int etHour = 0;
        int etMin = 0;
        int ltHour = 0;
        int ltMin = 0;
        
        if ( earlyTime.getText() != null && !earlyTime.getText().isBlank()
                && lateTime.getText() != null && !lateTime.getText().isBlank() ) {
            et = earlyTime.getText().split(":");
            lt = lateTime.getText().split(":");
            etHour = Integer.valueOf(et[0]);
            etMin = Integer.valueOf(et[1]);
            ltHour = Integer.valueOf(lt[0]);
            ltMin = Integer.valueOf(lt[1]);        
        }
        
        if ( (etHour < 0 || etHour > 23) || (etMin < 0 || etMin > 59)
                || (ltHour < 0 || ltHour > 23) || (ltMin < 0 || ltMin > 59) ) {
            // One of the above tests failed.
            lr.setMessage("Validation completed. Returning findings.");
            Starter.logger.exit(lr, new Object[] {false});
            return false;
        }
        
        // Create a couple of Date object to hold the early and late times for
        //+ comparison, and reset the SimpleDateFormat object to only hold the
        //+ times, in 24-hour format.
        sdf = new SimpleDateFormat("HH:mm");
        Date early = null;
        Date late = null;
        
        if ( earlyTime.getText() != null && lateTime.getText() != null ) {
            try {
                early = sdf.parse(earlyTime.getText());
                late = sdf.parse(lateTime.getText());
            } catch ( ParseException ex ) {
                    lr.setMessage("A stop time was not valid.");
                    lr.setThrown(ex);
                    Starter.logger.error(lr);
                    lr.setMessage("Validation completed. Returning findings.");
                    lr.setThrown(null);
                    Starter.logger.exit(lr, new Object[] {false});
                    // We will return false from here so that the data cannot be
                    //+ saved.
                    return false;
            }
        }
        
        // Make sure that the early and late time objects have been created.
        if ( early != null && late != null ) {
            // Now that we know that both times have been successfully created,
            //+ we need to validate the times, but only if the early and late
            //+ dates are the same day.
            if ( lateDate.getDate().compareTo(earlyDate.getDate()) == 0 ) {
                // Only perform the next text if the early and late dates are on
                //+ the same date.
                if ( late.compareTo(early) < 0 ) {
                    // The late time is before the early time.
                    lr.setMessage("Validation completed. Returning findings.");
                    Starter.logger.exit(lr, new Object[] {false});
                    return false;
                }
            }
        }
        
        lr.setMessage("Validation completed. Returning findings.");
        Starter.logger.exit(lr, new Object[] {true});
        return true;
    }
    
    private void doClose() {
        this.dispose();
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

        customerList.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                customerListItemStateChanged(evt);
            }
        });
        customerList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
        });

        selectButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pekinsoft/loadmaster/res/ok.png"))); // NOI18N
        selectButton.setMnemonic('S');
        selectButton.setText("Select Customer");
        selectButton.setEnabled(false);
        selectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectButtonActionPerformed(evt);
            }
        });
        selectButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
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
        earlyDate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                earlyDateKeyReleased(evt);
            }
        });

        try {
            earlyTime.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        earlyTime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                earlyTimeKeyReleased(evt);
            }
        });

        jLabel3.setText("Late:");

        lateDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lateDateActionPerformed(evt);
            }
        });
        lateDate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lateDateKeyReleased(evt);
            }
        });

        try {
            lateTime.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        lateTime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                lateTimeKeyReleased(evt);
            }
        });

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
        doSave();
    }//GEN-LAST:event_selectButtonActionPerformed

    private void earlyDateFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_earlyDateFocusLost
        
    }//GEN-LAST:event_earlyDateFocusLost

    private void earlyDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_earlyDateActionPerformed
        lateDate.setDate(earlyDate.getDate());
        lateDate.getEditor().setText(earlyDate.getEditor().getText());
        selectButton.setEnabled(isDataValid());
    }//GEN-LAST:event_earlyDateActionPerformed

    private void earlyTimeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_earlyTimeKeyReleased
        lateTime.setText(earlyTime.getText());
        selectButton.setEnabled(isDataValid());
    }//GEN-LAST:event_earlyTimeKeyReleased

    private void checkEnterEscape(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_checkEnterEscape
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER ) 
            doSave();
        else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
            doClose();
    }//GEN-LAST:event_checkEnterEscape

    private void customerListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_customerListItemStateChanged
        selectButton.setEnabled(isDataValid());
    }//GEN-LAST:event_customerListItemStateChanged

    private void lateDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lateDateActionPerformed
        selectButton.setEnabled(isDataValid());
    }//GEN-LAST:event_lateDateActionPerformed

    private void earlyDateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_earlyDateKeyReleased
        selectButton.setEnabled(isDataValid());
    }//GEN-LAST:event_earlyDateKeyReleased

    private void lateDateKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lateDateKeyReleased
        selectButton.setEnabled(isDataValid());
    }//GEN-LAST:event_lateDateKeyReleased

    private void lateTimeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lateTimeKeyReleased
        selectButton.setEnabled(isDataValid());
    }//GEN-LAST:event_lateTimeKeyReleased

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
