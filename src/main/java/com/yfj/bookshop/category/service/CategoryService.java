package com.yfj.bookshop.category.service;

import com.yfj.bookshop.category.dao.CategoryDao;
import com.yfj.bookshop.category.domain.Category;

import java.sql.SQLException;
import java.util.List;

//分类模块业务层
public class CategoryService {
    private CategoryDao categoryDao = new CategoryDao();


    public List<Category> findChild(String pid){
        try {
            return categoryDao.findByParent(pid);
        } catch (SQLException throwables) {
           throw new RuntimeException(throwables);
        }
    }


    public int findChildrenCountByParent(String pid){
        try {
            return categoryDao.findChildrenCountByParent(pid);
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);

        }
    }

    public void delete(String cid){
        try {
            categoryDao.delete(cid);
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    public void edit(Category category){
        try {
            categoryDao.edit(category);
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    public Category load(String cid){
        try {
            return categoryDao.load(cid);
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }


    //添加分类
    public void add(Category category){
        try {
            categoryDao.add(category);
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }

    }
    //查询所有父分类
    public List<Category> findParents(){
        try {
            return categoryDao.findParents();
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    //查询所有分类
    public List<Category> findAll(){
        try {
            return categoryDao.findAll();
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }
}
