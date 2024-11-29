package com.shopingcart.dream_shops.service.cart;
import com.shopingcart.dream_shops.model.CartItem;

import java.util.List;

public interface ICartItemService {
    void addItemToCart(Long cartId, Long productId, int quantity);
    void removeItemFromCart(Long cartId, Long productId);
    void updateItemQuantity(Long cartId, Long productId, int quantity);

    CartItem getCartItem(Long cartId, Long itemId);
}
