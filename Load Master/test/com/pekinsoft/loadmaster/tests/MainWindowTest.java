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
 *  Class      :   MainWindowTest.java
 *  Author     :   Jiří Kovalský
 *  Created    :   Oct 16, 2020 @ 06:28:10 PM
 *  Modified   :   Oct 23, 2020
 *  
 *  Purpose: Very basic test of the main application window.
 *  
 *  Revision History:
 *  
 *  WHEN          BY                  REASON
 *  ------------  ------------------- ------------------------------------------
 *  Oct 16, 2020  Jiří Kovalský       Initial creation.
 * *****************************************************************************
 */
package com.pekinsoft.loadmaster.tests;

import com.pekinsoft.loadmaster.operators.SettingsOperator;
import com.pekinsoft.loadmaster.operators.BookNewLoadWizardOperator;
import com.pekinsoft.loadmaster.Starter;
import com.pekinsoft.loadmaster.operators.AboutOperator;
import com.pekinsoft.loadmaster.operators.MainWindowOperator;
import org.junit.AfterClass;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Very basic test of the main application window.
 *
 * @author Jiří Kovalský
 */
public class MainWindowTest {

    private static MainWindowOperator mainWindowOperator;

    @BeforeClass
    public static void setUpClass() {
        Starter.main(new String[]{});
        mainWindowOperator = MainWindowOperator.getDefault();
    }

    @AfterClass
    public static void tearDownClass() {
        Tools.sleep(3000);
        mainWindowOperator.callExit();
        Tools.pushButtonInDialog("Confirm Close", "Yes");
    }

    //@Test
    public void testBookNewLoadWizard() {
        mainWindowOperator.callBookNewLoad();

        BookNewLoadWizardOperator bookNewLoadWizardOperator = new BookNewLoadWizardOperator();
        assertEquals(bookNewLoadWizardOperator.getPageName(), "Load Information");
        bookNewLoadWizardOperator.setOrderNumber("10");
        bookNewLoadWizardOperator.setTripNumber("20");
        bookNewLoadWizardOperator.setGrossPay("30");
        bookNewLoadWizardOperator.setMilesNumber("40");
        bookNewLoadWizardOperator.setCommodity("Bee hives");
        bookNewLoadWizardOperator.checkOption("Tarped", true);
        bookNewLoadWizardOperator.goNext();

        assertEquals(bookNewLoadWizardOperator.getPageName(), "Broker Information");
        bookNewLoadWizardOperator.cancel();
        Tools.pushButtonInDialog("Confirm Cancellation", "Yes");
    }

    @Test
    public void testLoadSettings() {
        mainWindowOperator.callLoadMasterSettings();

        SettingsOperator loadMasterSettingsOperator = new SettingsOperator();
        loadMasterSettingsOperator.switchToAccountingSettings();
        loadMasterSettingsOperator.switchToDirectoriesSettings();
        loadMasterSettingsOperator.switchToCompanySettings();
        loadMasterSettingsOperator.switchToGeneralSettings();
        loadMasterSettingsOperator.close();
    }

    @Test
    public void testAboutDialog() {
        mainWindowOperator.callAbout();

        AboutOperator aboutOperator = new AboutOperator();
        assertTrue(aboutOperator.projectDescriptionContains("Project is the culmination of a bunch of different"));
        aboutOperator.close();
    }
}
