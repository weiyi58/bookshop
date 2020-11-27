package com.yfj.bookshop.category.dao;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import com.yfj.bookshop.category.domain.Category;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoryDao {
    private QueryRunner qr = new TxQueryRunner();


    private Category toCategory(Map<String,Object> map){
        Category category = CommonUtils.toBean(map,Category.class);
        String pid = (String) map.get("pid");
        if (pid!=null){
            /*
            如果父分类不为空，
            使用父分类对象拦截pid
            再把父分类设置给category
             */
            Category parent = new Category();
            parent.setCid(pid);
            category.setParent(parent);
        }

        return category;
    }
    //把多个Map(List<Map>)映射成多个Category



    //查询指定父分类下子分类的个数
    public int findChildrenCountByParent(String pid) throws SQLException {

    String sql = "select count(*) from t_category where pid = ?";
    Number cnt = (Number) qr.query(sql,new ScalarHandler(),pid);
    return cnt == null ? 0:cnt.intValue();
     }

     //删除分类
    public void  delete(String cid) throws SQLException {
        String sql = "delete from t_category where cid=?";
        qr.update(sql,cid);
    }

    //加载一级分类或者二级分类
    public Category load(String cid) throws SQLException {
        String sql = "select * from  t_category where cid=?";
        return toCategory(qr.query(sql,new MapHandler(),cid));
    }

    //修改分类方法
    public void edit(Category category) throws SQLException {
        String sql = "update t_category set cname=?,pid=?,`desc`=? where cid=?";

        String pid = null;
        if (category.getParent() !=null){
            pid = category.getParent().getCid();
        }
        Object[] params = {category.getCname(),pid,category.getDesc(),category.getCid() };
        qr.update(sql,params);
    }

    //查询所有父分类
    public List<Category> findParents() throws SQLException {
        //查询所有一级分类

        String sql = "select * from t_category where pid is null order by orderBy";
        List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler());
        List<Category> parents = toCategoryList(mapList);

        return parents;
    }
    private List<Category> toCategoryList(List<Map<String,Object>> mapList){
        List<Category> categoryList = new ArrayList<Category>();
        for (Map<String,Object>map:mapList){
            Category c = toCategory(map);
            categoryList.add(c);
        }
        return categoryList;
    }

    //
    public void add(Category category) throws SQLException {

        String sql = "insert into t_category(cid,cname,pid,`desc`) values(?,?,?,?)";
        String pid = null;
        if (category.getParent()!=null){
            pid = category.getParent().getCid();
        }
        Object[] params = {category.getCid(),category.getCname(),pid,category.getDesc()};
        qr.update(sql,params);

    }

    public List<Category> findAll() throws SQLException {
        //查询所有一级分类

        String sql = "select * from t_category where pid is null order by orderBy";
        List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler());
        List<Category> parents = toCategoryList(mapList);
        //循环遍历所有一级分类，为每个一级分类加载二级分类
        for (Category parent:parents){
            List<Category> children = findByParent(parent.getCid());
            //设置给父分类
            parent.setChildren(children);
        }
        return parents;
    }
    public List<Category> findByParent(String pid) throws SQLException {
        String sql = "select * from t_category where pid=? order by orderBy";
        List<Map<String, Object>> mapList = qr.query(sql, new MapListHandler(),pid);

        return toCategoryList(mapList);
    }
}
