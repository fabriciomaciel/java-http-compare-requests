package br.com.main.implemetation;

import org.apache.commons.lang3.time.StopWatch;

import br.com.main.implemetation.common.Constantes;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import br.com.main.interfaces.IPingRequest;
/**
 * The Class RestAssuredImpl.
 */
public class RestAssuredImpl implements IPingRequest {

	/*
	 * (non-Javadoc)
	 * 
	 * @see br.com.main.implemetation.IPingRequest#ping(java.lang.String)
	 */
	public Long ping(String targetUrl) {
		StopWatch sWatch = new StopWatch();
		sWatch.start();
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.preemptive().basic(Constantes.NOME_USUARIO, Constantes.SENHA_USUARIO);
		Response response = RestAssured.get(targetUrl);
		Integer responseCode = response.getStatusCode();
		sWatch.stop();
		log.info("Request ended ("+ responseCode +"). "  + sWatch.toString() + " ms");
		return sWatch.getTime();
	}

}
