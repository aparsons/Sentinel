package net.aparsons.sentinel.concurrent;

import com.whitehatsec.xmlApiSite.SiteDocument;
import com.whitehatsec.xmlApiVuln.VulnerabilitiesDocument;
import java.io.File;
import net.aparsons.sentinel.api.SentinelAPI;
import net.aparsons.sentinel.http.HttpManager;
import net.aparsons.sentinel.io.FileManager;
import net.aparsons.sentinel.io.PathManager;
import org.apache.xmlbeans.XmlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SyncSentinelRunnable implements Runnable {

    private final Logger logger = LoggerFactory.getLogger(getClass());
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
                logger.error(xe.getMessage(), xe);
            }
        }

        logger.warn("Retrieval of site " + siteId + " failed");
        return null;
    }

    private VulnerabilitiesDocument getVulnerabilitiesDocument() {
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
