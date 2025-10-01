package edu.velvet.Wikiverse.api.services.logging;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.wikidata.wdtk.datamodel.implementation.ItemDocumentImpl;
import org.wikidata.wdtk.datamodel.implementation.PropertyDocumentImpl;
import org.wikidata.wdtk.datamodel.implementation.TermedStatementDocumentImpl;
import org.wikidata.wdtk.datamodel.interfaces.EntityDocument;
import org.wikidata.wdtk.datamodel.interfaces.EntityIdValue;
import org.wikidata.wdtk.datamodel.interfaces.ItemDocument;
import org.wikidata.wdtk.datamodel.interfaces.PropertyDocument;
import org.wikidata.wdtk.datamodel.interfaces.StatementDocument;
import org.wikidata.wdtk.wikibaseapi.WbSearchEntitiesResult;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * A specialized logger for Wikidata document entities that provides structured
 * logging
 * capabilities for different types of Wikidata documents including items,
 * properties, and statements.
 *
 * <p>
 * This class implements the Mappable interface to provide JSON serialization
 * capabilities
 * and offers comprehensive logging functionality for Wikidata entities with the
 * following features:
 * <ul>
 * <li>Timestamped log entries with ISO format</li>
 * <li>Type-specific logging for ItemDocument, PropertyDocument, and
 * StatementDocument</li>
 * <li>JSON-first logging approach with fallback to basic string format</li>
 * <li>Performance timing for logging operations</li>
 * <li>Error handling and fallback logging for unknown document types</li>
 * <li>Automatic clearing of existing document files on startup for clean
 * runs</li>
 * </ul>
 *
 * <p>
 * The logger uses a persistent Logfile instance for storing log entries and
 * provides
 * console output for real-time monitoring of document processing operations.
 *
 * @author @horaciovelvetine
 * @version 1.0
 * @since 1.0
 * @see Logfile
 * @see Mappable
 * @see EntityDocument
 */
public class WikidataDocumentLogger implements Mappable {

	/** The underlying Logfile instance for persistent logging. */
	private final Logfile logfile;

	/** Whether to output verbose messages to console. */
	private final boolean verbose;

	/** The source identifier used for console output messages. */
	private static final String SOURCE = "WikidataDocumentLogger";

	/** The directory for storing individual document JSON files. */
	private static final String DOCS_DIR = "logs/docs/";

	/** Whether JSON file logging is enabled. */
	@Value("${wikiverse.api.logging.json-files.enabled:true}")
	private boolean jsonFileLoggingEnabled;

	/**
	 * Constructs a new WikidataDocumentLogger with the specified log file.
	 *
	 * <p>
	 * This constructor initializes the logger and ensures that the docs
	 * subdirectory
	 * exists for storing individual JSON document files (if JSON file logging is
	 * enabled). It also clears any existing document files from previous runs
	 * to ensure a clean start. If the directory creation fails, a warning message
	 * is printed but the logger continues to function with fallback behavior.
	 * Verbose console output is enabled by default.
	 *
	 * @param logfile the name of the log file to use for persistent
	 *                logging, cannot be null
	 * @throws IllegalArgumentException if logfile is null
	 */
	public WikidataDocumentLogger(String logfile) {
		this(logfile, true);
	}

	/**
	 * Constructs a new WikidataDocumentLogger with the specified log file and
	 * verbose mode.
	 *
	 * <p>
	 * This constructor initializes the logger and ensures that the docs
	 * subdirectory
	 * exists for storing individual JSON document files (if JSON file logging is
	 * enabled). It also clears any existing document files from previous runs
	 * to ensure a clean start. If the directory creation fails, a warning message
	 * is printed but the logger continues to function with fallback behavior.
	 * Console output can be controlled via the verbose parameter.
	 *
	 * @param logfile the name of the log file to use for persistent
	 *                logging, cannot be null
	 * @param verbose whether to output verbose messages to console
	 * @throws IllegalArgumentException if logfile is null
	 */
	public WikidataDocumentLogger(String logfile, boolean verbose) {
		if (logfile == null) {
			throw new IllegalArgumentException("Log file name cannot be null");
		}
		this.logfile = new Logfile(logfile, verbose);
		this.verbose = verbose;
		initializeDocsDirectory();
	}

	/**
	 * Initializes the docs directory for storing JSON document files.
	 *
	 * <p>
	 * This method ensures that the docs subdirectory exists before any logging
	 * operations, but only if JSON file logging is enabled in the current
	 * environment. It also clears any existing document files from previous runs
	 * to ensure a clean start. If the directory creation fails, a warning is
	 * printed but the logger continues to function with fallback behavior.
	 */
	private void initializeDocsDirectory() {
		if (!jsonFileLoggingEnabled) {
			print("JSON file logging disabled for current environment, skipping docs directory initialization");
			return;
		}

		try {
			Files.createDirectories(Paths.get(DOCS_DIR));
			clearExistingDocumentFiles();
			print("Docs directory initialized: " + DOCS_DIR);
		} catch (IOException e) {
			print("Warning: Could not create docs directory: " + e.getMessage());
		}
	}

	/**
	 * Clears all existing document files from the docs directory.
	 *
	 * <p>
	 * This method removes all JSON files from the logs/docs/ directory to ensure
	 * a clean start for each application run. It only operates if JSON file logging
	 * is enabled and the docs directory exists. If any files cannot be deleted,
	 * a warning is printed but the operation continues for other files.
	 *
	 * <p>
	 * The method handles the following scenarios:
	 * <ul>
	 * <li>Directory doesn't exist: No action taken (normal for first run)</li>
	 * <li>Directory exists but is empty: No action taken</li>
	 * <li>Directory contains files: All files are deleted</li>
	 * <li>Some files cannot be deleted: Warning printed, operation continues</li>
	 * </ul>
	 */
	private void clearExistingDocumentFiles() {
		if (!jsonFileLoggingEnabled) {
			return; // No need to clear if JSON file logging is disabled
		}

		try {
			java.nio.file.Path docsPath = Paths.get(DOCS_DIR);
			if (!Files.exists(docsPath)) {
				print("Docs directory does not exist, nothing to clear");
				return;
			}

			// Count files before deletion for reporting
			long fileCount = Files.list(docsPath)
					.filter(Files::isRegularFile)
					.filter(path -> path.toString().toLowerCase().endsWith(".json"))
					.count();

			if (fileCount == 0) {
				print("Docs directory is empty, nothing to clear");
				return;
			}

			// Delete all JSON files in the directory
			Files.list(docsPath)
					.filter(Files::isRegularFile)
					.filter(path -> path.toString().toLowerCase().endsWith(".json"))
					.forEach(path -> {
						try {
							Files.delete(path);
							print("Deleted existing document file: " + path.getFileName());
						} catch (IOException e) {
							print("Warning: Could not delete file " + path.getFileName() + ": " + e.getMessage());
						}
					});

			print("Cleared " + fileCount + " existing document files from docs directory");
		} catch (IOException e) {
			print("Warning: Could not access docs directory for clearing: " + e.getMessage());
		}
	}

	/**
	 * Logs a Wikidata document with the specified message and performance timing.
	 *
	 * <p>
	 * This method performs type-specific logging based on the document type:
	 * <ul>
	 * <li>ItemDocument: Serializes to JSON using logItemDoc()</li>
	 * <li>PropertyDocument: Serializes to JSON using logPropertyDoc()</li>
	 * <li>StatementDocument: Serializes to JSON using logStatementDoc()</li>
	 * <li>Other EntityDocument types: Logs basic entity information using
	 * logEntityDocID()</li>
	 * </ul>
	 *
	 * <p>
	 * The method includes performance timing and error handling. If the document is
	 * null,
	 * it logs a warning message but continues execution. If the message is null, it
	 * throws
	 * an IllegalArgumentException.
	 *
	 * @param message  the log message to associate with this document, cannot be
	 *                 null
	 * @param document the Wikidata document to log, can be null (will log warning)
	 * @throws IllegalArgumentException if message is null
	 */
	public EntityDocument log(String message, EntityDocument document) {
		if (document == null) {
			print("Document provided was null, nothing to log...");
			return document;
		}

		validateMessage(message);
		return executeLoggingWithTiming(message, "WikidataDocument", () -> {
			switch (document) {
				case ItemDocument itemDoc -> logItemDoc(itemDoc);
				case PropertyDocument propDoc -> logPropertyDoc(propDoc);
				case StatementDocument stmtDoc -> logStatementDoc(stmtDoc);
				default -> logEntityDocID(document);
			}
			return document;
		});
	}

	/**
	 * Logs a list of Wikidata search entities results with the specified message
	 * and performance timing.
	 *
	 * <p>
	 * This method logs detailed information about search results including:
	 * <ul>
	 * <li>Total count of search results</li>
	 * <li>JSON serialization of all search results to a single file (preferred
	 * format)</li>
	 * <li>Fallback to basic string format if JSON serialization fails</li>
	 * <li>Performance timing for the entire logging operation</li>
	 * </ul>
	 *
	 * <p>
	 * The method includes comprehensive error handling and will log a warning if
	 * the
	 * search results list is null, but will continue execution. If the message is
	 * null,
	 * it throws an IllegalArgumentException.
	 *
	 * @param message       the log message to associate with this search results
	 *                      list, cannot be null
	 * @param searchResults the list of Wikidata search entities results to log, can
	 *                      be null (will log warning)
	 * @throws IllegalArgumentException if message is null
	 */
	public List<WbSearchEntitiesResult> log(String message, List<WbSearchEntitiesResult> searchResults) {
		if (searchResults == null) {
			print("Search results list provided was null, nothing to log...");
			return searchResults;
		}

		validateMessage(message);
		return executeLoggingWithTiming(message, "Wikidata Search Results", () -> {
			int resultCount = searchResults.size();
			logfile.write(String.format("Search Results Count: %d", resultCount));

			logSearchResultsAsSingleFile(searchResults, message);

			return searchResults;
		});
	}

	/**
	 * Logs basic entity information for unhandled EntityDocument types.
	 *
	 * <p>
	 * This method is used as a fallback when the document type is not specifically
	 * handled by the other logging methods. It extracts and logs the entity's basic
	 * identification information including ID, type, IRI, and site IRI.
	 *
	 * @param entityDoc the entity document to log, cannot be null
	 */
	private void logEntityDocID(EntityDocument entityDoc) {
		EntityIdValue entId = entityDoc.getEntityId();
		String logEntry = String.format(
				"Unhandled Entity Type - ID: %s, Type: %s, IRI: %s, Site IRI: %s",
				entId.getId(),
				entId.getEntityType(),
				entId.getIri(),
				entId.getSiteIri());
		logfile.write(logEntry);
	}

	/**
	 * Logs an ItemDocument by serializing it to JSON format.
	 *
	 * <p>
	 * This method attempts to serialize the ItemDocument to JSON using the mapper
	 * instance. If the document is an instance of ItemDocumentImpl, it will be
	 * serialized to JSON and logged. If serialization fails, an error message is
	 * logged.
	 * For non-ItemDocumentImpl instances, it falls back to logging basic entity
	 * information.
	 *
	 * @param itemDoc the ItemDocument to log, cannot be null
	 */
	private void logItemDoc(ItemDocument itemDoc) {
		logDocumentAsJson(itemDoc, ItemDocumentImpl.class, "ItemDocument");
	}

	/**
	 * Logs a PropertyDocument by serializing it to JSON format.
	 *
	 * <p>
	 * This method attempts to serialize the PropertyDocument to JSON using the
	 * mapper
	 * instance. If the document is an instance of PropertyDocumentImpl, it will be
	 * serialized to JSON and logged. If serialization fails, an error message is
	 * logged.
	 * For non-PropertyDocumentImpl instances, it falls back to logging basic entity
	 * information.
	 *
	 * @param propertyDoc the PropertyDocument to log, cannot be null
	 */
	private void logPropertyDoc(PropertyDocument propertyDoc) {
		logDocumentAsJson(propertyDoc, PropertyDocumentImpl.class, "PropertyDocument");
	}

	/**
	 * Logs a StatementDocument by serializing it to JSON format.
	 *
	 * <p>
	 * This method attempts to serialize the StatementDocument to JSON using the
	 * mapper
	 * instance. If the document is an instance of TermedStatementDocumentImpl, it
	 * will be
	 * serialized to JSON and logged. If serialization fails, an error message is
	 * logged.
	 * For non-TermedStatementDocumentImpl instances, it falls back to logging basic
	 * entity information.
	 *
	 * @param statementDoc the StatementDocument to log, cannot be null
	 */
	private void logStatementDoc(StatementDocument statementDoc) {
		logDocumentAsJson(statementDoc, TermedStatementDocumentImpl.class, "StatementDocument");
	}

	/**
	 * Validates that the message parameter is not null.
	 *
	 * @param message the message to validate
	 * @throws IllegalArgumentException if message is null
	 */
	private void validateMessage(String message) {
		if (message == null) {
			throw new IllegalArgumentException("Message cannot be null");
		}
	}

	/**
	 * Executes logging with timing and error handling.
	 *
	 * @param <T>           the return type
	 * @param message       the log message
	 * @param operationName the name of the operation being logged
	 * @param operation     the operation to execute
	 * @return the result of the operation
	 */
	private <T> T executeLoggingWithTiming(
			String message,
			String operationName,
			java.util.function.Supplier<T> operation) {
		String logMessage = String.format(
				"[%s] - %s",
				LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
				message);

		try {
			print("Logging " + operationName + ": " + message);
			logfile.write(logMessage + " - START");

			long startTime = System.currentTimeMillis();
			T result = operation.get();
			long duration = System.currentTimeMillis() - startTime;

			print(operationName + " Logged: " + message + " in " + duration + "ms");
			logfile.write(logMessage + " - " + operationName + " Logged in " + duration + "ms");
			return result;
		} catch (Exception e) {
			print("Execution failed: " + message + " - " + e.getMessage());
			logfile.write(logMessage + " - FAILED: " + e.getMessage());
			return operation.get(); // Return the result even if logging failed
		}
	}

	/**
	 * Logs a document as JSON if it's an instance of the expected implementation
	 * class.
	 *
	 * <p>
	 * This method attempts to serialize the document to JSON format and writes it
	 * to a separate file in the docs directory (if JSON file logging is enabled).
	 * If the document is not an instance of the expected class or JSON
	 * serialization fails,
	 * it falls back to logging basic entity information using logEntityDocID().
	 *
	 * @param <T>           the document type
	 * @param document      the document to log
	 * @param expectedClass the expected implementation class
	 * @param documentType  the document type name for logging
	 */
	private <T extends EntityDocument> void logDocumentAsJson(T document, Class<?> expectedClass, String documentType) {
		if (expectedClass.isInstance(document)) {
			try {
				String jsonString = Mappable.mapper.writeValueAsString(document);

				if (jsonFileLoggingEnabled) {
					String fileName = writeJsonToFile(document, documentType, jsonString);
					String summary = createDocumentSummary(document, documentType, fileName);
					logfile.write(summary);
				} else {
					// JSON file logging disabled, just log basic summary
					String summary = createDocumentSummaryWithoutFile(document, documentType);
					logfile.write(summary);
				}
			} catch (JsonProcessingException e) {
				logfile.write("Failed to serialize " + documentType + " to JSON: " + e.getMessage());
				logEntityDocID(document);
			} catch (IOException e) {
				logfile.write("Failed to write " + documentType + " JSON to file: " + e.getMessage());
				logEntityDocID(document);
			}
		} else {
			logEntityDocID(document);
		}
	}

	/**
	 * Logs all search results to a single JSON file.
	 *
	 * <p>
	 * This method attempts to serialize all search results to JSON and write them
	 * to a single file in the docs directory (if JSON file logging is enabled).
	 * If JSON serialization fails, it falls back to logging basic string
	 * information for each result.
	 *
	 * @param searchResults the list of search results to log
	 * @param message       the log message for context
	 */
	private void logSearchResultsAsSingleFile(List<WbSearchEntitiesResult> searchResults, String message) {
		try {
			String jsonString = Mappable.mapper.writeValueAsString(searchResults);

			if (jsonFileLoggingEnabled) {
				String fileName = writeSearchResultsToFile(searchResults, message, jsonString);
				String summary = String.format(
						"All %d search results stored in single JSON file: %s",
						searchResults.size(),
						fileName);
				logfile.write(summary);
			} else {
				// JSON file logging disabled, just log basic summary for each result
				for (int i = 0; i < searchResults.size(); i++) {
					WbSearchEntitiesResult result = searchResults.get(i);
					String summary = String.format(
							"Result %d/%d - Entity ID: %s, Title: %s, Label: %s, Description: %s",
							i + 1,
							searchResults.size(),
							result.getEntityId(),
							result.getTitle(),
							result.getLabel(),
							result.getDescription());
					logfile.write(summary);
				}
			}
		} catch (JsonProcessingException e) {
			// Fallback to basic string information if JSON serialization fails
			logfile.write("Failed to serialize search results to JSON: " + e.getMessage());
			for (int i = 0; i < searchResults.size(); i++) {
				WbSearchEntitiesResult result = searchResults.get(i);
				String basicInfo = String.format(
						"Result %d/%d - Entity ID: %s, Page ID: %d, Title: %s, Label: %s, Description: %s, URL: %s, Concept URI: %s",
						i + 1,
						searchResults.size(),
						result.getEntityId(),
						result.getPageId(),
						result.getTitle(),
						result.getLabel(),
						result.getDescription(),
						result.getUrl(),
						result.getConceptUri());
				logfile.write(basicInfo);
			}
		} catch (IOException e) {
			// Fallback to basic string information if file writing fails
			logfile.write("Failed to write search results JSON to file: " + e.getMessage());
			for (int i = 0; i < searchResults.size(); i++) {
				WbSearchEntitiesResult result = searchResults.get(i);
				String basicInfo = String.format(
						"Result %d/%d - Entity ID: %s, Page ID: %d, Title: %s, Label: %s, Description: %s, URL: %s, Concept URI: %s",
						i + 1,
						searchResults.size(),
						result.getEntityId(),
						result.getPageId(),
						result.getTitle(),
						result.getLabel(),
						result.getDescription(),
						result.getUrl(),
						result.getConceptUri());
				logfile.write(basicInfo);
			}
		}
	}

	/**
	 * Writes JSON data to a separate file in the docs directory.
	 *
	 * <p>
	 * This method creates a unique filename based on the document's entity ID and
	 * type, then writes the JSON data to a file in the logs/docs/ directory.
	 * The docs directory should already exist from initialization.
	 *
	 * @param <T>          the document type
	 * @param document     the document being logged
	 * @param documentType the type of document for filename generation
	 * @param jsonString   the JSON string to write to file
	 * @return the filename where the JSON was written
	 * @throws IOException if there's an error writing the file
	 */
	private <T extends EntityDocument> String writeJsonToFile(T document, String documentType, String jsonString)
			throws IOException {
		// Generate unique filename based on entity ID and timestamp
		EntityIdValue entityId = document.getEntityId();
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
		String fileName = String.format(
				"%s_%s_%s.json",
				documentType.toLowerCase(),
				entityId.getId().replace(":", "_"),
				timestamp);

		String filePath = DOCS_DIR + fileName;

		try (FileWriter writer = new FileWriter(filePath)) {
			writer.write(jsonString);
		}

		print("JSON written to file: " + filePath);
		return fileName;
	}

	/**
	 * Writes all search results JSON data to a single file in the docs directory.
	 *
	 * <p>
	 * This method creates a unique filename based on the search context and
	 * timestamp,
	 * then writes the JSON data containing all search results to a file in the
	 * logs/docs/ directory. The docs directory should already exist from
	 * initialization.
	 *
	 * @param searchResults the list of search results being logged
	 * @param message       the log message for context in filename
	 * @param jsonString    the JSON string to write to file
	 * @return the filename where the JSON was written
	 * @throws IOException if there's an error writing the file
	 */
	private String writeSearchResultsToFile(
			List<WbSearchEntitiesResult> searchResults,
			String message,
			String jsonString) throws IOException {
		// Generate unique filename based on message context and timestamp
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS"));
		String sanitizedMessage = message.replaceAll("[^a-zA-Z0-9]", "_").toLowerCase();
		String fileName = String.format(
				"searchresults_%s_%ditems_%s.json",
				sanitizedMessage,
				searchResults.size(),
				timestamp);

		String filePath = DOCS_DIR + fileName;

		try (FileWriter writer = new FileWriter(filePath)) {
			writer.write(jsonString);
		}

		print("All search results JSON written to file: " + filePath);
		return fileName;
	}

	/**
	 * Creates a summary of the document for logging to the main logfile.
	 *
	 * <p>
	 * This method generates a concise summary containing the document type,
	 * entity ID, and the filename where the full JSON data is stored.
	 *
	 * @param <T>          the document type
	 * @param document     the document being logged
	 * @param documentType the type of document
	 * @param fileName     the filename where the JSON data was written
	 * @return a summary string for logging
	 */
	private <T extends EntityDocument> String createDocumentSummary(T document, String documentType, String fileName) {
		EntityIdValue entityId = document.getEntityId();
		return String.format(
				"%s - ID: %s, Type: %s, JSON stored in: %s",
				documentType,
				entityId.getId(),
				entityId.getEntityType(),
				fileName);
	}

	/**
	 * Creates a summary of the document for logging to the main logfile without
	 * file reference.
	 *
	 * <p>
	 * This method generates a concise summary containing the document type and
	 * entity ID
	 * when JSON file logging is disabled.
	 *
	 * @param <T>          the document type
	 * @param document     the document being logged
	 * @param documentType the type of document
	 * @return a summary string for logging
	 */
	private <T extends EntityDocument> String createDocumentSummaryWithoutFile(T document, String documentType) {
		EntityIdValue entityId = document.getEntityId();
		return String.format(
				"%s - ID: %s, Type: %s (JSON file logging disabled)",
				documentType,
				entityId.getId(),
				entityId.getEntityType());
	}

	/**
	 * Prints a message to the standard output stream with a source identifier
	 * prefix.
	 * <p>
	 * This method provides a simple way to output messages to the standard output
	 * stream, typically used for providing helpful feedback about process execution
	 * status, timing information, and error reporting. The output is controlled by
	 * the verbose setting.
	 *
	 * @param message the message to output to the console (can be null)
	 */
	private void print(String message) {
		if (verbose) {
			System.out.println("[" + SOURCE + "] " + message);
		}
	}
}
