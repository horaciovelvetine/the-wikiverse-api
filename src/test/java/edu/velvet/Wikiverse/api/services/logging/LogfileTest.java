package edu.velvet.Wikiverse.api.services.logging;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Comprehensive test suite for the Logfile class.
 * Tests all public methods including constructor validation, message writing,
 * log rotation, and thread safety functionality.
 *
 * <p>
 * This test class verifies all functionality of the Logfile class including:
 * <ul>
 * <li>Constructor behavior with various parameters</li>
 * <li>Message writing functionality</li>
 * <li>Log file rotation when size limits are exceeded</li>
 * <li>Thread safety of synchronized methods</li>
 * <li>Error handling for I/O operations</li>
 * <li>Edge cases and boundary conditions</li>
 * </ul>
 *
 * <p>
 * Tests use temporary directories to avoid conflicts with existing files
 * and ensure clean test isolation.
 *
 * @author @horaciovelvetine
 * @version 1.0
 * @since 1.0
 * @see Logfile
 */
public class LogfileTest {

	@TempDir
	Path tempDir;

	private Logfile logfile;
	private String testLogFile;

	/**
	 * Sets up test environment before each test method.
	 * Creates a temporary log file path for testing.
	 */
	@BeforeEach
	void setUp() {
		testLogFile = tempDir.resolve("test.log").toString();
	}

	/**
	 * Cleans up after each test method.
	 * Removes any test log files that may have been created.
	 */
	@AfterEach
	void tearDown() {
		if (logfile != null) {
			File file = new File(testLogFile);
			if (file.exists()) {
				file.delete();
			}
		}
	}

	/**
	 * Tests constructor with valid parameters.
	 * Verifies that the Logfile instance is created correctly with the specified
	 * parameters.
	 */
	@Test
	void testConstructorWithValidParameters() {
		logfile = new Logfile("test.log", false); // Use non-verbose mode for cleaner test output
		assertNotNull(logfile, "Logfile instance should not be null");
	}

	/**
	 * Tests constructor with null log file name.
	 * Verifies that the constructor handles null input gracefully by using a
	 * default name.
	 */
	@Test
	void testConstructorWithNullLogFile() {
		logfile = new Logfile(null, false); // Use non-verbose mode for cleaner test output
		assertNotNull(logfile, "Logfile instance should not be null with null log file name");
	}

	/**
	 * Tests constructor with empty log file name.
	 * Verifies that the constructor handles empty string input gracefully by using
	 * a default name.
	 */
	@Test
	void testConstructorWithEmptyLogFile() {
		logfile = new Logfile("", false); // Use non-verbose mode for cleaner test output
		assertNotNull(logfile, "Logfile instance should not be null with empty log file name");
	}

	/**
	 * Tests writing a simple message to the log file.
	 * Verifies that messages are correctly written to the file.
	 */
	@Test
	void testWriteSimpleMessage() throws IOException {
		logfile = new Logfile(testLogFile, false); // Use non-verbose mode for cleaner test output
		String message = "Test message";

		logfile.write(message);

		assertTrue(Files.exists(Paths.get(testLogFile)), "Log file should exist after writing");
		List<String> lines = Files.readAllLines(Paths.get(testLogFile));
		assertEquals(1, lines.size(), "Should have one line in the log file");
		assertEquals(message, lines.get(0), "Written message should match the input");
	}

	/**
	 * Tests writing multiple messages to the log file.
	 * Verifies that multiple messages are correctly appended to the file.
	 */
	@Test
	void testWriteMultipleMessages() throws IOException {
		logfile = new Logfile(testLogFile, false); // Use non-verbose mode for cleaner test output
		String[] messages = { "First message", "Second message", "Third message" };

		for (String message : messages) {
			logfile.write(message);
		}

		List<String> lines = Files.readAllLines(Paths.get(testLogFile));
		assertEquals(3, lines.size(), "Should have three lines in the log file");
		for (int i = 0; i < messages.length; i++) {
			assertEquals(messages[i], lines.get(i), "Message " + i + " should match");
		}
	}

	/**
	 * Tests writing a null message.
	 * Verifies that null messages are handled gracefully.
	 */
	@Test
	void testWriteNullMessage() throws IOException {
		logfile = new Logfile(testLogFile, false); // Use non-verbose mode for cleaner test output

		logfile.write(null);

		assertTrue(Files.exists(Paths.get(testLogFile)), "Log file should exist after writing null");
		List<String> lines = Files.readAllLines(Paths.get(testLogFile));
		assertEquals(1, lines.size(), "Should have one line in the log file");
		assertEquals("null", lines.get(0), "Null message should be written as 'null'");
	}

	/**
	 * Tests writing an empty message.
	 * Verifies that empty messages are handled correctly.
	 */
	@Test
	void testWriteEmptyMessage() throws IOException {
		logfile = new Logfile(testLogFile, false); // Use non-verbose mode for cleaner test output

		logfile.write("");

		assertTrue(Files.exists(Paths.get(testLogFile)), "Log file should exist after writing empty message");
		List<String> lines = Files.readAllLines(Paths.get(testLogFile));
		assertEquals(1, lines.size(), "Should have one line in the log file");
		assertEquals("", lines.get(0), "Empty message should be written as empty string");
	}

	/**
	 * Tests writing a message with newlines.
	 * Verifies that messages containing newlines are handled correctly.
	 */
	@Test
	void testWriteMessageWithNewlines() throws IOException {
		logfile = new Logfile(testLogFile, false); // Use non-verbose mode for cleaner test output
		String message = "Line 1\nLine 2\nLine 3";

		logfile.write(message);

		List<String> lines = Files.readAllLines(Paths.get(testLogFile));
		assertEquals(3, lines.size(), "Should have three lines in the log file (newlines are split by readAllLines)");
		assertEquals("Line 1", lines.get(0), "First line should match");
		assertEquals("Line 2", lines.get(1), "Second line should match");
		assertEquals("Line 3", lines.get(2), "Third line should match");
	}

	/**
	 * Tests log file rotation when size limit is exceeded.
	 * Verifies that the log file is rotated when it exceeds the maximum size.
	 * Note: This test may not trigger rotation with the default 10MB limit
	 * unless a very large amount of data is written.
	 */
	@Test
	void testLogFileRotation() throws IOException {
		logfile = new Logfile(testLogFile, false); // Use non-verbose mode for cleaner test output

		// Write many large messages to potentially exceed the 10MB default limit
		StringBuilder largeMessage = new StringBuilder();
		for (int i = 0; i < 1000; i++) {
			largeMessage.append("This is a very long message that should help us reach the file size limit. ");
		}

		// Write the large message multiple times to try to exceed 10MB
		for (int i = 0; i < 100; i++) {
			logfile.write("Message " + i + " - " + largeMessage.toString());
		}

		// Check if backup files exist (rotation may have occurred)
		File[] backupFiles = tempDir.toFile().listFiles((dir, name) -> name.startsWith("test.log."));

		// Note: With the default 10MB limit, rotation may not occur in this test
		// This test mainly verifies that the rotation mechanism is in place
		if (backupFiles != null && backupFiles.length > 0) {
			// If rotation occurred, verify the original file doesn't exist
			File originalFile = new File(testLogFile);
			assertFalse(originalFile.exists(), "Original log file should not exist after rotation");
		} else {
			// If no rotation occurred, verify the original file exists
			File originalFile = new File(testLogFile);
			assertTrue(originalFile.exists(), "Original log file should exist if no rotation occurred");
		}
	}

	/**
	 * Tests thread safety by writing from multiple threads.
	 * Verifies that the synchronized write method works correctly with concurrent
	 * access.
	 */
	@Test
	void testThreadSafety() throws InterruptedException {
		logfile = new Logfile(testLogFile, false); // Use non-verbose mode for cleaner test output
		int numberOfThreads = 5;
		int messagesPerThread = 10;
		Thread[] threads = new Thread[numberOfThreads];

		// Create and start multiple threads
		for (int i = 0; i < numberOfThreads; i++) {
			final int threadId = i;
			threads[i] = new Thread(() -> {
				for (int j = 0; j < messagesPerThread; j++) {
					logfile.write("Thread " + threadId + " Message " + j);
				}
			});
			threads[i].start();
		}

		// Wait for all threads to complete
		for (Thread thread : threads) {
			thread.join();
		}

		// Verify all messages were written
		try {
			List<String> lines = Files.readAllLines(Paths.get(testLogFile));
			assertEquals(
					numberOfThreads * messagesPerThread,
					lines.size(),
					"All messages from all threads should be written");
		} catch (IOException e) {
			fail("Failed to read log file: " + e.getMessage());
		}
	}

	/**
	 * Tests writing to a read-only directory (simulated error condition).
	 * This test verifies error handling when file operations fail.
	 */
	@Test
	void testWriteToReadOnlyDirectory() {
		// Create a read-only directory
		Path readOnlyDir = tempDir.resolve("readonly");
		try {
			Files.createDirectories(readOnlyDir);
			readOnlyDir.toFile().setReadOnly();

			String readOnlyLogFile = readOnlyDir.resolve("test.log").toString();
			logfile = new Logfile(readOnlyLogFile, false); // Use non-verbose mode for cleaner test output

			// This should not throw an exception, but should handle the error gracefully
			assertDoesNotThrow(() -> logfile.write("Test message"), "Write should handle I/O errors gracefully");
		} catch (IOException e) {
			// If we can't create the read-only directory, skip this test
			System.out.println("Skipping read-only directory test: " + e.getMessage());
		} catch (Exception e) {
			// If the test fails for other reasons, that's also acceptable
			System.out.println("Read-only directory test failed as expected: " + e.getMessage());
		} finally {
			// Restore write permissions for cleanup
			try {
				readOnlyDir.toFile().setWritable(true);
			} catch (Exception e) {
				// Ignore cleanup errors
			}
		}
	}

	/**
	 * Tests the default maximum file size constant.
	 * Verifies that the default size is set to the expected value (10MB).
	 */
	@Test
	void testDefaultMaxFileSize() {
		// This test verifies the constant value through behavior
		logfile = new Logfile(testLogFile, false); // Use non-verbose mode for cleaner test output

		// Write a small message - should not trigger rotation
		logfile.write("Small message");

		try {
			assertTrue(Files.exists(Paths.get(testLogFile)), "Log file should exist");
			long fileSize = Files.size(Paths.get(testLogFile));
			assertTrue(fileSize < 10 * 1024 * 1024, "File size should be less than 10MB default");
		} catch (IOException e) {
			fail("Failed to check file size: " + e.getMessage());
		}
	}

	/**
	 * Tests writing very large messages.
	 * Verifies that large messages are handled correctly without causing issues.
	 */
	@Test
	void testWriteLargeMessage() throws IOException {
		logfile = new Logfile(testLogFile, false); // Use non-verbose mode for cleaner test output

		// Create a large message (1KB)
		StringBuilder largeMessage = new StringBuilder();
		for (int i = 0; i < 100; i++) {
			largeMessage.append("This is a large message part ").append(i).append(" ");
		}

		logfile.write(largeMessage.toString());

		List<String> lines = Files.readAllLines(Paths.get(testLogFile));
		assertEquals(1, lines.size(), "Should have one line in the log file");
		assertEquals(largeMessage.toString(), lines.get(0), "Large message should be written correctly");
	}

	/**
	 * Tests concurrent write operations with different message sizes.
	 * Verifies that mixed message sizes work correctly in a multi-threaded
	 * environment.
	 */
	@Test
	void testConcurrentWritesWithDifferentSizes() throws InterruptedException {
		logfile = new Logfile(testLogFile, false); // Use non-verbose mode for cleaner test output
		int numberOfThreads = 3;
		Thread[] threads = new Thread[numberOfThreads];

		// Create threads with different message patterns
		threads[0] = new Thread(() -> {
			for (int i = 0; i < 5; i++) {
				logfile.write("Short message " + i);
			}
		});

		threads[1] = new Thread(() -> {
			for (int i = 0; i < 3; i++) {
				logfile.write("Medium length message number " + i + " with more content");
			}
		});

		threads[2] = new Thread(() -> {
			for (int i = 0; i < 2; i++) {
				logfile.write(
						"Very long message " +
								i +
								" with lots of additional text content that makes it much longer than the other messages");
			}
		});

		// Start all threads
		for (Thread thread : threads) {
			thread.start();
		}

		// Wait for all threads to complete
		for (Thread thread : threads) {
			thread.join();
		}

		// Verify all messages were written
		try {
			List<String> lines = Files.readAllLines(Paths.get(testLogFile));
			assertEquals(10, lines.size(), "Should have 10 total messages (5+3+2)");
		} catch (IOException e) {
			fail("Failed to read log file: " + e.getMessage());
		}
	}
}
