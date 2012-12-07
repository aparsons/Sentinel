package net.aparsons.sentinel.io;

import java.io.File;
import java.io.IOException;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileManager {

    private static final Logger logger = LoggerFactory.getLogger(FileManager.class);
    
    public static void writeXML(File file, XmlObject xmlObj) {
        // Creates parent directories
        File dir = new File(file.getParent());
        if (!dir.exists()) {
            logger.info("Creating directory: " + dir.getPath());
            dir.mkdirs();
        }

        XmlOptions options = new XmlOptions();
        options.setUseDefaultNamespace();        
        options.setSavePrettyPrint();
        options.setSavePrettyPrintIndent(4);
        
        // Save xml file
        try {
            xmlObj.save(file, options);
            logger.info("Saved file: " + file.getPath());
        } catch (IOException ioe) {
            logger.warn("Unable to save file: " + file.getPath(), ioe);
        }
    }
    
}
