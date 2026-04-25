package com.shift.management.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.shift.management.entity.User;
import com.shift.management.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initAdminUser();
    }

    private void initAdminUser() {
        User admin = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, "admin")
        );

        String encodedPassword = passwordEncoder.encode("admin123");

        if (admin == null) {
            User newAdmin = new User();
            newAdmin.setUsername("admin");
            newAdmin.setPassword(encodedPassword);
            newAdmin.setRole("ADMIN");
            userMapper.insert(newAdmin);
            log.info("Admin user created");
        } else {
            userMapper.update(null,
                    new LambdaUpdateWrapper<User>()
                            .eq(User::getUsername, "admin")
                            .set(User::getPassword, encodedPassword)
            );
            log.info("Admin password reset to admin123");
        }
    }
}
