package com.servlet.admin;

import com.bean.ItemBean;
import com.bean.TypeBean;
import com.google.gson.Gson;
import com.service.ItemService;
import com.service.TypeService;
import com.util.PageUtil;
import com.util.ResponseInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

/**
 *
 */
@WebServlet(urlPatterns = "/servlet/TypeServlet")
public class TypeServlet extends HttpServlet {
    private TypeService typeService = new TypeService();
    private ItemService itemService = new ItemService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String task = req.getParameter("task");
        if ("list".equals(task)) {
            list(req, resp);
        } else if ("add".equals(task)) {
            add(req, resp);
        } else if ("save".equals(task)) {
            save(req, resp);
        } else if ("delete".equals(task)) {
            delete(req, resp);
        } else if ("edit".equals(task)) {
            edit(req, resp);
        } else if ("update".equals(task)) {
            update(req, resp);
        }

    }

    private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<TypeBean> typeList = typeService.getTypeList();
        req.setAttribute("typeList", typeList);

        String forwardUrl = "/admin/type_list.jsp";
        req.getRequestDispatcher(forwardUrl).forward(req, resp);
    }

    private void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<TypeBean> parentTypeList = typeService.getParentTypeList();
        req.setAttribute("parentTypeList", parentTypeList);

        int maxTypeId = typeService.getMaxTypeId();
        req.setAttribute("maxTypeId", maxTypeId);

        String forwardUrl = "/admin/type_add.jsp";
        req.getRequestDispatcher(forwardUrl).forward(req, resp);
    }

    private void save(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取表单数据
        String typeId = req.getParameter("typeId");
        String typeName = req.getParameter("typeName");
        String parentId = req.getParameter("parentId");

        TypeBean typeBean = new TypeBean();
        typeBean.setTypeId(Integer.parseInt(typeId));
        typeBean.setTypeName(typeName);
        typeBean.setParentId(Integer.parseInt(parentId));

        PrintWriter out = resp.getWriter();
        ResponseInfo responseInfo = new ResponseInfo();
        Gson gson = new Gson();
        try {
            typeService.saveType(typeBean);
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

    private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String typeId = req.getParameter("typeId");
        String parentId = req.getParameter("parentId");

        PrintWriter out = resp.getWriter();
        ResponseInfo responseInfo = new ResponseInfo();
        Gson gson = new Gson();
        try {
            typeService.deleteType(Integer.parseInt(typeId), Integer.parseInt(parentId));
            //删除该类别所有的商品
            itemService.deleteItemsByTypeId(Integer.parseInt(typeId));
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

    private void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String typeId = req.getParameter("typeId");
        try {
            TypeBean typeBean = typeService.getTypeById(Integer.parseInt(typeId));
            req.setAttribute("typeBean", typeBean);

            List<TypeBean> parentTypeList = typeService.getParentTypeList();
            req.setAttribute("parentTypeList", parentTypeList);

            String forwardUrl = "/admin/type_edit.jsp";
            req.getRequestDispatcher(forwardUrl).forward(req, resp);
        } catch (Exception e) {
            e.printStackTrace();
            resp.setContentType("text/html");
            String redirectUrl = req.getContextPath() + "/servlet/TypeServlet?task=list";
            out.print("<script>");
            out.print("alert('" + e.getMessage() + "');");
            out.print("window.location.href='" + redirectUrl + "'");
            out.print("</script>");
        } finally {
            out.flush();
            out.close();
        }
    }

    private void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //获取表单数据
        String typeId = req.getParameter("typeId");
        String typeName = req.getParameter("typeName");
        String parentId = req.getParameter("parentId");

        TypeBean typeBean = new TypeBean();
        typeBean.setTypeId(Integer.parseInt(typeId));
        typeBean.setTypeName(typeName);
        typeBean.setParentId(Integer.parseInt(parentId));

        PrintWriter out = resp.getWriter();
        ResponseInfo responseInfo = new ResponseInfo();
        Gson gson = new Gson();
        try {
            typeService.updateType(Integer.parseInt(typeId), typeBean);
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
}
