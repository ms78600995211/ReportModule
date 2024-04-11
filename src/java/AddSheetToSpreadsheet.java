import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

public class AddSheetToSpreadsheet {
    private static final String SPREADSHEET_ID = "YOUR_SPREADSHEET_ID";
    private static final String APPLICATION_NAME = "Your Application Name";

    public static void main(String[] args) {
        try {
            Sheets sheetsService = getSheetsService();
            addNewSheet(sheetsService, "NewSheetName");
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
}
