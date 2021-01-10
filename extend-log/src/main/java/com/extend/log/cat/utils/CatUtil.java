package com.extend.log.cat.utils;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Transaction;
import com.extend.common.exception.BaseException;

/**
 * CatUtil.
 *
 * @author KevinClair
 */
public class CatUtil {

    /**
     * 判断捕获到的异常是否为业务异常，如果是，则不记入cat的异常日志
     *
     * @param exception   捕获到的异常
     * @param transaction Cat事务
     */
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
