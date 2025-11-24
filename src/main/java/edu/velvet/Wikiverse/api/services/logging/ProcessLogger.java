package edu.velvet.Wikiverse.api.services.logging;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

/**
 * A process logging utility that provides method-level execution tracking and
 * timing.
 * This class wraps method execution with comprehensive logging capabilities
 * including
 * start/completion timestamps, execution duration, error handling, and stack
 * trace information.
 *
 * <p>
 * The ProcessLogger class provides the following features:
 * <ul>
 * <li>Execution timing with millisecond precision</li>
 * <li>Comprehensive logging of method start, completion, and failure
 * states</li>
 * <li>Support for both void (Runnable) and return value (Supplier/Callable)
 * methods</li>
 * <li>Exception handling with graceful error logging</li>
 * <li>Integration with the Logfile class for persistent logging</li>
 * <li>Real-time console output for execution status and timing</li>
 * </ul>
 *
 * <p>
 * Usage examples:
 *
 * <pre>{@code
 * ProcessLogger logger = new ProcessLogger("process.log");
 *
 * // For void methods
 * logger.log("Processing user data", () -> {
 * 	// Your processing logic here
 * 	processUserData();
 * });
 *
 * // For methods with return values
 * String result = logger.log("Fetching data", () -> {
 * 	return fetchDataFromAPI();
 * });
 * }</pre>
 *
 * <p>
 * Credit to Roman Glushach for the writeup/code for this process logger which
 * was adapted to work here
 * <a href=
 * "https://romanglushach.medium.com/java-rest-api-logging-best-practices-and-guidelines-bf5982ee4180"/>
 *
 * <p>
 * Each logged operation includes start time, completion time, duration, and any
 * error messages if exceptions occur.
 *
 * @author @horaciovelvetine
 * @version 1.0
 * @since 1.0
 * @see Logfile
 * @see StackWalker
 * @see Callable
 * @see Supplier
 * @see Runnable
 */
public class ProcessLogger {

	/** The underlying Logfile instance for persistent logging. */
	private final Logfile logfile;

	/** Whether to output verbose messages to console. */
	private final boolean verbose;

	/**
	 * Constructs a new ProcessLogger instance with the specified log file.
	 *
	 * <p>
	 * This constructor creates a ProcessLogger that will write all execution logs
	 * to the
	 * specified log file. The log file will be managed by an internal Logfile
	 * instance with
	 * automatic rotation when the maximum file size is exceeded. Verbose console
	 * output is enabled by default.
	 *
	 * @param logFile the name of the log file for storing execution logs
	 *                (cannot be null)
	 * @throws IllegalArgumentException if logFile is null
	 * @see Logfile#Logfile(String)
	 */
	public ProcessLogger(String logFile) {
		this(logFile, true);
	}

	/**
	 * Constructs a new ProcessLogger instance with the specified log file and
	 * verbose mode.
	 *
	 * <p>
	 * This constructor creates a ProcessLogger that will write all execution logs
	 * to the
	 * specified log file. The log file will be managed by an internal Logfile
	 * instance with
	 * automatic rotation when the maximum file size is exceeded. Console output
	 * can be controlled via the verbose parameter.
	 *
	 * @param logFile the name of the log file for storing execution logs
	 *                (cannot be null)
	 * @param verbose whether to output verbose messages to console
	 * @throws IllegalArgumentException if logFile is null
	 * @see Logfile#Logfile(String, boolean)
	 */
	public ProcessLogger(String logFile, boolean verbose) {
		if (logFile == null) {
			throw new IllegalArgumentException("Log file name cannot be null");
		}
		this.logfile = new Logfile(logFile, verbose);
		this.verbose = verbose;
	}

	/**
	 * Executes a callable function with comprehensive logging and timing.
	 *
	 * <p>
	 * This private method provides the core execution logic for all logging
	 * operations.
	 * It performs the following operations:
	 * <ul>
	 * <li>Creates a timestamped log message with custom message</li>
	 * <li>Outputs execution start message to console and logs to file</li>
	 * <li>Records execution start time in milliseconds</li>
	 * <li>Executes the provided callable function</li>
	 * <li>Records execution end time and calculates duration</li>
	 * <li>Outputs completion message to console and logs to file with duration</li>
	 * <li>Handles exceptions gracefully with console and file error logging</li>
	 * </ul>
	 *
	 * <p>
	 * In case of exceptions, the method logs the error message but does not
	 * re-throw
	 * the exception, instead returning null. This allows for graceful error
	 * handling
	 * while maintaining execution flow.
	 *
	 * @param <R>     the return type of the callable function
	 * @param message the custom message to include in log entries (cannot be null)
	 * @param fn      the callable function to execute (cannot be null)
	 * @return the result of the callable function, or null if an exception occurs
	 * @throws IllegalArgumentException if message or fn is null
	 * @see Callable#call()
	 */
	private <R> R execute(String message, Callable<R> fn) {
		if (message == null) {
			throw new IllegalArgumentException("Message cannot be null");
		}
		if (fn == null) {
			throw new IllegalArgumentException("Callable function cannot be null");
		}

		String logMessage = String.format(
			"[%s] - %s",
			LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
			message
		);

		try {
			print("Starting execution: " + message);
			logfile.write(logMessage + " - START");
			long startTime = System.currentTimeMillis();
			R result = fn.call();
			long endTime = System.currentTimeMillis();
			long duration = endTime - startTime;

			print("Execution completed: " + message + " in " + duration + " ms");
			logfile.write(logMessage + " - COMPLETED in " + duration + " ms");
			return result;
		} catch (Exception e) {
			print("Execution failed: " + message + " - " + e.getMessage());
			logfile.write(logMessage + " - FAILED: " + e.getMessage());
			return null;
		}
	}

	/**
	 * Logs the execution of a void (Runnable) method with timing and error
	 * handling.
	 *
	 * <p>
	 * This method provides a convenient way to wrap void methods with comprehensive
	 * logging.
	 * It automatically captures the calling method name, logs the start and
	 * completion of
	 * execution, measures execution time, and handles any exceptions that may
	 * occur.
	 *
	 * <p>
	 * The method performs the following operations:
	 * <ul>
	 * <li>Wraps the Runnable in a Callable that returns null</li>
	 * <li>Delegates to the execute method for actual logging and execution</li>
	 * <li>Logs start time with "START" suffix</li>
	 * <li>Executes the provided Runnable</li>
	 * <li>Logs completion time with duration in milliseconds</li>
	 * <li>Handles exceptions gracefully with error logging</li>
	 * </ul>
	 *
	 * <p>
	 * Usage example:
	 *
	 * <pre>{@code
	 * ProcessLogger logger = new ProcessLogger("process.log");
	 * logger.log("Processing user data", () -> {
	 * 	// Your void method logic here
	 * 	processUserData();
	 * 	updateUserStatus();
	 * });
	 * }</pre>
	 *
	 * @param message the custom message to include in log entries (cannot be null)
	 * @param fn      the Runnable to execute (cannot be null)
	 * @throws IllegalArgumentException if message or fn is null
	 * @see Runnable#run()
	 * @see #execute(String, Callable)
	 */
	public void log(String message, Runnable fn) {
		if (fn == null) {
			throw new IllegalArgumentException("Runnable function cannot be null");
		}
		print("ProcessLogger.log(): \"" + message + "\"");
		execute(message, () -> {
			fn.run();
			return null;
		});
	}

	/**
	 * Logs the execution of a method with return value (Supplier) with timing and
	 * error handling.
	 *
	 * <p>
	 * This method provides a convenient way to wrap methods that return values with
	 * comprehensive logging. It automatically captures the calling method name,
	 * logs the
	 * start and completion of execution, measures execution time, and handles any
	 * exceptions
	 * that may occur.
	 *
	 * <p>
	 * The method performs the following operations:
	 * <ul>
	 * <li>Converts the Supplier to a Callable using method reference</li>
	 * <li>Delegates to the execute method for actual logging and execution</li>
	 * <li>Logs start time with "START" suffix</li>
	 * <li>Executes the provided Supplier and captures the return value</li>
	 * <li>Logs completion time with duration in milliseconds</li>
	 * <li>Returns the result or null if an exception occurs</li>
	 * <li>Handles exceptions gracefully with error logging</li>
	 * </ul>
	 *
	 * <p>
	 * Usage example:
	 *
	 * <pre>{@code
	 * ProcessLogger logger = new ProcessLogger("process.log");
	 * String result = logger.log("Fetching user data", () -> {
	 * 	// Your method logic here that returns a value
	 * 	return fetchUserDataFromAPI();
	 * });
	 * }</pre>
	 *
	 * @param <R>     the return type of the supplier function
	 * @param message the custom message to include in log entries (cannot be null)
	 * @param fn      the Supplier to execute (cannot be null)
	 * @return the result of the supplier function, or null if an exception occurs
	 * @throws IllegalArgumentException if message or fn is null
	 * @see Supplier#get()
	 * @see #execute(String, Callable)
	 */
	public <R> R log(String message, Supplier<R> fn) {
		if (fn == null) {
			throw new IllegalArgumentException("Supplier function cannot be null");
		}
		print("ProcessLogger.log(): \"" + message + "\"");
		return execute(message, fn::get);
	}

	/**
	 * Logs exception details to both console and logfile with comprehensive error
	 * information.
	 *
	 * <p>
	 * This method provides a convenient way to log exception details including
	 * the source method, exception message, class name, and stack trace. It formats
	 * the error information with timestamps and writes it to both the console for
	 * immediate feedback and the logfile for persistent storage.
	 *
	 * <p>
	 * The method performs the following operations:
	 * <ul>
	 * <li>Creates a timestamped log message with error prefix and source
	 * method</li>
	 * <li>Outputs error details to console with source, exception class and
	 * message</li>
	 * <li>Writes comprehensive error information to logfile including stack
	 * trace</li>
	 * <li>Handles null exceptions gracefully by logging a generic error
	 * message</li>
	 * </ul>
	 *
	 * <p>
	 * Usage example:
	 *
	 * <pre>{@code
	 * ProcessLogger logger = new ProcessLogger("process.log");
	 * try {
	 * 	// Some operation that might throw an exception
	 * 	riskyOperation();
	 * } catch (Exception e) {
	 * 	logger.logError("riskyOperation", e);
	 * }
	 * }</pre>
	 *
	 * @param source    the method or operation that threw the exception
	 * @param exception the exception to log (can be null)
	 * @see Throwable#getMessage()
	 * @see Throwable#getClass()
	 * @see Throwable#printStackTrace()
	 */
	public void logError(String source, Exception exception) {
		if (source == null) {
			source = "No Source Provided for the Exception";
			print("No Source Provided for the Error to log");
		}
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		String logMessage = String.format("[%s] - ERROR in %s", timestamp, source);

		if (exception == null) {
			String nullError = "Exception object is null";
			print("ERROR in " + source + ": " + nullError);
			logfile.write(logMessage + " - " + nullError);
			return;
		}

		String errorDetails = String.format(
			"%s: %s",
			exception.getClass().getSimpleName(),
			exception.getMessage() != null ? exception.getMessage() : "No message available"
		);

		print("ERROR in " + source + ": " + errorDetails);
		logfile.write(logMessage + " - " + errorDetails);

		// Log stack trace to file
		StringBuilder stackTrace = new StringBuilder();
		stackTrace.append(logMessage).append(" - Stack Trace:\n");
		for (StackTraceElement element : exception.getStackTrace()) {
			stackTrace.append("  at ").append(element.toString()).append("\n");
		}
		logfile.write(stackTrace.toString());
	}

	/**
	 * Logs an informational message to both console and logfile with timestamp.
	 *
	 * <p>
	 * This method provides a lightweight way to log informational messages during
	 * process execution without wrapping code in execution blocks. It is ideal for
	 * tracking step-by-step progress, state changes, and debugging details. The
	 * message is formatted with a timestamp and written to both the console (if
	 * verbose mode is enabled) and the logfile for persistent storage.
	 *
	 * <p>
	 * The method performs the following operations:
	 * <ul>
	 * <li>Creates a timestamped log message with INFO prefix</li>
	 * <li>Outputs the message to console if verbose mode is enabled</li>
	 * <li>Writes the message to the logfile for persistent storage</li>
	 * </ul>
	 *
	 * <p>
	 * Usage example:
	 *
	 * <pre>{@code
	 * ProcessLogger logger = new ProcessLogger("process.log");
	 * logger.logInfo("Starting iteration " + iterationCount);
	 * logger.logInfo("Temperature: " + temperature);
	 * logger.logInfo("Completed repulsion calculations for vertex: " + vertexId);
	 * }</pre>
	 *
	 * @param message the informational message to log (cannot be null)
	 * @throws IllegalArgumentException if message is null
	 */
	public void logInfo(String message) {
		if (message == null) {
			throw new IllegalArgumentException("Message cannot be null");
		}
		String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		String logMessage = String.format("[%s] - INFO: %s", timestamp, message);
		print("INFO: " + message);
		logfile.write(logMessage);
	}

	/**
	 * Helper method to output messages to the console for debugging and user
	 * feedback.
	 *
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
			System.out.println("[ProcessLogger] " + message);
		}
	}
}
