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
 *  Class      :   NewBrokerDlg.java
 *  Author     :   Sean Carrick
 *  Created    :   Sep 14, 2020 @ 12:23:47 PM
 *  Modified   :   Sep 14, 2020
 *  
 *  Purpose:
 *  
 *  Revision History:
 *  
 *  WHEN          BY                  REASON
 *  ------------  ------------------- ------------------------------------------
 *  Sep 14, 2020  Sean Carrick        Initial creation.
 * *****************************************************************************
 */
package com.pekinsoft.loadmaster.view;

import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.controller.BrokerCtl;
import com.pekinsoft.loadmaster.err.DataStoreException;
import com.pekinsoft.loadmaster.model.BrokerModel;
import com.pekinsoft.loadmaster.utils.MessageBox;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.swing.JTextField;

/**
 *
 * @author Sean Carrick
 */
public class NewBrokerDlg extends javax.swing.JDialog {
    private final Color errFore = Color.YELLOW;
    private final Color errBack = Color.RED;
    private final Color fore = SystemColor.textText;
    private final Color back = SystemColor.text;
    private final Color ctl = SystemColor.control;
    private final Color tip = SystemColor.info;
    private final Color tipText = SystemColor.infoText;
        
    private BrokerCtl records;
    private BrokerModel broker;
    private boolean cancelled;
    private final LogRecord lr;

    /**
     * Creates new form NewBrokerDlg
     */
    public NewBrokerDlg(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        lr = new LogRecord(Level.ALL, "Creating the New Broker dialog...");
        lr.setSourceClassName(this.getClass().getName());
        lr.setSourceMethodName("NewBrokerDlg (constructor)");
        lr.setParameters(new Object[]{parent, modal});
        Starter.logger.enter(lr);
        
        initComponents();
        
        setTitle("New Broker/Agent Entry");
        
        int left = Toolkit.getDefaultToolkit().getScreenSize().width;
        int top = Toolkit.getDefaultToolkit().getScreenSize().height;
        
        left = (left - getWidth()) / 2;
        top = (top - getHeight()) / 2;
        setLocation(left, top);
        
        try {
            records = new BrokerCtl();
        } catch ( DataStoreException ex ) {
            lr.setMessage("Something went wrong accessing the brokers table.");
            lr.setThrown(ex);
            Starter.logger.error(lr);
            
            MessageBox.showError(ex, "Data Store Error");
        }
        
        cancelled = false;
    }
    
    public BrokerModel getBroker() {
        return broker;
    }
    
    public boolean isCancelled() {
        return cancelled;
    }
    
    private void doSave() {
        lr.setSourceMethodName("doSave");
        lr.setMessage("Saving the new broker record.");
        Starter.logger.enter(lr);
        
        companyField.requestFocus();

        broker = new BrokerModel();
        idField.setText(String.valueOf(broker.getId()));

        // Perform data save here.
        broker.setCity(cityField.getText());
        broker.setEmail(emailField.getText());
        broker.setCompany(companyField.getText());
        broker.setContact(nameField.getText());
        broker.setId(Long.valueOf(idField.getText()));
        broker.setPhone(phoneField.getText());
        broker.setFax(faxField.getText());
        broker.setState(stateField.getText());
        broker.setStreet(streetField.getText());
        broker.setSuite(suiteField.getText());
        broker.setZip(zipField.getText());

        records.addNew(broker);

        lr.setMessage("Attempting to save the data to file.");
        Starter.logger.debug(lr);
        try {
            records.close();
            lr.setMessage("Save to file was successful!");
            Starter.logger.info(lr);
            
            setTitle("Customer Entry (" + records.getRecordCount() + " Records)");
        } catch (DataStoreException ex) {
            lr.setMessage("Something went wrong accessing the brokers database.");
            lr.setThrown(ex);
            Starter.logger.error(lr);

            MessageBox.showError(ex, "Database Access");

            records = null;
        }

        lr.setMessage("Checking to see if user would like to enter another broker...");
        Starter.logger.info(lr);
        
        cancelled = false;
        
        setVisible(false);
        
//        int choice = MessageBox.askQuestion("Would you like to add another "
//                + "broker?", "Add Another", false);
//
//        if (choice == MessageBox.NO_OPTION) {
//            lr.setMessage("No more brokers being added. Exiting the function "
//                    + "and closing the window.");
//            Starter.logger.exit(lr, null);
//
//            lr.setMessage("Exiting doSave to close the window.");
//            Starter.logger.exit(lr, null);
//            setVisible(false);
//        } else {
//            lr.setMessage("User wants to add another broker. Clearing the form.");
//            Starter.logger.exit(lr, null);
//            doClear();
//        }
    }
    
    private void doCancel() {
        lr.setSourceMethodName("doCancel");
        lr.setMessage("Entering the form closing function.");
        Starter.logger.enter(lr);
        
        LoadMaster.fileProgress.setValue(0);
        
        lr.setMessage("Closing the window.");
        Starter.logger.exit(lr, null);
        cancelled = true;
        setVisible(false);
    }
    
    private void doClear() {
        broker = new BrokerModel();
        idField.setText(String.valueOf(broker.getId()));
        companyField.setText("");
        streetField.setText("");
        suiteField.setText("");
        cityField.setText("");
        stateField.setText("");
        zipField.setText("");
        nameField.setText("");
        phoneField.setText("");
        faxField.setText("");
        emailField.setText("");
    }
    
    private boolean isOneNamePresent() {
        return ( nameField.getText() != null && !nameField.getText().isBlank() 
                && !nameField.getText().isEmpty() ) ||
                ( companyField.getText() != null && !companyField.getText().isBlank()
                && !companyField.getText().isEmpty() );
    }
    
    private boolean isOneContactMethodPresent() {
        return ( phoneField.getValue() != null && !phoneField.getValue().equals("") ) 
                || ( faxField.getValue() != null && !faxField.getText().equals("") ) ||
                (( streetField.getText() != null && !streetField.getText().isBlank()
                    && !streetField.getText().isEmpty() ) &&
                ( cityField.getText() != null && !cityField.getText().isBlank() 
                    && !cityField.getText().isEmpty() ) &&
                ( stateField.getText() != null && !stateField.getText().isBlank()
                    && !stateField.getText().isEmpty() ) &&
                ( zipField.getText() != null && !zipField.getText().isBlank()
                    && !zipField.getText().isEmpty() 
                    && !zipField.getText().equalsIgnoreCase("unavailable") ))
                || ( emailField.getText() != null 
                    && !emailField.getText().isBlank()
                    && !emailField.getText().isEmpty() );
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        stateField = new javax.swing.JTextField();
        emailField = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        zipField = new javax.swing.JTextField();
        faxField = new javax.swing.JFormattedTextField();
        helpPanel = new javax.swing.JPanel();
        helpLabel = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        streetField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        companyField = new javax.swing.JTextField();
        controlPanel = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        suiteField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        cityField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        idField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        phoneField = new javax.swing.JFormattedTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("New Broker/Agent Entry");

        jLabel4.setText("Email:");

        stateField.setName("stateField"); // NOI18N
        stateField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                stateFielddoSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                stateFieldvalidateData(evt);
            }
        });
        stateField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                stateFieldcheckEnterEscape(evt);
            }
        });

        emailField.setName("emailField"); // NOI18N
        emailField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                emailFielddoSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                emailFieldvalidateData(evt);
            }
        });
        emailField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                emailFieldcheckEnterEscape(evt);
            }
        });

        jLabel10.setText("Zip Code:");

        jLabel5.setText("Fax:");

        zipField.setName("zipField"); // NOI18N
        zipField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                zipFielddoSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                zipFieldvalidateData(evt);
            }
        });
        zipField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                zipFieldcheckEnterEscape(evt);
            }
        });

        try {
            faxField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(###) ###-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        faxField.setName("faxField"); // NOI18N
        faxField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                faxFielddoSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                faxFieldvalidateData(evt);
            }
        });
        faxField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                faxFieldcheckEnterEscape(evt);
            }
        });

        javax.swing.GroupLayout helpPanelLayout = new javax.swing.GroupLayout(helpPanel);
        helpPanel.setLayout(helpPanelLayout);
        helpPanelLayout.setHorizontalGroup(
            helpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(helpPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(helpLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        helpPanelLayout.setVerticalGroup(
            helpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(helpPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(helpLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel6.setText("Street:");

        streetField.setName("streetField"); // NOI18N
        streetField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                streetFielddoSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                streetFieldvalidateData(evt);
            }
        });
        streetField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                streetFieldcheckEnterEscape(evt);
            }
        });

        jLabel11.setText("Company:");

        jLabel7.setText("Suite:");

        companyField.setName("companyField"); // NOI18N
        companyField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                companyFielddoSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                companyFieldvalidateData(evt);
            }
        });
        companyField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                companyFieldcheckEnterEscape(evt);
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
        cancelButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cancelButtoncheckEnterEscape(evt);
            }
        });

        saveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pekinsoft/loadmaster/res/add.png"))); // NOI18N
        saveButton.setMnemonic('S');
        saveButton.setText("Save");
        saveButton.setEnabled(false);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        saveButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                saveButtoncheckEnterEscape(evt);
            }
        });

        javax.swing.GroupLayout controlPanelLayout = new javax.swing.GroupLayout(controlPanel);
        controlPanel.setLayout(controlPanelLayout);
        controlPanelLayout.setHorizontalGroup(
            controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, controlPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(saveButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton)
                .addContainerGap())
        );
        controlPanelLayout.setVerticalGroup(
            controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, controlPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(controlPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(saveButton))
                .addContainerGap())
        );

        suiteField.setName("suiteField"); // NOI18N
        suiteField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                suiteFielddoSelection(evt);
            }
        });
        suiteField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                suiteFieldcheckEnterEscape(evt);
            }
        });

        jLabel8.setText("City:");

        cityField.setName("cityField"); // NOI18N
        cityField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cityFielddoSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                cityFieldvalidateData(evt);
            }
        });
        cityField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cityFieldcheckEnterEscape(evt);
            }
        });

        jLabel1.setText("ID:");

        jLabel9.setText("State:");

        idField.setEditable(false);
        idField.setFocusable(false);
        idField.setName("idField"); // NOI18N

        jLabel2.setText("Name:");

        nameField.setName("nameField"); // NOI18N
        nameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                nameFielddoSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                nameFieldvalidateData(evt);
            }
        });
        nameField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                nameFieldcheckEnterEscape(evt);
            }
        });

        jLabel3.setText("Phone:");

        try {
            phoneField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(###) ###-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        phoneField.setName("phoneField"); // NOI18N
        phoneField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                phoneFielddoSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                phoneFieldvalidateData(evt);
            }
        });
        phoneField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                phoneFieldcheckEnterEscape(evt);
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
                        .addComponent(helpPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(controlPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1)
                            .addComponent(jLabel11)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(companyField)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(streetField)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel7))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(cityField, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(stateField, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel10)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(suiteField, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(7, 7, 7)
                                        .addComponent(zipField, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap())
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(emailField, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                                    .addComponent(nameField, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel5))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(phoneField)
                                    .addComponent(faxField, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(phoneField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(faxField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(companyField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(streetField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(suiteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(cityField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(stateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10)
                    .addComponent(zipField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(controlPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(helpPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void stateFielddoSelection(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_stateFielddoSelection
        String name = "";

        if ( evt.getSource() instanceof javax.swing.JTextField ) {
            name = ((javax.swing.JTextField)evt.getSource()).getName();
            ((javax.swing.JTextField)evt.getSource()).selectAll();
        } else if ( evt.getSource() instanceof javax.swing.JTextArea ) {
            name = ((javax.swing.JTextArea)evt.getSource()).getName();
            ((javax.swing.JTextArea)evt.getSource()).select(
                ((javax.swing.JTextArea)evt.getSource()).getText().length(),
                ((javax.swing.JTextArea)evt.getSource()).getText().length());
        }

        String msg = "";

        switch ( name ) {
            case "cityField":
            msg = "<html>City in which the broker is located. "
            + "This field is <em>optional</em>.";
            break;
            case "companyField":
            msg = "<html>Company name for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "streetField":
            msg = "<html>Street address for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "suiteField":
            msg = "<html>Suite number for this broker, if any"
            + ". This field is <em>optional</em>.";
            break;
            case "stateField":
            msg = "<html>Postal abbreviation for the state or "
            + "province in which this broker is located. This field "
            + "is <em>optional</em>. However, if this field <strong>is"
            + "</strong> used, the State <strong>must</strong> be a "
            + "valid US State or Canadian Province abbreviation.";
            break;
            case "zipField":
            msg = "<html>Postal (Zip) Code for this broker. This field is "
            + "<em>optional</em> However, if this field <strong>is"
            + "</strong> used, the Zip Code <strong>must</strong> "
            + "be a valid US or Canadian Postal Code.";
            break;
            case "nameField":
            msg = "<html>Name of contact at this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "phoneField":
            msg = "<html>Phone number for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "faxField":
            msg = "<html>Fax number for this broker. This "
            + "field is <em>optional</em>.";
            break;
            case "emailField":
            msg = "<html>Email address for this broker. This field is "
            + "<em>optional</em>.";
            default:
            msg = "";
            break;
        }

        if ( msg != null && !msg.isBlank() && !msg.isEmpty() ) {
            helpPanel.setBackground(tip);
            helpLabel.setForeground(tipText);
            helpLabel.setText(msg);
        } else {
            helpPanel.setBackground(ctl);
            helpLabel.setForeground(fore);
            helpLabel.setText("");
        }
    }//GEN-LAST:event_stateFielddoSelection

    private void stateFieldvalidateData(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_stateFieldvalidateData
        // Deselect the text in the field.
        if ( evt.getSource() instanceof JTextField )
        ((JTextField)evt.getSource()).select(0, 0);

        saveButton.setEnabled(isOneContactMethodPresent() && isOneNamePresent());
    }//GEN-LAST:event_stateFieldvalidateData

    private void stateFieldcheckEnterEscape(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_stateFieldcheckEnterEscape
        // Check to see if the enter or escape key was pressed.
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
        doSave();
        else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
        doCancel();
    }//GEN-LAST:event_stateFieldcheckEnterEscape

    private void emailFielddoSelection(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_emailFielddoSelection
        String name = "";

        if ( evt.getSource() instanceof javax.swing.JTextField ) {
            name = ((javax.swing.JTextField)evt.getSource()).getName();
            ((javax.swing.JTextField)evt.getSource()).selectAll();
        } else if ( evt.getSource() instanceof javax.swing.JTextArea ) {
            name = ((javax.swing.JTextArea)evt.getSource()).getName();
            ((javax.swing.JTextArea)evt.getSource()).select(
                ((javax.swing.JTextArea)evt.getSource()).getText().length(),
                ((javax.swing.JTextArea)evt.getSource()).getText().length());
        }

        String msg = "";

        switch ( name ) {
            case "cityField":
            msg = "<html>City in which the broker is located. "
            + "This field is <em>optional</em>.";
            break;
            case "companyField":
            msg = "<html>Company name for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "streetField":
            msg = "<html>Street address for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "suiteField":
            msg = "<html>Suite number for this broker, if any"
            + ". This field is <em>optional</em>.";
            break;
            case "stateField":
            msg = "<html>Postal abbreviation for the state or "
            + "province in which this broker is located. This field "
            + "is <em>optional</em>. However, if this field <strong>is"
            + "</strong> used, the State <strong>must</strong> be a "
            + "valid US State or Canadian Province abbreviation.";
            break;
            case "zipField":
            msg = "<html>Postal (Zip) Code for this broker. This field is "
            + "<em>optional</em> However, if this field <strong>is"
            + "</strong> used, the Zip Code <strong>must</strong> "
            + "be a valid US or Canadian Postal Code.";
            break;
            case "nameField":
            msg = "<html>Name of contact at this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "phoneField":
            msg = "<html>Phone number for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "faxField":
            msg = "<html>Fax number for this broker. This "
            + "field is <em>optional</em>.";
            break;
            case "emailField":
            msg = "<html>Email address for this broker. This field is "
            + "<em>optional</em>.";
            default:
            msg = "";
            break;
        }

        if ( msg != null && !msg.isBlank() && !msg.isEmpty() ) {
            helpPanel.setBackground(tip);
            helpLabel.setForeground(tipText);
            helpLabel.setText(msg);
        } else {
            helpPanel.setBackground(ctl);
            helpLabel.setForeground(fore);
            helpLabel.setText("");
        }
    }//GEN-LAST:event_emailFielddoSelection

    private void emailFieldvalidateData(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_emailFieldvalidateData
        // Deselect the text in the field.
        if ( evt.getSource() instanceof JTextField )
        ((JTextField)evt.getSource()).select(0, 0);

        saveButton.setEnabled(isOneContactMethodPresent() && isOneNamePresent());
    }//GEN-LAST:event_emailFieldvalidateData

    private void emailFieldcheckEnterEscape(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_emailFieldcheckEnterEscape
        // Check to see if the enter or escape key was pressed.
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
        doSave();
        else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
        doCancel();
    }//GEN-LAST:event_emailFieldcheckEnterEscape

    private void zipFielddoSelection(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_zipFielddoSelection
        String name = "";

        if ( evt.getSource() instanceof javax.swing.JTextField ) {
            name = ((javax.swing.JTextField)evt.getSource()).getName();
            ((javax.swing.JTextField)evt.getSource()).selectAll();
        } else if ( evt.getSource() instanceof javax.swing.JTextArea ) {
            name = ((javax.swing.JTextArea)evt.getSource()).getName();
            ((javax.swing.JTextArea)evt.getSource()).select(
                ((javax.swing.JTextArea)evt.getSource()).getText().length(),
                ((javax.swing.JTextArea)evt.getSource()).getText().length());
        }

        String msg = "";

        switch ( name ) {
            case "cityField":
            msg = "<html>City in which the broker is located. "
            + "This field is <em>optional</em>.";
            break;
            case "companyField":
            msg = "<html>Company name for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "streetField":
            msg = "<html>Street address for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "suiteField":
            msg = "<html>Suite number for this broker, if any"
            + ". This field is <em>optional</em>.";
            break;
            case "stateField":
            msg = "<html>Postal abbreviation for the state or "
            + "province in which this broker is located. This field "
            + "is <em>optional</em>. However, if this field <strong>is"
            + "</strong> used, the State <strong>must</strong> be a "
            + "valid US State or Canadian Province abbreviation.";
            break;
            case "zipField":
            msg = "<html>Postal (Zip) Code for this broker. This field is "
            + "<em>optional</em> However, if this field <strong>is"
            + "</strong> used, the Zip Code <strong>must</strong> "
            + "be a valid US or Canadian Postal Code.";
            break;
            case "nameField":
            msg = "<html>Name of contact at this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "phoneField":
            msg = "<html>Phone number for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "faxField":
            msg = "<html>Fax number for this broker. This "
            + "field is <em>optional</em>.";
            break;
            case "emailField":
            msg = "<html>Email address for this broker. This field is "
            + "<em>optional</em>.";
            default:
            msg = "";
            break;
        }

        if ( msg != null && !msg.isBlank() && !msg.isEmpty() ) {
            helpPanel.setBackground(tip);
            helpLabel.setForeground(tipText);
            helpLabel.setText(msg);
        } else {
            helpPanel.setBackground(ctl);
            helpLabel.setForeground(fore);
            helpLabel.setText("");
        }
    }//GEN-LAST:event_zipFielddoSelection

    private void zipFieldvalidateData(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_zipFieldvalidateData
        // Deselect the text in the field.
        if ( evt.getSource() instanceof JTextField )
        ((JTextField)evt.getSource()).select(0, 0);

        saveButton.setEnabled(isOneContactMethodPresent() && isOneNamePresent());
    }//GEN-LAST:event_zipFieldvalidateData

    private void zipFieldcheckEnterEscape(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_zipFieldcheckEnterEscape
        // Check to see if the enter or escape key was pressed.
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
        doSave();
        else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
        doCancel();
    }//GEN-LAST:event_zipFieldcheckEnterEscape

    private void faxFielddoSelection(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_faxFielddoSelection
        String name = "";

        if ( evt.getSource() instanceof javax.swing.JTextField ) {
            name = ((javax.swing.JTextField)evt.getSource()).getName();
            ((javax.swing.JTextField)evt.getSource()).selectAll();
        } else if ( evt.getSource() instanceof javax.swing.JTextArea ) {
            name = ((javax.swing.JTextArea)evt.getSource()).getName();
            ((javax.swing.JTextArea)evt.getSource()).select(
                ((javax.swing.JTextArea)evt.getSource()).getText().length(),
                ((javax.swing.JTextArea)evt.getSource()).getText().length());
        }

        String msg = "";

        switch ( name ) {
            case "cityField":
            msg = "<html>City in which the broker is located. "
            + "This field is <em>optional</em>.";
            break;
            case "companyField":
            msg = "<html>Company name for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "streetField":
            msg = "<html>Street address for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "suiteField":
            msg = "<html>Suite number for this broker, if any"
            + ". This field is <em>optional</em>.";
            break;
            case "stateField":
            msg = "<html>Postal abbreviation for the state or "
            + "province in which this broker is located. This field "
            + "is <em>optional</em>. However, if this field <strong>is"
            + "</strong> used, the State <strong>must</strong> be a "
            + "valid US State or Canadian Province abbreviation.";
            break;
            case "zipField":
            msg = "<html>Postal (Zip) Code for this broker. This field is "
            + "<em>optional</em> However, if this field <strong>is"
            + "</strong> used, the Zip Code <strong>must</strong> "
            + "be a valid US or Canadian Postal Code.";
            break;
            case "nameField":
            msg = "<html>Name of contact at this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "phoneField":
            msg = "<html>Phone number for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "faxField":
            msg = "<html>Fax number for this broker. This "
            + "field is <em>optional</em>.";
            break;
            case "emailField":
            msg = "<html>Email address for this broker. This field is "
            + "<em>optional</em>.";
            default:
            msg = "";
            break;
        }

        if ( msg != null && !msg.isBlank() && !msg.isEmpty() ) {
            helpPanel.setBackground(tip);
            helpLabel.setForeground(tipText);
            helpLabel.setText(msg);
        } else {
            helpPanel.setBackground(ctl);
            helpLabel.setForeground(fore);
            helpLabel.setText("");
        }
    }//GEN-LAST:event_faxFielddoSelection

    private void faxFieldvalidateData(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_faxFieldvalidateData
        // Deselect the text in the field.
        if ( evt.getSource() instanceof JTextField )
        ((JTextField)evt.getSource()).select(0, 0);

        saveButton.setEnabled(isOneContactMethodPresent() && isOneNamePresent());
    }//GEN-LAST:event_faxFieldvalidateData

    private void faxFieldcheckEnterEscape(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_faxFieldcheckEnterEscape
        // Check to see if the enter or escape key was pressed.
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
        doSave();
        else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
        doCancel();
    }//GEN-LAST:event_faxFieldcheckEnterEscape

    private void streetFielddoSelection(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_streetFielddoSelection
        String name = "";

        if ( evt.getSource() instanceof javax.swing.JTextField ) {
            name = ((javax.swing.JTextField)evt.getSource()).getName();
            ((javax.swing.JTextField)evt.getSource()).selectAll();
        } else if ( evt.getSource() instanceof javax.swing.JTextArea ) {
            name = ((javax.swing.JTextArea)evt.getSource()).getName();
            ((javax.swing.JTextArea)evt.getSource()).select(
                ((javax.swing.JTextArea)evt.getSource()).getText().length(),
                ((javax.swing.JTextArea)evt.getSource()).getText().length());
        }

        String msg = "";

        switch ( name ) {
            case "cityField":
            msg = "<html>City in which the broker is located. "
            + "This field is <em>optional</em>.";
            break;
            case "companyField":
            msg = "<html>Company name for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "streetField":
            msg = "<html>Street address for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "suiteField":
            msg = "<html>Suite number for this broker, if any"
            + ". This field is <em>optional</em>.";
            break;
            case "stateField":
            msg = "<html>Postal abbreviation for the state or "
            + "province in which this broker is located. This field "
            + "is <em>optional</em>. However, if this field <strong>is"
            + "</strong> used, the State <strong>must</strong> be a "
            + "valid US State or Canadian Province abbreviation.";
            break;
            case "zipField":
            msg = "<html>Postal (Zip) Code for this broker. This field is "
            + "<em>optional</em> However, if this field <strong>is"
            + "</strong> used, the Zip Code <strong>must</strong> "
            + "be a valid US or Canadian Postal Code.";
            break;
            case "nameField":
            msg = "<html>Name of contact at this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "phoneField":
            msg = "<html>Phone number for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "faxField":
            msg = "<html>Fax number for this broker. This "
            + "field is <em>optional</em>.";
            break;
            case "emailField":
            msg = "<html>Email address for this broker. This field is "
            + "<em>optional</em>.";
            default:
            msg = "";
            break;
        }

        if ( msg != null && !msg.isBlank() && !msg.isEmpty() ) {
            helpPanel.setBackground(tip);
            helpLabel.setForeground(tipText);
            helpLabel.setText(msg);
        } else {
            helpPanel.setBackground(ctl);
            helpLabel.setForeground(fore);
            helpLabel.setText("");
        }
    }//GEN-LAST:event_streetFielddoSelection

    private void streetFieldvalidateData(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_streetFieldvalidateData
        // Deselect the text in the field.
        if ( evt.getSource() instanceof JTextField )
        ((JTextField)evt.getSource()).select(0, 0);

        saveButton.setEnabled(isOneContactMethodPresent() && isOneNamePresent());
    }//GEN-LAST:event_streetFieldvalidateData

    private void streetFieldcheckEnterEscape(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_streetFieldcheckEnterEscape
        // Check to see if the enter or escape key was pressed.
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
        doSave();
        else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
        doCancel();
    }//GEN-LAST:event_streetFieldcheckEnterEscape

    private void companyFielddoSelection(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_companyFielddoSelection
        String name = "";

        if ( evt.getSource() instanceof javax.swing.JTextField ) {
            name = ((javax.swing.JTextField)evt.getSource()).getName();
            ((javax.swing.JTextField)evt.getSource()).selectAll();
        } else if ( evt.getSource() instanceof javax.swing.JTextArea ) {
            name = ((javax.swing.JTextArea)evt.getSource()).getName();
            ((javax.swing.JTextArea)evt.getSource()).select(
                ((javax.swing.JTextArea)evt.getSource()).getText().length(),
                ((javax.swing.JTextArea)evt.getSource()).getText().length());
        }

        String msg = "";

        switch ( name ) {
            case "cityField":
            msg = "<html>City in which the broker is located. "
            + "This field is <em>optional</em>.";
            break;
            case "companyField":
            msg = "<html>Company name for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "streetField":
            msg = "<html>Street address for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "suiteField":
            msg = "<html>Suite number for this broker, if any"
            + ". This field is <em>optional</em>.";
            break;
            case "stateField":
            msg = "<html>Postal abbreviation for the state or "
            + "province in which this broker is located. This field "
            + "is <em>optional</em>. However, if this field <strong>is"
            + "</strong> used, the State <strong>must</strong> be a "
            + "valid US State or Canadian Province abbreviation.";
            break;
            case "zipField":
            msg = "<html>Postal (Zip) Code for this broker. This field is "
            + "<em>optional</em> However, if this field <strong>is"
            + "</strong> used, the Zip Code <strong>must</strong> "
            + "be a valid US or Canadian Postal Code.";
            break;
            case "nameField":
            msg = "<html>Name of contact at this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "phoneField":
            msg = "<html>Phone number for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "faxField":
            msg = "<html>Fax number for this broker. This "
            + "field is <em>optional</em>.";
            break;
            case "emailField":
            msg = "<html>Email address for this broker. This field is "
            + "<em>optional</em>.";
            default:
            msg = "";
            break;
        }

        if ( msg != null && !msg.isBlank() && !msg.isEmpty() ) {
            helpPanel.setBackground(tip);
            helpLabel.setForeground(tipText);
            helpLabel.setText(msg);
        } else {
            helpPanel.setBackground(ctl);
            helpLabel.setForeground(fore);
            helpLabel.setText("");
        }
    }//GEN-LAST:event_companyFielddoSelection

    private void companyFieldvalidateData(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_companyFieldvalidateData
        // Deselect the text in the field.
        if ( evt.getSource() instanceof JTextField )
        ((JTextField)evt.getSource()).select(0, 0);

        saveButton.setEnabled(isOneContactMethodPresent() && isOneNamePresent());
    }//GEN-LAST:event_companyFieldvalidateData

    private void companyFieldcheckEnterEscape(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_companyFieldcheckEnterEscape
        // Check to see if the enter or escape key was pressed.
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
        doSave();
        else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
        doCancel();
    }//GEN-LAST:event_companyFieldcheckEnterEscape

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doCancel();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void cancelButtoncheckEnterEscape(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cancelButtoncheckEnterEscape
        // Check to see if the enter or escape key was pressed.
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
        doSave();
        else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
        doCancel();
    }//GEN-LAST:event_cancelButtoncheckEnterEscape

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        doSave();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void saveButtoncheckEnterEscape(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_saveButtoncheckEnterEscape
        // Check to see if the enter or escape key was pressed.
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
        doSave();
        else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
        doCancel();
    }//GEN-LAST:event_saveButtoncheckEnterEscape

    private void suiteFielddoSelection(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_suiteFielddoSelection
        String name = "";

        if ( evt.getSource() instanceof javax.swing.JTextField ) {
            name = ((javax.swing.JTextField)evt.getSource()).getName();
            ((javax.swing.JTextField)evt.getSource()).selectAll();
        } else if ( evt.getSource() instanceof javax.swing.JTextArea ) {
            name = ((javax.swing.JTextArea)evt.getSource()).getName();
            ((javax.swing.JTextArea)evt.getSource()).select(
                ((javax.swing.JTextArea)evt.getSource()).getText().length(),
                ((javax.swing.JTextArea)evt.getSource()).getText().length());
        }

        String msg = "";

        switch ( name ) {
            case "cityField":
            msg = "<html>City in which the broker is located. "
            + "This field is <em>optional</em>.";
            break;
            case "companyField":
            msg = "<html>Company name for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "streetField":
            msg = "<html>Street address for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "suiteField":
            msg = "<html>Suite number for this broker, if any"
            + ". This field is <em>optional</em>.";
            break;
            case "stateField":
            msg = "<html>Postal abbreviation for the state or "
            + "province in which this broker is located. This field "
            + "is <em>optional</em>. However, if this field <strong>is"
            + "</strong> used, the State <strong>must</strong> be a "
            + "valid US State or Canadian Province abbreviation.";
            break;
            case "zipField":
            msg = "<html>Postal (Zip) Code for this broker. This field is "
            + "<em>optional</em> However, if this field <strong>is"
            + "</strong> used, the Zip Code <strong>must</strong> "
            + "be a valid US or Canadian Postal Code.";
            break;
            case "nameField":
            msg = "<html>Name of contact at this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "phoneField":
            msg = "<html>Phone number for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "faxField":
            msg = "<html>Fax number for this broker. This "
            + "field is <em>optional</em>.";
            break;
            case "emailField":
            msg = "<html>Email address for this broker. This field is "
            + "<em>optional</em>.";
            default:
            msg = "";
            break;
        }

        if ( msg != null && !msg.isBlank() && !msg.isEmpty() ) {
            helpPanel.setBackground(tip);
            helpLabel.setForeground(tipText);
            helpLabel.setText(msg);
        } else {
            helpPanel.setBackground(ctl);
            helpLabel.setForeground(fore);
            helpLabel.setText("");
        }
    }//GEN-LAST:event_suiteFielddoSelection

    private void suiteFieldcheckEnterEscape(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_suiteFieldcheckEnterEscape
        // Check to see if the enter or escape key was pressed.
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
        doSave();
        else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
        doCancel();
    }//GEN-LAST:event_suiteFieldcheckEnterEscape

    private void cityFielddoSelection(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cityFielddoSelection
        String name = "";

        if ( evt.getSource() instanceof javax.swing.JTextField ) {
            name = ((javax.swing.JTextField)evt.getSource()).getName();
            ((javax.swing.JTextField)evt.getSource()).selectAll();
        } else if ( evt.getSource() instanceof javax.swing.JTextArea ) {
            name = ((javax.swing.JTextArea)evt.getSource()).getName();
            ((javax.swing.JTextArea)evt.getSource()).select(
                ((javax.swing.JTextArea)evt.getSource()).getText().length(),
                ((javax.swing.JTextArea)evt.getSource()).getText().length());
        }

        String msg = "";

        switch ( name ) {
            case "cityField":
            msg = "<html>City in which the broker is located. "
            + "This field is <em>optional</em>.";
            break;
            case "companyField":
            msg = "<html>Company name for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "streetField":
            msg = "<html>Street address for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "suiteField":
            msg = "<html>Suite number for this broker, if any"
            + ". This field is <em>optional</em>.";
            break;
            case "stateField":
            msg = "<html>Postal abbreviation for the state or "
            + "province in which this broker is located. This field "
            + "is <em>optional</em>. However, if this field <strong>is"
            + "</strong> used, the State <strong>must</strong> be a "
            + "valid US State or Canadian Province abbreviation.";
            break;
            case "zipField":
            msg = "<html>Postal (Zip) Code for this broker. This field is "
            + "<em>optional</em> However, if this field <strong>is"
            + "</strong> used, the Zip Code <strong>must</strong> "
            + "be a valid US or Canadian Postal Code.";
            break;
            case "nameField":
            msg = "<html>Name of contact at this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "phoneField":
            msg = "<html>Phone number for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "faxField":
            msg = "<html>Fax number for this broker. This "
            + "field is <em>optional</em>.";
            break;
            case "emailField":
            msg = "<html>Email address for this broker. This field is "
            + "<em>optional</em>.";
            default:
            msg = "";
            break;
        }

        if ( msg != null && !msg.isBlank() && !msg.isEmpty() ) {
            helpPanel.setBackground(tip);
            helpLabel.setForeground(tipText);
            helpLabel.setText(msg);
        } else {
            helpPanel.setBackground(ctl);
            helpLabel.setForeground(fore);
            helpLabel.setText("");
        }
    }//GEN-LAST:event_cityFielddoSelection

    private void cityFieldvalidateData(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cityFieldvalidateData
        // Deselect the text in the field.
        if ( evt.getSource() instanceof JTextField )
        ((JTextField)evt.getSource()).select(0, 0);

        saveButton.setEnabled(isOneContactMethodPresent() && isOneNamePresent());
    }//GEN-LAST:event_cityFieldvalidateData

    private void cityFieldcheckEnterEscape(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cityFieldcheckEnterEscape
        // Check to see if the enter or escape key was pressed.
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
        doSave();
        else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
        doCancel();
    }//GEN-LAST:event_cityFieldcheckEnterEscape

    private void nameFielddoSelection(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameFielddoSelection
        String name = "";

        if ( evt.getSource() instanceof javax.swing.JTextField ) {
            name = ((javax.swing.JTextField)evt.getSource()).getName();
            ((javax.swing.JTextField)evt.getSource()).selectAll();
        } else if ( evt.getSource() instanceof javax.swing.JTextArea ) {
            name = ((javax.swing.JTextArea)evt.getSource()).getName();
            ((javax.swing.JTextArea)evt.getSource()).select(
                ((javax.swing.JTextArea)evt.getSource()).getText().length(),
                ((javax.swing.JTextArea)evt.getSource()).getText().length());
        }

        String msg = "";

        switch ( name ) {
            case "cityField":
            msg = "<html>City in which the broker is located. "
            + "This field is <em>optional</em>.";
            break;
            case "companyField":
            msg = "<html>Company name for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "streetField":
            msg = "<html>Street address for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "suiteField":
            msg = "<html>Suite number for this broker, if any"
            + ". This field is <em>optional</em>.";
            break;
            case "stateField":
            msg = "<html>Postal abbreviation for the state or "
            + "province in which this broker is located. This field "
            + "is <em>optional</em>. However, if this field <strong>is"
            + "</strong> used, the State <strong>must</strong> be a "
            + "valid US State or Canadian Province abbreviation.";
            break;
            case "zipField":
            msg = "<html>Postal (Zip) Code for this broker. This field is "
            + "<em>optional</em> However, if this field <strong>is"
            + "</strong> used, the Zip Code <strong>must</strong> "
            + "be a valid US or Canadian Postal Code.";
            break;
            case "nameField":
            msg = "<html>Name of contact at this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "phoneField":
            msg = "<html>Phone number for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "faxField":
            msg = "<html>Fax number for this broker. This "
            + "field is <em>optional</em>.";
            break;
            case "emailField":
            msg = "<html>Email address for this broker. This field is "
            + "<em>optional</em>.";
            default:
            msg = "";
            break;
        }

        if ( msg != null && !msg.isBlank() && !msg.isEmpty() ) {
            helpPanel.setBackground(tip);
            helpLabel.setForeground(tipText);
            helpLabel.setText(msg);
        } else {
            helpPanel.setBackground(ctl);
            helpLabel.setForeground(fore);
            helpLabel.setText("");
        }
    }//GEN-LAST:event_nameFielddoSelection

    private void nameFieldvalidateData(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_nameFieldvalidateData
        // Deselect the text in the field.
        if ( evt.getSource() instanceof JTextField )
        ((JTextField)evt.getSource()).select(0, 0);

        saveButton.setEnabled(isOneContactMethodPresent() && isOneNamePresent());
    }//GEN-LAST:event_nameFieldvalidateData

    private void nameFieldcheckEnterEscape(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nameFieldcheckEnterEscape
        // Check to see if the enter or escape key was pressed.
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
        doSave();
        else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
        doCancel();
    }//GEN-LAST:event_nameFieldcheckEnterEscape

    private void phoneFielddoSelection(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_phoneFielddoSelection
        String name = "";

        if ( evt.getSource() instanceof javax.swing.JTextField ) {
            name = ((javax.swing.JTextField)evt.getSource()).getName();
            ((javax.swing.JTextField)evt.getSource()).selectAll();
        } else if ( evt.getSource() instanceof javax.swing.JTextArea ) {
            name = ((javax.swing.JTextArea)evt.getSource()).getName();
            ((javax.swing.JTextArea)evt.getSource()).select(
                ((javax.swing.JTextArea)evt.getSource()).getText().length(),
                ((javax.swing.JTextArea)evt.getSource()).getText().length());
        }

        String msg = "";

        switch ( name ) {
            case "cityField":
            msg = "<html>City in which the broker is located. "
            + "This field is <em>optional</em>.";
            break;
            case "companyField":
            msg = "<html>Company name for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "streetField":
            msg = "<html>Street address for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "suiteField":
            msg = "<html>Suite number for this broker, if any"
            + ". This field is <em>optional</em>.";
            break;
            case "stateField":
            msg = "<html>Postal abbreviation for the state or "
            + "province in which this broker is located. This field "
            + "is <em>optional</em>. However, if this field <strong>is"
            + "</strong> used, the State <strong>must</strong> be a "
            + "valid US State or Canadian Province abbreviation.";
            break;
            case "zipField":
            msg = "<html>Postal (Zip) Code for this broker. This field is "
            + "<em>optional</em> However, if this field <strong>is"
            + "</strong> used, the Zip Code <strong>must</strong> "
            + "be a valid US or Canadian Postal Code.";
            break;
            case "nameField":
            msg = "<html>Name of contact at this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "phoneField":
            msg = "<html>Phone number for this broker. This field is "
            + "<em>optional</em>.";
            break;
            case "faxField":
            msg = "<html>Fax number for this broker. This "
            + "field is <em>optional</em>.";
            break;
            case "emailField":
            msg = "<html>Email address for this broker. This field is "
            + "<em>optional</em>.";
            default:
            msg = "";
            break;
        }

        if ( msg != null && !msg.isBlank() && !msg.isEmpty() ) {
            helpPanel.setBackground(tip);
            helpLabel.setForeground(tipText);
            helpLabel.setText(msg);
        } else {
            helpPanel.setBackground(ctl);
            helpLabel.setForeground(fore);
            helpLabel.setText("");
        }
    }//GEN-LAST:event_phoneFielddoSelection

    private void phoneFieldvalidateData(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_phoneFieldvalidateData
        // Deselect the text in the field.
        if ( evt.getSource() instanceof JTextField )
        ((JTextField)evt.getSource()).select(0, 0);

        saveButton.setEnabled(isOneContactMethodPresent() && isOneNamePresent());
    }//GEN-LAST:event_phoneFieldvalidateData

    private void phoneFieldcheckEnterEscape(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_phoneFieldcheckEnterEscape
        // Check to see if the enter or escape key was pressed.
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
        doSave();
        else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
        doCancel();
    }//GEN-LAST:event_phoneFieldcheckEnterEscape

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
            java.util.logging.Logger.getLogger(NewBrokerDlg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewBrokerDlg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewBrokerDlg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewBrokerDlg.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                NewBrokerDlg dialog = new NewBrokerDlg(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField cityField;
    private javax.swing.JTextField companyField;
    private javax.swing.JPanel controlPanel;
    private javax.swing.JTextField emailField;
    private javax.swing.JFormattedTextField faxField;
    public static javax.swing.JLabel helpLabel;
    public static javax.swing.JPanel helpPanel;
    private javax.swing.JTextField idField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JTextField nameField;
    private javax.swing.JFormattedTextField phoneField;
    private javax.swing.JButton saveButton;
    private javax.swing.JTextField stateField;
    private javax.swing.JTextField streetField;
    private javax.swing.JTextField suiteField;
    private javax.swing.JTextField zipField;
    // End of variables declaration//GEN-END:variables
}
