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
 */
package com.pekinsoft.loadmaster.sys;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import com.pekinsoft.loadmaster.Starter;

/**
 * This class is used for simply calculating the application version, as this 
 * functionality needs to be moved from the `AppProperties` class so that the
 * calculations will ***not*** be performed when the app is being run outside of
 * the IDE, as is currently happening.
 * 
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 */
public class VersionCalculator {
    // Private fields for versioning of the software.
    
    // Private fields for the softare and Project information.
    private static final String NAME = "Load Master™";
    private static final String PROJECT_NAME = "Load Master™";
    private static final String VENDOR = "PekinSOFT™ Systems";
    private static final String WEBSITE = "https://www.northwind.com";
    private static final String PROJECT_WEB = "https://www.github.com/SeanCarrick/LoadMaster";
    private static final String VENDOR_PHONE = "(309) 989-0672";
    private static final String PROJECT_LEAD = "Sean Carrick";
    private static final String PROJECT_EMAIL = "sean@pekinsoft.com";
    
    private static final Logger log = Logger.getInstance();
    private static final LogRecord record = new LogRecord(Level.FINE, 
            "Instantiating Logging");
    private static final AppProperties props = AppProperties.getInstance();
    
    static {
        record.setMessage("Calculating application version.");
        log.debug(record);
        long bui = Long.parseLong(props.getProperty("app.build", "1903"));
        int rev = Integer.parseInt(props.getProperty("app.revision", "0"));
        int min = Integer.parseInt(props.getProperty("app.minor", "1"));
        int maj = Integer.parseInt(props.getProperty("app.major", "0"));
        
        if ( Starter.params.isSwitchPresent("--debugging")
                || Starter.params.isSwitchPresent("-d") ) {
            // We want our build to run between the values of 0 and 49,
            //+ inclusive. Once we reach the 50th incrementation of the build,
            //+ we want to increase our revision by one. However, we want our
            //+ build number to continually increment. In order to accomplish
            //+ this feat, we will use a modulus calculation to know when to 
            //+ increment the revision.
            if ( bui % 50 == 0 ) 
                rev++;
            
            bui++;
            
            // We want our revision to run from 0 to 150, inclusive. Therefore,
            //+ once the revision is greater than 150, we want to increment our
            //+ minor by one and reset our revision to zero.
            if ( rev > 150 ) {
                min++; 
                rev = 0;
            }
            
            // We want our minor to run from 0 to 10, inclusive. Therefore, as
            //+ once our minor is greater than 10, we want to increment our 
            //+ major by one and reset the minor to zero.
            if ( min > 10 ) {
                maj++; 
                rev = 0;
            }
        }
        
        // Store the calculated version numbers to the application's properties 
        //+ file.
        Starter.props.setProperty("app.build", String.valueOf(bui));
        Starter.props.setProperty("app.revision", String.valueOf(rev));
        Starter.props.setProperty("app.minor", String.valueOf(min));
        Starter.props.setProperty("app.major", String.valueOf(maj));

        props.flush();
        
        record.setMessage("Application version calculated at: " 
                + maj + "." + min + "." + rev + " build " + bui);
        log.debug(record);
        record.setMessage("Initializing complete!");
        log.exit(record, null);
    }    
    
    public VersionCalculator() { /* Static class only. Do not initialize. */ }
    
    public static String getName() {
        return NAME;
    }
    
    public static String getProjectName() {
        return PROJECT_NAME;
    }
    
    public static String getVendor() {
        return VENDOR;
    }
    
    public static String getWebsite() {
        return WEBSITE;
    }
    
    public static String getProjectWebsite() {
        return PROJECT_WEB;
    }
    
    public static String getVendorPhone() {
        return VENDOR_PHONE;
    }
    
    public static String getProjectLead() {
        return PROJECT_LEAD;
    }
    
    public static String getProjectEmail() {
        return PROJECT_EMAIL;
    }
}
