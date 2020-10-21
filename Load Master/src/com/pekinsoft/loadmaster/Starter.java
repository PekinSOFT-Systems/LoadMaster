/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pekinsoft.loadmaster;

import com.pekinsoft.loadmaster.controller.BrokerCtl;
import com.pekinsoft.loadmaster.controller.CustomerCtl;
import com.pekinsoft.loadmaster.controller.EntryCtl;
import com.pekinsoft.loadmaster.controller.LoadCtl;
import com.pekinsoft.loadmaster.controller.StopCtl;
import com.pekinsoft.loadmaster.enums.SysExits;
import com.pekinsoft.loadmaster.err.DataStoreException;
import com.pekinsoft.loadmaster.err.InvalidLoggingLevelException;
import com.pekinsoft.loadmaster.sys.AppProperties;
import com.pekinsoft.loadmaster.sys.ArgumentParser;
import com.pekinsoft.loadmaster.sys.Logger;
import com.pekinsoft.loadmaster.sys.VersionCalculator;
import com.pekinsoft.loadmaster.view.LoadMaster;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import javax.swing.JFrame;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 */
public class Starter {
    // Public static constants:
    public static final Logger logger;
    public static final AppProperties props;
    public static final String DB_URL;
    public static VersionCalculator version;
    public static ArgumentParser params;
    public static EntryCtl batch;
    
    static {
        logger = Logger.getInstance();
        try {
            logger.setLevel(Logger.DEBUG);
        } catch (InvalidLoggingLevelException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace(System.err);
        }
        
        props = AppProperties.getInstance();
        DB_URL = props.getProperty("app.data.folder",
                System.getProperty("user.home") + System.getProperty("file.separator") +
                        ".loadmaster" + System.getProperty("file.separator") +
                        "data" + System.getProperty("file.separator"));    
    }

    /**
     * Main entry point method for the Starter Project
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LogRecord record = new LogRecord(Level.ALL,"Load Master logging "
                + "initiated...");
        record.setSourceClassName(Starter.class.getName());
        record.setSourceMethodName("main");
        record.setParameters(args);
        logger.enter(record);

        // Parse the arguments.
        params = new ArgumentParser(args);        
        version = new VersionCalculator();
        
        setUI();

        // Let's show the only window in the application.
        LoadMaster app = new LoadMaster();
        app.pack();
        app.setExtendedState(props.getPropertyAsInt("windows.main.state", "0"));
        
        if ( app.getExtendedState() != JFrame.MAXIMIZED_BOTH ) {
            int top = props.getPropertyAsInt("windows.main.top", "0");
            int left = props.getPropertyAsInt("windows.main.left", "0");
            int height = props.getPropertyAsInt("windows.main.height", "640");
            int width = props.getPropertyAsInt("windows.main.width", "1422");
            
            app.setLocation(left, top);
            app.setSize(width, height);
        }
        app.setVisible(true);
        
        record.setMessage("Leaving Starter.main()...");
    }

    public static void exit(SysExits status) {
        // Perform all cleanup here:\\
        ///////////////\\\\\\\\\\\\\\\
        // Store the number of records in each table to the settings file.
        try {
            BrokerCtl b = new BrokerCtl();
            props.setProperty("table.brokers.records", 
                    String.valueOf(b.getRecordCount()));
            b = null;
            
            CustomerCtl c = new CustomerCtl();
            props.setProperty("table.customers.records", 
                    String.valueOf(c.getRecordCount()));
            c = null;
            
            LoadCtl l = new LoadCtl();
            props.setProperty("table.loads.records", 
                    String.valueOf(l.getRecordCount()));
            l = null;
            
            StopCtl s = new StopCtl();
            props.setProperty("table.stops.records", 
                    String.valueOf(s.getRecordCount()));
            s = null;
        } catch ( DataStoreException ex ) {
            
        }
        
        // Last thing prior to exiting is to save the application settings and
        //+ to close out the application log.
        logger.close(); // Complete logging.
        props.flush();  // Complete settings.
        
        System.exit(status.toInt());
    }

    
    private static void setUI() {
        /* Set the look and feel */
        // If the system the application is being run on is Windows (any version),
        //+ then we will set the look and feel to the native Windows look and
        //+ feel. Otherwise, we will set the look and feel to Nimbus.
        try {
            if ( System.getProperty("os.name").toLowerCase().contains("windows") ) {
                javax.swing.UIManager.setLookAndFeel(
                        javax.swing.UIManager.getSystemLookAndFeelClassName());
            } else {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            }
        } catch (ClassNotFoundException 
                | InstantiationException 
                | IllegalAccessException 
                | javax.swing.UnsupportedLookAndFeelException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace(System.err);
        }
        //</editor-fold>
    }
}
