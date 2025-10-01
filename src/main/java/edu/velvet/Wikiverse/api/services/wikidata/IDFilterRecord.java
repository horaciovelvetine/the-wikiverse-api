package edu.velvet.Wikiverse.api.services.wikidata;

/**
 * Represents a record for a filtered Wikidata ID in the Wikiverse API system.
 * <p>
 * This record encapsulates the essential information for a Wikidata entity
 * that is subject to filtering, including:
 * <ul>
 *   <li>The Wikidata entity ID (e.g., "P31", "Q42")</li>
 *   <li>A human-readable label describing the entity</li>
 *   <li>Optional notes providing additional context or rationale for filtering</li>
 * </ul>
 *
 * <p>
 * Instances of this record are typically used to track and manage
 * Wikidata entities that are excluded or handled specially by the API.
 *
 * @author @horaciovelvetine
 * @version 1.0
 * @since 1.0
 */

public record IDFilterRecord(String id, String label, String notes) {}
