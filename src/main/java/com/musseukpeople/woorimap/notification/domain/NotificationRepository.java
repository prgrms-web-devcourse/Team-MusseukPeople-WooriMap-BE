package com.musseukpeople.woorimap.notification.domain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("SELECT n FROM Notification n WHERE n.receiverId = :id AND n.isRead = false ")
    List<Notification> findUnreadAllByMemerId(@Param("id") Long id);
}
