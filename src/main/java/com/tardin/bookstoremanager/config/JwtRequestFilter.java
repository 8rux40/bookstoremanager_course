package com.tardin.bookstoremanager.config;

import com.tardin.bookstoremanager.users.service.AuthenticationService;
import com.tardin.bookstoremanager.users.service.JwtTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private static final int JWT_TOKEN_START_POSITION = 7;
    private static final String BEARER_JWT_TOKEN_PREFIX = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private JwtTokenManager jwtTokenManager;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String username = null;
        String jwtToken = null;

        String requestTokenHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (isTokenPresent(requestTokenHeader)){
            jwtToken = requestTokenHeader.substring(JWT_TOKEN_START_POSITION);
            username = jwtTokenManager.getUsernameFromToken(jwtToken);
        } else {
            logger.warn("JWT Token does not begin with Bearer string");
        }

        if (isUsernameInContext(username)){
            addUsernameInContext(request, username, jwtToken);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isTokenPresent(String requestTokenHeader) {
        return requestTokenHeader != null && requestTokenHeader.startsWith(BEARER_JWT_TOKEN_PREFIX);
    }

    private boolean isUsernameInContext(String username) {
        return username != null && SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private void addUsernameInContext(HttpServletRequest request, String username, String jwtToken) {
        UserDetails userDetails = authenticationService.loadUserByUsername(username);
        if (jwtTokenManager.validateToken(jwtToken, userDetails)){
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
    }
}
