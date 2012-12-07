package net.aparsons.sentinel.http.ssl;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NaiveSSLSocketFactory {
    
    private static final Logger logger = LoggerFactory.getLogger(NaiveSSLSocketFactory.class);
    
    public static SSLSocketFactory getSocketFactory() {
        TrustManager[] tm = new TrustManager[] { new NaiveX509TrustManager() };
        
        SSLContext context = null;
        try {
            context = SSLContext.getInstance("SSL");
            context.init(null, tm, null);
        } catch (NoSuchAlgorithmException nsae) {
            logger.error(nsae.getMessage(), nsae);
        } catch (KeyManagementException kme) {        
            logger.error(kme.getMessage(), kme);
        }
        
        return new SSLSocketFactory(context, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    }
    
}

final class NaiveX509TrustManager implements X509TrustManager {

    @Override
    public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }
    
}
