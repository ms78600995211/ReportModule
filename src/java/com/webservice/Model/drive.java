/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.webservice.Model;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;

import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import java.sql.Connection;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletContext;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 *
 * @author Navitus1
 */
public class drive {

    ServletContext serveletContext;
    private Connection connection = null;

    /**
     * Application name.
     */
    private static final String APPLICATION_NAME
            = "Drive API Java Quickstart";
    /**
     * Directory to store user credentials for this application.
     */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/drive-java-quickstart");
    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    private static FileDataStoreFactory DATA_STORE_FACTORY;
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY
            = JacksonFactory.getDefaultInstance();
    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;
    /**
     * Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials at
     * ~/.credentials/drive-java-quickstart
     */
    private static final List<String> SCOPES
            = Arrays.asList(DriveScopes.DRIVE_FILE);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Build and return an authorized Drive client service.
     *
     * @return an authorized Drive client service
     * @throws IOException
     */
    public static Credential createCredentialWithRefreshToken(TokenResponse tokenResponse) {
        return new Credential.Builder(BearerToken.authorizationHeaderAccessMethod()).setTransport(
                new NetHttpTransport())
                .setJsonFactory(new JacksonFactory())
                .setTokenServerUrl(
                        new GenericUrl("https://oauth2.googleapis.com/token"))
                .setClientAuthentication(new BasicAuthentication("DQUPJyx2wT-hdPnhfsM4DExf", "795825412545-u8f2isupi4mbhb219hdfe6fn7fbobqsv.apps.googleusercontent.com"))
                .build()
                .setFromTokenResponse(tokenResponse);
    }

    public static TokenResponse refreshAccessToken(String refreshToken) throws IOException {

        String client_id = "795825412545-u8f2isupi4mbhb219hdfe6fn7fbobqsv.apps.googleusercontent.com";
        String secret_id = "DQUPJyx2wT-hdPnhfsM4DExf";

        TokenResponse response = new GoogleRefreshTokenRequest(
                new NetHttpTransport(),
                new JacksonFactory(),
                refreshToken,
                client_id,
                secret_id)
                .execute();
        System.out.println("Access token: " + response.getAccessToken());

        return response;
    }

    public static Drive getDriveService() throws IOException {
        //Credential credential = authorize();
        String refreshToken = "1//04SlFKMWjZytYCgYIARAAGAQSNwF-L9IrqhkeNxNRL5auyqb2SZFC4FIEZqSFTcMr0IzPgWDEZ_RuRTE37Og7nX8kPZg6LZghu4A";
        TokenResponse token = refreshAccessToken(refreshToken);
        Credential credential = createCredentialWithRefreshToken(token);
        System.err.println("credential -----" + credential.getAccessToken());
        return new Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
    }


    public String drive(String project_name, String filename, String file_type, String path, int report_table_id, int g_id) throws IOException, SQLException, JSONException {

        Drive service = getDriveService();
        GoogleSpreadSheetModel sm = new GoogleSpreadSheetModel();
        sm.setConnection(connection);
        String folder_id = "1FeRqYUU5HkKbHSxJz5v9LA6dh7MrPlCz";
        int sub_folder_id = sm.getfolderId(report_table_id);
        String sub_folder_drive_id = "";
        String file_drive_id = "";
        String report_name = sm.getReportName(report_table_id);
        File fileMetadata = new File();
        JSONObject jsonObj = new JSONObject();
        File file_drive = new File();
        if ((sub_folder_id == 0)) {
            fileMetadata.setParents(Collections.singletonList(folder_id));
            fileMetadata.setName(report_name);
            fileMetadata.setMimeType("application/vnd.google-apps.folder");
            File folder = service.files().create(fileMetadata).setFields("id").execute();
            sub_folder_drive_id = folder.getId();
            sm.insertfolder(sub_folder_drive_id, report_table_id, g_id);
        } else {
            System.out.println("folder exists already................");
        }
        folder_id = sm.getDrivefolderId(report_table_id);
        String folderURL = "https://drive.google.com/folder/d/" + folder_id + "/view";
        jsonObj.put("Parent_folder_Name", report_name);
        jsonObj.put("Parent_folder_URL", folderURL);

        if (!project_name.equalsIgnoreCase("SurveyModule")) {

            try {
                File file_folder = new File();
                file_folder.setParents(Collections.singletonList(folder_id));
                file_folder.setName(Integer.toString(g_id));
                file_folder.setMimeType("application/vnd.google-apps.folder");
                file_drive = service.files().create(file_folder).setFields("id").execute();
                folder_id = file_drive.getId();
                jsonObj.put("folder_name", g_id);
                String file_link = "https://drive.google.com/folder/d/" + file_drive.getId() + "/view";
                jsonObj.put("folder_url", file_link);
                sm.insertfolder(folder_id, report_table_id, g_id);
            } catch (IOException e) {
                System.out.println(e);
            }
        }

        if (file_type.equalsIgnoreCase("image")) {
            fileMetadata.setName(filename);
            fileMetadata.setMimeType("image/jpeg");
            fileMetadata.setParents(Collections.singletonList(folder_id));
            java.io.File filePath = new java.io.File(path);
            FileContent mediaContent = new FileContent("image/jpeg", filePath);
            File uploadedFile = service.files().create(fileMetadata, mediaContent).setFields("id").execute();
            jsonObj.put("filename", filename);
            String file_link = "https://drive.google.com/file/d/" + uploadedFile.getId() + "/view";
            jsonObj.put("file_link", file_link);
        }
        if (file_type.equalsIgnoreCase("video")) {
            fileMetadata.setName(filename);
            fileMetadata.setMimeType("video/*");
            fileMetadata.setParents(Collections.singletonList(folder_id));
            java.io.File filePath = new java.io.File(path);
            FileContent mediaContent = new FileContent("video/*", filePath);
            File uploadedFile = service.files().create(fileMetadata, mediaContent).setFields("id").execute();
            jsonObj.put("filename", filename);

            String file_link = "https://drive.google.com/file/d/" + uploadedFile.getId() + "/view";
            jsonObj.put("file_link", file_link);

        }
        if (file_type.equalsIgnoreCase("audio")) {
            fileMetadata.setName(filename);
            fileMetadata.setMimeType("audio/mpeg");
            fileMetadata.setParents(Collections.singletonList(folder_id));
            java.io.File filePath = new java.io.File(path);
            FileContent mediaContent = new FileContent("audio/mpeg", filePath);
            File uploadedFile = service.files().create(fileMetadata, mediaContent).setFields("id").execute();
            jsonObj.put("filename", filename);
            String file_link = "https://drive.google.com/file/d/" + uploadedFile.getId() + "/view";
            jsonObj.put("file_link", file_link);
        }
        if (file_type.equalsIgnoreCase("pdf")) {
            fileMetadata.setName(filename);
            fileMetadata.setMimeType("application/pdf");
            fileMetadata.setParents(Collections.singletonList(folder_id));
            java.io.File filePath = new java.io.File(path);
            FileContent mediaContent = new FileContent("application/pdf", filePath);
            File uploadedFile = service.files().create(fileMetadata, mediaContent).setFields("id").execute();
            jsonObj.put("filename", filename);
            String file_link = "https://drive.google.com/file/d/" + uploadedFile.getId() + "/view";
            jsonObj.put("file_link", file_link);
        }

//    // Upload the Excel file
//    fileMetadata.setName(filename);
//    fileMetadata.setMimeType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); // MIME type for Excel files
//    fileMetadata.setParents(Collections.singletonList(file_drive_id));
//    java.io.File filePath = new java.io.File(path);
//    FileContent mediaContent = new FileContent("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", filePath);
//    service.files().create(fileMetadata, mediaContent).setFields("id").execute();
        if (file_type.equalsIgnoreCase("xlsx") || file_type.equalsIgnoreCase("xls") || file_type.equalsIgnoreCase("EXCEL")) {
            fileMetadata.setName(filename);
            fileMetadata.setMimeType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"); // MIME type for Excel files
            fileMetadata.setParents(Collections.singletonList(folder_id));
            java.io.File filePath = new java.io.File(path);
            FileContent mediaContent = new FileContent("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", filePath);
            File uploadedFile = service.files().create(fileMetadata, mediaContent).setFields("id").execute();
            jsonObj.put("filename", filename);
            String file_link = "https://drive.google.com/file/d/" + uploadedFile.getId() + "/view";
            jsonObj.put("file_link", file_link);
        }
        return jsonObj.toString();
    }

    /**
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * @param connection the connection to set
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }

}
