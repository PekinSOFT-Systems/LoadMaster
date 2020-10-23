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
 *  Class      :   SettingsOperator.java
 *  Author     :   Jiří Kovalský
 *  Created    :   Oct 18, 2020 @ 09:51:27 PM
 *  Modified   :   Oct 18, 2020
 *  
 *  Purpose: Jemmy operator for Load Master Settings dialog.
 *  
 *  Revision History:
 *  
 *  WHEN          BY                  REASON
 *  ------------  ------------------- ------------------------------------------
 *  Oct 18, 2020  Jiří Kovalský       Initial creation.
 * *****************************************************************************
 */
package com.pekinsoft.loadmaster.operators;

import org.netbeans.jemmy.operators.JDialogOperator;
import org.netbeans.jemmy.operators.JTabbedPaneOperator;

/**
 * Jemmy operator for Load Master Settings dialog.
 *
 * @author Jiří Kovalský
 */
public class SettingsOperator extends JDialogOperator {

    public SettingsOperator() {
        super("Load Master Settings");
    }

    /**
     * Switches to specified settings tab.
     *
     * @param tabName Name of tab with settings to switch to.
     */
    private void switchToSettings(String tabName) {
        JTabbedPaneOperator paneOperator = new JTabbedPaneOperator(this);
        paneOperator.selectPage(tabName);
    }

    /**
     * Switches to General settings tab.
     */
    public void switchToGeneralSettings() {
        switchToSettings("General");
    }

    /**
     * Switches to Company settings tab.
     */
    public void switchToCompanySettings() {
        switchToSettings("Company");
    }

    /**
     * Switches to Directories settings tab.
     */
    public void switchToDirectoriesSettings() {
        switchToSettings("Directories");
    }

    /**
     * Switches to Accounting settings tab.
     */
    public void switchToAccountingSettings() {
        switchToSettings("Accounting");
    }
}
