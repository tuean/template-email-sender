package com.tuean.email.templatesenderrest.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tuean.email.templatesenderrest.filter.http.HttpRequestWrapper;
import com.tuean.email.templatesenderrest.filter.http.HttpResponseWrapper;
import com.tuean.email.templatesenderrest.utils.IpUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StopWatch;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class WebLogFilter extends OncePerRequestFilter{

    @Autowired
    private List<HandlerMapping> handlerMappingList;

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        String ip = IpUtils.getLocalIp(httpServletRequest);

        String requestUrl = httpServletRequest.getRequestURI();
        HttpRequestWrapper requestWrapper = new HttpRequestWrapper(httpServletRequest);
        HttpResponseWrapper responseWrapper = new HttpResponseWrapper(httpServletResponse);
//        HttpServletRequest request = new MineHttpServletRequestWrapper(httpServletRequest);

        String params = getBodyString(requestWrapper);
        JSONObject object = JSONObject.parseObject(params);

        String url = null;
        AntPathMatcher apm = new AntPathMatcher();
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = this.requestMappingHandlerMapping.getHandlerMethods();
        for (Map.Entry<RequestMappingInfo, HandlerMethod> item : handlerMethods.entrySet()) {
            for (String urlPattern : item.getKey().getPatternsCondition().getPatterns()) {
                urlPattern = requestWrapper.getContextPath() + urlPattern;
                if (apm.match(urlPattern, requestUrl)) {
                    url = urlPattern;
                    break;
                }
            }
        }
        if (url == null) {
            url = requestWrapper.getRequestURI();
        }

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } catch (Exception var) {
            var.printStackTrace();
        } finally {
//            System.out.println("request:" + object.toJSONString());

            String result = getResponseBody(responseWrapper);
//            System.out.println("response:" + result);

            stopWatch.stop();
//            System.out.println("timeCost:" + stopWatch.getTotalTimeMillis());

            log.info("request:{} response:{} timeCost:{}", JSON.toJSONString(object), result, stopWatch.getTotalTimeMillis());
        }
    }


    public static String getResponseBody(HttpResponseWrapper responseWrapper) {
        String bodyString = "";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //向OutPutStream中写入，如 message.writeTo(baos);

            responseWrapper.getOutPutStream().write(baos.toByteArray());
            bodyString = baos.toString();

            responseWrapper.getOutPutStream().flush();
            responseWrapper.getOutPutStream().close();
        } catch (Exception var) {
            var.printStackTrace();
        }

        return bodyString;
    }

    public static String getBodyString(ServletRequest request) {
        StringBuilder sb = new StringBuilder();
        try (InputStream inputStream = request.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")))) {
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception var) {

        }
        return sb.toString();
    }
}
