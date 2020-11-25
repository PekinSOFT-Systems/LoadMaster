/*
 * Copyright (C) 2019 Integrity Solutions
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
 */
package com.pekinsoft.loadmaster.verifiers;

import com.pekinsoft.loadmaster.utils.Utils;
import com.pekinsoft.loadmaster.view.Customers;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Sean Carrick &lt;IntegrityTrucking at outlook dot com&gt;
 * @version 0.1.0
 * @since 0.1.0
 */
public class StateAbbrVerifier extends InputVerifier {

    private final Color errFore = new Color(0.26f, 0.012f, 0.012f);
    private final Color errBack = new Color(1.0f, 0.752f, 0.752f);
    private final Color fore = SystemColor.textText;
    private final Color back = SystemColor.text;
    private final Color ctl = SystemColor.control;

    @Override
    public boolean verify(JComponent input) {
        boolean isValid;

        isValid = ((JTextComponent) input) != null
                && ((JTextComponent) input).getText().length() > 0;

        if (isValid) {
            isValid = Utils.createStateAbbreviations().contains(
                    ((JTextComponent) input).getText());
        }

        if (!isValid) {
            ((JTextComponent) input).setBackground(errBack);
            ((JTextComponent) input).setForeground(errFore);
            Customers.helpPanel.setBackground(ctl);
            Customers.helpLabel.setText("");
        } else {
            ((JTextComponent) input).setBackground(errBack);
            ((JTextComponent) input).setForeground(errFore);
            Customers.helpPanel.setBackground(errBack);
            Customers.helpLabel.setText("<html>Zip Code is a  <strong><em>required"
                    + "</em></strong> field and must be a valid US Zip Code or "
                    + "Canadian Postal Code, which \""
                    + ((JTextComponent) input).getText() + "\" is not.");
        }

        return isValid;
    }

}
