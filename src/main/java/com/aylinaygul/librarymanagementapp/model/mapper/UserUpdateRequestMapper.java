package com.aylinaygul.librarymanagementapp.model.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.aylinaygul.librarymanagementapp.model.dto.request.UserUpdateRequest;
import com.aylinaygul.librarymanagementapp.model.entity.User;

@Mapper(componentModel = "spring")
public interface UserUpdateRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", ignore = true)
    User toEntity(UserUpdateRequest dto);

    @InheritConfiguration(name = "toEntity")
    void updateUserFromRequest(UserUpdateRequest request, @MappingTarget User user);
}
