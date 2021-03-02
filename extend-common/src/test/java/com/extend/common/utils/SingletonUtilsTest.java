package com.extend.common.utils;


import com.extend.common.base.Result;
import org.junit.Test;

/**
 * TODO
 *
 * @author KevinClair
 */
public class SingletonUtilsTest {

    @Test
    public void getInstance() {

        SingletonUtils.INST.single(Result.class, new Result<>());
        SingletonUtils.INST.single(Result.class, new Result<>());

        Result result = SingletonUtils.INST.get(Result.class);
        Result result1 = SingletonUtils.INST.get(Result.class);
        System.out.println(result == result1);
    }

    void instance() {
    }
}