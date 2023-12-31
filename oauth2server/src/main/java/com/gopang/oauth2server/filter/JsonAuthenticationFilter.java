package com.gopang.oauth2server.filter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

public class JsonAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String CONTENT_TYPE = "application/json";
    private static final String SPRING_SECURITY_FORM_USERNAME_KEY = "username";
    private static final String SPRING_SECURITY_FORM_PASSWORD_KEY = "password";
    private static final String a= "client_id";
    private static final String DEFAULT_FILTER_PROCESSES_URL = "/oauth2/login";
    private final ObjectMapper objectMapper;


    public JsonAuthenticationFilter(ObjectMapper objectMapper, AuthenticationManager authenticationManager) {
        super(DEFAULT_FILTER_PROCESSES_URL, authenticationManager);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)) {
            throw new AuthenticationServiceException("Authentication Content-Type not supported: " + request.getContentType());
        }

        Map<String, String> parameter = objectMapper.readValue(request.getInputStream(), Map.class);
        String username = parameter.get(SPRING_SECURITY_FORM_USERNAME_KEY);
        String password = parameter.get(SPRING_SECURITY_FORM_PASSWORD_KEY);
        String client_id = parameter.get(a);

        System.out.println("********************************************************");
        System.out.println(username);
        System.out.println(password);
        System.out.println(client_id);

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        //  client_id 외 정보를 프로바이더 메니저를 통해서 확인

        // authrization 제너레이터를 통해서 인증코드를 만들어서 response 에 응답하면된다

        String redirectUrl = "/";
        response.sendRedirect(redirectUrl);
    }
}

