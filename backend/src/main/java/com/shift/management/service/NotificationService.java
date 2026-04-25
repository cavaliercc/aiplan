package com.shift.management.service;

import com.shift.management.entity.Notification;

import java.util.List;

public interface NotificationService {
    List<Notification> getByUsername(String username);
    void markAsRead(Integer id);
}
