package com.extend.log.cat.utils;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.extend.common.exception.BaseException;

/**
 * cat工具，用来判断该异常是否为业务异常
 *
 * @author mingj
 * @date 2020/9/4
 */
public class CatUtil {

    /**
     * @Description 判断捕获到的异常是否为业务异常，如果是，则不记入cat的异常日志
     * @Author mingj
     * @Date 2020/9/4 0:10
     * @param exception
     * @param transaction
     * @return void
     **/
    public static void processException(Exception exception, Transaction transaction){
        //如果是业务异常，则置为正常状态
        if (BaseException.isBaseException(exception)){
            if (transaction != null){
                transaction.setStatus(Transaction.SUCCESS);
            }
        }else {
            //如果是其它异常，则置为异常状态
            if (transaction != null){
                Cat.logError(exception);
                transaction.setStatus(exception);
            }
        }
    }
}
