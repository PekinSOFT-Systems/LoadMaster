/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pekinsoft.loadmaster;

import com.pekinsoft.loadmaster.enums.SysExits;
import com.pekinsoft.loadmaster.err.InvalidLoggingLevelException;
import com.pekinsoft.loadmaster.sys.AppProperties;
import com.pekinsoft.loadmaster.sys.Logger;
import com.pekinsoft.loadmaster.sys.VersionCalculator;


/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 */
public class Money {
    
    // Public static constants:
    public static final Logger logger;
    public static final VersionCalculator version;
    public static final AppProperties props;
    public static final String DB_URL;
//    public static final OverviewFrame overview;
    
    static {
        logger = Logger.getInstance();
        try {
            logger.setLevel(Logger.DEBUG);
        } catch (InvalidLoggingLevelException ex) {
            System.err.println(ex.getMessage());
            ex.printStackTrace(System.err);
        }
        
        props = AppProperties.getInstance();
        DB_URL = props.getProperty("app.data.folder");
        
//        MessageBox.showInfo(DB_URL, "Database URL");
        
        version = new VersionCalculator();
        
//        overview = new OverviewFrame();
    }

    /**
     * Main entry point method for the Money Project
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        
        setUI();
        
//        MessageBox.showInfo("Welcome to " + version.getName() + " by " 
//                + version.getVendor() + "!\n\n"
//                + "This is Money Version " 
//                + version.getMajor() + "." + version.getMinor() + "."
//                + version.getRevision() + " build " + version.getBuild() , 
//                "Welcome to " + version.getVendor() + "' " + version.getProjectName());

//        SelectorFrame dlg = new SelectorFrame();
//        dlg.pack();
//        dlg.setVisible(true);
        
//        overview.setVisible(true);
        
//        exit(SysExits.EX_OK);
    }

    /**
     * `exit` provides the functionality to exit the application cleanly and to
     * send back to the operating system the exit code for the application. The
     * only valid exit codes are stored in the enumeration 
     * `com.pekinsoft.money.enums.SysExits`, which is, in turn, based off of the
     * `sysexits.h` file from Berkeley University and contained with every
     * distribution of the Linux operating system.
     * 
     * @param code SysExits     One of the valid exit codes contained in the
     *                          `SysExits` enumeration file.
     */
    public static void exit(SysExits code) {
        // Store any properties that may have been changed.
        props.flush();
        
        System.exit(code.toInt());
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
