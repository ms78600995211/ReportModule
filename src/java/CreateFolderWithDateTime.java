
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CreateFolderWithDateTime {
    public static void main(String[] args) {
        // Get the current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();
        
        // Define a custom date and time format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        
        // Format the current date and time as a string
        String folderName = currentDateTime.format(formatter);
        
        // Specify the directory where you want to create the folder
        String directoryPath = "C:\\ssadvt_repository\\Reports\\toSplit"; // Change this to your desired directory path
        
        // Create a File object representing the new folder
        File newFolder = new File(directoryPath + folderName);
        
        // Check if the folder already exists
        if (!newFolder.exists()) {
            // Create the folder
            boolean folderCreated = newFolder.mkdir();
            
            if (folderCreated) {
                System.out.println("Folder created successfully: " + newFolder.getAbsolutePath());
            } else {
                System.err.println("Failed to create folder.");
            }
        } else {
            System.err.println("Folder already exists.");
        }
    }
}





