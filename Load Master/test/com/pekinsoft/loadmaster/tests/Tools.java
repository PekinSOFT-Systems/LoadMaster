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
 *  Class      :   Tools.java
 *  Author     :   Jiří Kovalský
 *  Created    :   Oct 18, 2020 @ 10:16:36 PM
 *  Modified   :   Oct 23, 2020
 *  
 *  Purpose: Auxiliary class for functional automated tests.
 *  
 *  Revision History:
 *  
 *  WHEN          BY                  REASON
 *  ------------  ------------------- ------------------------------------------
 *  Oct 18, 2020  Jiří Kovalský       Initial creation.
 * *****************************************************************************
 */
package com.pekinsoft.loadmaster.tests;

import com.pekinsoft.loadmaster.Starter;
import org.netbeans.jemmy.operators.JButtonOperator;
import org.netbeans.jemmy.operators.JDialogOperator;

/**
 * Auxiliary class for functional automated tests.
 *
 * @author Jiří Kovalský
 */
public class Tools {

    /**
     * Suspends execution of current thread for given time.
     *
     * @param millis Time in milliseconds to wait before next step.
     */
    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {
            System.out.println("Waiting for " + millis + " milliseconds interrupted.");
        }
    }

    /**
     * Returns title of the application.
     *
     * @return Title of the application after its startup.
     */
    public static String getApplicationTitle() {
        return Starter.props.getProjectName() + " - Current Trip: " + Starter.props.getProperty("load.current", "No Active Load");
    }

    /**
     * Clicks button with given name in the dialog with given title.
     *
     * @param dialogTitle Title of the dialog to be found.
     * @param buttonName Name of the button to be clicked.
     */
    public static void pushButtonInDialog(String dialogTitle, String buttonName) {
        JDialogOperator dialog = new JDialogOperator(dialogTitle);
        JButtonOperator button = new JButtonOperator(dialog, buttonName);
        button.pushNoBlock();
    }

}
