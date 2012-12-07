package net.aparsons.sentinel.concurrent;

import com.whitehatsec.xmlApiSite.SiteDocument;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.aparsons.sentinel.api.SentinelAPI;
import net.aparsons.sentinel.http.HttpManager;
import org.apache.xmlbeans.XmlException;

public class SentinelSiteDocumentCallable implements Callable<SiteDocument> {

    private final SentinelAPI api;
    private final int siteId;
    
    public SentinelSiteDocumentCallable(SentinelAPI api, int siteId) {
        this.api = api;
        this.siteId = siteId;
    }
    
    @Override
    public SiteDocument call(){        
        String response = HttpManager.getInstance().getRequest(api.getSiteDetailsString(siteId));

        if (response != null) {
            try {
                return SiteDocument.Factory.parse(response);
            } catch (XmlException xe) {
                Logger.getLogger(SentinelSiteDocumentCallable.class.getName()).log(Level.SEVERE, xe.getMessage(), xe);
            }
        }
        
        Logger.getLogger(SentinelSiteDocumentCallable.class.getName()).log(Level.WARNING, "Retrieval of site {0} failed", siteId);
        return null;
    }
    
}
