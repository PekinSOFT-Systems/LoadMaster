/*
 * Copyright (C) 2020 PekinSOFT™ Systems
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
 * 
 * Project :   Load Master™
 * Class   :   StringUtilsTest
 * Author  :   Sean Carrick
 * Created :   Oct 25, 2020
 * Modified:   Oct 25, 2020
 * 
 * Purpose:
 *     [Provide a general purpose overview for this class]
 * 
 * WHEN          BY                  REASON
 * ------------  ------------------  -------------------------------------------
 * Oct 25, 2020  Sean Carrick        Initial creation.
 * 
 * *****************************************************************************
 */
package com.pekinsoft.loadmaster.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 */
public class StringUtilsTest {
    
    public StringUtilsTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of abbreviate method, of class StringUtils.
     */
    @Test
    public void testAbbreviate() {
        System.out.println("abbreviate");
        String source = "Testing the abbreviate function.";
        int width = 19;
        String expResult = "Testing the abbr...";
        String result = StringUtils.abbreviate(source, width);
        assertEquals(expResult, result);
    }

    /**
     * Test of wrapAt method, of class StringUtils.
     */
    @Test
    public void testWrapAt() {
        System.out.println("wrapAt");
        String source = "Testing the abbreviate function.";
        int width = 11;
        String expResult = "Testing the\n wrapAt fun\nction.";
        String result = StringUtils.wrapAt(source, width);
        assertEquals(expResult, result);
    }

    /**
     * Test of deleteWhitespace method, of class StringUtils.
     */
    @Test
    public void testDeleteWhitespace() {
        System.out.println("deleteWhitespace");
        String source = "Testing the delete whitespace function.";
        String expResult = "Testingthedeletewhitespacefunction.";
        String result = StringUtils.deleteWhitespace(source);
        assertEquals(expResult, result);
    }

    /**
     * Test of removeStart method, of class StringUtils.
     */
    @Test
    public void testRemoveStart() {
        System.out.println("removeStart");
        String source = "Testing the removeStart function.";
        String remove = "Testing the ";
        String expResult = "removeStart function.";
        String result = StringUtils.removeStart(source, remove);
        assertEquals(expResult, result);
    }

    /**
     * Test of removeStartIgnoreCase method, of class StringUtils.
     */
    @Test
    public void testRemoveStartIgnoreCase() {
        System.out.println("removeStartIgnoreCase");
        String source = "Testing the removeStartIgnoreCase fucntion.";
        String remove = "tEsTiNg THE ";
        String expResult = "removeStartIgnoreCase function.";
        String result = StringUtils.removeStartIgnoreCase(source, remove);
        assertEquals(expResult, result);
    }

    /**
     * Test of getMiddle method, of class StringUtils.
     */
    @Test
    public void testGetMiddle() {
        System.out.println("getMiddle");
        String source = "Testing the getMiddle function.";
        String start = "e ";
        String end = " f";
        String expResult = "getMiddle";
        String result = StringUtils.getMiddle(source, start, end);
        assertEquals(expResult, result);
    }

    /**
     * Test of removeEnd method, of class StringUtils.
     */
    @Test
    public void testRemoveEnd() {
        System.out.println("removeEnd");
        String source = "Testing the removeEnd function.";
        int beginIndex = 0;
        int endIndex = 21;
        String expResult = "";
        String result = StringUtils.removeEnd(source, beginIndex, endIndex);
        assertEquals(expResult, result);
    }

    /**
     * Test of padLeft method, of class StringUtils.
     */
    @Test
    public void testPadLeft() {
        System.out.println("padLeft");
        String toPad = "     Testing the padLeft function.";
        int fieldWidth = 34;
        String expResult = "Testing the padLeft function.     ";
        String result = StringUtils.padLeft(toPad, fieldWidth);
        assertEquals(expResult, result);
    }

    /**
     * Test of padRight method, of class StringUtils.
     */
    @Test
    public void testPadRight() {
        System.out.println("padRight");
        String toPad = "Testing the padRight function.     ";
        int fieldWidth = 35;
        String expResult = "     Testing the padRight function.";
        String result = StringUtils.padRight(toPad, fieldWidth);
        assertEquals(expResult, result);
    }
    
}
