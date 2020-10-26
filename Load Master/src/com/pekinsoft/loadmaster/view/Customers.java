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
 * Project    :   Load Master
 * Class      :   Customers.java
 * Author     :   Sean Carrick
 * Created    :   Aug 29, 2020 @ 12:23:34 AM
 * Modified   :   Aug 29, 2020
 *  
 * Purpose:
 *  
 * Revision History:
 *  
 * WHEN          BY                  REASON
 * ------------  ------------------- ------------------------------------------
 * Aug 29, 2020  Sean Carrick        Initial creation.
 * Sep 01, 2020  Sean Carrick        Added data validation to the dialog to
 *                                   ensure that all required fields are filled
 *                                   in and that all data entered is valid.
 * Oct 25, 2020  Sean Carrick        Set up I18N.
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
import java.awt.SystemColor;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.LogRecord;
import java.util.logging.Level;
import javax.swing.JTextField;

/**
 *
 * @author Sean Carrick
 */
public class Customers extends javax.swing.JInternalFrame {
    private final ResourceBundle bundle = ResourceBundle.getBundle(
            "MessagesBundle", Locale.getDefault());
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
    public Customers() {
        lr = new LogRecord(Level.ALL, "Logging initialized for Customers.");
        lr.setSourceClassName(Customers.class.getName());
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
            
            MessageBox.showError(ex, bundle.getString("mbDBErrorTitle"));
            
            records = null;
        }
        
        initComponents();
        
        // Set up the input verifiers for the state and zip fields.
        stateField.setInputVerifier(new StateAbbrVerifier());
        zipField.setInputVerifier(new PostalCodeVerifier());
        
        setTitle(bundle.getString("customersdlg.frameTitle") + " (" 
                + records.getRecordCount() 
                + " " + bundle.getString("customersdlg.recordCountLabel") + ")");
        
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
            records.storeData();
            lr.setMessage("Save to file was successful!");
            Starter.logger.info(lr);
            
            setTitle(bundle.getString("customersDlgTitle") + " (" 
                    + records.getRecordCount() 
                    + bundle.getString("recordCountLabel"));
        } catch (DataStoreException ex) {
            lr.setMessage("Something went wrong accessing the customers database.");
            lr.setThrown(ex);
            Starter.logger.error(lr);

            MessageBox.showError(ex, bundle.getString("mbDBErrorTitle"));

            records = null;
        }

        lr.setMessage("Checking to see if user would like to enter another "
                + "customer...");
        Starter.logger.info(lr);

        int choice = MessageBox.askQuestion(bundle.getString(
                "customersdlg.mbAskQuestionText"), bundle.getString(
                        "customersdlg.mbAskQuestionTitle"), false);

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
        idLabel = new javax.swing.JLabel();
        idField = new javax.swing.JTextField();
        companyLabel = new javax.swing.JLabel();
        companyField = new javax.swing.JTextField();
        streetLabel = new javax.swing.JLabel();
        streetField = new javax.swing.JTextField();
        suiteLabel = new javax.swing.JLabel();
        suiteField = new javax.swing.JTextField();
        cityLabel = new javax.swing.JLabel();
        cityField = new javax.swing.JTextField();
        stateLabel = new javax.swing.JLabel();
        stateField = new javax.swing.JTextField();
        zipLabel = new javax.swing.JLabel();
        zipField = new javax.swing.JTextField();
        contactLabel = new javax.swing.JLabel();
        contactField = new javax.swing.JTextField();
        phoneLabel = new javax.swing.JLabel();
        phoneField = new javax.swing.JFormattedTextField();
        notesPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        notesField = new javax.swing.JTextArea();
        commandPanel = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();

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

        idLabel.setText(bundle.getString("customersdlg.customerIDLabelTxt"));

        idField.setEditable(false);
        idField.setFocusable(false);
        idField.setName("idField"); // NOI18N
        idField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
        });

        companyLabel.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        companyLabel.setText(bundle.getString("customersdlg.companyLabelTxt"));

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

        streetLabel.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        streetLabel.setText(bundle.getString("customersdlg.streetLabelTxt"));

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

        suiteLabel.setText(bundle.getString("customersdlg.suiteLabelTxt"));

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

        cityLabel.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        cityLabel.setText(bundle.getString("customersdlg.cityLabelTxt"));

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

        stateLabel.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        stateLabel.setText(bundle.getString("customersdlg.stateLabelTxt"));

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

        zipLabel.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        zipLabel.setText(bundle.getString("customersdlg.zipLabelTxt"));

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

        contactLabel.setText(bundle.getString("customersdlg.contactLabelTxt"));

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

        phoneLabel.setText(bundle.getString("customersdlg.phoneLabelTxt"));

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

        notesPanel.setBorder(null);
        notesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, bundle.getString("customersdlg.notesPanelTitle"), javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("sansserif", 0, 12))); // I18N

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

        javax.swing.GroupLayout notesPanelLayout = new javax.swing.GroupLayout(notesPanel);
        notesPanel.setLayout(notesPanelLayout);
        notesPanelLayout.setHorizontalGroup(
            notesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(notesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 535, Short.MAX_VALUE)
                .addContainerGap())
        );
        notesPanelLayout.setVerticalGroup(
            notesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(notesPanelLayout.createSequentialGroup()
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

        javax.swing.GroupLayout commandPanelLayout = new javax.swing.GroupLayout(commandPanel);
        commandPanel.setLayout(commandPanelLayout);
        commandPanelLayout.setHorizontalGroup(
            commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, commandPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(saveButton)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelButton)
                .addContainerGap())
        );
        commandPanelLayout.setVerticalGroup(
            commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, commandPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
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
                    .addComponent(notesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(companyLabel)
                            .addComponent(idLabel)
                            .addComponent(streetLabel)
                            .addComponent(cityLabel)
                            .addComponent(contactLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(contactField, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(phoneLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(phoneField))
                                .addComponent(companyField, javax.swing.GroupLayout.PREFERRED_SIZE, 496, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(streetField)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(cityField)
                                            .addGap(18, 18, 18)
                                            .addComponent(stateLabel)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(stateField, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(zipLabel)))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(zipField)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(suiteLabel)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(suiteField, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(helpPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(commandPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(idLabel)
                    .addComponent(idField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(companyLabel)
                    .addComponent(companyField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(streetLabel)
                    .addComponent(streetField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(suiteLabel)
                    .addComponent(suiteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cityLabel)
                    .addComponent(cityField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(stateLabel)
                    .addComponent(stateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(zipLabel)
                    .addComponent(zipField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(contactLabel)
                    .addComponent(contactField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(phoneLabel)
                    .addComponent(phoneField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(notesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(helpPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addComponent(commandPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                msg = bundle.getString("customersdlg.selectCityFieldMsg");
                break;
            case "companyField":
                msg = bundle.getString("customersdlg.selectCompanyFieldMsg");
                break;
            case "streetField":
                msg = bundle.getString("customersdlg.selectStreetFieldMsg");
                break;
            case "suiteField":
                msg = bundle.getString("customersdlg.selectSuiteFieldMsg");
                break;
            case "stateField":
                msg = bundle.getString("customersdlg.selectStateFieldMsg");
                break;
            case "zipField":
                msg = bundle.getString("customersdlg.selectZipFieldMsg");
                break;
            case "contactField":
                msg = bundle.getString("customersdlg.selectContactFieldMsg");
                break;
            case "phoneField":
                msg = bundle.getString("customersdlg.selectPhoneFieldMsg");
                break;
            case "notesField":
                msg = bundle.getString("customersdlg.selectNotesFieldMsg");
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
    private javax.swing.JLabel cityLabel;
    private javax.swing.JPanel commandPanel;
    private javax.swing.JTextField companyField;
    private javax.swing.JLabel companyLabel;
    private javax.swing.JTextField contactField;
    private javax.swing.JLabel contactLabel;
    public static javax.swing.JLabel helpLabel;
    public static javax.swing.JPanel helpPanel;
    private javax.swing.JTextField idField;
    private javax.swing.JLabel idLabel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea notesField;
    private javax.swing.JPanel notesPanel;
    private javax.swing.JFormattedTextField phoneField;
    private javax.swing.JLabel phoneLabel;
    private javax.swing.JButton saveButton;
    private javax.swing.JTextField stateField;
    private javax.swing.JLabel stateLabel;
    private javax.swing.JTextField streetField;
    private javax.swing.JLabel streetLabel;
    private javax.swing.JTextField suiteField;
    private javax.swing.JLabel suiteLabel;
    private javax.swing.JTextField zipField;
    private javax.swing.JLabel zipLabel;
    // End of variables declaration//GEN-END:variables
}
