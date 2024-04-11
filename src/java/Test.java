
import com.lowagie.text.pdf.codec.Base64;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLConnection;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.codehaus.jettison.json.JSONArray;
import org.json.simple.JSONObject;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author admin
 */
public class Test {

    public static void main(String[] args) {
        int i = callApi("C:\\ssadvt_repository\\Reports\\splitedfiles");
    }

    public static int callApi(String folderPath) {
        File folder = new File(folderPath);
        JSONArray arr = new JSONArray();

        try {

            // Check if the folder exists and is indeed a directory
            if (folder.exists() && folder.isDirectory()) {
                // List all files in the folder
                File[] files = folder.listFiles();

                if (files != null) {
                    System.out.println("Files in the folder:");
                    for (File file : files) {
                        if (file.isFile()) {
                            String file_path = folderPath + "\\" + file.getName();
                            JSONObject obj1 = new JSONObject();
                            // Load the PDF document
                            PDDocument document = PDDocument.load(file_path);

                            // Create a ByteArrayOutputStream to store the PDF content
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                            // Save the PDF content to the ByteArrayOutputStream
                            document.save(outputStream);
                            document.close();
                            // Convert the ByteArrayOutputStream to a byte array
                            byte[] pdfBytes = outputStream.toByteArray();
                            // Encode the byte array to Base64
                            String base64String = Base64.encodeBytes(pdfBytes);
                            obj1.put(file.getName(), base64String);
                            arr.put(obj1);

                        }
                    }

                } else {
                    System.out.println("No files found in the folder.");
                }
            }

            JSONObject obj = new JSONObject();
            obj.put("action_type", "merge");
            obj.put("file_name", "pdfapi");
            obj.put("byte_arr", arr);

            sendAllData(obj);

        } catch (Exception e) {
        }
        return 0;
    }

    public static String sendAllData(JSONObject obj) {
        String result = "";
        try {
//            URL url = new URL("http://localhost:8088/ReportModule/webAPI/pdfreport/pdfActionRequest");
            URL url = new URL("http://localhost:8088/ReportModule/webAPI/pdfreport/pdfActionRequest");
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
            System.out.println("com.apogee.report.ReportsModel.sendAllData()" + e);
        }
        return result;
    }
}
