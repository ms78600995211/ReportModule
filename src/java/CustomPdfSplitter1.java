import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.exceptions.COSVisitorException;

public class CustomPdfSplitter1 {
    public static void main(String[] args) throws COSVisitorException {
      String sourcePdfFile = "C:\\Users\\admin\\Desktop\\input.pdf"; // Replace with your source PDF file path
        String outputDirectory = "C:\\Users\\admin\\Desktop\\New folder"; // Replace with your desired output directory
        int pagesPerFile = 3;               // Customize the number of pages per output PDF

        try {
            PDDocument document = PDDocument.load(new File(sourcePdfFile));
            int pageCount = document.getNumberOfPages();

            for (int i = 0; i < pageCount; i += pagesPerFile) {
                PDDocument customPageDocument = new PDDocument();

                for (int j = i; j < i + pagesPerFile && j < pageCount; j++) {
                    customPageDocument.addPage((PDPage) document.getDocumentCatalog().getAllPages().get(j));
                }

                String outputFileName = outputDirectory + "custom_pages_" + (i / pagesPerFile + 1) + ".pdf";
                customPageDocument.save(outputFileName);
                customPageDocument.close();
                
                System.out.println("Custom pages " + (i + 1) + " to " + (i + pagesPerFile) + " saved as " + outputFileName);
            }

            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
