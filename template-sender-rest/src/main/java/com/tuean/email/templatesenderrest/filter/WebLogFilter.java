package com.tuean.email.templatesenderrest.filter;

import com.tuean.email.templatesenderrest.filter.http.SpringRequestWrapper;
import com.tuean.email.templatesenderrest.filter.http.SpringResponseWrapper;
import com.tuean.email.templatesenderrest.utils.HttpUtil;
import com.tuean.email.templatesenderrest.utils.IpUtils;
import com.tuean.email.templatesenderrest.utils.Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StopWatch;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@Order(2)
public class WebLogFilter implements Filter {

    @Autowired
    private List<HandlerMapping> handlerMappingList;

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        String contentType = httpServletRequest.getContentType();
        if (contentType != null && contentType.startsWith("multipart/form-data")) {
            log.info("form req, no log");
            filterChain.doFilter(servletRequest, servletResponse);
            return;
        }

        String ip = IpUtils.getLocalIp(httpServletRequest);

        String requestUrl = httpServletRequest.getRequestURI();



        boolean toLogResponse = true;
        String url = null;
        AntPathMatcher apm = new AntPathMatcher();
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = this.requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> item : handlerMethods.entrySet()) {
            for (String urlPattern : item.getKey().getPatternsCondition().getPatterns()) {
                urlPattern = httpServletRequest.getContextPath() + urlPattern;
                if (apm.match(urlPattern, requestUrl)) {
                    url = urlPattern;

                    HandlerMethod handlerMethod = item.getValue();
                    Method method = handlerMethod.getMethod();
//                    toLogResponse = method.getAnnotation(NoResponseLog.class) == null;
                    break;
                }
            }
        }
        if (url == null) {
            url = httpServletRequest.getRequestURI();
        }


        SpringResponseWrapper response = null;
        final SpringRequestWrapper requestWrapper = new SpringRequestWrapper(httpServletRequest);
        String params = HttpUtil.getBodyString(requestWrapper);
//        JSONObject object = JSONObject.parseObject(params);


        try {
            if (toLogResponse) {
                final SpringResponseWrapper responseWrapper = new SpringResponseWrapper(httpServletResponse);
                response = responseWrapper;
                filterChain.doFilter(requestWrapper, responseWrapper);
            } else {
                filterChain.doFilter(requestWrapper, httpServletResponse);
            }

        } catch (Exception var) {
            var.printStackTrace();
        } finally {

            String result = toLogResponse ? HttpUtil.getResponseBody(response) : "";
            if (url != null && url.contains("download")) {
                result = Util.eslipse(result);
            }
            stopWatch.stop();

            log.info("ip:{} url:{} request:{} response:{} timeCost:{}",
                    ip,
                    url,
                    params,
                    result,
                    stopWatch.getTotalTimeMillis() + "ms"
            );
        }
    }

    @Override
    public void destroy() {

    }
}
