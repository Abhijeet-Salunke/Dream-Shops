package com.shopingcart.dream_shops.service.user;

import com.shopingcart.dream_shops.dto.UserDto;
import com.shopingcart.dream_shops.model.User;
import com.shopingcart.dream_shops.request.CreateUserRequest;
import com.shopingcart.dream_shops.request.UpdateUserRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest user);
    User updateUser(UpdateUserRequest user, Long userId);
    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);

    User getAuthenticatedUser();
}
