package com.dao;

import com.bean.UserBean;
import com.util.GlobalUtil;
import com.util.JdbcUtil;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class UserDao {
    /**
     * @param paramMap 存放查询条件的一系列键值对
     * @return where子句
     * @description 拼接sql语句的查询条件参数
     */
    private String getSearchSql(Map<String, String> paramMap) {
        if (paramMap == null) {
            return null;
        }
        StringBuffer searchSql = new StringBuffer();
        String authLevel = paramMap.get("authLevel");//权限级别
        String username = paramMap.get("username");//用户名
        String sex = paramMap.get("sex");//性别
        String lockTag = paramMap.get("lockTag");//冻结状态

        if (!GlobalUtil.isEmpty(authLevel)) {
            searchSql.append(" and authLevel = " + authLevel);
        }
        if (!GlobalUtil.isEmpty(username)) {
            searchSql.append(" and username like '%" + username + "%'");
        }
        if (!GlobalUtil.isEmpty(sex)) {
            searchSql.append(" and sex = '" + sex + "'");
        }
        if (!GlobalUtil.isEmpty(lockTag)) {
            searchSql.append(" and lockTag = '" + lockTag + "'");
        }
        return searchSql.toString();
    }

    /**
     * @param paramMap 存放查询条件的一系列键值对
     * @return 用户数
     * @description 获取数据库中满足查询条件的用户的数量
     */
    public int getUserCount(Map<String, String> paramMap) {
        String searchSql = this.getSearchSql(paramMap);
        String sql = "SELECT IFNULL(COUNT(userid),0) AS user_count FROM t_user";
        if (searchSql != null && searchSql.length() > 0) {
            sql = sql + " WHERE 1=1 " + searchSql;
        }

        Long userCount = null;
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            userCount = (Long) runner.query(conn, sql, new ScalarHandler("user_count"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return userCount.intValue();
    }

    /**
     * @param beginIndex 开始的索引位
     * @param pageSize   获取的个数
     * @param paramMap   存放查询条件的一系列键值对
     * @return 用户列表
     * @description 从数据库中获取指定条件的用户
     */
    public List<UserBean> getUserList(int beginIndex, int pageSize, Map<String, String> paramMap) {
        String searchSql = this.getSearchSql(paramMap);
        String sql = "SELECT * FROM t_user";
        if (searchSql != null && searchSql.length() > 0) {
            sql = sql + " WHERE 1=1 " + searchSql;
        }
        sql += " ORDER BY userid ASC LIMIT " + beginIndex + "," + pageSize;
        List<UserBean> userList = null;
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            userList = runner.query(conn, sql, new BeanListHandler<>(UserBean.class));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return userList;
    }

    /**
     * @param userid 用户编号
     * @return 存在返回一个用户对象，不存在返回null
     * @description 通过用户编号从数据库中获取用户
     */
    public UserBean getUserById(int userid) throws SQLException {
        String sql = "SELECT * FROM t_user WHERE userid = ?";
        UserBean userBean = null;
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            userBean = runner.query(conn, sql, new BeanHandler<>(UserBean.class), userid);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return userBean;
    }

    /**
     * @param username 用户名
     * @param pwd      密码
     * @return 存在返回一个用户对象，不存在返回null
     * @description 通过用户名和密码从数据库中获取用户
     */
    public UserBean getUserByUP(String username, String pwd) throws SQLException {
        String sql = "SELECT * FROM t_user WHERE username = ? AND pwd = ?";
        UserBean userBean = null;
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        Object[] params = {username, pwd};
        try {
            userBean = runner.query(conn, sql, new BeanHandler<>(UserBean.class), params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return userBean;
    }

    /**
     * @return 最大userid加1后的值
     * @description 获取数据库中最大userid加1后的值
     */
    public int getMaxUserid() {
        String sql = "SELECT IFNULL(MAX(userid),0)+1 AS max_userid FROM t_user";
        Long maxUserid = null;
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            maxUserid = (Long) runner.query(conn, sql, new ScalarHandler("max_userid"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return maxUserid.intValue();
    }

    /**
     * @param userid 用户编号
     * @return 存放返回true, 不存在返回false
     * @description 判断用户编号是否在数据库中已存在
     */
    public boolean isExistUserid(int userid) {
        String sql = "SELECT IFNULL(COUNT(userid),0) AS is_exist_userid FROM t_user WHERE userid = ?";
        Long cntUserid = null;
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            cntUserid = (Long) runner.query(conn, sql, new ScalarHandler("is_exist_userid"), userid);
            if (cntUserid > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return false;
    }

    /**
     * @param username 用户名
     * @return 存放返回true, 不存在返回false
     * @description 判断用户名是否在数据库中已存在
     */
    public boolean isExistUsername(String username) {
        String sql = "SELECT IFNULL(COUNT(username),0) AS is_exist_username FROM t_user WHERE username = ?";
        Long cntUsername = null;
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            cntUsername = (Long) runner.query(conn, sql, new ScalarHandler("is_exist_username"), username);
            if (cntUsername > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return false;
    }

    /**
     * @param userBean 用户对象
     * @description 将用户保存到数据库
     */
    public void saveUser(UserBean userBean) throws SQLException {
        int userid = userBean.getUserid();
        String username = userBean.getUsername();
        String pwd = userBean.getPwd();
        String truename = userBean.getTruename();
        String sex = userBean.getSex();
        Date brith = userBean.getBirth();
        String email = userBean.getEmail();
        String phone = userBean.getPhone();
        String address = userBean.getAddress();
        String code = userBean.getPostcode();
        int authLevel = userBean.getAuthLevel();
        Date regDate = userBean.getRegDate();
        String lockTag = userBean.getLockTag();
        Date lastDate = userBean.getLastDate();
        int loginNum = userBean.getLoginNum();

        String sql = "INSERT INTO t_user VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        Object[] params = {userid, username, pwd, truename, sex, brith, email, phone, address, code, authLevel, regDate, lockTag, lastDate, loginNum};
        try {
            runner.update(conn, sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    /**
     * @param userid 用户编号
     * @description 通过用户编号从数据库中删除用户
     */
    public void deleteUserById(int userid) throws SQLException {
        String sql = "DELETE FROM t_user WHERE userid = ?";
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            runner.update(conn, sql, userid);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    /**
     * @param userBean 用户对象
     * @description 更新用户（只改变基本信息的字段，即用户名、性别、出生日期、邮箱、电话、地址、邮编）
     */
    public void updateUser(UserBean userBean) throws SQLException {
        String sql = "UPDATE t_user SET truename=?,sex=?,birth=?,email=?,phone=?,address=?,postcode=? WHERE userid=?";
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        String truename = userBean.getTruename();
        String sex = userBean.getSex();
        Date brith = userBean.getBirth();
        String email = userBean.getEmail();
        String phone = userBean.getPhone();
        String address = userBean.getAddress();
        String postcode = userBean.getPostcode();
        int userid = userBean.getUserid();
        Object[] params = {truename, sex, brith, email, phone, address, postcode, userid};
        try {
            runner.update(conn, sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    /**
     * @param userid 用户编号
     * @param pwd    新密码
     * @description 修改密码
     */
    public void updatePwd(int userid, String pwd) throws SQLException {
        String sql = "UPDATE t_user SET pwd = ? WHERE userid = ?";
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        Object[] params = {pwd, userid};
        try {
            runner.update(conn, sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    /**
     * @param userid  用户编号
     * @param lockTag 修改后的冻结状态
     * @description 修改冻结状态
     */
    public void updateLock(int userid, String lockTag) throws SQLException {
        String sql = "UPDATE t_user SET lockTag = ? WHERE userid = ?";
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            runner.update(conn, sql, lockTag, userid);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    /**
     * @param userid   用户编号
     * @param lastDate 登录时间
     * @param loginNum 登录次数
     * @description 修改登录参数，包括最后登录时间lastDate、登录次数loginNum
     */
    public void updateLoginParams(int userid, Date lastDate, int loginNum) throws SQLException {
        String sql = "UPDATE t_user SET lastDate = ?, loginNum = ? WHERE userid = ?";
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        Object[] params = {lastDate, loginNum, userid};
        try {
            runner.update(conn, sql, params);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }
}
