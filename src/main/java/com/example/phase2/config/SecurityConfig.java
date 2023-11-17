package com.example.phase2.config;

import com.example.phase2.repository.UserRepository;
import com.example.phase2.service.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder,
                                                               UserDetailsServiceImpl userDetailsService) {
        DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();
        daoAuthProvider.setPasswordEncoder(passwordEncoder);
        daoAuthProvider.setUserDetailsService(userDetailsService);
        return daoAuthProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   AuthenticationProvider authenticationProvider) throws Exception {
        return httpSecurity
                .cors(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy
                                (SessionCreationPolicy.STATELESS))
                .httpBasic(Customizer.withDefaults())
                .authenticationProvider(authenticationProvider)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }







    //    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity ,
//                                                   AuthenticationProvider authenticationProvider) throws Exception {
//        return httpSecurity
//                .cors(Customizer.withDefaults())
//                .csrf(AbstractHttpConfigurer::disable)
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
////                .authorizeHttpRequests( a -> a.requestMatchers("/api/v1/admin").hasRole("ADMIN"))
////                .authorizeHttpRequests( a -> a.requestMatchers("/api/v1/customer").hasRole("CUSTOMER"))
////                .authorizeHttpRequests( a -> a.requestMatchers("/api/v1/expert").hasRole("EXPERT"))
////                .authorizeHttpRequests(a -> a.requestMatchers("").permitAll())
//                .authorizeHttpRequests(a -> a.anyRequest().permitAll())
//                .httpBasic(Customizer.withDefaults())
//                .authenticationProvider(authenticationProvider)
//                .build();
//
//    }





//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//        http
//                .cors(AbstractHttpConfigurer::disable)
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(a -> a.anyRequest().permitAll())
////                .authorizeHttpRequests(a -> a.requestMatchers("/user").hasRole("ADMIN")
////                        .requestMatchers("/user").hasRole("USER")
////                        .requestMatchers("expert").hasRole("EXPERT"))
//                .httpBasic(withDefaults());
//        return http.build();
//    }

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
//        auth.userDetailsService((username -> (UserDetails) userRepository
//                .findUserByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username + " not found"))
//        ));
//    }
}
