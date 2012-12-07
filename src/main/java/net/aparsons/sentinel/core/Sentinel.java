package net.aparsons.sentinel.core;

import com.whitehatsec.xmlApiSite.SitesDocument;
import com.whitehatsec.xmlApiSite.Whsite;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.prefs.Preferences;
import net.aparsons.sentinel.api.SentinelAPI;
import net.aparsons.sentinel.concurrent.SentinelSitesDocumentCallable;
import net.aparsons.sentinel.concurrent.SyncSentinelRunnable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sentinel {
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final Preferences prefs;
    
    public Sentinel() {
        prefs = Preferences.userNodeForPackage(Sentinel.class);
    }
    
    public String getKey() {
        return prefs.get(Constants.PREF_SENTINEL_KEY, null);
    }
    
    public void setKey(String key) {
        prefs.put(Constants.PREF_SENTINEL_KEY, key);
    }
    
    public void sync() {       
        ExecutorService executor = Executors.newCachedThreadPool();
        
        SentinelAPI api = new SentinelAPI(getKey());
        
        // Retrieve list of sites
        SitesDocument sitesDoc = null;
        try {
            sitesDoc = executor.submit(new SentinelSitesDocumentCallable(api)).get();
        } catch (InterruptedException ie) {
            logger.warn(ie.getMessage(), ie);
        } catch (ExecutionException ee) {
            logger.warn(ee.getMessage(), ee);
        }
        
        // Retrieve data for each siteDocFuture
        if (sitesDoc != null) {
            for (Whsite site : sitesDoc.getSites().getSiteArray()) {
                executor.submit(new SyncSentinelRunnable(api, site.getId().intValue()));
            }
        }
        
        executor.shutdown();
    }
        
}