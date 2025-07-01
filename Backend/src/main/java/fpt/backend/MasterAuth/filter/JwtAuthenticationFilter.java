package fpt.backend.MasterAuth.filter;

import fpt.backend.MasterAuth.user.User;
import io.jsonwebtoken.lang.Collections;
import io.netty.util.internal.StringUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(!Collections.contains(request.getHeaderNames(),"authorization")){
            filterChain.doFilter(request,response);
            return;
        }

        String authHeader = request.getHeader("authorization");
        if(StringUtil.isNullOrEmpty(authHeader) || !authHeader.startsWith("Bearer")){
            throw new AccessDeniedException("Invalid token!");
        }

        final String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);
        User user = (User) userDetailsService.loadUserByUsername(username);
        if(jwtService.isValidToken(token)){
            var newContext = SecurityContextHolder.createEmptyContext();
            newContext.setAuthentication(new UsernamePasswordAuthenticationToken(
                user,null, user.getAuthorities()
            ));
            SecurityContextHolder.setContext(newContext);
            filterChain.doFilter(request,response);
        }


    }
}
