package com.yfj.bookshop.user.web.servlet;

import cn.itcast.commons.CommonUtils;
import com.yfj.bookshop.user.domain.User;
import com.yfj.bookshop.user.service.UserService;
import cn.itcast.servlet.BaseServlet;
import com.yfj.bookshop.user.service.exception.UserException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户模块WEB层
 * @author qdmmy6
 *
 */
public class UserServlet extends BaseServlet {
	private UserService userService = new UserService();

	public String quit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.println("进入退出操作");
		req.getSession().invalidate();
		return "r:/jsps/user/login.jsp";
	}
	public String updatePassword(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.printf("进入更改密码操作");
		//封装表单数据到User中
		User formUser = CommonUtils.toBean(req.getParameterMap(),User.class);



		//从session中获取uid
		User user = (User)req.getSession().getAttribute("sessionUser");
		//使用uid和表单中的密码调用service方法
		//保存到ruquest

		//转发
		try {
			userService.updatePassword(user.getUid(),formUser.getNewLoginpass(),formUser.getLoginpass());
			req.setAttribute("msg","修改密码成功");
			req.setAttribute("code","success");
			return "f:/jsps/msg.jsp";
		} catch (UserException e) {
			req.setAttribute("msg",e.getMessage());
			req.setAttribute("form",formUser);
			return "f:/jsps/user/pwd.jsp";
		}

	}
	public String login(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		System.out.printf("进入登录操作");

		/*
		封装表单数据到User
		校验表单数据
		使用servise查询
		是否存在
			是
				查看状态
					已激活
						保存user到session中

			否
				返回错误信息

		 */
		User formuser = CommonUtils.toBean(req.getParameterMap(),User.class);

		User user = userService.login(formuser);

		if (user == null){
			req.setAttribute("msg","用户名或密码错误");
			System.out.println("用户名或密码错误");
			req.setAttribute("user",formuser);
			return "f:/jsps/user/login.jsp";
		}else{
			if (!user.isStatus()){
				req.setAttribute("msg","用户未激活");
				System.out.println("用户未激活");
				req.setAttribute("user",formuser);
				return "f:/jsps/user/login.jsp";
			}else{
				//保存用户到Session中
				req.getSession().setAttribute("sessionUser",user);
				System.out.println("登录成功");
				//保存用户名到cookie中
				String loginname = user.getLoginname();
				loginname = URLEncoder.encode(loginname,"utf-8");
				Cookie cookie = new Cookie("loginname",loginname);
				cookie.setMaxAge(60*24*7);
				resp.addCookie(cookie);
				return "f:/index.jsp";
			}

		}

	}



	public String regist(HttpServletRequest req,HttpServletResponse resp){
		System.out.println("进入注册操作");
		//封装表单数据到User对象

		User formUser = CommonUtils.toBean(req.getParameterMap(),User.class);

		//校验，如果失败，保存错误信息，返回到regist.jsp显示

		Map<String,String> errrors = validateRegist(formUser,req.getSession());
		System.out.println("errors"+errrors.size());
		if (errrors.size()>0){
			req.setAttribute("errors",errrors);
			req.setAttribute("form",formUser);
			return "f:/jsps/user/regist.jsp";
		}

		//调用servise完成业务

		userService.regist(formUser);
		//保存成功信息，转发到jsp显示
		req.setAttribute("code","success");
		req.setAttribute("msg","注册完成，请马上到邮箱激活");

		return "f:/jsps/msg.jsp";
	}

	/*
	 * 注册校验
	 * 对表单的字段进行逐个校验，如果有错误，使用当前字段名称为key，错误信息为value，保存到map中
	 * 返回map
	 */
	private Map<String,String> validateRegist(User formUser, HttpSession session) {
		Map<String,String> errors = new HashMap<String,String>();
		/*
		 * 1. 校验登录名
		 */
		String loginname = formUser.getLoginname();
		if(loginname == null || loginname.trim().isEmpty()) {
			errors.put("loginname", "用户名不能为空！");
		} else if(loginname.length() < 3 || loginname.length() > 20) {
			errors.put("loginname", "用户名长度必须在3~20之间！");
		} else if(!userService.ajaxValidateLoginname(loginname)) {
			errors.put("loginname", "用户名已被注册！");
		}

		/*
		 * 2. 校验登录密码
		 */
		String loginpass = formUser.getLoginpass();
		if(loginpass == null || loginpass.trim().isEmpty()) {
			errors.put("loginpass", "密码不能为空！");
		} else if(loginpass.length() < 3 || loginpass.length() > 20) {
			errors.put("loginpass", "密码长度必须在3~20之间！");
		}

		/*
		 * 3. 确认密码校验
		 */
		String reloginpass = formUser.getReloginpass();
		if(reloginpass == null || reloginpass.trim().isEmpty()) {
			errors.put("reloginpass", "确认密码不能为空！");
		} else if(!reloginpass.equals(loginpass)) {
			errors.put("reloginpass", "两次输入不一致！");
		}

		/*
		 * 4. 校验email
		 */
		String email = formUser.getEmail();
		if(email == null || email.trim().isEmpty()) {
			errors.put("email", "Email不能为空！");
		} else if(!email.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")) {
			errors.put("email", "Email格式错误！");
		} else if(!userService.ajaxValidateEmail(email)) {
			errors.put("email", "Email已被注册！");
		}

		/*
		 * 5. 验证码校验
		 */
		String verifyCode = formUser.getVerifyCode();
		String vcode = (String) session.getAttribute("vCode");
		if(verifyCode == null || verifyCode.trim().isEmpty()) {
			errors.put("verifyCode", "验证码不能为空！");
		} else if(!verifyCode.equalsIgnoreCase(vcode)) {
			errors.put("verifyCode", "验证码错误！");
		}

		return errors;
	}

	//激活功能
	public void activation(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.printf("进入激活操作");
		String activat = "";
	}
	/*
	Email是否已注册
	 */
	public void ajaxValidateEmail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.printf("校验Email");

		String email = req.getParameter("email");

		//通过servlet得到校验结果
		boolean b = userService.ajaxValidateEmail(email);

		//发送给客户端
		resp.getWriter().print(b);
		System.out.println(b);

	}

	//用户名是否已注册
	public void ajaxValidateLoginname(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.printf("校验用户名");
		String loginname = req.getParameter("loginname");

		//通过servlet得到校验结果
		boolean b = userService.ajaxValidateLoginname(loginname);

		//发送给客户端
		resp.getWriter().print(b);
	}
	public String ajaxValidateVerifyCode(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		System.out.printf("进入验证码校验操作");

		//获取输入的验证码
		String verifyCode = req.getParameter("verifyCode");

		//获取真实验证码
		String vcode = (String) req.getSession().getAttribute("vCode");


		boolean b = verifyCode.equalsIgnoreCase(vcode); 
		//对比结果
		//发送给客户端
		resp.getWriter().print(b);
		return "";
	}
}
