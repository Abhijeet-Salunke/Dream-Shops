package com.shopingcart.dream_shops.service.cart;

import com.shopingcart.dream_shops.exception.ResourceNotFoundException;
import com.shopingcart.dream_shops.model.Cart;
import com.shopingcart.dream_shops.model.User;
import com.shopingcart.dream_shops.repository.CartItemRepository;
import com.shopingcart.dream_shops.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    //private final AtomicLong cartIdGenerator = new AtomicLong(0);
    private static final Long CART_ID = 1L;

    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cart Not Found"));
        BigDecimal totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        return cartRepository.save(cart);
    }

    //@Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
//        cart.getItems().clear();
//        cartRepository.deleteById(id);
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        Cart cart = getCart(id);
        return  cart.getTotalAmount();
//
//        return cart.getItems()
//                .stream()
//                .map(CartItem :: getTotalPrice)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

//    @Override
//    public Long initializeNewCart(){
//        // Check if cart with ID = 1 already exists
//        return cartRepository.findById(CART_ID).map(cart -> {
//            cart.getItems().clear();  // Clear any existing items in the cart
//            cart.setTotalAmount(BigDecimal.ZERO);  // Reset the total amount
//            cartRepository.save(cart);  // Save and reuse the existing cart
//            return CART_ID;
//        }).orElseGet(() -> {
//            Cart newCart = new Cart();
//            newCart.setId(CART_ID);  // Set cart ID to 1
//            newCart.setTotalAmount(BigDecimal.ZERO);  // Initialize total amount
//            return cartRepository.save(newCart).getId();  // Save and return the cart with ID = 1
//        });
////        Cart newCart = new Cart();
////        Long newCartId = cartIdGenerator.incrementAndGet();
////        newCart.setId(newCartId);
////        return cartRepository.save(newCart).getId();
//    }

    @Override
    public Cart initializeNewCart(User user) {
        Cart cart = getCartByUserId(user.getId());
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            user.setCart(cart);
            cart = cartRepository.save(cart);
        }
        return cart;
//        return Optional.ofNullable((getCartByUserId(user.getId())))
//                .orElseGet(() -> {
//                    Cart cart = new Cart();
//                    return cartRepository.save(cart);
//                });
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
}
