package com.webservice.controller;

import Bean.ReportBean;
import Model.ReportModel;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@MultipartConfig
public class UploadServlet extends HttpServlet {

    private File tmpDir;

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ServletContext serveletContext = getServletContext();
        Map<String, String> map = new HashMap<String, String>();

        response.setContentType("text/html;charset=UTF-8");
        ReportModel reportModel = new ReportModel();
        reportModel.setDriverClass(serveletContext.getInitParameter("driverClass"));
        reportModel.setConnectionString(serveletContext.getInitParameter("connectionString"));
        reportModel.setDb_username(serveletContext.getInitParameter("db_username"));
        reportModel.setDb_password(serveletContext.getInitParameter("db_password"));
        reportModel.setConnection();
        String message = null;
        String bgColor = null;
        String active = "Y";
        String ac = "ACTIVE RECORDS";

        List items = null;
        Iterator itr = null;
        Iterator itr2 = null;
        DiskFileItemFactory fileItemFactory = new DiskFileItemFactory(); //Set the size threshold, above which content will be stored on disk.
        fileItemFactory.setSizeThreshold(1 * 1024 * 1024); //1 MB Set the temporary directory to store the uploaded files of size above threshold.
        fileItemFactory.setRepository(tmpDir);
        ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);

        try {
            items = uploadHandler.parseRequest(request);
            itr = items.iterator();
            while (itr.hasNext()) {
                FileItem item = (FileItem) itr.next();
                if (item.isFormField()) {
                    map.put(item.getFieldName(), item.getString("UTF-8"));
                } else {
                    if (item.getName() == null || item.getName().isEmpty()) {
                        map.put(item.getFieldName(), "");
                    } else {
                        String image_name = item.getName();
                        image_name = image_name.substring(0, image_name.length());
                        map.put(item.getFieldName(), item.getName());
                    }
                }
            }
            itr = null;
            itr = items.iterator();

            itr2 = items.iterator();

        } catch (Exception ex) {
            System.out.println("Error encountered while uploading file" + ex);
        }
        String task1 = map.get("task1");
        if (task1 == null) {
            task1 = "";
        }
        if (!map.isEmpty()) {
            if (task1.equals("Send Text File")) {
                String report_name = map.get("report_name");
                if (report_name == null) {
                    report_name = "";
                }
                reportModel.setFileAPI(map, itr, itr2, report_name);

            }
        }

        String task = request.getParameter("task");
        String report_name = null;

        if (task == null) {
            task = "";
        }

        try {
            String JQstring = request.getParameter("action1");
            String q = request.getParameter("str");   // field own input

            if (q == null) {
                q = "";
            }
            if (JQstring != null) {
                PrintWriter out = response.getWriter();
                List<String> list = null;
                if (JQstring.equals("getSearchReportName")) {
                    list = reportModel.getReportName(q);
                }
                if (JQstring.equals("getSearchType")) {
                    list = reportModel.getReportTypeName(q);
                }
                if (JQstring.equals("getReportStatus")) {
                    list = reportModel.getReportStatus(q);
                }
                if (JQstring.equals("getStatus")) {
                    String previous_status = request.getParameter("previous_status");
                    if (previous_status == null) {
                        previous_status = "";
                    }
                    list = reportModel.getStatus(q, previous_status);
                }
                JSONObject gson = new JSONObject();
                gson.put("list", list);
                out.println(gson);

                reportModel.closeConnection();
                return;
            }

            String active1 = request.getParameter("active");
            String searchReportStatusName = request.getParameter("searchReportStatusName");
            String searchType = request.getParameter("searchType");
            String searchReportName = request.getParameter("searchReportName");

            try {

                if (searchReportStatusName == null) {
                    searchReportStatusName = "";
                }
                if (searchType == null) {
                    searchType = "";
                }
                if (searchReportName == null) {
                    searchReportName = "";
                }
            } catch (Exception e) {
                System.out.println("Throwing Nullpointer Exception!!!");
            }

            try {
                report_name = request.getParameter("report_name").trim();
            } catch (Exception e) {
                report_name = "";
            }
            if (task.equals("Delete")) {
                reportModel.deleteRecord(Integer.parseInt(request.getParameter("report_table_id")));  // Pretty sure that state_id will be available.
            } else if (task.equals("Save") || task.equals("Save AS New")) {
                int report_table_id;
                try {
                    report_table_id = Integer.parseInt(request.getParameter("report_table_id"));            // state_id may or may NOT be available i.e. it can be update or new record.
                } catch (Exception e) {
                    report_table_id = 0;
                }

                ReportBean reportBean = new ReportBean();

                reportBean.setReport_table_id(report_table_id);
                reportBean.setReport_status_id(reportModel.getReportStatusId(request.getParameter("report_status").trim()));

                int rowsaffected = reportModel.updateRecord(reportBean);
                org.json.simple.JSONObject gson = new org.json.simple.JSONObject();
                PrintWriter out = response.getWriter();
                if (rowsaffected > 0) {
                    gson.put("message", "Status Updated");
                } else {
                    gson.put("message", "Status Not Updated");
                }
                out.println(gson);
                return;
            }

            List<ReportBean> reportList = reportModel.showData(searchReportStatusName, searchType,
                    searchReportName, active);

            request.setAttribute("searchReportStatusName", searchReportStatusName);
            request.setAttribute("searchType", searchType);
            request.setAttribute("searchReportName", searchReportName);
            request.setAttribute("message", message);
            request.setAttribute("msgBgColor", bgColor);
            request.setAttribute("reportList", reportList);
            reportModel.closeConnection();
            request.getRequestDispatcher("report").forward(request, response);
        } catch (Exception ex) {
            System.out.println("UploadServlet error: " + ex);
        }
    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
