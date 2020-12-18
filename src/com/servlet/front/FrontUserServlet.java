package com.servlet.front;


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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@WebServlet(urlPatterns = "/servlet/FrontUserServlet")
public class FrontUserServlet extends HttpServlet {
    private UserService userService = new UserService();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String task = req.getParameter("task");
        if ("saveUser".equals(task)) {
            saveUser(req, resp);
        } else if ("editUser".equals(task)) {
            editUser(req, resp);
        } else if ("updateUser".equals(task)) {
            updateUser(req, resp);
        } else if ("isExistUsername".equals(task)) {
            isExistUsername(req, resp);
        } else if ("editUserPwd".equals(task)) {
            editUserPwd(req, resp);
        } else if ("updateUserPwd".equals(task)) {
            updateUserPwd(req, resp);
        }
    }

    private void saveUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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

        int userid = userService.getMaxUserid();
        String pwd = req.getParameter("pwd");
        String truename = req.getParameter("truename");
        String sex = req.getParameter("sex");
        String birth = req.getParameter("birth");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String postcode = req.getParameter("postcode");

        UserBean userBean = new UserBean();
        userBean.setUserid(userid);
        userBean.setUsername(username);
        userBean.setPwd(pwd);
        userBean.setTruename(truename);
        userBean.setSex(sex);
        userBean.setBirth(GlobalUtil.formatDate(birth));
        userBean.setEmail(email);
        userBean.setPhone(phone);
        userBean.setAddress(address);
        userBean.setPostcode(postcode);
        userBean.setAuthLevel(9);
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


    private void editUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        String userid = req.getParameter("userid");
        try {
            UserBean userBean = userService.getUser(Integer.parseInt(userid));
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

    private void updateUser(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
            userService.updateUser(Integer.parseInt(userid), userBean);
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

    private void editUserPwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        String userid = req.getParameter("userid");
        try {
            UserBean userBean = userService.getUser(Integer.parseInt(userid));
            req.setAttribute("userBean", userBean);

            String forwardUrl = "/admin/user/admin_edit_pwd.jsp";
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

    private void updateUserPwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
