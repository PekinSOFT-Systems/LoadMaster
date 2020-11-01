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
 *  Class      :   MainWindowOperator.java
 *  Author     :   Jiří Kovalský
 *  Created    :   Oct 17, 2020 @ 10:50:25 PM
 *  Modified   :   Oct 23, 2020
 *  
 *  Purpose: Jemmy operator for main application window.
 *  
 *  Revision History:
 *  
 *  WHEN          BY                  REASON
 *  ------------  ------------------- ------------------------------------------
 *  Oct 18, 2020  Jiří Kovalský       Initial creation.
 * *****************************************************************************
 */
package com.pekinsoft.loadmaster.operators;

import com.pekinsoft.loadmaster.tests.Tools;
import java.awt.Component;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.jdesktop.swingx.JXTaskPane;
import org.netbeans.jemmy.ComponentChooser;
import org.netbeans.jemmy.operators.JComponentOperator;
import org.netbeans.jemmy.operators.JFrameOperator;

/**
 * Jemmy operator for main application window.
 *
 * @author Jiří Kovalský
 */
public class MainWindowOperator extends JFrameOperator {

    private MainWindowOperator(String title) {
        super(title);
    }

    /**
     * Returns operator for main application window.
     *
     * @return Default operator for main application window.
     */
    public static MainWindowOperator getDefault() {
        String title = Tools.getApplicationTitle();
        MainWindowOperator mainWindowOperator = new MainWindowOperator(title);
        return mainWindowOperator;
    }

    /**
     * Invokes action at specified location from given toolbar category.
     *
     * @param category Name of toolbar section to invoke required action from.
     * @param actionPath Array with indexes of components in the structure tree
     * to get to the desired action.
     */
    private void invokeAction(String category, int[] actionPath) {
        JComponent component = (JXTaskPane) findSubComponent(new JXTaskPaneChooser(category));
        for (int i = 0; i < actionPath.length; i++) {
            component = (JComponent) component.getComponent(actionPath[i]);
        }
        JComponentOperator operator = new JComponentOperator(component);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                operator.clickMouse();
            }
        });
    }

    /**
     * Invokes Load Master Settings... action from the application toolbar.
     */
    public void callLoadMasterSettings() {
        invokeAction("Load Master System", new int[]{0, 0, 0, 0});
    }

    /**
     * Invokes About Load Master... action from the application toolbar.
     */
    public void callAbout() {
        invokeAction("Load Master System", new int[]{0, 0, 0, 1});
    }

    /**
     * Invokes Exit Master Settings... action from the application toolbar.
     */
    public void callExit() {
        invokeAction("Load Master System", new int[]{0, 0, 0, 6});
    }

    /**
     * Invokes Book New Load... action from the application toolbar.
     */
    public void callBookNewLoad() {
        invokeAction("Loads", new int[]{0, 0, 0, 0});
    }

    /**
     * Class to choose panel from the application toolbar with required actions
     * by its title.
     */
    private static class JXTaskPaneChooser implements ComponentChooser {

        private final String title;

        public JXTaskPaneChooser(String title) {
            this.title = title;
        }

        @Override
        public boolean checkComponent(Component component) {
            if (component instanceof JXTaskPane) {
                JXTaskPane pane = (JXTaskPane) component;
                if (pane.getTitle().equals(title)) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String getDescription() {
            return "JXTaskPane component chooser";
        }
    }
}
