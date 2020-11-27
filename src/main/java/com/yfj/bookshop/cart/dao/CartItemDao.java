package com.yfj.bookshop.cart.dao;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import com.yfj.bookshop.book.domain.Book;
import com.yfj.bookshop.cart.domain.CartItem;
import com.yfj.bookshop.user.domain.User;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartItemDao {
    private QueryRunner qr = new TxQueryRunner();


    //加载多个cartItem
    public List<CartItem> loadCartItems(String cartItemIds) throws SQLException {
        /*
        把cartItemIds生成数组，
        生成where字句
         */
        Object[] cartItemIdArray = cartItemIds.split(",");
        String wheresql = toWhereSql(cartItemIdArray.length);
        //3.生成where字句

        String sql = "select * from t_cartitem c,t_book b where c.bid = b.bid and "+wheresql;
        return toCartItemList(qr.query(sql, new MapListHandler(), cartItemIdArray));
    }
    //查询数量
    public CartItem findByCartItemId(String cartItemId) throws SQLException {
        String sql = "select * from t_cartItem c,t_book b where c.bid=b.bid and c.cartItemId=?";
        Map<String,Object> map = qr.query(sql,new MapHandler(),cartItemId);
        return toCartItem(map);
    }
    private String toWhereSql(int len){
        StringBuilder sb = new StringBuilder("cartItemId in(");
        for (int i=0;i<len;i++){
            sb.append("?");
            if (i<len-1){
                sb.append(",");
            }
        }
        sb.append(")");
        return sb.toString();
    }
    public void batchDelete(String cartItemIds) throws SQLException {


        //需要把cartItemIds转换成数组
        /*
        1.把cartItemIds转换成一个where字句
        2.与delete from 连接在一起，然后执行
         */
        Object[] cartItemIdArray = cartItemIds.split(",");
        String wheresql = toWhereSql(cartItemIdArray.length);
        String sql = "delete from t_cartitem where " + wheresql;
        qr.update(sql,cartItemIdArray);//必须得是Object类型数组，让可变参数识别到这是可变参数



    }

    //查询某个用户的模本图书的购物车条目是否存在
    public CartItem findByUidAndBid(String uid,String bid) throws SQLException {
        String sql = "select * from t_cartitem where uid = ? and bid=?";
        Map<String,Object> map = qr.query(sql,new MapHandler(),uid,bid);
        CartItem cartItem = toCartItem(map);
        return cartItem;
    }

    //修改购物车数量
    public void updateQuantity(String cartItemId,int quantity) throws SQLException {
         String sql = "update t_cartitem set quantity =?  where cartitemid=?";
         qr.update(sql,quantity,cartItemId);
    }

    //添加条目
    public void addCartItem(CartItem cartItem) throws SQLException {
        String sql = "insert into t_cartitem(cartItemId,quantity,bid,uid)"+"values(?,?,?,?)";
        Object[] params = {cartItem.getCartItemId(),cartItem.getQuantity(),
                cartItem.getBook().getBid(),cartItem.getUser().getUid()};
        qr.update(sql,params);
    }
    //把map映射成一个购物车条目
    public CartItem toCartItem(Map<String,Object> map){
        if (map == null || map.size() == 0){
            return null;
        }
        CartItem cartItem = CommonUtils.toBean(map,CartItem.class);
        Book book = CommonUtils.toBean(map,Book.class);
        User user = CommonUtils.toBean(map,User.class);

        cartItem.setBook(book);
        cartItem.setUser(user);
        return cartItem;
    }
    //把多个Map<String,Object>映射成多个CartItem
    public List<CartItem> toCartItemList(List<Map<String,Object>> mapList){
        List<CartItem> cartItemLists = new ArrayList<CartItem>();
        for (Map<String,Object> map:mapList){
            CartItem cartItem = toCartItem(map);
            cartItemLists.add(cartItem);


        }
        return cartItemLists;

    }


    //通过购物车查询购物车条目
    public List<CartItem> findByUser(String uid) throws SQLException {
        String sql = "select * from t_cartitem c,t_book b where c.bid = b.bid and uid = ? order by c.orderBy";
        List<Map<String,Object>> mapList = qr.query(sql,new MapListHandler(),uid);
        return toCartItemList(mapList);

    }

}
