/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pekinsoft.loadmaster;

import com.pekinsoft.loadmaster.enums.SysExits;
import com.pekinsoft.loadmaster.err.InvalidLoggingLevelException;
import com.pekinsoft.loadmaster.sys.AppProperties;
import com.pekinsoft.loadmaster.sys.ArgumentParser;
import com.pekinsoft.loadmaster.sys.Logger;
import com.pekinsoft.loadmaster.sys.VersionCalculator;
import com.pekinsoft.loadmaster.view.LoadMaster;
import java.util.logging.Level;
import java.util.logging.LogRecord;

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
        app.setVisible(true);
        
        record.setMessage("Leaving Starter.main()...");
    }

    public static void exit(SysExits status) {
        // TODO: Perform all cleanup here:
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
