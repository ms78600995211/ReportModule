import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;

import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.exceptions.COSVisitorException;

public class PdfSplitter {
    public static void main(String[] args) throws COSVisitorException {


        String sourcePdfFile = "C:\\Users\\admin\\Desktop\\input.pdf"; // Replace with your source PDF file path
        String outputDirectory = "C:\\Users\\admin\\Desktop\\New folder"; // Replace with your desired output directory

        try {
            PDDocument document = PDDocument.load(new File(sourcePdfFile));
            int pageCount = document.getNumberOfPages();

            for (int i = 0; i < pageCount; i++) {
                PDDocument singlePageDocument = new PDDocument();
                singlePageDocument.addPage((PDPage) document.getDocumentCatalog().getAllPages().get(i));
                
                String outputFileName = outputDirectory + "page_" + (i + 1) + ".pdf";
                singlePageDocument.save(outputFileName);
                singlePageDocument.close();
                
                System.out.println("Page " + (i + 1) + " saved as " + outputFileName);
            }

            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}