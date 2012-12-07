package net.aparsons.sentinel.core;

import com.whitehatsec.xmlApiSite.SitesDocument;
import com.whitehatsec.xmlApiSite.Whsite;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import net.aparsons.sentinel.api.SentinelAPI;
import net.aparsons.sentinel.concurrent.SentinelSitesDocumentCallable;
import net.aparsons.sentinel.concurrent.SyncSentinelRunnable;

public class Sentinel {
    
    private final Preferences prefs;
    
    public Sentinel() {
        prefs = Preferences.userNodeForPackage(Sentinel.class);
    }
    
    public String getKey() {
        return prefs.get(Constants.PREF_SENTINEL_KEY, null);
    }
    
    public String getLog() {
        return prefs.get(Constants.PREF_SENTINEL_LOG, Constants.DEFAULT_SENTINEL_LOG);
    }
    
    public void setKey(String key) {
        prefs.put(Constants.PREF_SENTINEL_KEY, key);
    }
    
    public void setLog(String location) {
        prefs.put(Constants.PREF_SENTINEL_LOG, location);
    }
    
    public void sync() {        
        ExecutorService executor = Executors.newCachedThreadPool();
        
        SentinelAPI api = new SentinelAPI(getKey());
        
        // Retrieve list of sites
        SitesDocument sitesDoc = null;
        try {
            sitesDoc = executor.submit(new SentinelSitesDocumentCallable(api)).get();
        } catch (InterruptedException ie) {
            Logger.getLogger(Sentinel.class.getName()).log(Level.WARNING, ie.getMessage(), ie);
        } catch (ExecutionException ee) {
            Logger.getLogger(Sentinel.class.getName()).log(Level.WARNING, ee.getMessage(), ee);
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