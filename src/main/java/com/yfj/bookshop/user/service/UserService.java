package com.yfj.bookshop.user.service;

import cn.itcast.commons.CommonUtils;
import cn.itcast.mail.Mail;
import cn.itcast.mail.MailUtils;
import com.yfj.bookshop.user.dao.UserDao;
import com.yfj.bookshop.user.domain.User;
import com.yfj.bookshop.user.service.exception.UserException;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import javax.mail.MessagingException;
import javax.mail.Session;
import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Properties;

/**
 * 用户模块业务层
 * @author qdmmy6
 *
 */
public class UserService {
	private UserDao userDao = new UserDao();



	//修改密码
	public void updatePassword(String uid,String newPass,String oldPass) throws UserException {
		//校验老密码
		try {
			boolean b = userDao.findByUidAndPassword(uid,oldPass);
			if (!b){
				throw new UserException("旧密码错误");

			}
			//修改密码

			userDao.updatePassword(uid,newPass);
		} catch (SQLException throwables) {
			throw new RuntimeException(throwables);
		}

	}
	//登录，根据账户密码查用户
	//返回User，传入User都是为了以后添加更多功能便于扩展

	public User login(User user){

		try {

			return userDao.findByLoginnameAndLoginpass(user.getLoginname(), user.getLoginpass());
		} catch (SQLException throwables) {
			throw new RuntimeException(throwables);
		}
	}


	public void regist(User user){
		//数据补齐
		user.setUid(CommonUtils.uuid());
		user.setStatus(false);
		user.setActivationCode(CommonUtils.uuid()+CommonUtils.uuid());

		//向数据库插入数据
		try {
			userDao.add(user);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		//发送邮件
		//读取配置文件
		Properties prop = new Properties();
		try {
			prop.load(this.getClass().getClassLoader().getResourceAsStream("email_template.properties"));
		} catch (IOException e) {
			throw new RuntimeException();
		}
		//登录邮件服务器
		String host = prop.getProperty("host");//主机名
		String name = prop.getProperty("username");//登录名
		String pass = prop.getProperty("password");//登录密码
		Session session = MailUtils.createSession(host,name,pass);

		//创建Mail对象
		String form = prop.getProperty("from");
		String to = user.getEmail();
		String subject = prop.getProperty("sbuject");
		String content = MessageFormat.format(prop.getProperty("content"),user.getActivationCode());
		Mail mail = new Mail(form,to,subject,content);
		//发送邮件

		try {
			MailUtils.send(session,mail);
		} catch (MessagingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


	}

	public boolean ajaxValidateLoginname(String loginname)  {
		try {
			return userDao.ajaxValidateLoginname(loginname);
		} catch (SQLException throwables) {
			throw new RuntimeException(throwables);
		}

	}

	//email校验
	public boolean ajaxValidateEmail(String email)  {

		try {
			return userDao.ajaxValidateEmail(email);
		} catch (SQLException throwables) {
			throw new RuntimeException(throwables);
		}
	}
}
