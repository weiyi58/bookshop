package com.yfj.bookshop.admin.category.web.servlet;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import com.yfj.bookshop.book.service.BookSevise;
import com.yfj.bookshop.category.domain.Category;
import com.yfj.bookshop.category.service.CategoryService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class AdminCategoryServlet extends BaseServlet {
    private CategoryService categoryService = new CategoryService();
    private BookSevise bookSevise = new BookSevise();


    //删除二级分类
    public  String deleteChild(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("删除二级分类");
        String cid = req.getParameter("cid");
        int cnt = bookSevise.findBookCountByCategory(cid);
        if (cnt == 0){
            categoryService.delete(cid);
            return findAll(req,resp);
        }else{
            req.setAttribute("msg","该分类下还有图书");
            return "f:/adminjsps/msg.jsp";
        }
    }


    //删除一级分类
    public String deleteParent(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String cid = req.getParameter("cid");
        int cnt = categoryService.findChildrenCountByParent(cid);
        if (cnt == 0){
            categoryService.delete(cid);
            return findAll(req,resp);
        }else{
            req.setAttribute("msg","该分类下还有子分类");
            return "f:/adminjsps/msg.jsp";
        }

    }

    //修改二级分类第一步
    public String editChildPre(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String cid = req.getParameter("cid");
        Category child = categoryService.load(cid);
        req.setAttribute("child",child);
        req.setAttribute("parents",categoryService.findParents());
        return "f:/adminjsps/admin/category/edit2.jsp";

    }

    //修改二级分类第二步
    public String editChild(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        System.out.println("开始修改二级分类");
        Category child = CommonUtils.toBean(req.getParameterMap(),Category.class);
        String pid = req.getParameter("pid");
        Category parent = new Category();
        parent.setCid(pid);
        child.setParent(parent);
        categoryService.edit(child);
        return  findAll(req,resp);



    }

    //修改一级分类第一步
    public String editParentPre(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String cid = req.getParameter("cid");
        Category pargent = categoryService.load(cid);
        req.setAttribute("parent",pargent);
        return "f:/adminjsps/admin/category/edit.jsp";
    }

    //修改一级分类第二步
    public String editParent(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Category paent = CommonUtils.toBean(req.getParameterMap(),Category.class);
        categoryService.edit(paent);
        return findAll(req,resp);
    }

    //添加二级分类第一步
    public String addChildPre(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String pid = req.getParameter("pid");   //获取点击的父分类id

        List<Category> parents = categoryService.findParents();
        req.setAttribute("pid",pid);
        req.setAttribute("parents",parents);

        return "f:/adminjsps/admin/category/add2.jsp";
    }

    //添加一级分类
    public String addParent(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Category parent = CommonUtils.toBean(req.getParameterMap(),Category.class);
        parent.setCid(CommonUtils.uuid());
        categoryService.add(parent);
        return findAll(req,resp);
    }

        //添加二级分类
    public String addChild(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Category child = CommonUtils.toBean(req.getParameterMap(),Category.class);
        child.setCid(CommonUtils.uuid());
        String pid = req.getParameter("pid");
        Category parent = new Category();
        parent.setCid(pid);
        child.setParent(parent);

        categoryService.add(child);
        return findAll(req,resp);
    }
    //查询所有分类
    public String findAll(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("parents",categoryService.findAll());
        return "f:/adminjsps/admin/category/list.jsp";
    }
}
