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
 *  Class      :   BookNewLoadWizardOperator.java
 *  Author     :   Jiří Kovalský
 *  Created    :   Oct 18, 2020 @ 06:19:23 PM
 *  Modified   :   Oct 18, 2020
 *  
 *  Purpose: Jemmy operator for Book New Load Wizard.
 *  
 *  Revision History:
 *  
 *  WHEN          BY                  REASON
 *  ------------  ------------------- ------------------------------------------
 *  Oct 18, 2020  Jiří Kovalský       Initial creation.
 * *****************************************************************************
 */
package com.pekinsoft.loadmaster.operators;

import org.netbeans.jemmy.EventTool;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JCheckBoxOperator;
import org.netbeans.jemmy.operators.JDialogOperator;
import org.netbeans.jemmy.operators.JLabelOperator;
import org.netbeans.jemmy.operators.JTextFieldOperator;

/**
 * Jemmy operator for Book New Load Wizard.
 *
 * @author Jiří Kovalský
 */
public class BookNewLoadWizardOperator extends JDialogOperator {

    public BookNewLoadWizardOperator() {
        super("Book New Load Wizard");
    }

    /**
     * Sets given string as order number for the load.
     *
     * @param orderNumber Number identifier of this order.
     */
    public void setOrderNumber(String orderNumber) {
        JTextFieldOperator orderNumberField = new JTextFieldOperator(this, 0);
        orderNumberField.typeText(orderNumber);
        new EventTool().waitNoEvent(1000);
        System.out.println("======== Order number: " + orderNumberField.getDisplayedText());
    }

    /**
     * Sets given string as gross pay for the load.
     *
     * @param grossPay Number representing gross pay for this load. Can contain
     * decimal point.
     */
    public void setGrossPay(String grossPay) {
        JTextFieldOperator grossPayField = new JTextFieldOperator(this, 1);
        grossPayField.typeText(grossPay);
        new EventTool().waitNoEvent(1000);
        System.out.println("======== Gross pay: " + grossPayField.getDisplayedText());
    }

    /**
     * Sets given string as trip number for the load.
     *
     * @param tripNumber Number of the trip this load represents.
     */
    public void setTripNumber(String tripNumber) {
        JTextFieldOperator tripNumberField = new JTextFieldOperator(this, 2);
        tripNumberField.typeText(tripNumber);
        new EventTool().waitNoEvent(1000);
        System.out.println("======== Trip: " + tripNumberField.getDisplayedText());
    }

    /**
     * Sets given string as number of miles for the load.
     *
     * @param milesNumber Number of miles this load represents.
     */
    public void setMilesNumber(String milesNumber) {
        JTextFieldOperator milesNumberField = new JTextFieldOperator(this, 3);
        milesNumberField.typeText(milesNumber);
        new EventTool().waitNoEvent(1000);
        System.out.println("======== Miles: " + milesNumberField.getDisplayedText());
    }

    /**
     * Sets given string as commodity to be loaded.
     *
     * @param commodity Name of the loaded commodity.
     */
    public void setCommodity(String commodity) {
        JTextFieldOperator commodityField = new JTextFieldOperator(this, 4);
        commodityField.typeText(commodity);
        new EventTool().waitNoEvent(1000);
        System.out.println("======== Commodity: " + commodityField.getDisplayedText());
    }

    /**
     * Either checks or unchecks selected option on currently selected page.
     *
     * @param option Name of the option to be un/checked.
     * @param value Required state of the selected option.
     */
    public void checkOption(String option, boolean value) {
        JCheckBoxOperator optionCheckBox = new JCheckBoxOperator(this, option);
        optionCheckBox.setSelected(value);
        new EventTool().waitNoEvent(1000);
        System.out.println("============ Option " + option + " is " + optionCheckBox.isSelected());
    }

    /**
     * Clicks Next > button of the wizard.
     */
    public void goNext() {
        JButtonOperator nextButtonOperator = new JButtonOperator(this, "Next >");
        System.out.println("============ Next > button is " + (nextButtonOperator.isEnabled() ? "ENABLED" : "DISABLED ============"));
        nextButtonOperator.push();
        new EventTool().waitNoEvent(1000);
    }

    /**
     * Clicks Cancel button of the wizard.
     */
    public void cancel() {
        JButtonOperator cancelButtonOperator = new JButtonOperator(this, "Cancel");
        cancelButtonOperator.push();
    }

    /**
     * Returns name of current wizard page based on the first visible label.
     *
     * @return Name of the currently displayed wizard page.
     */
    public String getPageName() {
        JLabelOperator labelOperator = new JLabelOperator(this, 0);
        return labelOperator.getText();
    }

}
