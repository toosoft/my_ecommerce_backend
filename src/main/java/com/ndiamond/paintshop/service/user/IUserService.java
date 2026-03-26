package com.ndiamond.paintshop.service.user;

import com.ndiamond.paintshop.dto.UserDto;
import com.ndiamond.paintshop.model.User;
import com.ndiamond.paintshop.request.CreateUserRequest;
import com.ndiamond.paintshop.request.UserUpdateRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UserUpdateRequest request, Long userId);
    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);

    User getAuthenticatedUser();
}


