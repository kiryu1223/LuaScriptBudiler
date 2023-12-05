package org.example;

import io.github.kiryu1223.expressionTree.Expression;

public class LuaScriptBuilder
{
    final static Object KEYS = new Object();
    final static Object ARGV = new Object();
    final static Object REDIS = new Object();

    public static <K, V> LuaScript createRedisScript(@Expression LuaScriptInterface<K, V> luaScriptInterface)
    {
        throw new RuntimeException("create");
    }

    public static <K, V> LuaScript createRedisScript(LuaScriptTree fn)
    {
        return new LuaScript(fn.invoke((Void)null, KEYS, ARGV, REDIS));
    }
}
