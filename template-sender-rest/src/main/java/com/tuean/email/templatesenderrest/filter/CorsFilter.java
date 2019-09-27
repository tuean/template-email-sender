package com.tuean.email.templatesenderrest.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class CorsFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String methodName = httpServletRequest.getMethod();
        httpServletResponse.addHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.addHeader("Access-Control-Allow-Methods","GET, OPTIONS, HEAD, PUT, POST");
        httpServletResponse.addHeader("Access-Control-Allow-Headers", "x-auth-token,Content-Type,type,Content-Disposition,cache-control,content-type,pragma,x-requested-with");
        httpServletResponse.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        if ("OPTIONS".equals(methodName)) {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
