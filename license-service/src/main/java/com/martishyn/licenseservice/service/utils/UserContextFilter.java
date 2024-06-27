package com.martishyn.licenseservice.service.utils;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class UserContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

        UserContextHolder.getContext().setCorrelationId(httpServletRequest.getHeader(UserContext.CORRELATION_ID_HEADER));
        UserContextHolder.getContext().setUserId(httpServletRequest.getHeader(UserContext.USER_ID_HEADER));
        UserContextHolder.getContext().setAuthToken(httpServletRequest.getHeader(UserContext.AUTH_TOKEN_HEADER));
        UserContextHolder.getContext().setOrganizationId(httpServletRequest.getHeader(UserContext.ORGANIZATION_ID_HEADER));

        log.info("UserContextFilter Correlation id: {}", UserContextHolder.getContext().getCorrelationId());
        log.info("UserContextFilter Auth token : {}", UserContextHolder.getContext().getAuthToken());
        filterChain.doFilter(httpServletRequest, servletResponse);
    }
}
