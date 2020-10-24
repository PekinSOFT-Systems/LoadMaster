/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.pekinsoft.loadmaster.utils;

import java.lang.reflect.Method;
import java.util.Comparator;

/**
 *
 * @author Sean Carrick &lt;sean at pekinsoft dot com&gt;
 * 
 * @version 0.1.0
 * @since 0.1.0
 */
public class SortByName implements Comparator<Method> {

    @Override
    public int compare(Method o1, Method o2) {
        return o1.getName().compareTo(o2.getName());
    }

}
