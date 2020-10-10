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
 *  Class      :   AppProperties.java
 *  Author     :   Sean Carrick
 *  Created    :   Sep 13, 2020 @ 3:49:17 PM
 *  Modified   :   Sep 13, 2020
 *  
 *  Purpose:
 *  
 *  Revision History:
 *  
 *  WHEN          BY                  REASON
 *  ------------  ------------------- ------------------------------------------
 *  Sep 13, 2020  Sean Carrick        Initial creation.
 *  Oct 10, 2020  Sean Carrick        Added the trademark symbol (™) to the
 *                                    Project Name and Vendor Name, as these two
 *                                    names are legal trademarks of PekinSOFT™
 *                  `                 Systems. Also added the copyright notice
 *                                    to the description and the application
 *                                    title to the About box titlebar.
 * *****************************************************************************
 */
package com.pekinsoft.loadmaster.sys;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import com.pekinsoft.loadmaster.enums.SysExits;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 */
public class AppProperties {
    
    // Application folder structure fields.
    public static final String APP_DIR;
    private static final String LOG_DIR;
    private static final String ERR_DIR;
    private static final String DATA_DIR;
    
    // Private fields for the softare and Project information.
    private static final String NAME = "Load Master™";
    private static final String PROJECT_NAME = "Load Master™";
    private static final String VENDOR = "PekinSOFT™ Systems";
    private static final String WEBSITE = "https://www.pekinsoft.com";
    private static final String PROJECT_WEB = "https://www.github.com/SeanCarrick/LoadMaster";
    private static final String VENDOR_PHONE = "(309) 989-0672";
    private static final String PROJECT_LEAD = "Sean Carrick";
    private static final String PROJECT_EMAIL = "sean@pekinsoft.com";
    
    private static final Properties props;
    private static final Logger log = Logger.getInstance();
    private static final LogRecord record = new LogRecord(Level.ALL, 
            "Instantiating Logging");
    private static final AppProperties appProps = new AppProperties();
    
    static {
        
        props = new Properties();
        
        APP_DIR = System.getProperty("user.home") + File.separator 
                + ".loadmaster" + File.separator;
        ERR_DIR = APP_DIR + "var" + File.separator + "err" + File.separator;
        LOG_DIR = APP_DIR + "var" + File.separator + "logs" + File.separator;
        DATA_DIR = APP_DIR + "data" + File.separator;
        
        File logPath = new File(LOG_DIR);
        File logFile = new File(logPath.getAbsoluteFile() + File.separator
                + "application.log");
        
        if ( !logPath.exists() )
            logPath.mkdirs();
        if ( !logFile.exists() )
            try {
                logFile.createNewFile();
            } catch ( IOException ex ) {
                System.err.println(ex.getMessage());
                ex.printStackTrace(System.err);
            }
        
        try ( FileReader in = new FileReader(new File(APP_DIR 
                + ".loadmaster.conf")); ) {
            props.load(in);
        } catch ( IOException ex ) {
            // If we come to this error handler, odds are teh application has
            //+ not been run before, which is why the file does not exist. 
            //+ Therefore, we will just write a configuration log entry, 
            //+ advising that this exception was thrown, but not write an error
            //+ message to the log.
            record.setSourceClassName(AppProperties.class.getName());
            record.setSourceMethodName("static {} initializer");
            log.enter(record);
            record.setMessage("Exception while trying to read in the "
                    + "configuration file:\n\t" + APP_DIR + 
                    NAME.replace(' ', '_') + ".conf" + "\n\n-> Fixing the "
                    + "problem by generating a setting and writing it.");
            log.debug(record);
            
            // Since this is the first time the system has been run, we are 
            //+ going to create the file structure right here.
            File appDir = new File(APP_DIR);
            File logDir = new File(LOG_DIR);
            File errDir = new File(ERR_DIR);
            File dataDir = new File(DATA_DIR);
            appDir.mkdirs();
            logDir.mkdirs();
            errDir.mkdirs();
            dataDir.mkdirs();
            
            props.put("app.home.folder", APP_DIR);
            props.put("app.logs.folder", LOG_DIR);
            props.put("app.err.folder", ERR_DIR);
            props.put("app.data.folder", DATA_DIR);
            
            record.setMessage("Application directory structure created.");
            log.debug(record);
        }
        
}
    
    private AppProperties() { /* to prevent instantiation */ }
    
    public static AppProperties getInstance() {
        return appProps;
    }
    
    /**
     * Convenience method for getting the data folder for the application. If 
     * after searching the properties list, and the defaults property lists,
     * recursively, and the key is not found, the default data folder will be
     * returned.
     * 
     * @return the value in this property list with the specified key value, or
     *         the default value, if the key is not found.
     */
    public String getDataFolder() {
        return getProperty("app.data.folder", DATA_DIR);
    }
    
    /**
     * Convenience method for setting the data folder for the application. 
     * 
     * @param value the new location for the application home folder
     */
    public void setDataFolder(String value) {
        setProperty("app.data.folder", value);
    }
    
    /**
     * Convenience method for getting the database name
     * 
     * @return the value in this property list for the name of the database
     */
    public String getDbName() {
        return getProperty("db.name", null);
    }
    
    /**
     * Convenience method for setting the database name.
     * 
     * @param value the new database name for the application.
     */
    public void setDbName(String value) {
        setProperty("db.name", value);
    }
    
    /**
     * Convenience method for getting the database connection URL.
     * 
     * @return the value in this property list for the URL for the database
     *         connection
     */
    public String getDbUrl() {
        return getProperty("db.url", "jdbc:hsqldb:");
    }
    
    /**
     * Convenience method for setting the database connection URL.
     * 
     * @param value the new database connection URL for the application.
     */
    public void setDbURL(String value) {
        setProperty("db.url", value);
    }
    
    /**
     * Convenience method for getting the database connection options.
     * 
     * @return the value in this property list for the database connection
     *         options.
     */
    public String getDbOptions() {
        return getProperty("db.options", ";shutdown=true");
    }
    
    /**
     * Convenience method for setting the database connection options.
     * 
     * @param value the new value for the database connection options.
     */
    public void setDbOptions(String value) {
        setProperty("db.options", value);
    }
    
    /**
     * Convenience method for getting the database driver.
     * 
     * @return the value in this property list for the database driver.
     */
    public String getDbDriver() {
        return getProperty("db.driver", "org.hsqldb.jdbcDriver");
    }
    
    /**
     * Convenience method for setting the database driver.
     * 
     * @param value the new value for the database driver.
     */
    public void setDbDriver(String value) {
        setProperty("db.driver", value);
    }
    
    /**
     * Convenience method for getting the application home folder.
     * 
     * @return the value in this property list for the application home folder.
     */
    public String getAppHome() {
        return getProperty("app.home", APP_DIR);
    }
    
    /**
     * Convenience method for setting the application home folder.
     * @param value 
     */
    public void setAppHome(String value) {
        setProperty("app.home", value);
    }
    
    /**
     * Convenience method for getting the logs folder.
     * 
     * @return the value in this property list for the log folder.
     */
    public String getLogHome() {
        return getProperty("logs.home", LOG_DIR);
    }
    
    /**
     * Convenience method for setting the logs folder.
     * 
     * @param value the new value for the logs folder.
     */
    public void setLogHome(String value) {
        setProperty("logs.home", value);
    }
    
    /**
     * Convenience method for getting the error report folder.
     * 
     * @return the value in this property list for the error report folder.
     */
    public String getErrHome() {
        return getProperty("err.home", ERR_DIR);
    }
    
    /**
     * Convenience method for setting the error report folder.
     * 
     * @param value the new value for the error report folder.
     */
    public void setErrHome(String value) {
        setProperty("err.home", value);
    }
    
    /**
     * Searches for the property with the specified key in this property list.
     * If the key is not found in this property list, the default property list,
     * and its defaults, recursively, are then checked. The method returns `null`
     * if the property is not found.
     * 
     * @param   key the property key
     * @return  the value in this property list with the specified key value.
     * @see #setProperty(java.lang.String, java.lang.String) 
     * @see #defaults
     */
    public String getProperty(String key) {
        return props.getProperty(key);
    }
    
    /**
     * Searches for the property with the specified key in this property list.
     * If the key is not found in this property list, the default property list,
     * and its defaults, recursively, are then checked. The method returns the
     * default value argument if the property is not found.
     * 
     * @param key           the property key
     * @param defaultValue  a default value
     * @return  the value in this property list with the specified key, or the
     *          supplied default value if the key value is not found.
     * @see #setProperty
     * @see #defaults
     */
    public String getProperty(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }
    
    /**
     * This convenience method searches the property for the specified key in 
     * this property list. If the key is not found in this property list, the 
     * default property list, and its defaults, recursively, are then checked.
     * The method returns the default value argument if the property is not
     * found.
     * <p>
     * However, with this method, the value located, or the supplied default
     * value, is returned an an `java.lang.Integer` object.
     * @param key           the property key
     * @param defaultValue  a default value
     * @return  the value in this property list with the specified key, the 
     *          supplied default value, or `null`, in the event neither the
     *          value for this key or the default will not parse into a number.
     */
    public Integer getPropertyAsInt(String key, String defaultValue) {
        Integer val;
        try {
            val = Integer.parseInt(props.getProperty(key, defaultValue));
        } catch ( NumberFormatException ex ) {
            try {
                val = Integer.parseInt(defaultValue);
            } catch ( NumberFormatException xe ) {
                val = null;
            }
        }
        
        return val;
    }

    /**
     * This convenience method searches the property for the specified key in 
     * this property list. If the key is not found in this property list, the 
     * default property list, and its defaults, recursively, are then checked.
     * The method returns the default value argument if the property is not
     * found.
     * <p>
     * However, with this method, the value located, or the supplied default
     * value, is returned an an `java.lang.Long` object.
     * @param key           the property key
     * @param defaultValue  a default value
     * @return  the value in this property list with the specified key, the 
     *          supplied default value, or `null`, in the event neither the
     *          value for this key or the default will not parse into a number.
     */
    public Long getPropertyAsLong(String key, String defaultValue) {
        Long val;
        try {
            val = Long.parseLong(props.getProperty(key, defaultValue));
        } catch ( NumberFormatException ex ) {
            try {
                val = Long.parseLong(defaultValue);
            } catch ( NumberFormatException xe ) {
                val = null;
            }
        }
        
        return val;
    }

    /**
     * This convenience method searches the property for the specified key in 
     * this property list. If the key is not found in this property list, the 
     * default property list, and its defaults, recursively, are then checked.
     * The method returns the default value argument if the property is not
     * found.
     * <p>
     * However, with this method, the value located, or the supplied default
     * value, is returned an an `java.lang.Double` object.
     * @param key           the property key
     * @param defaultValue  a default value
     * @return  the value in this property list with the specified key, the 
     *          supplied default value, or `null`, in the event neither the
     *          value for this key or the default will not parse into a number.
     */
    public Double getPropertyAsDouble(String key, String defaultValue) {
        Double val;
        try {
            val = Double.parseDouble(props.getProperty(key, defaultValue));
        } catch ( NumberFormatException ex ) {
            try {
                val = Double.parseDouble(defaultValue);
            } catch ( NumberFormatException xe ) {
                val = null;
            }
        }
        
        return val;
    }

    /**
     * This convenience method searches the property for the specified key in 
     * this property list. If the key is not found in this property list, the 
     * default property list, and its defaults, recursively, are then checked.
     * The method returns the default value argument if the property is not
     * found.
     * <p>
     * However, with this method, the value located, or the supplied default
     * value, is returned an an `java.lang.Float` object.
     * @param key           the property key
     * @param defaultValue  a default value
     * @return  the value in this property list with the specified key, the 
     *          supplied default value, or `null`, in the event neither the
     *          value for this key or the default will not parse into a number.
     */
    public Float getPropertyAsFloat(String key, String defaultValue) {
        Float val;
        try {
            val = Float.parseFloat(props.getProperty(key, defaultValue));
        } catch ( NumberFormatException ex ) {
            try {
                val = Float.parseFloat(defaultValue);
            } catch ( NumberFormatException xe ) {
                val = null;
            }
        }
        
        return val;
    }

    /**
     * This convenience method searches the property for the specified key in 
     * this property list. If the key is not found in this property list, the 
     * default property list, and its defaults, recursively, are then checked.
     * The method returns the default value argument if the property is not
     * found.
     * <p>
     * However, with this method, the value located, or the supplied default
     * value, is returned an an `java.lang.Boolean` object.
     * @param key           the property key
     * @param defaultValue  a default value
     * @return  the value in this property list with the specified key, the 
     *          supplied default value, or `null`, in the event neither the
     *          value for this key or the default will not parse into a number.
     */
    public Boolean getPropertyAsBoolean(String key, String defaultValue) {
        Boolean val;
        try {
            val = Boolean.parseBoolean(props.getProperty(key, defaultValue));
        } catch ( NumberFormatException ex ) {
            try {
                val = Boolean.parseBoolean(defaultValue);
            } catch ( NumberFormatException xe ) {
                val = null;
            }
        }
        
        return val;
    }
    
    /**
     * Calls the `Hashtable` method `put`. Provided for parallelism with the 
     * `getProperty` method. Enforces use of Strings for property keys and 
     * values. The value returned is the result of the `Hashtable.put` call.
     * 
     * @param key   the key to be placed into this property list
     * @param value the value corresponding to the key
     * @return      the previous value of the specified key in this property 
     *              list, or `null` if it did not have one.
     * @see #getProperty(java.lang.String)
     */
    public Object setProperty(String key, String value) {
        return props.setProperty(key, value);
    }
    
    public Object setPropertyAsInt(String key, Integer value) {
        return props.setProperty(key, value.toString());
    }
    
    public Object setPropertyAsLong(String key, Long value) {
        return props.setProperty(key, value.toString());
    }
    
    public Object setPropertyAsDouble(String key, Double value) {
        return props.setProperty(key, value.toString());
    }
    
    public Object setPropertyAsFloat(String key, Float value) {
        return props.setProperty(key, value.toString());
    }
    
    public Object setPropertyAsBoolean(String key, Boolean value) {
        return props.setProperty(key, value.toString());
    }
    
    public void flush() {
        try ( FileWriter out = new FileWriter(new File(APP_DIR)
                + System.getProperty("file.separator") 
                + /*NAME.toLowerCase().replace(' ', '_') +*/ ".loadmaster.conf"); ) {
            props.store(out, PROJECT_NAME + " by " + VENDOR);
        } catch ( IOException ex ) {
            // If we come to this error handler, unlike in the static initializer,
            //+ we have a problem. Therefore, we will log an error.
            record.setSourceClassName(AppProperties.class.getName());
            record.setSourceMethodName("exit");
            record.setMessage(ex.getMessage());
            record.setThrown(ex);
            log.error(record);
        }
    }
    
    /**
     * Provides a means to exit the application in a normalized manner. By using
     * this method to exit, we are able to provide useful meaning to the 
     * underlying operating system, such as the status by which we are exiting,
     * meaning, whether or not it is a normal exit or an exit with an error.
     * <p>
     * When exiting using this method, the application is able to store to disk
     * any properties setting that have been added, or updated, as well as 
     * perform and other necessary housekeeping tasks, such as removing any 
     * temporary files that were created.
     * 
     * @param status `com.northwind.enums.SysExits` enumeration value
     */
    public void exit(SysExits status) {
        record.setSourceClassName(AppProperties.class.getName());
        record.setSourceMethodName("exit");
        record.setParameters(new Object[]{status});
        record.setMessage("Entering the `exit` procedure.");
        log.enter(record);
        
//        props.setProperty("app.major", String.valueOf(VersionCalculator.MAJOR));
//        props.setProperty("app.minor", String.valueOf(VersionCalculator.MINOR));
//        props.setProperty("app.revision", String.valueOf(VersionCalculator.REVISION));
//        props.setProperty("app.build", String.valueOf(VersionCalculator.BUILD));
        
        // Any time that a property is changed anywhere in the application, it
        //+ should be immediately stored back to the properties list. Therefore,
        //+ all we need to do here is to store the properties list to the 
        //+ configuration file so that the settings will be available at the
        //+ next run of the application.
        try ( FileWriter out = new FileWriter(new File(props.getProperty(
                "app.home", APP_DIR) + ".northwind.conf")); ) {
            props.store(out, PROJECT_NAME + " by " + VENDOR);
            out.flush();
            out.close();
        } catch ( IOException ex ) {
            // If we come to this error handler, unlike in the static initializer,
            //+ we have a problem. Therefore, we will log an error.
            record.setSourceClassName(AppProperties.class.getName());
            record.setSourceMethodName("exit");
            record.setMessage(ex.getMessage());
            record.setThrown(ex);
            log.error(record);
        }
        
        // Perform other necessary cleanup here.
        
        // Perform other necessary cleanup here.
        
        
        ////////////////////////////////////////////////////////////////////////
        // KEEP AT THE END OF THIS METHOD! The lines below need to stay last. //
        ////////////////////////////////////////////////////////////////////////
        record.setMessage("Cleanup complete! We can now exit.");
        log.debug(record);
        record.setMessage("Exiting " + PROJECT_NAME + " with the status: "
                + status.toString() + " [" + status.toInt() + "]");
        log.exit(record, status);
        System.exit(status.toInt());
    }
    
    public String getProjectName() {
        return PROJECT_NAME;
    }
    
    public String getVendor() {
        return VENDOR;
    }
    
    public String getProjectWebsite() {
        return PROJECT_WEB;
    }
    
    public String getProjectDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html><h3>Load Master™</h3>\n");
        sb.append("<p>The Load Master™ Project is the culmination of a bunch ");
        sb.append("of different attempts and failures to try and bring about ");
        sb.append("a simple load tracking and accounting package for one-truck");
        sb.append(" owner/operator businesses. We at PekinSOFT™ believe that ");
        sb.append("we have finally figured out exactly how to make this dream");
        sb.append(" a reality...Load Master™ <strong><em>is</em></strong> ");
        sb.append("that reality. Enjoy!</p>");
        sb.append("<h5>Copyright &amp; Trademark</h5><p>");
        sb.append("Copyright© 2006-2020 ").append(getVendor());
        sb.append(" All rights under copyright reserved.</p><p>");
        sb.append(getProjectName()).append(" and PekinSOFT™");
        sb.append(" are legal trademarks of ").append(getVendor()).append("</p>");
        return sb.toString();
    }
    
    public String getProjectEmail() {
        return PROJECT_EMAIL;
    }
    
    public String getWebsite() {
        return WEBSITE;
    }
    
    public String getProjectLead() {
        return PROJECT_LEAD;
    }
    
    public String getName() {
        return NAME;
    }
    
    public String getVersion() {
        return getProperty("app.major") + "." + getProperty("app.minor") 
                + "." + getProperty("app.revision") + " build " 
                + getProperty("app.build");
    }
    
    public String getBuild() {
        return props.getProperty("app.build");
    }
    
    public String getComments() {
        StringBuilder sb = new StringBuilder();
        sb.append(NAME).append(" aims\nto become the de facto standard by ");
        sb.append("which all future accounting\nsystems are measured, at ");
        sb.append("least for the trucking industry. Primarily,\nwe aim ");
        sb.append("to have ").append(PROJECT_NAME).append(" become the ");
        sb.append("number one\naccounting system for small-business, owner-");
        sb.append("operator truck drivers,\nproviding financial reporting ");
        sb.append("that makes sense for a trucking\ncompany, including per ");
        sb.append("mile breakdowns of all financial information.\n\n");
        sb.append("Copyright© 2006-2020 ").append(VENDOR).append("\n");
        return sb.toString();
    }
}
