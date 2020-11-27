package com.yfj.bookshop.admin.admin.service;

import com.yfj.bookshop.admin.admin.dao.AdminDao;
import com.yfj.bookshop.admin.admin.domain.Admin;

import java.sql.SQLException;

public class AdminService {
    private AdminDao adminDao = new AdminDao();
    public Admin find(Admin admin){
        try {
            return adminDao.find(admin.getAdminname(),admin.getAdminpwd());
        } catch (SQLException throwables) {
            throw new RuntimeException(throwables);
        }

    }
}
