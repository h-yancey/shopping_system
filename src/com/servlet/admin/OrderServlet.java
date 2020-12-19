package com.servlet.admin;

import com.bean.*;
import com.google.gson.Gson;
import com.service.OrderItemService;
import com.service.OrderService;
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
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@WebServlet(urlPatterns = "/servlet/OrderServlet")
public class OrderServlet extends HttpServlet {
    private OrderService orderService = new OrderService();
    private OrderItemService orderItemService = new OrderItemService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String task = req.getParameter("task");
        if ("list".equals(task)) {
            list(req, resp);
        } else if ("orderInfo".equals(task)) {
            orderInfo(req, resp);
        } else if ("editOrderAudit".equals(task)) {
            editOrderAudit(req, resp);
        } else if ("updateOrderAudit".equals(task)) {
            updateOrderAudit(req, resp);
        } else if ("editOrder".equals(task)) {
            editOrder(req, resp);
        } else if ("updateOrder".equals(task)) {
            updateOrder(req, resp);
        }
    }

    private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //查询参数
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        String orderUser = req.getParameter("orderUser");
        String itemName = req.getParameter("itemName");
        String auditStatus = req.getParameter("auditStatus");

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);
        paramMap.put("orderUser", orderUser);
        paramMap.put("itemName", itemName);
        paramMap.put("auditStatus", auditStatus);
        req.setAttribute("paramMap", paramMap);

        //分页
        PageUtil pageUtil = new PageUtil(req);
        pageUtil.setPageSize(5);
        pageUtil.setRsCount(orderService.getOrderCount(paramMap));

        int pageSize = pageUtil.getPageSize();
        int currentPage = pageUtil.getCurrentPage();
        int rsCount = pageUtil.getRsCount();
        int pageCount = pageUtil.getPageCount();

        int beginIndex = (currentPage - 1) * pageSize;
        List<OrderBean> orderList = orderService.getOrderList(beginIndex, pageSize, paramMap);
        req.setAttribute("orderList", orderList);

        String pageTool = pageUtil.createPageTool(PageUtil.BbsText);
        req.setAttribute("pageTool", pageTool);

        String forwardUrl = "/admin/order_list.jsp";
        req.getRequestDispatcher(forwardUrl).forward(req, resp);
    }

    private void orderInfo(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String orderId = req.getParameter("orderId");
        try {
            OrderBean orderBean = orderService.getOrder(Integer.parseInt(orderId));
            List<OrderItemBean> orderItemList = orderItemService.getOrderItemListByOrderId(orderBean.getOrderId());
            req.setAttribute("orderBean", orderBean);
            req.setAttribute("orderItemList", orderItemList);
            String forwardUrl = "/admin/order_info.jsp";
            req.getRequestDispatcher(forwardUrl).forward(req, resp);
        } catch (Exception e) {
            String redirectUrl = req.getContextPath() + "/admin/admin_index.jsp";
            resp.sendRedirect(redirectUrl);
        }
    }

    private void editOrderAudit(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String orderId = req.getParameter("orderId");
        try {
            OrderBean orderBean = orderService.getOrder(Integer.parseInt(orderId));
            req.setAttribute("orderBean", orderBean);
            String forwardUrl = "/admin/order_audit.jsp";
            req.getRequestDispatcher(forwardUrl).forward(req, resp);
        } catch (Exception e) {
            String redirectUrl = req.getContextPath() + "/admin/admin_index.jsp";
            resp.sendRedirect(redirectUrl);
        }
    }

    private void updateOrderAudit(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        UserBean userBean = (UserBean) session.getAttribute("userBean");
        String orderId = req.getParameter("orderId");
        String auditStatus = req.getParameter("auditStatus");
        String msg = req.getParameter("msg");
        String username = userBean.getUsername();

        OrderBean orderBean = new OrderBean();
        orderBean.setOrderId(Integer.parseInt(orderId));
        orderBean.setAuditStatus(auditStatus);
        orderBean.setMsg(msg);
        orderBean.setAuditUser(username);
        orderBean.setAuditDate(new Date());

        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();
        ResponseInfo responseInfo = new ResponseInfo();
        try {
            orderService.updateOrderAudit(orderBean);
            responseInfo.setFlag(true);
        } catch (SQLException e) {
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

    private void editOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String orderId = req.getParameter("orderId");
        try {
            OrderBean orderBean = orderService.getOrder(Integer.parseInt(orderId));
            req.setAttribute("orderBean", orderBean);
            String forwardUrl = "/admin/order_edit.jsp";
            req.getRequestDispatcher(forwardUrl).forward(req, resp);
        } catch (Exception e) {
            String redirectUrl = req.getContextPath() + "/admin/admin_index.jsp";
            resp.sendRedirect(redirectUrl);
        }
    }

    private void updateOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String orderId = req.getParameter("orderId");
        String payType = req.getParameter("payType");
        String sendType = req.getParameter("sendType");
        String consignee = req.getParameter("consignee");
        String address = req.getParameter("address");
        String postcode = req.getParameter("postcode");
        String phone = req.getParameter("phone");
        String email = req.getParameter("email");

        OrderBean orderBean = new OrderBean();
        orderBean.setOrderId(Integer.parseInt(orderId));
        orderBean.setPayType(payType);
        orderBean.setSendType(sendType);
        orderBean.setConsignee(consignee);
        orderBean.setAddress(address);
        orderBean.setPostcode(postcode);
        orderBean.setPhone(phone);
        orderBean.setEmail(email);

        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();
        ResponseInfo responseInfo = new ResponseInfo();
        try {
            orderService.updateOrder(orderBean);
            responseInfo.setFlag(true);
        } catch (SQLException e) {
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
}
