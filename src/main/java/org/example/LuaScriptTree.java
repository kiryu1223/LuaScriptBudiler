package org.example;

import io.github.kiryu1223.expressionTree.expressionV2.IExpression;

import java.util.List;

@FunctionalInterface
public interface LuaScriptTree
{
    IExpression invoke(Void unused, Object keys, Object argv, Object redis);
}
