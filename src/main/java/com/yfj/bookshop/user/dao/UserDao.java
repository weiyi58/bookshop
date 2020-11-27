package com.yfj.bookshop.user.dao;

import com.yfj.bookshop.user.domain.User;
import org.apache.commons.dbutils.QueryRunner;

import cn.itcast.jdbc.TxQueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;

/**
 * 用户模块持久层
 * @author qdmmy6
 *
 */
public class UserDao {
	private QueryRunner qr = new TxQueryRunner();


	//查找账号密码
	public boolean findByUidAndPassword(String uid,String password) throws SQLException {
		String sql = "select count(*) from t_user where uid=? and loginpass=?";
		Number number = (Number) qr.query(sql,new ScalarHandler(),uid,password);
		return number.intValue() > 0;
	}

	//更新密码
	public void updatePassword(String uid,String password) throws SQLException {
		String sql = "update t_user set loginpass=? where uid=?";
		qr.update(sql,password,uid);
	}
	//根据用户名密码查询用户
	public User findByLoginnameAndLoginpass(String loginname,String loginpass) throws SQLException {

		String sql = "select * from t_user where loginname = ? And loginpass = ?";
		return qr.query(sql,new BeanHandler<User>(User.class),loginname,loginpass);
	}

	public void add(User user) throws SQLException {
		String sql = "insert into t_user values(?,?,?,?,?,?)";

		Object[] params = {user.getUid(),user.getLoginname(),user.getLoginpass(),
		user.getEmail(),user.getStatus(),user.getActivationCode()};
		int count = qr.update(sql,params);
	}


	public boolean ajaxValidateLoginname(String loginname) throws SQLException {
		String sql = "select count(1) from t_user where loginname=?";
		Number number = (Number) qr.query(sql, new ScalarHandler(), loginname);
		System.out.println("number-----"+number);
		return number.intValue() == 0 ;
	}

	public boolean ajaxValidateEmail(String email) throws SQLException {
		String sql = "select count(1) from t_user where email=?";
		Number number = (Number) qr.query(sql, new ScalarHandler(), email);

		return number.intValue() == 0 ;
	}
}
