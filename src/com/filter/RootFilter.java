package com.filter;

import com.bean.UserBean;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class RootFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException,
            ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpSession session = req.getSession();
        UserBean userBean = (UserBean) session.getAttribute("userBean");
        int authLevel = userBean.getAuthLevel();
        if (authLevel != 1) {
            String redirectUrl = req.getContextPath() + "/permission_denied.html";
            resp.sendRedirect(redirectUrl);
        }
        filterChain.doFilter(req, resp);
    }
}
