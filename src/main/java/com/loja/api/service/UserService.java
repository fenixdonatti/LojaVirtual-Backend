package com.loja.api.service;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.loja.api.dto.user.UserRequest;
import com.loja.api.dto.user.UserResponse;
import com.loja.api.entity.User;
import com.loja.api.mapper.UserMapper;
import com.loja.api.repository.UserRepository;

@Service 
public class UserService {
    
    @Autowired 
    private UserRepository userRepository;

    @Autowired 
    private UserMapper userMapper;
    
    public UserResponse getUserById(UUID id) {
        return userRepository.findById(id)
            .map(userMapper::toResponse)
            .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserResponse createUser(UserRequest userRequest) {
        User user = userMapper.toEntity(userRequest);
        UserResponse userResponse = userMapper.toResponse(userRepository.save(user));
        return userResponse;
    }

    public UserResponse updateUser(UUID id, UserRequest userRequest) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        User updatedUser = userMapper.toEntity(userRequest);
        updatedUser.setId(existingUser.getId());
        userRepository.save(updatedUser);

        return userMapper.toResponse(updatedUser);
    }

    public void deleteUser(UUID id) {
        User existingUser = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        userRepository.delete(existingUser);
    }
}
