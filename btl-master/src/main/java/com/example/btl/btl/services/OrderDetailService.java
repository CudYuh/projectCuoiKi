package com.example.btl.btl.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.btl.btl.models.OrderDetail;
import com.example.btl.btl.repositories.OrderDetailRepo;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailRepo orderDetailRepo;

    public OrderDetail getOrderDetailById(int id) {
        return orderDetailRepo.findById(id).orElse(null);
    }

    public OrderDetail saveOrderDetail(OrderDetail orderDetail) {
        return orderDetailRepo.save(orderDetail);
    }

    public OrderDetail updateOrderDetail(int id, OrderDetail orderDetailDetails) {
        OrderDetail orderDetail = getOrderDetailById(id);
        if (orderDetail == null) {
            return null;
        }
        orderDetail.setOrderId(orderDetailDetails.getOrderId());
        orderDetail.setShoeId(orderDetailDetails.getShoeId());
        orderDetail.setPrice(orderDetailDetails.getPrice());
        orderDetail.setQuantity(orderDetailDetails.getQuantity());
        return orderDetailRepo.save(orderDetail);
    }

    public void deleteOrderDetail(int id) {
        orderDetailRepo.deleteById(id);
    }
}