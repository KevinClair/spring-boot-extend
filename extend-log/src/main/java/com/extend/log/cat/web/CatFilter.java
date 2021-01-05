package com.extend.log.cat.web;

import com.dianping.cat.Cat;
import com.dianping.cat.CatConstants;
import com.dianping.cat.configuration.NetworkInterfaceManager;
import com.dianping.cat.configuration.client.entity.Server;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.MessageProducer;
import com.dianping.cat.message.Transaction;
import com.dianping.cat.message.internal.DefaultMessageManager;
import com.dianping.cat.message.internal.DefaultTransaction;
import com.dianping.cat.message.spi.MessageTree;
import com.extend.common.constant.CatTypeEnum;
import org.unidal.helper.Joiners;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Des
 *
 * @author mingj
 * @date 2020/9/8 15:50
 **/
public class CatFilter implements Filter {
    private static Map<MessageFormat, String> s_patterns = new LinkedHashMap<MessageFormat, String>();

    private List<Handler> m_handlers = new ArrayList<Handler>();

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Context ctx = new Context((HttpServletRequest) request, (HttpServletResponse) response, chain, m_handlers);
        try {
            ctx.handle();
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    protected String getOriginalUrl(ServletRequest request) {
        return ((HttpServletRequest) request).getRequestURI();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String pattern = filterConfig.getInitParameter("pattern");

        if (pattern != null) {
            try {
                String[] patterns = pattern.split(";");

                for (String temp : patterns) {
                    String[] temps = temp.split(":");

                    s_patterns.put(new MessageFormat(temps[0].trim()), temps[1].trim());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        m_handlers.add(CatFilter.CatHandler.ENVIRONMENT);
        m_handlers.add(CatFilter.CatHandler.ID_SETUP);
        m_handlers.add(CatFilter.CatHandler.LOG_SPAN);
        m_handlers.add(CatFilter.CatHandler.LOG_CLIENT_PAYLOAD);
    }

    private static enum CatHandler implements CatFilter.Handler {
        ENVIRONMENT {
            protected int detectMode(HttpServletRequest req) {
                String source = req.getHeader("X-CAT-SOURCE");
                String id = req.getHeader("X-CAT-ID");

                if ("container".equals(source)) {
                    return 2;
                } else if (id != null && id.length() > 0) {
                    return 1;
                } else {
                    return 0;
                }
            }

            @Override
            public void handle(Context ctx) throws Exception {
                HttpServletRequest req = ctx.getRequest();
                boolean top = !Cat.getManager().hasContext();

                ctx.setTop(top);

                if (top) {
                    ctx.setMode(detectMode(req));
                    ctx.setType(CatTypeEnum.URL.getName());

                    setTraceMode(req);
                } else {
                    ctx.setType(CatTypeEnum.URL_FORWARD.getName());
                }

                ctx.handle();
            }

            protected void setTraceMode(HttpServletRequest req) {
                String traceMode = "X-CAT-TRACE-MODE";
                String headMode = req.getHeader(traceMode);

                if ("true".equals(headMode)) {
                    Cat.getManager().setTraceMode(true);
                }
            }
        },

        ID_SETUP {
            private String m_servers;

            private String getCatServer() {
                if (m_servers == null) {
                    DefaultMessageManager manager = (DefaultMessageManager) Cat.getManager();
                    List<Server> servers = manager.getConfigService().getServers();
                    m_servers = Joiners.by(',').join(servers, new Joiners.IBuilder<Server>() {
                        @Override
                        public String asString(Server server) {
                            String ip = server.getIp();
                            Integer httpPort = server.getHttpPort();

                            if ("127.0.0.1".equals(ip)) {
                                ip = NetworkInterfaceManager.INSTANCE.getLocalHostAddress();
                            }

                            return ip + ":" + httpPort;
                        }
                    });
                }

                return m_servers;
            }

            @Override
            public void handle(Context ctx) throws Exception {
                boolean isTraceMode = Cat.getManager().isTraceMode();

                HttpServletRequest req = ctx.getRequest();
                HttpServletResponse res = ctx.getResponse();
                MessageProducer producer = Cat.getProducer();
                int mode = ctx.getMode();

                switch (mode) {
                    case 0:
                        ctx.setId(producer.createMessageId());
                        break;
                    case 1:
                        ctx.setRootId(req.getHeader("X-CAT-ROOT-ID"));
                        ctx.setParentId(req.getHeader("X-CAT-PARENT-ID"));
                        ctx.setId(req.getHeader("X-CAT-ID"));
                        break;
                    case 2:
                        ctx.setRootId(producer.createMessageId());
                        ctx.setParentId(ctx.getRootId());
                        ctx.setId(producer.createMessageId());
                        break;
                    default:
                        throw new RuntimeException(String.format("Internal Error: unsupported mode(%s)!", mode));
                }

                if (isTraceMode) {
                    MessageTree tree = Cat.getManager().getThreadLocalMessageTree();

                    tree.setMessageId(ctx.getId());
                    tree.setParentMessageId(ctx.getParentId());
                    tree.setRootMessageId(ctx.getRootId());

                    res.setHeader("X-CAT-SERVER", getCatServer());

                    switch (mode) {
                        case 0:
                            res.setHeader("X-CAT-ROOT-ID", ctx.getId());
                            break;
                        case 1:
                            res.setHeader("X-CAT-ROOT-ID", ctx.getRootId());
                            res.setHeader("X-CAT-PARENT-ID", ctx.getParentId());
                            res.setHeader("X-CAT-ID", ctx.getId());
                            break;
                        case 2:
                            res.setHeader("X-CAT-ROOT-ID", ctx.getRootId());
                            res.setHeader("X-CAT-PARENT-ID", ctx.getParentId());
                            res.setHeader("X-CAT-ID", ctx.getId());
                            break;
                    }
                }

                ctx.handle();
            }
        },

        LOG_CLIENT_PAYLOAD {
            @Override
            public void handle(Context ctx) throws Exception {
                HttpServletRequest req = ctx.getRequest();
                String type = ctx.getType();

                if (ctx.isTop()) {
                    logRequestClientInfo(req, type);
                    logRequestPayload(req, type);
                } else {
                    logRequestPayload(req, type);
                }

                ctx.handle();
            }

            protected void logRequestClientInfo(HttpServletRequest req, String type) {
                StringBuilder sb = new StringBuilder(1024);
                String ip = "";
                String ipForwarded = req.getHeader("x-forwarded-for");

                if (ipForwarded == null) {
                    ip = req.getRemoteAddr();
                } else {
                    ip = ipForwarded;
                }

                sb.append("IPS=").append(ip);
                sb.append("&VirtualIP=").append(req.getRemoteAddr());
                sb.append("&Server=").append(req.getServerName());
                sb.append("&Referer=").append(req.getHeader("referer"));
                sb.append("&Agent=").append(req.getHeader("user-agent"));

                Cat.logEvent(type, type + ".Server", Message.SUCCESS, sb.toString());
            }

            protected void logRequestPayload(HttpServletRequest req, String type) {
                StringBuilder sb = new StringBuilder(256);

                sb.append(req.getScheme().toUpperCase()).append('/');
                sb.append(req.getMethod()).append(' ').append(req.getRequestURI());

                String qs = req.getQueryString();

                if (qs != null) {
                    sb.append('?').append(qs);
                }

                Cat.logEvent(type, type + ".Method", Message.SUCCESS, sb.toString());
            }
        },

        LOG_SPAN {
            private void customizeStatus(Transaction t, HttpServletRequest req) throws Exception {
                List<Message> children = t.getChildren();
                for (Message child : children) {
                    if (!child.getStatus().equals(Message.SUCCESS) && child.getName().contains("Exception")){
                        t.setStatus(child.getStatus());
                        return;
//                        Cat.logError((Exception) Class.forName(child.getName()).newInstance());
                    }
                }
                Object catStatus = req.getAttribute(CatConstants.CAT_STATE);
                if (catStatus != null) {
                    t.setStatus(catStatus.toString());
                } else {
                    t.setStatus(Message.SUCCESS);
                }
            }

            private void customizeUri(Transaction t, HttpServletRequest req) {
                if (t instanceof DefaultTransaction) {
                    Object catPageType = req.getAttribute(CatConstants.CAT_PAGE_TYPE);

                    if (catPageType instanceof String) {
                        ((DefaultTransaction) t).setType(catPageType.toString());
                    }

                    Object catPageUri = req.getAttribute(CatConstants.CAT_PAGE_URI);

                    if (catPageUri instanceof String) {
                        ((DefaultTransaction) t).setName(catPageUri.toString());
                    }
                }
            }

            private String getRequestURI(HttpServletRequest req) {
                String requestURI = req.getRequestURI();

                if (s_patterns.size() == 0) {
                    return requestURI;
                } else {
                    for (Map.Entry<MessageFormat, String> entry : s_patterns.entrySet()) {
                        MessageFormat format = entry.getKey();

                        try {
                            format.parse(requestURI);

                            return entry.getValue();
                        } catch (Exception e) {
                            // ignore
                        }
                    }
                    return requestURI;
                }
            }

            @Override
            public void handle(Context ctx) throws Exception {
                HttpServletRequest req = ctx.getRequest();
                Transaction t = Cat.newTransaction(ctx.getType(), getRequestURI(req));
                try {
                    ctx.handle();
                    customizeStatus(t, req);
                } catch (Throwable e) {
                    t.setStatus(e);
                    Cat.logError(e);
                    throw e;
                } finally {
                    customizeUri(t, req);
                    t.complete();
                }
            }
        };
    }

    protected static interface Handler {
        public void handle(Context ctx) throws Exception;
    }

    protected static class Context {
        private FilterChain m_chain;

        private List<Handler> m_handlers;

        private int m_index;

        private int m_mode;

        private String m_rootId;

        private String m_parentId;

        private String m_id;

        private HttpServletRequest m_request;

        private HttpServletResponse m_response;

        private boolean m_top;

        private String m_type;

        public Context(HttpServletRequest request, HttpServletResponse response, FilterChain chain, List<Handler> handlers) {
            m_request = request;
            m_response = response;
            m_chain = chain;
            m_handlers = handlers;
        }

        public String getId() {
            return m_id;
        }

        public void setId(String id) {
            m_id = id;
        }

        public int getMode() {
            return m_mode;
        }

        public void setMode(int mode) {
            m_mode = mode;
        }

        public String getParentId() {
            return m_parentId;
        }

        public void setParentId(String parentId) {
            m_parentId = parentId;
        }

        public HttpServletRequest getRequest() {
            return m_request;
        }

        public HttpServletResponse getResponse() {
            return m_response;
        }

        public String getRootId() {
            return m_rootId;
        }

        public void setRootId(String rootId) {
            m_rootId = rootId;
        }

        public String getType() {
            return m_type;
        }

        public void setType(String type) {
            m_type = type;
        }

        public void handle() throws Exception{
            if (m_index < m_handlers.size()) {
                CatFilter.Handler handler = m_handlers.get(m_index++);

                handler.handle(this);
            } else {
                m_chain.doFilter(m_request, m_response);
            }
        }

        public boolean isTop() {
            return m_top;
        }

        public void setTop(boolean top) {
            m_top = top;
        }
    }

}
