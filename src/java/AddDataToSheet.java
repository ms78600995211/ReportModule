
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AddDataToSheet {

    private static final String SPREADSHEET_ID = "YOUR_SPREADSHEET_ID";
    private static final String APPLICATION_NAME = "Your Application Name";

    public static void main(String[] args) {
        try {
            Sheets sheetsService = getSheetsService();
            String sheetTitle = "NewSheetName";

            // Add a new sheet
            addNewSheet(sheetsService, sheetTitle);

            // Add data to the newly created sheet
            addDataToSheet(sheetsService, SPREADSHEET_ID, sheetTitle);
        } catch (IOException | GeneralSecurityException e) {
            e.printStackTrace();
        }
    }

    private static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        return new Sheets.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                jsonFactory,
                null)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    private static void addNewSheet(Sheets sheetsService, String sheetTitle) throws IOException {
        AddSheetRequest addSheetRequest = new AddSheetRequest();
        SheetProperties sheetProperties = new SheetProperties().setTitle(sheetTitle);
        addSheetRequest.setProperties(sheetProperties);

        BatchUpdateSpreadsheetRequest batchUpdateSpreadsheetRequest = new BatchUpdateSpreadsheetRequest();
        batchUpdateSpreadsheetRequest.setRequests(Collections.singletonList(
                new Request().setAddSheet(addSheetRequest)
        ));

        sheetsService.spreadsheets().batchUpdate(SPREADSHEET_ID, batchUpdateSpreadsheetRequest).execute();
        System.out.println("New sheet added successfully.");
    }

    private static void addDataToSheet(Sheets sheetsService, String spreadsheetId, String sheetTitle) throws IOException {
        ValueRange body = new ValueRange().setValues(Arrays.asList(
                Arrays.asList("Name", "Age", "City"),
                Arrays.asList("John Doe", 25, "New York"),
                Arrays.asList("Jane Smith", 30, "San Francisco"),
                Arrays.asList("Bob Johnson", 22, "Los Angeles")
        ));

        sheetsService.spreadsheets().values()
                .update(spreadsheetId, sheetTitle + "!A1", body)
                .setValueInputOption("RAW")
                .execute();

        System.out.println("Data added to the sheet successfully.");
    }




  private static void appendDataToSheet(Sheets sheetsService, String spreadsheetId, String sheetTitle) throws IOException {
        ValueRange body = new ValueRange().setValues(Arrays.asList(
                Arrays.asList("John Doe", 25, "New York"),
                Arrays.asList("Jane Smith", 30, "San Francisco"),
                Arrays.asList("Bob Johnson", 22, "Los Angeles")
        ));

        AppendValuesResponse appendValuesResponse = sheetsService.spreadsheets().values()
                .append(spreadsheetId, sheetTitle, body)
                .setValueInputOption("RAW")
                .execute();

        System.out.println("Data appended to the sheet successfully. Rows updated: " +
                appendValuesResponse.getUpdates().getUpdatedRows());
    }
}
