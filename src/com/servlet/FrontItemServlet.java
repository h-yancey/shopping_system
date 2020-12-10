package com.servlet;

import com.bean.ItemBean;
import com.bean.TypeBean;
import com.service.ItemService;
import com.service.TypeService;
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
@WebServlet(urlPatterns = "/servlet/FrontItemServlet")
public class FrontItemServlet extends HttpServlet {
    private ItemService itemService = new ItemService();
    private TypeService typeService = new TypeService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }


}
