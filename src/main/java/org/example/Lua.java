package org.example;

public class Lua
{
    //lua关键字
    public final static String Local = "local";
    public final static String If = "if";
    public final static String Else = "else";
    public final static String Then = "then";
    public final static String End = "end";
    public final static String Return = "return";
    public final static String Nil = "nil";

    public static int toNumber(Object o)
    {
        throw new RuntimeException("toNumber");
    }
}
