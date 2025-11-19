package edu.velvet.Wikiverse.api.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import java.time.Instant;

/**
 * Represents the error hierarchy for the Wikiverse API application.
 * This sealed interface defines the structure for all error types that can be
 * thrown
 * throughout the application, providing a comprehensive error handling
 * mechanism with
 * detailed context, tracking, and debugging information.
 *
 * <p>
 * All error implementations must provide:
 * <ul>
 * <li>A descriptive error message for debugging purposes</li>
 * <li>A source location to help identify where the error originated</li>
 * <li>A timestamp indicating when the error occurred</li>
 * <li>Contextual data relevant to the error</li>
 * <li>An error category for classification</li>
 * </ul>
 *
 * <p>
 * The error hierarchy is organized into two main categories:
 * <ul>
 * <li>{@link ServiceFault} - Generic service-level errors</li>
 * <li>{@link WikidataServiceErr} - Specific errors related to Wikidata service
 * operations</li>
 * </ul>
 *
 * <p>
 * This enhanced design ensures that all errors provide comprehensive context
 * for debugging,
 * monitoring, and troubleshooting issues encountered at runtime.
 *
 * @author @horaciovelvetine
 * @version 1.0
 * @since 1.0
 * @see ServiceFault
 * @see WikidataServiceErr
 */
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public sealed interface WikiverseError permits WikiverseError.ServiceFault, WikiverseError.WikidataServiceError {
	/**
	 * Provides a descriptive error message that helps with debugging and
	 * troubleshooting.
	 * The message should be concise yet informative, providing context about what
	 * went wrong.
	 *
	 * @return a descriptive error message, never null or empty
	 */
	public String message();

	/**
	 * Provides the originating function, scope, or location in the codebase where
	 * the error occurred.
	 * This helps developers quickly identify the source of the problem and trace
	 * the error back
	 * to its origin for debugging purposes.
	 *
	 * @return the source location or function name where the error originated,
	 *         never null or empty
	 */
	public String source();

	/**
	 * Provides the timestamp when the error occurred.
	 *
	 * @return the instant when the error was created
	 */
	public Instant timestamp();

	/**
	 * Provides the error category for classification and filtering.
	 *
	 * @return the error category
	 */
	public ErrorCategory category();

	/**
	 * Provides the HTTP status code for this error.
	 *
	 * @return the HTTP status code, never null
	 */
	public Integer httpStatusCode();

	/**
	 * Provides the stack trace for debugging purposes.
	 *
	 * @return the stack trace, never null
	 */
	public StackTraceElement[] stackTrace();

	/**
	 * Represents the categories for error classification and filtering.
	 */
	public enum ErrorCategory {
		/** Network-related errors (connectivity, timeouts, etc.) */
		NETWORK,
		/** Input validation errors */
		VALIDATION,
		/** Data processing errors */
		PROCESSING,
		/** Authentication errors */
		CONFIGURATION,
		/** External service errors */
		EXTERNAL_SERVICE,
		/** Internal logic errors */
		INTERNAL_LOGIC,
		/** Resource not found errors */
		NOT_FOUND,
		/** Rate limiting errors */
		RATE_LIMITED
	}

	/**
	 * Represents a generic service-level error that serves as a catch-all for
	 * various
	 * service-related issues that don't fit into more specific error categories.
	 *
	 * <p>
	 * This record implements the enhanced error standard by providing comprehensive
	 * error details including timestamps and contextual information.
	 * It can be used for general service failures, configuration issues, or other
	 * unexpected service-level problems.
	 *
	 * @param message        a descriptive error message explaining what went wrong
	 * @param source         the location or function where the error occurred
	 * @param timestamp      the instant when the error occurred
	 * @param category       the error category for classification
	 * @param httpStatusCode the HTTP status code for this error
	 * @param stackTrace     the stack trace for debugging
	 *
	 * @author @horaciovelvetine
	 * @version 1.0
	 * @since 1.0
	 */
	public record ServiceFault(
		String message,
		String source,
		Instant timestamp,
		ErrorCategory category,
		Integer httpStatusCode,
		StackTraceElement[] stackTrace
	) implements WikiverseError {
		/**
		 * Convenience constructor with minimal required parameters.
		 * Uses default values for HTTP status code and stack trace, generates
		 * timestamp.
		 *
		 * @param message  a descriptive error message explaining what went wrong
		 * @param source   the location or function where the error occurred
		 * @param category the error category for classification
		 */
		public ServiceFault(String message, String source, ErrorCategory category) {
			this(message, source, Instant.now(), category, 500, new StackTraceElement[0]);
		}
	}

	/**
	 * Represents errors specific to Wikidata service operations and interactions.
	 * This sealed interface defines all error types that can occur when working
	 * with the Wikidata API or processing Wikidata-related data.
	 *
	 * <p>
	 * Common scenarios include:
	 * <ul>
	 * <li>API connectivity issues</li>
	 * <li>No matching results for queries</li>
	 * <li>Data processing failures</li>
	 * </ul>
	 *
	 * @author @horaciovelvetine
	 * @version 2.0
	 * @since 1.0
	 * @see APIOffline
	 * @see NoMatchingResultsFound
	 * @see UnableToProcessWikidataEntity
	 */
	sealed interface WikidataServiceError extends WikiverseError {
		/**
		 * Represents an error that occurs when the Wikidata API is unavailable or
		 * offline.
		 * This error is thrown when attempts to communicate with the Wikimedia API
		 * fail due to service unavailability, network issues, or maintenance.
		 *
		 * <p>
		 * This record provides comprehensive error information including
		 * network-specific
		 * context and debugging information.
		 *
		 * @param message        a descriptive error message explaining the API
		 *                       unavailability
		 * @param source         the location where the API call was attempted
		 * @param timestamp      the instant when the error occurred
		 * @param category       the error category for classification
		 * @param httpStatusCode the HTTP status code for this error
		 * @param stackTrace     the stack trace for debugging
		 *
		 * @author @horaciovelvetine
		 * @version 1.0
		 * @since 1.0
		 */
		record APIOffline(
			String message,
			String source,
			Instant timestamp,
			ErrorCategory category,
			Integer httpStatusCode,
			StackTraceElement[] stackTrace
		) implements WikidataServiceError {
			/**
			 * Convenience constructor with minimal required parameters.
			 * Uses default values for timestamp and category, generates timestamp.
			 *
			 * @param message    a descriptive error message explaining the API
			 *                   unavailability
			 * @param source     the location where the API call was attempted
			 * @param stackTrace the stack trace for debugging
			 */
			public APIOffline(String message, String source, StackTraceElement[] stackTrace) {
				this(message, source, Instant.now(), ErrorCategory.EXTERNAL_SERVICE, 503, stackTrace);
			}
		}

		/**
		 * Represents an error that occurs when a Wikidata query returns no matching
		 * results.
		 * This error indicates that the Wikimedia API is functioning correctly, but the
		 * provided search term or query did not match any entities in the Wikidata
		 * database.
		 *
		 * <p>
		 * The error message is automatically constructed using the query value that
		 * produced no results, making it easy to identify what was searched for.
		 *
		 * @param queryValue     the original search term or query that returned no
		 *                       results
		 * @param timestamp      the instant when the error occurred
		 * @param category       the error category for classification
		 * @param httpStatusCode the HTTP status code for this error
		 * @param stackTrace     the stack trace for debugging
		 *
		 * @author @horaciovelvetine
		 * @version 1.0
		 * @since 1.0
		 * @apiNote The queryValue is used to construct the message() and should be the
		 *          original provided search String
		 */
		record NoMatchingResultsFound(
			String queryValue,
			Instant timestamp,
			ErrorCategory category,
			Integer httpStatusCode,
			StackTraceElement[] stackTrace
		) implements WikidataServiceError {
			/**
			 * Convenience constructor with minimal required parameters.
			 * Uses default values for timestamp, category, HTTP status code, and stack
			 * trace.
			 *
			 * @param queryValue the original search term or query that returned no results
			 */
			public NoMatchingResultsFound(String queryValue) {
				this(queryValue, Instant.now(), ErrorCategory.NOT_FOUND, 404, new StackTraceElement[0]);
			}

			/**
			 * Returns the default source location for this error type.
			 *
			 * @return the source location where the query was executed
			 */
			@Override
			public String source() {
				return "WikidataFetchBroker.java";
			}

			/**
			 * Constructs and returns a descriptive error message using the query value.
			 *
			 * @return a formatted error message indicating no results were found for the
			 *         query
			 */
			@Override
			public String message() {
				return "No results found for: " + queryValue;
			}
		}

		/**
		 * Represents an error that occurs when a Wikidata document cannot be processed
		 * into the intended core model structure. This typically happens when the
		 * Wikidata response contains unexpected data format or structure that cannot
		 * be properly parsed or mapped to the application's data models.
		 *
		 * <p>
		 * This error is useful for debugging data transformation issues and
		 * identifying problematic Wikidata entities that may need special handling.
		 *
		 * @param message        a descriptive error message explaining why the entity
		 *                       could not be processed
		 * @param source         the location where the entity processing was attempted
		 * @param timestamp      the instant when the error occurred
		 * @param category       the error category for classification
		 * @param httpStatusCode the HTTP status code for this error
		 * @param stackTrace     the stack trace for debugging
		 *
		 * @author @horaciovelvetine
		 * @version 1.0
		 * @since 1.0
		 */
		record UnableToProcessWikidataEntity(
			String message,
			String source,
			Instant timestamp,
			ErrorCategory category,
			Integer httpStatusCode,
			StackTraceElement[] stackTrace
		) implements WikidataServiceError {
			/**
			 * Convenience constructor with minimal required parameters.
			 * Uses default values for timestamp, category, HTTP status code, and stack
			 * trace.
			 *
			 * @param message a descriptive error message explaining why the entity could
			 *                not be processed
			 * @param source  the location where the entity processing was attempted
			 */
			public UnableToProcessWikidataEntity(String message, String source) {
				this(message, source, Instant.now(), ErrorCategory.PROCESSING, 422, new StackTraceElement[0]);
			}
		}
	}
}
