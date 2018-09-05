package br.com.main.implemetation;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.time.StopWatch;
import org.javalite.http.Get;
import org.javalite.http.Http;

import br.com.main.implemetation.common.Constantes;
import br.com.main.interfaces.IPingRequest;
/**
 * The Class JavaLiteHttpImpl.
 */
public class JavaLiteHttpImpl implements IPingRequest {

	private TrustManager[] trustAllCerts;
	
	public JavaLiteHttpImpl() {
		// Create a trust manager that does not validate certificate chains
		trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType)
					throws CertificateException {
			}

			public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType)
					throws CertificateException {
			}

			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}
		} };
	}
 	/*
	 * (non-Javadoc)
	 *  
	 * @see br.com.main.implemetation.IPingRequest#ping(java.lang.String)
	 */
	public Long ping(String targetUrl) {
		StopWatch sWatch = new StopWatch();
		//Mesma solução do que a implementação Default do JDK 
		try {
			// Install the all-trusting trust manager
			final SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
			sslContext.init(null, trustAllCerts, new SecureRandom());
		    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());				
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(), e);
		} catch (KeyManagementException e) {
			log.error(e.getMessage(), e);			
		}
		sWatch.start();
		Get get = Http.get(targetUrl).basic(Constantes.NOME_USUARIO, Constantes.SENHA_USUARIO);
		Integer responseCode = get.responseCode(); 
		sWatch.stop();
		log.info("Request ended ("+ responseCode +"). "  + sWatch.toString() + " ms");
		return sWatch.getTime();
	}

}
