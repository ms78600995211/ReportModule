
import java.io.File;

public class FetchAllFilesFromFolder {

    public static void main(String[] args) {
        // Specify the folder path
        String folderPath = "C:\\ssadvt_repository\\Reports\\splitedfiles"; // Update with your folder path

        // Create a File object representing the folder
        File folder = new File(folderPath);

        // Check if the folder exists and is indeed a directory
        if (folder.exists() && folder.isDirectory()) {
            // List all files in the folder
            File[] files = folder.listFiles();

            if (files != null) {
                System.out.println("Files in the folder:");
                for (File file : files) {
                    if (file.isFile()) {
                        String file_path = folderPath + "\\" + file.getName();


                    }
                }

            } else {
                System.out.println("No files found in the folder.");
            }
        } else {
            System.out.println("Folder not found: " + folderPath);
        }
    }
}
