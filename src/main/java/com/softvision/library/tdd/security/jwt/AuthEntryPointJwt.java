package com.softvision.library.tdd.security.jwt;

import com.softvision.library.tdd.model.exception.UnauthorizedException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthEntryPointJwt implements AuthenticationEntryPoint {
    private final Log logger = LogFactory.getLog(AuthEntryPointJwt.class.getName());
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) {
        logger.error(authException);
        throw new UnauthorizedException(authException.getMessage());
    }
}
