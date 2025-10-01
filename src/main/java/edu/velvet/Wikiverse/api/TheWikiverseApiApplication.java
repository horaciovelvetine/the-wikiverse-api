package edu.velvet.Wikiverse.api;

import edu.velvet.Wikiverse.api.services.logging.WikidataDocumentLogger;
import edu.velvet.Wikiverse.api.services.wikidata.DocumentProcessor;
import edu.velvet.Wikiverse.api.services.wikidata.FetchBroker;
import edu.velvet.Wikiverse.api.services.wikidata.WikidataService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.metrics.buffering.BufferingApplicationStartup;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TheWikiverseApiApplication {

	/**
	 * Setup to track startup details:
	 * https://www.baeldung.com/spring-boot-actuator-startup
	 */
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(TheWikiverseApiApplication.class);
		app.setApplicationStartup(new BufferingApplicationStartup(2048));
		app.run(args);
	}

	@Bean
	FetchBroker fetchBroker() {
		return new FetchBroker();
	}

	@Bean
	DocumentProcessor documentProcessor() {
		WikidataDocumentLogger logger = new WikidataDocumentLogger("document-processor.log");
		return new DocumentProcessor(logger);
	}

	@Bean
	WikidataService wikidataService() {
		return new WikidataService();
	}
}
