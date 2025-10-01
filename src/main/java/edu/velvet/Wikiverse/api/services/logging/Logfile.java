package edu.velvet.Wikiverse.api.services.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A thread-safe logging utility that writes messages to files with automatic
 * log rotation.
 * This class provides file-based logging capabilities with configurable file
 * size limits
 * and automatic backup creation when the log file exceeds the maximum size.
 *
 * <p>
 * The Logfile class provides the following features:
 * <ul>
 * <li>Thread-safe file writing with synchronized methods</li>
 * <li>Automatic log file rotation when size limit is exceeded</li>
 * <li>Configurable maximum file size with default of 100MB</li>
 * <li>Timestamped backup file creation during rotation</li>
 * <li>Error handling with both console output and SLF4J logging</li>
 * <li>Flexible log file path configuration</li>
 * </ul>
 *
 * <p>
 * Usage example:
 *
 * <pre>{@code
 * Logfile logger = new Logfile("application.log");
 * logger.write("Application started successfully");
 * }</pre>
 *
 * <p>
 * When the log file exceeds the maximum size, it will be automatically rotated
 * to a backup file with a timestamp suffix (e.g.,
 * "application.log.20231215_143022").
 *
 * @author @horaciovelvetine
 * @version 1.0
 * @since 1.0
 * @see Logger
 * @see BufferedWriter
 * @see FileWriter
 */
public class Logfile {

	/** Default directory for log files. */
	private static final String LOGFILE_DIR = "logs/";

	/** Default maximum file size in bytes (100MB). */
	private static final int MAX_FILE_SIZE = 100 * 1024 * 1024; // 100MB

	/** SLF4J logger instance for error logging. */
	private static final Logger logger = LoggerFactory.getLogger(FileWriter.class);

	/** The absolute path to the log file. */
	private final String logFile;

	/** Whether to output verbose messages to console. */
	private final boolean verbose;

	/**
	 * Constructs a new Logfile instance with the specified log file name.
	 *
	 * <p>
	 * The constructor performs the following operations:
	 * <ul>
	 * <li>If logFile is null or empty, uses "application.log" in the logs
	 * directory</li>
	 * <li>If logFile is an absolute path, uses it as-is</li>
	 * <li>If logFile is a relative path, combines it with the logs directory</li>
	 * <li>Creates the logs directory if it doesn't exist (for relative paths)</li>
	 * <li>Clears the contents of any existing log file</li>
	 * <li>Sets the maximum file size to the default value (100MB)</li>
	 * <li>Enables verbose console output by default</li>
	 * </ul>
	 *
	 * @param logFile the name of the log file (can be null or empty for default)
	 */
	public Logfile(String logFile) {
		this(logFile, true);
	}

	/**
	 * Constructs a new Logfile instance with the specified log file name and
	 * verbose mode.
	 *
	 * <p>
	 * The constructor performs the following operations:
	 * <ul>
	 * <li>If logFile is null or empty, uses "application.log" in the logs
	 * directory</li>
	 * <li>If logFile is an absolute path, uses it as-is</li>
	 * <li>If logFile is a relative path, combines it with the logs directory</li>
	 * <li>Creates the logs directory if it doesn't exist (for relative paths)</li>
	 * <li>Clears the contents of any existing log file</li>
	 * <li>Sets the maximum file size to the default value (100MB)</li>
	 * <li>Sets verbose console output based on the verbose parameter</li>
	 * </ul>
	 *
	 * @param logFile the name of the log file (can be null or empty for default)
	 * @param verbose whether to output verbose messages to console
	 */
	public Logfile(String logFile, boolean verbose) {
		// Determine the log file path
		if (logFile == null || logFile.isEmpty()) {
			// Use default log file in logs directory
			try {
				Files.createDirectories(Paths.get(LOGFILE_DIR));
			} catch (IOException e) {
				print("Warning: Could not create logs directory: " + e.getMessage());
			}
			this.logFile = Paths.get(LOGFILE_DIR, "application.log").toString();
		} else if (Paths.get(logFile).isAbsolute()) {
			// Use absolute path as-is
			this.logFile = logFile;
		} else {
			// Use relative path in logs directory
			try {
				Files.createDirectories(Paths.get(LOGFILE_DIR));
			} catch (IOException e) {
				print("Warning: Could not create logs directory: " + e.getMessage());
			}
			this.logFile = Paths.get(LOGFILE_DIR, logFile).toString();
		}
		this.verbose = verbose;

		// Clear existing log file contents if it exists
		clearExistingLogFile();

		print("Logfile initialized see: logs/" + this.logFile + " (max size: " + MAX_FILE_SIZE + " bytes)");
	}

	/**
	 * Writes a message to the log file in a thread-safe manner.
	 *
	 * <p>
	 * This method performs the following operations:
	 * <ul>
	 * <li>Appends the message to the log file with a newline character</li>
	 * <li>Checks if the file size exceeds the maximum allowed size</li>
	 * <li>Automatically rotates the log file if the size limit is exceeded</li>
	 * <li>Handles I/O errors gracefully with both console output and SLF4J
	 * logging</li>
	 * </ul>
	 *
	 * <p>
	 * The method is synchronized to ensure thread safety when multiple threads
	 * attempt to write to the same log file simultaneously.
	 *
	 * @param message the message to write to the log file (can be null, will be
	 *                converted to "null")
	 * @throws RuntimeException if an I/O error occurs during file writing
	 */
	public synchronized void write(String message) {
		print("Writing to log file: " + (message != null ? message : "null"));

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
			writer.write(message != null ? message : "null");
			writer.newLine();

			long currentFileSize = new File(logFile).length();
			print(
					"Message written successfully. Current file size: " +
							currentFileSize +
							" bytes (limit: " +
							MAX_FILE_SIZE +
							" bytes)");

			if (currentFileSize > MAX_FILE_SIZE) {
				print("File size exceeded limit! Initiating log rotation...");
				rotateLogFile();
			}
		} catch (IOException e) {
			print("Failed to write to log file! " + e.getMessage());
			logger.error("Failed to write to log file: " + e.getMessage(), e);
		}
	}

	/**
	 * Rotates the current log file by moving it to a timestamped backup file.
	 *
	 * <p>
	 * This method performs the following operations:
	 * <ul>
	 * <li>Generates a timestamp in the format "yyyyMMdd_HHmmss"</li>
	 * <li>Creates a backup filename by appending the timestamp to the original log
	 * file name</li>
	 * <li>Moves the current log file to the backup location</li>
	 * <li>Handles I/O errors gracefully with both console output and SLF4J
	 * logging</li>
	 * </ul>
	 *
	 * <p>
	 * The method is synchronized to ensure thread safety during log rotation.
	 * After rotation, subsequent write operations will create a new log file.
	 *
	 * <p>
	 * Example: If the log file is "application.log" and rotation occurs on
	 * December 15, 2023 at 2:30:22 PM, the backup file will be named
	 * "application.log.20231215_143022".
	 */
	private synchronized void rotateLogFile() {
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
		String backupFile = logFile + "." + timestamp;

		print("Creating backup file: " + backupFile);

		// Add milliseconds to make the filename unique
		int counter = 1;
		while (Files.exists(Paths.get(backupFile))) {
			backupFile = logFile + "." + timestamp + "_" + counter;
			counter++;
			print("Backup file already exists, trying: " + backupFile);
		}

		try {
			print("Moving current log file to backup: " + logFile + " -> " + backupFile);
			Files.move(Paths.get(logFile), Paths.get(backupFile));
			print("Log rotation completed successfully. Backup created: " + backupFile);
		} catch (IOException e) {
			print("Failed to rotate log file: " + e.getMessage());
			logger.error("Failed to rotate log file: " + e.getMessage(), e);
		}
	}

	/**
	 * Clears the contents of an existing log file if it exists.
	 *
	 * <p>
	 * This method performs the following operations:
	 * <ul>
	 * <li>Checks if the log file exists</li>
	 * <li>If the file exists, truncates it to zero length (clears all
	 * contents)</li>
	 * <li>Handles I/O errors gracefully with both console output and SLF4J
	 * logging</li>
	 * </ul>
	 *
	 * <p>
	 * This method is called during construction to ensure that each new Logfile
	 * instance starts with a clean log file, preventing accumulation of old log
	 * entries from previous application runs.
	 */
	private void clearExistingLogFile() {
		File file = new File(logFile);
		if (file.exists()) {
			try {
				// Truncate the file to zero length to clear its contents
				Files.write(Paths.get(logFile), new byte[0]);
				print("Cleared existing log file: " + logFile);
			} catch (IOException e) {
				print("Warning: Could not clear existing log file: " + e.getMessage());
				logger.warn("Could not clear existing log file: " + e.getMessage(), e);
			}
		}
	}

	/**
	 * Helper method to output messages to the console for debugging and error
	 * reporting.
	 *
	 * <p>
	 * This method provides a simple way to output messages to the standard output
	 * stream, typically used for error messages and debugging information when
	 * file operations fail. The output is controlled by the verbose setting.
	 *
	 * @param message the message to output to the console (can be null)
	 */
	private void print(String message) {
		if (verbose) {
			System.out.println(message);
		}
	}
}
