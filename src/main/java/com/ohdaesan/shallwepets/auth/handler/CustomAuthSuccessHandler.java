package com.ohdaesan.shallwepets.auth.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ohdaesan.shallwepets.auth.common.AuthConstants;
import com.ohdaesan.shallwepets.auth.service.CustomUserDetails;
import com.ohdaesan.shallwepets.auth.util.ConvertUtil;
import com.ohdaesan.shallwepets.auth.util.TokenUtils;
import com.ohdaesan.shallwepets.member.domain.entity.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.json.simple.JSONObject;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/*
* 로그인 성공했을 때 동작하는 핸들러
* JWT를 생성하고 responseHeader(응답 헤더)를 넘겨주는 역할
* */
@Configuration
public class CustomAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private final ObjectMapper objectMapper;

    public CustomAuthSuccessHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        // Security Context에 저장된 로그인한 사용자 정보를 꺼내온다.
        Member member = ((CustomUserDetails) authentication.getPrincipal()).getMember();

        // Member 객체를 JSON 형태의 객체로 변환
//        JSONObject jsonValue = (JSONObject) ConvertUtil.convertObjectToJsonObject(member);

        // 토큰 생성
        String token = TokenUtils.generateJwtToken(member);

        // 응답하기 위한 Map
        Map<String, Object> responseMap = new HashMap<>();
//        responseMap.put("userInfo", jsonValue);
        responseMap.put("userInfo", member);
        responseMap.put("message", "로그인 성공");
//        responseMap.put("token", token);

        // 헤더에 토큰 담기
        response.addHeader(AuthConstants.AUTH_HEADER, AuthConstants.TOKEN_TYPE + " " + token);

        // responseMap을 JSONObject로 변환
//        JSONObject jsonObject = new JSONObject(responseMap);

        response.setContentType("application/json");    // 응답 MIME 설정
        response.setCharacterEncoding("UTF-8");         // 응답 인코딩 설정
        response.getWriter().write(objectMapper.writeValueAsString(responseMap));

        // 만든 response를 내보내기
//        PrintWriter printWriter = response.getWriter();
//        printWriter.println(jsonObject);
//        printWriter.flush();
//        printWriter.close();
    }
}
