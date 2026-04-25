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
        log.info("[DataInitializer] Waiting 5s for database to be fully ready...");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.warn("[DataInitializer] Sleep interrupted, proceeding immediately");
        }
        initAdminUser();
    }

    private void initAdminUser() {
        log.info("[DataInitializer] Starting admin user initialization...");
        try {
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
                log.info("[DataInitializer] Admin user created successfully");
            } else {
                userMapper.update(null,
                        new LambdaUpdateWrapper<User>()
                                .eq(User::getUsername, "admin")
                                .set(User::getPassword, encodedPassword)
                );
                log.info("[DataInitializer] Admin password reset to admin123");
            }
        } catch (Exception e) {
            log.error("[DataInitializer] Failed to initialize admin user, app will continue running: {}", e.getMessage(), e);
        }
    }
}
