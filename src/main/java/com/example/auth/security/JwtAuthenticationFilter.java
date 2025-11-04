package com.example.auth.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
// ... imports
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.auth.service.JwtService;

import jakarta.servlet.ServletException;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final org.springframework.security.core.userdetails.UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, org.springframework.security.core.userdetails.UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
        @org.springframework.lang.NonNull jakarta.servlet.http.HttpServletRequest request,
        @org.springframework.lang.NonNull jakarta.servlet.http.HttpServletResponse response,
        @org.springframework.lang.NonNull jakarta.servlet.FilterChain filterChain
    ) throws ServletException, IOException {
        
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // 1. Kiểm tra header Authorization
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Trích xuất Token
        jwt = authHeader.substring(7); // "Bearer " có 7 ký tự
        
        // 3. Trích xuất Username từ Token
        username = jwtService.extractUsername(jwt);

        // 4. Kiểm tra Username và Context Security
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            
            // 5. Tải thông tin User từ DB
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // 6. Xác thực Token
            if (jwtService.isTokenValid(jwt, userDetails)) {
                
                // 7. Tạo đối tượng Authentication
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 8. Thiết lập Authentication vào Security Context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}