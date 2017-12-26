package com.converter.rtfconverter.service;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


public class TextFooter {
    public static final String SOURCE = "page_footer.pdf";
    public static final String DESTINATION = "page_footers.pdf";

    public static void main(String[] args) throws IOException, DocumentException {
        File file = new File(DESTINATION);
        new TextFooter().manipulatePdf(SOURCE,DESTINATION);
    }
    public void manipulatePdf(String src, String dest){
        try {
            PdfReader reader = new PdfReader(src);
            PdfStamper stamper = new PdfStamper(reader, new FileOutputStream(dest));
            Phrase header = new Phrase("Header editted",new Font(FontFamily.HELVETICA,12));
            float x,y;

            for(int i=1;i<=reader.getNumberOfPages();i++){
                System.out.println("Reader PagerRotation: "+reader.getPageRotation(i));
                x=reader.getPageSize(i).getWidth()/8;
                y=reader.getPageSize(i).getTop(20);

                System.out.print("X="+ x + " Y="+ y);

                ColumnText.showTextAligned(
                    stamper.getOverContent(i),Element.ALIGN_CENTER,header,x,y,0);
            }
            stamper.close();
            reader.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

}
