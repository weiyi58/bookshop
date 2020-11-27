package com.yfj.bookshop.book.service;

import com.yfj.bookshop.book.dao.BookDao;
import com.yfj.bookshop.book.domain.Book;
import com.yfj.bookshop.pager.PageBean;

import java.sql.SQLException;

public class BookSevise {
    private BookDao bookDao = new BookDao();

    public void delete(String bid){
        try {
            bookDao.delete(bid);
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    public void edit(Book book){
        try {
            bookDao.edit(book);
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    /**
     * 添加图书
     * @param book
     */
    public void add(Book book) {
        try {
            bookDao.add(book);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int findBookCountByCategory(String cid){
        try {
            return bookDao.findBookCountByCategory(cid);
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    public Book load(String bid){
        try {
            return bookDao.findByBid(bid);
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }
    //按书名查
    public PageBean<Book> findByBname(String bname,int pc){
        System.out.println(pc);
        try {
            return bookDao.findByBname(bname,pc);
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }
    //按分类查
    public PageBean<Book> findByCategory(String cid,int pc){
        System.out.println(pc);
            try {
                return bookDao.findByCategory(cid,pc);
            } catch (SQLException throwables) {
                throw new RuntimeException(throwables);
            }
    }
    //按作者查
    public PageBean<Book> findByAuthor(String author,int pc){

        try {
            return bookDao.findByAuthor(author,pc);
        } catch (SQLException throwables) {
            throw new RuntimeException();
        }
    }

    //按出版社查
    public PageBean<Book> findByPress(String press,int pc){

        try {
            return bookDao.findByPress(press,pc);
        } catch (SQLException throwables) {
            throw new RuntimeException();
        }
    }
    //多条件组合查询
    public PageBean<Book> findByCombination(Book crietria,int pc){

        try {
            return bookDao.findByCommbination(crietria,pc);

        } catch (SQLException throwables) {
            throw new RuntimeException();
        }
    }
}
