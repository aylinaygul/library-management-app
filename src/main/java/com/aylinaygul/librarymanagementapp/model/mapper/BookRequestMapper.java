package com.aylinaygul.librarymanagementapp.model.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.aylinaygul.librarymanagementapp.model.dto.request.BookRequestDTO;
import com.aylinaygul.librarymanagementapp.model.entity.Book;

@Mapper(componentModel = "spring")
public interface BookRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "records", ignore = true)
    Book toEntity(BookRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "records", ignore = true)
    void updateBookFromRequest(BookRequestDTO request, @MappingTarget Book book);
}
