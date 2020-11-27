package com.yfj.bookshop.pager;

import java.util.List;
//分页bean
public class PageBean<T> {
    private int pc;//当前页码
    private int tr;//总记录数
    private int ps; //每页记录数
    private String url;  //请求路径和参数
    private List<T> beanList;

    //private String tb;
    //计算最大页数

    public int getTp(){
        int tp = tr/ps;
        return tr%ps == 0 ? tp:tp+1;
    }
    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    public int getTr() {
        return tr;
    }

    public void setTr(int tr) {
        this.tr = tr;
    }

    public int getPs() {
        return ps;
    }

    public void setPs(int ps) {
        this.ps = ps;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<T> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<T> beanList) {
        this.beanList = beanList;
    }

    @Override
    public String toString() {
        return "PageBean{" +
                "pc=" + pc +
                ", tr=" + tr +
                ", ps=" + ps +
                ", url='" + url + '\'' +
                ", beanList=" + beanList +
                '}';
    }
}
