package com.servlet.front;

import com.bean.UserBean;
import com.google.gson.Gson;
import com.service.UserService;
import com.util.GlobalUtil;
import com.util.ResponseInfo;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;


@WebServlet(urlPatterns = "/servlet/FrontLoginServlet")
public class FrontLoginServlet extends HttpServlet {
    private UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String task = req.getParameter("task");
        if ("login".equals(task)) {
            login(req, resp);
        } else if ("logout".equals(task)) {
            logout(req, resp);
        }
    }

    private void login(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String pwd = req.getParameter("pwd");
        String referer = req.getParameter("referer");

        PrintWriter out = resp.getWriter();
        ResponseInfo responseInfo = new ResponseInfo();
        Gson gson = new Gson();
        try {
            UserBean userBean = userService.getLoginUser(username, pwd);
            String lockTag = userBean.getLockTag();
            if ("1".equals(lockTag)) {
                //被冻结，禁止登录
                responseInfo.setFlag(false);
                responseInfo.setMessage("用户被冻结");
            } else if ("0".equals(lockTag)) {
                //登录
                HttpSession session = req.getSession();
                session.setAttribute("frontUserBean", userBean);
                userService.updateLoginParams(userBean);
                responseInfo.setFlag(true);
                if (GlobalUtil.isEmpty(referer)) {
                    //referer为空时，跳转到的url为首页
                    responseInfo.setMessage(req.getContextPath());
                } else {
                    responseInfo.setMessage(referer);
                }
            }
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

    private void logout(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        session.removeAttribute("frontUserBean");
        String redirectUrl = req.getContextPath();
        resp.sendRedirect(redirectUrl);
    }
}
