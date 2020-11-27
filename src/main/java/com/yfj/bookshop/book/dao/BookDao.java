package com.yfj.bookshop.book.dao;

import cn.itcast.commons.CommonUtils;
import cn.itcast.jdbc.TxQueryRunner;
import com.yfj.bookshop.book.domain.Book;
import com.yfj.bookshop.category.domain.Category;
import com.yfj.bookshop.pager.Expression;
import com.yfj.bookshop.pager.PageBean;
import com.yfj.bookshop.pager.PageConstancs;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BookDao {
    private QueryRunner qr = new TxQueryRunner();

    public void delete (String bid) throws SQLException {
        String sql = "delete from t_book where bid = ?";
        qr.update(sql,bid);

    }

    public void edit(Book book) throws SQLException {
        String sql = "update  t_book set bname=?,author=?,price=?,currPrice=?," +
                "discount=?,press=?,publishtime=?,edition=?,pageNum=?,wordNum=?," +
                "printtime=?,booksize=?,paper=?,cid=? where bid = ?";
        Object[] params = {book.getBname(),book.getAuthor(),
                book.getPrice(),book.getCurrPrice(),book.getDiscount(),
                book.getPress(),book.getPublishtime(),book.getEdition(),
                book.getPageNum(),book.getWordNum(),book.getPrinttime(),
                book.getBooksize(),book.getPaper(), book.getCategory().getCid(),
                book.getBid()};
        qr.update(sql, params);
    }

    //查找二级分类下书籍数量
    public int findBookCountByCategory(String cid) throws SQLException {
        String sql = "select count(*) from t_book where cid = ?";
        Number cnt = (Number) qr.query(sql,new ScalarHandler(),cid);
        return cnt == null ? 0:cnt.intValue();
    }

    //id查book
    public Book findByBid(String bid) throws SQLException {
        //只使用book映射时cid没有传递进去
        String sql = "select * from t_book b,t_category c where b.cid=c.cid and bid=?";
        Map<String,Object> map = qr.query(sql,new MapHandler(),bid);
        Book book = CommonUtils.toBean(map,Book.class);
        Category category = CommonUtils.toBean(map,Category.class);
        if (map.get("pid")!=null){
            Category parent = new Category();
            parent.setCid((String) map.get("pid"));
            category.setParent(parent);
        }

        book.setCategory(category);
        return book;
    }
    //分类查询
    public PageBean<Book> findByCategory(String cid,int pc) throws SQLException {
        System.out.println(cid);
        List<Expression> expList = new ArrayList<Expression>();
        expList.add(new Expression("cid","=",cid));
        return findByCriteria(expList,pc);
    }
    //按书名模糊查询
    public PageBean<Book> findByBname(String bname,int pc) throws SQLException {
        List<Expression> expList = new ArrayList<Expression>();
        expList.add(new Expression("bname","like","%"+bname+"%"));
        return findByCriteria(expList,pc);
    }
    //按作者模糊查询
    public PageBean<Book> findByAuthor(String author,int pc) throws SQLException {
        List<Expression> expList = new ArrayList<Expression>();
        expList.add(new Expression("author","like","%"+author+"%"));
        return findByCriteria(expList,pc);
    }
    //按出版社模糊查询
    public PageBean<Book> findByPress(String press,int pc) throws SQLException {
        List<Expression> expList = new ArrayList<Expression>();
        expList.add(new Expression("press","like","%"+press+"%"));
        return findByCriteria(expList,pc);
    }
    //多条件组合查询
    public PageBean<Book> findByCommbination(Book criteria,int pc) throws SQLException {
        List<Expression> expList = new ArrayList<Expression>();
        expList.add(new Expression("bname","like","%"+criteria.getBname()+"%"));
        expList.add(new Expression("author","like","%"+criteria.getAuthor()+"%"));
        expList.add(new Expression("press","like","%"+criteria.getPress() +"%"));

        return findByCriteria(expList,pc);
    }

    //通用查询方法
    private PageBean<Book> findByCriteria(List<Expression> expressionList,int pc) throws SQLException {
        /*
        得到ps
        得到pr
        得到beanList
        创建Pagebean,返回

         */
        int ps = PageConstancs.BOOK_PAGE_SIZE;

        //通过exprList来生成where子句
        StringBuilder whereSql = new StringBuilder(" where 1=1");
        //Sql中有问号，它对应问号的值
        List<Object> params = new ArrayList<Object>();
        for (Expression expression:expressionList){
            /*添加一个添加上，
            以and开头，
            条件的名称，
            条件的运算符 ： = ，！=，is null;
            条件不是空，追加？，再添加？对应的值


             */

            whereSql.append(" and ").append(expression.getName()).append(" ").append(expression.getOperator())
                    .append(" ");
            if (!expression.getOperator().equals("is null")){

                whereSql.append("?");
                params.add(expression.getValue());

            }

        }
        //总记录数
        String sql = "select count(*) from t_book" + whereSql;
        Number number = (Number) qr.query(sql, new ScalarHandler(), params.toArray());
        int tr = number.intValue();

        //得到benList，当前页记录
        sql = "select * from t_book" + whereSql + " order by orderBy limit ?,?";
        params.add((pc-1)*ps);   //第一个问号
        params.add(ps);

        List<Book> beanList = qr.query(sql, new BeanListHandler<Book>(Book.class), params.toArray());

        //创建pageBean,设置参数
        PageBean<Book> pb = new PageBean<Book>();
        pb.setBeanList(beanList);
        pb.setPc(pc);
        pb.setPs(ps);
        pb.setTr(tr);
        return pb;

    }
    /**
     * 添加图书
     * @param book
     * @throws SQLException
     */
    public void add(Book book) throws SQLException {
        String sql = "insert into t_book(bid,bname,author,price,currPrice," +
                "discount,press,publishtime,edition,pageNum,wordNum,printtime," +
                "booksize,paper,cid,image_w,image_b)" +
                " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Object[] params = {book.getBid(),book.getBname(),book.getAuthor(),
                book.getPrice(),book.getCurrPrice(),book.getDiscount(),
                book.getPress(),book.getPublishtime(),book.getEdition(),
                book.getPageNum(),book.getWordNum(),book.getPrinttime(),
                book.getBooksize(),book.getPaper(), book.getCategory().getCid(),
                book.getImage_w(),book.getImage_b()};
        qr.update(sql, params);
    }
}
