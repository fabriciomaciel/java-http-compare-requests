package br.com.main.interfaces;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Interface IPingRequest.
 */
public interface IPingRequest {

	/** The log. */
	Logger log = LoggerFactory.getLogger(IPingRequest.class);

	/**
	 * Ping.
	 *
	 * @param targetUrl
	 *            the target url
	 * @return the response time
	 */
	Long ping(String targetUrl);

}
