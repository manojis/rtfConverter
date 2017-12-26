package com.converter.rtfconverter.service;

import com.artofsolving.jodconverter.DefaultDocumentFormatRegistry;
import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.DocumentFormat;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;
import com.artofsolving.jodconverter.openoffice.converter.StreamOpenOfficeDocumentConverter;
import org.apache.commons.io.output.NullOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;


public class ConvertDocumentToPdfTest {
    private Logger logger = LoggerFactory.getLogger(ConvertDocumentToPdfTest.class);

    public static final int SPEED_TEST_ITERATIONS = 100;
    public static final String OPENOFFICE_HOST = "dev.emma-nl.hypoport.local";

    private static final String SOURCE_ODF_NAME = "sample.rtf";
    private static final String TARGET_PDF_NAME = "sample.pdf";

    public static void main(String[] args) {
        OpenOfficeConnection connection = null;
        try {
            File inputFile = new File(SOURCE_ODF_NAME);
            System.out.println("presenting the path: "+ inputFile.getAbsolutePath().toString());
            File outputFile = new File(TARGET_PDF_NAME);
            System.out.println("presenting the path: "+ outputFile.getAbsolutePath().toString());


            // connect to an OpenOffice.org instance running on port 8100
            // run in shell: soffice -headless -accept="socket,host=127.0.0.1,port=8100;urp;" -nofirststartwizard
            // See: http://www.artofsolving.com/
            connection = new SocketOpenOfficeConnection(8100);
            connection.connect();

            // convert
            DocumentConverter converter = new OpenOfficeDocumentConverter(connection);
            converter.convert(inputFile, outputFile);
        }catch (ConnectException e) {
            e.printStackTrace();
        }
        finally {
            // close the connection
            if (connection.isConnected()) {
                connection.disconnect();
            }
        }
    }


    public void testConvertToPdfUsingStreams() throws Exception {
        OpenOfficeConnection connection = null;
        try {
            File inputFile = new File(SOURCE_ODF_NAME);
            File outputFile = new File(TARGET_PDF_NAME);
            InputStream inputStream = new FileInputStream(inputFile);
            OutputStream outputStream = new FileOutputStream(outputFile);

            // connect to an OpenOffice.org instance running on port 8100
            // run in shell: soffice -headless -accept="socket,host=127.0.0.1,port=8100;urp;" -nofirststartwizard
            // See: http://www.artofsolving.com/
            connection = new SocketOpenOfficeConnection(OPENOFFICE_HOST, 8100);
            connection.connect();

            // convert
            final DefaultDocumentFormatRegistry formatRegistry = new DefaultDocumentFormatRegistry();
            DocumentConverter converter = new StreamOpenOfficeDocumentConverter(connection, formatRegistry);
            final DocumentFormat odtFileFormat = formatRegistry.getFormatByFileExtension("odt");
            final DocumentFormat pdfFileFormat = formatRegistry.getFormatByFileExtension("pdf");
            converter.convert(inputStream, odtFileFormat, outputStream, pdfFileFormat);
        }
        finally {
            // close the connection
            if (connection.isConnected()) {
                connection.disconnect();
            }
        }
    }

    public void testConversionSpeedWithStreams() throws Exception {
        OpenOfficeConnection connection = null;
        try {
            File inputFile = new File(SOURCE_ODF_NAME);
            File outputFile = new File(TARGET_PDF_NAME);

            // connect to an OpenOffice.org instance running on port 8100
            // run in shell: soffice -headless -accept="socket,host=127.0.0.1,port=8100;urp;" -nofirststartwizard
            // See: http://www.artofsolving.com/
            connection = new SocketOpenOfficeConnection(OPENOFFICE_HOST, 8100);
            connection.connect();

            // convert
            final DefaultDocumentFormatRegistry formatRegistry = new DefaultDocumentFormatRegistry();
            DocumentConverter converter = new StreamOpenOfficeDocumentConverter(connection, formatRegistry);
            final DocumentFormat odtFileFormat = formatRegistry.getFormatByFileExtension("odt");
            final DocumentFormat pdfFileFormat = formatRegistry.getFormatByFileExtension("pdf");
            final long timestampAtStart = System.currentTimeMillis();
            for (int i = 0; i < SPEED_TEST_ITERATIONS; i++) {
                InputStream inputStream = new FileInputStream(inputFile);
                OutputStream outputStream = new NullOutputStream();
                converter.convert(inputStream, odtFileFormat, outputStream, pdfFileFormat);
            }
            final long timestampAtEnd = System.currentTimeMillis();

            final long duration = timestampAtEnd - timestampAtStart;
            logger.info("Duration: " + duration + "ms (=> " + duration * 1.0d / SPEED_TEST_ITERATIONS + "ms per document generation)");
        }
        finally {
            // close the connection
            if (connection.isConnected()) {
                connection.disconnect();
            }
        }
    }
}
