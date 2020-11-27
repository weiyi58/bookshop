package com.yfj.bookshop.admin.admin.web.servlet;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import com.yfj.bookshop.admin.admin.domain.Admin;
import com.yfj.bookshop.admin.admin.service.AdminService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AdminServlet extends BaseServlet {
    private AdminService adminService = new AdminService();

    public String login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Admin formadmin = CommonUtils.toBean(request.getParameterMap(),Admin.class);
        Admin admin = adminService.find(formadmin);
        if (admin == null){
            request.setAttribute("msg","用户名或密码错误");
            return "/adminjsps/login.jsp";
        }
        request.getSession().setAttribute("admin",admin);
        return "r:/adminjsps/admin/index.jsp";
    }
}
