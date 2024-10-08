package com.ohdaesan.shallwepets.verification.controller;

import com.ohdaesan.shallwepets.verification.MailManager;
import com.ohdaesan.shallwepets.verification.SHA256Util;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Controller
// ref: https://velog.io/@kimtaehyeun/IT-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8%EC%87%BC%ED%95%91%EB%AA%B0-%EB%A7%8C%EB%93%A4%EA%B8%B0-Spring-Boot-%EC%8A%A4%ED%94%84%EB%A7%81-%EB%B6%80%ED%8A%B8-%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8%EC%87%BC%ED%95%91%EB%AA%B0-%EB%A7%8C%EB%93%A4%EA%B8%B0-SpringBoot-Java-Mail-Sender%EC%9D%B4%EB%A9%94%EC%9D%BC-%EB%B3%B8%EC%9D%B8-%EC%9D%B8%EC%A6%9D
public class MailController {
//    private final MailManager mailManager;
//    private final String salt = SHA256Util.generateSalt();
//
//    public MailController(MailManager mailManager) {
//        this.mailManager = mailManager;
//    }
//
//    @PostMapping("/sendMail")
//    @ResponseBody
//    public String sendMail(@RequestBody Map<String, String> map) throws Exception {
//        String email = map.get("email");
//
//        UUID uuid = UUID.randomUUID();
//        String key = uuid.toString().substring(0,6);
//        String subject = "🐕 Shall We Pets 인증번호 🐈";
//        String content = "인증 번호 : " + key;
//
//        mailManager.send(email, subject, content);
//        key = SHA256Util.getEncrypt(key, salt);
//
//        return key;
//    }
//
//    @PostMapping("/checkMail") //
//    @ResponseBody
//    public boolean CheckMail(@RequestBody Map<String, String> map) throws Exception {
////        String email = map.get("email");
//        String key = map.get("key");
//        String insertKey = map.get("insertKey");
//
//        insertKey = SHA256Util.getEncrypt(insertKey, salt);
//
//        if(key.equals(insertKey)) {
//            return true;
//        }
//        return false;
//    }
}

