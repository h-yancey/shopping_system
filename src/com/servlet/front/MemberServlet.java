package com.servlet.front;

import com.bean.OrderBean;
import com.bean.OrderItemBean;
import com.bean.UserBean;
import com.google.gson.Gson;
import com.service.OrderItemService;
import com.service.OrderService;
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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(urlPatterns = "/member")
public class MemberServlet extends HttpServlet {
    private UserService userService = new UserService();
    private OrderService orderService = new OrderService();
    private OrderItemService orderItemService = new OrderItemService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String task = req.getParameter("task");
        if (GlobalUtil.isEmpty(task)) {
            editProfile(req, resp);
        } else if ("updateProfile".equals(task)) {
            updateProfile(req, resp);
        } else if ("editPwd".equals(task)) {
            editPwd(req, resp);
        } else if ("updatePwd".equals(task)) {
            updatePwd(req, resp);
        } else if ("myOrder".equals(task)) {
            myOrder(req, resp);
        } else if ("orderInfo".equals(task)) {
            orderInfo(req, resp);
        }
    }


    /**
     * 重新设置登录的用户
     */
    private void refreshLoginFrontUser(HttpServletRequest req, int userid) throws Exception {
        HttpSession session = req.getSession();
        UserBean frontUserBean = userService.getUserById(userid);
        session.setAttribute("frontUserBean", frontUserBean);
    }

    private void editProfile(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        PrintWriter out = resp.getWriter();

        try {
            String forwardUrl = "/front/member/profile_edit.jsp";
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

    private void updateProfile(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        HttpSession session = req.getSession();
        UserBean frontUserBean = (UserBean) session.getAttribute("frontUserBean");
        int userid = frontUserBean.getUserid();
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
            this.refreshLoginFrontUser(req, userid);
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

    private void editPwd(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        PrintWriter out = resp.getWriter();

        try {
            String forwardUrl = "/front/member/pwd_edit.jsp";
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

    private void updatePwd(HttpServletRequest req, HttpServletResponse resp) throws  IOException {
        HttpSession session = req.getSession();
        UserBean frontUserBean = (UserBean) session.getAttribute("frontUserBean");
        int userid = frontUserBean.getUserid();
        String oldPwd = req.getParameter("oldPwd");
        String newPwd = req.getParameter("pwd");


        PrintWriter out = resp.getWriter();
        ResponseInfo responseInfo = new ResponseInfo();
        Gson gson = new Gson();
        try {
            userService.updatePwd(userid, oldPwd, newPwd);
            this.refreshLoginFrontUser(req, userid);
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

    private void myOrder(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        UserBean frontUserBean = (UserBean) session.getAttribute("frontUserBean");

        //查询参数
        String orderDate = req.getParameter("orderDate");
        String auditStatus = req.getParameter("auditStatus");
        String orderUser = frontUserBean.getUsername();

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("orderDate", orderDate);
        paramMap.put("auditStatus", auditStatus);
        paramMap.put("orderUser", orderUser);
        req.setAttribute("paramMap", paramMap);

        //分页
        PageUtil pageUtil = new PageUtil(req);
        pageUtil.setPageSize(5);
        pageUtil.setRsCount(orderService.getOrderCount(paramMap));

        int pageSize = pageUtil.getPageSize();
        int currentPage = pageUtil.getCurrentPage();
        int rsCount = pageUtil.getRsCount();
        int pageCount = pageUtil.getPageCount();

        String pageTool = pageUtil.createPageTool(PageUtil.BbsText);
        req.setAttribute("pageTool", pageTool);

        int beginIndex = (currentPage - 1) * pageSize;
        List<OrderBean> orderList = orderService.getOrderList(beginIndex, pageSize, paramMap);
        req.setAttribute("orderList", orderList);

        String forwardUrl = "/front/member/my_order.jsp";
        req.getRequestDispatcher(forwardUrl).forward(req, resp);
    }

    private void orderInfo(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //查询参数
        String orderId = req.getParameter("orderId");

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("orderId", orderId);
        req.setAttribute("paramMap", paramMap);

        try {
            OrderBean orderBean = orderService.getOrder(Integer.parseInt(orderId));
            req.setAttribute("orderBean", orderBean);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //分页
        PageUtil pageUtil = new PageUtil(req);
        pageUtil.setPageSize(5);
        pageUtil.setRsCount(orderItemService.getOrderItemCount(paramMap));

        int pageSize = pageUtil.getPageSize();
        int currentPage = pageUtil.getCurrentPage();
        int rsCount = pageUtil.getRsCount();
        int pageCount = pageUtil.getPageCount();

        String pageTool = pageUtil.createPageTool(PageUtil.BbsText);
        req.setAttribute("pageTool", pageTool);

        int beginIndex = (currentPage - 1) * pageSize;
        List<OrderItemBean> orderItemList = orderItemService.getOrderItemList(beginIndex, pageSize, paramMap);
        req.setAttribute("orderItemList", orderItemList);

        String forwardUrl = "/front/member/order_info.jsp";
        req.getRequestDispatcher(forwardUrl).forward(req, resp);
    }

}
