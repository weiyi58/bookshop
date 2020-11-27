package com.yfj.bookshop.book.web.servlet;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import com.yfj.bookshop.book.domain.Book;
import com.yfj.bookshop.book.service.BookSevise;
import com.yfj.bookshop.pager.PageBean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class BookServlet extends BaseServlet {
    private BookSevise bookSevise = new BookSevise();


    private int getPc(HttpServletRequest request){
        int pc =1;
        String param = request.getParameter("pc");
        if (param!=null && !param.trim().isEmpty()){
            try {
                pc = Integer.parseInt(param);
            }catch (RuntimeException e){

            }
        }
        System.out.println("pc---"+pc);
        return pc;
    }
    private String getUrl(HttpServletRequest request){
        String url = request.getRequestURI()+"?"+request.getQueryString();
        System.out.println("--url---"+url);
        try {
            url = new String(url.getBytes("iso8859-1"),"utf-8");
            System.out.println(url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //如果url中存在pc参数，截取掉
        int index = url.lastIndexOf("&pc=");
        if (index != -1){
            url = url.substring(0,index);
        }
        return url;

    }

    //按bid查询图书
    public String load(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*

         */
        String bid = req.getParameter("bid");
        Book book = bookSevise.load(bid);
        req.setAttribute("book",book);
        return "f:/jsps/book/desc.jsp";
    }
    //多条件组合查询
    public String findByCommbination(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
        1.得到pc:如果界面传递，使用页面的，没传使用pc=1

        2.得到url

        3、获取查询条件，本方法是cid

        4，使用pc和cid
         */
        int pc = getPc(req);

        String url = getUrl(req);

        Book criteria = CommonUtils.toBean(req.getParameterMap(),Book.class);

        PageBean<Book> pb=bookSevise.findByCombination(criteria,pc);

        pb.setUrl(url);

        req.setAttribute("pb",pb);
        System.out.println(pb);
        return "f:/jsps/book/list.jsp";


    }

    //按书名查
    public String findByBname(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
        1.得到pc:如果界面传递，使用页面的，没传使用pc=1

        2.得到url

        3、获取查询条件，本方法是cid

        4，使用pc和cid
         */
        int pc = getPc(req);

        String url = getUrl(req);

        String bname = req.getParameter("bname");

        PageBean<Book> pb=bookSevise.findByBname(bname,pc);

        pb.setUrl(url);

        req.setAttribute("pb",pb);
        System.out.println(pb);
        return "f:/jsps/book/list.jsp";


    }

    //按出版社查
    public String findByPress(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
        1.得到pc:如果界面传递，使用页面的，没传使用pc=1

        2.得到url

        3、获取查询条件，本方法是cid

        4，使用pc和cid
         */
        int pc = getPc(req);

        String url = getUrl(req);

        String press = req.getParameter("press");

        PageBean<Book> pb=bookSevise.findByPress(press,pc);

        pb.setUrl(url);

        req.setAttribute("pb",pb);
        System.out.println(pb);
        return "f:/jsps/book/list.jsp";


    }

    //按作者查
    public String findByAuthor(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
        1.得到pc:如果界面传递，使用页面的，没传使用pc=1

        2.得到url

        3、获取查询条件，本方法是cid

        4，使用pc和cid
         */
        int pc = getPc(req);

        String url = getUrl(req);

        String author = req.getParameter("author");

        PageBean<Book> pb=bookSevise.findByAuthor(author,pc);

        pb.setUrl(url);

        req.setAttribute("pb",pb);
        System.out.println(pb);
        return "f:/jsps/book/list.jsp";


    }
    //按分类查
    public String findByCategory(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*
        1.得到pc:如果界面传递，使用页面的，没传使用pc=1

        2.得到url

        3、获取查询条件，本方法是cid

        4，使用pc和cid
         */
        int pc = getPc(req);

        String url = getUrl(req);

        String cid = req.getParameter("cid");

        PageBean<Book> pb=bookSevise.findByCategory(cid,pc);

        pb.setUrl(url);

        req.setAttribute("pb",pb);
        return "f:/jsps/book/list.jsp";


    }

}
