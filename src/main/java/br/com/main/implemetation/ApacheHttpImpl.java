package br.com.main.implemetation;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.time.StopWatch;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;

import br.com.main.implemetation.common.Constantes;
import br.com.main.interfaces.IPingRequest;

/**
 * The Class ApacheHttpImpl.
 */
public class ApacheHttpImpl implements IPingRequest {

	/** The client. */
	private HttpClient client;

	/**
	 * Instantiates a new apache http impl.
	 */
	public ApacheHttpImpl() {
		client = generateHttpClient();
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
			HttpGet getBody = new HttpGet(targetUrl);
			HttpResponse response = client.execute(getBody);
			Integer responseCode = response.getStatusLine().getStatusCode();
			sWatch.stop();
			log.info("Request ended ("+ responseCode +"). "  + sWatch.toString() + " ms");
			return sWatch.getTime();
		} catch (ClientProtocolException e) {
			log.error("ClientProtocolException", e);
		} catch (IOException e) {
			log.error("IOException", e);
		}
		return 0L;
	}

	/**
	 * Generate http client that ignores SSL Hand shake errors.
	 *
	 * @return the http client
	 */
	public HttpClient generateHttpClient() {
		try {
			//Authentication
			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(Constantes.NOME_USUARIO, Constantes.SENHA_USUARIO);
			provider.setCredentials(AuthScope.ANY, credentials);
			//Mock SSL
			HttpClient client = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
			SSLContext sslContext;
			sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
				public boolean isTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
						throws java.security.cert.CertificateException {
					return true;
				}
			}).build();
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,
					NoopHostnameVerifier.INSTANCE);
			provider.setCredentials(AuthScope.ANY, credentials);					
			client = HttpClientBuilder.create().setSSLSocketFactory(sslsf).setDefaultCredentialsProvider(provider)
					.build();
			return client;
		} catch (KeyManagementException e) {
			log.error(e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage(), e);
		} catch (KeyStoreException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}
}
