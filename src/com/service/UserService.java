package com.service;

import com.bean.ItemBean;
import com.bean.UserBean;
import com.dao.ItemDao;
import com.dao.UserDao;
import com.util.GlobalUtil;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class UserService {
    private UserDao userDao = new UserDao();

    public List<UserBean> getUserList(int beginIndex, int pageSize, Map<String, String> paramMap) {
        return userDao.getUserList(beginIndex, pageSize, paramMap);
    }


    public int getUserCount(Map<String, String> paramMap) {
        return userDao.getUserCount(paramMap);
    }

    public UserBean getUser(int userid) throws Exception {
        UserBean userBean = userDao.getUser(userid);
        if (userBean == null) {
            throw new Exception("userid为" + userid + "的用户不存在");
        }
        return userBean;
    }

    public UserBean getUser(String username, String pwd) throws Exception {
        if (GlobalUtil.isEmpty(username) || GlobalUtil.isEmpty(pwd)) {
            throw new Exception("参数不能为null");
        } else {
            UserBean userBean = userDao.getUser(username, pwd);
            if (userBean == null) {
                throw new Exception("用户名或密码错误");
            }
            return userBean;
        }
    }

    public UserBean getAdmin(String username, String pwd) throws Exception {
        UserBean userBean = this.getUser(username, pwd);
        int authLevel = userBean.getAuthLevel();
        if (authLevel == 1 || authLevel == 5) {
            return userBean;
        } else {
            throw new Exception("用户名或密码错误");
        }
    }

    public void updateLock(int userid) throws Exception {
        UserBean userBean = this.getUser(userid);
        String oldLockTag = userBean.getLockTag();
        String newLockTag = null;
        if ("0".equals(oldLockTag)) {
            newLockTag = "1";
        } else if ("1".equals(oldLockTag)) {
            newLockTag = "0";
        }
        userDao.updateLock(userid, newLockTag);
    }

    public int getMaxUserid() {
        return userDao.getMaxUserid();
    }

    public void saveUser(UserBean userBean) throws Exception {
        int userid = userBean.getUserid();
        boolean isExistUserid = userDao.isExistUserid(userid);
        if (isExistUserid) {
            throw new Exception("添加的用户编号已存在，请更改用户编号");
        }
        userBean.setAuthLevel(5);
        userDao.saveUser(userBean);
    }

    public void deleteUser(int userid) throws Exception {
        boolean isExist = userDao.isExistUserid(userid);
        if (isExist) {
            userDao.deleteUser(userid);
        } else {
            throw new Exception("您要删除的用户不存在");
        }
    }

    public void updateUser(int userid, UserBean userBean) throws SQLException {
        userDao.updateUser(userid, userBean);
    }

    public void updatePwd(int userid, String oldPwd, String newPwd) throws Exception {
        UserBean userBean = userDao.getUser(userid);
        this.updatePwd(userBean, oldPwd, newPwd);
    }

    public void updatePwd(UserBean userBean, String oldPwd, String newPwd) throws Exception {
        if (userBean != null) {
            String pwd = userBean.getPwd();
            if (pwd.equals(oldPwd)) {
                userDao.updatePwd(userBean.getUserid(), newPwd);
            } else {
                throw new Exception("旧密码不正确");
            }
        }
    }

    public boolean isExistUsername(String username) {
        return userDao.isExistUsername(username);
    }

    public void updateLoginParams(UserBean userBean) throws SQLException {
        if (userBean != null) {
            int userid = userBean.getUserid();
            int loginNum = userBean.getLoginNum();
            userDao.updateLoginParams(userid, new Date(), loginNum + 1);
        }

    }
}
