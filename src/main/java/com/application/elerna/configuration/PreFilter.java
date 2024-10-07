package com.application.elerna.configuration;

import com.application.elerna.exception.InvalidRequestData;
import com.application.elerna.exception.ResourceNotFound;
import com.application.elerna.model.Token;
import com.application.elerna.model.User;
import com.application.elerna.service.JwtService;
import com.application.elerna.service.TokenService;
import com.application.elerna.service.UserService;
import com.application.elerna.utils.TokenEnum;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class PreFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;
    private final TokenService tokenService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // extract token from header
        String token = request.getHeader("Authorization");

        log.info("Token: " + token);

        if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        token = token.substring("Bearer ".length());

        // extract username from token
        String username = jwtService.extractUsername(token, TokenEnum.ACCESS_TOKEN);

        if (username.isEmpty()) {
            log.error("PreFilter: User is empty");
            filterChain.doFilter(request, response);
            return;
        }

        Optional<User> userDetails = userService.getByUserName(username);

        // validate access token
        if (!jwtService.isValid(token, TokenEnum.ACCESS_TOKEN, userDetails.get())) {
            log.error("PreFilter: Token is invalid or user is inactive");
            filterChain.doFilter(request, response);
            return;
        }

        Token currentToken = tokenService.getByUuid(request.getHeader("Device-Id"));
        if (!currentToken.isAccStatus() || !currentToken.getAccessToken().equals(token)) {
            log.error("PreFilter: Token is not active or accurate");
            filterChain.doFilter(request, response);
            return;
        }

        // save authentication into security context
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails.get(), null, userDetails.get().getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        securityContext.setAuthentication(authenticationToken);
        SecurityContextHolder.setContext(securityContext);

//        for (GrantedAuthority authority : SecurityContextHolder.getContext().getAuthentication().getAuthorities()) {
//            log.info(authority.getAuthority());
//        }

        filterChain.doFilter(request, response);
    }

}
