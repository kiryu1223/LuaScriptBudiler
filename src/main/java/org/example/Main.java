package org.example;

public class Main
{
    public static void main(String[] args)
    {
        LuaScript luaScript = LuaScriptBuilder.
                <Integer[], Integer[]>createRedisScript((keys, argv, redis) ->
        {
            var key = keys[0];
            var value = argv[0];
            var lock = redis.call(RedisCommand.SET, key, value);
            if (lock)
            {
                return true;
            }
            else
            {
                return false;
            }
        });

        System.out.println(luaScript.toLua());
    }
}
