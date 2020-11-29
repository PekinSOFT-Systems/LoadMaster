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

import java.awt.Color;
import java.awt.SystemColor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Sean Carrick &lt;IntegrityTrucking at outlook dot com&gt;
 * @version 0.1.0
 * @since 0.1.0
 */
public class PostalCodeVerifier extends InputVerifier {

    private final Color errFore = new Color(0.26f, 0.012f, 0.012f);
    private final Color errBack = new Color(1.0f, 0.752f, 0.752f);
    private final Color fore = SystemColor.textText;
    private final Color back = SystemColor.text;
    private final Color ctl = SystemColor.control;
    private JPanel helpPanel;
    private JLabel helpLabel;

    public PostalCodeVerifier(JPanel helpPanel, JLabel helpLabel) {
        super();
        this.helpPanel = helpPanel;
        this.helpLabel = helpLabel;
    }

    /**
     * This method checks the provided Zip Code (or Postal Code) and validates
     * that the provided data is either a U.S. Zip Code or a Canadian Postal
     * Code.
     * <p>
     * The validation routine uses regular expressions to disqualify characters
     * that are not included in valid Zip and Postal Codes. U.S. Zip Codes may
     * be entered either with or without the Plus-4 part of the Zip Code and
     * pass validation. Canadian Postal Codes must be of the format A0A0A0, or
     * A0A 0A0. <strong>All</strong> Canadian Postal Codes are of the
     * letter-number-letter number-letter-number format. The first letter, in
     * the first section, designates the Province in which that Postal Code is
     * located, i.e., 'Y' for Yukon or 'M' for Manitoba, etc.</p>
     * <p>
     * The current version of the {@code isValidPostalCode} method does not
     * validate the Postal Code to the Province, nor the Zip Code to the State.
     * However, this method could be updated in the future to provide such
     * validation.
     *
     * @param input The object that contains the postal code to validate.
     * @return Whether the code is valid or not.
     */
    @Override
    public boolean verify(JComponent input) {
        JTextComponent zipCodeComponent = (JTextComponent) input;
        String zipCode = zipCodeComponent.getText();

        if (!zipCode.isEmpty() && !zipCode.isBlank() && !zipCode.equalsIgnoreCase("unavailable")) {
            // Create our regex strings.
            String regEx = "^(\\d{5}(-\\d{4})?|[A-CEGHJ-NPRSTVXY]\\d[A-CEGHJ-NPRSTV-Z]";
            regEx += " ?\\d[A-CEGHJ-NPRSTV-Z]\\d)$";

            // Next, we need a Pattern object to place this regex in.
            Pattern pattern = Pattern.compile(regEx);

            // We will also need a matcher object to see if the string is valid.
            Matcher matcher = pattern.matcher(zipCode);

            // Check the validity of the supplied Zip/Postal Code.
            if (matcher.matches()) {
                zipCodeComponent.setBackground(back);
                zipCodeComponent.setForeground(fore);
                helpPanel.setBackground(ctl);
                helpLabel.setText("");
                return true;
            }
        }
        zipCodeComponent.setBackground(errBack);
        zipCodeComponent.setForeground(errFore);
        helpPanel.setBackground(errBack);
        helpLabel.setText("<html>Zip Code is a  <strong><em>required </em></strong> field and must be a valid US Zip Code or Canadian Postal Code, which \"" + zipCodeComponent.getText() + "\" is not.");
        return false;
    }

    @Override
    public boolean shouldYieldFocus(JComponent source, JComponent target) {
        return super.shouldYieldFocus(source, target);
    }

}
