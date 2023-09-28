package com.windcoder.nightbook.common.repository;

import com.windcoder.nightbook.common.entity.Book;

public interface BookRepository extends SupportRepository<Book,Long> {
    Book findOneByIsbn(String isbin);
}
