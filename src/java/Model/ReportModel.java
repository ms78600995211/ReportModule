/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import Bean.ReportBean;
import Decoder.BASE64Decoder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import static java.lang.Class.forName;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.fileupload.FileItem;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReportModel {

    private Connection connection;
    private String driverClass;
    private String connectionString;
    private String db_username;
    private String db_password;
    private String message;
    private String msgBgColor;
    private final String COLOR_OK = "yellow";
    private final String COLOR_ERROR = "red";

    public void setConnection() {
        try {

            Class.forName(driverClass);
            // connection = DriverManager.getConnection(connectionString+"?useUnicode=true&characterEncoding=UTF-8&character_set_results=utf8", db_username, db_password);
            connection = (Connection) DriverManager.getConnection(connectionString, db_username, db_password);

        } catch (Exception e) {
            System.out.println("CommandModel setConnection() Error: " + e);
        }
    }

    public byte[] generateRecordList(String jrxmlFilePath, List li) {
        byte[] reportInbytes = null;
        try {
            JRBeanCollectionDataSource jrBean = new JRBeanCollectionDataSource(li);
            JasperReport compiledReport = JasperCompileManager.compileReport(jrxmlFilePath);
            reportInbytes = JasperRunManager.runReportToPdf(compiledReport, null, jrBean);
        } catch (Exception e) {

            System.out.println("ReportModel generatReport() JRException: " + e);
        }
        return reportInbytes;
    }

    public void saveFile(String sample_report_file_name, String sample_report_file_bytes) {
        byte[] bytes = sample_report_file_bytes.getBytes();
        try {
            File file = new File("C:\\ssadvt_repository\\Reports\\sample reports\\" + sample_report_file_name);
            byte[] fileAsBytes = new BASE64Decoder().decodeBuffer(sample_report_file_bytes);
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(fileAsBytes);
            outputStream.close();

        } catch (Exception e) {
            System.out.println("ReportModel Report() JRException :" + e);
        }
    }

    public void GenerateJrxmlFile(String sample_report_path, String jrxml_file_name, String report_name, String jrxml, String jasper) {
        try {
            File reportFile = new File(jrxml);
            boolean value = reportFile.createNewFile();

            if (value) {
                System.out.println("New File is created.");
                String program = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                        + "<!DOCTYPE jasperReport PUBLIC \"-//JasperReports//DTD JasperReport//EN\" \"http://jasperreports.sourceforge.net/dtds/jasperreport.dtd\">\n"
                        + "\n"
                        + "<jasperReport name=\"report name\" pageWidth=\"595\" pageHeight=\"842\" columnWidth=\"535\" leftMargin=\"20\" rightMargin=\"20\" topMargin=\"20\" bottomMargin=\"20\">\n"
                        + "    <background>\n"
                        + "        <band/>\n"
                        + "    </background>\n"
                        + "    <title>\n"
                        + "        <band height=\"79\"/>\n"
                        + "    </title>\n"
                        + "    <pageHeader>\n"
                        + "        <band height=\"35\"/>\n"
                        + "    </pageHeader>\n"
                        + "    <columnHeader>\n"
                        + "        <band height=\"61\"/>\n"
                        + "    </columnHeader>\n"
                        + "    <detail>\n"
                        + "        <band height=\"125\"/>\n"
                        + "    </detail>\n"
                        + "    <columnFooter>\n"
                        + "        <band height=\"45\"/>\n"
                        + "    </columnFooter>\n"
                        + "    <pageFooter>\n"
                        + "        <band height=\"54\"/>\n"
                        + "    </pageFooter>\n"
                        + "    <summary>\n"
                        + "        <band height=\"42\"/>\n"
                        + "    </summary>\n"
                        + "</jasperReport>\n"
                        + "";

                // Creates a Writer using FileWriter
                FileWriter output = new FileWriter(jrxml);

                // Writes the program to file
                output.write(program);
                System.out.println("Data is written to the file.");

                // Closes the writer
                output.close();
            } else {
                System.out.println("The file already exists.");
            }

        } catch (Exception e) {
            System.out.println("Model.ReportModel.GenerateJrxmlFile()" + e);
        }
    }

    public String getImageDataFromDatabasePath(String id) {
        String imagePath = "";
        String query = "select sample_report_path from report_table where active='Y' "
                + " and report_table_id='" + id + "' ";

        try {
            ResultSet rset = connection.prepareStatement(query).executeQuery();
            if (rset.next()) {

                imagePath = rset.getString("sample_report_path");
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex);
        }
        return imagePath;
    }
    public int insertRecord(int report_type_id, String report_name, String sample_report_path, String jrxml_file_name, String api_json_file_path, int report_status_id, String text_file_url) {

        int rowsAffected = 0;

        String query = " insert into report_table(report_type_id, report_name, sample_report_path, jrxml_file_name, api_json_file_path, "
                + " no_of_variables ,active, revision_no, created_by, description, remark, report_status_id, text_file_url) "
                + " values(?,?,?,?,?,?,?,?,?,?,?,?,?) ";

        String query1 = " select count(*) as count from report_table where active='Y' and report_name = '" + report_name + "'";
        try {

            PreparedStatement pstmt = (PreparedStatement) connection.prepareStatement(query1);
            ResultSet rs = pstmt.executeQuery();
            int count = 0;
            while (rs.next()) {
                count = rs.getInt("count");
                message = "Report name is already exist";
                msgBgColor = COLOR_ERROR;
            }
            if (count == 0) {

                PreparedStatement pstm = (PreparedStatement) connection.prepareStatement(query);
                pstm.setInt(1, report_type_id);
                pstm.setString(2, report_name);
                pstm.setString(3, sample_report_path);
                pstm.setString(4, jrxml_file_name);
                pstm.setString(5, api_json_file_path);
                pstm.setInt(6, 0);
                pstm.setString(7, "Y");
                pstm.setInt(8, 0);
                pstm.setString(9, "");
                pstm.setString(10, "");
                pstm.setString(11, "");
                pstm.setInt(12, report_status_id);
                pstm.setString(13, text_file_url);

                rowsAffected = pstm.executeUpdate();
            }
        } catch (Exception e) {
            System.out.println("insertRecord ERROR inside ReportModel - " + e);
        }

        return rowsAffected;
    }

    public int insertRecordForJson(int report_table_id, String file_name, String file_path) {

        int rowsAffected = 0;

        String query = " insert into report_copies(report_table_id, file_name, file_path, active, revision_no, description) "
                + " values(?,?,?,?,?,?) ";
        try {

            PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, report_table_id);
            pstmt.setString(2, file_name);
            pstmt.setString(3, file_path);
            pstmt.setString(4, "Y");
            pstmt.setInt(5, 0);
            pstmt.setString(6, "");
            rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    rowsAffected = rs.getInt(1);
                }
            }
        } catch (Exception e) {
            System.out.println("insertRecord ERROR inside ReportModel - " + e);
        }
        return rowsAffected;
    }

    public int insertImageRecord(String image_name, int id, int image_upload_for) {
        int rowsAffected = 0;
        DateFormat dateFormat = new SimpleDateFormat("dd.MMMMM.yyyy/ hh:mm:ss aaa");
        Date date = new Date();
        String current_date = dateFormat.format(date);
        String imageQuery = "INSERT INTO general_image_details (image_name, image_destination_id, date_time, description,key_person_id) "
                + " VALUES(?, ?, ?, ?,?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(imageQuery, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, image_name);
            pstmt.setInt(2, image_upload_for);
            pstmt.setString(3, current_date);
            pstmt.setString(4, "this file is for complaint");
            pstmt.setInt(5, id);
            pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                rowsAffected = rs.getInt(1);
            }
            pstmt.close();
        } catch (Exception e) {
            System.out.println("Error:keypersonModel--insertRecord-- " + e);
        }
        return rowsAffected;
    }

    public int getReportTypeId(String report_type) {
        int report_type_id = 0;
        String query = " select report_type_id, type from report_type where active='Y' "
                + " and type='" + report_type + "' ";
        try {
            System.out.println("Model.ReportModel.getReportTypeId()connection===" + connection);
            ResultSet rset = connection.prepareStatement(query).executeQuery();
            if (rset.next()) {

                report_type_id = rset.getInt("report_type_id");
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex);
        }
        return report_type_id;
    }

    public int getReportStatusId(String report_status_name) {
        int report_status_id = 0;
        String query = "select report_status_id, status from report_status where active='Y' "
                + " and status='" + report_status_name + "' ";
        try {
            ResultSet rset = connection.prepareStatement(query).executeQuery();
            if (rset.next()) {

                report_status_id = rset.getInt("report_status_id");
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex);
        }
        return report_status_id;
    }

    public int getReportTableId(String report_name) {
        int report_table_id = 0;
        String query = "select report_table_id, report_name from report_table where active='Y' "
                + " and report_name='" + report_name + "' ";
        try {
            ResultSet rset = connection.prepareStatement(query).executeQuery();
            if (rset.next()) {

                report_table_id = rset.getInt("report_table_id");
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex);
        }
        return report_table_id;
    }

    public int getNoOfVariables(int report_table_id) {
        int no_of_variables = 0;

        String query = "select no_of_variables from report_table where active = 'Y' and report_table_id = '" + report_table_id + "'";
        try {
            ResultSet rset = connection.prepareStatement(query).executeQuery();
            if (rset.next()) {

                no_of_variables = rset.getInt("no_of_variables");
            }
        } catch (Exception ex) {
            System.out.println("ERROR: " + ex);
        }
        return no_of_variables;
    }

    public void setFileAPI(Map<String, String> map, Iterator itr, Iterator itr2, String report_name) {
        org.codehaus.jettison.json.JSONObject inputJsonObj = new org.codehaus.jettison.json.JSONObject();
        try {
            String file_name = "", file_data = "", byte_arr = "";

            JSONObject obj = new JSONObject();

            String encodedString = "", image_path = "";
            byte[] arr = null;

            WriteImage("1", itr2, file_name);
            while (itr.hasNext()) {
                JSONObject obj2 = new JSONObject();
                FileItem item = (FileItem) itr.next();
                try {
                    if (!item.isFormField()) {
                        String uploadPath = "C:\\ssadvt_repository\\Reports\\text_files\\";
                        file_name = item.getName();
                        image_path = uploadPath + file_name;
                        file_name = item.getName();
                        file_data = item.getString();

                        File file = new File(image_path);
                        InputStream finput = new FileInputStream(file);
                        byte[] imageBytes = new byte[(int) file.length()];
                        finput.read(imageBytes, 0, imageBytes.length);
                        finput.close();
                        byte_arr = Base64.encodeBase64String(imageBytes);

                        obj.put("file_name", file_name);
                        obj.put("file_bytes", byte_arr);
                    }
                } catch (Exception e) {
                    System.out.println("com.apogee.model.UploadDocumentsModel.setDataforAPI()" + e);
                }
            }

            obj.put("report_name", report_name);

            String msg = callFileAPI(obj);
            message = "Record Inserted successfully.";
            msgBgColor = COLOR_OK;

        } catch (Exception e) {
            System.out.println("com.apogee.model.UploadDocumentsModel.setDataforAPI()" + e);
            message = "Record Not Inserted successfully.";
            msgBgColor = COLOR_ERROR;
        }

    }

    public void WriteImage(String image_id, Iterator itr, String file_name) {
        try {
            String uploadPath = "C:\\ssadvt_repository\\Reports\\text_files\\";
            while (itr.hasNext()) {
                FileItem item = (FileItem) itr.next();
                makeDirectory(uploadPath);
                try {
                    if (!item.isFormField()) {
                        if (item.getSize() > 0) {
                            String image = item.getName();
                            String image_path = uploadPath + image;
                            File savedFile = new File(uploadPath + image);
                            item.write(savedFile);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Image upload error: " + e);
                }
            }
            //}
        } catch (Exception ex) {
        }
    }

    public boolean makeDirectory(String dirPathName) {
        boolean result = false;
        File directory = new File(dirPathName);
        if (!directory.exists()) {
            try {
                result = directory.mkdirs();
            } catch (Exception e) {
                System.out.println("Error - " + e);
            }
        }
        return result;
    }

    public String getTextUrl(String report_name) {
        String text_url = "";

        try {
            String query = " select text_file_url from report_table where report_name='" + report_name + "' and active='Y' ";

            PreparedStatement psmt = connection.prepareStatement(query);
            ResultSet rs = psmt.executeQuery();
            while (rs.next()) {
                text_url = rs.getString("text_file_url");
            }
        } catch (SQLException ex) {
            System.out.println("Model.ReportModel.getTextUrl()" + ex);
        }
        return text_url;

    }

    public String callFileAPI(JSONObject obj) throws Exception {
        String result = "";
        String report_name = obj.getString("report_name");
        String text_url = getTextUrl(report_name);

        try {
            String resp = "";
//            String url = "http://localhost:8080/ReportModule/webAPI/report/reportGenerationRequest";
            URL url = new URL(text_url);
//            URL url = new URL("http://localhost:8080/location_module/webAPI/locationService/sendJsonData");
            URLConnection urlc = url.openConnection();
            urlc.setRequestProperty("datatype", "POST");
            urlc.setRequestProperty("Content-Type", "application/json");
            urlc.setDoOutput(true);
            urlc.setAllowUserInteraction(false);

            PrintStream ps = new PrintStream(urlc.getOutputStream());
            ps.print(obj);
            ps.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(urlc.getInputStream()));
            String l = null;
            StringBuffer response = new StringBuffer();
            while ((l = br.readLine()) != null) {
                response.append(l);
            }
            br.close();
            result = response.toString();
        } catch (Exception e) {
            System.out.println("com.apogee.model.UploadDocumentsModel.callApi()" + e);
        }
        return result;
    }

    public List<String> getReportName(String q) {
        List<String> list = new ArrayList<String>();
        q = q.trim();
        int count = 0;
        String report_name = "";
        String query = " SELECT report_name FROM report_table ORDER BY report_name ";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                report_name = rset.getString("report_name");
                if (report_name.toUpperCase().startsWith(q.toUpperCase())) {
                    list.add(report_name);
                    count++;
                }
            }
            if (count == 0) {
                list.add("No such Report exists.");
            }
        } catch (Exception e) {
            System.out.println("Model.StateModel.getReportName()" + e);
        }
        return list;
    }

    public List<String> getReportStatus(String q) {
        List<String> list = new ArrayList<String>();
        q = q.trim();
        int count = 0;
        String report_status_name = "";
        String query = " SELECT status FROM report_status ORDER BY report_status_id ";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                report_status_name = rset.getString("status");
                if (report_status_name.toUpperCase().startsWith(q.toUpperCase())) {
                    list.add(report_status_name);
                    count++;
                }
            }
            if (count == 0) {
                list.add("No such Report exists.");
            }
        } catch (Exception e) {
            System.out.println("Model.ReportModel.getReportStatus()" + e);
        }
        return list;
    }

    public List<String> getStatus(String q, String previous_status) {
        List<String> list = new ArrayList<String>();
        q = q.trim();
        int count = 0;
        String report_status_name = "";
        String query = "";

        if (previous_status.equals("Request Accepted")) {
            query = " SELECT status FROM report_status where status not in ('Request Accepted')  ORDER BY report_status_id ";
        } else if (previous_status.equals("Designed")) {
            query = " SELECT status FROM report_status where status not in ('Request Accepted','Designed')  ORDER BY report_status_id ";

        } else if (previous_status.equals("Request Sent for JSON")) {
            query = " SELECT status FROM report_status where status not in ('Request Accepted','Designed','Request Sent for JSON') "
                    + " ORDER BY report_status_id ";

        } else if (previous_status.equals("Data Received")) {
            query = " SELECT status FROM report_status where status not in ('Request Accepted','Designed','Request Sent for JSON','Data Received') "
                    + " ORDER BY report_status_id ";
        }
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                report_status_name = rset.getString("status");
                if (report_status_name.toUpperCase().startsWith(q.toUpperCase())) {
                    list.add(report_status_name);
                    count++;
                }
            }
            if (count == 0) {
                list.add("No such Report exists.");
            }
        } catch (Exception e) {
            System.out.println("Model.ReportModel.getStatus()" + e);
        }
        return list;
    }

    public List<String> getReportTypeName(String q) {
        List<String> list = new ArrayList<String>();
        q = q.trim();
        String type = "";
        int count = 0;
        String query = " SELECT type FROM report_type ORDER BY type ";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            ResultSet rset = pstmt.executeQuery();
            while (rset.next()) {
                type = rset.getString("type");
                if (type.toUpperCase().startsWith(q.toUpperCase())) {
                    list.add(type);
                    count++;
                }
            }
            if (count == 0) {
                list.add("No such Report exists.");
            }
        } catch (Exception e) {
            System.out.println("Model.ReportModel.getReportType()" + e);
        }
        return list;
    }

    public int deleteRecord(int report_table_id) {
        String query = "DELETE FROM report_table WHERE report_table_id = " + report_table_id;
        int rowsAffected = 0;
        try {
            rowsAffected = connection.prepareStatement(query).executeUpdate();
        } catch (Exception e) {
            System.out.println("reportModel Error: " + e);
        }
        if (rowsAffected > 0) {
            message = "Record deleted successfully.";
            msgBgColor = COLOR_OK;
        } else {
            message = "Cannot delete the record, some error.";
            msgBgColor = COLOR_ERROR;
        }
        return rowsAffected;
    }

    public int getRevisionno(int report_table_id) {
        int revision = 0;
        try {
            String query = " SELECT max(revision_no) as revision_no FROM report_table WHERE report_table_id =" + report_table_id + "  && active='Y' ";

            PreparedStatement pstmt = (PreparedStatement) connection.prepareStatement(query);

            ResultSet rset = pstmt.executeQuery();

            while (rset.next()) {
                revision = rset.getInt("revision_no");

            }
        } catch (Exception e) {
            System.out.println("com.apogee.ticket.TicketModel.getRevisionno()" + e);
        }
        return revision;
    }

    public int updateRecord(ReportBean reportBean) {
        int rowsAffected = 0;
        int updateRowsAffected = 0;
        int report_table_id = reportBean.getReport_table_id();
        int revision = getRevisionno(report_table_id);

        String query1 = " SELECT max(revision_no) as revision_no,r.report_type_id,report_name,sample_report_path,jrxml_file_name,api_json_file_path,"
                + " no_of_variables,text_file_url,description,remark "
                + " FROM report_table r where r.report_table_id = " + reportBean.getReport_table_id() + " && active='Y' ";
        String query2 = " UPDATE report_table SET active=?  WHERE report_table_id=? && revision_no = ? ";
        String query3 = " insert into report_table(report_table_id,report_type_id,report_status_id,report_name,sample_report_path,jrxml_file_name,api_json_file_path, "
                + " no_of_variables,text_file_url,active,revision_no,description,remark) "
                + "VALUES( ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = (PreparedStatement) connection.prepareStatement(query1);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                PreparedStatement pstm = (PreparedStatement) connection.prepareStatement(query2);

                pstm.setString(1, "N");
                pstm.setInt(2, reportBean.getReport_table_id());
                pstm.setInt(3, revision);
                updateRowsAffected = pstm.executeUpdate();
                if (updateRowsAffected >= 1) {
                    int revision_no = rs.getInt("revision_no") + 1;
                    PreparedStatement psmt = (PreparedStatement) connection.prepareStatement(query3);

                    psmt.setInt(1, reportBean.getReport_table_id());
                    psmt.setInt(2, rs.getInt("report_type_id"));
                    psmt.setInt(3, reportBean.getReport_status_id());
                    psmt.setString(4, rs.getString("report_name"));
                    psmt.setString(5, rs.getString("sample_report_path"));
                    psmt.setString(6, rs.getString("jrxml_file_name"));
                    psmt.setString(7, rs.getString("api_json_file_path"));
                    psmt.setInt(8, rs.getInt("no_of_variables"));
                    psmt.setString(9, rs.getString("text_file_url"));
                    psmt.setString(10, "Y");
                    psmt.setInt(11, revision_no);
                    psmt.setString(12, rs.getString("description"));
                    psmt.setString(13, rs.getString("remark"));
                    rowsAffected = psmt.executeUpdate();
                }
            }
            if (rowsAffected > 0) {
                message = "Record updated successfully.";
                msgBgColor = COLOR_OK;
            } else {
                message = "Cannot update the record, some error.";
                msgBgColor = COLOR_ERROR;
            }
        } catch (Exception e) {
            System.out.println("ReportModel updateRecord() Error: " + e);
        }
        return rowsAffected;
    }

    public List<ReportBean> showData(String searchreportStatusName, String searchType, String searchReportName,
            String active) {
        List<ReportBean> list = new ArrayList<ReportBean>();

        // Use DESC or ASC for descending or ascending order respectively of fetched data.
        String query = "select rt.report_table_id, rt.report_name, rs.status, rtt.type from report_table rt,report_status rs,report_type rtt "
                + " where rs.active='Y' and rt.active='Y' and rtt.active='Y' and rs.report_status_id=rt.report_status_id and "
                + " rt.report_type_id=rtt.report_type_id ";

        if (!searchReportName.equals("") && searchReportName != null) {
            query += " and rt.report_name='" + searchReportName + "' ";
        }
        if (!searchreportStatusName.equals("") && searchreportStatusName != null) {
            query += " and rs.status='" + searchreportStatusName + "' ";
        }
        if (!searchType.equals("") && searchType != null) {
            query += " and rtt.type='" + searchType + "' ";
        }

        query += " order by rt.report_table_id desc ";

        try {
            ResultSet rset = connection.prepareStatement(query).executeQuery();
            // pstmt.setString(1, searchDesignationCode);

            while (rset.next()) {
                ReportBean reportBean = new ReportBean();
                reportBean.setReport_table_id(rset.getInt("report_table_id"));
                reportBean.setReport_name(rset.getString("report_name"));
                reportBean.setReport_status_name(rset.getString("status"));
                reportBean.setType(rset.getString("type"));
                list.add(reportBean);
            }
        } catch (Exception e) {
            System.out.println("ReportModel showData  Error: " + e);
        }
        return list;
    }

    public int insertRecordd(int report_type_id, String report_name, int report_status_id) throws SQLException {
        int rowsAffected = 0;

        String query = "insert into report_table(report_type_id,report_name,report_status_id) "
                + "VALUES( ?, ?, ?)";
        try {
            PreparedStatement pstmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            //   pstmt.setInt(1, orgOffice.getOrganisation_id());
            pstmt.setInt(1, report_type_id);
            pstmt.setString(2, report_name);
            pstmt.setInt(3, report_status_id);

            rowsAffected = pstmt.executeUpdate();
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs != null && rs.next()) {
                int key = rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("Error: ReportModel---insertRecord" + e);
        }
        if (rowsAffected > 0) {
            message = "Record saved successfully.";
            msgBgColor = COLOR_OK;

        } else {
            message = "Cannot save the record, some error.";
            msgBgColor = COLOR_ERROR;
        }
        return rowsAffected;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public String getDb_username() {
        return db_username;
    }

    public void setDb_username(String db_username) {
        this.db_username = db_username;
    }

    public String getConnectionString() {
        return connectionString;
    }

    public void setConnectionString(String connectionString) {
        this.connectionString = connectionString;
    }

    public String getDb_password() {
        return db_password;
    }

    public void setDb_password(String db_password) {
        this.db_password = db_password;
    }

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMsgBgColor() {
        return msgBgColor;
    }

    public void setMsgBgColor(String msgBgColor) {
        this.msgBgColor = msgBgColor;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (Exception e) {
            System.out.println(" closeConnection() Error: " + e);
        }
    }

}
