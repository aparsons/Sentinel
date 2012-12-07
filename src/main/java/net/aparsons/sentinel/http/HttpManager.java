package net.aparsons.sentinel.http;

import java.io.IOException;
import java.net.SocketTimeoutException;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpManager {
    
    private static final int REQUEST_ATTEMPTS = 5;
    
    private static HttpManager instance;
    
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final HttpClient client;
    private final ResponseHandler<String> rh;
    
    private HttpManager() {
        final SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));
        
        final BasicHttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, "UTF-8");
        HttpProtocolParams.setUserAgent(params, "");
        
        client = new DefaultHttpClient(new PoolingClientConnectionManager(schemeRegistry), params);
        
        rh = new BasicResponseHandler();
    }
    
    public static HttpManager getInstance() {
        if (instance == null) {
            instance = new HttpManager();
        }
        
        return instance;
    }
    
    public String getRequest(String url) {
        return request(new HttpGet(url));
    }
    
    private String request(HttpUriRequest hur) {
        String response = null;
                
        for (int attempt = 1; attempt <= REQUEST_ATTEMPTS && response == null; attempt++) {
            try {
                logger.info("Executing request " + attempt + "/" + REQUEST_ATTEMPTS + ": " + hur.getRequestLine().toString());
                response = client.execute(hur, rh);
            } catch (SocketTimeoutException ste) {
                if (attempt < REQUEST_ATTEMPTS) {
                    logger.info("Retrying request in " + attempt + " seconds");
                    try {
                        Thread.sleep(1000 * attempt);
                    } catch (InterruptedException ie) {
                    }
                }
            } catch (ClientProtocolException cpe) {
                logger.error(cpe.getMessage(), cpe);
            } catch (IOException ioe) {
                logger.error(ioe.getMessage(), ioe);
            }
        }
        
        return response;
    }
    
}