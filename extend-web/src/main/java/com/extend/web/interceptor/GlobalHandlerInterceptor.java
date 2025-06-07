package com.extend.web.interceptor;

import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO
 *
 * @author KevinClair
 */
public class GlobalHandlerInterceptor implements HandlerInterceptor {

    private final String HEAERDR_USER_ID = "X-User-Id";

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (StringUtils.hasText(request.getHeader(HEAERDR_USER_ID))) {
            SessionUserLocal.setUserId(request.getHeader(HEAERDR_USER_ID));
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 在请求处理完成后清理ThreadLocal中的用户ID
        SessionUserLocal.clear();
    }
}
