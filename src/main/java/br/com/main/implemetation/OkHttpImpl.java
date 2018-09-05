package br.com.main.implemetation;

import java.io.IOException;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.time.StopWatch;

import br.com.main.implemetation.common.Constantes;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import br.com.main.interfaces.IPingRequest;
/**
 * The Class OkHttpImpl.
 */
public class OkHttpImpl implements IPingRequest {

	/** The client. */
	private OkHttpClient client;

	/**
	 * Instantiates a new ok http impl.
	 */
	public OkHttpImpl() {
		log.info("Starting OkHttpImpl()");
		client = getUnsafeOkHttpClient();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.main.implemetation.IPingRequest#ping(java.lang.String)
	 */
	public Long ping(String targetUrl) {
		String auth = Credentials.basic(Constantes.NOME_USUARIO, Constantes.SENHA_USUARIO);
		Request request = new Request.Builder().header("Authorization", auth).url(targetUrl).build();
		Response response;
		try {
			StopWatch sWatch = new StopWatch();
			sWatch.start();
			response = client.newCall(request).execute();
			Integer responseCode = response.code();
			sWatch.stop();
			log.info("Request ended ("+ responseCode +"). "  + sWatch.toString() + " ms");
			return sWatch.getTime();
		} catch (IOException e) {
			log.error("OkHttpImpl - IOException ", e);
		}
		return 0L;
	}

	/**
	 * Gets the unsafe ok http client.
	 * ref: https://gist.github.com/drakeet/93be3c6ff9ce2d4ca8fb
	 * @return the unsafe ok http client
	 */
	private OkHttpClient getUnsafeOkHttpClient() {
		try {
			// Create a trust manager that does not validate certificate chains
			final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

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
			// Install the all-trusting trust manager
			final SSLContext sslContext = SSLContext.getInstance("SSL");
			sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
			// Create an ssl socket factory with our all-trusting manager
			final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
			OkHttpClient.Builder builder = new OkHttpClient.Builder();
			builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
			builder.hostnameVerifier(new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			});
			OkHttpClient okHttpClient = builder.build();
			return okHttpClient;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
}
