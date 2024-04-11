public class FilePathSeparatorExample {
    public static void main(String[] args) {
        // Replace single separator with double separator in a file path
        String filePath = "C:\\Users\\User\\Documents\\file.txt"; // Windows path
        filePath = filePath.replace("\\", "\\\\"); // Replace single backslashes with double backslashes
        
        // Print the modified file path
        System.out.println(filePath);
    }
}