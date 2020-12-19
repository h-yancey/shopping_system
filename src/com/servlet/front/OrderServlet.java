package com.servlet.front;

import com.bean.OrderBean;
import com.bean.OrderItemBean;
import com.bean.UserBean;
import com.google.gson.Gson;
import com.service.OrderItemService;
import com.service.OrderService;
import com.util.GlobalUtil;
import com.util.ResponseInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;;import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;


@WebServlet(urlPatterns = "/order")
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
        if (GlobalUtil.isEmpty(task)) {
            toCheckout(req, resp);
        } else if ("checkout".equals(task)) {
            checkout(req, resp);
        } else if ("placeOrder".equals(task)) {
            placeOrder(req, resp);
        }
    }


    private void toCheckout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String forwardUrl = "/front/order/checkout.jsp";
        req.getRequestDispatcher(forwardUrl).forward(req, resp);
    }

    private void checkout(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String payType = req.getParameter("payType");
        String sendType = req.getParameter("sendType");
        int maxOrderId = orderService.getMaxOrderId();
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("maxOrderId", maxOrderId);
        paramMap.put("payType", payType);
        paramMap.put("sendType", sendType);
        HttpSession session = req.getSession();
        session.setAttribute("paramMap", paramMap);
        String forwardUrl = "/front/order/order.jsp";
        req.getRequestDispatcher(forwardUrl).forward(req, resp);
    }

    private void placeOrder(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        UserBean frontUserBean = (UserBean) session.getAttribute("frontUserBean");

        String orderId = req.getParameter("orderId");
        String itemTypeSize = req.getParameter("itemTypeSize");
        String itemSize = req.getParameter("itemSize");
        String totalPrice = req.getParameter("totalPrice");
        String payType = req.getParameter("payType");
        String sendType = req.getParameter("sendType");
        String consignee = req.getParameter("consignee");
        String address = req.getParameter("address");
        String postcode = req.getParameter("postcode");
        String phone = req.getParameter("phone");
        String email = req.getParameter("email");

        OrderBean orderBean = new OrderBean();
        orderBean.setOrderId(Integer.parseInt(orderId));
        orderBean.setOrderUser(frontUserBean.getUsername());
        orderBean.setOrderDate(new Date());
        orderBean.setPayType(payType);
        orderBean.setSendType(sendType);
        orderBean.setItemTypeSize(Integer.parseInt(itemTypeSize));
        orderBean.setItemSize(Integer.parseInt(itemSize));
        orderBean.setTotalPrice(new BigDecimal(totalPrice));
        orderBean.setAuditStatus("1");//未审核
        orderBean.setConsignee(consignee);
        orderBean.setAddress(address);
        orderBean.setPostcode(postcode);
        orderBean.setPhone(phone);
        orderBean.setEmail(email);

        PrintWriter out = resp.getWriter();
        ResponseInfo responseInfo = new ResponseInfo();
        Gson gson = new Gson();

        Set<OrderItemBean> cartItemSet = (LinkedHashSet<OrderItemBean>) session.getAttribute("cartItemSet");
        try {
            orderService.saveOrder(orderBean);
            for (OrderItemBean orderItemBean : cartItemSet) {
                int id = orderItemService.getMaxId();
                orderItemBean.setId(id);
                orderItemBean.setOrderId(orderBean.getOrderId());
                orderItemService.saveOrderItem(orderItemBean);
            }
            //成功下单后清空购物车相关信息
            session.removeAttribute("cartItemSet");
            session.removeAttribute("cartItemCount");
            session.removeAttribute("cartTotalPrice");
            session.removeAttribute("typeSet");
            responseInfo.setFlag(true);
            responseInfo.setMessage("订单编号：" + orderBean.getOrderId() + "<br>下单时间：" + GlobalUtil.formatDateTime(orderBean.getOrderDate()));
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

}
