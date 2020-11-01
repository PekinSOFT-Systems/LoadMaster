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
 *  Class      :   ChartOfAccountsViewer.java
 *  Author     :   Sean Carrick
 *  Created    :   Oct 11, 2020 @ 6:31:21 PM
 *  Modified   :   Oct 11, 2020
 *  
 *  Purpose:
 *  
 *  Revision History:
 *  
 *  WHEN          BY                  REASON
 *  ------------  ------------------- ------------------------------------------
 *  Oct 11, 2020  Sean Carrick        Initial creation.
 * *****************************************************************************
 */
package com.pekinsoft.loadmaster.view;

import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.model.ChartModel;
import com.pekinsoft.loadmaster.utils.ScreenUtils;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.LogRecord;
import java.util.logging.Level;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Sean Carrick
 */
public class ChartOfAccountsViewer extends javax.swing.JDialog {

    private final File COA;
    private LogRecord entry;
    private ArrayList<ChartModel> chart;

    /**
     * Creates new form ChartOfAccountsViewer
     */
    public ChartOfAccountsViewer(java.awt.Frame parent, boolean modal) {
        super(parent, modal);

        entry = new LogRecord(Level.ALL, "Instantiating the Chart of Accounts "
                + "Viewer...");
        entry.setSourceClassName(this.getClass().getName());

        initComponents();

        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource(
                "/com/pekinsoft/loadmaster/res/open-list.png"));
        setIconImage(icon);

        getRootPane().setDefaultButton(closeButton);

        setLocation(ScreenUtils.centerDialog(this));

        chart = new ArrayList<>();

        COA = new File(Starter.props.getDataFolder() + "coa.tbl");

        if (!COA.exists()) {
            generateChartOfAccounts();
        } else {
            getChartOfAccountsFromFile();
        }

        populateTable();
    }

    private void generateChartOfAccounts() {
        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(COA));

            // We need to create the default Chart of Accounts, which will 
            // Consist of the following:
            //+     * Accounts Payable
            //+     * Accounting
            //+     * Communications
            //+     * Fees
            //+     * Fuel
            //+     * Insurance
            //+     * Interest
            //+     * Loading/Lumpers
            //+     * Maintenance
            //+     * Office
            //+     * Physicals
            //+     * Rent/Lease
            //+     * Scales
            //+     * Supplies
            //+     * Tax/License
            //+     * Tolls/Parking
            //+     * Travel
            //+     * Uniforms
            //+     * Wages
            //+     * Checking Acct
            //+     * Savings Acct
            //+     * Retirement Savings Acct
            //+     * Fuel Card
            //+     * Accounts Receivable
            out.write("9999~AP~Accounts Payable: What you owe others on credit.\n");
            out.write("10010~Accounting~All accounting and legal expenses.\n");
            out.write("10020~Communications~All communications expenses.\n");
            out.write("10030~Fees~Any fees that you need to pay.\n");
            out.write("10040~Fuel~All fuel related expenses.\n");
            out.write("10050~Insurance~All insurance expenses.\n");
            out.write("10060~Interest~All business-related interest expenses.\n");
            out.write("10070~Loading/Lumping~All loader and lumper fees.\n");
            out.write("10080~Maintenance~Maintenance and repair expenses.\n");
            out.write("10090~Office~All office expenses.\n");
            out.write("10100~Physicals~All business-related medical costs.\n");
            out.write("10110~Rent/Lease~All rent/lease fees and expenses.\n");
            out.write("10120~Scales~Scale costs and PrePASS expenses.\n");
            out.write("10130~Supplies~Miscellaneous supplies.\n");
            out.write("10140~Tax/License~Business-related tax/license expenses.\n");
            out.write("10150~Tolls/Parking~Toll charges and parking fees.\n");
            out.write("10160~Travel~Business-related travel expenses.\n");
            out.write("10170~Uniforms~Any uniform or logo shirt expenses.\n");
            out.write("10180~Wages~All payroll expenses.\n");
            out.write("50010~Checking Account~Primary banking account.\n");
            out.write("50020~Savings Account~Primary savings account.\n");
            out.write("50030~Retirement Savings Account~Savings for retirement.\n");
            out.write("50040~Fuel Card~Fuel card account.\n");
            out.write("50500~Accounts Receivable~Your truck pay at dispatch.\n");

            // Now that we've created the default chart of accounts, we can 
            //+ close the file.
            out.close();
        } catch (IOException ex) {
            entry.setSourceMethodName("generateChartOfAccounts");
            entry.setMessage(ex.getMessage());
            entry.setThrown(ex);
            Starter.logger.error(entry);
        }

        // Once the file is created, let's read it back in so we only need to
        //+ populate the table one time.
        getChartOfAccountsFromFile();
    }

    private void getChartOfAccountsFromFile() {
        try {
            BufferedReader in = new BufferedReader(new FileReader(COA));

            // We need to read in the chart of accounts from file and save it to
            //+ our member field `chart`.
            String line = in.readLine();

            while (line != null) {
                String[] record = line.split("~");

                createAndAddRecord(record);

                line = in.readLine();
            }
        } catch (IOException ex) {
            entry.setSourceMethodName("getChartOfAccountsFromFile");
            entry.setMessage(ex.getMessage());
            entry.setThrown(ex);
            Starter.logger.error(entry);
        }

    }

    private void createAndAddRecord(String[] record) {
        ChartModel model = new ChartModel();
        model.setNumber(Long.parseLong(record[0]));
        model.setName(record[1]);
        model.setDescription(record[2]);

        chart.add(model);
    }

    private void populateTable() {
        DefaultTableModel model = (DefaultTableModel) coaTable.getModel();

        for (int x = 0; x < chart.size(); x++) {
            Object[] row = {chart.get(x).getNumber(),
                chart.get(x).getName(),
                chart.get(x).getDescription()
            };
            model.addRow(row);
        }

        coaTable.setModel(model);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        coaTable = new javax.swing.JTable();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Chart of Accounts");
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEscape(evt);
            }
        });

        coaTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Account Number", "Account Name", "Description"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Long.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        coaTable.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEscape(evt);
            }
        });
        jScrollPane1.setViewportView(coaTable);

        closeButton.setMnemonic('C');
        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        closeButton.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                checkEscape(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 622, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(closeButton)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(closeButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        // Close the dialog.
        dispose();
    }//GEN-LAST:event_closeButtonActionPerformed

    private void checkEscape(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_checkEscape
        if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ESCAPE) {
            dispose();
        }
    }//GEN-LAST:event_checkEscape

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
            java.util.logging.Logger.getLogger(ChartOfAccountsViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChartOfAccountsViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChartOfAccountsViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChartOfAccountsViewer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ChartOfAccountsViewer dialog = new ChartOfAccountsViewer(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton closeButton;
    private javax.swing.JTable coaTable;
    private javax.swing.JScrollPane jScrollPane1;
    // End of variables declaration//GEN-END:variables
}
