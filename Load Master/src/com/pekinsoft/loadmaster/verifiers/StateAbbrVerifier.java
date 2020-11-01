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
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 *
 * @author Sean Carrick &lt;IntegrityTrucking at outlook dot com&gt;
 * @version 0.1.0
 * @since 0.1.0
 */
public class StateAbbrVerifier extends InputVerifier {

    private final Color errFore = Color.YELLOW;
    private final Color errBack = Color.RED;
    private final Color fore = SystemColor.textText;
    private final Color back = SystemColor.text;
    private final Color ctl = SystemColor.control;

    @Override
    public boolean verify(JComponent input) {
        // In order to validate the input, we need to first check that the
        //+ parameter is an instance of JTextField.
        String abbr = new String();
        if (input instanceof JTextField) {
            abbr = ((JTextField) input).getText();
        }

        if (abbr != null && !abbr.isBlank() && !abbr.isEmpty()) {
            if (!Utils.createStateAbbreviations().contains(abbr)) {
                ((JTextField) input).setBackground(errBack);
                ((JTextField) input).setForeground(errFore);
            } else {
                ((JTextField) input).setBackground(back);
                ((JTextField) input).setForeground(fore);
            }

            return Utils.createStateAbbreviations().contains(abbr);
        } else {
            return true;
        }
    }

}
