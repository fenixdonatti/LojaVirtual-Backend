package com.loja.api.mapper;

import org.springframework.stereotype.Component;

import com.loja.api.dto.user.UserRequest;
import com.loja.api.dto.user.UserResponse;
import com.loja.api.entity.User;

@Component
public class UserMapper {

    public User toEntity(UserRequest request) {
        User user = new User();

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setPassword(request.password());

        return user;
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
            user.getId(),
            user.getFirstName(),
            user.getLastName(),
            user.getEmail()
        );
    }
}