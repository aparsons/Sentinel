package net.aparsons.sentinel.concurrent;

import com.whitehatsec.xmlApiSite.SiteDocument;
import com.whitehatsec.xmlApiVuln.VulnerabilitiesDocument;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.aparsons.sentinel.api.SentinelAPI;
import net.aparsons.sentinel.http.HttpManager;
import net.aparsons.sentinel.io.FileManager;
import net.aparsons.sentinel.io.PathManager;
import org.apache.xmlbeans.XmlException;

public class SyncSentinelRunnable implements Runnable {

    private final SentinelAPI api;
    private final int siteId;

    public SyncSentinelRunnable(SentinelAPI api, int siteId) {
        this.api = api;
        this.siteId = siteId;
    }

    @Override
    public void run() {
        final long time = System.currentTimeMillis();

        // SiteDocument
        SiteDocument siteDoc = getSiteDocument();
        if (siteDoc != null) {
            FileManager.writeXML(new File(PathManager.getDataPath(siteId), time + ".site.xml"), siteDoc);
        }
        
        // Vulnerabilities
        VulnerabilitiesDocument vulnDoc = getVulnerabilitiesDocument();
        if (vulnDoc != null) {
            FileManager.writeXML(new File(PathManager.getDataPath(siteId), time + ".vulnerabilities.xml"), vulnDoc);
        }
    }

    private SiteDocument getSiteDocument() {
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

    private VulnerabilitiesDocument getVulnerabilitiesDocument() {
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
