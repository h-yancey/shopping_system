package com.servlet;

import com.bean.ItemBean;
import com.bean.TypeBean;
import com.service.ItemService;
import com.service.TypeService;
import com.util.GlobalUtil;
import com.util.PageUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
@WebServlet(urlPatterns = "/index")
public class IndexServlet extends HttpServlet {
    private ItemService itemService = new ItemService();
    private TypeService typeService = new TypeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String task = req.getParameter("task");
        if (GlobalUtil.isEmpty(task)) {
            list(req, resp);
        } else if ("info".equals(task)) {
            info(req, resp);
        }

    }

    private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //查询参数
        String itemTypeId = req.getParameter("itemType");
        String keyword = req.getParameter("keyword");
        String priceMin = req.getParameter("priceMin");
        String priceMax = req.getParameter("priceMax");

        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("itemTypeId", itemTypeId);
        paramMap.put("keyword", keyword);
        paramMap.put("priceMin", priceMin);
        paramMap.put("priceMax", priceMax);
        req.setAttribute("paramMap", paramMap);

        //分页
        PageUtil pageUtil = new PageUtil(req);
        pageUtil.setPageSize(10);
        pageUtil.setRsCount(itemService.getItemCount(paramMap));

        int pageSize = pageUtil.getPageSize();
        int currentPage = pageUtil.getCurrentPage();
        int rsCount = pageUtil.getRsCount();
        int pageCount = pageUtil.getPageCount();

        int beginIndex = (currentPage - 1) * pageSize;
        List<ItemBean> itemList = itemService.getItemList(beginIndex, pageSize, paramMap);
        req.setAttribute("itemList", itemList);

        String pageTool = pageUtil.createPageTool(PageUtil.BbsText);
        req.setAttribute("pageTool", pageTool);

        List<TypeBean> typeList = typeService.getTypeList();
        req.setAttribute("typeList", typeList);

        String forwardUrl = "/front/front_index.jsp";
        req.getRequestDispatcher(forwardUrl).forward(req, resp);
    }

    private void info(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        String itemId = req.getParameter("itemId");
        try {
            ItemBean itemBean = itemService.getItem(Integer.parseInt(itemId));
            TypeBean bigTypeBean = typeService.getTypeById(itemBean.getBigTypeId());
            TypeBean smallTypeBean = typeService.getTypeById(itemBean.getSmallTypeId());
            itemBean.setBigTypeName(bigTypeBean.getTypeName());
            itemBean.setSmallTypeName(smallTypeBean.getTypeName());

            req.setAttribute("itemBean", itemBean);

            List<TypeBean> typeList = typeService.getTypeList();
            req.setAttribute("typeList", typeList);

            String forwardUrl = "/front/item_info.jsp";
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
}
