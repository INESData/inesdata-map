package com.inesdatamap.mapperbackend.utils;

import java.util.HashMap;
import java.util.Map;

import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLOntology;

/**
 * Utility class for NameSpace.
 */
public final class NameSpaceUtils {

	/**
	 * Private constructor to prevent instantiation.
	 */
	private NameSpaceUtils() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Extracts the prefix-namespace mapping from the ontology
	 *
	 * @param ontology
	 *            The OWLOntology
	 * @return A Map where the key is the prefix and the value is the corresponding namespace
	 */
	public static Map<String, String> getPrefixNamespaceMap(OWLOntology ontology) {
		Map<String, String> prefixMap = new HashMap<>();

		OWLDocumentFormat format = ontology.getNonnullFormat();

		// Set to map prefix and namespace
		if (format.isPrefixOWLDocumentFormat()) {
			prefixMap = format.asPrefixOWLDocumentFormat().getPrefixName2PrefixMap();
		}
		return prefixMap;
	}

	/**
	 * Retrieves the prefix associated with a namespace
	 *
	 * @param prefixMap
	 *            The map containing prefix-namespace pairs
	 * @param namespace
	 *            The namespace URI
	 * @return The prefix associated with the given namespace
	 */
	public static String getPrefixForNamespace(Map<String, String> prefixMap, String namespace) {

		// Iterate through the map to find the matching namespace
		for (Map.Entry<String, String> entry : prefixMap.entrySet()) {
			if (entry.getValue().equals(namespace)) {
				return entry.getKey();
			}
		}
		return null;
	}

}
