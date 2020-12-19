package com.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.bean.TypeBean;
import com.util.JdbcUtil;

public class TypeDao {
    /**
     * 获取所有大类类别
     */
    public List<TypeBean> getParentTypeList() {
        String sql = "SELECT * FROM t_mc_type WHERE parentId = 0 ORDER BY typeId ASC";
        List<TypeBean> typeList = null;
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            typeList = runner.query(conn, sql, new BeanListHandler<>(TypeBean.class));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return typeList;
    }

    /**
     * 获取父类编号为parentId的所有小类类别
     */
    public List<TypeBean> getChildTypeList(int parentId) {
        String sql = "SELECT * FROM t_mc_type WHERE parentId = ? ORDER BY typeId ASC";
        List<TypeBean> typeList = null;
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            typeList = runner.query(conn, sql, new BeanListHandler<>(TypeBean.class), parentId);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return typeList;
    }

    public int getMaxTypeId() {
        String sql = "SELECT IFNULL(MAX(typeId),0)+1 AS max_id FROM t_mc_type";
        Long maxTypeId = null;
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            maxTypeId = (Long) runner.query(conn, sql, new ScalarHandler("max_id"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return maxTypeId.intValue();
    }

    public boolean isExistTypeId(int typeId) {
        String sql = "SELECT IFNULL(COUNT(typeId),0) AS is_exist_typeid FROM t_mc_type WHERE typeId = ?";
        Long cntTypeId = null;
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            cntTypeId = (Long) runner.query(conn, sql, new ScalarHandler("is_exist_typeid"), typeId);
            if (cntTypeId > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return false;
    }

    public void saveType(TypeBean typeBean) throws SQLException {
        int typeId = typeBean.getTypeId();
        String typeName = typeBean.getTypeName();
        int parentId = typeBean.getParentId();
        String sql = "INSERT INTO t_mc_type VALUES(?,?,?)";
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            runner.update(conn, sql, typeId, typeName, parentId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    public void deleteTypeById(int typeId) throws SQLException {
        String sql = "DELETE FROM t_mc_type WHERE typeId = ?";
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            runner.update(conn, sql, typeId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    public void deleteChildType(int parentId) throws SQLException {
        String sql = "DELETE FROM t_mc_type WHERE parentId = ?";
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            runner.update(conn, sql, parentId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtils.closeQuietly(conn);
        }
    }

    public TypeBean getType(int typeId) throws SQLException {
        String sql = "SELECT * FROM t_mc_type WHERE typeId = ?";
        TypeBean typeBean = null;
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            typeBean = runner.query(conn, sql, new BeanHandler<>(TypeBean.class), typeId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return typeBean;
    }

    public void updateType(TypeBean typeBean) throws SQLException {
        String sql = "UPDATE t_mc_type SET typeName = ?, parentId = ? WHERE typeId = ?";
        String typeName = typeBean.getTypeName();
        int parentId = typeBean.getParentId();
        int typeId = typeBean.getTypeId();
        Object[] params = {typeName, parentId, typeId};
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
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
     * 判断类别编号为parentId的大类下是否有商品名称为typeName的商品
     */
    public boolean isExistTypeName(String typeName, int parentId) {
        String sql = "SELECT IFNULL(COUNT(typeName),0) AS is_exist_typename FROM t_mc_type WHERE typeName = ?  AND parentId = ?";
        Long cntTypeName = null;
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            cntTypeName = (Long) runner.query(conn, sql, new ScalarHandler("is_exist_typename"), typeName, parentId);
            if (cntTypeName > 0) {
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
     * 判断类别编号为parentId的大类下是否有除类别编号为typeId以外的商品名称为typeName的商品
     */
    public boolean isExistTypeName(int typeId, String typeName, int parentId) {
        String sql = "SELECT IFNULL(COUNT(typeName),0) AS is_exist_typename FROM t_mc_type WHERE typeId != ? AND typeName = ? AND parentId = ?";
        Long cntTypeName = null;
        Connection conn = JdbcUtil.getConnection();
        QueryRunner runner = new QueryRunner();
        try {
            cntTypeName = (Long) runner.query(conn, sql, new ScalarHandler("is_exist_typename"), typeId, typeName, parentId);
            if (cntTypeName > 0) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn);
        }
        return false;
    }

    public Map<Integer, String> getTypeMap() {
        String sql = "SELECT typeId,typeName FROM t_mc_type ORDER BY typeId ASC";
        Connection conn = JdbcUtil.getConnection();
        Statement stmt = null;
        ResultSet rs = null;
        Map<Integer, String> typeMap = new LinkedHashMap<>();
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                int typeId = rs.getInt("typeId");
                String typeName = rs.getString("typeName");
                typeMap.put(typeId, typeName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        return typeMap;
    }

}
