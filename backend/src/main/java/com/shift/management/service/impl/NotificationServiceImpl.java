package com.shift.management.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.shift.management.entity.Notification;
import com.shift.management.entity.User;
import com.shift.management.mapper.NotificationMapper;
import com.shift.management.mapper.UserMapper;
import com.shift.management.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationMapper notificationMapper;
    private final UserMapper userMapper;

    @Override
    public List<Notification> getByUsername(String username) {
        User user = userMapper.selectOne(
                new LambdaQueryWrapper<User>().eq(User::getUsername, username)
        );
        if (user == null) return Collections.emptyList();
        return notificationMapper.selectList(
                new LambdaQueryWrapper<Notification>()
                        .eq(Notification::getTargetUser, user.getId())
                        .orderByDesc(Notification::getCreatedAt)
        );
    }

    @Override
    public void markAsRead(Integer id) {
        Notification n = notificationMapper.selectById(id);
        if (n == null) throw new IllegalArgumentException("通知不存在: " + id);
        n.setIsRead(true);
        notificationMapper.updateById(n);
    }
}
