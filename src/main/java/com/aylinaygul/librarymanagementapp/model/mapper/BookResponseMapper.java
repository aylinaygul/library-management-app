package com.aylinaygul.librarymanagementapp.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.aylinaygul.librarymanagementapp.model.dto.response.BookResponseDTO;
import com.aylinaygul.librarymanagementapp.model.entity.Book;

@Mapper(componentModel = "spring")
public interface BookResponseMapper {
    BookResponseMapper INSTANCE = Mappers.getMapper(BookResponseMapper.class);

    BookResponseDTO toDTO(Book book);

    List<BookResponseDTO> toDTOList(List<Book> books);
}
