import org.apache.pdfbox.pdmodel.PDDocument;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.util.PDFMergerUtility;

public class PDFCombine {
    public static void main(String[] args) throws COSVisitorException {
        try {
            // Create a PDFMergerUtility
            PDFMergerUtility merger = new PDFMergerUtility();

            // Add PDF files that you want to combine
            merger.addSource(new File("C:\\Users\\admin\\Desktop\\New folderpage_1.pdf"));
            merger.addSource(new File("C:\\Users\\admin\\Desktop\\New folderpage_2.pdf"));
            // Add more sources if needed

            // Create an output stream for the merged PDF
            OutputStream outputStream = new FileOutputStream("C:\\Users\\admin\\Desktop\\combined.pdf");

            // Merge the PDFs to the specified output stream
            merger.setDestinationStream(outputStream);
            merger.mergeDocuments();

            // Close the output stream
            outputStream.close();

            System.out.println("PDFs merged successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
