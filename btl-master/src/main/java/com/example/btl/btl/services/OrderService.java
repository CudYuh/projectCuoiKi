package com.example.btl.btl.services;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.example.btl.btl.dtos.CartItem;
import com.example.btl.btl.dtos.CheckoutModelDTO;
import com.example.btl.btl.dtos.OrderFullDetail;
import com.example.btl.btl.dtos.ShoeOrderDetail;
import com.example.btl.btl.models.Order;
import com.example.btl.btl.models.OrderDetail;
import com.example.btl.btl.models.Shoe;
import com.example.btl.btl.models.ShoeDetail;
import com.example.btl.btl.models.User;
import com.example.btl.btl.repositories.OrderDetailRepo;
import com.example.btl.btl.repositories.OrderRepo;
import com.example.btl.btl.repositories.ShoeDetailRepo;
import com.example.btl.btl.repositories.ShoeRepo;

@Service
public class OrderService {
    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private OrderDetailRepo orderDetailRepo;

    @Autowired
    private ShoeDetailRepo shoeDetailRepo;

    @Autowired
    private ShoeRepo shoeRepo;

    public Page<Order> getAllOrders(Pageable pageable) {
        return orderRepo.findAll(pageable);
    }

    public OrderFullDetail getOrderById(UUID id) {
        Order order = orderRepo.findById(id).orElse(null);
        List<OrderDetail> orderDetails = orderDetailRepo.findAllByOrderId(id);
        List<ShoeOrderDetail> shoeOrderDetails = new ArrayList<ShoeOrderDetail>();
        for (OrderDetail orderDetail : orderDetails) {
            ShoeOrderDetail shoeOrderDetail = new ShoeOrderDetail();

            Shoe shoe = shoeRepo.findById(orderDetail.getShoeId()).orElse(null);
            if (shoe != null) {
                shoeOrderDetail.setTitle(shoe.getTitle());
                shoeOrderDetail.setImage(shoe.getImage());
            }
            shoeOrderDetail.setOrderDetail(orderDetail);
            shoeOrderDetails.add(shoeOrderDetail);
        }
        return new OrderFullDetail(order, shoeOrderDetails);
    }

    @Transactional
    public void saveOrder(OrderFullDetail createOrderDTO) throws Exception {
        Order order = createOrderDTO.getOrder();
        List<ShoeOrderDetail> shoeOrderDetails = createOrderDTO.getOrderDetails();

        order.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        order.setStatus(0);

        orderRepo.save(order);

        for (ShoeOrderDetail shoeOrderDetail : shoeOrderDetails) {
            OrderDetail orderDetail = shoeOrderDetail.getOrderDetail();
            ShoeDetail shoeDetail = shoeDetailRepo.findByShoeIdAndColorAndSize(
                    orderDetail.getShoeId(), orderDetail.getColor(), orderDetail.getSize());
            orderDetail.setOrderId(order.getId());
            orderDetailRepo.save(orderDetail);
            shoeDetailRepo.save(shoeDetail);
        }
    }

    public Order updateOrder(UUID id, Order orderDetails) {
        OrderFullDetail fullOrderDetail = getOrderById(id);
        Order order = fullOrderDetail.getOrder();
        if (order == null) {
            return null;
        }
        order.setLastUpdatedBy(orderDetails.getLastUpdatedBy());
        order.setLastUpdatedTime(new Timestamp(System.currentTimeMillis()));
        order.setName(orderDetails.getName());
        order.setEmail(orderDetails.getEmail());
        order.setPhone(orderDetails.getPhone());
        order.setAddress(orderDetails.getAddress());
        order.setNote(orderDetails.getNote());
        order.setStatus(orderDetails.getStatus());
        return orderRepo.save(order);
    }

    @Transactional
    public void deleteOrder(String orderId) throws Exception {
        Order order = orderRepo.findById(UUID.fromString(orderId)).orElse(null);
        if (order == null) {
            return;
        }

        int order_status = order.getStatus();
        if (order_status == 0 || order_status == 1 || order_status == 2 || order_status == -1)
            throw new Exception("Không thể xóa do đang chờ xác nhận, chờ giao, hoặc đang giao");
        // delete the order and the order details
        orderDetailRepo.deleteAllByOrderId(UUID.fromString(orderId));
        orderRepo.deleteById(UUID.fromString(orderId));
    }

    @Transactional
    public void updateOrderStatus(String orderId, int newStatus, String adminId) {
        Order order = orderRepo.findById(UUID.fromString(orderId)).orElse(null);
        if (order == null) {
            return;
        }

        order.setLastUpdatedBy(adminId);
        order.setLastUpdatedTime(new Timestamp(System.currentTimeMillis()));
        order.setStatus(newStatus);
        orderRepo.save(order);
    }

    @Transactional
    public UUID createFullOrder(CheckoutModelDTO checkoutModelDTO) throws Exception {
        if (checkoutModelDTO.getCartItems().isEmpty())
            throw new Exception("cartItems is null");
        // Tạo đơn đặt hàng
        Order order = new Order();
        order.setName(checkoutModelDTO.getName());
        order.setEmail(checkoutModelDTO.getEmail());
        order.setPhone(checkoutModelDTO.getPhone());
        order.setAddress(checkoutModelDTO.getAddress());
        order.setNote(checkoutModelDTO.getNote());
        order.setUserId(checkoutModelDTO.getUserId());
        order.setStatus(0);
        order.setCreatedTime(new Timestamp(System.currentTimeMillis()));
        orderRepo.save(order);

        // Lưu chi tiết đơn đặt hàng
        UUID orderId = order.getId();
        List<CartItem> cartItems = checkoutModelDTO.getCartItems();
        for (CartItem cartItem : cartItems) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrderId(orderId);
            orderDetail.setShoeId(cartItem.getShoeId());
            orderDetail.setPrice(cartItem.getPrice());
            orderDetail.setColor(cartItem.getColor());
            orderDetail.setSize(cartItem.getSize());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetailRepo.save(orderDetail);
        }

        return order.getId();
    }

    public List<Order> getOrdersByUser(User user) {
        return orderRepo.findByEmailOrPhoneOrUserId(user.getEmail(), user.getPhone(), user.getId());
    }
}