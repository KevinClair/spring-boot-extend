package com.extend.log.cat.dubbo;


import com.alibaba.dubbo.common.Constants;
import com.alibaba.dubbo.common.extension.Activate;
import com.alibaba.dubbo.rpc.*;

/**
 * 应用名过滤器
 *
 * @author mingj
 * @date 2020/9/4
 */
@Activate(group = {Constants.CONSUMER})
public class AppNameAppendFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext.getContext().setAttachment(Constants.APPLICATION_KEY, invoker.getUrl().getParameter(Constants.APPLICATION_KEY));
        return invoker.invoke(invocation);
    }
}
