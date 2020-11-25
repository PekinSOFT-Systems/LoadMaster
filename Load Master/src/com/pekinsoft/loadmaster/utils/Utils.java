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
 *  Project    :   Northwind-Basic
 *  Class      :   Utils.java
 *  Author     :   Sean Carrick
 *  Created    :   Mar 8, 2020 @ 12:32:47 PM
 *  Modified   :   Mar 8, 2020
 *  
 *  Purpose:
 *      Provides useful string-related methods.
 *  
 *  Revision History:
 *  
 *  WHEN          BY                  REASON
 *  ------------  ------------------- ------------------------------------------
 *  Mar 08, 2020  Sean Carrick        Initial creation.
 *  Mar 21, 2020  Jiri Kovalsky       Added the getCenterPoint function.
 *  Mar 21, 2020  Sean Carrick        Moved getCenterPoint function into code 
 *                                    fold for `public static methods and 
 *                                    functions`.
 *  Nov 24, 2020  Sean Carrick        Added the following methods:
 *                                      - getApplicationFolderByOS
 *                                      - getApplicationDataFolderByOS
 *                                      - getApplicationSettingsLocationByOS
 *                                      - getSystemLogLocationByOS
 * *****************************************************************************
 */

package com.pekinsoft.loadmaster.utils;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.2.15
 * @since 0.1.0
 */
public class Utils {
    //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
    private Utils () {
        // Privatized in order to prohibit this class from being instantiated.
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Private Static Methods and Functions">
    /**
     * Gets the name of the program's JAR file.
     * 
     * @return java.lang.String program's JAR file name
     */
    private static String getJarName()
    {
        return new File(Utils.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath())
                .getName();
    }

    /**
     * Tests to see if the project is currently executing from within a JAR file
     * or from a folder structure. This is useful for testing if the program is
     * running inside or outside the IDE.
     * 
     * @return boolean true if running inside a JAR file; false if running in
     *                  the IDE
     */
    private static boolean runningFromJAR()
    {
        String jarName = getJarName();
        return jarName.contains(".jar");
    }
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Public Static Methods and Functions">
    /**
     * Gets the operating system (OS) specific location of where to store any
     * application data files. Each OS has a different location where these
     * files should be stored, and this application will follow those standards.
     * 
     * The typical location for data files on each OS are:
     * 
     * | Operating System | Application Data Location |
     * | ---------------- | ------------------------- |
     * | Microsoft Windows™ | {user.home}\\AppData\\LoadMaster\\ |
     * | Apple Mac OS-X™ | {user.home}/Library/Application Data/LoadMaster/ |
     * | Linux and Solaris™ | {user.home}/.loadmaster/* |
     * 
     * <dl><dt>Developer's Note</dt><dd>Notice the difference between the Linux
     * and Solaris location of the application settings file and the information
     * regarding the location of the application folder in Linux and Solaris. 
     * this one has the same name, but this one **does not** end with a slash (/) 
     * character. That is the only difference between the configuration file and  
     * the application data folder.</dd></dl>
     * 
     * @return the OS-specific storage location.
     */
    public static String getApplicationFolderByOS() {
        String osName = System.getProperty("os.name").toLowerCase();
        String userHome = System.getProperty("user.home");
        String location = "";
        
        if ( osName.contains("win") ) {
            location = "AppData\\LoadMaster";
        } else if ( osName.contains("mac") ) {
            location = "Library/Preferences/LoadMaster";
        } else {
            location = ".loadmaster";
        }
        
        return userHome + File.separator + location + File.separator;
    } 
    
        /**
     * Gets the operating system (OS) specific location of where to store any
     * application data files. Each OS has a different location where these 
     * files get stored, and this application will follow those standards.
     * 
     * The typical location for data files on each OS are:
     * 
     * | Operating System | Application Data Location |
     * | ---------------- | ------------------------- |
     * | Microsoft Windows™ | {user.home}\\AppData\\LoadMaster\\data\\ |
     * | Apple Mac OS-X™ | {user.home}/Library/Application Data/LoadMaster/data/ |
     * | Linux and Solaris™ | {user.home}/.loadmaster/data/ |
     * 
     * @return the OS-specific storage location.
     */
    public static String getApplicationDataFolderByOS() {
        String osName = System.getProperty("os.name").toLowerCase();
        String appFolder = getApplicationDataFolderByOS();
        
        if ( osName.contains("mac") ) {
            appFolder = System.getProperty("user.home") + "/Library/Application"
                    + " Data/LoadMaster";
        }
        
        return appFolder + "data" + File.separator;
    }
    
    /**
     * Gets the operating system (OS) specific location of where to store any
     * application settings files. Each OS has a different location where these
     * file get stored, and this method allows applications to follow those
     * well-established standards.
     * | Operating System | Application Data Location |
     * | ---------------- | ------------------------- |
     * | Microsoft Windows™ | {user.home}\\AppData\\LoadMaster\\LoadMaster.cnf |
     * | Apple Mac OS-X™ | {user.home}/Library/Preferences/LoadMaster/LoadMaster.pref |
     * | Linux and Solaris™ | {user.home}/.loadmaster* |
     * 
     * <dl><dt>Developer's Note</dt><dd>Notice the difference between the Linux
     * and Solaris location of the application settings file and the information
     * regarding the location of the application folder in Linux and Solaris. 
     * this one has the same name, but this one **does not** end with a slash (/) 
     * character. That is the only difference between the configuration file and  
     * the application data folder.</dd></dl>
     * 
     * @return the OS-specific storage location.
     */
    public static String getApplicationSettingsLocationByOS() {
        String osName = System.getProperty("os.name").toLowerCase();
        String settingsLocationFolder = getApplicationFolderByOS();
        
        if ( osName.contains("win") ) {
            settingsLocationFolder = settingsLocationFolder + "LoadMaster.cnf";
        } else if ( osName.contains("mac") ) {
            settingsLocationFolder = settingsLocationFolder + "LoadMaster.pref";
        } else {
            settingsLocationFolder = settingsLocationFolder + "loadmaster.conf";
        }
        
        return settingsLocationFolder;
    }
    
    /**
     * Gets the operating system (OS) specific location of where to store any
     * application log files. Each OS has a different location where these
     * file get stored, and this method allows applications to follow those
     * well-established standards.
     * | Operating System | Application Data Location |
     * | ---------------- | ------------------------- |
     * | Microsoft Windows™ | %SystemRoot%\System32\Config\LoadMaster\loadmaster.evt |
     * | Apple Mac OS-X™ | /Library/Logs/LoadMaster/application.log |
     * | Linux and Solaris™ | /var/log/loadmaster/application.log |
     * 
     * <dl><dt>Developer's Note</dt><dd>On Linux and Solaris, we may run into an
     * issue with storing our logs in the `/var/log/` folder due to user security
     * permissions. We will need to look into this as we develop the LoadMaster
     * Project, as we may need to create a dummy user that is a member of the
     * appropriate system group to have write access to the `/var/log/` folder.
     * </dd></dl>
     * 
     * @return the OS-specific storage location.
     */
    public static String getSystemLogLocationByOS() {
        String osName = System.getProperty("os.name").toLowerCase();
        String logLocation = "";
        
        if ( osName.contains("win") ) {
            logLocation = "%SystemRoot%\\System32\\Config\\LoadMaster\\loadmaster.evt";
        } else if ( osName.contains("mac") ) {
            logLocation = "/Library/Logs/LoadMaster/application.log";
        } else {
            logLocation = "/var/log/loadmaster/application.log";
        }
        
        return logLocation;
    }
    
    /**
     * Retrieves the currently executing program's program directory. This 
     * should be the directory in which the program was executed, which could
     * also be considered the program's installation path.
     * 
     * @return java.lang.String the directory from which the program is running
     */
    public static String getProgramDirectory()
    {
        if (runningFromJAR())
        {
            return getCurrentJARDirectory();
        } else
        {
            return getCurrentProjectDirectory();
        }
    }

    /**
     * Retrieves the current project's project directory.
     * 
     * @return java.lang.String the project's directory
     */
    private static String getCurrentProjectDirectory()
    {
        return new File("").getAbsolutePath();
    }

    /**
     * Retrieves the JAR file's current directory location.
     * 
     * @return java.lang.String the directory in which the JAR file is located
     */
    private static String getCurrentJARDirectory()
    {
        try
        {
            return new File(Utils.class.getProtectionDomain()
                    .getCodeSource()
                    .getLocation()
                    .toURI()
                    .getPath())
                    .getParent();
        } catch (URISyntaxException exception)
        {
            exception.printStackTrace();
        }

        return "";
    }
    
    /**
     *  Calculates central position of the window within its container.
     * 
     * <dl>
     *  <dt>Contributed By</dt>
     *  <dd>Jiří Kovalský &lt;jiri dot kovalsky at centrum dot cz&gt;</dd>
     * </dl>
     * 
     * @param container Dimensions of parent container where window will be 
     *                  located.
     * @param window Dimensions of child window which will be displayed within 
     *               its parent container.
     * @return Location of top left corner of window to be displayed in the 
     *         center of its parent container.
     */
    public static Point getCenterPoint(Dimension container, Dimension window) {
        int x = container.width / 2;
        int y = container.height / 2;
        x = x - window.width / 2;
        y = y - window.height / 2;
        x = x < 0 ? 0 : x;
        y = y < 0 ? 0 : y;
        return new Point(x, y);
    }

    
    public static Set<String> createStateAbbreviations() {
        Set<String> states = new HashSet<>();
        
        // Add our state and province abbreviations into our set.
        states.add("AL"); // Alabama
        states.add("AK"); // Alaska
        states.add("AR"); // Arkansas
        states.add("AZ"); // Arizona
        states.add("CA"); // California
        states.add("CO"); // Colorado
        states.add("CT"); // Connecticut
        states.add("DC"); // Washington, D. C. (District of Columbia)
        states.add("DE"); // Delaware
        states.add("FL"); // Florida
        states.add("GA"); // Georgia
        states.add("HI"); // Hawaii
        states.add("IA"); // Iowa
        states.add("ID"); // Idaho
        states.add("IL"); // Illinois
        states.add("IN"); // Indiana
        states.add("KS"); // Kansas
        states.add("KY"); // Kentucky
        states.add("LA"); // Louisiana
        states.add("MA"); // Massachusetts
        states.add("MD"); // Maryland
        states.add("ME"); // Maine
        states.add("MI"); // Michigan
        states.add("MN"); // Minnesota
        states.add("MO"); // Missouri
        states.add("MS"); // Mississippi
        states.add("MT"); // Montana
        states.add("NC"); // North Carolina
        states.add("ND"); // North Dakota
        states.add("NE"); // Nebraska
        states.add("NH"); // New Hampshire
        states.add("NJ"); // New Jersey
        states.add("NM"); // New Mexico
        states.add("NV"); // Nevada
        states.add("NY"); // New York
        states.add("OH"); // Ohio
        states.add("OK"); // Oklahoma
        states.add("OR"); // Oregon
        states.add("PA"); // Pennsylvania
        states.add("RI"); // Rhode Island
        states.add("SC"); // South Carolina
        states.add("SD"); // South Dakota
        states.add("TN"); // Tennessee
        states.add("TX"); // Texas
        states.add("UT"); // Utah
        states.add("VA"); // Virginia
        states.add("VT"); // Vermont
        states.add("WA"); // Washington
        states.add("WI"); // Wisconsin
        states.add("WV"); // West Virginia
        states.add("WY"); // Wyoming
        states.add("AL"); // Alberta
        states.add("BC"); // British Columbia
        states.add("MB"); // Manitoba
        states.add("NB"); // New Brunswick
        states.add("NF"); // Newfoundland
        states.add("NS"); // Nova Scotia
        states.add("NT"); // Northwest Territories
        states.add("ON"); // Ontario
        states.add("PE"); // Prince Edward Island
        states.add("QC"); // Quebec
        states.add("SK"); // Saskatchewan
        states.add("YT"); // Yukon
        
        // Return the set of state/province abbreviations.
        return states;
    }
    
    /**
     * Retrieves the state or province name abbreviation for the given state or
     * province name.
     * 
     * @param state Name of the state or province to abbreviate.
     * @return      The state or province abbreviation.
     */
    public static String getStateAbbreviation(String state) {
        String st = null;
        
        switch (state.toLowerCase()) {
            case "alabama":
                st = "AL";
                break;
            case "alaska":
                st = "AK";
                break;
            case "arizona":
                st = "AZ";
                break;
            case "arkansas":
                st = "AR";
                break;
            case "california":
                st = "CA";
                break;
            case "colorado":
                st = "CO";
                break;
            case "connecticut":
                st = "CT";
                break;
            case "delaware":
                st = "DE";
                break;
            case "florida":
                st = "FL";
                break;
            case "georgia":
                st = "GA";
                break;
            case "hawaii":
                st = "HI";
                break;
            case "idaho":
                st = "ID";
                break;
            case "illinois":
                st = "IL";
                break;
            case "indiana":
                st = "IN";
                break;
            case "iowa":
                st = "IA";
                break;
            case "kansas":
                st = "KS";
                break;
            case "kentucky":
                st = "KY";
                break;
            case "louisiana":
                st = "LA";
                break;
            case "maine":
                st = "ME";
                break;
            case "maryland":
                st = "MD";
                break;
            case "massachusetts":
                st = "MA";
                break;
            case "michigan":
                st = "MI";
                break;
            case "minnesota":
                st = "MN";
                break;
            case "mississippi":
                st = "MS";
                break;
            case "missouri":
                st = "MO";
                break;
            case "montana":
                st = "MT";
                break;
            case "nebraska":
                st = "NE";
                break;
            case "nevada":
                st = "NV";
                break;
            case "new hampshire":
                st = "NH";
                break;
            case "new jersey":
                st = "NJ";
                break;
            case "new mexico":
                st = "NM";
                break;
            case "new york":
                st = "NY";
                break;
            case "north carolina":
                st = "NC";
                break;
            case "north dakota":
                st = "ND";
                break;
            case "ohio":
                st = "OH";
                break;
            case "oklahoma":
                st = "OK";
                break;
            case "oregon":
                st = "OR";
                break;
            case "pennsylvania":
                st = "PA";
                break;
            case "rhode island":
                st = "RI";
                break;
            case "south carolina":
                st = "SC";
                break;
            case "south dakota":
                st = "SD";
                break;
            case "tennessee":
                st = "TN";
                break;
            case "texas":
                st = "TX";
                break;
            case "utah":
                st = "UT";
                break;
            case "vermont":
                st = "VT";
                break;
            case "virginia":
                st = "VA";
                break;
            case "washington":
                st = "WA";
                break;
            case "west virginia":
                st = "WV";
                break;
            case "wisconsin":
                st = "WI";
                break;
            case "wyoming":
                st = "WY";
                break;
            case "washington dc":
            case "washington, dc":
            case "washington, d.c.":
            case "washington, d. c.":
            case "dc":
                st = "DC";
                break;
            case "alberta":
                st = "AB";
                break;
            case "british columbia":
                st = "BC";
                break;
            case "manitoba":
                st = "MB";
                break;
            case "new brunswick":
                st = "NB";
                break;
            case "newfoundland and labrador":
            case "newfoundland":
            case "labrador":
                st = "NL";
                break;
            case "northwest territories":
                st = "NT";
                break;
            case "nova scotia":
            case "novascotia":
                st = "NS";
                break;
            case "nunavut":
                st = "NU";
                break;
            case "ontario":
                st = "ON";
                break;
            case "prince edward":
            case "prince edward island":
                st = "PE";
                break;
            case "quebec":
                st = "QC";
                break;
            case "saskatchewan":
                st = "SK";
                break;
            case "yukon":
            case "yukon territory":
                st = "YT";
                break;
            default:
                st = null;
                
        }
        
        // Return the state abbreviation.
        return st;
    }
    
    
    //</editor-fold>
}
