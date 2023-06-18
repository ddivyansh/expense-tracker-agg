package com.example.expensetracker.Filters;

import com.example.expensetracker.Constants.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;


/*
@WebFilter annotation -- it's a filter class, and it's bean would be injected by spring,
however we have to make sure we enable bean filter scanning by using @ServletComponentScan.

the filter woould only filter requests for "/api/categories/*" endpoint.
 */
@WebFilter("/api/categories/*")
public class AuthFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        Optional<String> cookie = readCookieValue(httpRequest);
        if (cookie.isPresent()) {
            String token = cookie.get();
            try {
                Claims claims = Jwts.parser().setSigningKey(Constants.API_SECRET_KEY).parseClaimsJws(token).getBody();
                httpRequest.setAttribute("userid", Integer.parseInt(claims.get("userId").toString()));
            } catch (Exception e) {
                httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "invalid/expired token");
                return;
            }
        } else {
            httpResponse.sendError(HttpStatus.FORBIDDEN.value(), "User is not authenticated !");
            return;
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private Optional<String> readCookieValue(HttpServletRequest request) {
        return Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().equals("user-id")).map(Cookie::getValue).findAny();
    }
}
