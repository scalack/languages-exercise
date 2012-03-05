/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2009-12-9
 * Time: 18:59:15
 * To change this template use File | Settings | File Templates.
 */
package com.blue;

public class Hello {
    private static Hello ourInstance = new Hello();

    public static Hello getInstance() {
        return ourInstance;
    }

    private Hello() {
     Object x="abc" ;
     String y="def"     ;
     x = y                ;
     //x=x+y                ;
    }
}
