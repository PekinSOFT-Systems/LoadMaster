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
 *  Class      :   NewCustomerDlg.java
 *  Author     :   Sean Carrick
 *  Created    :   Sep 14, 2020 @ 1:52:27 PM
 *  Modified   :   Sep 14, 2020
 *  
 *  Purpose:
 *  
 *  Revision History:
 *  
 *  WHEN          BY                  REASON
 *  ------------  ------------------- ------------------------------------------
 *  Sep 14, 2020  Sean Carrick        Initial creation.
 *  Oct 09, 2020  Sean Carrick        Removed the main() method from this class.
 * *****************************************************************************
 */
package com.pekinsoft.loadmaster.view;

import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.controller.BrokerCtl;
import com.pekinsoft.loadmaster.controller.CustomerCtl;
import com.pekinsoft.loadmaster.err.DataStoreException;
import com.pekinsoft.loadmaster.model.CustomerModel;
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
public class NewCustomerDlg extends javax.swing.JDialog {
    private final Color errFore = Color.YELLOW;
    private final Color errBack = Color.RED;
    private final Color fore = SystemColor.textText;
    private final Color back = SystemColor.text;
    private final Color ctl = SystemColor.control;
    private final Color tip = SystemColor.info;
    private final Color tipText = SystemColor.infoText;
        
    private CustomerCtl records;
    private CustomerModel customer;
    private boolean cancelled;
    private final LogRecord lr;

    /**
     * Creates new form NewCustomerDlg
     */
    public NewCustomerDlg(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        lr = new LogRecord(Level.ALL, "Creating the New Customer dialog...");
        lr.setSourceClassName(this.getClass().getName());
        lr.setSourceMethodName("NewBrokerDlg (constructor)");
        lr.setParameters(new Object[]{parent, modal});
        Starter.logger.enter(lr);
        
        initComponents();
        
        setTitle("New Customer Entry");
        
        int left = Toolkit.getDefaultToolkit().getScreenSize().width;
        int top = Toolkit.getDefaultToolkit().getScreenSize().height;
        
        left = (left - getWidth()) / 2;
        top = (top - getHeight()) / 2;
        setLocation(left, top);
        
        try {
            records = new CustomerCtl();
        } catch ( DataStoreException ex ) {
            lr.setMessage("Something went wrong accessing the customers table.");
            lr.setThrown(ex);
            Starter.logger.error(lr);
            
            MessageBox.showError(ex, "Data Store Error");
        }
        
        cancelled = false;
    }
    
    public boolean isCancelled() {
        return cancelled;
    }
    
    public CustomerModel getCustomer() {
        return customer;
    }
    
    private void doSave() {
        lr.setSourceMethodName("doSave");
        lr.setMessage("Saving the new customer record.");
        Starter.logger.enter(lr);
        
        companyField.requestFocus();

        customer = new CustomerModel();
        idField.setText(String.valueOf(customer.getId()));

        // Perform data save here.
        customer.setCity(cityField.getText());
        customer.setComments(notesField.getText());
        customer.setCompany(companyField.getText());
        customer.setContact(contactField.getText());
        customer.setId(Long.valueOf(idField.getText()));
        customer.setPhone(phoneField.getText());
        customer.setState(stateField.getText());
        customer.setStreet(streetField.getText());
        customer.setSuite(suiteField.getText());
        customer.setZip(zipField.getText());

        records.addNew(customer);

        lr.setMessage("Attempting to save the data to file.");
        Starter.logger.debug(lr);
        try {
            records.close();
            lr.setMessage("Save to file was successful!");
            Starter.logger.info(lr);
            
            setTitle("Customer Entry (" + records.getRecordCount() + " Records)");
        } catch (DataStoreException ex) {
            lr.setMessage("Something went wrong accessing the customers database.");
            lr.setThrown(ex);
            Starter.logger.error(lr);

            MessageBox.showError(ex, "Database Access");

            records = null;
        }

        lr.setMessage("Checking to see if user would like to enter another customer...");
        Starter.logger.info(lr);

        int choice = MessageBox.askQuestion("Would you like to add another "
                + "customer?", "Add Another", false);

        if (choice == MessageBox.NO_OPTION) {
            lr.setMessage("No more customers being added. Exiting the function "
                    + "and closing the window.");
            Starter.logger.exit(lr, null);

            lr.setMessage("Exiting doSave to close the window.");
            Starter.logger.exit(lr, null);
            doCancel();
        } else {
            lr.setMessage("User wants to add another customer. Clearing the form.");
            Starter.logger.exit(lr, null);
            doClear();
        }
    }
    
    private void doCancel() {
        lr.setSourceMethodName("doCancel");
        lr.setMessage("Entering the form closing function.");
        Starter.logger.enter(lr);
        
        LoadMaster.fileProgress.setValue(0);
        
        lr.setMessage("Closing the window.");
        Starter.logger.exit(lr, null);
        dispose();
    }
    
    private void doClear() {
        customer = new CustomerModel();
        idField.setText(String.valueOf(customer.getId()));
        companyField.setText("");
        streetField.setText("");
        suiteField.setText("");
        cityField.setText("");
        stateField.setText("");
        zipField.setText("");
        contactField.setText("");
        phoneField.setText("");
        notesField.setText("");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel8 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        contactField = new javax.swing.JTextField();
        streetField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        phoneField = new javax.swing.JFormattedTextField();
        suiteField = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        notesField = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        helpPanel = new javax.swing.JPanel();
        helpLabel = new javax.swing.JLabel();
        cityField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        stateField = new javax.swing.JTextField();
        idField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        zipField = new javax.swing.JTextField();
        companyField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel8.setText("Contact:");

        jLabel3.setText("<html><strong>Street:</strong>");

        contactField.setName("contactField"); // NOI18N
        contactField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                contactFielddoSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                contactFieldvalidateData(evt);
            }
        });
        contactField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                contactFieldcheckEnterEscape(evt);
            }
        });

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

        jLabel9.setText("Phone:");

        jLabel4.setText("Suite:");

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

        suiteField.setName("suiteField"); // NOI18N
        suiteField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                suiteFielddoSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                suiteFieldvalidateData(evt);
            }
        });
        suiteField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                suiteFieldcheckEnterEscape(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Notes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("sansserif", 0, 12))); // NOI18N

        notesField.setColumns(20);
        notesField.setLineWrap(true);
        notesField.setRows(5);
        notesField.setWrapStyleWord(true);
        notesField.setName("notesField"); // NOI18N
        notesField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                notesFielddoSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                notesFieldvalidateData(evt);
            }
        });
        notesField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                notesFieldcheckEnterEscape(evt);
            }
        });
        jScrollPane1.setViewportView(notesField);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 535, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel5.setText("<html><strong>City:</strong>");

        javax.swing.GroupLayout helpPanelLayout = new javax.swing.GroupLayout(helpPanel);
        helpPanel.setLayout(helpPanelLayout);
        helpPanelLayout.setHorizontalGroup(
            helpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(helpPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(helpLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 374, Short.MAX_VALUE)
                .addContainerGap())
        );
        helpPanelLayout.setVerticalGroup(
            helpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(helpPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(helpLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                .addContainerGap())
        );

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

        jLabel6.setText("<html><strong>State:</strong>");

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pekinsoft/loadmaster/res/cancel16.png"))); // NOI18N
        cancelButton.setMnemonic('C');
        cancelButton.setText("Cancel");
        cancelButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                cancelButtondoSelection(evt);
            }
        });
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

        saveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pekinsoft/loadmaster/res/ok.png"))); // NOI18N
        saveButton.setMnemonic('S');
        saveButton.setText("Save");
        saveButton.setEnabled(false);
        saveButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                saveButtondoSelection(evt);
            }
        });
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(saveButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelButton)
                    .addComponent(saveButton))
                .addContainerGap())
        );

        jLabel1.setText("Customer ID:");

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

        idField.setEditable(false);
        idField.setFocusable(false);
        idField.setName("idField"); // NOI18N
        idField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                idFieldcheckEnterEscape(evt);
            }
        });

        jLabel7.setText("<html><strong>Zip Code:</strong>");

        jLabel2.setText("<html><strong>Company:</strong>");

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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(contactField, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabel9)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(phoneField))
                                .addComponent(companyField, javax.swing.GroupLayout.PREFERRED_SIZE, 496, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(streetField)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(cityField)
                                            .addGap(18, 18, 18)
                                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(stateField, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(zipField)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabel4)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(suiteField, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(helpPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
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
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(companyField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(streetField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(suiteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cityField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(zipField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(contactField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(phoneField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(helpPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void contactFielddoSelection(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_contactFielddoSelection
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
            msg = "<html>City in which the customer is located. "
            + "This is a <strong>required</strong> field.";
            break;
            case "companyField":
            msg = "<html>Company name for this customer. This "
            + "is a <strong>required</strong> field.";
            break;
            case "streetField":
            msg = "<html>Street address for this customer. This"
            + " is a <strong>required</strong> field.";
            break;
            case "suiteField":
            msg = "<html>Suite number for this customer, if any"
            + ". This field is <em>optional</em>.";
            break;
            case "stateField":
            msg = "<html>Postal abbreviation for the state or "
            + "province in which this customer is located. This is "
            + "a <strong>required</strong> field.";
            break;
            case "zipField":
            msg = "<html>Postal (Zip) Code for this customer. This is a "
            + "<strong>required</strong> field.";
            break;
            case "contactField":
            msg = "<html>Name of contact at this customer. This field is "
            + "<em>optional</em>.";
            break;
            case "phoneField":
            msg = "<html>Phone number for this customer. This field is "
            + "<em>optional</em>.";
            break;
            case "notesField":
            msg = "<html>Any notes or comments about this customer. This "
            + "field is <em>optional</em>.";
            break;
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
    }//GEN-LAST:event_contactFielddoSelection

    private void contactFieldvalidateData(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_contactFieldvalidateData
        if ( evt.getSource() instanceof JTextField ) {
            if ( ((JTextField)evt.getSource()).getAccessibleContext()
                .getAccessibleDescription() != null ) {

                switch ( ((JTextField)evt.getSource()).getName() ) {
                    case "companyField":
                    case "streetField":
                    case "cityField":
                    case "stateField":
                    case "zipField":

                    if ( ((JTextField)evt.getSource()).getText().isBlank()
                        || ((JTextField)evt.getSource()).getText()
                        .isEmpty() ) {
                        ((JTextField)evt.getSource()).setForeground(errFore);
                        ((JTextField)evt.getSource()).setBackground(errBack);
                    } else {
                        ((JTextField)evt.getSource()).setForeground(fore);
                        ((JTextField)evt.getSource()).setBackground(back);
                    }

                    break;
                    default:
                    break;
                }
            } else {
                ((JTextField)evt.getSource()).setForeground(fore);
                ((JTextField)evt.getSource()).setBackground(back);
            }

            if ( companyField.getText().isBlank() || companyField.getText().isEmpty()
                && streetField.getText().isBlank() || streetField.getText().isEmpty()
                && cityField.getText().isBlank() || cityField.getText().isEmpty()
                && stateField.getText().isBlank() || stateField.getText().isEmpty()
                && zipField.getText().isBlank() || zipField.getText().isEmpty() )
            saveButton.setEnabled(false);
            else
            saveButton.setEnabled(true);
        }
    }//GEN-LAST:event_contactFieldvalidateData

    private void contactFieldcheckEnterEscape(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_contactFieldcheckEnterEscape
        // Check to see if the enter or escape key was pressed.
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
        doSave();
        else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
        doCancel();
    }//GEN-LAST:event_contactFieldcheckEnterEscape

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
            msg = "<html>City in which the customer is located. "
            + "This is a <strong>required</strong> field.";
            break;
            case "companyField":
            msg = "<html>Company name for this customer. This "
            + "is a <strong>required</strong> field.";
            break;
            case "streetField":
            msg = "<html>Street address for this customer. This"
            + " is a <strong>required</strong> field.";
            break;
            case "suiteField":
            msg = "<html>Suite number for this customer, if any"
            + ". This field is <em>optional</em>.";
            break;
            case "stateField":
            msg = "<html>Postal abbreviation for the state or "
            + "province in which this customer is located. This is "
            + "a <strong>required</strong> field.";
            break;
            case "zipField":
            msg = "<html>Postal (Zip) Code for this customer. This is a "
            + "<strong>required</strong> field.";
            break;
            case "contactField":
            msg = "<html>Name of contact at this customer. This field is "
            + "<em>optional</em>.";
            break;
            case "phoneField":
            msg = "<html>Phone number for this customer. This field is "
            + "<em>optional</em>.";
            break;
            case "notesField":
            msg = "<html>Any notes or comments about this customer. This "
            + "field is <em>optional</em>.";
            break;
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
        if ( evt.getSource() instanceof JTextField ) {
            if ( ((JTextField)evt.getSource()).getAccessibleContext()
                .getAccessibleDescription() != null ) {

                switch ( ((JTextField)evt.getSource()).getName() ) {
                    case "companyField":
                    case "streetField":
                    case "cityField":
                    case "stateField":
                    case "zipField":

                    if ( ((JTextField)evt.getSource()).getText().isBlank()
                        || ((JTextField)evt.getSource()).getText()
                        .isEmpty() ) {
                        ((JTextField)evt.getSource()).setForeground(errFore);
                        ((JTextField)evt.getSource()).setBackground(errBack);
                    } else {
                        ((JTextField)evt.getSource()).setForeground(fore);
                        ((JTextField)evt.getSource()).setBackground(back);
                    }

                    break;
                    default:
                    break;
                }
            } else {
                ((JTextField)evt.getSource()).setForeground(fore);
                ((JTextField)evt.getSource()).setBackground(back);
            }

            if ( companyField.getText().isBlank() || companyField.getText().isEmpty()
                && streetField.getText().isBlank() || streetField.getText().isEmpty()
                && cityField.getText().isBlank() || cityField.getText().isEmpty()
                && stateField.getText().isBlank() || stateField.getText().isEmpty()
                && zipField.getText().isBlank() || zipField.getText().isEmpty() )
            saveButton.setEnabled(false);
            else
            saveButton.setEnabled(true);
        }
    }//GEN-LAST:event_streetFieldvalidateData

    private void streetFieldcheckEnterEscape(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_streetFieldcheckEnterEscape
        // Check to see if the enter or escape key was pressed.
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
        doSave();
        else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
        doCancel();
    }//GEN-LAST:event_streetFieldcheckEnterEscape

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
            msg = "<html>City in which the customer is located. "
            + "This is a <strong>required</strong> field.";
            break;
            case "companyField":
            msg = "<html>Company name for this customer. This "
            + "is a <strong>required</strong> field.";
            break;
            case "streetField":
            msg = "<html>Street address for this customer. This"
            + " is a <strong>required</strong> field.";
            break;
            case "suiteField":
            msg = "<html>Suite number for this customer, if any"
            + ". This field is <em>optional</em>.";
            break;
            case "stateField":
            msg = "<html>Postal abbreviation for the state or "
            + "province in which this customer is located. This is "
            + "a <strong>required</strong> field.";
            break;
            case "zipField":
            msg = "<html>Postal (Zip) Code for this customer. This is a "
            + "<strong>required</strong> field.";
            break;
            case "contactField":
            msg = "<html>Name of contact at this customer. This field is "
            + "<em>optional</em>.";
            break;
            case "phoneField":
            msg = "<html>Phone number for this customer. This field is "
            + "<em>optional</em>.";
            break;
            case "notesField":
            msg = "<html>Any notes or comments about this customer. This "
            + "field is <em>optional</em>.";
            break;
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
        if ( evt.getSource() instanceof JTextField ) {
            if ( ((JTextField)evt.getSource()).getAccessibleContext()
                .getAccessibleDescription() != null ) {

                switch ( ((JTextField)evt.getSource()).getName() ) {
                    case "companyField":
                    case "streetField":
                    case "cityField":
                    case "stateField":
                    case "zipField":

                    if ( ((JTextField)evt.getSource()).getText().isBlank()
                        || ((JTextField)evt.getSource()).getText()
                        .isEmpty() ) {
                        ((JTextField)evt.getSource()).setForeground(errFore);
                        ((JTextField)evt.getSource()).setBackground(errBack);
                    } else {
                        ((JTextField)evt.getSource()).setForeground(fore);
                        ((JTextField)evt.getSource()).setBackground(back);
                    }

                    break;
                    default:
                    break;
                }
            } else {
                ((JTextField)evt.getSource()).setForeground(fore);
                ((JTextField)evt.getSource()).setBackground(back);
            }

            if ( companyField.getText().isBlank() || companyField.getText().isEmpty()
                && streetField.getText().isBlank() || streetField.getText().isEmpty()
                && cityField.getText().isBlank() || cityField.getText().isEmpty()
                && stateField.getText().isBlank() || stateField.getText().isEmpty()
                && zipField.getText().isBlank() || zipField.getText().isEmpty() )
            saveButton.setEnabled(false);
            else
            saveButton.setEnabled(true);
        }
    }//GEN-LAST:event_phoneFieldvalidateData

    private void phoneFieldcheckEnterEscape(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_phoneFieldcheckEnterEscape
        // Check to see if the enter or escape key was pressed.
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
        doSave();
        else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
        doCancel();
    }//GEN-LAST:event_phoneFieldcheckEnterEscape

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
            msg = "<html>City in which the customer is located. "
            + "This is a <strong>required</strong> field.";
            break;
            case "companyField":
            msg = "<html>Company name for this customer. This "
            + "is a <strong>required</strong> field.";
            break;
            case "streetField":
            msg = "<html>Street address for this customer. This"
            + " is a <strong>required</strong> field.";
            break;
            case "suiteField":
            msg = "<html>Suite number for this customer, if any"
            + ". This field is <em>optional</em>.";
            break;
            case "stateField":
            msg = "<html>Postal abbreviation for the state or "
            + "province in which this customer is located. This is "
            + "a <strong>required</strong> field.";
            break;
            case "zipField":
            msg = "<html>Postal (Zip) Code for this customer. This is a "
            + "<strong>required</strong> field.";
            break;
            case "contactField":
            msg = "<html>Name of contact at this customer. This field is "
            + "<em>optional</em>.";
            break;
            case "phoneField":
            msg = "<html>Phone number for this customer. This field is "
            + "<em>optional</em>.";
            break;
            case "notesField":
            msg = "<html>Any notes or comments about this customer. This "
            + "field is <em>optional</em>.";
            break;
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

    private void suiteFieldvalidateData(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_suiteFieldvalidateData
        if ( evt.getSource() instanceof JTextField ) {
            if ( ((JTextField)evt.getSource()).getAccessibleContext()
                .getAccessibleDescription() != null ) {

                switch ( ((JTextField)evt.getSource()).getName() ) {
                    case "companyField":
                    case "streetField":
                    case "cityField":
                    case "stateField":
                    case "zipField":

                    if ( ((JTextField)evt.getSource()).getText().isBlank()
                        || ((JTextField)evt.getSource()).getText()
                        .isEmpty() ) {
                        ((JTextField)evt.getSource()).setForeground(errFore);
                        ((JTextField)evt.getSource()).setBackground(errBack);
                    } else {
                        ((JTextField)evt.getSource()).setForeground(fore);
                        ((JTextField)evt.getSource()).setBackground(back);
                    }

                    break;
                    default:
                    break;
                }
            } else {
                ((JTextField)evt.getSource()).setForeground(fore);
                ((JTextField)evt.getSource()).setBackground(back);
            }

            if ( companyField.getText().isBlank() || companyField.getText().isEmpty()
                && streetField.getText().isBlank() || streetField.getText().isEmpty()
                && cityField.getText().isBlank() || cityField.getText().isEmpty()
                && stateField.getText().isBlank() || stateField.getText().isEmpty()
                && zipField.getText().isBlank() || zipField.getText().isEmpty() )
            saveButton.setEnabled(false);
            else
            saveButton.setEnabled(true);
        }
    }//GEN-LAST:event_suiteFieldvalidateData

    private void suiteFieldcheckEnterEscape(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_suiteFieldcheckEnterEscape
        // Check to see if the enter or escape key was pressed.
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
        doSave();
        else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
        doCancel();
    }//GEN-LAST:event_suiteFieldcheckEnterEscape

    private void notesFielddoSelection(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_notesFielddoSelection
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
            msg = "<html>City in which the customer is located. "
            + "This is a <strong>required</strong> field.";
            break;
            case "companyField":
            msg = "<html>Company name for this customer. This "
            + "is a <strong>required</strong> field.";
            break;
            case "streetField":
            msg = "<html>Street address for this customer. This"
            + " is a <strong>required</strong> field.";
            break;
            case "suiteField":
            msg = "<html>Suite number for this customer, if any"
            + ". This field is <em>optional</em>.";
            break;
            case "stateField":
            msg = "<html>Postal abbreviation for the state or "
            + "province in which this customer is located. This is "
            + "a <strong>required</strong> field.";
            break;
            case "zipField":
            msg = "<html>Postal (Zip) Code for this customer. This is a "
            + "<strong>required</strong> field.";
            break;
            case "contactField":
            msg = "<html>Name of contact at this customer. This field is "
            + "<em>optional</em>.";
            break;
            case "phoneField":
            msg = "<html>Phone number for this customer. This field is "
            + "<em>optional</em>.";
            break;
            case "notesField":
            msg = "<html>Any notes or comments about this customer. This "
            + "field is <em>optional</em>.";
            break;
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
    }//GEN-LAST:event_notesFielddoSelection

    private void notesFieldvalidateData(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_notesFieldvalidateData
        if ( evt.getSource() instanceof JTextField ) {
            if ( ((JTextField)evt.getSource()).getAccessibleContext()
                .getAccessibleDescription() != null ) {

                switch ( ((JTextField)evt.getSource()).getName() ) {
                    case "companyField":
                    case "streetField":
                    case "cityField":
                    case "stateField":
                    case "zipField":

                    if ( ((JTextField)evt.getSource()).getText().isBlank()
                        || ((JTextField)evt.getSource()).getText()
                        .isEmpty() ) {
                        ((JTextField)evt.getSource()).setForeground(errFore);
                        ((JTextField)evt.getSource()).setBackground(errBack);
                    } else {
                        ((JTextField)evt.getSource()).setForeground(fore);
                        ((JTextField)evt.getSource()).setBackground(back);
                    }

                    break;
                    default:
                    break;
                }
            } else {
                ((JTextField)evt.getSource()).setForeground(fore);
                ((JTextField)evt.getSource()).setBackground(back);
            }

            if ( companyField.getText().isBlank() || companyField.getText().isEmpty()
                && streetField.getText().isBlank() || streetField.getText().isEmpty()
                && cityField.getText().isBlank() || cityField.getText().isEmpty()
                && stateField.getText().isBlank() || stateField.getText().isEmpty()
                && zipField.getText().isBlank() || zipField.getText().isEmpty() )
            saveButton.setEnabled(false);
            else
            saveButton.setEnabled(true);
        }
    }//GEN-LAST:event_notesFieldvalidateData

    private void notesFieldcheckEnterEscape(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_notesFieldcheckEnterEscape
        // Check to see if the enter or escape key was pressed.
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
        doSave();
        else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
        doCancel();
    }//GEN-LAST:event_notesFieldcheckEnterEscape

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
            msg = "<html>City in which the customer is located. "
            + "This is a <strong>required</strong> field.";
            break;
            case "companyField":
            msg = "<html>Company name for this customer. This "
            + "is a <strong>required</strong> field.";
            break;
            case "streetField":
            msg = "<html>Street address for this customer. This"
            + " is a <strong>required</strong> field.";
            break;
            case "suiteField":
            msg = "<html>Suite number for this customer, if any"
            + ". This field is <em>optional</em>.";
            break;
            case "stateField":
            msg = "<html>Postal abbreviation for the state or "
            + "province in which this customer is located. This is "
            + "a <strong>required</strong> field.";
            break;
            case "zipField":
            msg = "<html>Postal (Zip) Code for this customer. This is a "
            + "<strong>required</strong> field.";
            break;
            case "contactField":
            msg = "<html>Name of contact at this customer. This field is "
            + "<em>optional</em>.";
            break;
            case "phoneField":
            msg = "<html>Phone number for this customer. This field is "
            + "<em>optional</em>.";
            break;
            case "notesField":
            msg = "<html>Any notes or comments about this customer. This "
            + "field is <em>optional</em>.";
            break;
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
        if ( evt.getSource() instanceof JTextField ) {
            if ( ((JTextField)evt.getSource()).getAccessibleContext()
                .getAccessibleDescription() != null ) {

                switch ( ((JTextField)evt.getSource()).getName() ) {
                    case "companyField":
                    case "streetField":
                    case "cityField":
                    case "stateField":
                    case "zipField":

                    if ( ((JTextField)evt.getSource()).getText().isBlank()
                        || ((JTextField)evt.getSource()).getText()
                        .isEmpty() ) {
                        ((JTextField)evt.getSource()).setForeground(errFore);
                        ((JTextField)evt.getSource()).setBackground(errBack);
                    } else {
                        ((JTextField)evt.getSource()).setForeground(fore);
                        ((JTextField)evt.getSource()).setBackground(back);
                    }

                    break;
                    default:
                    break;
                }
            } else {
                ((JTextField)evt.getSource()).setForeground(fore);
                ((JTextField)evt.getSource()).setBackground(back);
            }

            if ( companyField.getText().isBlank() || companyField.getText().isEmpty()
                && streetField.getText().isBlank() || streetField.getText().isEmpty()
                && cityField.getText().isBlank() || cityField.getText().isEmpty()
                && stateField.getText().isBlank() || stateField.getText().isEmpty()
                && zipField.getText().isBlank() || zipField.getText().isEmpty() )
            saveButton.setEnabled(false);
            else
            saveButton.setEnabled(true);
        }
    }//GEN-LAST:event_cityFieldvalidateData

    private void cityFieldcheckEnterEscape(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cityFieldcheckEnterEscape
        // Check to see if the enter or escape key was pressed.
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
        doSave();
        else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
        doCancel();
    }//GEN-LAST:event_cityFieldcheckEnterEscape

    private void cancelButtondoSelection(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cancelButtondoSelection
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
            msg = "<html>City in which the customer is located. "
            + "This is a <strong>required</strong> field.";
            break;
            case "companyField":
            msg = "<html>Company name for this customer. This "
            + "is a <strong>required</strong> field.";
            break;
            case "streetField":
            msg = "<html>Street address for this customer. This"
            + " is a <strong>required</strong> field.";
            break;
            case "suiteField":
            msg = "<html>Suite number for this customer, if any"
            + ". This field is <em>optional</em>.";
            break;
            case "stateField":
            msg = "<html>Postal abbreviation for the state or "
            + "province in which this customer is located. This is "
            + "a <strong>required</strong> field.";
            break;
            case "zipField":
            msg = "<html>Postal (Zip) Code for this customer. This is a "
            + "<strong>required</strong> field.";
            break;
            case "contactField":
            msg = "<html>Name of contact at this customer. This field is "
            + "<em>optional</em>.";
            break;
            case "phoneField":
            msg = "<html>Phone number for this customer. This field is "
            + "<em>optional</em>.";
            break;
            case "notesField":
            msg = "<html>Any notes or comments about this customer. This "
            + "field is <em>optional</em>.";
            break;
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
    }//GEN-LAST:event_cancelButtondoSelection

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

    private void saveButtondoSelection(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_saveButtondoSelection
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
            msg = "<html>City in which the customer is located. "
            + "This is a <strong>required</strong> field.";
            break;
            case "companyField":
            msg = "<html>Company name for this customer. This "
            + "is a <strong>required</strong> field.";
            break;
            case "streetField":
            msg = "<html>Street address for this customer. This"
            + " is a <strong>required</strong> field.";
            break;
            case "suiteField":
            msg = "<html>Suite number for this customer, if any"
            + ". This field is <em>optional</em>.";
            break;
            case "stateField":
            msg = "<html>Postal abbreviation for the state or "
            + "province in which this customer is located. This is "
            + "a <strong>required</strong> field.";
            break;
            case "zipField":
            msg = "<html>Postal (Zip) Code for this customer. This is a "
            + "<strong>required</strong> field.";
            break;
            case "contactField":
            msg = "<html>Name of contact at this customer. This field is "
            + "<em>optional</em>.";
            break;
            case "phoneField":
            msg = "<html>Phone number for this customer. This field is "
            + "<em>optional</em>.";
            break;
            case "notesField":
            msg = "<html>Any notes or comments about this customer. This "
            + "field is <em>optional</em>.";
            break;
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
    }//GEN-LAST:event_saveButtondoSelection

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        doSave();
    }//GEN-LAST:event_saveButtonActionPerformed

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
            msg = "<html>City in which the customer is located. "
            + "This is a <strong>required</strong> field.";
            break;
            case "companyField":
            msg = "<html>Company name for this customer. This "
            + "is a <strong>required</strong> field.";
            break;
            case "streetField":
            msg = "<html>Street address for this customer. This"
            + " is a <strong>required</strong> field.";
            break;
            case "suiteField":
            msg = "<html>Suite number for this customer, if any"
            + ". This field is <em>optional</em>.";
            break;
            case "stateField":
            msg = "<html>Postal abbreviation for the state or "
            + "province in which this customer is located. This is "
            + "a <strong>required</strong> field.";
            break;
            case "zipField":
            msg = "<html>Postal (Zip) Code for this customer. This is a "
            + "<strong>required</strong> field.";
            break;
            case "contactField":
            msg = "<html>Name of contact at this customer. This field is "
            + "<em>optional</em>.";
            break;
            case "phoneField":
            msg = "<html>Phone number for this customer. This field is "
            + "<em>optional</em>.";
            break;
            case "notesField":
            msg = "<html>Any notes or comments about this customer. This "
            + "field is <em>optional</em>.";
            break;
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
        if ( evt.getSource() instanceof JTextField ) {
            if ( ((JTextField)evt.getSource()).getAccessibleContext()
                .getAccessibleDescription() != null ) {

                switch ( ((JTextField)evt.getSource()).getName() ) {
                    case "companyField":
                    case "streetField":
                    case "cityField":
                    case "stateField":
                    case "zipField":

                    if ( ((JTextField)evt.getSource()).getText().isBlank()
                        || ((JTextField)evt.getSource()).getText()
                        .isEmpty() ) {
                        ((JTextField)evt.getSource()).setForeground(errFore);
                        ((JTextField)evt.getSource()).setBackground(errBack);
                    } else {
                        ((JTextField)evt.getSource()).setForeground(fore);
                        ((JTextField)evt.getSource()).setBackground(back);
                    }

                    break;
                    default:
                    break;
                }
            } else {
                ((JTextField)evt.getSource()).setForeground(fore);
                ((JTextField)evt.getSource()).setBackground(back);
            }

            if ( companyField.getText().isBlank() || companyField.getText().isEmpty()
                && streetField.getText().isBlank() || streetField.getText().isEmpty()
                && cityField.getText().isBlank() || cityField.getText().isEmpty()
                && stateField.getText().isBlank() || stateField.getText().isEmpty()
                && zipField.getText().isBlank() || zipField.getText().isEmpty() )
            saveButton.setEnabled(false);
            else
            saveButton.setEnabled(true);
        }
    }//GEN-LAST:event_stateFieldvalidateData

    private void stateFieldcheckEnterEscape(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_stateFieldcheckEnterEscape
        // Check to see if the enter or escape key was pressed.
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
        doSave();
        else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
        doCancel();
    }//GEN-LAST:event_stateFieldcheckEnterEscape

    private void idFieldcheckEnterEscape(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_idFieldcheckEnterEscape
        // Check to see if the enter or escape key was pressed.
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
        doSave();
        else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
        doCancel();
    }//GEN-LAST:event_idFieldcheckEnterEscape

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
            msg = "<html>City in which the customer is located. "
            + "This is a <strong>required</strong> field.";
            break;
            case "companyField":
            msg = "<html>Company name for this customer. This "
            + "is a <strong>required</strong> field.";
            break;
            case "streetField":
            msg = "<html>Street address for this customer. This"
            + " is a <strong>required</strong> field.";
            break;
            case "suiteField":
            msg = "<html>Suite number for this customer, if any"
            + ". This field is <em>optional</em>.";
            break;
            case "stateField":
            msg = "<html>Postal abbreviation for the state or "
            + "province in which this customer is located. This is "
            + "a <strong>required</strong> field.";
            break;
            case "zipField":
            msg = "<html>Postal (Zip) Code for this customer. This is a "
            + "<strong>required</strong> field.";
            break;
            case "contactField":
            msg = "<html>Name of contact at this customer. This field is "
            + "<em>optional</em>.";
            break;
            case "phoneField":
            msg = "<html>Phone number for this customer. This field is "
            + "<em>optional</em>.";
            break;
            case "notesField":
            msg = "<html>Any notes or comments about this customer. This "
            + "field is <em>optional</em>.";
            break;
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
        if ( evt.getSource() instanceof JTextField ) {
            if ( ((JTextField)evt.getSource()).getAccessibleContext()
                .getAccessibleDescription() != null ) {

                switch ( ((JTextField)evt.getSource()).getName() ) {
                    case "companyField":
                    case "streetField":
                    case "cityField":
                    case "stateField":
                    case "zipField":

                    if ( ((JTextField)evt.getSource()).getText().isBlank()
                        || ((JTextField)evt.getSource()).getText()
                        .isEmpty() ) {
                        ((JTextField)evt.getSource()).setForeground(errFore);
                        ((JTextField)evt.getSource()).setBackground(errBack);
                    } else {
                        ((JTextField)evt.getSource()).setForeground(fore);
                        ((JTextField)evt.getSource()).setBackground(back);
                    }

                    break;
                    default:
                    break;
                }
            } else {
                ((JTextField)evt.getSource()).setForeground(fore);
                ((JTextField)evt.getSource()).setBackground(back);
            }

            if ( companyField.getText().isBlank() || companyField.getText().isEmpty()
                && streetField.getText().isBlank() || streetField.getText().isEmpty()
                && cityField.getText().isBlank() || cityField.getText().isEmpty()
                && stateField.getText().isBlank() || stateField.getText().isEmpty()
                && zipField.getText().isBlank() || zipField.getText().isEmpty() )
            saveButton.setEnabled(false);
            else
            saveButton.setEnabled(true);
        }
    }//GEN-LAST:event_zipFieldvalidateData

    private void zipFieldcheckEnterEscape(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_zipFieldcheckEnterEscape
        // Check to see if the enter or escape key was pressed.
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
        doSave();
        else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
        doCancel();
    }//GEN-LAST:event_zipFieldcheckEnterEscape

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
            msg = "<html>City in which the customer is located. "
            + "This is a <strong>required</strong> field.";
            break;
            case "companyField":
            msg = "<html>Company name for this customer. This "
            + "is a <strong>required</strong> field.";
            break;
            case "streetField":
            msg = "<html>Street address for this customer. This"
            + " is a <strong>required</strong> field.";
            break;
            case "suiteField":
            msg = "<html>Suite number for this customer, if any"
            + ". This field is <em>optional</em>.";
            break;
            case "stateField":
            msg = "<html>Postal abbreviation for the state or "
            + "province in which this customer is located. This is "
            + "a <strong>required</strong> field.";
            break;
            case "zipField":
            msg = "<html>Postal (Zip) Code for this customer. This is a "
            + "<strong>required</strong> field.";
            break;
            case "contactField":
            msg = "<html>Name of contact at this customer. This field is "
            + "<em>optional</em>.";
            break;
            case "phoneField":
            msg = "<html>Phone number for this customer. This field is "
            + "<em>optional</em>.";
            break;
            case "notesField":
            msg = "<html>Any notes or comments about this customer. This "
            + "field is <em>optional</em>.";
            break;
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
        if ( evt.getSource() instanceof JTextField ) {
            if ( ((JTextField)evt.getSource()).getAccessibleContext()
                .getAccessibleDescription() != null ) {

                switch ( ((JTextField)evt.getSource()).getName() ) {
                    case "companyField":
                    case "streetField":
                    case "cityField":
                    case "stateField":
                    case "zipField":

                    if ( ((JTextField)evt.getSource()).getText().isBlank()
                        || ((JTextField)evt.getSource()).getText()
                        .isEmpty() ) {
                        ((JTextField)evt.getSource()).setForeground(errFore);
                        ((JTextField)evt.getSource()).setBackground(errBack);
                    } else {
                        ((JTextField)evt.getSource()).setForeground(fore);
                        ((JTextField)evt.getSource()).setBackground(back);
                    }

                    break;
                    default:
                    break;
                }
            } else {
                ((JTextField)evt.getSource()).setForeground(fore);
                ((JTextField)evt.getSource()).setBackground(back);
            }

            if ( companyField.getText().isBlank() || companyField.getText().isEmpty()
                && streetField.getText().isBlank() || streetField.getText().isEmpty()
                && cityField.getText().isBlank() || cityField.getText().isEmpty()
                && stateField.getText().isBlank() || stateField.getText().isEmpty()
                && zipField.getText().isBlank() || zipField.getText().isEmpty() )
            saveButton.setEnabled(false);
            else
            saveButton.setEnabled(true);
        }
    }//GEN-LAST:event_companyFieldvalidateData

    private void companyFieldcheckEnterEscape(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_companyFieldcheckEnterEscape
        // Check to see if the enter or escape key was pressed.
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
        doSave();
        else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE )
        doCancel();
    }//GEN-LAST:event_companyFieldcheckEnterEscape

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton cancelButton;
    private javax.swing.JTextField cityField;
    private javax.swing.JTextField companyField;
    private javax.swing.JTextField contactField;
    public static javax.swing.JLabel helpLabel;
    public static javax.swing.JPanel helpPanel;
    private javax.swing.JTextField idField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea notesField;
    private javax.swing.JFormattedTextField phoneField;
    private javax.swing.JButton saveButton;
    private javax.swing.JTextField stateField;
    private javax.swing.JTextField streetField;
    private javax.swing.JTextField suiteField;
    private javax.swing.JTextField zipField;
    // End of variables declaration//GEN-END:variables
}
