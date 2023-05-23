package nyan.ecommerce.spreeze.auth;

import java.io.IOException;
import java.time.Instant;
import java.util.Arrays;

import javax.mail.Header;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import nyan.ecommerce.spreeze.auth.token.AuthToken;
import nyan.ecommerce.spreeze.auth.token.VerifiedToken;
import nyan.ecommerce.spreeze.user.User;
import nyan.ecommerce.spreeze.user.UserRepository;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private AuthHelper authHelper;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String token = getTokenFromCookie(request);
            if (token == null) {
                throw new IllegalStateException("You're not logged in. Please login!");
            }

            VerifiedToken verifiedToken = authHelper.validateToken(token);

            User user = userRepository.findById(verifiedToken.getId()).orElse(null);

            if (user.getPasswordChangedAt() != null
                    && verifiedToken.getIssuedAt().before(user.getPasswordChangedAt())) {
                throw new IllegalStateException("Password has been changed! Please login!");
            }

            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null,
                    user.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    private String getTokenFromCookie(HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        String token = null;
        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.contains("Bearer ")) {
            token = authorization.split("Bearer ")[1];
        } else {
            Cookie authToken = Arrays.stream(cookies)
                    .filter(c -> "jwt".equals(c.getName()))
                    .findFirst()
                    .orElse(null);
            token = authToken != null ? authToken.getValue() : null;
        }

        return token;
    }
}
