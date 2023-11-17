package com.example.phase2.controller;

import io.springboot.captcha.SpecCaptcha;
import io.springboot.captcha.utils.CaptchaJakartaUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class CaptchaController {

    public static Map<Integer,String> captchaMap = new ConcurrentHashMap<>();
    public AtomicInteger counter = new AtomicInteger();
    
    @RequestMapping("/captcha")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        CaptchaJakartaUtil.out(request, response);

    }

    @GetMapping("/generateCaptcha")
    public Captcha generateCaptcha(){
        SpecCaptcha captcha = new SpecCaptcha(130, 48);
        Integer id = counter.incrementAndGet();
        captchaMap.put(id ,captcha.text());
        return new Captcha(id,captcha.toBase64());
    }

    public record Captcha(Integer id , String base64){

    }
}