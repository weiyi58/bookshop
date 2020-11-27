package com.yfj.bookshop.cart.service;

import cn.itcast.commons.CommonUtils;
import com.yfj.bookshop.cart.dao.CartItemDao;
import com.yfj.bookshop.cart.domain.CartItem;

import java.sql.SQLException;
import java.util.List;

public class CartItemService {
    private CartItemDao cartItemDao = new CartItemDao();
    //
    public List<CartItem> loadCartItems(String cartItemIds){
        try {
            return cartItemDao.loadCartItems(cartItemIds);
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    //修改购物车
    public CartItem updateQuantity(String cartItemId,int quantity){
        try {
            cartItemDao.updateQuantity(cartItemId,quantity);
            return cartItemDao.findByCartItemId(cartItemId);
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }

    }

    //删除购物车
    public void batchDelete(String cartItemIds){
        try {
            cartItemDao.batchDelete(cartItemIds);
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }

    //添加条目
    public void add(CartItem cartItem){
        System.out.println(cartItem);
        try {
            CartItem _cartItem = cartItemDao.findByUidAndBid(cartItem.getUser().getUid(),
            cartItem.getBook().getBid());
            if (_cartItem == null){

                //添加条目
                cartItem.setCartItemId(CommonUtils.uuid());
                cartItemDao.addCartItem(cartItem);
            }else{
                //修改数量

                int quantity = cartItem.getQuantity()+_cartItem.getQuantity();
                cartItemDao.updateQuantity(_cartItem.getCartItemId(),quantity);

            }
        } catch (SQLException throwables) {
            throw new RuntimeException();
        }

    }
    //我的购物车功能
    public List<CartItem> myCart(String uid){
        try {
            return cartItemDao.findByUser(uid);
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }
    }
}
