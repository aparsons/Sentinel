package net.aparsons.sentinel.io;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.xmlbeans.XmlObject;
import org.apache.xmlbeans.XmlOptions;

public class FileManager {

    public static void writeXML(File file, XmlObject xmlObj) {
        // Creates parent directories
        File dir = new File(file.getParent());
        if (!dir.exists()) {
            Logger.getLogger(FileManager.class.getName()).log(Level.INFO, "Creating directory {0}", dir.getPath());
            dir.mkdirs();
        }

        XmlOptions options = new XmlOptions();
        options.setUseDefaultNamespace();        
        options.setSavePrettyPrint();
        options.setSavePrettyPrintIndent(4);
        
        // Save xml file
        try {
            xmlObj.save(file, options);
            Logger.getLogger(FileManager.class.getName()).log(Level.INFO, "Saved file {0} ", file.getPath());
        } catch (IOException ioe) {
            Logger.getLogger(FileManager.class.getName()).log(Level.WARNING, "Unable to save file" + file.getPath(), ioe);
        }
    }
    
}
