package com.example.btl.btl.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.btl.btl.models.OrderDetail;
import org.springframework.stereotype.Repository;
@Repository
public interface OrderDetailRepo extends JpaRepository<OrderDetail, Integer> {

    List<OrderDetail> findAllByOrderId(UUID id);
    void deleteAllByOrderId(UUID orderId);
}