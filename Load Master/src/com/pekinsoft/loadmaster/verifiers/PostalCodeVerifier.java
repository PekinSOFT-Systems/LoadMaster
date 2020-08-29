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

import com.pekinsoft.loadmaster.view.Customers;
import java.awt.Color;
import java.awt.SystemColor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.InputVerifier;
import javax.swing.JComponent;
import javax.swing.JTextField;

/**
 *
 * @author Sean Carrick &lt;IntegrityTrucking at outlook dot com&gt;
 * @version 0.1.0
 * @since 0.1.0
 */
public class PostalCodeVerifier extends InputVerifier {
    private final Color errFore = Color.YELLOW;
    private final Color errBack = Color.RED;
    private final Color fore = SystemColor.textText;
    private final Color back = SystemColor.text;
    private final Color ctl = SystemColor.control;
    
    /**
     * This method checks the provided Zip Code (or Postal Code) and validates
     * that the provided data is either a U.S. Zip Code or a Canadian Postal
     * Code.
     * <p>
     * The validation routine uses regular expressions to disqualify characters
     * that are not included in valid Zip and Postal Codes. U.S. Zip Codes may
     * be entered either with or without the Plus-4 part of the Zip Code and pass
     * validation. Canadian Postal Codes must be of the format A0A0A0, or A0A 
     * 0A0. <strong>All</strong> Canadian Postal Codes are of the letter-number-letter
     * number-letter-number format. The first letter, in the first section,
     * designates the Province in which that Postal Code is located, i.e.,
     * 'Y' for Yukon or 'M' for Manitoba, etc.</p>
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
        String zipCode = new String();
        
        // Make sure that the JComponent parameter is a JTextField.
        if ( input instanceof JTextField ) {
            // Since it is, get the text from the object.
            zipCode = ((JTextField) input).getText();
        }
        
        // Create our return variable and default it to invalid.
        boolean isValid = false;

        if ( zipCode != null && !zipCode.isEmpty() && !zipCode.isBlank() ) {
            // Create our regex strings.
            String regEx = "^(\\d{5}(-\\d{4})?|[A-CEGHJ-NPRSTVXY]\\d[A-CEGHJ-NPRSTV-Z]";
            regEx += " ?\\d[A-CEGHJ-NPRSTV-Z]\\d)$";

            // Next, we need a Pattern object to place this regex in.
            Pattern pattern = Pattern.compile(regEx);

            // We will also need a matcher object to see if the string is valid.
            Matcher matcher = pattern.matcher(zipCode);

            // Check the validity of the supplied Zip/Postal Code.
            isValid = matcher.matches();

            if ( isValid ) {
                ((JTextField) input).setBackground(back);
                ((JTextField) input).setForeground(fore);
                Customers.helpPanel.setBackground(ctl);
                Customers.helpLabel.setText("");
            } else {
                ((JTextField) input).setBackground(errBack);
                ((JTextField) input).setForeground(errFore);
                Customers.helpPanel.setBackground(Color.RED);
                Customers.helpLabel.setText("Zip Code is required and must be a "
                        + "valid US Zip Code or Canadian Postal Code.");
            }
        } else 
            isValid = true;
        
        // Return our findings.
        return isValid;
    }
    
}
