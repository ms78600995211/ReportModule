/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.webservice.Model;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author admin
 */
public class GoogleSpreadSheetModel {

    private Connection connection;

    public int getfolderId(int report_table_id) {
        int id = 0;
        String query = "SELECT folder_id FROM folder f,report_table rt WHERE "
                + " f.report_table_id=rt.report_table_id "
                + " and rt.report_table_id=" + report_table_id + " ";
        try {

            System.out.println("Model.ReportModel.getReportTypeId()connection===" + connection);

            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getInt("folder_id");
            }
        } catch (Exception ex) {
            System.out.println("Error:keypersonModel--insertRecord-- " + ex);
        }
        return id;
    }

    public String getReportName(int report_table_id) {
        String report_name = "";
        String query = " SELECT report_name FROM report_table  WHERE   report_table_id =" + report_table_id + " ";

        try {

            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                report_name = rs.getString("report_name");
            }
        } catch (Exception ex) {
            System.out.println("Error:keypersonModel--getreport_name-- " + ex);
        }
        return report_name;
    }

    public int insertfolder(String drive_folder_id, int report_table_id, int g_id) {

        int rowsAffected = 0;
        String query = "INSERT INTO folder (drive_folder_id, report_table_id,file_details_id) "
                + " VALUES (?,?,?)";
        try {

            PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, drive_folder_id);
            ps.setInt(2, report_table_id);
            ps.setInt(3, g_id);

            rowsAffected = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                rowsAffected = rs.getInt(1);
            }

        } catch (Exception e) {
            System.out.println("insertRecord ERROR insertfolder - " + e);
        }
        return rowsAffected;
    }

    public int insertfile(int folder_id, int general_image_details_id, String file_id) {
        int rowsAffected = 0;
        String query = "INSERT INTO drivefile (folder_id, general_image_details_id,file_id) "
                + " VALUES (?,?,?)";
        try {

            PreparedStatement ps = connection.prepareStatement(query);
            ps.setInt(1, folder_id);
            ps.setInt(2, general_image_details_id);
            ps.setString(3, file_id);

            rowsAffected = ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("insertRecord ERROR inside google_spread_sheet_model - " + e);
        }
        return rowsAffected;
    }

    public String getDrivefolderId(int report_table_id) {
        String id = "";
        String query = "SELECT drive_folder_id FROM folder f,report_table rt WHERE "
                + " f.report_table_id=rt.report_table_id "
                + " and rt.report_table_id=" + report_table_id + " ";

        try {

            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getString("drive_folder_id");
            }
        } catch (Exception ex) {
            System.out.println("Error:keypersonModel--insertRecord-- " + ex);
        }
        return id;
    }

    public String getReportTypeId(String report_type) {
        String id = "";
        String query = " select report_type_id from report_type where type='" + report_type + "' ";
        try {

            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                id = rs.getString("report_type_id");
            }
        } catch (Exception ex) {
            System.out.println("Error:report_type_id---- " + ex);
        }
        return id;
    }

    public int insertGoogleSheetRecord(String report_name, String googleSheetId, URL text_file_url) {

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
            }
            if (count == 0) {

                PreparedStatement pstm = (PreparedStatement) connection.prepareStatement(query);
                pstm.setInt(1, 3);
                pstm.setString(2, report_name);
                pstm.setString(3, "");
                pstm.setString(4, "");
                pstm.setString(5, "");
                pstm.setInt(6, 0);
                pstm.setString(7, "Y");
                pstm.setInt(8, 0);
                pstm.setString(9, "");
                pstm.setString(10, googleSheetId);
                pstm.setString(11, "");
                pstm.setInt(12, 5);
                pstm.setURL(13, text_file_url);
                rowsAffected = pstm.executeUpdate();

            }
        } catch (Exception e) {
            System.out.println("insertRecord ERROR inside ReportModel - " + e);
        }

        return rowsAffected;
    }

    public int ReportExist(String report_name) {
        int report_table_id = 0;
        String query = "select report_table_id from report_table where report_name='" + report_name + "' ";
        try {

            ResultSet rs = connection.prepareStatement(query).executeQuery();
            if (rs.next()) {
                report_table_id = rs.getInt("report_table_id");
            }
        } catch (Exception ex) {
            System.out.println("Error:getfolderIdFromDatabase-- " + ex);
        }
        return report_table_id;
    }

    public String getGoogleSheetId(String report_table_id) {
        String googleSheetId = "";
        String query = "select description from report_table where report_table_id='" + report_table_id + "' ";
        try {

            ResultSet rs = connection.prepareStatement(query).executeQuery();
            if (rs.next()) {
                googleSheetId = rs.getString("description");
            }
        } catch (Exception ex) {
            System.out.println("Error:getfolderIdFromDatabase-- " + ex);
        }
        return googleSheetId;
    }

    public int insertFileRecord(String file_name, int file_upload_for) {

        int rowsAffected = 0;
        DateFormat dateFormat = new SimpleDateFormat("dd.MMMMM.yyyy/ hh:mm:ss aaa");
        Date date = new Date();
        String current_date = dateFormat.format(date);
        String imageQuery = "INSERT INTO file_details (file_name, date_time,description) "
                + " VALUES(?, ?, ?)";
        try {

            PreparedStatement pstmt = connection.prepareStatement(imageQuery, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, file_name);
            pstmt.setString(2, current_date);
            pstmt.setString(3, "");
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

    public String getfolderIdFromDatabase(int g_id) {
        String folder_id = "";
        String query = "select drive_folder_id from folder as f where f.file_details_id='" + g_id + "' ";
        try {

            ResultSet rs = connection.prepareStatement(query).executeQuery();
            if (rs.next()) {
                folder_id = rs.getString("drive_folder_id");
            }
        } catch (Exception ex) {
            System.out.println("Error:getfolderIdFromDatabase-- " + ex);
        }
        return folder_id;
    }

    public List<String> getClientData() {
        List list = new ArrayList();
        String query = "SELECT client_email from client_details c ";

        try {

            PreparedStatement ps = connection.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getString("client_email"));
            }
        } catch (Exception e) {
            System.out.println("Error DownloadZipFilesModel getClientData() : " + e);
        }

        return list;
    }

    public boolean makeDirectory(String dirPathName) {
        boolean result = false;
        File directory = new File(dirPathName);
        if (!directory.exists()) {
            result = directory.mkdir();
        }
        return result;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (Exception e) {
            System.out.println(" closeConnection() Error: " + e);
        }
    }

}
