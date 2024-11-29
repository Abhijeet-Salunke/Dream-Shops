package com.shopingcart.dream_shops.security.user;

import com.shopingcart.dream_shops.model.User;
import com.shopingcart.dream_shops.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ShopUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = Optional.ofNullable(userRepository.findUserByEmail(email))
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return ShopUserDetails.buildUserDetails(user);
    }
}
