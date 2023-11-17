package com.example.phase2;

import com.example.phase2.entity.Admin;
import com.example.phase2.entity.Customer;
import com.example.phase2.service.CustomerService;
import com.example.phase2.service.impl.AdminServiceImpl;
import com.example.phase2.service.impl.CustomerServiceImpl;
import com.example.phase2.service.impl.EmailServiceImpl;
import com.example.phase2.service.impl.ServiceCoServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
public class Phase2Application {

    public static void main(String[] args) {
        SpringApplication.run(Phase2Application.class, args);
    }

//    @Bean
//    CommandLineRunner commandLineRunner(AdminServiceImpl adminService){
//        return args -> adminService.saveOrUpdate(Admin.getInstance());
//    }

//    @Bean
//    CommandLineRunner commandLineRunner(EmailServiceImpl emailService){
//        return args -> emailService.sendSimpleMessage("alimousavi04897@gmail.com" , "sub" , "test");
//    }

}
