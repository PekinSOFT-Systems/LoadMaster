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
 *  Class      :   FileUtils.java
 *  Author     :   Sean Carrick
 *  Created    :   Mar 8, 2020 @ 12:33:30 PM
 *  Modified   :   Mar 8, 2020
 *  
 *  Purpose:
 *  
 *  Revision History:
 *  
 *  WHEN          BY                  REASON
 *  ------------  ------------------- ------------------------------------------
 *  Mar 8, 2020  Sean Carrick        Initial creation.
 * *****************************************************************************
 */
package com.pekinsoft.loadmaster.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 *
 * @version 0.1.0
 * @since 0.1.0
 */
public class FileUtils {

    //<editor-fold defaultstate="collapsed" desc="Constructor(s)">
    private FileUtils() {
        // Privatized to prevent this class from being instantiated.
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Public Static Methods and Functions">
    public static boolean copyFile(final File toCopy, final File destFile) {
        try {
            return FileUtils.copyStream(new FileInputStream(toCopy),
                    new FileOutputStream(destFile));
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static boolean copyFilesRecursively(final File toCopy,
            final File destDir) {
        assert destDir.isDirectory();

        if (!toCopy.isDirectory()) {
            return FileUtils.copyFile(toCopy, new File(destDir, toCopy.getName()));
        } else {
            final File newDestDir = new File(destDir, toCopy.getName());

            if (!newDestDir.exists() && !newDestDir.mkdir()) {
                return false;
            }

            for (final File child : toCopy.listFiles()) {
                if (!FileUtils.copyFilesRecursively(child, newDestDir)) {
                    return false;
                }
            }
        }

        return true;
    }

    public static boolean copyJarResourcesRecursively(final File destDir,
            final JarURLConnection jarConnection) throws IOException {
        final JarFile jarFile = jarConnection.getJarFile();

        for (final Enumeration<JarEntry> e = jarFile.entries();
                e.hasMoreElements();) {
            final JarEntry entry = e.nextElement();

            if (entry.getName().startsWith(jarConnection.getEntryName())) {
                final String filename = StringUtils.removeStart(entry.getName(),
                        jarConnection.getEntryName());

                final File f = new File(destDir, filename);

                if (!entry.isDirectory()) {
                    final InputStream entryInputStream = jarFile.getInputStream(entry);

                    if (!FileUtils.copyStream(entryInputStream, f)) {
                        return false;
                    }

                    entryInputStream.close();
                } else {
                    if (!FileUtils.ensureDirectoryExists(f)) {
                        throw new IOException("Could not create directory: "
                                + f.getAbsolutePath());
                    }
                }
            }
        }

        return true;
    }

    public static boolean copyResourcesRecursively(final URL originUrl,
            final File destination) {
        try {
            final URLConnection urlConnection = originUrl.openConnection();

            if (urlConnection instanceof JarURLConnection) {
                return FileUtils.copyJarResourcesRecursively(destination,
                        (JarURLConnection) urlConnection);
            } else {
                return FileUtils.copyFilesRecursively(new File(
                        originUrl.getPath()),
                        destination);
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static boolean copyStream(final InputStream is, final File f) {
        try {
            return FileUtils.copyStream(is, new FileOutputStream(f));
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static boolean copyStream(final InputStream is,
            final OutputStream os) {
        try {
            final byte[] buf = new byte[1024];

            int len = 0;
            while ((len = is.read(buf)) > 0) {
                os.write(buf);
            }

            is.close();
            os.close();
            return true;
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    private static boolean ensureDirectoryExists(final File f) {
        return f.exists() || f.mkdir();
    }
    //</editor-fold>
}
