package com.magenic.mobog.exercise4app.security.config.components;

import com.magenic.mobog.exercise4app.exceptions.UserNotAllowedException;
import com.magenic.mobog.exercise4app.security.jwt.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
public class UserRequestFilter extends OncePerRequestFilter {

    @Autowired
    UserDetailsService service;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public UserRequestFilter(UserDetailsService service, JwtTokenUtil jwtTokenUtil){
        this.service = service;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");
        System.out.println("Token: " + requestTokenHeader);
        String userName = null;
        String jwtToken = null;
        if(Optional.ofNullable(requestTokenHeader).isPresent() && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.replace("Bearer ", "");
            try {
                userName = jwtTokenUtil.getUsernameFromToken(jwtToken);
            } catch(IllegalArgumentException e) {
                System.out.println("Unable to get JWT Token.");
            } catch (ExpiredJwtException e) {
                System.out.println("JWT Token has expired.");
            }
        } else {
            logger.warn("JWT Token does not begin with Bearer String");
        }
        try {
            if(userName != null && SecurityContextHolder.getContext().getAuthentication() == null ) {
                UserDetails userDetails = this.service.loadUserByUsername(userName);
                UsernamePasswordAuthenticationToken userNamePwAuthToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                userNamePwAuthToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(userNamePwAuthToken);
            }
        } catch (UsernameNotFoundException e){
            throw new UserNotAllowedException("User does not exists");
        }
        filterChain.doFilter(request, response);
    }
}
