package net.aparsons.sentinel.io;

import java.io.File;

public class PathManager {   
    
    private static File rootPath;
    private static File dataPath;

    public static File getRootPath() {
        if (rootPath == null) {
            rootPath = new File("").getAbsoluteFile();
        }
        
        return rootPath;
    }

    public static File getDataPath() {
        if (dataPath == null) {
            dataPath = new File(rootPath, "data");
        }
        
        return dataPath;
    }
    
    public static File getDataPath(int siteId) {        
        return new File(getDataPath(), Integer.toString(siteId));
    }
    
}
