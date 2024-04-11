/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.webservice.controller;

import Decoder.BASE64Decoder;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.util.PDFMergerUtility;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author admin
 */
@Path("/pdfreport")
public class PdfSplitterApi {

    @Context
    ServletContext serveletContext;
    Connection connection = null;
    private String path = "C:\\ssadvt_repository\\Reports\\toMerge";
    private String path1 = "C:\\ssadvt_repository\\Reports\\toSplit";

    @POST
    @Path("/pdfActionRequest")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)

    public String pdfSplitterRequest(JSONObject jsonObject) throws Exception {
        String action_type = "";
        String filepath = "";
        String file_name = "";
        String valuedatatype = "";
        String byte_arr = "";
        byte[] fileAsBytes = null;
        String response = "";
        int pagesPerFile = 0;
        PdfSplitterApi psa = new PdfSplitterApi();
        LinkedHashMap<String, String> mp = new LinkedHashMap<>();

        action_type = jsonObject.get("action_type").toString();
        // Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // Define a format for displaying the date and time (optional)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

        // Format the current date and time as a string (optional)
        String foldername = currentDateTime.format(formatter);

        file_name = jsonObject.get("file_name").toString();

        if (jsonObject.get("byte_arr").getClass().getSimpleName().equalsIgnoreCase("JSONArray")) {

            JSONArray file_bytes_arr = (JSONArray) jsonObject.get("byte_arr");

            for (int i = 0; i < file_bytes_arr.length(); i++) {

                JSONObject jObj = file_bytes_arr.getJSONObject(i);

                Iterator<String> datakeys = jObj.keys();

                while (datakeys.hasNext()) {
                    String datakey1 = datakeys.next();

                    Object datavalue1 = jObj.get(datakey1);
                    mp.put(datakey1, datavalue1.toString());

                }

            }

        } else if (jsonObject.get("byte_arr").getClass().getSimpleName().equalsIgnoreCase("String")) {
            byte_arr = jsonObject.get("byte_arr").toString();
        }
        psa.makedir(path1);
        psa.makedir(path);

        if (action_type.equalsIgnoreCase("split")) {
            path1 = path1 + "\\" + foldername;
            psa.makedir(path1);
            if (jsonObject.get("pagecount").getClass().getSimpleName().equalsIgnoreCase("Integer")) {
                pagesPerFile = (int) jsonObject.get("pagecount");
            } else {

                valuedatatype = "String";

            }
            if (!valuedatatype.equalsIgnoreCase("String")) {

                if (!byte_arr.isEmpty()) {
                    String path2 = path1 + "\\original";
                    psa.makedir(path2);
                    fileAsBytes = new BASE64Decoder().decodeBuffer(byte_arr);
                    filepath = path2 + "\\" + file_name + ".pdf";
                    FileOutputStream outputStream = new FileOutputStream(filepath);
                    outputStream.write(fileAsBytes);
                    outputStream.close();

                    // Customize the number of pages per output PDF
                    try {
                        PDDocument document = PDDocument.load(new File(filepath));
                        int pageCount = document.getNumberOfPages();
                        if (pagesPerFile > pageCount) {

                            response = "pdf does't have enough pages please check.......";

                        } else {
                            path1 = path1 + "\\splitted";
                            psa.makedir(path1);

                            for (int i = 0; i < pageCount; i += pagesPerFile) {
                                PDDocument customPageDocument = new PDDocument();

                                for (int j = i; j < i + pagesPerFile && j < pageCount; j++) {
                                    customPageDocument.addPage((PDPage) document.getDocumentCatalog().getAllPages().get(j));
                                }

                                String outputFileName = path1 + "\\" + file_name + (i / pagesPerFile + 1) + ".pdf";
                                customPageDocument.save(outputFileName);
                                customPageDocument.close();

                            }

                            document.close();

                            String str1 = "your file path  is  ";

                            response = str1.concat(path1); // Appends str2 to str1
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {

                    response = "please attach file is form of Base64 codes...............";
                }
            } else if (valuedatatype.equalsIgnoreCase("String")) {

                response = "only integer datatype of pagecount is allowed ...............";

            }

        } else if (action_type.equalsIgnoreCase("merge")) {

            String path2 = path + "\\" + foldername;
            psa.makedir(path2);

            try {

                // You can work with each file as needed
                PDFMergerUtility merger = new PDFMergerUtility();
                path = path2 + "//orginal";
                psa.makedir(path);
                for (Map.Entry<String, String> entry : mp.entrySet()) {

                    byte_arr = entry.getValue().toString();
                    fileAsBytes = new BASE64Decoder().decodeBuffer(byte_arr);
                    System.out.println(" filename=============================>"+entry.getKey());
                    filepath = path + "/" + entry.getKey();
                    FileOutputStream outputStream = new FileOutputStream(filepath);
                    outputStream.write(fileAsBytes);
                    outputStream.close();

                    // Add PDF files that you want to combine
                    merger.addSource(new File(filepath));

                }

                path = path2 + "//mergedfiles";
                psa.makedir(path);

                OutputStream outputStream = new FileOutputStream(path + "\\" + file_name + ".pdf");

                // Merge the PDFs to the specified output stream
                merger.setDestinationStream(outputStream);
                merger.mergeDocuments();

                // Close the output stream
                outputStream.close();

                String str1 = "your file path  is  ";

                response = str1.concat(path + "\\" + file_name + ".pdf"); // Appends str2 to str1

            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            response = "please choose right action_type";
        }

        return response;

    }

    public void makedir(String filepath) {

        File file = new File(filepath);
        File directory = new File(file.toString());
        if (!directory.exists()) {
            directory.mkdir();
        }

    }

}
