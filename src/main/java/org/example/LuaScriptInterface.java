package org.example;

import java.util.List;

@FunctionalInterface
public interface LuaScriptInterface<K, V>
{
    Object invoke(K keys, V argv, Redis redis);
}

