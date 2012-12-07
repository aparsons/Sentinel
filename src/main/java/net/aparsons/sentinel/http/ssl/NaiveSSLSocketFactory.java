package net.aparsons.sentinel.http.ssl;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.http.conn.ssl.SSLSocketFactory;

public final class NaiveSSLSocketFactory {
        
    public static SSLSocketFactory getSocketFactory() {
        TrustManager[] tm = new TrustManager[] { new NaiveX509TrustManager() };
        
        SSLContext context = null;
        try {
            context = SSLContext.getInstance("SSL");
            context.init(null, tm, null);
        } catch (NoSuchAlgorithmException nsae) {
            // TODO Log
        } catch (KeyManagementException kme) {        
            // TODO Log
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
