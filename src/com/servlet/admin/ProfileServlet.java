package com.servlet.admin;

import com.bean.UserBean;
import com.google.gson.Gson;
import com.service.UserService;
import com.util.GlobalUtil;
import com.util.ResponseInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(urlPatterns = "/servlet/ProfileServlet")
public class ProfileServlet extends HttpServlet {
    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String task = req.getParameter("task");
        if ("editProfile".equals(task)) {
            editProfile(req, resp);
        } else if ("updateProfile".equals(task)) {
            updateProfile(req, resp);
        } else if ("editPwd".equals(task)) {
            editPwd(req, resp);
        } else if ("updatePwd".equals(task)) {
            updatePwd(req, resp);
        }
    }
    /**
     * @description 重新设置登录的用户
     * @param req
     * @param userid 用户编号
     * @return
     */
    private void refreshLoginUser(HttpServletRequest req, int userid) throws Exception {
        HttpSession session = req.getSession();
        UserBean userBean = userService.getUserById(userid);
        session.setAttribute("userBean", userBean);
    }


    private void editProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        try {
            String forwardUrl = "/admin/profile/profile_edit.jsp";
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

    private void updateProfile(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        UserBean loginUserBean = (UserBean) session.getAttribute("userBean");
        int userid = loginUserBean.getUserid();

        String truename = req.getParameter("truename");
        String sex = req.getParameter("sex");
        String birth = req.getParameter("birth");
        String email = req.getParameter("email");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String postcode = req.getParameter("postcode");

        UserBean userBean = new UserBean();
        userBean.setUserid(userid);
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
            refreshLoginUser(req, userid);
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

    private void editPwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        try {
            String forwardUrl = "/admin/profile/pwd_edit.jsp";
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

    private void updatePwd(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        UserBean userBean = (UserBean) session.getAttribute("userBean");
        int userid = userBean.getUserid();

        String oldPwd = req.getParameter("oldPwd");
        String newPwd = req.getParameter("pwd");

        PrintWriter out = resp.getWriter();
        ResponseInfo responseInfo = new ResponseInfo();
        Gson gson = new Gson();
        try {
            userService.updatePwd(userid, oldPwd, newPwd);
            refreshLoginUser(req, userid);
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
