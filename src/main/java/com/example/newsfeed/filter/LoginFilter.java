package com.example.newsfeed.filter;

import com.example.newsfeed.config.Const;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ErrorResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

import static com.example.newsfeed.exception.ErrorCode.INVALID_LOGIN;

public class LoginFilter implements Filter {

    /**
     * 로그인 필터 화이트리스트
     */
    private static final String[] WHITE_LIST = {"/", "/api/users/signup", "/api/users/login"};

    /**
     * @param request  유저 정보가 담겨 있는 객체
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        try{
            //화이트 리스트에 포함되지 않은 경우
            if (!isWhiteList(requestURI)) {

                HttpSession session = httpRequest.getSession(false);

                if (session == null || session.getAttribute(Const.SESSION_KEY) == null) {
                    throw new CustomException(INVALID_LOGIN);
                }
            }

            chain.doFilter(request, response);
        }catch (CustomException e){
            httpResponse.setStatus(e.getErrorCode().getHttpStatus().value());
            httpResponse.setContentType("application/json;charset=UTF-8");

            ErrorResponseEntity errorResponse = ErrorResponseEntity.builder()
                    .status(e.getErrorCode().getHttpStatus().value())
                    .code(e.getErrorCode().name())
                    .message(e.getErrorCode().getMessage())
                    .detailMessage(e.getDetailMessage())
                    .build();

            ObjectMapper objectMapper = new ObjectMapper();
            String jsonResponse = objectMapper.writeValueAsString(errorResponse);

            httpResponse.getWriter().write(jsonResponse);
        }


    }

    /**
     * @param requestURI 검사할 URL
     * @return
     */
    private boolean isWhiteList(String requestURI) {
        return PatternMatchUtils.simpleMatch(WHITE_LIST, requestURI);
    }
}
