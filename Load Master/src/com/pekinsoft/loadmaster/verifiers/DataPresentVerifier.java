/*
 * Copyright (C) 2020 PekinSOFT™ Systems
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
 * 
 * Project :   Load Master™
 * Class   :   DataPresentVerifier
 * Author  :   Sean Carrick
 * Created :   Nov 24, 2020
 * Modified:   Nov 24, 2020
 * 
 * Purpose:
 *     [Provide a general purpose overview for this class]
 * 
 * WHEN          BY                  REASON
 * ------------  ------------------  -------------------------------------------
 * Nov 24, 2020  Sean Carrick        Initial creation.
 * 
 * *****************************************************************************
 */
package com.pekinsoft.loadmaster.verifiers;

import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

/**
 * The `DataPresentVerifier` is a generic `InputVerifier` for use on GUI fields
 * where data is required to be present, but is not something that can be truly
 * validated, such as a phone number, which can be validated for format, and
 * even as a valid, active phone number, if the application developer so
 * chooses.
 *
 * This verifier should be used on fields, such as last name, first name, or
 * company name, where data is required to be present, but cannot in any other
 * way be validated.
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 *
 * @version 0.1.0
 * @since 0.1.0
 */
public class DataPresentVerifier extends InputVerifier {

    private final Color errFore = new Color(0.26f, 0.012f, 0.012f);
    private final Color errBack = new Color(1.0f, 0.752f, 0.752f);
    private final Color fore = SystemColor.textText;
    private final Color back = SystemColor.text;
    private final Color ctl = SystemColor.control;
    private JPanel helpPanel;
    private JLabel helpLabel;

    public DataPresentVerifier(JPanel helpPanel, JLabel helpLabel) {
        super();
        this.helpPanel = helpPanel;
        this.helpLabel = helpLabel;
    }

    /**
     * This verifies that some sort of data is present. The data may not be able
     * to be validated in any other way, except to tell if there is data present
     * in the provided field.
     *
     * @param input the component to be verified
     * @return `true` if data is present; `false` otherwise
     */
    @Override
    public boolean verify(JComponent input) {
        JTextComponent textInput = (JTextComponent) input;

        if (textInput.getText().isBlank() || textInput.getText().isEmpty()) {
            textInput.setBackground(errBack);
            textInput.setForeground(errFore);
            helpPanel.setBackground(errBack);
            helpLabel.setText("<html>This is a <strong><em>required </em></strong> field, so something needs to be entered.");
            return false;
        }
        textInput.setBackground(back);
        textInput.setForeground(fore);
        helpPanel.setBackground(ctl);
        helpLabel.setText("");
        return true;
    }

}
