/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.webservice.controller;

import Decoder.BASE64Decoder;
import Model.ReportModel;
import com.google.gson.JsonObject;
import com.sun.jersey.api.core.ClasspathResourceConfig;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONObject;
import org.codehaus.jettison.json.JSONArray;

/**
 *
 * @author lENOVO
 */
@Path("/report")
public class ReportGenerationRequest {

    @Context
    ServletContext serveletContext;
    Connection connection = null;

    @POST
    @Path("/reportGenerationRequest")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)

    public String reportGenerationRequest(JSONObject inputJsonObj) throws Exception {

        ReportModel reportModel = new ReportModel();
        reportModel.setDriverClass(serveletContext.getInitParameter("driverClass"));
        reportModel.setConnectionString(serveletContext.getInitParameter("connectionString"));
        reportModel.setDb_username(serveletContext.getInitParameter("db_username"));
        reportModel.setDb_password(serveletContext.getInitParameter("db_password"));
        reportModel.setConnection();
        String resp = "Request Rejected";

        String report_type = inputJsonObj.getString("report_type");
        int report_type_id = reportModel.getReportTypeId(report_type);

        String report_name = inputJsonObj.get("report_name").toString();
        String sample_report_file_name = inputJsonObj.get("sample_report_file_name").toString();
        String sample_report_file_bytes = inputJsonObj.get("sample_report_file_bytes").toString();
        String text_file_url = inputJsonObj.getString("text_file_url").toString();
        String report_status_name = "Request Accepted";
        int report_status_id = reportModel.getReportStatusId(report_status_name);

        reportModel.saveFile(sample_report_file_name, sample_report_file_bytes);

        String api_json_file_path = "C:\\ssadvt_repository\\Reports\\text_files\\";

        //  String text_file_url = "http://localhost:8080/location_module/webAPI/locationService/sendJsonData";
        String sample_report_path = "C:\\ssadvt_repository\\Reports\\sample reports\\";
        String jrxml_file_name = report_name + ".jrxml";
        String currentDirectoryPath = serveletContext.getRealPath("");

//        String new_path = currentDirectoryPath.substring(0, currentDirectoryPath.length() - 10);
//
//        String jrxml = new_path + "web\\reports\\sampleReports\\" + report_name + ".jrxml";
//        String jasper = new_path + "web\\reports\\sampleReports\\" + report_name + ".jasper";


            String jrxml = serveletContext.getRealPath("reports\\sampleReports\\" + report_name + ".jrxml");
            String jasper = serveletContext.getRealPath("reports\\sampleReports\\" + report_name + ".jasper");

        reportModel.GenerateJrxmlFile(sample_report_path, jrxml_file_name, report_name, jrxml, jasper);
        int savedata = 0;
        savedata = reportModel.insertRecord(report_type_id, report_name, sample_report_path, jrxml_file_name, api_json_file_path, report_status_id, text_file_url);
        //   message = "Request Accepted";
        if (savedata > 0) {
            resp = "Request Accepted";
        }
        return resp;
    }

    public boolean makeDirectory(String dirPathName) {
        boolean result = false;
        File directory = new File(dirPathName);
        if (!directory.exists()) {
            result = directory.mkdir();
        }
        return result;
    }

    @POST
    @Path("/getAllDataForGeneratingReport")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)

    //  http://localhost:8080/ReportModule/webAPI/report/getAllDataForGeneratingReport
    public JSONObject getAllDataForGeneratingReport(JSONObject inputJsonObj) throws Exception {

        ReportModel reportModel = new ReportModel();
        reportModel.setDriverClass(serveletContext.getInitParameter("driverClass"));
        reportModel.setConnectionString(serveletContext.getInitParameter("connectionString"));
        reportModel.setDb_username(serveletContext.getInitParameter("db_username"));
        reportModel.setDb_password(serveletContext.getInitParameter("db_password"));
        reportModel.setConnection();
        JSONObject obj = new JSONObject();
        String byte_arr = "";
        try {
            JSONArray jsonAray = inputJsonObj.getJSONArray("data");
            String rawJsonData = jsonAray.toString();

            String report_name = inputJsonObj.getString("report_name");
            int report_table_id = reportModel.getReportTableId(report_name);
            DateFormat dateFormat = new SimpleDateFormat("ddMMMMyyhhmmss");
            Date date = new Date();

            String current_date_time = dateFormat.format(date);

//            String currentDirectoryPath = serveletContext.getRealPath("");
//            String new_path = currentDirectoryPath.substring(0, currentDirectoryPath.length() - 10);
//            String logopath=new_path+"web\\reports\\img\\logo.png";
//            String jrxml = new_path + "web\\reports\\sampleReports\\" + report_name + ".jrxml";
//            String jasper = new_path + "web\\reports\\sampleReports\\" + report_name + ".jasper";


            String jrxml = serveletContext.getRealPath("reports\\sampleReports\\" + report_name + ".jrxml");
            String jasper = serveletContext.getRealPath("reports\\sampleReports\\" + report_name + ".jasper");
            String logopath = serveletContext.getRealPath("reports\\img\\logo.png");
            JasperCompileManager.compileReportToFile(jrxml, jasper);
            JasperReport report = (JasperReport) JRLoader.loadObject(new File(jasper));
            ByteArrayInputStream jsonDataStream = new ByteArrayInputStream(rawJsonData.getBytes());
            JsonDataSource ds = new JsonDataSource(jsonDataStream);
            Map parameters = new HashMap();
            parameters.put("report_name", report_name);
            parameters.put("logopath", logopath);


            JasperPrint jasperPrint = JasperFillManager.fillReport(report, parameters, ds);

            // String outputFile = serveletContext.getRealPath("/theFolder");
            //  JasperExportManager.exportReportToPdfFile(jasperPrint, outputFile + report_name + "_" + current_date_time + ".pdf");
            String path = "C:\\ssadvt_repository\\Reports\\All reports\\" + report_name + "\\";
            reportModel.makeDirectory(path);

            JasperExportManager.exportReportToPdfFile(jasperPrint, path + report_name + "_" + current_date_time + ".pdf");
            String file_name = report_name + "_" + current_date_time + ".pdf";
            String file_path = path;

            int savedata = 0;
            savedata = reportModel.insertRecordForJson(report_table_id, file_name, file_path);

            if (savedata > 0) {
                File file = new File(path + file_name);
                InputStream finput = new FileInputStream(file);
                byte[] imageBytes = new byte[(int) file.length()];
                finput.read(imageBytes, 0, imageBytes.length);
                finput.close();
                byte_arr = Base64.encodeBase64String(imageBytes);
            }
            obj.put("byte_arr", byte_arr);
            obj.put("file_name", file_name);

        } catch (JRException ex) {
            System.out.println("com.webservice.controller.ReportGenerationRequest.getAllDataForGeneratingReport()" + ex);
            ex.printStackTrace();
        }
        return obj;
    }
}
