package net.aparsons.sentinel.concurrent;

import com.whitehatsec.xmlApiSite.SiteDocument;
import java.util.concurrent.Callable;
import net.aparsons.sentinel.api.SentinelAPI;
import net.aparsons.sentinel.http.HttpManager;
import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SentinelSiteDocumentCallable implements Callable<SiteDocument> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
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
                logger.error(xe.getMessage(), xe);
            }
        }
        
        logger.warn("Retrieval of site " + siteId + " failed");
        return null;
    }
    
}
