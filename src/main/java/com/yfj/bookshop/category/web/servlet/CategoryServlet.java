package com.yfj.bookshop.category.web.servlet;

import cn.itcast.servlet.BaseServlet;
import com.yfj.bookshop.category.domain.Category;
import com.yfj.bookshop.category.service.CategoryService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class CategoryServlet extends BaseServlet {
    private CategoryService categoryService = new CategoryService();

    public String findAll(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
        1.通过servise得到啊所有的分类
        2.保存到resquest，转发到left.jsp

         */
        System.out.println("进入查找操作");
        List<Category> parents = categoryService.findAll();
        req.setAttribute("parents",parents) ;
        return "f:/jsps/left.jsp";

    }
}
