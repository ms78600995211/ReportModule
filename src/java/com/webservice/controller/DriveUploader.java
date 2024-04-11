package com.webservice.controller;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;

import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletContext;

public class DriveUploader {

    ServletContext serveletContext;
    Connection connection = null;

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
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     * @throws IOException
     */
    public static Credential authorize() throws IOException {
        // Load client secrets.

        //   java.io.File file = new java.io.File("E:\\Netebeans Project\\Smart_Meter_survey\\src\\java\\com\\general\\model\\client_secret.json");
        java.io.File file = new java.io.File("C:\\ssadvt_repository\\SmartMeterSurvey\\credentials.json");
        java.io.FileInputStream in = new java.io.FileInputStream(file);
        GoogleClientSecrets clientSecrets
                = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow
                = new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
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

    // {"web":{"client_id":"795825412545-rp4iqfb47bskr73rdhg6fu1an6nk27rf.apps.googleusercontent.com","project_id":"smart-meter-survey","auth_uri":"https://accounts.google.com/o/oauth2/auth","token_uri":"https://oauth2.googleapis.com/token","auth_provider_x509_cert_url":"https://www.googleapis.com/oauth2/v1/certs","client_secret":"BrwRuDTn8lnG7ORaR2etMz1F","redirect_uris":["http://localhost/Callback"]}}s
    /**
     * Build and return an authorized Drive client service.
     *
     * @return an authorized Drive client service
     * @throws IOException
     */
    public static Drive getDriveService() throws IOException {
        //Credential credential = authorize();
        String refreshToken = "1//04SlFKMWjZytYCgYIARAAGAQSNwF-L9IrqhkeNxNRL5auyqb2SZFC4FIEZqSFTcMr0IzPgWDEZ_RuRTE37Og7nX8kPZg6LZghu4A";
        TokenResponse token = refreshAccessToken(refreshToken);
        Credential credential = createCredentialWithRefreshToken(token);
        System.err.println("credential -----" + credential.getAccessToken());
        return new Drive.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();
    }

    public static TokenResponse refreshAccessToken(String refreshToken) throws IOException {
//        String client_id = "765406836926-dmdni26h4jdsotnpjgmgrt3g6rlplgss.apps.googleusercontent.com";
//        String secret_id = "Yl1k_a8bko6lAYLG-P_EhbVe";

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

    public static void main(String[] args) throws IOException {
        // Initialize the Drive service
        Drive service = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, authorize())
                .setApplicationName(APPLICATION_NAME)
                .build();

        // Specify the folder ID dynamically (replace with your dynamic logic)
        String folderId = "1fVK-i5WQ5qTP93sP7LKepYTHuBkfolqd";

        // Create the file metadata
        com.google.api.services.drive.model.File fileMetadata = new com.google.api.services.drive.model.File();
        fileMetadata.setName("My File");
        fileMetadata.setParents(Collections.singletonList(folderId));

        // Set the file content
        java.io.File filePath = new java.io.File("C:\\Users\\Apogee\\Desktop\\BLE Registration Report.txt");
        FileContent mediaContent = new FileContent("text/plain", filePath);
    }
}
