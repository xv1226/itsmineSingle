package com.sparta.itsminesingle.global.Redisson.Lock;

import java.util.function.Supplier;

public interface LockManager {
    <T> T lock(String lockName, Supplier<T> operation);
}

