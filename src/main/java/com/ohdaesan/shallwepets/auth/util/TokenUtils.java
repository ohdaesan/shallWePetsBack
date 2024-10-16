package com.ohdaesan.shallwepets.auth.util;

import com.ohdaesan.shallwepets.auth.common.AuthConstants;
import com.ohdaesan.shallwepets.member.domain.entity.Member;
import io.jsonwebtoken.*;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 토큰을 관리하기 위한 utils 모음 클래스
 *  yml -> jwt-key, jwt-time 설정이 필요하다.
 *  jwt lib 버전 "io.jsonwebtoken:jjwt:0.9.1" 사용
 * */
@Component
public class TokenUtils {
    private static String jwtSecretKey;
    private static Long tokenValidateTime;

    @Value("${jwt.key}")
    public void setJwtSecretKey(String jwtSecretKey) {
        TokenUtils.jwtSecretKey = jwtSecretKey;
    }

    @Value("${jwt.time}")
    public void setTokenValidateTime(Long tokenValidateTime) {
        TokenUtils.tokenValidateTime = tokenValidateTime;
    }

    /**
     * header의 token을 분리하는 메서드
     * @param header: Authorization의 header값을 가져온다.
     * @return token: Authorization의 token 부분을 반환한다.
     * */
//    public static String splitHeader(String header){
////        if(!header.equals("")){
////            return header.split(" ")[1];    // 'bearer' 제외한 토큰
////        } else {
////            return null;
////        }
////         헤더가 null이 아니고 비어있지 않은지 확인
//        if (header != null && !header.isEmpty()) {
//            // 헤더가 "Bearer "로 시작하는지 확인
//            if (header.toLowerCase().startsWith(AuthConstants.TOKEN_TYPE.toLowerCase() + " ")) {
//                // "Bearer " 이후의 토큰을 반환
//                return header.substring(AuthConstants.TOKEN_TYPE.length() + 1); // "Bearer "의 길이 + 공백
//            } else {
//                throw new IllegalArgumentException("Invalid Authorization header format");
//            }
//        }
//        throw new IllegalArgumentException("Authorization header is empty or null");
//    }
    public static String splitHeader(String header){
        if (header != null && !header.isEmpty()) {
            // "Bearer "가 두 번 중복되어 있으면 이를 제거
            String cleanedHeader = header.replaceFirst("(?i)^Bearer\\sBearer\\s", "Bearer ");

            if (cleanedHeader.toLowerCase().startsWith(AuthConstants.TOKEN_TYPE.toLowerCase() + " ")) {
                return cleanedHeader.substring(AuthConstants.TOKEN_TYPE.length() + 1); // "Bearer " 이후의 토큰 반환
            } else {
                throw new IllegalArgumentException("Invalid Authorization header format: Bearer missing");
            }
        }
        throw new IllegalArgumentException("Authorization header is empty or null");
    }

    /**
     * 유효한 토큰인지 확인하는 메서드
     * @param token : 토큰
     * @return boolean : 유효 여부
     * @throws ExpiredJwtException, {@link JwtException} {@link NullPointerException}
     * */
    public static boolean isValidToken(String token){
//        try {
//            Claims claims = getClaimsFromToken(token);
//            return true;
//        } catch (ExpiredJwtException e){
//            e.printStackTrace();
//            return false;
//        } catch (JwtException e){
//            e.printStackTrace();
//            return false;
//        } catch (NullPointerException e){
//            e.printStackTrace();
//            return false;
//        }
        if (token == null || token.isEmpty()) {
            System.out.println("토큰이 없어 없다구");
            return false;
        }
        try {
            Claims claims = getClaimsFromToken(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("토큰이 만료되었습니다.");
            e.printStackTrace();
            return false;
        } catch (JwtException e) {
            System.out.println("유효하지 않은 토큰입니다.");
            e.printStackTrace();
            return false;
        } catch (NullPointerException e) {
            System.out.println("토큰이 존재하지 않는다고 한다 열받는다");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 토큰을 복호화 하는 메서드
     * @param token
     * @return Claims
     * */
    public static Claims getClaimsFromToken(String token){
        System.out.println("TokenUtil ======> GetClaimsFromToken : " + token);
        return Jwts.parser().setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecretKey))   // 파싱을 통해 시크릿 키가 맞는지 확인
                .parseClaimsJws(token).getBody();
    }

    /**
     * token을 생성하는 메서드
     * @param member 사용자객체
     * @return String - token
     * */
    public static String generateJwtToken(Member member) {
        // 토큰 만료 시간 설정
        Date expireTime = new Date(System.currentTimeMillis() + tokenValidateTime);

        // 토큰 생성
        JwtBuilder builder = Jwts.builder()
                // 토큰 헤더 설정
                .setHeader(createHeader())

                // 토큰에 담길 payload 설정
                .setClaims(createClaims(member))
                .setSubject(member.getMemberEmail())
                .setExpiration(expireTime)

                // 토큰 시그니처 설정
                .signWith(SignatureAlgorithm.HS256, createSignature()); // secret key

        return builder.compact();
    }

    /**
     * token의 header를 설정하는 부분이다.
     * @return Map<String, Object> - header의 설정 정보
     * */
    private static Map<String, Object> createHeader(){
        Map<String, Object> header = new HashMap<>();

        // 토큰 타입
        header.put("type", "jwt");

        // 토큰에 사용된 알고리즘
        header.put("alg", "HS256");

        // 토큰 생성일
        header.put("date", System.currentTimeMillis());

        return header;
    }

    /**
     * 사용자 정보를 기반으로 클레임을 생성해주는 메서드
     *
     * @param member - 사용자 정보
     * @return Map<String, Object> - cliams 정보
     * */
    private static Map<String, Object> createClaims(Member member){
        Map<String, Object> claims = new HashMap<>();

        claims.put("memberName", member.getMemberName());
        claims.put("memberRole", member.getMemberRole());
        claims.put("memberEmail", member.getMemberEmail());
        claims.put("memberNo", member.getMemberNo());

        return claims;
    }

    /**
     * JWT 서명을 발급해주는 메서드이다.
     *
     * @return key
     * */
    private static Key createSignature(){
        // HS256 알고리즘을 사용해서 Base64로 인코딩된 비밀키를 바이트 배열로 변환
        byte[] secretBytes = DatatypeConverter.parseBase64Binary(jwtSecretKey);

        // signature에 사용할 수 있는 key 객체로 변환
        return new SecretKeySpec(secretBytes, SignatureAlgorithm.HS256.getJcaName());
    }
}
