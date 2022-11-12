package com.api.security.web.config.security;

import com.api.security.domain.service.impl.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.http.HttpServletRequest;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // inyeccion de dependencia jwt util
    @Autowired
    private JwtUtil jwtUtil;

    // inyeccion de dependencia user details service
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, javax.servlet.http.HttpServletResponse response,
                                    javax.servlet.FilterChain filterChain) throws javax.servlet.ServletException,
                                    java.io.IOException {
        // obtener el token del header
        String authorizationHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // validar que el token no sea nulo y que empiece con la palabra Bearer
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            token = authorizationHeader.substring(7);
            username = jwtUtil.extractUsername(token);

            try{
                username = jwtUtil.extractUsername(token);
            }catch (ExpiredJwtException e){
                System.out.println("Token expirado");
            }catch (Exception e){
                e.printStackTrace();
            }

        }else {
            System.out.println("Token invalido, no empieza con Bearer string");
        }


        // validar que el username no sea nulo y que el contexto de seguridad no tenga un usuario autenticado
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // validar que el token sea valido
            if (jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                        = new UsernamePasswordAuthenticationToken
                        (userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            } else {
                System.out.println("Token invalido");
            }
        }
        filterChain.doFilter(request, response);
    }

}
