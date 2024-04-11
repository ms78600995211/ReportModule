/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.webservice.Model;

import Decoder.BASE64Decoder;
import com.google.gson.Gson;
import java.io.FileOutputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author admin
 */
@Path("/googleReport")
public class GoogleSheetAPI {

    @Context
    ServletContext serveletContext;
    Connection connection = null;

    @POST
    @Path("/getAllDataRequiredInGoogleSheet")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)

    //  http://localhost:8088/ReportModule/webAPI/googleReport/getAllDataRequiredInGoogleSheet
    public JSONObject getAllDataRequiredInGoogleSheet(JSONObject jsonObject) throws Exception {

        GoogleSpreadSheetModel model = new GoogleSpreadSheetModel();

        String driverClass = serveletContext.getInitParameter("driverClass");
        String connectionString = serveletContext.getInitParameter("connectionString");
        String db_username = serveletContext.getInitParameter("db_username");
        String db_password = serveletContext.getInitParameter("db_password");
        try {

            Class.forName(driverClass);
            connection = (Connection) DriverManager.getConnection(connectionString, db_username, db_password);

        } catch (Exception e) {
            System.out.println("GoogleSpreadSheetModel setConnection() Error: " + e);
        }
        model.setConnection(connection);
        String report_name = "";
        String sheet_name = "";
        String byte_arr = "";
        String file_name = " ";
        String file_type = " ";

        ArrayList<String> collum_names = new ArrayList<>();
        ArrayList<String> collum_values = new ArrayList<>();

        String project_name = (String) jsonObject.get("project_name");

        java.util.Iterator<String> keys = jsonObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            Object value = jsonObject.get(key);
            if (key.equalsIgnoreCase("report_name")) {
                report_name = value.toString();
            }

            if (key.equalsIgnoreCase("report_data")) {
                JSONArray arr = (JSONArray) value;
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject jsonObject1 = arr.getJSONObject(i);
                    Iterator<String> datakeys = jsonObject1.keys();
                    while (datakeys.hasNext()) {
                        String datakey1 = datakeys.next();
                        Object datavalue1 = jsonObject1.get(datakey1);
                        if (!collum_names.contains(datakey1)) {
                            collum_names.add(datakey1);
                        }
                        collum_values.add(datavalue1.toString());

                    }
                }
            }

            if (key.equalsIgnoreCase("sheet_name")) {
                sheet_name = value.toString();
            }

            if (key.equalsIgnoreCase("file")) {

                JSONArray file_arr = (JSONArray) value;

                for (int i = 0; i < file_arr.length(); i++) {

                    JSONObject jsonObject2 = file_arr.getJSONObject(i);
                    Iterator<String> datakeys = jsonObject2.keys();
                    while (datakeys.hasNext()) {
                        String datakey2 = datakeys.next();
                        Object datavalue2 = jsonObject2.get(datakey2);

                        if (datakey2.equalsIgnoreCase("byte_arr")) {

                            byte_arr = datavalue2.toString();

                        }

                        if (datakey2.equalsIgnoreCase("file_name")) {

                            file_name = datavalue2.toString();

                        }
                        if (datakey2.equalsIgnoreCase("file_type")) {

                            file_type = datavalue2.toString();

                        }
                    }

                }
            }
        }
        byte[] fileAsBytes = null;
        JSONObject json = new JSONObject();
        ArrayList<List> list2 = new ArrayList<>();
        ArrayList<String> list3 = new ArrayList<>();
        ArrayList<String> list4 = new ArrayList<>();
        int id = 0;
        String sheetId = "";
        String isReportExist = "NO";
        String image_link = "";
        URL googleSheetUrl = null;
        String folder_id = "";
        JSONObject obj = new JSONObject();
        String message = "Data updated in existing report and url is===>";

        if (report_name.equals(null) || report_name.equalsIgnoreCase("null") || report_name == null || report_name.equalsIgnoreCase("") || report_name.isEmpty()) {
            message = "please choose report_name.....";
            obj.put("message", message);

        } else {
            int report_table_id = model.ReportExist(report_name);
            if (report_table_id > 0) {
                isReportExist = "YES";
                sheetId = model.getGoogleSheetId(Integer.toString(report_table_id));
                googleSheetUrl = new URL("https://docs.google.com/spreadsheets/d/" + sheetId + "/edit#gid=0");
                obj.put("googleSheetUrl", googleSheetUrl);

            } else {
//                spreadsheet.setReportName(report_name);
                sheetId = spreadsheet.generateNewspreadsheetId(report_name);
                googleSheetUrl = new URL("https://docs.google.com/spreadsheets/d/" + sheetId + "/edit#gid=0");
                obj.put("googleSheetUrl", googleSheetUrl);
                id = model.insertGoogleSheetRecord(report_name, sheetId, googleSheetUrl);
                report_table_id = model.ReportExist(report_name);
            }
            if (!byte_arr.isEmpty()) {
                fileAsBytes = new BASE64Decoder().decodeBuffer(byte_arr);
                String path = "C:\\ssadvt_repository\\Reports\\spreadsheetfile";
                model.makeDirectory(path);
                String filepath = path + "/" + file_name;
                FileOutputStream outputStream = new FileOutputStream(filepath);
                outputStream.write(fileAsBytes);
                outputStream.close();
                int affect = model.insertFileRecord(file_name, report_table_id);
                drive d = new drive();
                System.out.println("get con: " + model.getConnection());
                d.setConnection(model.getConnection());
                String str = d.drive(project_name, file_name, file_type, filepath, report_table_id, affect);
                JSONObject jobj = new JSONObject(str);
                obj.put("file", jobj);
                folder_id = model.getfolderIdFromDatabase(affect);
                image_link = "https://drive.google.com/folder/d/" + folder_id + "/view";
                list3.add(image_link);
            }

            int chunkSize = collum_names.size();
            List<List<String>> result = splitList(collum_values, chunkSize);
            for (int i = 0; i < result.size(); i++) {

                if (i == 0) {
                    list4.addAll(result.get(i));
                    list4.add(image_link);
                    list2.add(list4);

                } else {
                    list2.add(result.get(i));

                }

            }
            if (!project_name.equalsIgnoreCase("SurveyModule")) {
                spreadsheet.uploadGoogleSheetAppendValue(collum_names, list2, sheetId, isReportExist, sheet_name, folder_id);

            }

        }
        return obj;

    }

    public static List<List<String>> splitList(List<String> originalList, int chunkSize) {
        List<List<String>> resultList = new ArrayList<>();
        int index = 0;

        while (index < originalList.size()) {
            int endIndex = Math.min(index + chunkSize, originalList.size());
            resultList.add(originalList.subList(index, endIndex));
            index += chunkSize;
        }
        return resultList;
    }

}
