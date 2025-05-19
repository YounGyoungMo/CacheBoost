package com.example.CacheBoost.domain.book.repository;

import com.example.CacheBoost.common.exception.base.CustomException;
import com.example.CacheBoost.common.exception.enums.ErrorCode;
import com.example.CacheBoost.domain.book.entity.Book;
import com.example.CacheBoost.domain.book.entity.Status;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {

    default Book findByIdOrElseThrow (Long id){
        return findById(id).orElseThrow(()-> new CustomException(ErrorCode.BOOK_NOT_FOUND));
    }

    List<Book> findByAllBookName(String bookName);

}
