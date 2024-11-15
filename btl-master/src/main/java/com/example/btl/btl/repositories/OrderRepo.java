package com.example.btl.btl.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.btl.btl.models.Order;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepo extends JpaRepository<Order, UUID> {
    @Modifying
    @Query("UPDATE Order o SET o.status = :newStatus WHERE o.id = :orderId")
    void updateOrderStatus(@Param("orderId") String orderId, @Param("newStatus") int newStatus);

    List<Order> findByEmailOrPhoneOrUserId(String email, String phone, String userId);
}