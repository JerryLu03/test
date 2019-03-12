package com.baidu.dao;

import com.baidu.entity.Book;

import java.util.List;

public interface BookDao {
    //查询全部图书列表
    List<Book> findAllBooks();
}
