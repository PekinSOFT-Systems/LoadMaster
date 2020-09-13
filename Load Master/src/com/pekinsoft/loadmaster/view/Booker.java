/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pekinsoft.loadmaster.view;

import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.controller.LoadCtl;
import com.pekinsoft.loadmaster.err.DataStoreException;
import com.pekinsoft.loadmaster.err.InvalidTimeException;
import com.pekinsoft.loadmaster.model.BrokerModel;
import com.pekinsoft.loadmaster.model.CustomerModel;
import com.pekinsoft.loadmaster.model.LoadModel;
import com.pekinsoft.loadmaster.model.StopModel;
import com.pekinsoft.loadmaster.utils.MessageBox;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.swing.JFormattedTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Sean Carrick
 */
public class Booker extends javax.swing.JInternalFrame {
    private boolean isDirty;
    private boolean isLoading;
    private int stopNumber;
    private LogRecord entry;
    private LoadCtl loads;
    private LoadModel load;
    
    /**
     * Creates new form Booker
     */
    public Booker() {
        isLoading = true;
        entry = new LogRecord(Level.ALL, "Creating new Booker object.");
        entry.setSourceClassName(Booker.class.getName());
        entry.setSourceMethodName("Booker");
        entry.setParameters(new Object[]{});
        Starter.logger.enter(entry);
        
        initComponents();
        
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date now = new Date();
        dispatchDate.setDate(now);
        dispatchDate.getEditor().setText(sdf.format(now));
        
        getRootPane().setDefaultButton(bookLoad);
        
        entry.setMessage("Creating our LoadModel object.");
        Starter.logger.config(entry);
        load = new LoadModel();
        
        entry.setMessage("Attempting to create our LoadCtl object.");
        Starter.logger.config(entry);
        try {
            loads = new LoadCtl();
            entry.setMessage("LoadCtl object successfully created.");
            Starter.logger.info(entry);
        } catch ( DataStoreException ex ) {
                entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                        + "Throwing DataStoreException to calling method...");
                entry.setThrown(ex);
                entry.setSourceMethodName("connect");
                entry.setParameters(null);
                Starter.logger.error(entry);
        }
        
        isDirty = false;
        stopNumber = 0;
        isLoading = false;
        
        entry.setMessage("Leaving constructor...");
        Starter.logger.exit(entry, new Object[]{});
    }
    
    private boolean arePickupTimesValid() {
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
        
        if ( earlyPickupTime.getText() != null && !earlyPickupTime.getText().isBlank()
                && latePickupTime.getText() != null && !latePickupTime.getText().isBlank() ) {
            et = earlyPickupTime.getText().split(":");
            lt = latePickupTime.getText().split(":");
            etHour = Integer.valueOf(et[0]);
            etMin = Integer.valueOf(et[1]);
            ltHour = Integer.valueOf(lt[0]);
            ltMin = Integer.valueOf(lt[1]);        
        }
        
        if ( (etHour < 0 || etHour > 23) || (etMin < 0 || etMin > 59)
                || (ltHour < 0 || ltHour > 23) || (ltMin < 0 || ltMin > 59) ) {
            // One of the above tests failed.
            MessageBox.showWarning("Either the Early or Late time is not valid.", 
                    "Invalid Time Format");
            return false;
        }
        
        // Create a couple of Date object to hold the early and late times for
        //+ comparison, and reset the SimpleDateFormat object to only hold the
        //+ times, in 24-hour format.
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date early = null;
        Date late = null;
        
        if ( earlyPickupTime.getText() != null && latePickupTime.getText() != null ) {
            try {
                early = sdf.parse(earlyPickupTime.getText());
                late = sdf.parse(latePickupTime.getText());
            } catch ( ParseException ex ) {
                    entry.setMessage("A stop time was not valid.");
                    entry.setThrown(ex);
                    Starter.logger.error(entry);
                    entry.setMessage("Validation completed. Returning findings.");
                    entry.setThrown(null);
                    Starter.logger.exit(entry, new Object[] {false});
                    // We will return false from here so that the data cannot be
                    //+ saved.
                    MessageBox.showError(ex, "Date Parsing Error");
                    return false;
            }
        }
        
        // Now that we know that both times have been successfully created,
        //+ we need to validate the times, but only if the early and late
        //+ dates are the same day.
        if ( latePickupDate.getDate().compareTo(earlyPickupDate.getDate()) == 0 ) {
            // Only perform the next text if the early and late dates are on
            //+ the same date.
            if ( late.compareTo(early) < 0 ) {
                // The late time is before the early time.
                entry.setMessage("Validation completed. Returning findings.");
                Starter.logger.exit(entry, new Object[] {false});
                MessageBox.showWarning("Late pickup date is cannot be before the"
                        + " early pickup date", "Invalid Late Pickup Date");
                return false;
            }
        }
        
        return true;
    }
    
    private boolean areDeliveryTimesValid() {
        // Before we compare the early and late times, we need to make sure that
        //+ they are valid times. In order for them to be valid, they must pass
        //+ these two tests:
        //+
        //+     Test 1: The hour must NOT be less than zero nor greater than 23.
        //+     Test 2: The minute must NOT be less than zero nor greater than
        //+             59.
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
        
        if ( earlyDeliveryTime.getText() != null && !earlyDeliveryTime.getText().isBlank()
                && lateDeliveryTime.getText() != null && !lateDeliveryTime.getText().isBlank() ) {
            et = earlyDeliveryTime.getText().split(":");
            lt = lateDeliveryTime.getText().split(":");
            etHour = Integer.valueOf(et[0]);
            etMin = Integer.valueOf(et[1]);
            ltHour = Integer.valueOf(lt[0]);
            ltMin = Integer.valueOf(lt[1]);        
        }
        
        if ( (etHour < 0 || etHour > 23) || (etMin < 0 || etMin > 59)
                || (ltHour < 0 || ltHour > 23) || (ltMin < 0 || ltMin > 59) ) {
            // One of the above tests failed.
            MessageBox.showWarning("Either the Early or Late time is not valid.", 
                    "Invalid Time Format");
            return false;
        }
        
        // Create a couple of Date object to hold the early and late times for
        //+ comparison, and reset the SimpleDateFormat object to only hold the
        //+ times, in 24-hour format.
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date early = null;
        Date late = null;
        
        if ( earlyDeliveryTime.getText() != null && lateDeliveryTime.getText() != null ) {
            try {
                early = sdf.parse(earlyDeliveryTime.getText());
                late = sdf.parse(lateDeliveryTime.getText());
            } catch ( ParseException ex ) {
                    entry.setMessage("A stop time was not valid.");
                    entry.setThrown(ex);
                    Starter.logger.error(entry);
                    entry.setMessage("Validation completed. Returning findings.");
                    entry.setThrown(null);
                    Starter.logger.exit(entry, new Object[] {false});
                    // We will return false from here so that the data cannot be
                    //+ saved.
                    MessageBox.showError(ex, "Date Parsing Error");
                    return false;
            }
        }
        
        // Now that we know that both times have been successfully created,
        //+ we need to validate the times, but only if the early and late
        //+ dates are the same day.
        if ( lateDeliveryDate.getDate().compareTo(earlyDeliveryDate.getDate()) == 0 ) {
            // Only perform the next text if the early and late dates are on
            //+ the same date.
            if ( late.compareTo(early) < 0 ) {
                // The late time is before the early time.
                entry.setMessage("Validation completed. Returning findings.");
                Starter.logger.exit(entry, new Object[] {false});
                MessageBox.showWarning("Late pickup date is cannot be before the"
                        + " early pickup date", "Invalid Late Pickup Date");
                return false;
            }
        }
        
        return true;
    }
    
    private boolean arePickupAndDeliveryDatesValid() {
        // Lastly, we will perform the most complex validation we need to do, 
        //+ and that is making sure that the dates for pickup and delivery are
        //+ correct according to the rules at the beginning of this method.
        if ( latePickupDate.getDate() != null && earlyPickupDate.getDate() != null ) {
            if ( latePickupDate.getDate().compareTo(earlyPickupDate.getDate()) < 0 ) {
                // Less than zero (0) means that lateDate is BEFORE earlyDate.
                MessageBox.showWarning("Late pickup date must be the same date"
                        + "or later than early date.", "Invlid Pickup Date");
                return false;
            }
        }

        if ( lateDeliveryDate.getDate() != null && earlyDeliveryDate.getDate() != null ) {
            if ( lateDeliveryDate.getDate().compareTo(earlyDeliveryDate.getDate()) < 0 ) {
                // Less than zero (0) means that lateDate is BEFORE earlyDate.
                MessageBox.showWarning("Late delivery date must be the same date"
                        + "or later than early date.", "Invalid Delivery Date");
                return false;
            }
        }
        
        return true;
    }
    
    private boolean isAtLeastOneBrokerContactMethodPresent() {
        // Then, we will make sure that at least one of the broker/agent contact
        //+ methods have been provided.
        if ( (phoneField.getValue() == null 
                || phoneField.getValue().toString().isBlank()
                || phoneField.getValue().toString().isEmpty())
                && (faxField.getValue() == null 
                || faxField.getValue().toString().isBlank()
                || faxField.getValue().toString().isEmpty()) 
                && (emailField.getText() == null 
                || emailField.getText().isBlank()
                || emailField.getText().isEmpty()) ) {
            MessageBox.showWarning("Need at least one contact method for the\n"
                    + "broker or agent.", "Missing Data");
            return false;
        }
        
        return true;
    }
    
    private boolean areRequiredFieldsPresent() {
        // First, we will validate the easy stuff: order, trip, odometer, rate,
        //+ miles, and broker/agent.
        if ( orderNumberField.getText() == null
                || orderNumberField.getText().isBlank()
                || orderNumberField.getText().isEmpty() ) {
            MessageBox.showWarning("Order number is required.", "Missing Data");
            return false;
        }
        if ( tripNumberField.getText() == null
                || tripNumberField.getText().isBlank()
                || tripNumberField.getText().isEmpty() ) {
            MessageBox.showWarning("Trip number is required.", "Missing Data");
            return false;
        }
        if ( begOdoField.getText() == null
                ||begOdoField.getText().isBlank()
                || begOdoField.getText().isEmpty() ) {
            MessageBox.showWarning("Starting odometer is required.", "Missing Data");
            return false;
        }
        if ( grossPay.getText() == null || grossPay.getText().isBlank()
                || grossPay.getText().isEmpty() ) {
            MessageBox.showWarning("Gross rate is required.", "Missing Data");
            return false;
        }
        if ( loadMiles.getText() == null || loadMiles.getText().isBlank()
                || loadMiles.getText().isEmpty() ) {
            MessageBox.showWarning("Loaded miles is required.", "Missing Data");
            return false;
        }
        if ( brokerField.getText() == null || brokerField.getText().isBlank()
                || brokerField.getText().isEmpty() ) {
            MessageBox.showWarning("Broker name or company is required.", "Missing Data");
            return false;
        }
        
        return true;
    }
    
    private boolean areThereAtLeastTwoStops() {
        // Next, we will validate that a minimum of two (2) stops have been 
        //+ entered.
        if ( load.getStopCount() < 2 ) {
            MessageBox.showWarning("Minimum of two stops are required.", "Missing Data");
            return false;
        }
        
        return true;
    }
    
    private boolean isDataValid() {
        /* For validating the data on this screen, the following fields are 
         * required:
         *  
         *  * Order #
         *  * Trip #
         *  * Begin Odometer
         *  * Dispatched date - Autofilled with the current date when the window
         *                      opens.
         *  * First Pickup Early Date
         *  * First Pickup Early Time
         *  * First Pickup Late Date - Autofilled with the same date as early
         *  * First Pickup Late Time - Autofilled with the same time as early
         *  * Final Stop Early Date
         *  * Final Stop Early Time
         *  * Final Stop Late Date - Autofilled with the same date as early
         *  * Final Stop Late Time - Autofilled with the same time as early
         *  * Rate
         *  * Miles
         *  * Broker/Agent
         * 
         * Furthermore, there are other, more complicated, requirements on the
         * data provided. These are:
         *
         *  * Minimum of one (1) contact method for the Broker/Agent *MUST* be
         *    provided
         *  * Minimum of two (2) stops must be provided.
         *  * First Pickup Late Date *MUST* be greater than or equal to the 
         *    First Pickup Early Date
         *  * If the First Pickup Early Date and Late Date are the same, the
         *    Late Time must be greater than or equal to the Early Time
         *  * The same two rules go for the Last Stop Late Date and Time
         */
        return areRequiredFieldsPresent()
                && areThereAtLeastTwoStops()
                && isAtLeastOneBrokerContactMethodPresent()
                && arePickupAndDeliveryDatesValid()
                && arePickupTimesValid()
                && areDeliveryTimesValid();
    }
    
    private void calculateRPM() {
        if ( (grossPay.getText() != null || !grossPay.getText().isBlank() ||
                !grossPay.getText().isEmpty()) && (loadMiles.getText() != null
                || !loadMiles.getText().isBlank() 
                || !loadMiles.getText().isEmpty()) ) {
            Double pay = Double.valueOf(grossPay.getText());
            Double miles = Double.valueOf(loadMiles.getText());
            
            Double rate = pay / miles;
            perMileRate.setEditable(true);
            perMileRate.setText(String.format("%.2f", rate));
            perMileRate.setEditable(false);
            
            MessageBox.showInfo(pay.toString() + " / " + miles.toString(), "Debugging Message");
        }
    }

    private void doBook() {
        MessageBox.showInfo("All data is valid!", "Debugging Message");
    }
    
    private void doCancel() {
        if ( isDirty ) {
            int choice = MessageBox.askQuestion("Load has been changed.\n\n"
                    + "Are you sure you wish to discard changes?", 
                    "Confirm Cancel", false);
            
            if ( choice == MessageBox.YES_OPTION ) {
        
                LoadMaster.fileProgress.setValue(0);
                dispose();
            }
        } else
        
            LoadMaster.fileProgress.setValue(0);
            dispose();
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
        orderNumberField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        tripNumberField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        begOdoField = new javax.swing.JFormattedTextField();
        jPanel1 = new javax.swing.JPanel();
        datesPanel = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        dispatchDate = new org.jdesktop.swingx.JXDatePicker();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        earlyPickupDate = new org.jdesktop.swingx.JXDatePicker();
        earlyPickupTime = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        latePickupDate = new org.jdesktop.swingx.JXDatePicker();
        latePickupTime = new javax.swing.JFormattedTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        earlyDeliveryDate = new org.jdesktop.swingx.JXDatePicker();
        earlyDeliveryTime = new javax.swing.JFormattedTextField();
        jLabel8 = new javax.swing.JLabel();
        lateDeliveryDate = new org.jdesktop.swingx.JXDatePicker();
        lateDeliveryTime = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        grossPay = new javax.swing.JFormattedTextField();
        jLabel11 = new javax.swing.JLabel();
        perMileRate = new javax.swing.JFormattedTextField();
        jLabel12 = new javax.swing.JLabel();
        loadMiles = new javax.swing.JFormattedTextField();
        jLabel13 = new javax.swing.JLabel();
        freightWeight = new javax.swing.JFormattedTextField();
        jLabel14 = new javax.swing.JLabel();
        commodity = new javax.swing.JTextField();
        chkHazMat = new javax.swing.JCheckBox();
        chkTarped = new javax.swing.JCheckBox();
        chkTWIC = new javax.swing.JCheckBox();
        chkTop100 = new javax.swing.JCheckBox();
        chkLTL = new javax.swing.JCheckBox();
        chkSigAndTally = new javax.swing.JCheckBox();
        chkRamps = new javax.swing.JCheckBox();
        jPanel4 = new javax.swing.JPanel();
        cancelLoad = new javax.swing.JButton();
        bookLoad = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        addStop = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        stopsTable = new javax.swing.JTable();
        jLabel15 = new javax.swing.JLabel();
        brokerField = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        emailField = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        phoneField = new javax.swing.JFormattedTextField();
        jLabel18 = new javax.swing.JLabel();
        faxField = new javax.swing.JFormattedTextField();
        lookupButton = new javax.swing.JButton();
        chkTeam = new javax.swing.JCheckBox();

        setClosable(true);
        setTitle("Book Load");
        setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pekinsoft/loadmaster/res/Delivery.png"))); // NOI18N

        jLabel1.setText("Order #");

        orderNumberField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                flagDirtyProperty(evt);
            }
        });
        orderNumberField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                flagDirty(evt);
            }
        });

        jLabel2.setText("Trip #");

        tripNumberField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                flagDirtyProperty(evt);
            }
        });
        tripNumberField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                flagDirty(evt);
            }
        });

        jLabel3.setText("Begin Odometer:");

        begOdoField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.#"))));
        begOdoField.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                flagDirtyProperty(evt);
            }
        });
        begOdoField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                flagDirty(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        datesPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Load Dates"));

        jLabel10.setText("Dispatched:");

        dispatchDate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                flagDirtyProperty(evt);
            }
        });
        dispatchDate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("First Pickup Information"));

        jLabel5.setText("Early:");

        earlyPickupDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                earlyPickupDateActionPerformed(evt);
            }
        });
        earlyPickupDate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                flagDirtyProperty(evt);
            }
        });
        earlyPickupDate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
        });

        try {
            earlyPickupTime.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        earlyPickupTime.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                flagDirtyProperty(evt);
            }
        });
        earlyPickupTime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                earlyPickupTimeKeyReleased(evt);
            }
        });

        jLabel6.setText("Late:");

        latePickupDate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                flagDirtyProperty(evt);
            }
        });
        latePickupDate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
        });

        try {
            latePickupTime.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        latePickupTime.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                flagDirtyProperty(evt);
            }
        });
        latePickupTime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                flagDirty(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(earlyPickupDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(earlyPickupTime, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(latePickupDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(latePickupTime)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(earlyPickupDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(earlyPickupTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(latePickupDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6))
                    .addComponent(latePickupTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder("Last Delivery Information"));

        jLabel7.setText("Early:");

        earlyDeliveryDate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                earlyDeliveryDateActionPerformed(evt);
            }
        });
        earlyDeliveryDate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                flagDirtyProperty(evt);
            }
        });
        earlyDeliveryDate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
        });

        try {
            earlyDeliveryTime.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        earlyDeliveryTime.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                flagDirtyProperty(evt);
            }
        });
        earlyDeliveryTime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                earlyDeliveryTimeKeyReleased(evt);
            }
        });

        jLabel8.setText("Late:");

        lateDeliveryDate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                flagDirtyProperty(evt);
            }
        });
        lateDeliveryDate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
        });

        try {
            lateDeliveryTime.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        lateDeliveryTime.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                flagDirtyProperty(evt);
            }
        });
        lateDeliveryTime.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                flagDirty(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(earlyDeliveryDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(earlyDeliveryTime, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lateDeliveryDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lateDeliveryTime)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(earlyDeliveryDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(earlyDeliveryTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lateDeliveryTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lateDeliveryDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout datesPanelLayout = new javax.swing.GroupLayout(datesPanel);
        datesPanel.setLayout(datesPanelLayout);
        datesPanelLayout.setHorizontalGroup(
            datesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(datesPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(datesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(datesPanelLayout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(datesPanelLayout.createSequentialGroup()
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(dispatchDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        datesPanelLayout.setVerticalGroup(
            datesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(datesPanelLayout.createSequentialGroup()
                .addGroup(datesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(dispatchDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(datesPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jLabel9.setText("Rate:");

        grossPay.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        grossPay.setName("rate"); // NOI18N
        grossPay.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                grossPayFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                grossPayFocusLost(evt);
            }
        });
        grossPay.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                grossPayCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        grossPay.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                flagDirtyProperty(evt);
            }
        });
        grossPay.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                grossPayVetoableChange(evt);
            }
        });

        jLabel11.setText("Per Mile:");

        perMileRate.setEditable(false);
        perMileRate.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        perMileRate.setFocusable(false);
        perMileRate.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                flagDirtyProperty(evt);
            }
        });
        perMileRate.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                flagDirty(evt);
            }
        });

        jLabel12.setText("Miles:");

        loadMiles.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        loadMiles.setName("miles"); // NOI18N
        loadMiles.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                loadMilesFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                loadMilesFocusLost(evt);
            }
        });
        loadMiles.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadMilesActionPerformed(evt);
            }
        });
        loadMiles.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                flagDirtyProperty(evt);
            }
        });
        loadMiles.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                loadMilesVetoableChange(evt);
            }
        });

        jLabel13.setText("Weight:");

        freightWeight.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                flagDirtyProperty(evt);
            }
        });
        freightWeight.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                flagDirty(evt);
            }
        });

        jLabel14.setText("Commodity:");

        commodity.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                flagDirtyProperty(evt);
            }
        });
        commodity.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                flagDirty(evt);
            }
        });

        chkHazMat.setText("HazMat");
        chkHazMat.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                flagDirtyCheck(evt);
            }
        });
        chkHazMat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
        });

        chkTarped.setText("Tarped");
        chkTarped.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                flagDirtyCheck(evt);
            }
        });
        chkTarped.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
        });

        chkTWIC.setText("TWIC");
        chkTWIC.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                flagDirtyCheck(evt);
            }
        });
        chkTWIC.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
        });

        chkTop100.setText("Top100");
        chkTop100.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                flagDirtyCheck(evt);
            }
        });
        chkTop100.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
        });

        chkLTL.setText("LTL");
        chkLTL.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                flagDirtyCheck(evt);
            }
        });
        chkLTL.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
        });

        chkSigAndTally.setText("Sig & Tally");
        chkSigAndTally.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                flagDirtyCheck(evt);
            }
        });
        chkSigAndTally.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
        });

        chkRamps.setText("Ramps");
        chkRamps.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                flagDirtyCheck(evt);
            }
        });
        chkRamps.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
        });

        cancelLoad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pekinsoft/loadmaster/res/Cancel.png"))); // NOI18N
        cancelLoad.setMnemonic('C');
        cancelLoad.setText("Cancel Load");
        cancelLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelLoadActionPerformed(evt);
            }
        });
        cancelLoad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
        });

        bookLoad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pekinsoft/loadmaster/res/add.png"))); // NOI18N
        bookLoad.setMnemonic('B');
        bookLoad.setText("Book Load");
        bookLoad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bookLoadActionPerformed(evt);
            }
        });
        bookLoad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bookLoad)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cancelLoad)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cancelLoad)
                    .addComponent(bookLoad))
                .addContainerGap())
        );

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Stops"));

        addStop.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pekinsoft/loadmaster/res/add.png"))); // NOI18N
        addStop.setMnemonic('A');
        addStop.setText("Add Stop...");
        addStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStopActionPerformed(evt);
            }
        });
        addStop.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
        });

        stopsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "#", "Company", "Address", "Early Date", "Early Time", "Late Date", "Late Time"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        stopsTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
        });
        jScrollPane1.setViewportView(stopsTable);
        if (stopsTable.getColumnModel().getColumnCount() > 0) {
            stopsTable.getColumnModel().getColumn(0).setMinWidth(15);
            stopsTable.getColumnModel().getColumn(0).setPreferredWidth(35);
            stopsTable.getColumnModel().getColumn(0).setMaxWidth(50);
        }

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(addStop))
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(addStop))
        );

        jLabel15.setText("Broker/Agent:");

        brokerField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                flagDirty(evt);
            }
        });

        jLabel16.setText("Email:");

        emailField.setEditable(false);
        emailField.setFocusable(false);
        emailField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                flagDirty(evt);
            }
        });

        jLabel17.setText("Phone:");

        phoneField.setEditable(false);
        phoneField.setFocusable(false);
        phoneField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                flagDirty(evt);
            }
        });

        jLabel18.setText("Fax:");

        faxField.setEditable(false);
        faxField.setFocusable(false);
        faxField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                flagDirty(evt);
            }
        });

        lookupButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/com/pekinsoft/loadmaster/res/Find.png"))); // NOI18N
        lookupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lookupButtonActionPerformed(evt);
            }
        });
        lookupButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
        });

        chkTeam.setText("Team");
        chkTeam.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                flagDirtyCheck(evt);
            }
        });
        chkTeam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEnterEscape(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(datesPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(orderNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tripNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(begOdoField, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel15)
                            .addComponent(jLabel9)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(grossPay, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(perMileRate, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel12)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(loadMiles, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(freightWeight, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel14)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(commodity))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(brokerField, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(lookupButton))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel18)
                                            .addComponent(jLabel17))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(phoneField, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                                            .addComponent(faxField))))
                                .addGap(0, 115, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(chkHazMat)
                        .addGap(18, 18, 18)
                        .addComponent(chkTarped)
                        .addGap(18, 18, 18)
                        .addComponent(chkTWIC)
                        .addGap(18, 18, 18)
                        .addComponent(chkTeam)
                        .addGap(18, 18, 18)
                        .addComponent(chkTop100)
                        .addGap(18, 18, 18)
                        .addComponent(chkLTL)
                        .addGap(18, 18, 18)
                        .addComponent(chkSigAndTally)
                        .addGap(18, 18, 18)
                        .addComponent(chkRamps)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(orderNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(tripNumberField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(begOdoField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(datesPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(grossPay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel11)
                    .addComponent(perMileRate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(loadMiles, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel13)
                    .addComponent(freightWeight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14)
                    .addComponent(commodity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkHazMat)
                    .addComponent(chkTarped)
                    .addComponent(chkTWIC)
                    .addComponent(chkTop100)
                    .addComponent(chkLTL)
                    .addComponent(chkSigAndTally)
                    .addComponent(chkRamps)
                    .addComponent(chkTeam))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(brokerField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(phoneField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lookupButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel16)
                    .addComponent(emailField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(faxField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void checkEnterEscape(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_checkEnterEscape
        // Check to see if the enter or escape key was pressed.
        if ( evt.getKeyCode() == KeyEvent.VK_ENTER ) 
            doBook();
        else if ( evt.getKeyCode() == KeyEvent.VK_ESCAPE ) 
            doCancel();
    }//GEN-LAST:event_checkEnterEscape

    private void lookupButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lookupButtonActionPerformed
        // Show the broker selection dialog.
        BrokerSelector dlg = new BrokerSelector(null, true);
        dlg.pack();
        dlg.show();
        
        if ( dlg != null ) {
            BrokerModel b = dlg.getSelectedBroker();
            
            if ( b.getContact() == null || b.getContact().isBlank()
                    || b.getContact().isEmpty() ) {
                if ( b.getCompany() != null && !b.getCompany().isBlank()
                        && !b.getCompany().isEmpty() )
                    brokerField.setText(b.getCompany());
            } else {
                brokerField.setText(b.getContact());
            }
            
            if ( b.getPhone() == null || b.getPhone().isBlank()
                    || b.getPhone().isEmpty() ) {
                if ( b.getFax() == null || b.getFax().isBlank()
                        || b.getFax().isEmpty() ) {
                    if ( b.getEmail() != null && !b.getEmail().isBlank()
                            && !b.getEmail().isEmpty() )
                        emailField.setText(b.getEmail());
                } else 
                    faxField.setText(b.getFax());
            } else 
                phoneField.setText(b.getPhone());
        }
        if ( !isLoading ) 
            bookLoad.setEnabled(isDataValid());
    }//GEN-LAST:event_lookupButtonActionPerformed

    private void addStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStopActionPerformed
        CustomerSelector dlg = new CustomerSelector(null, true);
        dlg.pack();
        dlg.show();
        
        if ( dlg != null ) {
             
            SimpleDateFormat fmt = new SimpleDateFormat("MM/dd/yyyy");
            String eDate = fmt.format(dlg.getEarlyDate());
            String lDate = fmt.format(dlg.getLateDate());
            DefaultTableModel model = (DefaultTableModel)stopsTable.getModel();
            CustomerModel c = dlg.getSelectedCustomer();
            Object[] row = {++stopNumber,       // Stop Number
                            c.getCompany(),     // Company name
                            c.getAddress(),     // Complete address
                            eDate,              // Early date
                            dlg.getEarlyTime(), // Early time
                            lDate,              // Late date
                            dlg.getLateTime()}; // Late time
            model.addRow(row);
            
            stopsTable.setModel(model);
            
            StopModel stop = new StopModel();
            stop.setEarlyDate(dlg.getEarlyDate());
            stop.setLateDate(dlg.getLateDate());
            stop.setCustomer(c.getId());
            
            try {
                stop.setEarlyTime(dlg.getEarlyTime());
                stop.setLateTime(dlg.getLateTime());
            } catch ( InvalidTimeException ex ) {
                entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                        + "Provided time is invalid.");
                entry.setThrown(ex);
                entry.setSourceMethodName("connect");
                entry.setParameters(null);
                Starter.logger.error(entry);
            } catch ( ParseException ex ) {
                entry.setMessage(ex.getMessage() + "\n\n" + "-".repeat(80)
                        + "Throwing DataStoreException to calling method...");
                entry.setThrown(ex);
                entry.setSourceMethodName("connect");
                entry.setParameters(null);
                Starter.logger.error(entry);
            }
            
            load.addStop(stop);
            
        }

        if ( !isLoading ) 
            bookLoad.setEnabled(isDataValid());
    }//GEN-LAST:event_addStopActionPerformed

    private void cancelLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelLoadActionPerformed
        doCancel();
    }//GEN-LAST:event_cancelLoadActionPerformed

    private void flagDirty(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_flagDirty
        isDirty = true;
        
        if ( !isLoading ) 
            bookLoad.setEnabled(isDataValid());
    }//GEN-LAST:event_flagDirty

    private void flagDirtyProperty(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_flagDirtyProperty
        isDirty = true;
        
        if ( !isLoading ) 
            bookLoad.setEnabled(isDataValid());
    }//GEN-LAST:event_flagDirtyProperty

    private void flagDirtyCheck(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_flagDirtyCheck
        isDirty = true;
        
        if ( !isLoading ) 
            bookLoad.setEnabled(isDataValid());
    }//GEN-LAST:event_flagDirtyCheck

    private void bookLoadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bookLoadActionPerformed
        doBook();
    }//GEN-LAST:event_bookLoadActionPerformed

    private void earlyPickupDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_earlyPickupDateActionPerformed
        // Autofill the late pickup date with the date selected here.
        latePickupDate.setDate(earlyPickupDate.getDate());
        latePickupDate.getEditor().setText(earlyPickupDate.getEditor().getText());
        
        isDirty = true;
    }//GEN-LAST:event_earlyPickupDateActionPerformed

    private void earlyPickupTimeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_earlyPickupTimeKeyReleased
        latePickupTime.setText(earlyPickupTime.getText());
        
        flagDirty(evt);
    }//GEN-LAST:event_earlyPickupTimeKeyReleased

    private void earlyDeliveryTimeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_earlyDeliveryTimeKeyReleased
        lateDeliveryTime.setText(earlyDeliveryTime.getText());
        
        flagDirty(evt);
    }//GEN-LAST:event_earlyDeliveryTimeKeyReleased

    private void earlyDeliveryDateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_earlyDeliveryDateActionPerformed
        // Autofill the late delivery date with the date selected here.
        lateDeliveryDate.setDate(earlyDeliveryDate.getDate());
        lateDeliveryDate.getEditor().setText(earlyDeliveryDate.getEditor().getText());
    }//GEN-LAST:event_earlyDeliveryDateActionPerformed

    private void grossPayCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_grossPayCaretPositionChanged
        calculateRPM();
    }//GEN-LAST:event_grossPayCaretPositionChanged

    private void grossPayVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_grossPayVetoableChange
        calculateRPM();
    }//GEN-LAST:event_grossPayVetoableChange

    private void loadMilesVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_loadMilesVetoableChange
        calculateRPM();
    }//GEN-LAST:event_loadMilesVetoableChange

    private void grossPayFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_grossPayFocusLost
        calculateRPM();       
    }//GEN-LAST:event_grossPayFocusLost

    private void grossPayFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_grossPayFocusGained
        calculateRPM();       
    }//GEN-LAST:event_grossPayFocusGained

    private void loadMilesFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_loadMilesFocusGained
        calculateRPM();
    }//GEN-LAST:event_loadMilesFocusGained

    private void loadMilesFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_loadMilesFocusLost
        calculateRPM();    
    }//GEN-LAST:event_loadMilesFocusLost

    private void loadMilesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadMilesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_loadMilesActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addStop;
    private javax.swing.JFormattedTextField begOdoField;
    private javax.swing.JButton bookLoad;
    private javax.swing.JTextField brokerField;
    private javax.swing.JButton cancelLoad;
    private javax.swing.JCheckBox chkHazMat;
    private javax.swing.JCheckBox chkLTL;
    private javax.swing.JCheckBox chkRamps;
    private javax.swing.JCheckBox chkSigAndTally;
    private javax.swing.JCheckBox chkTWIC;
    private javax.swing.JCheckBox chkTarped;
    private javax.swing.JCheckBox chkTeam;
    private javax.swing.JCheckBox chkTop100;
    private javax.swing.JTextField commodity;
    private javax.swing.JPanel datesPanel;
    private org.jdesktop.swingx.JXDatePicker dispatchDate;
    private org.jdesktop.swingx.JXDatePicker earlyDeliveryDate;
    private javax.swing.JFormattedTextField earlyDeliveryTime;
    private org.jdesktop.swingx.JXDatePicker earlyPickupDate;
    private javax.swing.JFormattedTextField earlyPickupTime;
    private javax.swing.JTextField emailField;
    private javax.swing.JFormattedTextField faxField;
    private javax.swing.JFormattedTextField freightWeight;
    private javax.swing.JFormattedTextField grossPay;
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
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private org.jdesktop.swingx.JXDatePicker lateDeliveryDate;
    private javax.swing.JFormattedTextField lateDeliveryTime;
    private org.jdesktop.swingx.JXDatePicker latePickupDate;
    private javax.swing.JFormattedTextField latePickupTime;
    private javax.swing.JFormattedTextField loadMiles;
    private javax.swing.JButton lookupButton;
    private javax.swing.JTextField orderNumberField;
    private javax.swing.JFormattedTextField perMileRate;
    private javax.swing.JFormattedTextField phoneField;
    private javax.swing.JTable stopsTable;
    private javax.swing.JTextField tripNumberField;
    // End of variables declaration//GEN-END:variables
}
