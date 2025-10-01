package edu.velvet.Wikiverse.api.services.logging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/**
 * Provides universal access to a single static ObjectMapper for JSON serialization and deserialization.
 * This interface serves as a centralized point for Jackson ObjectMapper configuration across the application,
 * ensuring consistent JSON processing behavior throughout the Wikiverse API.
 *
 * <p>The ObjectMapper is configured with default settings and can be used by any class that implements
 * this interface to convert Java objects to JSON and vice versa. This approach promotes code reusability
 * and maintains consistency in JSON handling across different parts of the application.
 *
 * <p>Usage example:
 * <pre>{@code
 * public class MyService implements Mappable {
 *   public String toJson(Object obj) {
 *     try {
 *       return objectMapper.writeValueAsString(obj);
 *     } catch (Exception e) {
 *       // handle exception
 *     }
 *   }
 * }
 * }</pre>
 *
 * <p>This interface is particularly useful for:
 * <ul>
 *   <li>Converting model objects to JSON for API responses</li>
 *   <li>Parsing JSON data from external APIs</li>
 *   <li>Serializing configuration objects to JSON files</li>
 *   <li>Logging structured data in JSON format</li>
 * </ul>
 *
 * @author @horaciovelvetine
 * @version 1.0
 * @since 1.0
 * @see com.fasterxml.jackson.databind.ObjectMapper
 */
public interface Mappable {
	/**
	 * A shared ObjectMapper instance for JSON serialization and deserialization.
	 * This static field provides a single, reusable ObjectMapper instance that can be
	 * used by any class implementing this interface to convert between Java objects
	 * and JSON representations.
	 *
	 * <p>The ObjectMapper is initialized with default configuration settings and can
	 * be used for both reading JSON into Java objects and writing Java objects to JSON.
	 *
	 * <p>Thread Safety: ObjectMapper instances are thread-safe and can be safely used
	 * across multiple threads without additional synchronization.
	 *
	 * @return the shared ObjectMapper instance for JSON operations
	 */
	static final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
}
