package com.yfj.bookshop.admin.book.web.servlet;

import cn.itcast.commons.CommonUtils;
import cn.itcast.servlet.BaseServlet;
import com.yfj.bookshop.book.domain.Book;
import com.yfj.bookshop.book.service.BookSevise;
import com.yfj.bookshop.category.domain.Category;
import com.yfj.bookshop.category.service.CategoryService;
import com.yfj.bookshop.pager.PageBean;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

@WebServlet(name = "AdminBookServlet")
public class AdminBookServlet extends BaseServlet {
    private BookSevise bookSevise = new BookSevise();
    private CategoryService categoryService = new CategoryService();

    private String tojson(Category category){
        StringBuilder sb = new StringBuilder("{");
        sb.append("\"cid\"").append(":").append("\"").append(category.getCid()).append("\"");
        sb.append(",");
        sb.append("\"cname\"").append(":").append("\"").append(category.getCname()).append("\"");
        sb.append("}");
        return sb.toString();
    }
    private String tojson(List<Category> categoryList){
        StringBuilder sb = new StringBuilder("[");
        for (int i= 0;i<categoryList.size();i++){
            sb.append(tojson(categoryList.get(i)));
            if (i<categoryList.size()-1){
                sb.append(",");
            }
        }
        sb.append("]");
        return sb.toString();
    }

    public String delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("进入修改图书操作");
        String bid = req.getParameter("bid");
        Book book = bookSevise.load(bid);
        String savePate = this.getServletContext().getRealPath("/");//获取真是路径
        new File(savePate,book.getImage_b()).delete();
        new File(savePate,book.getImage_w()).delete();
        bookSevise.delete(bid);
        req.setAttribute("msg","删除图书成功！");

        return "f:/adminjsps/msg.jsp" ;
    }

    //修改图书
    public String edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        System.out.println("进入修改图书操作");
        Map map = req.getParameterMap();
        Book book = CommonUtils.toBean(map,Book.class);
        Category category = CommonUtils.toBean(map,Category.class);
        book.setCategory(category);
        bookSevise.edit(book);
        req.setAttribute("msg","修改图书成功");
        return "f:/adminjsps/msg.jsp";

    }


    //按bid查询图书
    public String load(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*

         */
        String bid = req.getParameter("bid");
        Book book = bookSevise.load(bid);
        req.setAttribute("book",book);

        //加载一级分类，保存
        req.setAttribute("parents",categoryService.findParents());

        //二级分类
        String pid = book.getCategory().getParent().getCid();

        req.setAttribute("children",categoryService.findChild(pid));
        return "f:/adminjsps/admin/book/desc.jsp";
    }

    //添加图书第一步
    public String ajaxFindChildren(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入书操作");
        String pid = request.getParameter("pid");
        List<Category> children = categoryService.findChild(pid);
        String json = tojson(children);
        System.out.println(json);
        response.getWriter().print(json);
        return null;
    }


    //添加图书第一步
    public String addPre(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("进入添加图书操作");
        List<Category> parents = categoryService.findParents();
        request.setAttribute("parents",parents);
        return "f:/adminjsps/admin/book/add.jsp";
    }


    public String findCategoryAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Category> parents = categoryService.findAll();
        request.setAttribute("parents",parents) ;
        return "f:/adminjsps/admin/book/left.jsp";
    }
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
        return "f:/adminjsps/admin/book/list.jsp";


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
        return "f:/adminjsps/admin/book/list.jsp";


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
        return "f:/adminjsps/admin/book/list.jsp";


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
        return "f:/adminjsps/admin/book/list.jsp";


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
        return "f:/adminjsps/admin/book/list.jsp";


    }

}
