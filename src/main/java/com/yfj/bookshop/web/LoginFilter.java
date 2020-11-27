package com.yfj.bookshop.web;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@WebFilter(filterName = "Filter")
public class LoginFilter implements Filter {
    public void destroy() {
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws ServletException, IOException {
        System.out.println("进入过滤器");

        //判断session中的user是否为空
        HttpServletRequest request = (HttpServletRequest) req;
        Object user = request.getSession().getAttribute("sessionUser");
        System.out.println(user);
        if (user == null){
            System.out.println("您还没有登录");
            req.setAttribute("code","error");
            req.setAttribute("msg","您还没有登录，请登录");
            req.getRequestDispatcher("/jsps/msg.jsp").forward(req,resp);
        }else{
            chain.doFilter(request,resp);
        }
    }

    public void init(FilterConfig config) throws ServletException {
        System.out.println(123);
    }

}
