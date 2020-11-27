package com.yfj.bookshop.cart.web.servlet;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import com.yfj.bookshop.book.domain.Book;
import com.yfj.bookshop.cart.domain.CartItem;
import com.yfj.bookshop.cart.service.CartItemService;
import com.yfj.bookshop.user.domain.User;

import javax.servlet.ServletException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CartItemServlet extends BaseServlet {
    private CartItemService cartItemService = new CartItemService();
    //加载多个CartItem
    public String loadCartItems(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取参数
        System.out.println("进入加载购物车操作");
        String cartItemIds = request.getParameter("cartItemIds");

        Double total = Double.parseDouble(request.getParameter("total"));
        List<CartItem> cartItemList = cartItemService.loadCartItems(cartItemIds);
        request.setAttribute("cartItemList",cartItemList);
        request.setAttribute("total",total);
        System.out.println("total-----"+total);
        return "f:/jsps/cart/showitem.jsp";
    }

    public String updateQuantity(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取参数
        System.out.println("进入购物车数量修改操作");
        String cartItemId = request.getParameter("cartItemId");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        CartItem cartItem = cartItemService.updateQuantity(cartItemId,quantity);

        StringBuilder sb = new StringBuilder("{");
        sb.append("\"quantity\"").append(":").append(cartItem.getQuantity());
        sb.append(",");
        sb.append("\"subtotal\"").append(":").append(cartItem.getSubtotal());
        sb.append("}");

        response.getWriter().print(sb);
        return null;
    }

    public String batchDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取参数
        System.out.println("进入购物车删除操作");
        String cartItems = request.getParameter("cartItemIds");
        cartItemService.batchDelete(cartItems);
        return myCart(request,response);

    }

    //添加购物车
    public String add(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //封装表单数据到CartItem
        Map map = request.getParameterMap();
        CartItem cartItem = CommonUtils.toBean(map,CartItem.class);
        Book book = CommonUtils.toBean(map,Book.class  );
        User user = (User) request.getSession().getAttribute("sessionUser");
        cartItem.setBook(book);
        cartItem.setUser(user);

        cartItemService.add(cartItem);
        System.out.println(cartItem);
        return myCart(request,response);
    }

    //展示购物车
    public String myCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        //得到uid
        User user = (User) request.getSession().getAttribute("sessionUser");

        String uid = user.getUid();

        //通过service得到当前用户的所有购物车条目
        List<CartItem> cartItemLists = cartItemService.myCart(uid);

        request.setAttribute("cartItemList",cartItemLists);
        return "f:/jsps/cart/list.jsp";



    }
}
