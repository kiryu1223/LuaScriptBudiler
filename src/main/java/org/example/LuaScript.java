package org.example;

import io.github.kiryu1223.expressionTree.expressionV2.*;

import java.time.Duration;
import java.util.Timer;
import java.util.TimerTask;

public class LuaScript
{
    private final IExpression tree;
    private final StringBuilder sb = new StringBuilder();

    LuaScript(IExpression tree)
    {
        this.tree = tree;
    }

    private void delLast()
    {
        if (sb.charAt(sb.length() - 1) == ',')
        {
            sb.deleteCharAt(sb.length() - 1);
        }
    }

    private void analysis(IExpression tree)
    {
        if (tree instanceof VarExpression)
        {
            var var = (VarExpression) tree;
            sb.append(Lua.Local).append(" ")
                    .append(var.getName())
                    .append(" = ");
            analysis(var.getInit());
            sb.append(";");
        }
        else if (tree instanceof ArrayAccessExpression)
        {
            var array = (ArrayAccessExpression) tree;
            analysis(array.getIndexed());
            sb.append("[");
            analysis(array.getIndex());
            sb.append("]");
        }
        else if (tree instanceof ReferenceExpression)
        {
            var reference = (ReferenceExpression) tree;
            var obj = reference.getReference();
            if (obj.equals(LuaScriptBuilder.KEYS))
            {
                sb.append("KEYS");
            }
            else if (obj.equals(LuaScriptBuilder.ARGV))
            {
                sb.append("ARGV");
            }
            else if (obj.equals(LuaScriptBuilder.REDIS))
            {
                sb.append("redis");
            }
            else
            {
                sb.append(obj);
            }
        }
        else if (tree instanceof MethodCallExpression)
        {
            var methodCall = (MethodCallExpression) tree;
            var obj = methodCall.getSource().getReference();
            if (obj.equals(LuaScriptBuilder.KEYS)
                    || obj.equals(LuaScriptBuilder.ARGV)
                    || obj.equals(LuaScriptBuilder.REDIS))
            {
                analysis(methodCall.getSource());
                sb.append(".").append(methodCall.getSelectedMethod());
                sb.append("(");
                for (IExpression param : methodCall.getParams())
                {
                    analysis(param);
                    sb.append(",");
                }
                delLast();
                sb.append(")");
            }
        }
        else if (tree instanceof LocalReferenceExpression)
        {
            var localRef = (LocalReferenceExpression) tree;
            sb.append(localRef.getName());
        }
        else if (tree instanceof FieldSelectExpression)
        {
            var fieldSelect = (FieldSelectExpression) tree;
            if (fieldSelect.getSource().getReference().equals(RedisCommand.class))
            {
                sb.append("\"").append(fieldSelect.getSelectedField()).append("\"");
            }
            else
            {
                sb.append(fieldSelect.getValue());
            }
        }
        else if (tree instanceof IfExpression)
        {
            var ifExpression = (IfExpression) tree;
            sb.append(Lua.If).append(" ");
            analysis(ifExpression.getCond());
            sb.append(" ").append(Lua.Then).append("\n");
            analysis(ifExpression.getBody());
            if (ifExpression.getElSe() != null)
            {
                var elSe = ifExpression.getElSe();
                if (elSe instanceof IfExpression)
                {
                    sb.append(Lua.Else);
                    analysis(ifExpression.getElSe());
                }
                else
                {
                    sb.append(Lua.Else).append("\n");
                    analysis(ifExpression.getElSe());
                    sb.append(Lua.End);
                }
            }
            else
            {
                sb.append(Lua.End);
            }
        }
        else if (tree instanceof ParensExpression)
        {
            var parens = (ParensExpression) tree;
            sb.append("(");
            analysis(parens.getExpression());
            sb.append(")");
        }
        else if (tree instanceof ReturnExpression)
        {
            var returnExpression = (ReturnExpression) tree;
            sb.append(Lua.Return).append(" ");
            analysis(returnExpression.getExpression());
            sb.append(";");
        }
        else if (tree instanceof ValueExpression<?>)
        {
            var val = (ValueExpression<?>) tree;
            var value = val.getValue();
            if (value == null)
            {
                sb.append(Lua.Nil);
            }
            else
            {
                if (value instanceof String)
                {
                    sb.append("\"").append(value).append("\"");
                }
                else
                {
                    sb.append(value);
                }
            }
        }
        else if (tree instanceof BlockExpression)
        {
            var block = (BlockExpression) tree;
            for (var expression : block.getExpressions())
            {
                analysis(expression);
                sb.append("\n");
            }
        }
        else
        {
            System.out.println(tree);
        }
    }

    public String toLua()
    {
        if (sb.length() == 0)
        {
            if (tree instanceof BlockExpression)
            {
                var blockExpression = (BlockExpression) tree;
                for (var expression : blockExpression.getExpressions())
                {
                    analysis(expression);
                    sb.append("\n");
                }
            }
            else
            {
                analysis(tree);
            }
        }
        return sb.toString();
    }

    @Override
    public String toString()
    {
        return toLua();
    }
}
