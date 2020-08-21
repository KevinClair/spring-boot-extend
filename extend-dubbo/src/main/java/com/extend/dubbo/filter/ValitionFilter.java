package com.extend.dubbo.filter;

import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;
import com.alibaba.fastjson.JSON;
import com.extend.common.utils.ResultUtil;
import com.extend.common.utils.ValidationUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @version 1.0
 * @ClassName ValitionFilter
 * @Description 拦截所有的dubbo请求参数
 * @Author mingj
 * @Date 2018/10/31 10:07
 **/
@Activate(group = Constants.PROVIDER)
public class ValitionFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(ValitionFilter.class);

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Object[] t = invocation.getArguments();
        Result result = null;
        long takeTime = 0L;
        try {
            long startTime = System.currentTimeMillis();
            //对请求参数为空的方法不做校验
            if (t.length != 0){
                for (Object o : t) {
                    ValidationUtils.validate(o);
                }
            }
            result = invoker.invoke(invocation);
            //异常捕捉
            if (result.hasException()){
                throw result.getException();
            }
            takeTime = System.currentTimeMillis() - startTime;
        } catch (Throwable e) {
            logger.error("Exception:{},request{},curr error:{},msg:{}", invocation.getClass(),
                    JSON.toJSON(invocation.getArguments()), e.toString(),
                    ExceptionUtils.getStackTrace(e));
            result = new RpcResult(ResultUtil.response(e));
            return result;
        } finally {
            logger.info("interface:[{}],method:[{}],request:{},result:{},takeTime:{}ms", invoker.getInterface(), invocation.getMethodName(), JSON.toJSON(invocation.getArguments()), (result == null || result.getValue() == null ? null :JSON.toJSON(result.getValue())), takeTime);
        }
        return result;
    }
}
