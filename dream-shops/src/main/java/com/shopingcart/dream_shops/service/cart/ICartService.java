package com.shopingcart.dream_shops.service.cart;

import com.shopingcart.dream_shops.model.Cart;
import com.shopingcart.dream_shops.model.User;

import java.math.BigDecimal;

public interface ICartService {
    Cart getCart(Long id);
    void clearCart(Long id);
    BigDecimal getTotalPrice(Long id);

//    Long initializeNewCart();

    Cart initializeNewCart(User user);

    Cart getCartByUserId(Long userId);
}
