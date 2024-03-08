package com.kidchang.lingopress.jwt;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.kidchang.lingopress._base.constant.Code;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    Map<String, String> map = new HashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        // UTF-8 인코딩 에러 해결
        response.setContentType("text/html;charset=utf-8");
        try {
            filterChain.doFilter(request, response);
        } catch (JwtException ex) {
            String message = ex.getMessage();
            //토큰 만료된 경우
            if (Code.EXPIRED_TOKEN.getMessage().equals(message)) {
                setResponse(response, Code.EXPIRED_TOKEN);
            } else if (Code.NOT_SUPPORTED_TOKEN.getMessage().equals(message)) {
                setResponse(response, Code.NOT_SUPPORTED_TOKEN);
            } else if (Code.NOT_SIGNATURE_TOKEN.getMessage().equals(message)) {
                setResponse(response, Code.NOT_SIGNATURE_TOKEN);
            } else if (Code.MALFORMED_TOKEN.getMessage().equals(message)) {
                setResponse(response, Code.MALFORMED_TOKEN);
            } else if (Code.NOT_ACCESS_TOKEN.getMessage().equals(message)) {
                setResponse(response, Code.NOT_ACCESS_TOKEN);
            } else {
                setResponse(response, Code.JWT_UNKNOWN_ERROR);
            }
        }
    }

    private void setResponse(HttpServletResponse response, Code errorCode)
        throws RuntimeException, IOException {
        response.setStatus(401);
        JSONObject responseJson = new JSONObject();
        responseJson.put("message", errorCode.getMessage());

        response.getWriter().print(responseJson);
    }
}
