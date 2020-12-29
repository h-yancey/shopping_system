package com.service;

import com.bean.UserBean;
import com.dao.UserDao;
import com.util.GlobalUtil;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class UserService {
    private UserDao userDao = new UserDao();

    /**
     * @param paramMap 存放查询条件的一系列键值对
     * @return 用户数
     * @description 获取满足查询条件的用户的数量
     */
    public int getUserCount(Map<String, String> paramMap) {
        return userDao.getUserCount(paramMap);
    }

    /**
     * @param beginIndex 开始的索引位
     * @param pageSize   获取的个数
     * @param paramMap   存放查询条件的一系列键值对
     * @return 用户列表
     * @description 获取指定条件的用户
     */
    public List<UserBean> getUserList(int beginIndex, int pageSize, Map<String, String> paramMap) {
        return userDao.getUserList(beginIndex, pageSize, paramMap);
    }

    /**
     * @param userid 用户编号
     * @return 存在返回一个用户对象，不存在返回null并抛出异常
     * @description 通过用户编号获取用户
     */
    public UserBean getUserById(int userid) throws Exception {
        UserBean userBean = userDao.getUserById(userid);
        if (userBean == null) {
            throw new Exception("userid为" + userid + "的用户不存在");
        }
        return userBean;
    }

    /**
     * @param username 用户名
     * @param pwd      密码
     * @return 存在返回一个用户对象，不存在返回null并抛出异常
     * @description 通过用户名和密码获取用户
     */
    private UserBean getUserByUP(String username, String pwd) throws Exception {
        if (GlobalUtil.isEmpty(username) || GlobalUtil.isEmpty(pwd)) {
            throw new Exception("参数不能为null");
        } else {
            UserBean userBean = userDao.getUserByUP(username, pwd);
            if (userBean == null) {
                throw new Exception("用户名或密码错误");
            }
            return userBean;
        }
    }

    /**
     * @param username 用户名
     * @param pwd      密码
     * @return 存在返回一个用户对象，不存在返回null并抛出异常
     * @description 获取后台登录用户
     */
    public UserBean getLoginAdmin(String username, String pwd) throws Exception {
        UserBean userBean = this.getUserByUP(username, pwd);
        int authLevel = userBean.getAuthLevel();
        if (authLevel == 1 || authLevel == 5) {
            //后台只允许普通管理员和超级管理员登录
            return userBean;
        } else {
            throw new Exception("用户名或密码错误");
        }
    }

    /**
     * @param username 用户名
     * @param pwd      密码
     * @return 存在返回一个用户对象，不存在返回null并抛出异常
     * @description 获取前台登录用户
     */
    public UserBean getLoginUser(String username, String pwd) throws Exception {
        UserBean userBean = this.getUserByUP(username, pwd);
        int authLevel = userBean.getAuthLevel();
        if (authLevel == 9) {
            //前台只允许注册用户登录
            return userBean;
        } else {
            throw new Exception("用户名或密码错误");
        }
    }

    /**
     * @return 用户编号
     * @description 获取下一个可用的用户编号
     */
    public int getMaxUserid() {
        return userDao.getMaxUserid();
    }

    /**
     * @param userid 用户编号
     * @description 冻结用户
     */
    public void lockUser(int userid) throws Exception {
        userDao.updateLock(userid, "1");
    }

    /**
     * @param userid 用户编号
     * @description 解冻用户
     */
    public void unlockUser(int userid) throws Exception {
        userDao.updateLock(userid, "0");
    }

    /**
     * @param userBean 用户对象
     * @description 保存用户
     */
    public void saveUser(UserBean userBean) throws Exception {
        int userid = userBean.getUserid();
        boolean isExistUserid = userDao.isExistUserid(userid);
        if (isExistUserid) {
            throw new Exception("添加的用户编号已存在，请更改用户编号");
        }
        userDao.saveUser(userBean);
    }

    /**
     * @param userid 用户编号
     * @description 通过用户编号删除用户
     */
    public void deleteUserById(int userid) throws Exception {
        boolean isExist = userDao.isExistUserid(userid);
        if (isExist) {
            userDao.deleteUserById(userid);
        } else {
            throw new Exception("您要删除的用户不存在");
        }
    }

    /**
     * @param userBean 用户对象
     * @description 修改用户
     */
    public void updateUser(UserBean userBean) throws SQLException {
        userDao.updateUser(userBean);
    }

    /**
     * @param userid 用户编号
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @description 修改密码
     */
    public void updatePwd(int userid, String oldPwd, String newPwd) throws Exception {
        UserBean userBean = userDao.getUserById(userid);
        this.updatePwd(userBean, oldPwd, newPwd);
    }

    /**
     * @param userBean 用户对象
     * @param oldPwd   旧密码
     * @param newPwd   新密码
     * @description 修改密码
     */
    public void updatePwd(UserBean userBean, String oldPwd, String newPwd) throws Exception {
        if (userBean != null) {
            String pwd = userBean.getPwd();
            if (pwd.equals(oldPwd)) {
                //旧密码正确才能修改
                userDao.updatePwd(userBean.getUserid(), newPwd);
            } else {
                throw new Exception("旧密码不正确");
            }
        }
    }

    /**
     * @param userBean 用户对象
     * @description 更新登录参数
     */
    public void updateLoginParams(UserBean userBean) throws SQLException {
        if (userBean != null) {
            int userid = userBean.getUserid();
            Date lastDate = new Date();//最后登录时间
            int loginNum = userBean.getLoginNum();//登陆次数
            userDao.updateLoginParams(userid, lastDate, loginNum + 1);
        }
    }

    /**
     * @param username 用户名
     * @return 存放返回true, 不存在返回false
     * @description 判断用户名是否已存在
     */
    public boolean isExistUsername(String username) {
        return userDao.isExistUsername(username);
    }


}
