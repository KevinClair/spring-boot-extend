package com.extend.log.cat.dubbo;
import com.dianping.cat.Cat;
import com.dianping.cat.message.Event;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;
import com.extend.common.constant.CatEventTypeEnum;
import com.extend.common.constant.CatTypeEnum;
import com.extend.common.constant.CommonConstant;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.remoting.RemotingException;
import org.apache.dubbo.remoting.TimeoutException;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.support.RpcUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * CatTransaction.
 *
 * @author KevinClair
 */
@Activate(group = {CommonConstants.PROVIDER, CommonConstants.CONSUMER},order = -9000)
public class CatTransaction implements Filter {

    private final static String DUBBO_BIZ_ERROR = "Dubbo.biz.error";

    private final static String DUBBO_TIMEOUT_ERROR = "Dubbo.timeout.error";

    private final static String DUBBO_REMOTING_ERROR = "Dubbo.remoting.error";


    private static final ThreadLocal<Cat.Context> CAT_CONTEXT = new ThreadLocal<Cat.Context>();

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if (!DubboCat.isEnable()) {
            Result result = invoker.invoke(invocation);
            return result;
        }
        URL url = invoker.getUrl();
        String sideKey = url.getParameter(CommonConstants.SIDE_KEY);
        String loggerName = invoker.getInterface().getSimpleName() + "." + invocation.getMethodName();
        String type = CatTypeEnum.DUBBO_CLIENT.getName();
        if (CommonConstants.PROVIDER_SIDE.equals(sideKey)) {
            type = CatTypeEnum.DUBBO_SERVER.getName();
        }

        boolean init = false;
        Transaction transaction = null;

        try {
            transaction = Cat.newTransaction(type, loggerName);
            Cat.Context context = getContext();
            if (CommonConstants.CONSUMER_SIDE.equals(sideKey)) {
                createConsumerCross(url, transaction);
                // 该方法内部初始化了Cat.Context.ROOT，Cat.Context.CHILD，Cat.Context.PARENT，只是在消费端做了这个事情
                Cat.logRemoteCallClient(context, invoker.getUrl().getParameter("remote.application"));
            } else {
                // 如果是Provider端，从RpcContext中获取Cat.Context的三个参数
                context.addProperty(Cat.Context.ROOT, RpcContext.getContext().getAttachment(Cat.Context.ROOT));
                context.addProperty(Cat.Context.CHILD, RpcContext.getContext().getAttachment(Cat.Context.CHILD));
                context.addProperty(Cat.Context.PARENT, RpcContext.getContext().getAttachment(Cat.Context.PARENT));
                createProviderCross(url, transaction);
                Cat.logRemoteCallServer(context);
            }
            // 消费端处理完之后，将Cat.Context.ROOT，Cat.Context.CHILD，Cat.Context.PARENT放在了RpcContext里面，这样Provider端就可以通过RpcContext中获取
            setAttachment(context);
            init = true;
        } catch (Exception e) {
            Cat.logError(e);
        }

        Result result = null;
        try {
            // invoke方法，Provider端进入
            result = invoker.invoke(invocation);

            if (!init) {
                return result;
            }

            boolean isAsync = RpcUtils.isAsync(invoker.getUrl(), invocation);

            //异步的不能判断是否有异常,这样会阻塞住接口(<AsyncRpcResult>hasException->getRpcResult->resultFuture.get()
            if (isAsync) {
                transaction.setStatus(Message.SUCCESS);
                return result;
            }

            if (result.hasException()) {
                //给调用接口出现异常进行打点
                Throwable throwable = result.getException();
                Event event = null;
                if (RpcException.class == throwable.getClass()) {
                    Throwable caseBy = throwable.getCause();
                    if (caseBy != null && caseBy.getClass() == TimeoutException.class) {
                        event = Cat.newEvent(DUBBO_TIMEOUT_ERROR, "TimeOutError");
                    } else {
                        event = Cat.newEvent(DUBBO_REMOTING_ERROR, "RemotingError");
                    }
                } else if (RemotingException.class.isAssignableFrom(throwable.getClass())) {
                    event = Cat.newEvent(DUBBO_REMOTING_ERROR, "RemotingError");
                } else {
                    event = Cat.newEvent(DUBBO_BIZ_ERROR, "BizError");
                }
                event.addData(loggerName);
                event.setStatus(result.getException());
                completeEvent(event);
//                transaction.addChild(event);
                transaction.setStatus(result.getException());
            } else {
                transaction.setStatus(Message.SUCCESS);
            }
            return result;
        } catch (RuntimeException e) {
            if (init) {
                Cat.logError(e);
                Event event;
                if (RpcException.class == e.getClass()) {
                    Throwable caseBy = e.getCause();
                    if (caseBy != null && caseBy.getClass() == TimeoutException.class) {
                        event = Cat.newEvent(DUBBO_TIMEOUT_ERROR, "TimeOutError");
                    } else {
                        event = Cat.newEvent(DUBBO_REMOTING_ERROR, "RemotingError");
                    }
                } else {
                    event = Cat.newEvent(DUBBO_BIZ_ERROR, "BizError");
                }
                event.addData(loggerName);
                event.setStatus(e);
                completeEvent(event);
//                transaction.addChild(event);
                transaction.setStatus(e.getClass().getSimpleName());
            }
            if (result == null) {
                throw e;
            } else {
                return result;
            }
        } finally {
            if (transaction != null) {
                transaction.complete();
            }
            CAT_CONTEXT.remove();
        }
    }

    static class DubboCatContext implements Cat.Context {

        private Map<String, String> properties = new HashMap<String, String>();

        @Override
        public void addProperty(String key, String value) {
            properties.put(key, value);
        }

        @Override
        public String getProperty(String key) {
            return properties.get(key);
        }
    }

    private String getProviderAppName(URL url) {
        String appName = url.getParameter(CommonConstant.PROVIDER_APPLICATION_NAME);
        if (StringUtils.isEmpty(appName)) {
            String interfaceName = url.getParameter(CommonConstants.INTERFACE_KEY);
            appName = interfaceName.substring(0, interfaceName.lastIndexOf('.'));
        }
        return appName;
    }

    private void setAttachment(Cat.Context context) {
        RpcContext.getContext().setAttachment(Cat.Context.ROOT, context.getProperty(Cat.Context.ROOT));
        RpcContext.getContext().setAttachment(Cat.Context.CHILD, context.getProperty(Cat.Context.CHILD));
        RpcContext.getContext().setAttachment(Cat.Context.PARENT, context.getProperty(Cat.Context.PARENT));
    }

    private Cat.Context getContext() {
        // 第一次初始化的时候，context为null
        Cat.Context context = CAT_CONTEXT.get();
        if (context == null) {
            context = initContext();
            CAT_CONTEXT.set(context);
        }
        return context;
    }

    private Cat.Context initContext() {
        Cat.Context context = new DubboCatContext();
        // 这里attachments为空，context的MessageTree信息不在这里设置；
        Map<String, String> attachments = RpcContext.getContext().getAttachments();
        if (attachments != null && attachments.size() > 0) {
            for (Map.Entry<String, String> entry : attachments.entrySet()) {
                if (Cat.Context.CHILD.equals(entry.getKey()) || Cat.Context.ROOT.equals(entry.getKey()) || Cat.Context.PARENT.equals(entry.getKey())) {
                    context.addProperty(entry.getKey(), entry.getValue());
                }
            }
        }
        return context;
    }

    private void createConsumerCross(URL url, Transaction transaction) {
        Event crossAppEvent = Cat.newEvent(CatEventTypeEnum.DUBBO_CLIENT_APP.getName(), "app");
        Event crossServerEvent = Cat.newEvent(CatEventTypeEnum.DUBBO_CLIENT_SERVER.getName(), "server");
        Event crossPortEvent = Cat.newEvent(CatEventTypeEnum.DUBBO_CLIENT_PORT.getName(), "port");
        crossAppEvent.addData(getProviderAppName(url));
        crossServerEvent.addData(url.getHost());
        crossPortEvent.addData(url.getPort() + "");
        crossAppEvent.setStatus(Event.SUCCESS);
        crossServerEvent.setStatus(Event.SUCCESS);
        crossPortEvent.setStatus(Event.SUCCESS);
        completeEvent(crossAppEvent);
        completeEvent(crossPortEvent);
        completeEvent(crossServerEvent);
//        transaction.addChild(crossAppEvent);
//        transaction.addChild(crossPortEvent);
//        transaction.addChild(crossServerEvent);
    }

    private void completeEvent(Event event) {
        event.complete();
    }

    private void createProviderCross(URL url, Transaction transaction) {
        String consumerAppName = RpcContext.getContext().getAttachment(CommonConstants.APPLICATION_KEY);
        if (StringUtils.isEmpty(consumerAppName)) {
            consumerAppName = RpcContext.getContext().getRemoteHost() + ":" + RpcContext.getContext().getRemotePort();
        }
        Event crossAppEvent = Cat.newEvent(CatEventTypeEnum.DUBBO_SERVER_APP.getName(), "app");
        Event crossServerEvent = Cat.newEvent(CatEventTypeEnum.DUBBO_SERVER_CLIENT.getName(), "client");
        Event crossPortEvent = Cat.newEvent(CatEventTypeEnum.DUBBO_SERVER_PORT.getName(), "port");
        crossAppEvent.addData(consumerAppName);
        crossServerEvent.addData(url.getHost());
        crossPortEvent.addData(url.getPort() + "");
        crossAppEvent.setStatus(Event.SUCCESS);
        crossServerEvent.setStatus(Event.SUCCESS);
        crossPortEvent.setStatus(Event.SUCCESS);
        completeEvent(crossAppEvent);
        completeEvent(crossPortEvent);
        completeEvent(crossServerEvent);
//        transaction.addChild(crossAppEvent);
//        transaction.addChild(crossPortEvent);
//        transaction.addChild(crossServerEvent);
    }

}
