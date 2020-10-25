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
 * Project    :   Northwind-Basic
 * Class      :   StringUtils.java
 * Author     :   Sean Carrick
 * Created    :   Mar 8, 2020 @ 12:34:09 PM
 * Modified   :   Mar 8, 2020
 *  
 * Purpose:
 *  
 * Revision History:
 *  
 * WHEN          BY                  REASON
 * ------------  ------------------- ------------------------------------------
 * Mar 08, 2020  Sean Carrick        Initial creation.
 * Oct 25, 2020  Sean Carrick        Added the method getMiddle() to allow users
 *                                   to get the substring between two given
 *                                   characters, exclusively. The given
 *                                   characters will not be returned as part of
 *                                   the resulting substring.
 * 
 * *****************************************************************************
 */

package com.pekinsoft.loadmaster.utils;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.1.0
 * @since 0.1.0
 */
public class StringUtils {
    //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
    private StringUtils () {
        // Privatized to prevent this class from being instantiated.
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Static Methods and Functions">
    /**
     * Abbreviates a String using ellipses. This will turn "Now is the time for
     * all good men" into "Now is the time for...".
     * 
     * Specifically:
     *  * If the number of characters in `source` is less than or equal to
     *    `maxLength, return `source`.
     *  * Else abbreviate it to (`substring(source, maxLangth - 3) + "..."`).
     *  * If `width` is less than four, throw and `IllegalArgumentException`.
     *  * In no case will it return a String of length greater than `width`.
     * 
     * ```java
     * StringUtils.abbreviate(null, *)          = null
     * StringUtils.abbreviate("", 4)            = ""
     * StringUtils.abbreviate(abcdefg, 6)       = "abc..."
     * StringUtils.abbreviate("abcdefg", 7)     = "abcdefg"
     * StringUtils.abbreviate("abcdefg", 8)     = "abcdefg"
     * StringUtils.abbreviate("abcdefg", 4)     = "a..."
     * StringUtils.abbreviate("abcdefg", 3)     = IllegalArgumentException
     * 
     * @param source the String to check, may be null
     * @param width maximum length of result String, must be at least 4
     * @return abbreviated String, null if null String input
     */
    public static String abbreviate(String source, int width)
            throws IllegalArgumentException {
        if ( source == null ) {
            return null;
        }
        if ( width < 4 ) {
            throw new IllegalArgumentException("maxLength must be greater than "
                    + "four (4).");
        }
        if ( source.length() <= width ) {
            return source;
        }
        
        return source.substring(0, width - 3) + "...";
    }
    
    /**
     * Unlike `abbreviate`, this method will allow you to wrap the `String` at a
     * given width. This is accomplished by placing a newline character into the
     * string, `source`, at the given `width` position. Once the newline 
     * character is inserted, the portion of the string prior to the newline is
     * dropped out of the source string, and the process repeats until the 
     * source string fits within the given width.
     * 
     * @param   source  the source string to wrap.
     * @param   width   the width at which the source string should be wrapped.
     * @return  the source string wrapped at the given width using newline 
     *          characters.
     */
    public static String wrapAt(String source, int width) {
        StringBuilder sb = new StringBuilder();
        int current = 0;
        
        while ( source.length() > width ) {
            sb.append(source.substring(0, width)).append("\n");
            source = source.substring(width);
        }
        sb.append(source);
        
        return sb.toString();
    }
    
    /**
     * Deletes all whitespaces from a String as defined by 
     * `Character.isWhitespace(char)`.
     * 
     * ```java
     * StringUtils.deleteWhitespace(null)               = null
     * StringUtils.deleteWhitespace("")                 = ""
     * StringUtils.deleteWhitespace("abc")              = "abc"
     * StringUtils.deleteWhitespace("   ab  c  ")       = "abc"
     * 
     * @param source the String to delete whitespace from, may be null
     * @return String without whitespaces, null if null String input
     */
    public static String deleteWhitespace(String source) {
        if ( source == null ) {
            return null;
        }
        
        char[] chars = source.toCharArray();
        source = "";
        
        for ( char c : chars ) {
            if ( !Character.isWhitespace(c) ) {
                source += c;
            }
        }
        
        return source;
    }
    
    /**
     * Removes a substring only if it is at the beginning of a source string, 
     * otherwise returns the source string. A `null` source string will return
     * `null`. An empty ("") source string will return the empty string. A `null`
     * search string will return the source string. This method is 
     * case-sensitive.
     * 
     * @param source    the string from which the substring should be removed.
     * @param remove    the substring to remove from the source string.
     * @return java.lang.String the source string with the substring removed,
     *                          except as provided for above.
     */
    public static String removeStart(String source, String remove) {
        if ( source == null ) {
            return null;
        } else if ( source.isBlank() ) {
            return source;
        }
        if ( remove == null ) {
            return source;
        }
        
        String holder = "";
        
        if ( source.startsWith(remove) ) {
            holder = source.substring(remove.length());
        }
        
        return holder;
    }
    
    /**
     * Removes a substring only if it is at the beginning of a source string, 
     * otherwise returns the source string. A `null` source string will return
     * `null`. An empty ("") source string will return the empty string. A `null`
     * search string will return the source string. This method is 
     * case-insensitive.
     * 
     * @param source    the string from which the substring should be removed.
     * @param remove    the substring to remove from the source string.
     * @return java.lang.String the source string with the substring removed,
     *                          except as provided for above.
     */
    public static String removeStartIgnoreCase(String source, String remove) {
        if ( source == null ) {
            return null;
        } else if ( source.isBlank() ) {
            return source;
        }
        if ( remove == null ) {
            return source;
        }
        
        String holder = "";
        
        if ( source.toLowerCase().startsWith(remove.toLowerCase()) ) {
            holder = source.substring(remove.length());
        }
        
        return holder;
    }
    
    /**
     * Returns a substring pulled from the source from the start to the 
     * end, exclusive. The substring that is returned from the middle of
     * the source string will begin at the first character **after** the 
     * character provided as the start, and end **before** the character
     * provided as the end. The start and end characters ***will not*** be a 
     * part of the returned substring.
     * 
     * @param source the source String to search, may be null
     * @param start the character or string after which the substring will start
     * @param end  the character or string before which the substring will end
     * @return the substring between the start and end values, if found, `null`
     *              if `null` String provided as source, the source string if
     */
    public static String getMiddle(String source, String start, String end) {
        if ( source == null ) {
            return null;
        }
        if ( start == null && end == null ) {
            return source;
        }
        if ( source.isBlank() || source.isEmpty() ) {
            return source;
        }
        
        if ( source.contains(start) && source.contains(end) ) {
            return source.substring(source.indexOf(start) + 1, 
                    source.indexOf(end) - 1);
        } else if ( source.contains(start) && (!source.contains(end)
                || end == null || end.isBlank() || end.isEmpty()) ) {
            return source.substring(source.indexOf(start) + 1);
        } else {
            return source;
        }
    }
    
    /**
     * Removes a substring only if it is at the end of a source string, otherwise
     * returns the source string.A `null` source string will return `null`.An 
     * empty ("") source string will return the empty string.A `null` search 
     * string will return the source string.
     * 
     * 
     * @param source the source String to search, may be null
     * @param beginIndex the beginning index of the substring to retrieve
     * @param endIndex the ending index of the substring to remove
     * @return the substring with the string removed, if found, `null` if `null`
     *              String input
     */
    public static String removeEnd(String source, int beginIndex, int endIndex) {
        if ( source == null ) {
            return null;
        }
        if ( beginIndex == endIndex ) {
            return null;
        }
        if ( source.isBlank() || source.isEmpty() ) {
            return source;
        }
        
        return source.substring(beginIndex, endIndex);
    }
    
    /**
     * Pads the provided `String` to the left side of the field of the given
     * field width. This method is guaranteed to never return a value longer
     * than the given `fieldWidth`. If the provided value is already longer than
     * the `fieldWidth` value, then an abbreviated `String` is returned.
     * 
     * @param toPad         the `String` to left-align in the field.
     * @param fieldWidth    the width of the field
     * @return              `String` containing the `toPad` value padded on the
     *                      right end with spaces to bring the length of the
     *                      value to the width of the field. `String` of `width`
     *                      number of spaces if an empty `String` is provided.
     * @throws IllegalArgumentException in the event `toPad` is `null`, or 
     *          `fieldWidth` is less than four.
     */
    public static String padLeft(String toPad, int fieldWidth) {
        if ( toPad == null || fieldWidth < 4 ) {
            throw new IllegalArgumentException("Values are no good");
        } else if ( toPad.isBlank() ) {
            return " ".repeat(fieldWidth);
        }
        
        if ( toPad.length() > fieldWidth ) {
            return abbreviate(toPad, fieldWidth);
        } else {
            return toPad + " ".repeat(fieldWidth - toPad.length());
        }
    }
    
    /**
     * Pads the provided `String` to the right side of the field of the given
     * field width. This method is guaranteed to never return a value longer
     * than the given `fieldWidth`. If the provided value is already longer than
     * the `fieldWidth` value, then an abbreviated `String` is returned.
     * 
     * @param toPad         the `String` to right-align in the field.
     * @param fieldWidth    the width of the field
     * @return              `String` containing the `toPad` value padded on the
     *                      left end with spaces to bring the length of the
     *                      value to the width of the field. `String` of `width`
     *                      number of spaces if an empty `String` is provided.
     * @throws IllegalArgumentException in the event `toPad` is `null`, or 
     *          `fieldWidth` is less than four.
     */
    public static String padRight(String toPad, int fieldWidth) {
        if ( toPad == null || fieldWidth < 4 ) {
            throw new IllegalArgumentException("Values are no good");
        } else if ( toPad.isBlank() ) {
            return " ".repeat(fieldWidth);
        }
        
        if ( toPad.length() > fieldWidth ) {
            return abbreviate(toPad, fieldWidth);
        } else {
            return " ".repeat(fieldWidth - toPad.length()) + toPad;
        }
    }
    //</editor-fold>
    
}
