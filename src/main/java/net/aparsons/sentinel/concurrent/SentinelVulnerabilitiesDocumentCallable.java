package net.aparsons.sentinel.concurrent;

import com.whitehatsec.xmlApiVuln.VulnerabilitiesDocument;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.aparsons.sentinel.api.SentinelAPI;
import net.aparsons.sentinel.http.HttpManager;
import org.apache.xmlbeans.XmlException;

public class SentinelVulnerabilitiesDocumentCallable implements Callable<VulnerabilitiesDocument> {

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
                Logger.getLogger(SentinelVulnerabilitiesDocumentCallable.class.getName()).log(Level.SEVERE, xe.getMessage(), xe);
            }
        }
        
        Logger.getLogger(SentinelVulnerabilitiesDocumentCallable.class.getName()).log(Level.WARNING, "Retrieval of vulnerabilities for site {0} failed", siteId);
        return null;
    }
    
}
