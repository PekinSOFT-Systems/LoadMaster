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
 *  Class      :   AboutOperator.java
 *  Author     :   Jiří Kovalský
 *  Created    :   Oct 18, 2020 @ 09:51:02 PM
 *  Modified   :   Oct 21, 2020
 *  
 *  Purpose: Jemmy operator for About Load Master dialog.
 *  
 *  Revision History:
 *  
 *  WHEN          BY                  REASON
 *  ------------  ------------------- ------------------------------------------
 *  Oct 21, 2020  Jiří Kovalský       Initial creation.
 * *****************************************************************************
 */
package com.pekinsoft.loadmaster.operators;

import com.pekinsoft.loadmaster.Starter;
import org.netbeans.jemmy.operators.JDialogOperator;
import org.netbeans.jemmy.operators.JEditorPaneOperator;

/**
 * Jemmy operator for About Load Master project dialog.
 *
 * @author Jiří Kovalský
 */
public class AboutOperator extends JDialogOperator {

    public AboutOperator() {
        super("About " + Starter.props.getProjectName());
    }

    /**
     * Method testing whether project description contains given text.
     *
     * @param text Text to be searched for in the project description.
     * @return Returns true if given text is included in the project description
     * and false otherwise.
     */
    public boolean projectDescriptionContains(String text) {
        JEditorPaneOperator projectDescription = new JEditorPaneOperator(this);
        return projectDescription.getText().contains(text);
    }
}
