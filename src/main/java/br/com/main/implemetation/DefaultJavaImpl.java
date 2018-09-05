package br.com.main.implemetation;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.Base64;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.time.StopWatch;

import br.com.main.implemetation.common.Constantes;
import br.com.main.interfaces.IPingRequest;
/**
 * The Class DefaultJavaImpl.
 */
public class DefaultJavaImpl implements IPingRequest {

	/** The trust all certs. */
	private TrustManager[] trustAllCerts;

	/**
	 * Instantiates a new default java impl.
	 */
	public DefaultJavaImpl() {
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
		try {
			StopWatch sWatch = new StopWatch();
			sWatch.start();
			// Install the all-trusting trust manager
			final SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
			sslContext.init(null, trustAllCerts, new SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

			URL serverUrl = new URL(targetUrl);
			HttpsURLConnection https = (HttpsURLConnection) serverUrl.openConnection();

			String encoded = Base64.getEncoder().encodeToString(
					(Constantes.NOME_USUARIO + ":" + Constantes.SENHA_USUARIO).getBytes(StandardCharsets.UTF_8)); 																													
			https.setRequestProperty("Authorization", "Basic " + encoded);

			https.setHostnameVerifier(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			int responseCode = https.getResponseCode();
			sWatch.stop();
			log.info("Request ended (" + responseCode + "). " + sWatch.toString() + " ms");
			return sWatch.getTime();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(), e);
		} catch (KeyManagementException e) {
			log.error(e.getMessage(), e);
		}
		return 0L;
	}

}
