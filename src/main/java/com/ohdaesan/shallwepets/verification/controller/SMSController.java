//package com.ohdaesan.shallwepets.verification.controller;
//
//import com.ohdaesan.shallwepets.verification.SHA256Util;
//import com.ohdaesan.shallwepets.verification.service.SMSService;
//import lombok.extern.slf4j.Slf4j;
//import net.nurigo.java_sdk.exceptions.CoolsmsException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@Slf4j
//@RestController
//// ref: https://jay-cheol.tistory.com/254
//public class SMSController {
//    private final String salt = SHA256Util.generateSalt();
//
//    @Autowired
//    private SMSService smsService;
//
//    @PostMapping("/sendSms")
//    @ResponseBody
//    public String sendSms(@RequestBody Map<String, String> map) {
//        String phone = map.get("phone");
//        try {
//            String generatedCode = smsService.sendSms(phone, salt);
//            return generatedCode;
//        } catch (CoolsmsException e) {
//            e.printStackTrace();
//            return "Failed to send SMS: " + e.getMessage();
//        }
//    }
//
//    @PostMapping("/checkSms") //
//    @ResponseBody
//    public boolean checkSms(@RequestBody Map<String, String> map) throws Exception {
//        String key = map.get("key");
//        String insertKey = map.get("insertKey");
//
//        insertKey = SHA256Util.getEncrypt(insertKey, salt);
//
//        if (key.equals(insertKey)) {
//            return true;
//        }
//        return false;
//    }
//}
