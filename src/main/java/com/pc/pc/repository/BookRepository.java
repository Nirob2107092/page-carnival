package com.pc.pc.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pc.pc.model.Book;
import com.pc.pc.model.User;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findBySeller(User seller);
}