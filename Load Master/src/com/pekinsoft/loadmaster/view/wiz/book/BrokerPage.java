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
 *  Class      :   BrokerPage.java
 *  Author     :   Sean Carrick
 *  Created    :   Sep 6, 2020 @ 3:34:44 PM
 *  Modified   :   Sep 6, 2020
 *  
 *  Purpose:
 *  
 *  Revision History:
 *  
 *  WHEN          BY                  REASON
 *  ------------  ------------------- ------------------------------------------
 *  Sep 06, 2020  Sean Carrick        Initial creation.
 *  Oct 06, 2020  Sean Carrick        Added "Add New Broker/Agent..." item to 
 *                                    the brokerList JCombobox and the method to
 *                                    display the Add Broker/Agent dialog.
 *  Oct 09, 2020  Sean Carrick        Added putWizardData() method call into the
 *                                    validateContents() method once the user
 *                                    selects a broker/agent and store only the
 *                                    unique ID for the broker/agent.
 * *****************************************************************************
 */
package com.pekinsoft.loadmaster.view.wiz.book;

import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.controller.BrokerCtl;
import com.pekinsoft.loadmaster.err.DataStoreException;
import com.pekinsoft.loadmaster.model.BrokerModel;
import com.pekinsoft.loadmaster.utils.MessageBox;
import com.pekinsoft.loadmaster.view.LoadMaster;
import com.pekinsoft.loadmaster.view.NewBrokerDlg;
import java.awt.Component;
import java.util.ArrayList;
import org.netbeans.spi.wizard.WizardPage;
import java.util.logging.Level;
import java.util.logging.LogRecord;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.1.0
 * @since 0.1.0
 */
public class BrokerPage extends WizardPage {
    //<editor-fold defaultstate="collapsed" desc="Public Static Constants">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Member Fields">
    // Variables declaration - do not modify                     
    private javax.swing.JRadioButton allFilterOption;
    private javax.swing.JLabel brokerLabel;
    private javax.swing.JComboBox<String> brokerList;
    private javax.swing.JTextField cityField;
    private javax.swing.JRadioButton cityFilterOption;
    private javax.swing.JLabel cityLabel;
    private javax.swing.JTextField companyField;
    private javax.swing.JLabel companyLabel;
    private javax.swing.JTextField contactField;
    private javax.swing.JRadioButton contactFilterOption;
    private javax.swing.JLabel contactLabel;
    private javax.swing.JTextField criteriaField;
    private javax.swing.JLabel criteriaLabel;
    private javax.swing.JLabel emailAddress;
    private javax.swing.JTextField emailField;
    private javax.swing.JTextField faxField;
    private javax.swing.JLabel faxLabel;
    private javax.swing.JButton filterButton;
    private javax.swing.ButtonGroup filterButtonGroup;
    private javax.swing.JPanel filterPanel;
    private javax.swing.JTextField phoneField;
    private javax.swing.JRadioButton phoneFilterOption;
    private javax.swing.JLabel phoneLabel;
    private javax.swing.JTextField stateField;
    private javax.swing.JRadioButton stateFilterOption;
    private javax.swing.JLabel stateLabel;
    private javax.swing.JTextField streetField;
    private javax.swing.JLabel streetLabel;
    private javax.swing.JTextField suiteField;
    private javax.swing.JLabel suiteLabel;
    private javax.swing.JTextField zipField;
    private javax.swing.JLabel zipLabel;
    
    private boolean filtering;
    
    private LogRecord lr;
    private BrokerCtl records;
    private BrokerModel broker;
    // End of variables declaration                   
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Static Initializer">
    static {
        
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Intstance Initializer">
    {
        
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
    public BrokerPage () {
        lr = new LogRecord(Level.ALL, "Logging initialized for BrokerSelector.");
        lr.setSourceClassName(BrokerPage.class.getCanonicalName());
        lr.setSourceMethodName(BrokerPage.class.getName());
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
        
        // For now, we will make the phoneFilterOption not visible.
        phoneFilterOption.setVisible(false);
        
        // For now, we will make the contactFilterOption not visible, as it is
        //+ not working for some reason.
        contactFilterOption.setVisible(false);
        
        // Set our filtering flag to false, until it is needed.
        filtering = false;
        
        loadBrokerList();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Static Methods">
    public static final String getDescription() {
        return "Broker/Agent Information()";
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Instance Methods">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Instance Methods">
    
    private void loadBrokerList() {
        brokerList.removeAllItems();
        brokerList.addItem("Select Broker/Agent...");
        brokerList.addItem("Add New Broker/Agent...");
        
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
        } else if ( contactFilterOption.isSelected() ) {
            BrokerModel b;
            try {
                b = records.getCompanyByContact(contactField.getText(), 
                        LoadMaster.fileProgress);
            } catch ( DataStoreException ex ) {
                lr.setMessage("Something went wrong moving to the next record.");
                lr.setThrown(ex);
                Starter.logger.error(lr);
            }
        } else if ( phoneFilterOption.isSelected() ) {
            BrokerModel b;
            try {
                b = records.getCompanyByPhone(phoneField.getText(), 
                        LoadMaster.fileProgress);
            } catch ( DataStoreException ex ) {
                lr.setMessage("Something went wrong moving to the next record.");
                lr.setThrown(ex);
                Starter.logger.error(lr);
            }
        }
        
        filtering = false;
    }
    
    public BrokerModel getSelectedBroker() {
        return broker;
    }
    
    private void selectedBrokerChanged(java.awt.event.ItemEvent evt) {          
        // Before doing anything else, we need to see if the user wants to add
        //+ a new broker/agent to the database.
        if ( !filtering ) {
            if ( brokerList.getSelectedItem().toString().equalsIgnoreCase(
                    "add new broker/agent...") ) {
                NewBrokerDlg dlg = new NewBrokerDlg(null, true);
                dlg.pack();
                dlg.setVisible(true);

                // Make sure the user did not cancel the dialog.
                if ( !dlg.isCancelled() ) {
                    String newBroker = dlg.getBroker().getContact() + " (" +
                            String.valueOf(dlg.getBroker().getId()) + ")";
                    brokerList.addItem(newBroker);
                    brokerList.setSelectedItem(newBroker);
                }
            }

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

                    if ( broker.getContact() != null ) {
                        contactField.setText(broker.getContact());
                    }
                    if ( broker.getCompany() != null ) {
                        companyField.setText(broker.getCompany());
                    }
                    if ( broker.getCity() != null ) {
                        cityField.setText(broker.getCity());
                    }
                    if ( broker.getState() != null ) {
                        stateField.setText(broker.getState());
                    }
                    if ( broker.getStreet() != null ) {
                        streetField.setText(broker.getStreet());
                    }
                    if ( broker.getSuite() != null ) {
                        suiteField.setText(broker.getSuite());
                    }
                    if ( broker.getZip() != null ) {
                        zipField.setText(broker.getZip());
                    }
                    if ( broker.getPhone() != null ) {
                        phoneField.setText(broker.getPhone());
                    }
                    if ( broker.getFax() != null ) {
                        faxField.setText(broker.getFax());
                    }
                    if ( broker.getEmail() != null ) {
                        emailField.setText(broker.getEmail());
                    }
                } catch ( DataStoreException ex ) {
                    lr.setMessage("Something went wrong moving to the next record.");
                    lr.setThrown(ex);
                    Starter.logger.error(lr);

        //            MessageBox.showError(ex, "Database Access");
                }
            }       
        }
    }                                      
    //</editor-fold>

    
    @Override
    protected String validateContents(Component comp, Object o) {
        if ( !filtering ) {
            if ( brokerList.getSelectedItem().toString().equals(
                    "Select Broker/Agent...") ) {
                return "An agent or broker must be selected.";
            }
        }

        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        filterButtonGroup = new javax.swing.ButtonGroup();
        filterPanel = new javax.swing.JPanel();
        stateFilterOption = new javax.swing.JRadioButton();
        allFilterOption = new javax.swing.JRadioButton();
        cityFilterOption = new javax.swing.JRadioButton();
        contactFilterOption = new javax.swing.JRadioButton();
        phoneFilterOption = new javax.swing.JRadioButton();
        criteriaLabel = new javax.swing.JLabel();
        criteriaField = new javax.swing.JTextField();
        filterButton = new javax.swing.JButton();
        brokerLabel = new javax.swing.JLabel();
        brokerList = new javax.swing.JComboBox<>();
        companyLabel = new javax.swing.JLabel();
        companyField = new javax.swing.JTextField();
        contactLabel = new javax.swing.JLabel();
        contactField = new javax.swing.JTextField();
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
        phoneLabel = new javax.swing.JLabel();
        phoneField = new javax.swing.JTextField();
        faxField = new javax.swing.JTextField();
        faxLabel = new javax.swing.JLabel();
        emailAddress = new javax.swing.JLabel();
        emailField = new javax.swing.JTextField();

        filterPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Filter List"));

        filterButtonGroup.add(stateFilterOption);
        stateFilterOption.setMnemonic('S');
        stateFilterOption.setText("By State");
        stateFilterOption.setName("StateFilter"); // NOI18N

        filterButtonGroup.add(allFilterOption);
        allFilterOption.setMnemonic('A');
        allFilterOption.setSelected(true);
        allFilterOption.setText("All");
        allFilterOption.setName("AllFilter"); // NOI18N

        filterButtonGroup.add(cityFilterOption);
        cityFilterOption.setMnemonic('C');
        cityFilterOption.setText("By City");
        cityFilterOption.setName("CityFilter"); // NOI18N

        filterButtonGroup.add(contactFilterOption);
        contactFilterOption.setMnemonic('o');
        contactFilterOption.setText("By Contact");
        contactFilterOption.setName("ContactFilter"); // NOI18N

        filterButtonGroup.add(phoneFilterOption);
        phoneFilterOption.setMnemonic('P');
        phoneFilterOption.setText("By  Phone");
        phoneFilterOption.setName("PhoneFilter"); // NOI18N

        criteriaLabel.setText("Criteria:");

        criteriaField.setName("FilterCriteria"); // NOI18N
        criteriaField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                criteriaFieldKeyTyped(evt);
            }
        });

        filterButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pekinsoft/loadmaster/res/filter.png"))); // NOI18N
        filterButton.setEnabled(false);
        filterButton.setName("FilterButton"); // NOI18N
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
                .addComponent(allFilterOption)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(stateFilterOption)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cityFilterOption)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(contactFilterOption)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(phoneFilterOption)
                .addContainerGap(32, Short.MAX_VALUE))
            .addGroup(filterPanelLayout.createSequentialGroup()
                .addComponent(criteriaLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(criteriaField)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(filterButton))
        );
        filterPanelLayout.setVerticalGroup(
            filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(filterPanelLayout.createSequentialGroup()
                .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(allFilterOption)
                    .addComponent(stateFilterOption)
                    .addComponent(cityFilterOption)
                    .addComponent(contactFilterOption)
                    .addComponent(phoneFilterOption))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(filterPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(criteriaLabel)
                    .addComponent(criteriaField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(filterButton)))
        );

        brokerLabel.setText("Broker/Agent:");

        brokerList.setName("brokerList"); // NOI18N
        brokerList.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                brokerListItemStateChanged(evt);
            }
        });

        companyLabel.setText("Company:");

        companyField.setName("Company"); // NOI18N

        contactLabel.setText("Contact:");

        contactField.setName("Contact"); // NOI18N

        streetLabel.setText("Street Address:");

        streetField.setName("Street"); // NOI18N

        suiteLabel.setText("Suite:");

        suiteField.setName("Suite"); // NOI18N

        cityLabel.setText("City:");

        cityField.setName("City"); // NOI18N

        stateLabel.setText("State:");

        stateField.setName("State"); // NOI18N

        zipLabel.setText("Zip:");

        zipField.setName("Zip"); // NOI18N

        phoneLabel.setText("Phone:");

        phoneField.setName("Phone"); // NOI18N

        faxField.setName("Fax"); // NOI18N
        faxField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                faxFieldActionPerformed(evt);
            }
        });

        faxLabel.setText("Fax:");

        emailAddress.setText("Email Address:");

        emailField.setName("Email"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(filterPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(companyLabel)
                            .addComponent(brokerLabel)
                            .addComponent(contactLabel)
                            .addComponent(streetLabel)
                            .addComponent(cityLabel)
                            .addComponent(phoneLabel)
                            .addComponent(emailAddress))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(brokerList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(companyField)
                            .addComponent(contactField)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(streetField)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(suiteLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(suiteField, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(cityField, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(stateLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(stateField, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(zipLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(zipField))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(phoneField, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(faxLabel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(faxField, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(emailField))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(filterPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(brokerLabel)
                    .addComponent(brokerList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(companyLabel)
                    .addComponent(companyField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(contactLabel)
                    .addComponent(contactField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(phoneLabel)
                    .addComponent(phoneField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(faxField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(faxLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(emailAddress)
                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(99, Short.MAX_VALUE))
        );
    }// </editor-fold>                        

    private void faxFieldActionPerformed(java.awt.event.ActionEvent evt) {                                         
        // TODO add your handling code here:
    }                                        

    private void filterButtonActionPerformed(java.awt.event.ActionEvent evt) {                                             
        filtering = true;
        loadBrokerList();
    }                                            

    private void criteriaFieldKeyTyped(java.awt.event.KeyEvent evt) {                                       
        filterButton.setEnabled(criteriaField.getText().length() > 0);
    }                                      

    private void brokerListItemStateChanged(java.awt.event.ItemEvent evt) {                                            
        selectedBrokerChanged(evt);
    }                                           

}
