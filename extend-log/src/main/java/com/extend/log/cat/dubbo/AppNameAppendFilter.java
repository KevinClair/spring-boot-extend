package com.extend.log.cat.dubbo;


import com.alibaba.dubbo.common.Constants;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;

/**
 * 应用名过滤器
 *
 * @author mingj
 * @date 2020/9/4
 */
@Activate(group = {CommonConstants.CONSUMER})
public class AppNameAppendFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext.getContext().setAttachment(Constants.APPLICATION_KEY, invoker.getUrl().getParameter(Constants.APPLICATION_KEY));
        return invoker.invoke(invocation);
    }
}
