package com.inesdatamap.mapperbackend.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.jena.riot.Lang;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.io.StringDocumentSource;
import org.semanticweb.owlapi.model.AxiomType;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotationProperty;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLEntity;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyDomainAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static java.lang.System.out;

@ExtendWith(SpringExtension.class)
class OWLUtilsTest {

	@Test
	@Disabled("This test is disabled because the output is too verbose for CI")
	void testOwlApi() throws OWLOntologyCreationException, IOException {
		Optional<IRI> ontologyIri = getInfo("unesco-thesaurus.ttl");
		Assertions.assertTrue(ontologyIri.isPresent());
	}

	@Test
	void testGeometry() throws OWLOntologyCreationException, IOException {
		Optional<IRI> ontologyIri = getInfo("skos/loterre-turtle/absolute_geometry.ttl");
		Assertions.assertTrue(ontologyIri.isPresent());
	}

	@Test
	void testSkosRdfXml() throws OWLOntologyCreationException, IOException {
		Optional<IRI> ontologyIri = getInfo("skos/eurovoc-rdf-xml/eurovoc_3952_ciencias_de_la_tierra.xml");
		JenaUtils.printWithJena("skos/eurovoc-rdf-xml/eurovoc_3952_ciencias_de_la_tierra.xml", Lang.RDFXML);
		Assertions.assertFalse(ontologyIri.isPresent());
	}

	@Test
	void testSkosTurtle1() throws OWLOntologyCreationException, IOException {
		Optional<IRI> ontologyIri = getInfo("skos/loterre-turtle/loterre_nutricion_artificial_perioperative_nutrition.ttl");
		Assertions.assertTrue(ontologyIri.isPresent());
	}

	@Test
	void testSkosTurtle2() throws OWLOntologyCreationException, IOException {
		Optional<IRI> ontologyIri = getInfo("skos/loterre-turtle/loterre_patologias_humanas_cellule.ttl");
		Assertions.assertTrue(ontologyIri.isPresent());
	}

	@Test
	void testSkosTurtle3() throws OWLOntologyCreationException, IOException {
		Optional<IRI> ontologyIri = getInfo("skos/loterre-turtle/loterre_tabla_elementos_polonio.ttl");
		JenaUtils.printWithJena("skos/loterre-turtle/loterre_tabla_elementos_polonio.ttl", Lang.TTL);
		Assertions.assertFalse(ontologyIri.isPresent());
	}

	@Test
	@Disabled("This test is disabled because the output is too verbose for CI")
	void testOntolexTurtle() throws OWLOntologyCreationException, IOException {
		Optional<IRI> ontologyIri = getInfo("ontolex/Diccionari_de_dret_administratiu_RDF.ttl");
		JenaUtils.printWithJena("ontolex/Diccionari_de_dret_administratiu_RDF.ttl", Lang.TTL);
		Assertions.assertFalse(ontologyIri.isPresent());
	}

	private Optional<IRI> getInfo(String fileName) throws OWLOntologyCreationException, IOException {
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();

		InputStream file = getClass().getClassLoader().getResourceAsStream(fileName);
		String content = new String(file.readAllBytes(), StandardCharsets.UTF_8);

		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(new StringDocumentSource(content));

		Optional<IRI> ontologyIri = getOntologyInfo(ontology, manager);
		System.out.println("***** CONTENT *****");
		getClasses(ontology, manager);

		System.out.println("***** HIERARCHY *****");
		printHierarchy(ontology, manager);

		return ontologyIri;
	}

	private void printHierarchy(OWLOntology ontology, OWLOntologyManager manager) {
		OWLDataFactory factory = manager.getOWLDataFactory();
		OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		SimpleHierarchy hierarchy = new SimpleHierarchy(reasonerFactory, ontology);
		OWLClass topClass = factory.getOWLThing();
		System.out.println("Class       : " + topClass);
		hierarchy.printHierarchy(topClass);
	}

	private Optional<IRI> getOntologyInfo(OWLOntology owl, OWLOntologyManager manager) {

		out.println("Ontology Loaded...");
		out.println("Ontology : " + owl.getOntologyID());
		out.println("Format : " + manager.getOntologyFormat(owl));

		out.println("Axiom count: " + owl.getAxiomCount());

		owl.getAxioms().forEach(axiom -> {
			out.println("Axiom: " + axiom);
		});

		out.println("Declaration count: " + owl.axioms(AxiomType.DECLARATION).count());

		out.println("Logical Axiom count: " + owl.getLogicalAxiomCount());
		out.println("Classes count: " + owl.classesInSignature().count());
		out.println("Object properties count: " + owl.objectPropertiesInSignature().count());
		out.println("Data properties count: " + owl.dataPropertiesInSignature().count());
		out.println("Individuals count: " + owl.individualsInSignature().count());
		out.println("Annotation properties count: " + owl.annotationPropertiesInSignature().count());

		owl.annotationPropertiesInSignature().forEach(annotationProperty -> {
			out.println("Annotation Property: " + annotationProperty);
		});

		return owl.getOntologyID().getOntologyIRI();
	}

	private void getClasses(OWLOntology owl, OWLOntologyManager manager) {

		// Get all classes in a ontology
		owl.classesInSignature()

			// Map them into a OntologyObjectDto
			//.map(clazz -> getLabel(clazz, owl, manager))
			.forEach(clazz -> {
				List<OWLIndividual> individuals = getIndividuals(clazz, owl, manager);
				individuals.forEach(individual -> {
					getIndividualAttributes(individual, owl, manager);
				});
			});
	}

	private List<OWLIndividual> getIndividuals(OWLClass clazz, OWLOntology owl, OWLOntologyManager manager) {

		OWLDataFactory factory = manager.getOWLDataFactory();

		System.out.println(clazz);

		// Find and print object properties for each class
		Set<OWLObjectProperty> objectProperties = owl.objectPropertiesInSignature().collect(Collectors.toSet());
		for (OWLObjectProperty objectProperty : objectProperties) {
			// Check if the class is a domain or range of the object property
			boolean isDomain = EntitySearcher.getDomains(objectProperty, owl).anyMatch(domain -> domain.containsEntityInSignature(clazz));
			boolean isRange = EntitySearcher.getRanges(objectProperty, owl).anyMatch(range -> range.containsEntityInSignature(clazz));

			if (isDomain || isRange) {
				System.out.println(
					"\tObject Property: " + objectProperty.getIRI().getShortForm() + " (Domain: " + isDomain + ", Range: " + isRange + ")");
			}
		}

		List<OWLIndividual> labels = EntitySearcher.getIndividuals(clazz, owl).toList();

		return labels;
	}

	private Object getIndividualAttributes(OWLIndividual individual, OWLOntology owl, OWLOntologyManager manager) {

		OWLDataFactory factory = manager.getOWLDataFactory();

		//System.out.println("Individual: " + individual);

		owl.axioms(AxiomType.OBJECT_PROPERTY_DOMAIN)

			// Filter by the ones which class is the one we are looking for
			.filter(axiom -> axiom.getDomain().equals(individual))

			// Get the properties of the axiom
			.flatMap(OWLObjectPropertyDomainAxiom::objectPropertiesInSignature)

			// Map them into a OntologyObjectDto
			.forEach(property -> {
				System.out.println("ObjectPropertyDomain: " + property);
			});

		owl.axioms(AxiomType.DATA_PROPERTY_DOMAIN)

			// Filter by the ones which class is the one we are looking for
			.filter(axiom -> axiom.getDomain().equals(individual))

			// Get the properties of the axiom
			.flatMap(OWLDataPropertyDomainAxiom::dataPropertiesInSignature)
			// Map them into a OntologyObjectDto
			.forEach(property -> {
				System.out.println("DataPropertyDomain: " + property);
			});

		EntitySearcher.getObjectPropertyValues(individual, owl).forEach((property, value) -> {
			System.out.println("ObjectProperty: " + property);
		});

		EntitySearcher.getDataPropertyValues(individual, owl).forEach((property, literal) -> {
			System.out.println("DataProperty: " + property);
			System.out.println("Literal: " + literal);
		});

		return null;

	}

	private String getLabel(OWLEntity owlEntity, OWLOntology owl, OWLOntologyManager manager) {

		OWLDataFactory factory = manager.getOWLDataFactory();
		OWLAnnotationProperty rdfsLabel = factory.getRDFSLabel();

		Optional<String> label =

			// Get annotation labels of the entity
			EntitySearcher.getAnnotationObjects(owlEntity, owl, rdfsLabel)

				// Map each annotation as a OWLLiteral
				.map(annotation -> annotation.getValue().asLiteral())

				// Filter the ones present
				.filter(Optional::isPresent)

				// Map the literal to return its value
				.map(value -> value.get().getLiteral())

				.findFirst();

		return label.isPresent() ? label.get() : "";
	}
}
