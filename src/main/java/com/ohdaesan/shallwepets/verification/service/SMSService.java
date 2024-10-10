package com.ohdaesan.shallwepets.verification.service;

import com.ohdaesan.shallwepets.verification.SHA256Util;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

@Service
public class SMSService {
    @Value("${coolsms.api.key}")
    private String apiKey;

    @Value("${coolsms.api.secret}")
    private String apiSecret;

    @Value("${coolsms.api.number}")
    private String fromPhoneNumber;

    public String sendSms(String to, String salt) throws CoolsmsException {
        try {
            UUID uuid = UUID.randomUUID();
            String key = uuid.toString().substring(0,6);
//            String key = generateRandomNumber();

            Message sms = new Message(apiKey, apiSecret); // 생성자를 통해 API 키와 API 시크릿 전달

            HashMap<String, String> params = new HashMap<>();
            params.put("to", to);                   // 수신 전화번호
            params.put("from", fromPhoneNumber);    // 발신 전화번호
            params.put("type", "sms");
            params.put("text", "[쉘위펫즈] 인증번호는 [" + key + "] 입니다.");

            // 메시지 전송
            sms.send(params);

            key = SHA256Util.getEncrypt(key, salt);

            return key;     // 생성된 인증번호 암호화해서 반환
        } catch (Exception e) {
            throw new CoolsmsException("Failed to send SMS", 1);
        }
    }

     // 랜덤한 4자리 숫자 생성 메서드
    private String generateRandomNumber() {
        Random rand = new Random();
        StringBuilder numStr = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            numStr.append(rand.nextInt(10));
        }
        return numStr.toString();
    }
}
