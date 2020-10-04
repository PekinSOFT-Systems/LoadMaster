/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pekinsoft.loadmaster.view;

import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.sys.AppProperties;
import com.pekinsoft.loadmaster.utils.ScreenUtils;
import java.awt.event.KeyEvent;

/**
 *
 * @author Sean Carrick
 */
public class SettingsDialog extends javax.swing.JDialog {

    /**
     * Creates new form SettingsDialog
     */
    public SettingsDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        setLocation(ScreenUtils.centerDialog(this));
        
        loadProperties();
    }
    
    private void loadProperties() {
        settingsTabbedPane.remove(accountingTab);
        settingsTabbedPane.remove(numberingTab);
        orderPrefixField.setEnabled(false);
        tripPrefixField.setEnabled(false);
        invoicePrefixField.setEnabled(false);
        
        truckNumberField.setText(Starter.props.getProperty("truck.number", "[NOT SET]"));
        vinField.setText(Starter.props.getProperty("truck.vin", "[NOT SET]"));
        makeList.setSelectedItem(Starter.props.getProperty("truck.make", "Select Make..."));
        modelField.setText(Starter.props.getProperty("truck.model", "[NOT SET]"));
        odometerField.setText(Starter.props.getProperty("truck.start.odometer", "0"));
        trailerNumberField.setText(Starter.props.getProperty("trailer.number", "[NOT SET]"));
        trailerVINField.setText(Starter.props.getProperty("trailer.vin", "[NOT SET]"));
        trailerMakeField.setText(Starter.props.getProperty("trailer.make", "[NOT SET]"));
        trailerModelField.setText(Starter.props.getProperty("trailer.model", "[NOT SET]"));
        companyNameField.setText(Starter.props.getProperty("company.name", "[NOT SET]"));
        companyTypeList.setSelectedItem(Starter.props.getProperty("company.type", "Select Organization Type..."));
        streetField.setText(Starter.props.getProperty("company.street", ""));
        suiteField.setText(Starter.props.getProperty("company.suite", ""));
        cityField.setText(Starter.props.getProperty("company.city", ""));
        stateField.setText(Starter.props.getProperty("company.state", ""));
        zipField.setText(Starter.props.getProperty("company.zip", ""));
        phoneField.setText(Starter.props.getProperty("company.phone", ""));
        faxField.setText(Starter.props.getProperty("company.fax", ""));
        emailField.setText(Starter.props.getProperty("company.email", ""));
        chkAuthority.setSelected(Starter.props.getPropertyAsBoolean("authority", "false"));
        orderPrefixList.setSelectedItem(Starter.props.getProperty("order.prefix", "Select Prefix..."));
        orderPrefixField.setText(Starter.props.getProperty("order.prefix.value", ""));
        orderMinField.setText(Starter.props.getProperty("order.min", ""));
        orderMaxField.setText(Starter.props.getProperty("order.max", ""));
        tripPrefixList.setSelectedItem(Starter.props.getProperty("trip.prefix", "Select Prefix..."));
        tripPrefixField.setText(Starter.props.getProperty("trip.prefix.value", ""));
        tripMinField.setText(Starter.props.getProperty("trip.min", ""));
        tripMaxField.setText(Starter.props.getProperty("trip.max", ""));
        invoicePrefixList.setSelectedItem(Starter.props.getProperty("invoice.prefix", "Select Prefix..."));
        invoicePrefixField.setText(Starter.props.getProperty("invoice.prefix.value", ""));
        invoiceMinField.setText(Starter.props.getProperty("invoice.min", ""));
        invoiceMaxField.setText(Starter.props.getProperty("invoice.max", "")
                + System.getProperty("file.separator") + "data"
                + System.getProperty("file.separator"));
        dataField.setText(Starter.props.getProperty("data.directory", AppProperties.APP_DIR + "data"
                + System.getProperty("file.separator")));
        logField.setText(AppProperties.APP_DIR
                /*+ System.getProperty("file.separator")*/ + "var"
                + System.getProperty("file.separator") + "log"
                + System.getProperty("file.separator"));
        errorField.setText(AppProperties.APP_DIR
                /*+ System.getProperty("file.separator")*/ + "var" 
                + System.getProperty("file.separator") + "err"
                + System.getProperty("file.separator"));
        configField.setText(AppProperties.APP_DIR);
    }

    private void doClose() {
        // Save the settings to the settings file.
        Starter.props.setProperty("truck.number", truckNumberField.getText());
        Starter.props.setProperty("truck.vin", vinField.getText());
        Starter.props.setProperty("truck.make", makeList.getSelectedItem().toString());
        Starter.props.setProperty("truck.model", modelField.getText());
        Starter.props.setProperty("truck.start.odometer", odometerField.getText());
        Starter.props.setProperty("trailer.number", trailerNumberField.getText());
        Starter.props.setProperty("trailer.vin", trailerVINField.getText());
        Starter.props.setProperty("trailer.make", trailerMakeField.getText());
        Starter.props.setProperty("trailer.model", trailerModelField.getText());
        Starter.props.setProperty("company.name", companyNameField.getText());
        Starter.props.setProperty("company.type", companyTypeList.getSelectedItem().toString());
        Starter.props.setProperty("company.street", streetField.getText());
        Starter.props.setProperty("company.suite", suiteField.getText());
        Starter.props.setProperty("company.city", cityField.getText());
        Starter.props.setProperty("company.state", stateField.getText());
        Starter.props.setProperty("company.zip", zipField.getText());
        Starter.props.setProperty("company.phone", phoneField.getText());
        Starter.props.setProperty("company.fax", faxField.getText());
        Starter.props.setProperty("company.email", emailField.getText());
        Starter.props.setPropertyAsBoolean("authority", chkAuthority.isSelected());
        Starter.props.setProperty("order.prefix", orderPrefixList.getSelectedItem().toString());
        Starter.props.setProperty("order.prefix.value", orderPrefixField.getText());
        Starter.props.setProperty("order.min", orderMinField.getText());
        Starter.props.setProperty("order.max", orderMaxField.getText());
        Starter.props.setProperty("trip.prefix", tripPrefixList.getSelectedItem().toString());
        Starter.props.setProperty("trip.prefix.value", tripPrefixField.getText());
        Starter.props.setProperty("trip.min", tripMinField.getText());
        Starter.props.setProperty("trip.max", tripMaxField.getText());
        Starter.props.setProperty("invoice.prefix", invoicePrefixList.getSelectedItem().toString());
        Starter.props.setProperty("invoice.prefix.value", invoicePrefixField.getText());
        Starter.props.setProperty("invoice.min", invoiceMinField.getText());
        Starter.props.setProperty("invoice.max", invoiceMaxField.getText());
        
        Starter.props.flush();
        
        // Then, close the dialog.
        this.dispose();
    }
    
    private void toggleFields() {
        orderPrefixField.setEnabled(orderPrefixList.getSelectedItem().toString()
                .equalsIgnoreCase("other..."));
        tripPrefixField.setEnabled(tripPrefixList.getSelectedItem().toString()
                .equalsIgnoreCase("other..."));
        invoicePrefixField.setEnabled(invoicePrefixList.getSelectedItem()
                .toString().equalsIgnoreCase("other..."));
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        settingsTabbedPane = new javax.swing.JTabbedPane();
        generalTab = new javax.swing.JPanel();
        truckInfoPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        truckNumberField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        makeList = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        modelField = new javax.swing.JTextField();
        vinField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        odometerField = new javax.swing.JFormattedTextField();
        tailerInfoPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        trailerNumberField = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        trailerMakeField = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        trailerModelField = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        trailerVINField = new javax.swing.JTextField();
        companyTab = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        companyNameField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        companyTypeList = new javax.swing.JComboBox<>();
        chkAuthority = new javax.swing.JCheckBox();
        jLabel12 = new javax.swing.JLabel();
        streetField = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        suiteField = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        cityField = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        stateField = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        zipField = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        phoneField = new javax.swing.JFormattedTextField();
        jLabel19 = new javax.swing.JLabel();
        faxField = new javax.swing.JFormattedTextField();
        jLabel22 = new javax.swing.JLabel();
        emailField = new javax.swing.JTextField();
        numberingTab = new javax.swing.JPanel();
        orderPanel = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        orderPrefixList = new javax.swing.JComboBox<>();
        orderPrefixField = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        orderMinField = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        orderMaxField = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        tripPrefixList = new javax.swing.JComboBox<>();
        tripPrefixField = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        tripMinField = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        tripMaxField = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        invoicePrefixList = new javax.swing.JComboBox<>();
        invoicePrefixField = new javax.swing.JTextField();
        jLabel32 = new javax.swing.JLabel();
        invoiceMinField = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        invoiceMaxField = new javax.swing.JTextField();
        directoriesTab = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        dataField = new javax.swing.JTextField();
        dataBrowseButton = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        logField = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        errorField = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        configField = new javax.swing.JTextField();
        accountingTab = new javax.swing.JPanel();
        commandPanel = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Load Master Settings");
        setModal(true);
        setModalExclusionType(java.awt.Dialog.ModalExclusionType.APPLICATION_EXCLUDE);

        truckInfoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Truck Information"));

        jLabel1.setText("Truck #:");

        truckNumberField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                doSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                doDeselection(evt);
            }
        });
        truckNumberField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterKey(evt);
            }
        });

        jLabel3.setText("Make:");

        makeList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Make...", "Freightliner", "Hino", "International", "Kenworth", "Peterbilt", "Sterling", "Scania", "Volvo", "Western Star" }));
        makeList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterKey(evt);
            }
        });

        jLabel4.setText("Model:");

        modelField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                doSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                doDeselection(evt);
            }
        });
        modelField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterKey(evt);
            }
        });

        vinField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                doSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                doDeselection(evt);
            }
        });
        vinField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterKey(evt);
            }
        });

        jLabel5.setText("VIN:");

        jLabel6.setText("Starting Odometer:");

        odometerField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        odometerField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                doSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                doDeselection(evt);
            }
        });
        odometerField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterKey(evt);
            }
        });

        javax.swing.GroupLayout truckInfoPanelLayout = new javax.swing.GroupLayout(truckInfoPanel);
        truckInfoPanel.setLayout(truckInfoPanelLayout);
        truckInfoPanelLayout.setHorizontalGroup(
            truckInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(truckInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(truckInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(truckInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(truckInfoPanelLayout.createSequentialGroup()
                        .addGroup(truckInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(truckNumberField)
                            .addComponent(makeList, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(truckInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, truckInfoPanelLayout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(modelField, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, truckInfoPanelLayout.createSequentialGroup()
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(odometerField, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(vinField, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(40, Short.MAX_VALUE))
        );
        truckInfoPanelLayout.setVerticalGroup(
            truckInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(truckInfoPanelLayout.createSequentialGroup()
                .addGroup(truckInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(truckInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel6)
                        .addComponent(odometerField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(truckInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(truckNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(truckInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(makeList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(modelField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(truckInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(vinField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 6, Short.MAX_VALUE))
        );

        tailerInfoPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Trailer Information"));

        jLabel2.setText("Trailer #:");

        trailerNumberField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                doSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                doDeselection(evt);
            }
        });
        trailerNumberField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterKey(evt);
            }
        });

        jLabel7.setText("Make:");

        trailerMakeField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                doSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                doDeselection(evt);
            }
        });
        trailerMakeField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterKey(evt);
            }
        });

        jLabel8.setText("Model:");

        trailerModelField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                doSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                doDeselection(evt);
            }
        });
        trailerModelField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterKey(evt);
            }
        });

        jLabel9.setText("VIN:");

        trailerVINField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                doSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                doDeselection(evt);
            }
        });
        trailerVINField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterKey(evt);
            }
        });

        javax.swing.GroupLayout tailerInfoPanelLayout = new javax.swing.GroupLayout(tailerInfoPanel);
        tailerInfoPanel.setLayout(tailerInfoPanelLayout);
        tailerInfoPanelLayout.setHorizontalGroup(
            tailerInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tailerInfoPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(tailerInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel7)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(tailerInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tailerInfoPanelLayout.createSequentialGroup()
                        .addGroup(tailerInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(trailerNumberField, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
                            .addComponent(trailerMakeField))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(trailerModelField, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(trailerVINField))
                .addGap(39, 39, 39))
        );
        tailerInfoPanelLayout.setVerticalGroup(
            tailerInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tailerInfoPanelLayout.createSequentialGroup()
                .addGroup(tailerInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(trailerNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tailerInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(trailerMakeField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8)
                    .addComponent(trailerModelField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tailerInfoPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(trailerVINField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout generalTabLayout = new javax.swing.GroupLayout(generalTab);
        generalTab.setLayout(generalTabLayout);
        generalTabLayout.setHorizontalGroup(
            generalTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(generalTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(truckInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(tailerInfoPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        generalTabLayout.setVerticalGroup(
            generalTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(generalTabLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(truckInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tailerInfoPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        settingsTabbedPane.addTab("General", generalTab);

        jLabel10.setText("Name:");

        companyNameField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                doSelection(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                doDeselection(evt);
            }
        });
        companyNameField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterKey(evt);
            }
        });

        jLabel11.setText("Organization:");

        companyTypeList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Organization Type...", "Sole Proprietorship", "Limited Liability Company", "S-Corporation", "Corporation", "501(c)3 Corporation" }));
        companyTypeList.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterKey(evt);
            }
        });

        chkAuthority.setText("Have Own Authority");
        chkAuthority.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                chkAuthorityStateChanged(evt);
            }
        });
        chkAuthority.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterKey(evt);
            }
        });

        jLabel12.setText("Street:");

        jLabel13.setText("Suite:");

        jLabel14.setText("City:");

        jLabel15.setText("State:");

        jLabel16.setText("Zip:");

        jLabel18.setText("Phone:");

        try {
            phoneField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(###) ###-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel19.setText("Fax:");

        try {
            faxField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(###) ###-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabel22.setText("Email:");

        javax.swing.GroupLayout companyTabLayout = new javax.swing.GroupLayout(companyTab);
        companyTab.setLayout(companyTabLayout);
        companyTabLayout.setHorizontalGroup(
            companyTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(companyTabLayout.createSequentialGroup()
                .addGap(85, 85, 85)
                .addComponent(chkAuthority)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(companyTabLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(companyTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14)
                    .addComponent(jLabel12)
                    .addComponent(jLabel10)
                    .addComponent(jLabel18)
                    .addComponent(jLabel22))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(companyTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(companyNameField)
                    .addGroup(companyTabLayout.createSequentialGroup()
                        .addGroup(companyTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(companyTabLayout.createSequentialGroup()
                                .addComponent(streetField)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel13))
                            .addGroup(companyTabLayout.createSequentialGroup()
                                .addComponent(cityField)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel15)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(stateField, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel16)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(companyTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(zipField, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE)
                            .addComponent(suiteField)))
                    .addGroup(companyTabLayout.createSequentialGroup()
                        .addComponent(phoneField, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel19)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(faxField, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(companyTabLayout.createSequentialGroup()
                        .addComponent(emailField, javax.swing.GroupLayout.DEFAULT_SIZE, 215, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel11)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(companyTypeList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        companyTabLayout.setVerticalGroup(
            companyTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(companyTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(companyTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(companyNameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(companyTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(streetField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(suiteField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(companyTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(cityField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15)
                    .addComponent(stateField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(zipField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(companyTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(phoneField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(faxField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(companyTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(companyTypeList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkAuthority)
                .addContainerGap(118, Short.MAX_VALUE))
        );

        settingsTabbedPane.addTab("Company", companyTab);

        orderPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Order Number Options"));

        jLabel17.setText("Prefix:");

        orderPrefixList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Prefix...", "Long Year", "Short Year", "Other..." }));
        orderPrefixList.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                orderPrefixListItemStateChanged(evt);
            }
        });

        jLabel20.setText("Min:");

        jLabel21.setText("Max:");

        javax.swing.GroupLayout orderPanelLayout = new javax.swing.GroupLayout(orderPanel);
        orderPanel.setLayout(orderPanelLayout);
        orderPanelLayout.setHorizontalGroup(
            orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(orderPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(orderPrefixList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(orderPrefixField, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(orderMinField, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel21)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(orderMaxField, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(101, Short.MAX_VALUE))
        );
        orderPanelLayout.setVerticalGroup(
            orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(orderPanelLayout.createSequentialGroup()
                .addGroup(orderPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(orderPrefixList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(orderPrefixField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(orderMinField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(orderMaxField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Trip Number Options"));

        jLabel25.setText("Prefix:");

        tripPrefixList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Prefix...", "Long Year", "Short Year", "Other..." }));
        tripPrefixList.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                tripPrefixListItemStateChanged(evt);
            }
        });

        jLabel27.setText("Min:");

        jLabel28.setText("Max:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tripPrefixList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(tripPrefixField, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel27)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tripMinField, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel28)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tripMaxField, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(tripPrefixList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tripPrefixField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27)
                    .addComponent(tripMinField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28)
                    .addComponent(tripMaxField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Invoice Number Options"));

        jLabel30.setText("Prefix:");

        invoicePrefixList.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Select Prefix...", "Long Year", "Short Year", "Other..." }));
        invoicePrefixList.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                invoicePrefixListItemStateChanged(evt);
            }
        });

        jLabel32.setText("Min:");

        jLabel33.setText("Max:");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(invoicePrefixList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(invoicePrefixField, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel32)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(invoiceMinField, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel33)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(invoiceMaxField, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(invoicePrefixList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(invoicePrefixField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel32)
                    .addComponent(invoiceMinField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel33)
                    .addComponent(invoiceMaxField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout numberingTabLayout = new javax.swing.GroupLayout(numberingTab);
        numberingTab.setLayout(numberingTabLayout);
        numberingTabLayout.setHorizontalGroup(
            numberingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(orderPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        numberingTabLayout.setVerticalGroup(
            numberingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(numberingTabLayout.createSequentialGroup()
                .addComponent(orderPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        settingsTabbedPane.addTab("Numbering", numberingTab);

        jLabel23.setText("Data Directory:");

        dataBrowseButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pekinsoft/loadmaster/res/Folder.png"))); // NOI18N

        jLabel24.setText("Log Directory:");

        logField.setEditable(false);
        logField.setFocusable(false);

        jLabel26.setText("Error Directory:");

        errorField.setEditable(false);
        errorField.setFocusable(false);

        jLabel29.setText("Config Directory:");

        configField.setEditable(false);
        configField.setFocusable(false);

        javax.swing.GroupLayout directoriesTabLayout = new javax.swing.GroupLayout(directoriesTab);
        directoriesTab.setLayout(directoriesTabLayout);
        directoriesTabLayout.setHorizontalGroup(
            directoriesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(directoriesTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(directoriesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel29)
                    .addComponent(jLabel26)
                    .addComponent(jLabel24)
                    .addComponent(jLabel23))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(directoriesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dataField)
                    .addComponent(logField)
                    .addComponent(errorField)
                    .addComponent(configField, javax.swing.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dataBrowseButton)
                .addContainerGap())
        );
        directoriesTabLayout.setVerticalGroup(
            directoriesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, directoriesTabLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(directoriesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(configField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(directoriesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dataBrowseButton)
                    .addGroup(directoriesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel23)
                        .addComponent(dataField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(directoriesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(errorField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(directoriesTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(logField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(182, Short.MAX_VALUE))
        );

        settingsTabbedPane.addTab("Directories", directoriesTab);

        javax.swing.GroupLayout accountingTabLayout = new javax.swing.GroupLayout(accountingTab);
        accountingTab.setLayout(accountingTabLayout);
        accountingTabLayout.setHorizontalGroup(
            accountingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 564, Short.MAX_VALUE)
        );
        accountingTabLayout.setVerticalGroup(
            accountingTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 318, Short.MAX_VALUE)
        );

        settingsTabbedPane.addTab("Accounting", accountingTab);

        closeButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pekinsoft/loadmaster/res/Cancel.png"))); // NOI18N
        closeButton.setMnemonic('C');
        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout commandPanelLayout = new javax.swing.GroupLayout(commandPanel);
        commandPanel.setLayout(commandPanelLayout);
        commandPanelLayout.setHorizontalGroup(
            commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, commandPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(closeButton)
                .addContainerGap())
        );
        commandPanelLayout.setVerticalGroup(
            commandPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, commandPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(closeButton)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(commandPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(settingsTabbedPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(settingsTabbedPane)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(commandPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void doSelection(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_doSelection
        if ( evt.getSource() instanceof javax.swing.JTextField )
            ((javax.swing.JTextField)evt.getSource()).selectAll();
    }//GEN-LAST:event_doSelection

    private void doDeselection(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_doDeselection
        if ( evt.getSource() instanceof javax.swing.JTextField )
            ((javax.swing.JTextField)evt.getSource()).select(0, 0);
    }//GEN-LAST:event_doDeselection

    private void checkEnterKey(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_checkEnterKey
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER )
            doClose();
    }//GEN-LAST:event_checkEnterKey

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        doClose();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void chkAuthorityStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_chkAuthorityStateChanged
        // If the authority checkbox is checked, we want to show the numbering
        //+ options frame, otherwise, we want it hidden.
        if ( chkAuthority.isSelected() ) {
            settingsTabbedPane.add(numberingTab, 2);
            settingsTabbedPane.setTitleAt(2, "Numbering");
        } else
            settingsTabbedPane.remove(numberingTab);
    }//GEN-LAST:event_chkAuthorityStateChanged

    private void orderPrefixListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_orderPrefixListItemStateChanged
        toggleFields();
    }//GEN-LAST:event_orderPrefixListItemStateChanged

    private void tripPrefixListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_tripPrefixListItemStateChanged
        toggleFields();
    }//GEN-LAST:event_tripPrefixListItemStateChanged

    private void invoicePrefixListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_invoicePrefixListItemStateChanged
        toggleFields();
    }//GEN-LAST:event_invoicePrefixListItemStateChanged

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
            java.util.logging.Logger.getLogger(SettingsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SettingsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SettingsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SettingsDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SettingsDialog dialog = new SettingsDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JPanel accountingTab;
    private javax.swing.JCheckBox chkAuthority;
    private javax.swing.JTextField cityField;
    private javax.swing.JButton closeButton;
    private javax.swing.JPanel commandPanel;
    private javax.swing.JTextField companyNameField;
    private javax.swing.JPanel companyTab;
    private javax.swing.JComboBox<String> companyTypeList;
    private javax.swing.JTextField configField;
    private javax.swing.JButton dataBrowseButton;
    private javax.swing.JTextField dataField;
    private javax.swing.JPanel directoriesTab;
    private javax.swing.JTextField emailField;
    private javax.swing.JTextField errorField;
    private javax.swing.JFormattedTextField faxField;
    private javax.swing.JPanel generalTab;
    private javax.swing.JTextField invoiceMaxField;
    private javax.swing.JTextField invoiceMinField;
    private javax.swing.JTextField invoicePrefixField;
    private javax.swing.JComboBox<String> invoicePrefixList;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField logField;
    private javax.swing.JComboBox<String> makeList;
    private javax.swing.JTextField modelField;
    private javax.swing.JPanel numberingTab;
    private javax.swing.JFormattedTextField odometerField;
    private javax.swing.JTextField orderMaxField;
    private javax.swing.JTextField orderMinField;
    private javax.swing.JPanel orderPanel;
    private javax.swing.JTextField orderPrefixField;
    private javax.swing.JComboBox<String> orderPrefixList;
    private javax.swing.JFormattedTextField phoneField;
    private javax.swing.JTabbedPane settingsTabbedPane;
    private javax.swing.JTextField stateField;
    private javax.swing.JTextField streetField;
    private javax.swing.JTextField suiteField;
    private javax.swing.JPanel tailerInfoPanel;
    private javax.swing.JTextField trailerMakeField;
    private javax.swing.JTextField trailerModelField;
    private javax.swing.JTextField trailerNumberField;
    private javax.swing.JTextField trailerVINField;
    private javax.swing.JTextField tripMaxField;
    private javax.swing.JTextField tripMinField;
    private javax.swing.JTextField tripPrefixField;
    private javax.swing.JComboBox<String> tripPrefixList;
    private javax.swing.JPanel truckInfoPanel;
    private javax.swing.JTextField truckNumberField;
    private javax.swing.JTextField vinField;
    private javax.swing.JTextField zipField;
    // End of variables declaration//GEN-END:variables
}
