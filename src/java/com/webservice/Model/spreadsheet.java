/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.webservice.Model;

import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.BasicAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AddSheetRequest;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.CellFormat;
import com.google.api.services.sheets.v4.model.Color;
import com.google.api.services.sheets.v4.model.GridRange;
import com.google.api.services.sheets.v4.model.RepeatCellRequest;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.SheetProperties;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.TextFormat;
import com.google.api.services.sheets.v4.model.ValueRange;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;

public class spreadsheet {

    @Context
    ServletContext serveletContext;
    Connection connection = null;

    /**
     * Application name.
     */
    private static final String APPLICATION_NAME = "Google Sheets API Java Quickstart";

    /**
     * Directory to store user credentials for this application.
     */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/sheets.googleapis.com-java-quickstart");
    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    private static FileDataStoreFactory DATA_STORE_FACTORY;
    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;
    /**
     * Global instance of the scopes required by this spreadsheet.
     */
    private static final List<String> SCOPES = Arrays.asList(SheetsScopes.DRIVE);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
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

    public static Credential authorize() throws IOException {
        // Load client secrets

        java.io.File file = new java.io.File("C:\\ssadvt_repository\\SmartMeterSurvey\\credentials.json");

        InputStream in = new FileInputStream(file);

        GoogleClientSecrets clientSecrets
                = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow
                = new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES).setDataStoreFactory(DATA_STORE_FACTORY).setAccessType("offline").build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        System.out.println("Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return credential;
    }

    /**
     * Build and return an authorized Sheets API client service.
     *
     * @return an authorized Sheets API client service
     * @throws IOException
     */
    //Generate new spreadsheed Id
    public static String generateNewspreadsheetId(String report_name) throws IOException, GeneralSecurityException, NoSuchFieldException, SQLException {

        Sheets sheetsService = createSheetsService();
        // Create a new spreadsheet
        Spreadsheet spreadsheet = sheetsService.spreadsheets().create(new Spreadsheet().setProperties(new SpreadsheetProperties().setTitle(report_name))).execute();
        // Get the spreadsheet ID
        String spreadsheetId1 = spreadsheet.getSpreadsheetId();
        return spreadsheetId1;
    }

//To count filled Row used to get Last row for styling
    private static int countFilledRows(Sheets sheetsService, String SPREADSHEET_ID) throws IOException {
        ValueRange response = sheetsService.spreadsheets().values()
                .get(SPREADSHEET_ID, "A1:Z")
                .execute();

        List<List<Object>> values = response.getValues();
        int filledRowCount = 0;

        if (values != null) {
            for (List<Object> row : values) {
                if (!row.isEmpty() && !row.get(0).toString().isEmpty()) {
                    filledRowCount++;
                }
            }
        }

        return filledRowCount;
    }

    public static Sheets createSheetsService() throws IOException, GeneralSecurityException {

        String refreshToken = "1//04SlFKMWjZytYCgYIARAAGAQSNwF-L9IrqhkeNxNRL5auyqb2SZFC4FIEZqSFTcMr0IzPgWDEZ_RuRTE37Og7nX8kPZg6LZghu4A";
        TokenResponse token = refreshAccessToken(refreshToken);
        Credential credential = createCredentialWithRefreshToken(token);
        System.err.println("credential -----" + credential.getAccessToken());

        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential) // .setApplicationName("Google-SheetsSample/0.1")
                .setApplicationName(APPLICATION_NAME).build();
    }

    private static void addNewSheet(String sheetId, String title) throws IOException, GeneralSecurityException {
        AddSheetRequest addSheetRequest = new AddSheetRequest();
        Sheets sheetsService = createSheetsService();
        SheetProperties sheetProperties = new SheetProperties().setTitle(title);
        addSheetRequest.setProperties(sheetProperties);

        BatchUpdateSpreadsheetRequest batchUpdateSpreadsheetRequest = new BatchUpdateSpreadsheetRequest();
        batchUpdateSpreadsheetRequest.setRequests(Collections.singletonList(
                new Request().setAddSheet(addSheetRequest)
        ));

        sheetsService.spreadsheets().batchUpdate(sheetId, batchUpdateSpreadsheetRequest).execute();
        System.out.println("New sheet added successfully.");
    }

    private static boolean sheetExists(Sheets sheetsService, String spreadsheetId, String sheetTitle) throws IOException {
        Spreadsheet spreadsheet = sheetsService.spreadsheets().get(spreadsheetId).execute();
        List<Sheet> sheets = spreadsheet.getSheets();

        for (Sheet sheet : sheets) {
            if (sheet.getProperties().getTitle().equals(sheetTitle)) {
                return true; // Sheet already exists
            }
        }

        return false; // Sheet does not exist
    }

//get Sheet_id this sheet id is usefull for styling cell
    private static int getsheetId(Sheets sheetsService, String spreadsheetId, String sheetTitle) throws IOException {
        Spreadsheet spreadsheet = sheetsService.spreadsheets().get(spreadsheetId).execute();
        List<Sheet> sheets = spreadsheet.getSheets();
        int sheet_id = 0;
        for (Sheet sheet : sheets) {
            if (sheet.getProperties().getTitle().equals(sheetTitle)) {

                sheet_id = sheet.getProperties().getSheetId();

            }
        }

        return sheet_id;
    }

    public static String uploadGoogleSheetAppendValue(List collum_names, List collum_values, String sheetId, String isReportExist, String sheet_name, String folder_id) throws IOException, GeneralSecurityException, NoSuchFieldException, SQLException {

        Sheets sheetsService = createSheetsService();
        boolean sheetExist = sheetExists(sheetsService, sheetId, sheet_name);

        if (!sheetExist) {
            addNewSheet(sheetId, sheet_name);
        }
        String sheetRange = sheet_name + "!A1";

        int sheet_id = getsheetId(sheetsService, sheetId, sheet_name);

        //To count filled Row used to get Last row for styling
        String spreadsheetId = sheetId;
        int countFilledRows = countFilledRows(sheetsService, spreadsheetId);

        int lastrow = collum_values.size() + countFilledRows;

        if (countFilledRows == 0) {

            lastrow = collum_values.size() + countFilledRows + 1;
        }

        // if folderId is not empty then we add File url column
        String valueInputOption = "USER_ENTERED";
        if (!folder_id.isEmpty()) {
            collum_names.add("Fileurl");
        }

        // if this report is not existing ie new report then header will be added
        List<List<Object>> listOfListOfStrings = new ArrayList<>();
        if (!sheetExist) {
            listOfListOfStrings.add(collum_names); // Column names
        }

        // here we are adding column values
        List<Object> list = null;
        for (int i = 0; i < collum_values.size(); i++) {

            list = (List<Object>) collum_values.get(i);

            listOfListOfStrings.add(list);

        }

        ValueRange requestBody1 = new ValueRange();
        requestBody1.setRange(sheetRange);
        requestBody1.setMajorDimension("ROWS");
        requestBody1.setValues(listOfListOfStrings);
        Sheets.Spreadsheets.Values.Append request = sheetsService.spreadsheets().values().append(spreadsheetId, sheetRange, requestBody1);
        request.setValueInputOption(valueInputOption);
        List<Request> requests = new ArrayList<>();

// Apply bold formatting to a column header
        requests.add(new Request()
                .setRepeatCell(new RepeatCellRequest()
                        .setRange(new GridRange()
                                .setSheetId(sheet_id) // Use the correct sheet ID
                                .setStartRowIndex(0) // Row index of the column header
                                .setEndRowIndex(1) // End row index of the column header
                                .setStartColumnIndex(0) // Start column index of the header
                                .setEndColumnIndex(collum_names.size()) // End column index of the header
                        )
                        .setCell(new CellData()
                                .setUserEnteredFormat(new CellFormat()
                                        .setTextFormat(new TextFormat().setBold(true))
                                )
                        )
                        .setFields("userEnteredFormat.textFormat.bold")
                ));

// Apply cell background color to a specific range
        requests.add(new Request()
                .setRepeatCell(new RepeatCellRequest()
                        .setRange(new GridRange()
                                .setSheetId(sheet_id) // Use the correct sheet ID
                                .setStartRowIndex(0) // Starting row index
                                .setEndRowIndex(1) // Ending row index
                                .setStartColumnIndex(0) // Starting column index
                                .setEndColumnIndex(collum_names.size()) // Ending column index
                        )
                        .setCell(new CellData()
                                .setUserEnteredFormat(new CellFormat()
                                        .setBackgroundColor(new Color().setRed(0.4f).setGreen(1.0f).setBlue(0.5f)) // Set RGB color values (Yellow)
                                )
                        )
                        .setFields("userEnteredFormat.backgroundColor")
                ));

        // Apply center alignment to a specific range
        requests.add(new Request()
                .setRepeatCell(new RepeatCellRequest()
                        .setRange(new GridRange()
                                .setSheetId(sheet_id) // Use the correct sheet ID
                                .setStartRowIndex(countFilledRows) // Starting row index
                                .setEndRowIndex(lastrow) // Ending row index
                                .setStartColumnIndex(0) // Starting column index
                                .setEndColumnIndex(collum_names.size()) // Ending column index
                        )
                        .setCell(new CellData()
                                .setUserEnteredFormat(new CellFormat()
                                        .setHorizontalAlignment("CENTER") // Center alignment
                                )
                        )
                        .setFields("userEnteredFormat.horizontalAlignment")
                ));

        // Apply text wrapping to a specific range
        requests.add(new Request()
                .setRepeatCell(new RepeatCellRequest()
                        .setRange(new GridRange()
                                .setSheetId(sheet_id) // Use the correct sheet ID
                                .setStartRowIndex(countFilledRows) // Starting row index
                                .setEndRowIndex(lastrow) // Ending row index
                                .setStartColumnIndex(0) // Starting column index
                                .setEndColumnIndex(collum_names.size()) // Ending column index
                        )
                        .setCell(new CellData()
                                .setUserEnteredFormat(new CellFormat()
                                        .setWrapStrategy("WRAP") // Text wrapping strategy
                                )
                        )
                        .setFields("userEnteredFormat.wrapStrategy")
                ));

        // Apply middle vertical alignment to a specific range
        requests.add(new Request()
                .setRepeatCell(new RepeatCellRequest()
                        .setRange(new GridRange()
                                .setSheetId(sheet_id) // Use the correct sheet ID
                                .setStartRowIndex(countFilledRows) // Starting row index
                                .setEndRowIndex(lastrow) // Ending row index
                                .setStartColumnIndex(0) // Starting column index
                                .setEndColumnIndex(collum_names.size()) // Ending column index
                        )
                        .setCell(new CellData()
                                .setUserEnteredFormat(new CellFormat()
                                        .setVerticalAlignment("MIDDLE") // Middle vertical alignment
                                )
                        )
                        .setFields("userEnteredFormat.verticalAlignment")
                ));

        BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest()
                .setRequests(requests);
        sheetsService.spreadsheets().batchUpdate(spreadsheetId, batchUpdateRequest)
                .execute();
        AppendValuesResponse response = request.execute();
        return response.getSpreadsheetId();

    }

}
