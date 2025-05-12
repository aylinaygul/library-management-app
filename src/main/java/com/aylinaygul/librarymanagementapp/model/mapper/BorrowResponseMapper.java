package com.aylinaygul.librarymanagementapp.model.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.aylinaygul.librarymanagementapp.model.dto.response.BorrowResponse;
import com.aylinaygul.librarymanagementapp.model.entity.BorrowRecord;

@Mapper(componentModel = "spring")
public interface BorrowResponseMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    BorrowResponse toDTO(BorrowRecord borrowRecord);

    List<BorrowResponse> toDTOList(List<BorrowRecord> borrowRecords);
}
