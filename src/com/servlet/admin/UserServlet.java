package com.servlet.admin;


import com.bean.UserBean;
import com.google.gson.Gson;

import com.service.UserService;
import com.util.GlobalUtil;
import com.util.PageUtil;
import com.util.ResponseInfo;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;

import java.util.*;


@WebServlet(urlPatterns = "/servlet/UserServlet")
public class UserServlet extends HttpServlet {
    private UserService userService = new UserService();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String task = req.getParameter("task");
        if ("userList".equals(task)) {
            userList(req, resp);
        } else if ("adminList".equals(task)) {
            adminList(req, resp);
        } else if ("updateLock".equals(task)) {
            updateLock(req, resp);
        } else if ("addAdmin".equals(task)) {
            addAdmin(req, resp);
        } else if ("saveAdmin".equals(task)) {
            saveAdmin(req, resp);
        } else if ("deleteAdmin".equals(task)) {
            deleteAdmin(req, resp);
        } else if ("editAdmin".equals(task)) {
            editAdmin(req, resp);
        } else if ("updateAdmin".equals(task)) {
            updateAdmin(req, resp);
        } else if ("isExistUsername".equals(task)) {
            isExistUsername(req, resp);
        } else if ("editAdminPwd".equals(task)) {
            editAdminPwd(req, resp);
        } else if ("updateAdminPwd".equals(task)) {
            updateAdminPwd(req, resp);
        }
    }

    private void userList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //查询参数
        String authLevel = "9";
        String username = req.getParameter("username");
        String sex = req.getParameter("sex");
        String lockTag = req.getParameter("lockTag");

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("authLevel", authLevel);
        paramMap.put("username", username);
        paramMap.put("sex", sex);
        paramMap.put("lockTag", lockTag);
        req.setAttribute("paramMap", paramMap);

        //分页
        PageUtil pageUtil = new PageUtil(req);
        pageUtil.setPageSize(5);
        pageUtil.setRsCount(userService.getUserCount(paramMap));
        int pageSize = pageUtil.getPageSize();
        int currentPage = pageUtil.getCurrentPage();
        int rsCount = pageUtil.getRsCount();
        int pageCount = pageUtil.getPageCount();
        int beginIndex = (currentPage - 1) * pageSize;

        List<UserBean> userList = userService.getUserList(beginIndex, pageSize, paramMap);
        req.setAttribute("userList", userList);

        String pageTool = pageUtil.createPageTool(PageUtil.BbsText);
        req.setAttribute("pageTool", pageTool);

        String forwardUrl = "/admin/user/user_list.jsp";
        req.getRequestDispatcher(forwardUrl).forward(req, resp);
    }

    private void adminList(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //查询参数
        String authLevel = "5";
        String username = req.getParameter("username");
        String sex = req.getParameter("sex");
        String lockTag = req.getParameter("lockTag");

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("authLevel", authLevel);
        paramMap.put("username", username);
        paramMap.put("sex", sex);
        paramMap.put("lockTag", lockTag);
        req.setAttribute("paramMap", paramMap);

        //分页
        PageUtil pageUtil = new PageUtil(req);
        pageUtil.setPageSize(5);
        pageUtil.setRsCount(userService.getUserCount(paramMap));
        int pageSize = pageUtil.getPageSize();
        int currentPage = pageUtil.getCurrentPage();
        int rsCount = pageUtil.getRsCount();
        int pageCount = pageUtil.getPageCount();
        int beginIndex = (currentPage - 1) * pageSize;

        List<UserBean> userList = userService.getUserList(beginIndex, pageSize, paramMap);
        req.setAttribute("userList", userList);

        String pageTool = pageUtil.createPageTool(PageUtil.BbsText);
        req.setAttribute("pageTool", pageTool);

        String forwardUrl = "/admin/user/admin_list.jsp";
        req.getRequestDispatcher(forwardUrl).forward(req, resp);
    }

    private void updateLock(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userid = req.getParameter("userid");

        PrintWriter out = resp.getWriter();
        ResponseInfo responseInfo = new ResponseInfo();
        Gson gson = new Gson();

        try {
            UserBean userBean = userService.getUserById(Integer.parseInt(userid));
            String lockTag = userBean.getLockTag();//原先的冻结状态
            if ("0".equals(lockTag)) {
                //状态为未冻结，冻结用户
                userService.lockUser(Integer.parseInt(userid));
            } else if ("1".equals(lockTag)) {
                //状态为已冻结，解冻用户
                userService.unlockUser(Integer.parseInt(userid));
            }
            responseInfo.setFlag(true);
        } catch (Exception e) {
            e.printStackTrace();
            responseInfo.setFlag(false);
            responseInfo.setMessage(e.getMessage());
        } finally {
            String json = gson.toJson(responseInfo);
            out.print(json);
            out.flush();
            out.close();
        }
    }

    private void addAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int maxUserid = userService.getMaxUserid();
        req.setAttribute("maxUserid", maxUserid);

        String forwardUrl = "/admin/user/admin_add.jsp";
        req.getRequestDispatcher(forwardUrl).forward(req, resp);
    }

    private void saveAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        ResponseInfo responseInfo = new ResponseInfo();
        Gson gson = new Gson();

        String username = req.getParameter("username");

        boolean isExistUsername = userService.isExistUsername(username);
        if (isExistUsername) {
            responseInfo.setFlag(false);
            responseInfo.setMessage("该用户名已存在");
            String json = gson.toJson(responseInfo);
            out.print(json);
            out.flush();
            out.close();
            return;
        }

        String userid = req.getParameter("userid");
        String pwd = req.getParameter("pwd");
        String truename = req.getParameter("truename");
        String sex = req.getParameter("sex");
        String birth = req.getParameter("birth");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String postcode = req.getParameter("postcode");

        UserBean userBean = new UserBean();
        userBean.setUserid(Integer.parseInt(userid));
        userBean.setUsername(username);
        userBean.setPwd(pwd);
        userBean.setTruename(truename);
        userBean.setSex(sex);
        userBean.setBirth(GlobalUtil.formatDate(birth));
        userBean.setEmail(email);
        userBean.setPhone(phone);
        userBean.setAddress(address);
        userBean.setPostcode(postcode);
        userBean.setAuthLevel(5);
        userBean.setRegDate(new Date());
        userBean.setLockTag("0");
        userBean.setLastDate(null);
        userBean.setLoginNum(0);

        try {
            userService.saveUser(userBean);
            responseInfo.setFlag(true);
        } catch (Exception e) {
            e.printStackTrace();
            responseInfo.setFlag(false);
            responseInfo.setMessage(e.getMessage());
        } finally {
            String json = gson.toJson(responseInfo);
            out.print(json);
            out.flush();
            out.close();
        }
    }

    private void deleteAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userid = req.getParameter("userid");

        PrintWriter out = resp.getWriter();
        ResponseInfo responseInfo = new ResponseInfo();
        Gson gson = new Gson();
        try {
            userService.deleteUserById(Integer.parseInt(userid));
            responseInfo.setFlag(true);
        } catch (Exception e) {
            e.printStackTrace();
            responseInfo.setFlag(false);
            responseInfo.setMessage(e.getMessage());
        } finally {
            String json = gson.toJson(responseInfo);
            out.print(json);
            out.flush();
            out.close();
        }
    }

    private void editAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        String userid = req.getParameter("userid");
        try {
            UserBean userBean = userService.getUserById(Integer.parseInt(userid));
            req.setAttribute("userBean", userBean);

            String forwardUrl = "/admin/user/admin_edit.jsp";
            req.getRequestDispatcher(forwardUrl).forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setContentType("text/html");
            //String redirectUrl = req.getContextPath() + "/servlet/ItemServlet?task=list";
            out.print("<script>");
            out.print("alert('" + e.getMessage() + "');");
            // out.print("window.location.href='" + redirectUrl + "'");
            out.print("</script>");
        } finally {
            out.flush();
            out.close();
        }
    }

    private void updateAdmin(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取表单数据
        String userid = req.getParameter("userid");
        String truename = req.getParameter("truename");
        String sex = req.getParameter("sex");
        String birth = req.getParameter("birth");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String postcode = req.getParameter("postcode");

        UserBean userBean = new UserBean();
        userBean.setUserid(Integer.parseInt(userid));
        userBean.setTruename(truename);
        userBean.setSex(sex);
        userBean.setBirth(GlobalUtil.formatDate(birth));
        userBean.setEmail(email);
        userBean.setPhone(phone);
        userBean.setAddress(address);
        userBean.setPostcode(postcode);

        PrintWriter out = resp.getWriter();
        ResponseInfo responseInfo = new ResponseInfo();
        Gson gson = new Gson();
        try {
            userService.updateUser(userBean);
            responseInfo.setFlag(true);
        } catch (Exception e) {
            e.printStackTrace();
            responseInfo.setFlag(false);
            responseInfo.setMessage(e.getMessage());
        } finally {
            String json = gson.toJson(responseInfo);
            out.print(json);
            out.flush();
            out.close();
        }
    }

    private void isExistUsername(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        boolean isExistUsername = userService.isExistUsername(username);

        ResponseInfo responseInfo = new ResponseInfo();
        if (isExistUsername) {
            responseInfo.setFlag(true);
            responseInfo.setMessage("该用户名已存在");
        } else {
            responseInfo.setFlag(false);
            responseInfo.setMessage("该用户名可用");
        }

        Gson gson = new Gson();
        String json = gson.toJson(responseInfo);
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
        out.close();
    }

    private void editAdminPwd(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        PrintWriter out = resp.getWriter();

        String userid = req.getParameter("userid");
        try {
            UserBean userBean = userService.getUserById(Integer.parseInt(userid));
            req.setAttribute("userBean", userBean);

            String forwardUrl = "/admin/user/admin_edit_pwd.jsp";
            req.getRequestDispatcher(forwardUrl).forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setContentType("text/html");
            out.print("<script>");
            out.print("alert('" + e.getMessage() + "');");
            out.print("</script>");
        } finally {
            out.flush();
            out.close();
        }
    }

    private void updateAdminPwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userid = req.getParameter("userid");
        String oldPwd = req.getParameter("oldPwd");
        String newPwd = req.getParameter("pwd");


        PrintWriter out = resp.getWriter();
        ResponseInfo responseInfo = new ResponseInfo();
        Gson gson = new Gson();
        try {
            userService.updatePwd(Integer.parseInt(userid), oldPwd, newPwd);
            responseInfo.setFlag(true);
        } catch (Exception e) {
            responseInfo.setFlag(false);
            responseInfo.setMessage(e.getMessage());
        } finally {
            String json = gson.toJson(responseInfo);
            out.print(json);
            out.flush();
            out.close();
        }
    }

}
