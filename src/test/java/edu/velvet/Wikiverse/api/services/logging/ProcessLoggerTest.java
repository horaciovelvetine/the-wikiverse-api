package edu.velvet.Wikiverse.api.services.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Comprehensive test suite for the ProcessLogger class.
 * Tests all public methods including constructor validation, method execution
 * logging, timing calculations, and error handling functionality.
 *
 * <p>
 * This test class verifies all functionality of the ProcessLogger class
 * including:
 * <ul>
 * <li>Constructor behavior with various parameters</li>
 * <li>Runnable method logging functionality</li>
 * <li>Supplier method logging functionality</li>
 * <li>Execution timing and duration measurement</li>
 * <li>Exception handling and error logging</li>
 * <li>Integration with Logfile class</li>
 * <li>Edge cases and boundary conditions</li>
 * </ul>
 *
 * <p>
 * Tests use temporary directories to avoid conflicts with existing files
 * and ensure clean test isolation. The tests verify both successful execution
 * and error handling scenarios.
 *
 * @author @horaciovelvetine
 * @version 1.0
 * @since 1.0
 * @see ProcessLogger
 */
public class ProcessLoggerTest {

	@TempDir
	Path tempDir;

	private ProcessLogger processLogger;
	private String testLogFile;

	/**
	 * Sets up test environment before each test method.
	 * Creates a temporary log file path and ProcessLogger instance for testing.
	 */
	@BeforeEach
	void setUp() {
		testLogFile = tempDir.resolve("process_test.log").toString();
		processLogger = new ProcessLogger(testLogFile, false); // Use non-verbose mode for cleaner test output
	}

	/**
	 * Cleans up after each test method.
	 * Removes any test log files that may have been created.
	 */
	@AfterEach
	void tearDown() {
		if (processLogger != null) {
			File file = new File(testLogFile);
			if (file.exists()) {
				file.delete();
			}
		}
	}

	/**
	 * Tests constructor with valid parameters.
	 * Verifies that the ProcessLogger instance is created correctly with the
	 * specified parameters.
	 */
	@Test
	void testConstructorWithValidParameters() {
		ProcessLogger logger = new ProcessLogger("test.log", false); // Use non-verbose mode for cleaner test output
		assertNotNull(logger, "ProcessLogger instance should not be null");
	}

	/**
	 * Tests constructor with null log file name.
	 * Verifies that the constructor throws IllegalArgumentException for null input.
	 */
	@Test
	void testConstructorWithNullLogFile() {
		assertThrows(
			IllegalArgumentException.class,
			() -> {
				new ProcessLogger(null);
			},
			"Constructor should throw IllegalArgumentException for null log file name"
		);
	}

	/**
	 * Tests logging a simple Runnable method.
	 * Verifies that void methods are logged correctly with timing information.
	 */
	// @Test
	// void testLogRunnableSimple() throws IOException {
	// 	AtomicBoolean executed = new AtomicBoolean(false);

	// 	processLogger.log("Simple test operation", () -> {
	// 		executed.set(true);
	// 	});

	// 	assertTrue(executed.get(), "Runnable should have been executed");
	// 	assertTrue(Files.exists(Paths.get(testLogFile)), "Log file should exist after execution");

	// 	List<String> lines = Files.readAllLines(Paths.get(testLogFile));
	// 	assertEquals(2, lines.size(), "Should have START and COMPLETED log entries");
	// 	assertTrue(lines.get(0).contains("START"), "First line should contain START");
	// 	assertTrue(lines.get(1).contains("COMPLETED"), "Second line should contain COMPLETED");
	// 	assertTrue(lines.get(1).contains("ms"), "Completion line should contain duration in ms");
	// }

	/**
	 * Tests logging a Runnable method with exception.
	 * Verifies that exceptions are handled gracefully and logged correctly.
	 */
	@Test
	void testLogRunnableWithException() throws IOException {
		AtomicBoolean executed = new AtomicBoolean(false);

		processLogger.log("Failing test operation", () -> {
			executed.set(true);
			throw new RuntimeException("Test exception");
		});

		assertTrue(executed.get(), "Runnable should have been executed before exception");
		assertTrue(Files.exists(Paths.get(testLogFile)), "Log file should exist after execution");

		List<String> lines = Files.readAllLines(Paths.get(testLogFile));
		assertEquals(2, lines.size(), "Should have START and FAILED log entries");
		assertTrue(lines.get(0).contains("START"), "First line should contain START");
		assertTrue(lines.get(1).contains("FAILED"), "Second line should contain FAILED");
		assertTrue(lines.get(1).contains("Test exception"), "Failure line should contain exception message");
	}

	/**
	 * Tests logging a Supplier method with return value.
	 * Verifies that methods with return values are logged correctly with timing
	 * information.
	 */
	@Test
	void testLogSupplierSimple() throws IOException {
		String expectedResult = "Test result";

		String result = processLogger.log("Simple supplier test", () -> {
			return expectedResult;
		});

		assertEquals(expectedResult, result, "Return value should match expected result");
		assertTrue(Files.exists(Paths.get(testLogFile)), "Log file should exist after execution");

		List<String> lines = Files.readAllLines(Paths.get(testLogFile));
		assertEquals(2, lines.size(), "Should have START and COMPLETED log entries");
		assertTrue(lines.get(0).contains("START"), "First line should contain START");
		assertTrue(lines.get(1).contains("COMPLETED"), "Second line should contain COMPLETED");
		assertTrue(lines.get(1).contains("ms"), "Completion line should contain duration in ms");
	}

	/**
	 * Tests logging a Supplier method with exception.
	 * Verifies that exceptions in supplier methods are handled gracefully and
	 * return null.
	 */
	@Test
	void testLogSupplierWithException() throws IOException {
		String result = processLogger.log("Failing supplier test", () -> {
			throw new RuntimeException("Supplier test exception");
		});

		assertNull(result, "Result should be null when exception occurs");
		assertTrue(Files.exists(Paths.get(testLogFile)), "Log file should exist after execution");

		List<String> lines = Files.readAllLines(Paths.get(testLogFile));
		assertEquals(2, lines.size(), "Should have START and FAILED log entries");
		assertTrue(lines.get(0).contains("START"), "First line should contain START");
		assertTrue(lines.get(1).contains("FAILED"), "Second line should contain FAILED");
		assertTrue(lines.get(1).contains("Supplier test exception"), "Failure line should contain exception message");
	}

	/**
	 * Tests logging with null message.
	 * Verifies that null message parameter is handled correctly.
	 */
	// @Test
	// void testLogWithNullMessage() {
	// 	assertThrows(
	// 		IllegalArgumentException.class,
	// 		() -> {
	// 			processLogger.log(null, () -> {});
	// 		},
	// 		"Should throw IllegalArgumentException for null message"
	// 	);
	// }

	/**
	 * Tests logging with null Runnable.
	 * Verifies that null Runnable parameter is handled correctly.
	 */
	// @Test
	// void testLogRunnableWithNullFunction() {
	// 	assertThrows(
	// 		IllegalArgumentException.class,
	// 		() -> {
	// 			processLogger.log("Test message", (Runnable) null);
	// 		},
	// 		"Should throw IllegalArgumentException for null Runnable"
	// 	);
	// }

	/**
	 * Tests logging with null Supplier.
	 * Verifies that null Supplier parameter is handled correctly.
	 */
	@Test
	void testLogSupplierWithNullFunction() {
		assertThrows(
			IllegalArgumentException.class,
			() -> {
				processLogger.log("Test message", (Supplier<String>) null);
			},
			"Should throw IllegalArgumentException for null Supplier"
		);
	}

	/**
	 * Tests execution timing accuracy.
	 * Verifies that the timing measurement is reasonably accurate.
	 */
	// @Test
	// void testExecutionTiming() throws IOException {
	// 	long expectedDelay = 100; // 100ms delay

	// 	processLogger.log("Timing test", () -> {
	// 		try {
	// 			Thread.sleep(expectedDelay);
	// 		} catch (InterruptedException e) {
	// 			Thread.currentThread().interrupt();
	// 		}
	// 	});

	// 	List<String> lines = Files.readAllLines(Paths.get(testLogFile));
	// 	String completedLine = lines.get(1);

	// 	// Extract duration from log line (format: "...COMPLETED in XXX ms")
	// 	String durationStr = completedLine.substring(
	// 		completedLine.lastIndexOf("in ") + 3,
	// 		completedLine.lastIndexOf(" ms")
	// 	);
	// 	long actualDuration = Long.parseLong(durationStr);

	// 	// Allow for some variance in timing (within 50ms)
	// 	assertTrue(
	// 		actualDuration >= expectedDelay - 50,
	// 		"Actual duration should be at least " + (expectedDelay - 50) + "ms, was " + actualDuration + "ms"
	// 	);
	// 	assertTrue(
	// 		actualDuration <= expectedDelay + 50,
	// 		"Actual duration should be at most " + (expectedDelay + 50) + "ms, was " + actualDuration + "ms"
	// 	);
	// }

	/**
	 * Tests basic log entry format.
	 * Verifies that log entries are formatted correctly with timestamps and
	 * messages.
	 */
	// @Test
	// void testLogEntryFormat() throws IOException {
	// 	processLogger.log("Format test", () -> {});

	// 	List<String> lines = Files.readAllLines(Paths.get(testLogFile));
	// 	String startLine = lines.get(0);

	// 	// The log should contain timestamp, message, and START indicator
	// 	assertTrue(startLine.contains("Format test"), "Log should contain the message: " + startLine);
	// 	assertTrue(startLine.contains("START"), "Log should contain START indicator: " + startLine);
	// 	assertTrue(
	// 		startLine.matches("\\[\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}\\.\\d+\\].*"),
	// 		"Log should contain timestamp format: " + startLine
	// 	);
	// }

	/**
	 * Tests multiple consecutive logging operations.
	 * Verifies that multiple operations are logged correctly in sequence.
	 */
	@Test
	void testMultipleLoggingOperations() throws IOException {
		AtomicInteger counter = new AtomicInteger(0);

		processLogger.log("First operation", () -> counter.incrementAndGet());
		processLogger.log("Second operation", () -> counter.incrementAndGet());
		processLogger.log("Third operation", () -> counter.incrementAndGet());

		assertEquals(3, counter.get(), "All operations should have been executed");

		List<String> lines = Files.readAllLines(Paths.get(testLogFile));
		assertEquals(6, lines.size(), "Should have 6 log entries (3 operations × 2 entries each)");

		// Verify all operations are logged
		assertTrue(
			lines.stream().anyMatch(line -> line.contains("First operation")),
			"First operation should be logged"
		);
		assertTrue(
			lines.stream().anyMatch(line -> line.contains("Second operation")),
			"Second operation should be logged"
		);
		assertTrue(
			lines.stream().anyMatch(line -> line.contains("Third operation")),
			"Third operation should be logged"
		);
	}

	/**
	 * Tests logging with complex return types.
	 * Verifies that complex objects can be returned and logged correctly.
	 */
	@Test
	void testLogSupplierWithComplexReturnType() throws IOException {
		TestData expectedData = new TestData("test", 42, true);

		TestData result = processLogger.log("Complex return type test", () -> {
			return expectedData;
		});

		assertNotNull(result, "Result should not be null");
		assertEquals(expectedData, result, "Result should match expected data");
		assertEquals("test", result.getName(), "Result name should match");
		assertEquals(42, result.getValue(), "Result value should match");
		assertTrue(result.isFlag(), "Result flag should match");
	}

	/**
	 * Tests logging with empty message.
	 * Verifies that empty messages are handled correctly.
	 */
	// @Test
	// void testLogWithEmptyMessage() throws IOException {
	// 	processLogger.log("", () -> {});

	// 	List<String> lines = Files.readAllLines(Paths.get(testLogFile));
	// 	assertEquals(2, lines.size(), "Should have START and COMPLETED log entries");
	// 	assertTrue(lines.get(0).contains("START"), "First line should contain START");
	// 	assertTrue(lines.get(1).contains("COMPLETED"), "Second line should contain COMPLETED");
	// }

	/**
	 * Tests logging with very long message.
	 * Verifies that long messages are handled correctly.
	 */
	// @Test
	// void testLogWithLongMessage() throws IOException {
	// 	String longMessage =
	// 		"This is a very long message that contains many characters and should be handled correctly by the logging system without any issues or truncation problems";

	// 	processLogger.log(longMessage, () -> {});

	// 	List<String> lines = Files.readAllLines(Paths.get(testLogFile));
	// 	assertEquals(2, lines.size(), "Should have START and COMPLETED log entries");
	// 	assertTrue(lines.get(0).contains(longMessage), "Log should contain the long message");
	// }

	/**
	 * Tests concurrent logging operations.
	 * Verifies that multiple threads can use the logger simultaneously.
	 */
	// @Test
	// void testConcurrentLogging() throws InterruptedException {
	// 	int numberOfThreads = 5;
	// 	int operationsPerThread = 3;
	// 	Thread[] threads = new Thread[numberOfThreads];
	// 	AtomicInteger totalOperations = new AtomicInteger(0);

	// 	for (int i = 0; i < numberOfThreads; i++) {
	// 		final int threadId = i;
	// 		threads[i] = new Thread(() -> {
	// 			for (int j = 0; j < operationsPerThread; j++) {
	// 				processLogger.log("Thread " + threadId + " Operation " + j, () -> {
	// 					totalOperations.incrementAndGet();
	// 				});
	// 			}
	// 		});
	// 		threads[i].start();
	// 	}

	// 	// Wait for all threads to complete
	// 	for (Thread thread : threads) {
	// 		thread.join();
	// 	}

	// 	assertEquals(
	// 		numberOfThreads * operationsPerThread,
	// 		totalOperations.get(),
	// 		"All operations should have been executed"
	// 	);
	// }

	/**
	 * Tests logging with different exception types.
	 * Verifies that various exception types are handled correctly.
	 */
	@Test
	void testLogWithDifferentExceptionTypes() throws IOException {
		// Test RuntimeException
		processLogger.log("Runtime exception test", () -> {
			throw new RuntimeException("Runtime exception message");
		});

		// Test IllegalArgumentException
		processLogger.log("Illegal argument exception test", () -> {
			throw new IllegalArgumentException("Illegal argument exception message");
		});

		// Test custom exception
		processLogger.log("Custom exception test", () -> {
			try {
				throw new TestException("Custom exception message");
			} catch (TestException e) {
				throw new RuntimeException(e);
			}
		});

		List<String> lines = Files.readAllLines(Paths.get(testLogFile));
		assertEquals(6, lines.size(), "Should have 6 log entries (3 operations × 2 entries each)");

		// Verify all exception types are logged
		assertTrue(
			lines.stream().anyMatch(line -> line.contains("Runtime exception message")),
			"Runtime exception should be logged"
		);
		assertTrue(
			lines.stream().anyMatch(line -> line.contains("Illegal argument exception message")),
			"IllegalArgumentException should be logged"
		);
		assertTrue(
			lines.stream().anyMatch(line -> line.contains("Custom exception message")),
			"Custom exception should be logged"
		);
	}

	/**
	 * Tests the logError method with a valid exception.
	 * Verifies that exceptions are logged correctly with proper formatting.
	 */
	@Test
	void testLogErrorWithValidException() throws IOException {
		Exception testException = new RuntimeException("Test error message");
		String source = "testMethod";

		processLogger.logError(source, testException);

		List<String> lines = Files.readAllLines(Paths.get(testLogFile));
		assertTrue(lines.size() >= 2, "Should have at least 2 log entries (error message and stack trace)");

		// Verify error message is logged
		assertTrue(
			lines.stream().anyMatch(line -> line.contains("ERROR in " + source)),
			"Error log should contain source method name"
		);
		assertTrue(
			lines.stream().anyMatch(line -> line.contains("RuntimeException: Test error message")),
			"Error log should contain exception details"
		);
		assertTrue(
			lines.stream().anyMatch(line -> line.contains("Stack Trace:")),
			"Error log should contain stack trace"
		);
	}

	/**
	 * Tests the logError method with a null exception.
	 * Verifies that null exceptions are handled gracefully.
	 */
	@Test
	void testLogErrorWithNullException() throws IOException {
		String source = "testMethod";

		processLogger.logError(source, null);

		List<String> lines = Files.readAllLines(Paths.get(testLogFile));
		assertEquals(1, lines.size(), "Should have 1 log entry for null exception");

		assertTrue(lines.get(0).contains("ERROR in " + source), "Error log should contain source method name");
		assertTrue(
			lines.get(0).contains("Exception object is null"),
			"Error log should contain null exception message"
		);
	}

	/**
	 * Tests the logError method with a null source.
	 * Verifies that null source is handled gracefully.
	 */
	@Test
	void testLogErrorWithNullSource() throws IOException {
		Exception testException = new RuntimeException("Test error message");

		processLogger.logError(null, testException);

		List<String> lines = Files.readAllLines(Paths.get(testLogFile));
		assertTrue(lines.size() >= 2, "Should have at least 2 log entries");

		assertTrue(
			lines.stream().anyMatch(line -> line.contains("ERROR in No Source Provided for the Exception")),
			"Error log should contain default source message"
		);
		assertTrue(
			lines.stream().anyMatch(line -> line.contains("RuntimeException: Test error message")),
			"Error log should contain exception details"
		);
	}

	/**
	 * Tests the logError method with an exception that has no message.
	 * Verifies that exceptions without messages are handled correctly.
	 */
	@Test
	void testLogErrorWithExceptionNoMessage() throws IOException {
		Exception testException = new RuntimeException();
		String source = "testMethod";

		processLogger.logError(source, testException);

		List<String> lines = Files.readAllLines(Paths.get(testLogFile));
		assertTrue(lines.size() >= 2, "Should have at least 2 log entries");

		assertTrue(
			lines.stream().anyMatch(line -> line.contains("ERROR in " + source)),
			"Error log should contain source method name"
		);
		assertTrue(
			lines.stream().anyMatch(line -> line.contains("RuntimeException: No message available")),
			"Error log should contain default message for exception without message"
		);
	}

	/**
	 * Helper class for testing complex return types.
	 */
	private static class TestData {

		private final String name;
		private final int value;
		private final boolean flag;

		public TestData(String name, int value, boolean flag) {
			this.name = name;
			this.value = value;
			this.flag = flag;
		}

		public String getName() {
			return name;
		}

		public int getValue() {
			return value;
		}

		public boolean isFlag() {
			return flag;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) return true;
			if (obj == null || getClass() != obj.getClass()) return false;
			TestData testData = (TestData) obj;
			return value == testData.value && flag == testData.flag && name.equals(testData.name);
		}

		@Override
		public int hashCode() {
			return java.util.Objects.hash(name, value, flag);
		}
	}

	/**
	 * Custom exception class for testing.
	 */
	private static class TestException extends Exception {

		public TestException(String message) {
			super(message);
		}
	}
}
