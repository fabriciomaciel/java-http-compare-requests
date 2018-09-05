package br.com.main;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.main.implemetation.ApacheHttpImpl;
import br.com.main.implemetation.DefaultJavaImpl;
import br.com.main.implemetation.JavaLiteHttpImpl;
import br.com.main.implemetation.OkHttpImpl;
import br.com.main.implemetation.RestAssuredImpl;

/**
 * The Class MainRequest.
 * @author Fabricio Maciel
 */
public class MainRequest {

	/** The log. */
	static Logger log = LoggerFactory.getLogger(MainRequest.class);

	/**
	 * Instantiates a new main request.
	 */
	public MainRequest() {
		log.info("== Starting Process ==");
	}

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		/*
		 * Variável que será usada para a url alvo da request
		 */
		String targetUrl = "";
		/*
		 * Array usado para os alvos disponiveis da request
		 */
		String[] targets_urls = {"https://www.terra.com.br/",
				         "https://www.google.com.br", 
					 "https://www.uol.com.br/",
				         "https://pt.wikipedia.org/wiki/Wiki", 
					 "https://start.spring.io/" };
		/*
		 * Lista que guarda todos os tempos de execução de cada função para cada Url
		 * alvo
		 */
		List<Long> tempoTotalExecucao = new ArrayList<Long>() {
			{
				add(0, 0L);
				add(1, 0L);
				add(2, 0L);
				add(3, 0L);
				add(4, 0L);
			}
		};
		/*
		 * Array para executar todas as funções em diferentes ordens
		 */
		int[][] pRodada = { { 0, 1, 2, 3, 4 }, { 1, 2, 3, 4, 0 }, { 2, 3, 4, 0, 1 }, { 3, 4, 0, 1, 2 },
				{ 4, 0, 1, 2, 3 } };
		/*
		 * Percorre o array chamando a função de acordo com o posicionamento indicado
		 */
		for (int i = 0; i < pRodada.length; i++) {
			for (int j = 0; j < pRodada[i].length; j++) {
				// Indice (codigo da função)
				int index = pRodada[i][j];
				// Url Alvo
				targetUrl = targets_urls[i];
				// tempo retornado por esta execução
				Long tempoExecucao = callMyFunction(index, targetUrl);
				// guarda o resultado do tempo total de execução
				tempoTotalExecucao.set(index, (tempoExecucao + tempoTotalExecucao.get(index)));
			}
		}
		/*
		 * Imprime o resultado do tempo total de execução
		 */
		for (int i = 0; i < tempoTotalExecucao.size(); i++) {
			System.out.println("Tempo total de execução da função [" + i + "] = " + tempoTotalExecucao.get(i) + "ms.");
		}
	}

	/**
	 * Call my function.
	 *
	 * @param functionCode
	 *            the function code
	 * @param targetUrl
	 *            the target url
	 * @return the request execution time in milliseconds
	 */
	private static long callMyFunction(int functionCode, String targetUrl) {
		long responseCode = 0L;
		switch (functionCode) {
			case 0:
				// JavaLite Http Implementation
				log.info("** INICIO JavaLite Http **");
				JavaLiteHttpImpl jHttp = new JavaLiteHttpImpl();
				responseCode = jHttp.ping(targetUrl);
				log.info("Request time: " + responseCode + " ms\n");
				break;
			case 1:
				// OK Http Implementation
				log.info("** INICIO OkHttp **");
				OkHttpImpl okRequest = new OkHttpImpl();
				responseCode = okRequest.ping(targetUrl);
				log.info("Request time: " + responseCode + " ms\n");
				break;
			case 2:
				// Java Default Http Implementation
				log.info("** INICIO Java Default Http **");
				DefaultJavaImpl dHttp = new DefaultJavaImpl();
				responseCode = dHttp.ping(targetUrl);
				log.info("Request time: " + responseCode + " ms\n");
				break;
			case 3:
				// Apache Http implementation
				log.info("** INICIO Apache Http **");
				ApacheHttpImpl aHttp = new ApacheHttpImpl();
				responseCode = aHttp.ping(targetUrl);
				log.info("Request time: " + responseCode + " ms\n");
				break;
			default:
				// Rest Assured Implementation
				log.info("** INICIO RestAssured **");
				RestAssuredImpl raRequest = new RestAssuredImpl();
				responseCode = raRequest.ping(targetUrl);
				log.info("Request time: " + responseCode + " ms\n");
				break;
		}
		return responseCode;
	}

}
