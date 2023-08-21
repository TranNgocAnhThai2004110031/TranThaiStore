package com.tranthai.tranthaistore.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.tranthai.tranthaistore.service.UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private UserService userService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    // Cấu hình quyền truy cập và bảo mật cho các đường dẫn trong ứng dụng
    http.authorizeRequests()
        .antMatchers("/register**", "/", "/shop/**") // Các đường dẫn này được cấp quyền truy cập cho tất cả
        .permitAll()
        .antMatchers("/admin/**").hasRole("ADMIN") // Đường dẫn bắt đầu bằng "/admin" yêu cầu quyền "ADMIN"
        .antMatchers("/users/**").hasRole("USER") // Đường dẫn bắt đầu bằng "/users" yêu cầu quyền "USER"
        .anyRequest().authenticated() // Các request còn lại yêu cầu đã đăng nhập
        .and()
        .exceptionHandling()
        .accessDeniedPage("/403") // Xử lý khi người dùng không có quyền truy cập
        .and()
        .formLogin()
        .loginPage("/login") // Đường dẫn đến trang đăng nhập
        .successHandler(new SavedRequestAwareAuthenticationSuccessHandler()) // Xử lý sau khi đăng nhập thành công
        .defaultSuccessUrl("/") // Chuyển hướng sau khi đăng nhập thành công đến trang "/"
        .permitAll() // Cho phép tất cả truy cập trang đăng nhập
        .and()
        .logout()
        .invalidateHttpSession(true) // Hủy bỏ session sau khi đăng xuất
        .clearAuthentication(true) // Xóa thông tin xác thực sau khi đăng xuất
        .logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // Đường dẫn đăng xuất
        .logoutSuccessHandler(logoutSuccessHandler()) // Xử lý sau khi đăng xuất thành công
        .permitAll(); // Cho phép tất cả truy cập trang đăng xuất

    return http.build(); // Trả về đối tượng SecurityFilterChain đã được cấu hình
}


    private LogoutSuccessHandler logoutSuccessHandler() {
        return (request, response, authentication) -> {
            // CartController.clearCart();
            response.sendRedirect("/login");
        };
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }
    // @Bean
    // public UserDetailsService userDetailsService() {
    //     return new YourUserDetailsService(); // Thay YourUserDetailsService() bằng lớp cung cấp thông tin người dùng của bạn
    // }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() throws Exception {
        return (web) -> web.ignoring().antMatchers("/resources/**", "/static/**", "/images/**", "/css/**", "/js/**",
                "/error");
    }// Bỏ xác minh các package đường dẫn này

}