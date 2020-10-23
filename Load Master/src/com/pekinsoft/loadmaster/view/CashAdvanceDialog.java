/*
 * Copyright (C) 2020 PekinSOFT™ Systems
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
 * 
 * Project :   Load Master™
 * Class   :   CashAdvanceDialog
 * Author  :   Sean Carrick
 * Created :   Oct 22, 2020
 * Modified:   Oct 22, 2020
 * 
 * Purpose:
 *     [Provide a general purpose overview for this class]
 * 
 * WHEN          BY                  REASON
 * ------------  ------------------  -------------------------------------------
 * Oct 22, 2020  Sean Carrick        Initial creation.
 * 
 * *****************************************************************************
 */
package com.pekinsoft.loadmaster.view;

import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.controller.EntryCtl;
import com.pekinsoft.loadmaster.controller.FuelCardCtl;
import com.pekinsoft.loadmaster.controller.ReceivablesCtl;
import com.pekinsoft.loadmaster.controller.ReserveCtl;
import com.pekinsoft.loadmaster.err.DataStoreException;
import com.pekinsoft.loadmaster.model.EntryModel;
import com.pekinsoft.loadmaster.model.FuelCardModel;
import com.pekinsoft.loadmaster.model.ReceivablesModel;
import com.pekinsoft.loadmaster.model.ReserveModel;
import com.pekinsoft.loadmaster.utils.MessageBox;
import com.pekinsoft.loadmaster.utils.ScreenUtils;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.LogRecord;
import java.util.logging.Level;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 */
public class CashAdvanceDialog extends javax.swing.JDialog {
    private ReceivablesCtl receivables;
    private ReceivablesModel receivable;
    private LogRecord entry;
    private double availAmount;
    
    /**
     * Creates new form CashAdvance
     */
    public CashAdvanceDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        entry = new LogRecord(Level.ALL, "Creating an instance of the "
                + "CashAdvanceDialog");
        entry.setSourceClassName(getClass().getCanonicalName());
        entry.setSourceMethodName(getClass().getName() + " (Constructor)");
        entry.setParameters(new Object[]{parent, modal});
        entry.setInstant(Instant.now());
        Starter.logger.enter(entry);
        
        initComponents();
        
        setLocation(ScreenUtils.centerDialog(this));
        
        getRootPane().setDefaultButton(okButton);
        
        try {
            receivables = new ReceivablesCtl();
            
            receivable = receivables.getByTripNumber(Starter.props.getProperty(
                    "load.current", "No Active Load"));
        } catch ( DataStoreException ex ) {
            entry.setSourceClassName(getClass().getCanonicalName());
            entry.setSourceMethodName(getClass().getName() + " (Constructor)");
            entry.setMessage(ex.getMessage());
            entry.setThrown(ex);
            entry.setParameters(new Object[]{parent, modal});
            entry.setInstant(Instant.now());
            entry.setLevel(Level.SEVERE);
            Starter.logger.error(entry);
        }
        
        if ( receivable == null ) {
            // The driver is not on an active trip, so let them know and close
            //+ this dialog.
            String msg = "You are not on an active load.\nYou may only take a "
                    + "cash advance when you\nare on a load.\n\nHave you "
                    + "started your trip yet?";
            MessageBox.showWarning(msg, "Cannot Process Advance");
            doClose();
        } else {
            currentTripField.setText(receivable.getTripNumber());
            
            double totalTaken = 0.0;
            
            // To only allow the driver to take up to 50% of the gross revenue
            //+ on the load, we need to access the Fuel Journal to see if the
            //+ user has already taken some money out on this load.
            try {
                FuelCardCtl fuel = new FuelCardCtl();
                ArrayList<FuelCardModel> trips = fuel.getByTripNumber(
                        Starter.props.getProperty("load.current", 
                        "No Active Load"));
                
                
                if ( trips.size() > 0 ) {
                    for ( int z = 0; z < trips.size(); z++ ) {
                        totalTaken += trips.get(z).getAmount();
                    }
                }
            } catch ( DataStoreException ex ) {
                entry.setSourceClassName(getClass().getCanonicalName());
                entry.setSourceMethodName(getClass().getName() + " (Constructor)");
                entry.setMessage(ex.getMessage());
                entry.setLevel(Level.ALL);
                entry.setParameters(new Object[]{parent, modal});
                entry.setThrown(ex);
                entry.setInstant(Instant.now());
                Starter.logger.error(entry);
            }
            
            double percentageAllowed = Starter.props.getPropertyAsDouble(
                    "acct.max.advance", "50");
            availAmount = (receivable.getAmount() * (percentageAllowed / 100)) 
                    - totalTaken;
            NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(2);
            nf.setMinimumFractionDigits(2);
            nf.setMinimumIntegerDigits(1);
            
            if ( availAmount == 0.0 ) {
                MessageBox.showInfo("You have no funds remaining.", 
                        "No Cash Advance");
            } else
                availableAmountField.setText(nf.format(availAmount));
        }
        
        entry.setSourceClassName(getClass().getCanonicalName());
        entry.setSourceMethodName(getClass().getName() + " (Constructor)");
        entry.setMessage("CashAdvanceDialog successfully initialized.");
        entry.setLevel(Level.ALL);
        entry.setParameters(new Object[]{parent, modal});
        entry.setThrown(null);
        entry.setInstant(Instant.now());
        Starter.logger.exit(entry, null);
    }
    
    private void doClose() {
        dispose();
    }
    
    private void doSave() {
        // Make sure that we have data to work with.
        if ( receivable != null ) {
            
            if ( checkingOption.isSelected() ) {
                throw new UnsupportedOperationException("Not yet implemented");
//                to = "Checking Account";
//                toAcct = 50010;
            } else if ( savingsOption.isSelected() ) {
                throw new UnsupportedOperationException("Not yet implemented");
//                to = "Savings Account";
//                toAcct = 50020;
            } else if ( retirementOption.isSelected() ){
                throw new UnsupportedOperationException("Not yet implemented");
//                to = "Retirement Savings";
//                toAcct = 50030;
            } else if ( fuelOption.isSelected() ) {
                FuelCardModel advance = new FuelCardModel();
                advance.setAmount(Double.parseDouble(advAmountField.getText()));
                advance.setDate(new Date());
                advance.setFromAcct(ReceivablesModel.ACCOUNT_NUMBER);
                advance.setId(System.currentTimeMillis());
                advance.setPosted(false);
                advance.setTripNumber(Starter.props.getProperty("load.current",
                        "No Active Load"));
                
                postFuelJournal(advance);
            } else if ( reserveOption.isSelected() ) {
                ReserveModel advance = new ReserveModel();
                advance.setAmount(Double.parseDouble(advAmountField.getText()));
                advance.setDate(new Date());
                advance.setFromAccount(ReceivablesModel.ACCOUNT_NUMBER);
                advance.setId(System.currentTimeMillis());
                advance.setPosted(false);
                advance.setTripNumber(Starter.props.getProperty("load.current", 
                        "No Active Load"));
                
                postReserves(advance);
            } else {
//                to = "<UNKNOWN>";
            }
        }
        
        doClose();
    }
    
    private void postReserves(ReserveModel advance) {
        ReserveCtl journal = null;
        EntryCtl gl = null;
        
        try {
            journal = new ReserveCtl();
            journal.addNew(advance);
            
            // Close the journal to write the data to the journal file.
            journal.close();
            
            // Reopen the journal to work with the data.
            journal = new ReserveCtl();
            
            if ( !Starter.props.getPropertyAsBoolean("acct.batch", "false") ) {
                gl = new EntryCtl();
                
                for ( int x = 0; x < journal.getRecordCount(); x++ ) {
                    if ( !journal.get().isPosted() ) {
                        EntryModel model = new EntryModel();
                        model.setAmount(journal.get(x).getAmount());
                        model.setBalanced(false);
                        model.setCode("CAdv");
                        model.setDate(journal.get(x).getDate());
                        model.setDeductible(false);
                        model.setDescription("Advance on Trip # " 
                                + journal.get(x).getTripNumber() 
                                + " from AR");
                        model.setFromAccount(journal.get(x).getFromAccount());
                        model.setToAccount(ReserveModel.ACCOUNT_NUMBER);
                        
                        gl.addNew(model);
                        
                        journal.get(x).setPosted(true);
                    }
                }
                
                gl.close();
            } else
                LoadMaster.batchToProcess = true;
        } catch ( DataStoreException ex ) {
            entry.setSourceClassName(getClass().getCanonicalName());
            entry.setSourceMethodName("postFuelJournal");
            entry.setParameters(null);
            entry.setThrown(ex);
            entry.setMessage(ex.getMessage());
            entry.setLevel(Level.SEVERE);
            entry.setInstant(Instant.now());
            Starter.logger.error(entry);
        }
    }
    
    private void postFuelJournal(FuelCardModel advance) {
        FuelCardCtl journal = null;
        EntryCtl gl = null;
        
        try {
            journal = new FuelCardCtl();
            journal.addNew(advance);
            
            // Close the journal to write the data to the journal file.
            journal.close();
            
            // Reopen the journal to work with the data.
            journal = new FuelCardCtl();
            
            if ( !Starter.props.getPropertyAsBoolean("acct.batch", "false") ) {
                gl = new EntryCtl();
                
                for ( int x = 0; x < journal.getRecordCount(); x++ ) {
                    if ( !journal.get().isPosted() ) {
                        EntryModel model = new EntryModel();
                        model.setAmount(journal.get(x).getAmount());
                        model.setBalanced(false);
                        model.setCode("CAdv");
                        model.setDate(journal.get(x).getDate());
                        model.setDeductible(false);
                        model.setDescription("Advance on Trip # " 
                                + journal.get(x).getTripNumber() 
                                + " from AR");
                        model.setFromAccount(journal.get(x).getFromAcct());
                        model.setToAccount(FuelCardModel.ACCOUNT_NUMBER);
                        
                        gl.addNew(model);
                        
                        journal.get(x).setPosted(true);
                    }
                }
                
                gl.close();
            } else
                LoadMaster.batchToProcess = true;
        } catch ( DataStoreException ex ) {
            entry.setSourceClassName(getClass().getCanonicalName());
            entry.setSourceMethodName("postFuelJournal");
            entry.setParameters(null);
            entry.setThrown(ex);
            entry.setMessage(ex.getMessage());
            entry.setLevel(Level.SEVERE);
            entry.setInstant(Instant.now());
            Starter.logger.error(entry);
        }
    }
    
    private boolean isDataValid() {
        double desired = 0.0;
        double amt = 0.0;
        if ( advAmountField.getText() != null 
                && !advAmountField.getText().isBlank()
                && ! advAmountField.getText().isEmpty()) {
            desired = Double.parseDouble(advAmountField.getText());
        }
        
        if ( receivable != null ) {
            amt = receivable.getAmount() - desired;
        }
        
        boolean retVal = false;
        
        if ( amt > 0 && amt <= receivable.getAmount() )
            retVal = true;
        
        return retVal;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        depositButtonGroup = new javax.swing.ButtonGroup();
        availableAmountLabel = new javax.swing.JLabel();
        availableAmountField = new javax.swing.JFormattedTextField();
        currentTripLabel = new javax.swing.JLabel();
        currentTripField = new javax.swing.JTextField();
        depositPanel = new javax.swing.JPanel();
        checkingOption = new javax.swing.JRadioButton();
        savingsOption = new javax.swing.JRadioButton();
        retirementOption = new javax.swing.JRadioButton();
        fuelOption = new javax.swing.JRadioButton();
        reserveOption = new javax.swing.JRadioButton();
        advAmountLabel = new javax.swing.JLabel();
        advAmountField = new javax.swing.JFormattedTextField();
        cancelButton = new javax.swing.JButton();
        okButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        availableAmountLabel.setText("Available Amount:");

        availableAmountField.setEditable(false);
        availableAmountField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        availableAmountField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkForEscape(evt);
            }
        });

        currentTripLabel.setText("Current Trip:");

        currentTripField.setEditable(false);
        currentTripField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkForEscape(evt);
            }
        });

        depositPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Deposit To"));

        depositButtonGroup.add(checkingOption);
        checkingOption.setMnemonic('C');
        checkingOption.setText("Checking Account");
        checkingOption.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkForEscape(evt);
            }
        });

        depositButtonGroup.add(savingsOption);
        savingsOption.setMnemonic('S');
        savingsOption.setText("Savings Account");
        savingsOption.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkForEscape(evt);
            }
        });

        depositButtonGroup.add(retirementOption);
        retirementOption.setMnemonic('R');
        retirementOption.setText("Retirement Account");
        retirementOption.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkForEscape(evt);
            }
        });

        depositButtonGroup.add(fuelOption);
        fuelOption.setMnemonic('F');
        fuelOption.setSelected(true);
        fuelOption.setText("Fuel Card");
        fuelOption.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkForEscape(evt);
            }
        });

        depositButtonGroup.add(reserveOption);
        reserveOption.setText("Maintenance Reserve");

        javax.swing.GroupLayout depositPanelLayout = new javax.swing.GroupLayout(depositPanel);
        depositPanel.setLayout(depositPanelLayout);
        depositPanelLayout.setHorizontalGroup(
            depositPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(depositPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(depositPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(checkingOption)
                    .addComponent(savingsOption)
                    .addComponent(retirementOption)
                    .addComponent(fuelOption)
                    .addComponent(reserveOption))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        depositPanelLayout.setVerticalGroup(
            depositPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(depositPanelLayout.createSequentialGroup()
                .addComponent(checkingOption)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(savingsOption)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(retirementOption)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(reserveOption)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(fuelOption)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        advAmountLabel.setText("Advance Amount:");

        advAmountField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        advAmountField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                advAmountFieldFocusLost(evt);
            }
        });
        advAmountField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                advAmountFieldPropertyChange(evt);
            }
        });
        advAmountField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkForEscape(evt);
            }
        });

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pekinsoft/loadmaster/res/cancel16.png"))); // NOI18N
        cancelButton.setMnemonic('C');
        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });

        okButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pekinsoft/loadmaster/res/ok.png"))); // NOI18N
        okButton.setMnemonic('T');
        okButton.setText("Take Advance");
        okButton.setEnabled(false);
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(availableAmountLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(availableAmountField, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(currentTripLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(currentTripField, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(depositPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(12, 12, 12)
                                .addComponent(advAmountLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(advAmountField))
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(okButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(cancelButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(availableAmountLabel)
                    .addComponent(availableAmountField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(currentTripLabel)
                    .addComponent(currentTripField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(advAmountLabel)
                            .addComponent(advAmountField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(okButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cancelButton))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(depositPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void checkForEscape(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_checkForEscape
        if ( evt.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE )
            doClose();
    }//GEN-LAST:event_checkForEscape

    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed
        doSave();
    }//GEN-LAST:event_okButtonActionPerformed

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doClose();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void advAmountFieldFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_advAmountFieldFocusLost
        okButton.setEnabled(isDataValid());
    }//GEN-LAST:event_advAmountFieldFocusLost

    private void advAmountFieldPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_advAmountFieldPropertyChange
        double want = 0.0;
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMinimumIntegerDigits(1);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        if ( receivable != null ) {
            try {
                want = nf.parse(advAmountField.getText()).doubleValue();
            } catch ( ParseException ex ) {
                entry.setSourceClassName(getClass().getCanonicalName());
                entry.setSourceMethodName(getClass().getName() + " (Constructor)");
                entry.setMessage(ex.getMessage());
                entry.setLevel(Level.ALL);
                entry.setParameters(new Object[]{evt});
                entry.setThrown(ex);
                entry.setInstant(Instant.now());
                Starter.logger.error(entry);
            }
            
            double left = availAmount - want;
            
            if ( left <= 0.0 )
                availableAmountField.setText(nf.format(0.0));
            else
                availableAmountField.setText(nf.format(left));
        }
        okButton.setEnabled((availableAmountField.getText() != null)
                && ((availAmount - want) > 0) && advAmountField.getText() != null );
    }//GEN-LAST:event_advAmountFieldPropertyChange

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
            java.util.logging.Logger.getLogger(CashAdvanceDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CashAdvanceDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CashAdvanceDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CashAdvanceDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                CashAdvanceDialog dialog = new CashAdvanceDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JFormattedTextField advAmountField;
    private javax.swing.JLabel advAmountLabel;
    private javax.swing.JFormattedTextField availableAmountField;
    private javax.swing.JLabel availableAmountLabel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JRadioButton checkingOption;
    private javax.swing.JTextField currentTripField;
    private javax.swing.JLabel currentTripLabel;
    private javax.swing.ButtonGroup depositButtonGroup;
    private javax.swing.JPanel depositPanel;
    private javax.swing.JRadioButton fuelOption;
    private javax.swing.JButton okButton;
    private javax.swing.JRadioButton reserveOption;
    private javax.swing.JRadioButton retirementOption;
    private javax.swing.JRadioButton savingsOption;
    // End of variables declaration//GEN-END:variables
}
