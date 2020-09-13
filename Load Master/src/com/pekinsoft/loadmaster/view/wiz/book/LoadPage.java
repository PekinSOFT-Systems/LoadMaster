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
 *  Class      :   LoadPage.java
 *  Author     :   Sean Carrick
 *  Created    :   Sep 6, 2020 @ 3:34:33 PM
 *  Modified   :   Sep 6, 2020
 *  
 *  Purpose:
 *  
 *  Revision History:
 *  
 *  WHEN          BY                  REASON
 *  ------------  ------------------- ------------------------------------------
 *  Sep 6, 2020  Sean Carrick        Initial creation.
 * *****************************************************************************
 */

package com.pekinsoft.loadmaster.view.wiz.book;

import java.awt.Component;
import org.netbeans.spi.wizard.WizardPage;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.1.0
 * @since 0.1.0
 */
public class LoadPage extends WizardPage {
    //<editor-fold defaultstate="collapsed" desc="Public Static Constants">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Member Fields">
    // Variables declaration - do not modify                     
    private javax.swing.JCheckBox cbdField;
    private javax.swing.JTextField commdityField;
    private javax.swing.JFormattedTextField dispMilesField;
    private javax.swing.JFormattedTextField grossPayField;
    private javax.swing.JCheckBox hazMatField;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JCheckBox ltlField;
    private javax.swing.JTextField orderField;
    private javax.swing.JCheckBox rampsField;
    private javax.swing.JCheckBox tarpedField;
    private javax.swing.JCheckBox teamField;
    private javax.swing.JCheckBox topField;
    private javax.swing.JTextField tripField;
    private javax.swing.JCheckBox twicField;
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
    public LoadPage () {
        initComponents();
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Static Methods">
    public static final String getDescription() {
        return "Load Information";
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Instance Methods">
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Instance Methods">
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code copied from Holder.java">                          
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        orderField = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        tripField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        grossPayField = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        dispMilesField = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        commdityField = new javax.swing.JTextField();
        hazMatField = new javax.swing.JCheckBox();
        tarpedField = new javax.swing.JCheckBox();
        teamField = new javax.swing.JCheckBox();
        twicField = new javax.swing.JCheckBox();
        topField = new javax.swing.JCheckBox();
        ltlField = new javax.swing.JCheckBox();
        cbdField = new javax.swing.JCheckBox();
        rampsField = new javax.swing.JCheckBox();

        jLabel4.setText("jLabel4");

        jLabel5.setText("Order #:");

        orderField.setName("order"); // NOI18N

        jLabel1.setText("Trip #:");

        tripField.setName("trip"); // NOI18N

        jLabel2.setText("Gross Pay:");

        grossPayField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        grossPayField.setName("truck.pay"); // NOI18N

        jLabel3.setText("Miles:");

        dispMilesField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0"))));
        dispMilesField.setName("miles"); // NOI18N

        jLabel6.setText("Commodity:");

        commdityField.setName("commodity"); // NOI18N

        hazMatField.setText("HazMat");
        hazMatField.setName("hazmat"); // NOI18N

        tarpedField.setText("Tarped");
        tarpedField.setName("tarped"); // NOI18N

        teamField.setText("Team");
        teamField.setName("team"); // NOI18N

        twicField.setText("TWIC");
        twicField.setName("twic"); // NOI18N

        topField.setText("Top 100");
        topField.setName("top.customer"); // NOI18N

        ltlField.setText("LTL");
        ltlField.setName("ltl"); // NOI18N

        cbdField.setText("Sig & Talley");
        cbdField.setName("cbd"); // NOI18N

        rampsField.setText("Ramps");
        rampsField.setName("ramps"); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(topField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ltlField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cbdField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rampsField))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(hazMatField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tarpedField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(teamField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(twicField))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(orderField, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                                .addComponent(grossPayField))
                            .addGap(18, 18, 18)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel1)
                                .addComponent(jLabel3))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(tripField, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                                .addComponent(dispMilesField)))
                        .addComponent(commdityField)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(orderField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(tripField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(grossPayField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(dispMilesField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(commdityField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hazMatField)
                    .addComponent(tarpedField)
                    .addComponent(teamField)
                    .addComponent(twicField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(topField)
                    .addComponent(ltlField)
                    .addComponent(cbdField)
                    .addComponent(rampsField))
                .addContainerGap(168, Short.MAX_VALUE))
        );
    }// </editor-fold>                        
    //</editor-fold>

    @Override
    protected String validateContents(Component comp, Object o) {
        // Set the flags to our settings.
        if ( orderField.getText().length() == 0 )
            return "Order Number is required.";
        else if ( tripField.getText().length() == 0 )
            return "Trip Number is required.";
        else if ( grossPayField.getText().length() == 0 )
            return "Gross Truck Pay is required.";
        else if ( dispMilesField.getText().length() == 0 )
            return "Dispatched Miles is required.";
        else
            return null;
    }
}
