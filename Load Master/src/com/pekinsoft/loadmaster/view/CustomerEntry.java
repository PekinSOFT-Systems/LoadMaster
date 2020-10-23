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
 *  Project    :   Load Master
 *  Class      :   CustomerEntry.java
 *  Author     :   Sean Carrick
 *  Created    :   Aug 29, 2020 @ 12:23:34 AM
 *  Modified   :   Aug 29, 2020
 *  
 *  Purpose:
 *  
 *  Revision History:
 *  
 *  WHEN          BY                  REASON
 *  ------------  ------------------- ------------------------------------------
 *  Aug 29, 2020  Sean Carrick        Initial creation.
 *  Sep 01, 2020  Sean Carrick        Added data validation to the dialog to
 *                                    ensure that all required fields are filled
 *                                    in and that all data entered is valid.
 * *****************************************************************************
 */
package com.pekinsoft.loadmaster.view;

import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.controller.CustomerCtl;
import com.pekinsoft.loadmaster.err.DataStoreException;
import com.pekinsoft.loadmaster.model.CustomerModel;
import com.pekinsoft.loadmaster.utils.MessageBox;
import com.pekinsoft.loadmaster.verifiers.PostalCodeVerifier;
import com.pekinsoft.loadmaster.verifiers.StateAbbrVerifier;
import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.LogRecord;
import java.util.logging.Level;
import javax.swing.JTextField;

/**
 *
 * @author Sean Carrick
 */
public class CustomerEntry extends javax.swing.JInternalFrame {
    private final Color errFore = Color.YELLOW;
    private final Color errBack = Color.RED;
    private final Color fore = SystemColor.textText;
    private final Color back = SystemColor.text;
    private final Color ctl = SystemColor.control;
    private final Color tip = SystemColor.info;
    private final Color tipText = SystemColor.infoText;
        
    private CustomerCtl records;
    private CustomerModel customer;
    private final LogRecord lr;
    
    /**
     * Creates new form Customers
     */
    public CustomerEntry() {
        lr = new LogRecord(Level.ALL, "Logging initialized for Customers.");
        lr.setSourceClassName(CustomerEntry.class.getName());
        lr.setSourceMethodName("Customers");
        Starter.logger.enter(lr);
        
        lr.setMessage("Attempting to access the customers database...");
        Starter.logger.debug(lr);
        try {
            records = new CustomerCtl();
            lr.setMessage("Customers database accessed successfully!");
        } catch ( DataStoreException ex ) {
            lr.setMessage("Something went wrong accessing the customers database.");
            lr.setThrown(ex);
            Starter.logger.error(lr);
            
            MessageBox.showError(ex, "Database Access");
            
            records = null;
        }
        
        initComponents();
        
        // Set up the input verifiers for the state and zip fields.
        stateField.setInputVerifier(new StateAbbrVerifier());
        zipField.setInputVerifier(new PostalCodeVerifier());
        
        setTitle(getTitle() + " (" + records.getRecordCount() + " Records)");
        
        // Set the accessible description for the required fields to "required"
        //+ for data validation purposes.
        companyField.getAccessibleContext().setAccessibleDescription("required");
        streetField.getAccessibleContext().setAccessibleDescription("required");
        cityField.getAccessibleContext().setAccessibleDescription("required");
        stateField.getAccessibleContext().setAccessibleDescription("required");
        zipField.getAccessibleContext().setAccessibleDescription("required");
        
        
        lr.setMessage("Customers dialog construction complete.");
        Starter.logger.exit(lr, null);
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

        helpPanel = new javax.swing.JPanel();
        helpLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        idField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        companyField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        streetField = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        suiteField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        cityField = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        stateField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        zipField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        contactField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        phoneField = new javax.swing.JFormattedTextField();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        notesField = new javax.swing.JTextArea();
        jPanel2 = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setIconifiable(true);
        setTitle("Customer Entry");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pekinsoft/loadmaster/res/people.png"))); // NOI18N

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

        jLabel1.setText("Customer ID:");

        idField.setEditable(false);
        idField.setFocusable(false);
        idField.setName("idField"); // NOI18N
        idField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
        });

        jLabel2.setText("<html><strong>Company:</strong>");

        companyField.setName("companyField"); // NOI18N
        companyField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                doSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                validateData(evt);
            }
        });
        companyField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
        });

        jLabel3.setText("<html><strong>Street:</strong>");

        streetField.setName("streetField"); // NOI18N
        streetField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                doSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                validateData(evt);
            }
        });
        streetField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
        });

        jLabel4.setText("Suite:");

        suiteField.setName("suiteField"); // NOI18N
        suiteField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                doSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                validateData(evt);
            }
        });
        suiteField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
        });

        jLabel5.setText("<html><strong>City:</strong>");

        cityField.setName("cityField"); // NOI18N
        cityField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                doSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                validateData(evt);
            }
        });
        cityField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
        });

        jLabel6.setText("<html><strong>State:</strong>");

        stateField.setName("stateField"); // NOI18N
        stateField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                doSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                validateData(evt);
            }
        });
        stateField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
        });

        jLabel7.setText("<html><strong>Zip Code:</strong>");

        zipField.setName("zipField"); // NOI18N
        zipField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                doSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                validateData(evt);
            }
        });
        zipField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
        });

        jLabel8.setText("Contact:");

        contactField.setName("contactField"); // NOI18N
        contactField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                doSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                validateData(evt);
            }
        });
        contactField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
        });

        jLabel9.setText("Phone:");

        try {
            phoneField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(###) ###-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        phoneField.setName("phoneField"); // NOI18N
        phoneField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                doSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                validateData(evt);
            }
        });
        phoneField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
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
                doSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                validateData(evt);
            }
        });
        notesField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
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
                .addComponent(jScrollPane1)
                .addContainerGap())
        );

        cancelButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pekinsoft/loadmaster/res/cancel16.png"))); // NOI18N
        cancelButton.setMnemonic('C');
        cancelButton.setText("Cancel");
        cancelButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                doSelection(evt);
            }
        });
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        cancelButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
        });

        saveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pekinsoft/loadmaster/res/ok.png"))); // NOI18N
        saveButton.setMnemonic('S');
        saveButton.setText("Save");
        saveButton.setEnabled(false);
        saveButton.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                doSelection(evt);
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

    private void checkEnterEscape(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_checkEnterEscape
        // Check to see if the enter or escape key was pressed.
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER ) 
            doSave();
        else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ) 
            doCancel();
    }//GEN-LAST:event_checkEnterEscape

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        doCancel();
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        doSave();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void doSelection(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_doSelection
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
    }//GEN-LAST:event_doSelection

    private void validateData(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_validateData
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
    }//GEN-LAST:event_validateData


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
