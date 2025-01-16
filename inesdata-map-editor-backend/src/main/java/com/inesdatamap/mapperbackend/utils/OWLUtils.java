package com.inesdatamap.mapperbackend.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLDataPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLObjectPropertyRangeAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;

import com.inesdatamap.mapperbackend.model.dto.PropertyDTO;
import com.inesdatamap.mapperbackend.model.enums.PropertyTypeEnum;

/**
 * Utility class for OWL.
 */
public final class OWLUtils {

	/**
	 * Private constructor to prevent instantiation.
	 */
	private OWLUtils() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Retrieves all the class names from the provided OWL ontology.
	 *
	 * @param owl
	 *            the OWL ontology from which to retrieve class names
	 * @return a list of class names
	 */
	public static List<String> getClasses(OWLOntology owl) {

		// List to store the classes
		List<String> ontologyClasses = new ArrayList<>();

		// Iterate over all classes in the ontology
		for (OWLClass owlClass : owl.getClassesInSignature()) {
			// Extract the class name from its IRI fragment
			String className = owlClass.getIRI().getFragment();

			// Add the class name to the list
			if (className != null && !className.isEmpty()) {
				ontologyClasses.add(className);
			}
		}

		return ontologyClasses;
	}

	/**
	 * Retrieves the data properties associated with a specified class from the given ontology
	 *
	 * @param ontologyContent
	 *            The content of the ontology as a string
	 * @param className
	 *            The name of the class for which the data properties are to be retrieved
	 * @return A list of properties associated with the specified class
	 * @throws OWLOntologyCreationException
	 *             if there is an error during the ontology creation process
	 */
	public static List<PropertyDTO> getProperties(String ontologyContent, String className) throws OWLOntologyCreationException {

		if (ontologyContent == null || ontologyContent.isEmpty()) {
			throw new IllegalArgumentException("Ontology content is empty.");
		}
		if (className == null || className.isEmpty()) {
			throw new IllegalArgumentException("Class name is empty.");
		}

		// Create OWLOntologyManager instance and load the ontology
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new StringDocumentSource(ontologyContent));


		// Find the owl class by class name
		OWLClass owlClass = ontology.classesInSignature().filter(clazz -> clazz.getIRI().getFragment().equals(className)).findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Class " + className + " not found in the ontology"));

		Map<String, String> namespacePrefixes = NameSpaceUtils.getPrefixNamespaceMap(ontology);

		List<PropertyDTO> properties = new ArrayList<>();

		// Collect properties and add them
		properties.addAll(getDataProperties(owlClass, ontology, namespacePrefixes));
		properties.addAll(getObjectProperties(owlClass, ontology, namespacePrefixes));
		properties.addAll(getAnnotationProperties(ontology, namespacePrefixes));

		return properties;
	}

	/**
	 * Retrieves a list of data properties associated with a specified class in the ontology
	 *
	 * @param owlClass
	 *            The OWL class
	 * @param ontology
	 *            The ontology in which to search for the data properties
	 * @param namespacePrefixes
	 *            The map with prefix and namespace
	 * @return A list of data properties
	 */
	public static List<PropertyDTO> getDataProperties(OWLClass owlClass, OWLOntology ontology, Map<String, String> namespacePrefixes) {

		Set<PropertyDTO> dataProperties = new HashSet<>();

		// Iterate over all axioms
		for (OWLAxiom axiom : ontology.getAxioms()) {
			OWLDataProperty property = null;
			boolean isAssociated = false;

			// Check if axiom is of data property domain
			if (axiom.isOfType(AxiomType.DATA_PROPERTY_DOMAIN)) {
				OWLDataPropertyDomainAxiom domainAxiom = (OWLDataPropertyDomainAxiom) axiom;
				OWLClassExpression domain = domainAxiom.getDomain();
				property = domainAxiom.getProperty().asOWLDataProperty();
				isAssociated = domain.equals(owlClass) || domain.getClassesInSignature().contains(owlClass);
			}
			// Check if axiom is of data property range
			else if (axiom.isOfType(AxiomType.DATA_PROPERTY_RANGE)) {
				OWLDataPropertyRangeAxiom rangeAxiom = (OWLDataPropertyRangeAxiom) axiom;
				OWLDataRange range = rangeAxiom.getRange();
				property = rangeAxiom.getProperty().asOWLDataProperty();
				isAssociated = range.getClassesInSignature().contains(owlClass);
			}

			// If the property is associated to the class, add it to the list
			if (property != null && isAssociated) {
				String prefix = NameSpaceUtils.getPrefixForNamespace(namespacePrefixes, property.getIRI().getNamespace());
				dataProperties.add(
						createPropertyDTO((prefix != null ? prefix : "") + property.getIRI().getFragment(), PropertyTypeEnum.DATA, true));
			}
		}

		// Get data properties not associated to a class
		dataProperties.addAll(getUnassociatedDataProperties(ontology, namespacePrefixes));

		return new ArrayList<>(dataProperties);
	}

	/**
	 * Retrieves a set of unassociated data properties from the given ontology.
	 *
	 * @param ontology
	 *            The OWL ontology
	 * @param namespacePrefixes
	 *            The map with prefix and namespace
	 * @return A Set of PropertyDTO
	 */
	public static Set<PropertyDTO> getUnassociatedDataProperties(OWLOntology ontology, Map<String, String> namespacePrefixes) {
		Set<PropertyDTO> unassociatedDataProperties = new HashSet<>();

		// Iterate over all data properties
		for (OWLDataProperty property : ontology.dataPropertiesInSignature().collect(Collectors.toSet())) {
			boolean isAssociated = false;

			// Check if the property is associated with domain or range
			for (OWLAxiom axiom : ontology.getAxioms(property)) {
				if (axiom.isOfType(AxiomType.DATA_PROPERTY_DOMAIN, AxiomType.DATA_PROPERTY_RANGE)) {
					isAssociated = true;
					break;
				}
			}

			if (!isAssociated) {
				String prefix = NameSpaceUtils.getPrefixForNamespace(namespacePrefixes, property.getIRI().getNamespace());
				unassociatedDataProperties.add(
						createPropertyDTO((prefix != null ? prefix : "") + property.getIRI().getFragment(), PropertyTypeEnum.DATA, false));
			}
		}

		return unassociatedDataProperties;
	}

	/**
	 * Retrieves a list of object properties associated with a specified class in the ontology
	 *
	 * @param owlClass
	 *            The OWL class
	 * @param ontology
	 *            The ontology in which to search for the object properties
	 * @param namespacePrefixes
	 *            The map with prefix and namespace
	 * @return A list of object properties
	 */
	public static List<PropertyDTO> getObjectProperties(OWLClass owlClass, OWLOntology ontology, Map<String, String> namespacePrefixes) {

		Set<PropertyDTO> objectProperties = new HashSet<>();

		// Iterate over all axioms
		for (OWLAxiom axiom : ontology.getAxioms()) {
			OWLObjectProperty property = null;
			boolean isAssociated = false;

			// Check if axiom is of object property domain
			if (axiom.isOfType(AxiomType.OBJECT_PROPERTY_DOMAIN)) {
				OWLObjectPropertyDomainAxiom domainAxiom = (OWLObjectPropertyDomainAxiom) axiom;
				OWLClassExpression domainExpression = domainAxiom.getDomain();
				property = domainAxiom.getProperty().asOWLObjectProperty();
				isAssociated = domainExpression.equals(owlClass) || domainExpression.getClassesInSignature().contains(owlClass);
			}
			// Check if axiom is of object property range
			else if (axiom.isOfType(AxiomType.OBJECT_PROPERTY_RANGE)) {
				OWLObjectPropertyRangeAxiom rangeAxiom = (OWLObjectPropertyRangeAxiom) axiom;
				OWLClassExpression rangeExpression = rangeAxiom.getRange();
				property = rangeAxiom.getProperty().asOWLObjectProperty();
				isAssociated = rangeExpression.equals(owlClass) || rangeExpression.getClassesInSignature().contains(owlClass);
			}

			// If the property is associated, add it to the result
			if (property != null && isAssociated) {
				String prefix = NameSpaceUtils.getPrefixForNamespace(namespacePrefixes, property.getIRI().getNamespace());
				objectProperties.add(
						createPropertyDTO((prefix != null ? prefix : "") + property.getIRI().getFragment(), PropertyTypeEnum.OBJECT, true));
			}
		}

		// Get object properties not associated to a class
		objectProperties.addAll(getUnassociatedObjectProperties(ontology, namespacePrefixes));

		return new ArrayList<>(objectProperties);
	}

	/**
	 * Retrieves a set of unassociated object properties from the given ontology
	 *
	 * @param ontology
	 *            The OWL ontology
	 * @param namespacePrefixes
	 *            The map with prefix and namespace
	 * @return A Set of PropertyDTO
	 */
	public static Set<PropertyDTO> getUnassociatedObjectProperties(OWLOntology ontology, Map<String, String> namespacePrefixes) {
		Set<PropertyDTO> unassociatedObjectProperties = new HashSet<>();

		// Iterate over all object properties
		for (OWLObjectProperty property : ontology.objectPropertiesInSignature().collect(Collectors.toSet())) {
			boolean isAssociated = false;

			// Check if the property is associated with domain or range
			for (OWLAxiom axiom : ontology.getAxioms(property)) {
				if (axiom.isOfType(AxiomType.OBJECT_PROPERTY_DOMAIN, AxiomType.OBJECT_PROPERTY_RANGE)) {
					isAssociated = true;
					break;
				}
			}

			if (!isAssociated) {
				String prefix = NameSpaceUtils.getPrefixForNamespace(namespacePrefixes, property.getIRI().getNamespace());
				unassociatedObjectProperties.add(createPropertyDTO((prefix != null ? prefix : "") + property.getIRI().getFragment(),
						PropertyTypeEnum.OBJECT, false));
			}
		}

		return unassociatedObjectProperties;
	}

	/**
	 * Retrieves a list of annotation properties in the ontology
	 *
	 * @param ontology
	 *            the OWLOntology
	 * @param namespacePrefixes
	 *            The map with prefix and namespace
	 * @return A list of annotation properties
	 */
	public static List<PropertyDTO> getAnnotationProperties(OWLOntology ontology, Map<String, String> namespacePrefixes) {

		List<PropertyDTO> annotationProperties = new ArrayList<>();

		// Get all annotation properties in the ontology
		for (OWLAnnotationProperty property : ontology.annotationPropertiesInSignature().collect(Collectors.toSet())) {
			String prefix = NameSpaceUtils.getPrefixForNamespace(namespacePrefixes, property.getIRI().getNamespace());

		annotationProperties
				.add(createPropertyDTO((prefix != null ? prefix : "") + property.getIRI().getFragment(), PropertyTypeEnum.ANNOTATION,
						false));
		}
		return annotationProperties;
	}

	/**
	 * Creates a new PropertyDTO object with the specified property name and type.
	 *
	 * @param property
	 *            the name
	 * @param propertyEnum
	 *            the type
	 * @param isAssociated
	 *            is associated or not with the class
	 * @return a PropertyDTO
	 */
	public static PropertyDTO createPropertyDTO(String property, PropertyTypeEnum propertyEnum, boolean isAssociated) {
		PropertyDTO propertyDTO = new PropertyDTO();
		propertyDTO.setPropertyType(propertyEnum);
		propertyDTO.setName(property);
		propertyDTO.setAssociated(isAssociated);
		return propertyDTO;
	}

}
