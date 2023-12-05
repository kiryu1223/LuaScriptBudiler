package org.example;

public class RedisCommand<T>
{
    public static final RedisCommand<String> GET = new RedisCommand<>(String.class);
    public static final RedisCommand<Boolean> SET = new RedisCommand<>(Boolean.class);
    public static final RedisCommand<Integer> DEL = new RedisCommand<>(Integer.class);
    public static final RedisCommand<Integer> EXISTS = new RedisCommand<>(Integer.class);
    public static final RedisCommand<Integer> EXPIRE = new RedisCommand<>(Integer.class);
    public static final RedisCommand<Integer> TTL = new RedisCommand<>(Integer.class);
    private final Class<T> returnType;
    private RedisCommand(Class<T> returnType)
    {
        this.returnType = returnType;
    }
    public Class<T> getReturnType()
    {
        return returnType;
    }
}
