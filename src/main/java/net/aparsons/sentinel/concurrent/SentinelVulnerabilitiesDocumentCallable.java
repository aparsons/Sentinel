package net.aparsons.sentinel.concurrent;

import com.whitehatsec.xmlApiVuln.VulnerabilitiesDocument;
import java.util.concurrent.Callable;
import net.aparsons.sentinel.api.SentinelAPI;
import net.aparsons.sentinel.http.HttpManager;
import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SentinelVulnerabilitiesDocumentCallable implements Callable<VulnerabilitiesDocument> {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final SentinelAPI api;
    private final int siteId;
    
    public SentinelVulnerabilitiesDocumentCallable(SentinelAPI api, int siteId) {
        this.api = api;
        this.siteId = siteId;
    }
    
    @Override
    public VulnerabilitiesDocument call(){        
        String response = HttpManager.getInstance().getRequest(api.getSiteVulnDetailsString(siteId));

        if (response != null) {
            try {
                return VulnerabilitiesDocument.Factory.parse(response);
            } catch (XmlException xe) {
                logger.error(xe.getMessage(), xe);
            }
        }
        
        logger.warn("Retrieval of vulnerabilities for site " + siteId + " failed");
        return null;
    }
    
}
