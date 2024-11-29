package com.shopingcart.dream_shops.service.order;

import com.shopingcart.dream_shops.dto.OrderDto;
import com.shopingcart.dream_shops.enums.OrderStatus;
import com.shopingcart.dream_shops.exception.ResourceNotFoundException;
import com.shopingcart.dream_shops.model.Cart;
import com.shopingcart.dream_shops.model.Order;
import com.shopingcart.dream_shops.model.OrderItem;
import com.shopingcart.dream_shops.model.Product;
import com.shopingcart.dream_shops.repository.OrderRepository;
import com.shopingcart.dream_shops.repository.ProductRepository;
import com.shopingcart.dream_shops.service.cart.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

@Service
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, CartService cartService, ModelMapper modelMapper) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.cartService = cartService;
        this.modelMapper = modelMapper;
    }


    //@Transactional
    @Override
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);

        if (cart.getUser() == null || !cart.getUser().getId().equals(userId)) {
            throw new IllegalStateException("Cart user does not match requested user");
        }
        Order order = createOrder(cart);
        List<OrderItem> orderItemsList = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItemsList));
        order.setTotalAmount(calculateTotalAmount(orderItemsList));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());

        return savedOrder;
    }

    private Order createOrder(Cart cart){
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderStatus(OrderStatus.PENDING);
        order.setOrderDate(LocalDate.now());
        return order;
    }
    private List<OrderItem> createOrderItems(Order order, Cart cart){
        return cart.getItems().stream().map(cartItem ->{
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);
            return new OrderItem(
                            order,
                            product,
                            cartItem.getQuantity(),
                            cartItem.getUnitPrice()
            );
        }).toList();
    }

    public BigDecimal calculateTotalAmount(List<OrderItem> orderItemList){
        return orderItemList.stream()
                .map(item -> item.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .map(this :: convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    @Override
    public List<OrderDto> getUserOrder(Long userId){
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream()
                .map(this :: convertToDto)
                .toList();
    }

    @Override
    public OrderDto convertToDto(Order order){
        return modelMapper.map(order, OrderDto.class);
    }
}
