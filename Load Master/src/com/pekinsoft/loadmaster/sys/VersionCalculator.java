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
    public static final int MAJOR;
    public static final int MINOR;
    public static final int REVISION;
    public static final long BUILD;
    
    // Private fields for the softare and Project information.
    private static final String NAME = "Load Master";
    private static final String PROJECT_NAME = "LoadMaster";
    private static final String VENDOR = "PekinSOFT Systems";
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
        
        if ( Boolean.parseBoolean(props.getProperty("debugging", "true")) ) {
            if ( bui >= 1903 ) {
                String sysTime = String.valueOf(System.currentTimeMillis());
                bui = Long.valueOf(sysTime.substring((sysTime.length() / 3) * 2, 
                        sysTime.length()));
            } else {
                System.out.println("Current System Time in milliseconds: " +
                        System.currentTimeMillis());
                bui += (System.currentTimeMillis() / 900000000);
            }
            Starter.props.setProperty("app.build", String.valueOf(bui));
            
            if ( bui >= 9900 ) {
                bui = 1903;
                rev++; 
            }
            Starter.props.setProperty("app.revision", String.valueOf(rev));
            
            if ( rev >= 49 ) {
                rev = 0;
                min++; 
            }
            Starter.props.setProperty("app.minor", String.valueOf(min));
            
            if ( min >= 9 ) {
                min = 0;
                maj++; 
            }
            Starter.props.setProperty("app.major", String.valueOf(maj));
        }
        
        MAJOR = maj;
        MINOR = min;
        REVISION = rev;
        BUILD = bui;

        props.flush();
        
        record.setMessage("Application version calculated at: " 
                + MAJOR + "." + MINOR + "." + REVISION + " build " + BUILD);
        log.debug(record);
        record.setMessage("Initializing complete!");
        log.exit(record, null);
        }    
    
    public VersionCalculator() {}
    
    public int getMajor() {
        return MAJOR;
    }
    
    public int getMinor() {
        return MINOR;
    }
    
    public int getRevision() {
        return REVISION;
    }
    
    public long getBuild() {
        return BUILD;
    }
    
    public String getName() {
        return NAME;
    }
    
    public String getProjectName() {
        return PROJECT_NAME;
    }
    
    public String getVendor() {
        return VENDOR;
    }
    
    public String getWebsite() {
        return WEBSITE;
    }
    
    public String getProjectWebsite() {
        return PROJECT_WEB;
    }
    
    public String getVendorPhone() {
        return VENDOR_PHONE;
    }
    
    public String getProjectLead() {
        return PROJECT_LEAD;
    }
    
    public String getProjectEmail() {
        return PROJECT_EMAIL;
    }
}
