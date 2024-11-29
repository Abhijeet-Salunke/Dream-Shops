package com.shopingcart.dream_shops.service.order;

import com.shopingcart.dream_shops.dto.OrderDto;
import com.shopingcart.dream_shops.model.Order;
import com.shopingcart.dream_shops.model.OrderItem;

import java.math.BigDecimal;
import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);
    List<OrderDto> getUserOrder(Long userId);

    OrderDto convertToDto(Order order);
}
