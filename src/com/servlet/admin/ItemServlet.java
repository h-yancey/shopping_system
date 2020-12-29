package com.servlet.admin;

import com.bean.ItemBean;
import com.bean.TypeBean;
import com.google.gson.Gson;
import com.service.ItemService;
import com.service.TypeService;
import com.util.GlobalUtil;
import com.util.PageUtil;
import com.util.ResponseInfo;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@WebServlet(urlPatterns = "/servlet/ItemServlet")
public class ItemServlet extends HttpServlet {
    private ItemService itemService = new ItemService();
    private TypeService typeService = new TypeService();

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

    /**
     * @description 处理显示商品列表的请求
     */
    private void list(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //查询参数
        String itemTypeId = req.getParameter("itemType");
        String keyword = req.getParameter("keyword");
        String priceMin = req.getParameter("priceMin");
        String priceMax = req.getParameter("priceMax");

        //封装查询参数
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("itemTypeId", itemTypeId);
        paramMap.put("keyword", keyword);
        paramMap.put("priceMin", priceMin);
        paramMap.put("priceMax", priceMax);
        req.setAttribute("paramMap", paramMap);

        //分页
        PageUtil pageUtil = new PageUtil(req);
        pageUtil.setPageSize(5);
        pageUtil.setRsCount(itemService.getItemCount(paramMap));
        int pageSize = pageUtil.getPageSize();
        int currentPage = pageUtil.getCurrentPage();
        int rsCount = pageUtil.getRsCount();
        int pageCount = pageUtil.getPageCount();
        int beginIndex = (currentPage - 1) * pageSize;

        //将商品列表放到reques作用域
        List<ItemBean> itemList = itemService.getItemList(beginIndex, pageSize, paramMap);
        req.setAttribute("itemList", itemList);

        //将分页工具放到reques作用域
        String pageTool = pageUtil.createPageTool(PageUtil.BbsText);
        req.setAttribute("pageTool", pageTool);

        //将商品类别列表放到reques作用域
        List<TypeBean> typeList = typeService.getTypeList();
        req.setAttribute("typeList", typeList);

        //转发
        String forwardUrl = "/admin/item/item_list.jsp";
        req.getRequestDispatcher(forwardUrl).forward(req, resp);
    }

    /**
     * @description 处理添加商品的请求，跳转到添加商品页面
     */
    private void add(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //商品类别下拉框需要动态获取
        List<TypeBean> typeList = typeService.getTypeList();
        req.setAttribute("typeList", typeList);

        //自动生成商品编号
        int maxItemId = itemService.getMaxItemId();
        req.setAttribute("maxItemId", maxItemId);

        //自动生成添加时间
        String currentDatetime = GlobalUtil.getCurrentDatetime();
        req.setAttribute("currentDatetime", currentDatetime);

        //重定向到添加商品页面
        String forwardUrl = "/admin/item/item_add.jsp";
        req.getRequestDispatcher(forwardUrl).forward(req, resp);
    }

    /**
     * @description 处理保存商品的请求
     */
    private void save(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        FileItemFactory fileItemFactory = new DiskFileItemFactory();
        ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);

        PrintWriter out = resp.getWriter();
        ResponseInfo responseInfo = new ResponseInfo();
        Gson gson = new Gson();

        try {
            List<FileItem> fileItemList = fileUpload.parseRequest(req);
            Map<String, Object> dataMap = new LinkedHashMap<>();

            for (FileItem fileItem : fileItemList) {
                boolean isFormField = fileItem.isFormField();
                if (isFormField) {
                    String fieldName = fileItem.getFieldName();
                    String fieldValue = fileItem.getString("UTF-8");
                    dataMap.put(fieldName, fieldValue);
                } else {
                    String imgName = fileItem.getName();
                    if (!GlobalUtil.isEmpty(imgName)) {

                        imgName = GlobalUtil.getTimeStamp() + "_" + imgName;
                        InputStream inputStream = fileItem.getInputStream();
                        String uploadDir = this.getServletContext().getRealPath("/upload");
                        OutputStream outputStream = new FileOutputStream(uploadDir + "/" + imgName);
                        int result = IOUtils.copy(inputStream, outputStream);

                        //下面一段代码是测试用的，将文件上传到工程目录下（以绝对路径的形式）
                        //这么做是为了让工程重新部署时，原先上传到部署路径下的图片能够保留
                        inputStream = new FileInputStream(uploadDir + "/" + imgName);
                        String path = "E:\\shopping_system\\WebContent\\upload\\" + imgName;
                        OutputStream outputStream1 = new FileOutputStream(path);
                        IOUtils.copy(inputStream, outputStream1);
                        //

                        dataMap.put("imgName", imgName);
                        IOUtils.closeQuietly(inputStream);
                        IOUtils.closeQuietly(outputStream);
                        IOUtils.closeQuietly(outputStream1);
                    } else {
                        dataMap.put("imgName", "");
                    }
                }
            }

            ItemBean itemBean = new ItemBean();
            dataMap.put("addDate", GlobalUtil.parseDateTime((String) dataMap.get("addDate")));
            BeanUtils.populate(itemBean, dataMap);

            int smallTypeId = itemBean.getSmallTypeId();
            TypeBean typeBean = typeService.getType(smallTypeId);
            int parentId = typeBean.getParentId();
            itemBean.setBigTypeId(parentId);

            itemService.saveItem(itemBean);
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

    /**
     * @description 处理删除商品的请求
     */
    private void delete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String itemId = req.getParameter("itemId");
        String imgName = req.getParameter("imgName");

        PrintWriter out = resp.getWriter();
        ResponseInfo responseInfo = new ResponseInfo();
        Gson gson = new Gson();
        try {
            itemService.deleteItem(Integer.parseInt(itemId));

            //删除商品图片
            String uploadDir = this.getServletContext().getRealPath("/upload");
            String imgPath = uploadDir + "/" + imgName;
            File imgFIle = new File(imgPath);
            imgFIle.delete();

            String testPath = "E:\\shopping_system\\WebContent\\upload\\" + imgName;
            imgFIle = new File(testPath);
            imgFIle.delete();

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

    /**
     * @description 处理编辑商品的请求，跳转到编辑商品页面
     */
    private void edit(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        String itemId = req.getParameter("itemId");
        try {
            ItemBean itemBean = itemService.getItem(Integer.parseInt(itemId));
            req.setAttribute("itemBean", itemBean);

            List<TypeBean> typeList = typeService.getTypeList();
            req.setAttribute("typeList", typeList);

            //重定向到添加商品页面
            String forwardUrl = "/admin/item/item_edit.jsp";
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

    /**
     * @description 处理修改商品的请求
     */
    private void update(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FileItemFactory fileItemFactory = new DiskFileItemFactory();
        ServletFileUpload fileUpload = new ServletFileUpload(fileItemFactory);

        PrintWriter out = resp.getWriter();
        ResponseInfo responseInfo = new ResponseInfo();
        Gson gson = new Gson();

        try {
            List<FileItem> fileItemList = fileUpload.parseRequest(req);
            Map<String, Object> dataMap = new LinkedHashMap<>();

            for (FileItem fileItem : fileItemList) {
                boolean isFormField = fileItem.isFormField();
                if (isFormField) {
                    String fieldName = fileItem.getFieldName();
                    String fieldValue = fileItem.getString("UTF-8");
                    dataMap.put(fieldName, fieldValue);
                } else {
                    String imgName = fileItem.getName();
                    if (!GlobalUtil.isEmpty(imgName)) {
                        //图片有修改
                        imgName = GlobalUtil.getTimeStamp() + "_" + imgName;
                        InputStream inputStream = fileItem.getInputStream();
                        String uploadDir = this.getServletContext().getRealPath("/upload");
                        OutputStream outputStream = new FileOutputStream(uploadDir + "/" + imgName);
                        int result = IOUtils.copy(inputStream, outputStream);

                        //test
                        inputStream = new FileInputStream(uploadDir + "/" + imgName);
                        String path = "E:\\shopping_system\\WebContent\\upload\\" + imgName;
                        OutputStream outputStream1 = new FileOutputStream(path);
                        IOUtils.copy(inputStream, outputStream1);
                        IOUtils.closeQuietly(outputStream1);
                        //

                        dataMap.put("imgName", imgName);
                        IOUtils.closeQuietly(inputStream);
                        IOUtils.closeQuietly(outputStream);
                    }
                }
            }

            String imgName = (String) dataMap.get("imgName");
            String oldImgName = (String) dataMap.get("oldImgName");
            if (GlobalUtil.isEmpty(imgName)) {
                //无重传图片，图片名称不需要改变
                dataMap.put("imgName", oldImgName);
            } else {
                //重传图片后，删除旧图片
                String uploadDir = this.getServletContext().getRealPath("/upload");
                String imgPath = uploadDir + "/" + oldImgName;
                File imgFIle = new File(imgPath);
                imgFIle.delete();

                //测试用的，和上述上传图片到工程目录一样，要删掉工程目录下的图片
                String testPath = "E:\\shopping_system\\WebContent\\upload\\" + oldImgName;
                imgFIle = new File(testPath);
                imgFIle.delete();
            }

            ItemBean itemBean = new ItemBean();
            dataMap.put("addDate", GlobalUtil.parseDateTime((String) dataMap.get("addDate")));
            BeanUtils.populate(itemBean, dataMap);

            int smallTypeId = itemBean.getSmallTypeId();
            TypeBean typeBean = typeService.getType(smallTypeId);
            int parentId = typeBean.getParentId();
            itemBean.setBigTypeId(parentId);

            itemService.updateItem(itemBean);
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
