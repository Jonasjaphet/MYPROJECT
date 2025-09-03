package com.example.hospital.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    // 📋 ORodha ya endpoints ambazo hazihitaji JWT token
    private static final List<String> PUBLIC_PATHS = List.of(
            "/auth/",
            "/v3/api-docs",
            "/swagger-ui",
            "/swagger-resources",
            "/webjars",
            "/api/availability"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // ✅ Angalia kama hii ni public endpoint
        boolean isPublicPath = PUBLIC_PATHS.stream().anyMatch(requestURI::startsWith);

        if (isPublicPath) {
            // 🔓 Ruhusu request iendelee bila kuangalia token
            filterChain.doFilter(request, response);
            return;
        }

        // 🔐 Angalia kama kuna Authorization header
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // ❌ Hakuna token iliyotumwa, toa error
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token ya JWT haipo au si sahihi");
            return;
        }

        try {
            // 📝 Toa token kutoka header
            final String jwt = authHeader.substring(7);
            final String userEmail = jwtService.extractUsername(jwt);

            // 🔍 Angalia kama user email ipo na kama hakuna authentication ya sasa
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                // ✅ Thibitisha kama token ni sahihi
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    // 🎉 Weka authentication kwenye context
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    // ❌ Token si sahihi
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token si sahihi");
                    return;
                }
            }
        } catch (Exception e) {
            // ⚠️ Hitilafu wakati wa kuchakata token
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Hitilafu ya kuchakata token: " + e.getMessage());
            return;
        }

        // ➡️ Endelea na mchakato wa filter
        filterChain.doFilter(request, response);
    }

    // 🎯 Method ya kuhakikisha filter haitumiki kwa public paths
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return PUBLIC_PATHS.stream().anyMatch(path::startsWith);
    }
}