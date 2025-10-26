package edu.velvet.Wikiverse.api.models.requests;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonIgnore;

import edu.velvet.Wikiverse.api.models.WikiverseError;

/**
 * Represents a request tracking object in the Wikiverse API system.
 * A request object captures timing information for API requests, including
 * when the request was received and when a response was sent.
 *
 * <p>
 * This class provides methods to manage request timing properties such as:
 * <ul>
 * <li>Request received timestamp (automatically set on construction)</li>
 * <li>Response sent timestamp (manually set when response is sent)</li>
 * <li>Duration calculation capabilities</li>
 * </ul>
 *
 * <p>
 * The class automatically sets the received timestamp to the current time
 * when a new instance is created, ensuring accurate request timing tracking.
 *
 * @author @horaciovelvetine
 * @version 1.0
 * @since 1.0
 * @see Instant
 * @see WikiverseError
 */
public class Request {

	/**
	 * The timestamp when this request was received. Set automatically on
	 * construction.
	 */
	private Instant receivedAt;

	/**
	 * The timestamp when this request was responded to. Set manually when response
	 * is sent.
	 */
	private Instant respondedAt;

	/**
	 * An Error object containing details of any error which may have occurred
	 * during the request.
	 */
	private WikiverseError error;

	/**
	 * Creates a new Request instance and sets the received timestamp to the current
	 * time.
	 * The respondedAt timestamp remains null until explicitly set.
	 */
	public Request() {
		this.receivedAt = Instant.now();
	}

	/**
	 * Gets the timestamp when this request was received.
	 *
	 * @return the received timestamp, or null if not set
	 */
	public Instant getReceivedAt() {
		return receivedAt;
	}

	/**
	 * Sets the timestamp when this request was received.
	 *
	 * @param receivedAt the received timestamp, can be null
	 */
	public void setReceivedAt(Instant receivedAt) {
		this.receivedAt = receivedAt;
	}

	/**
	 * Gets the timestamp when this request was responded to.
	 *
	 * @return the responded timestamp, or null if not set
	 */
	public Instant getRespondedAt() {
		return respondedAt;
	}

	/**
	 * Sets the timestamp when this request was responded to to the current time.
	 * This method automatically captures the current timestamp when called.
	 */
	public void setRespondedAt() {
		this.respondedAt = Instant.now();
	}

	/**
	 * Calculates the duration between request received and response sent.
	 *
	 * @return the duration in milliseconds, or null if either timestamp is missing
	 */
	public Long getRequestDurationMillis() {
		if (receivedAt == null || respondedAt == null) {
			return null;
		}
		return respondedAt.toEpochMilli() - receivedAt.toEpochMilli();
	}

	/**
	 * Checks if this request has been completed (both received and responded
	 * timestamps are set).
	 *
	 * @return true if both timestamps are set, false otherwise
	 */
	@JsonIgnore
	public boolean isCompleted() {
		return receivedAt != null && respondedAt != null;
	}

	/**
	 * Marks this request as completed by setting the responded timestamp to the
	 * current time.
	 * This is a convenience method for completing the request lifecycle.
	 *
	 * @return this Request.
	 */
	public Request markCompleted() {
		this.respondedAt = Instant.now();
		return this;
	}

	/**
	 * Checks if this request has encountered an error.
	 *
	 * @return true if an error is present, false otherwise
	 */
	public boolean errored() {
		return this.error != null;
	}

	/**
	 * Gets the error associated with this request.
	 *
	 * @return the WikiverseError object if an error occurred, or null if no error
	 *         is present
	 */
	public WikiverseError getError() {
		return this.error;
	}

	/**
	 * Sets the error associated with this request.
	 * This method allows marking the request as having encountered an error
	 * during processing by storing the WikiverseError details.
	 *
	 * <p>
	 * Setting an error indicates that the request failed to complete
	 * successfully and provides context about what went wrong during
	 * the request processing lifecycle.
	 *
	 * @param error the WikiverseError object describing the error that occurred,
	 *              or null to clear any existing error
	 */
	public void setError(WikiverseError error) {
		this.error = error;
	}
}
