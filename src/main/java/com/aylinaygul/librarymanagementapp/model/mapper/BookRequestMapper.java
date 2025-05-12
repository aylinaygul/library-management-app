package com.aylinaygul.librarymanagementapp.model.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.aylinaygul.librarymanagementapp.model.dto.request.BookRequest;
import com.aylinaygul.librarymanagementapp.model.entity.Book;

@Mapper(componentModel = "spring")
public interface BookRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "records", ignore = true)
    Book toEntity(BookRequest dto);

    @InheritConfiguration(name = "toEntity")
    void updateBookFromRequest(BookRequest request, @MappingTarget Book book);
}
