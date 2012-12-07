package net.aparsons.sentinel.concurrent;

import com.whitehatsec.xmlApiSite.SitesDocument;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.aparsons.sentinel.api.SentinelAPI;
import net.aparsons.sentinel.http.HttpManager;
import org.apache.xmlbeans.XmlException;

public class SentinelSitesDocumentCallable implements Callable<SitesDocument> {

    private final SentinelAPI api;
    
    public SentinelSitesDocumentCallable(SentinelAPI api) {
        this.api = api;
    }
    
    @Override
    public SitesDocument call(){        
        String response = HttpManager.getInstance().getRequest(api.getSiteListString());
        
        if (response != null) {
            try {
                return SitesDocument.Factory.parse(response);
            } catch (XmlException xe) {
                Logger.getLogger(SentinelSitesDocumentCallable.class.getName()).log(Level.SEVERE, xe.getMessage(), xe);
            }
        }
        
        Logger.getLogger(SentinelSitesDocumentCallable.class.getName()).log(Level.WARNING, "Retrieval of site list failed");
        return null;
    }
    
}
