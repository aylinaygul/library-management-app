package com.aylinaygul.librarymanagementapp.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.aylinaygul.librarymanagementapp.model.dto.response.UserResponse;
import com.aylinaygul.librarymanagementapp.model.entity.User;

@Mapper(componentModel = "spring")
public interface UserResponseMapper {

    UserResponse toDTO(User user);

    List<UserResponse> toDTOList(List<User> users);
}
