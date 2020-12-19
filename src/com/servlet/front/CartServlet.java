package com.servlet.front;

import com.bean.ItemBean;
import com.bean.OrderItemBean;
import com.google.gson.Gson;
import com.service.ItemService;
import com.util.GlobalUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;

@WebServlet(urlPatterns = "/cart")
public class CartServlet extends HttpServlet {
    private ItemService itemService = new ItemService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String task = req.getParameter("task");
        if (GlobalUtil.isEmpty(task)) {
            list(req, resp);
        } else if ("add".equals(task)) {
            add(req, resp);
        } else if ("delete".equals(task)) {
            delete(req, resp);
        } else if ("clear".equals(task)) {
            clear(req, resp);
        } else if ("updateCartItemCount".equals(task)) {
            updateCartItemCount(req, resp);
        }
    }


    private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String forwardUrl = "front/cart.jsp";
        req.getRequestDispatcher(forwardUrl).forward(req, resp);
    }

    private void add(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        Set<OrderItemBean> cartItemSet = (LinkedHashSet<OrderItemBean>) session.getAttribute("cartItemSet");//购物车中的商品集合
        if (cartItemSet == null) {
            cartItemSet = new LinkedHashSet<>();
        }

        String itemId = req.getParameter("itemId");
        try {
            ItemBean itemBean = itemService.getItem(Integer.parseInt(itemId));
            OrderItemBean addOrderItem = new OrderItemBean();
            addOrderItem.setItemId(itemBean.getItemId());
            addOrderItem.setItemName(itemBean.getItemName());
            addOrderItem.setItemDesc(itemBean.getItemDesc());
            addOrderItem.setImgName(itemBean.getImgName());
            addOrderItem.setItemPrice(itemBean.getItemPrice());
            boolean containFlag = false;
            for (OrderItemBean orderItemBean : cartItemSet) {
                if (orderItemBean.getItemId() == addOrderItem.getItemId()) {
                    containFlag = true;
                    orderItemBean.setItemCount(orderItemBean.getItemCount() + 1);
                    orderItemBean.setTotalPrice(orderItemBean.getItemPrice().multiply(new BigDecimal(orderItemBean.getItemCount())));
                    break;
                }
            }
            if (!containFlag) {
                addOrderItem.setItemCount(1);
                addOrderItem.setTotalPrice(addOrderItem.getItemPrice());
                cartItemSet.add(addOrderItem);
            }
            session.setAttribute("cartItemSet", cartItemSet);
            this.refreshCart(session);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("cartItemCount", session.getAttribute("cartItemCount"));
        returnMap.put("typeCount", ((Set) session.getAttribute("typeSet")).size());
        returnMap.put("cartTotalPrice", GlobalUtil.formatBigDecimal((BigDecimal) session.getAttribute("cartTotalPrice")));

        String json = gson.toJson(returnMap);
        out.print(json);
        out.flush();
        out.close();
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) {
        String itemId = req.getParameter("itemId");

        HttpSession session = req.getSession();
        Set<OrderItemBean> cartItemSet = (LinkedHashSet<OrderItemBean>) session.getAttribute("cartItemSet");
        Iterator<OrderItemBean> iterator = cartItemSet.iterator();
        OrderItemBean deleteOrderItem = null;
        while (iterator.hasNext()) {
            deleteOrderItem = iterator.next();
            if (deleteOrderItem.getItemId() == Integer.parseInt(itemId)) {
                iterator.remove();
                break;
            }
        }
        session.setAttribute("cartItemSet", cartItemSet);
        this.refreshCart(session);
//        Integer cartItemCount = (Integer) session.getAttribute("cartItemCount");
//        BigDecimal cartTotalPrice = (BigDecimal) session.getAttribute("cartTotalPrice");
//        int deleteItemCount = deleteOrderItem == null ? 0 : deleteOrderItem.getItemCount();
//        cartItemCount -= deleteItemCount;
//        cartTotalPrice = cartTotalPrice.subtract(deleteOrderItem.getItemPrice().multiply(new BigDecimal(deleteItemCount)));
//        session.setAttribute("cartItemSet", cartItemSet);
//        session.setAttribute("cartItemCount", cartItemCount);
//        session.setAttribute("cartTotalPrice", cartTotalPrice);
    }

    private void clear(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        session.removeAttribute("cartItemSet");
        session.removeAttribute("cartItemCount");
        session.removeAttribute("cartTotalPrice");
        session.removeAttribute("typeSet");
    }

    private void updateCartItemCount(HttpServletRequest req, HttpServletResponse resp) {
        String itemId = req.getParameter("itemId");
        int newItemCount = Integer.parseInt(req.getParameter("itemCount"));

        HttpSession session = req.getSession();
        Set<OrderItemBean> cartItemSet = (LinkedHashSet<OrderItemBean>) session.getAttribute("cartItemSet");

        for (OrderItemBean orderItemBean : cartItemSet) {
            if (orderItemBean.getItemId() == Integer.parseInt(itemId)) {
                orderItemBean.setItemCount(newItemCount);
                orderItemBean.setTotalPrice(orderItemBean.getItemPrice().multiply(new BigDecimal(orderItemBean.getItemCount())));
                break;
            }
        }
        session.setAttribute("cartItemSet", cartItemSet);
        this.refreshCart(session);
//        Integer cartItemCount = (Integer) session.getAttribute("cartItemCount");
//        BigDecimal cartTotalPrice = (BigDecimal) session.getAttribute("cartTotalPrice");
//        //修改商品数量
//        cartItemSet.put(itemBean, newItemCount);
//        int oldItemCount = cartItemSet.get(itemBean);
//        //计算商品数量修改前后的差值
//        int diffCount = newItemCount - oldItemCount;
//        //修改商品总个数
//        cartItemCount += diffCount;
//        BigDecimal itemPrice = itemBean.getItemPrice();
//        //修改商品总金额
//        cartTotalPrice = cartTotalPrice.add(itemPrice.multiply(new BigDecimal(diffCount)));
//
//        session.setAttribute("cartItemCount", cartItemCount);
//        session.setAttribute("cartTotalPrice", cartTotalPrice);
    }

    private void refreshCart(HttpSession session) {
        Set<OrderItemBean> cartItemSet = (LinkedHashSet<OrderItemBean>) session.getAttribute("cartItemSet");
        if (cartItemSet == null) {
            session.removeAttribute("cartItemCount");
            session.removeAttribute("typeSet");
            session.removeAttribute("cartTotalPrice");
        } else {
            int cartItemCount = 0;
            BigDecimal cartTotalPrice = new BigDecimal(0);
            Set<Integer> typeSet = new HashSet<>();
            for (OrderItemBean orderItemBean : cartItemSet) {
                cartItemCount += orderItemBean.getItemCount();
                cartTotalPrice = cartTotalPrice.add(orderItemBean.getTotalPrice());
                int itemId = orderItemBean.getItemId();
                try {
                    ItemBean itemBean = itemService.getItem(itemId);
                    typeSet.add(itemBean.getSmallTypeId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            session.setAttribute("cartItemCount", cartItemCount);
            session.setAttribute("typeSet", typeSet);
            session.setAttribute("cartTotalPrice", cartTotalPrice);
        }
    }
}
