package com.baidu.impl;

import com.baidu.dao.BookDao;
import com.baidu.entity.Book;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDaoImpl implements BookDao {
    /**
     * 查询全部图书列表
     */
    @Override
    public List<Book> findAllBooks() {
        // 创建结果集集合
        List<Book> bookList = new ArrayList<Book>();

        Connection connection = null;
        PreparedStatement psmt =null;
        ResultSet rs = null;
        try {
            // 加载驱动
            Class.forName("com.mysql.jdbc.Driver");
            // 创建数据库连接对象
            connection = DriverManager
                    .getConnection("jdbc:mysql://127.0.0.1:3306/itheima", "root", "root");
            // 定义sql语句
            String sql = "select * from book";
            // 创建statement语句对象
            psmt = connection.prepareStatement(sql);
            // 设置参数

            // 执行
            rs = psmt.executeQuery();
            // 处理结果集
            while(rs.next()){
                // 创建图书对象
                Book book = new Book();

                // 图书id
                book.setId(rs.getInt("id"));
                // 图书名称
                book.setBookname(rs.getString("bookname"));
                // 图书价格
                book.setPrice(rs.getFloat("price"));
                // 图书图片
                book.setPic(rs.getString("pic"));
                // 图书描述
                book.setBookDesc(rs.getString("bookdesc"));

                bookList.add(book);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            // 释放资源
            try {
                if(rs !=null) rs.close();
                if(psmt !=null) psmt.close();
                if(connection !=null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return bookList;

    }
}
