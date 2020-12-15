package com.servlet;

import com.bean.ItemBean;
import com.google.gson.Gson;
import com.service.ItemService;
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
import java.math.BigDecimal;
import java.util.*;

/**
 *
 */
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
        }
    }


    private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String forwardUrl = "front/cart.jsp";
        req.getRequestDispatcher(forwardUrl).forward(req, resp);
    }

    private void add(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        HttpSession session = req.getSession();
        Map<ItemBean, Integer> cartMap = (LinkedHashMap<ItemBean, Integer>) session.getAttribute("cartMap");
        Integer cartNum = (Integer) session.getAttribute("cartNum");
        BigDecimal cartMoney = (BigDecimal) session.getAttribute("cartMoney");
        Set<Integer> typeSet = (HashSet<Integer>) session.getAttribute("typeSet");
        if (cartMap == null) {
            cartMap = new LinkedHashMap<ItemBean, Integer>();
        }
        if (cartNum == null) {
            cartNum = 0;
        }
        if (cartMoney == null) {
            cartMoney = new BigDecimal(0);
        }
        if (typeSet == null) {
            typeSet = new HashSet<>();
        }

        String itemId = req.getParameter("itemId");
        try {
            ItemBean addItem = itemService.getItem(Integer.parseInt(itemId));
            if (cartMap.containsKey(addItem)) {
                cartMap.put(addItem, cartMap.get(addItem) + 1);
            } else {
                cartMap.put(addItem, 1);
            }
            cartNum++;
            typeSet.add(addItem.getSmallTypeId());
            cartMoney = cartMoney.add(addItem.getItemPrice());
        } catch (Exception e) {
            e.printStackTrace();
        }
        session.setAttribute("cartMap", cartMap);
        session.setAttribute("cartNum", cartNum);
        session.setAttribute("typeSet", typeSet);
        session.setAttribute("cartMoney", cartMoney);
        PrintWriter out = resp.getWriter();
        Gson gson = new Gson();
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("cartNum", cartNum);
        returnMap.put("typeCount", typeSet.size());
        returnMap.put("cartMoney", GlobalUtil.formatBigDecimal(cartMoney));
        String json = gson.toJson(returnMap);
        out.print(json);
        out.flush();
        out.close();
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp) {
        String itemId = req.getParameter("itemId");
        ItemBean deleteItem = null;
        try {
            deleteItem = itemService.getItem(Integer.parseInt(itemId));
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpSession session = req.getSession();
        Map<ItemBean, Integer> cartMap = (LinkedHashMap<ItemBean, Integer>) session.getAttribute("cartMap");
        Integer cartNum = (Integer) session.getAttribute("cartNum");
        BigDecimal cartMoney = (BigDecimal) session.getAttribute("cartMoney");
        Integer removeNum = cartMap.remove(deleteItem);
        cartNum -= removeNum;
        cartMoney = cartMoney.subtract(deleteItem.getItemPrice().multiply(new BigDecimal(removeNum)));
        session.setAttribute("cartMap", cartMap);
        session.setAttribute("cartNum", cartNum);
        session.setAttribute("cartMoney", cartMoney);
    }

    private void clear(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession session = req.getSession();
        session.removeAttribute("cartMap");
        session.removeAttribute("cartNum");
        session.removeAttribute("cartMoney");
        session.removeAttribute("typeSet");
    }
}
