package net.aparsons.sentinel.concurrent;

import com.whitehatsec.xmlApiSite.SitesDocument;
import java.util.concurrent.Callable;
import net.aparsons.sentinel.api.SentinelAPI;
import net.aparsons.sentinel.http.HttpManager;
import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SentinelSitesDocumentCallable implements Callable<SitesDocument> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
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
                logger.error(xe.getMessage(), xe);                
            }
        }
        
        logger.warn("Retrieval of site list failed");
        return null;
    }
    
}
