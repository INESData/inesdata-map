package com.inesdatamap.mapperbackend.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.List;

import org.eclipse.rdf4j.model.BNode;
import org.eclipse.rdf4j.model.impl.SimpleValueFactory;
import org.eclipse.rdf4j.model.util.ModelBuilder;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.inesdatamap.mapperbackend.model.dto.ObjectMapDTO;

@ExtendWith(SpringExtension.class)
class RmlUtilsTest {

	@Test
	void rdf4jTest() throws Exception {

		ModelBuilder builder = new ModelBuilder();
		SimpleValueFactory vf = SimpleValueFactory.getInstance();

		// Define namespaces and base IRI
		builder.setNamespace("rr", "http://www.w3.org/ns/r2rml#")

			.setNamespace("rml", "http://semweb.mmlab.be/ns/rml#")

			.setNamespace("ex", "http://example.org/")

			.setNamespace("xsd", "http://www.w3.org/2001/XMLSchema#");

		// Create a blank node for the mapping
		BNode mappingNode = vf.createBNode();

		// Create logical source
		BNode logicalSourceNode = vf.createBNode();
		builder.subject(mappingNode).add("rml:logicalSource", logicalSourceNode);
		builder.subject(logicalSourceNode).add("rml:source", vf.createLiteral("people.csv")).add("rml:referenceFormulation",
			vf.createIRI("http://semweb.mmlab.be/ns/rml#CSV")).add("rml:iterator", vf.createLiteral("/rows/row"));

		// Create subject map
		BNode subjectMapNode = vf.createBNode();
		builder.subject(mappingNode).add("rr:subjectMap", subjectMapNode);
		builder.subject(subjectMapNode).add("rr:template", vf.createLiteral("http://example.org/person/{id}")).add("rr:class",
			vf.createIRI("http://example.org/Person"));

		// Create first predicate-object map
		BNode predicateObjectMapNode1 = vf.createBNode();
		BNode objectMapNode1 = vf.createBNode();
		builder.subject(mappingNode).add("rr:predicateObjectMap", predicateObjectMapNode1);
		builder.subject(predicateObjectMapNode1).add("rr:predicate", vf.createIRI("http://example.org/hasName")).add("rr:objectMap",
			objectMapNode1);
		builder.subject(objectMapNode1).add("rml:reference", vf.createLiteral("name"));

		// Create second predicate-object map
		BNode predicateObjectMapNode2 = vf.createBNode();
		BNode objectMapNode2 = vf.createBNode();
		builder.subject(mappingNode).add("rr:predicateObjectMap", predicateObjectMapNode2);
		builder.subject(predicateObjectMapNode2).add("rr:predicate", vf.createIRI("http://example.org/hasAge")).add("rr:objectMap",
			objectMapNode2);
		builder.subject(objectMapNode2).add("rml:reference", vf.createLiteral("age")).add("rr:datatype",
			vf.createIRI("http://www.w3.org/2001/XMLSchema#integer"));

		StringWriter out = new StringWriter();
		Rio.write(builder.build(), out, "http://example.org/", RDFFormat.TURTLE);

		System.out.println(out);

		assertTrue(out.toString().contains("rml:source \"people.csv\""));
		assertTrue(out.toString().contains("rr:predicate ex:hasName"));
		assertTrue(out.toString().contains("rr:predicate ex:hasAge"));

	}

	@Test
	void testCreateRml() throws URISyntaxException {
		ModelBuilder builder = new ModelBuilder();
		SimpleValueFactory vf = SimpleValueFactory.getInstance();

		// Define base URI
		String baseUri = "http://example.org/";

		// Define namespaces and base IRI
		builder.setNamespace("rr", "http://www.w3.org/ns/r2rml#")

			.setNamespace("rml", "http://semweb.mmlab.be/ns/rml#")

			.setNamespace("ex", baseUri)

			.setNamespace("ql", "http://semweb.mmlab.be/ns/ql#")

			.setNamespace("xsd", "http://www.w3.org/2001/XMLSchema#");

		BNode mappingNode = vf.createBNode();

		// Create logical source
		RmlUtils.createLogicalSourceNode(builder, mappingNode, "people.csv", "ql:CSV", "/people/person");

		// Create subject map
		RmlUtils.createSubjectMapNode(builder, mappingNode, baseUri + "person/{id}", baseUri + "Person");

		// Create object map for name
		ObjectMapDTO objectMapDTO = new ObjectMapDTO();
		objectMapDTO.setKey("rml:reference");
		objectMapDTO.setLiteralValue("name");

		List<ObjectMapDTO> objectMap = List.of(objectMapDTO);

		// Create name predicate-object map
		RmlUtils.createPredicateObjectMapNode(builder, mappingNode, baseUri + "hasName", objectMap);

		ObjectMapDTO objectMapAgeDTO = new ObjectMapDTO();
		objectMapAgeDTO.setKey("rml:reference");
		objectMapAgeDTO.setLiteralValue("age");

		ObjectMapDTO objectMapTypeAgeDTO = new ObjectMapDTO();
		objectMapTypeAgeDTO.setKey("rr:datatype");
		objectMapTypeAgeDTO.setLiteralValue("xsd:integer");

		List<ObjectMapDTO> objectMapAge = List.of(objectMapAgeDTO, objectMapTypeAgeDTO);

		// Create age predicate-object map
		RmlUtils.createPredicateObjectMapNode(builder, mappingNode, baseUri + "hasAge", objectMapAge);

		StringWriter out = new StringWriter();
		Rio.write(builder.build(), out, baseUri, RDFFormat.TURTLE);

		System.out.println(out);

		assertTrue(out.toString().contains("rml:source \"people.csv\""));
		assertTrue(out.toString().contains("rr:predicate ex:hasName"));
		assertTrue(out.toString().contains("rml:reference \"name\""));
		assertTrue(out.toString().contains("rr:predicate ex:hasAge"));
		assertTrue(out.toString().contains("rml:reference \"age\""));

	}

	@Test
	void testCreateRmlWithXML() throws URISyntaxException {
		ModelBuilder builder = new ModelBuilder();
		SimpleValueFactory vf = SimpleValueFactory.getInstance();

		// Define base URI
		String baseUri = "http://example.org/";

		// Define namespaces and base IRI
		builder.setNamespace("rr", "http://www.w3.org/ns/r2rml#")

				.setNamespace("rml", "http://semweb.mmlab.be/ns/rml#")

				.setNamespace("ex", baseUri)

				.setNamespace("ql", "http://semweb.mmlab.be/ns/ql#")

				.setNamespace("xsd", "http://www.w3.org/2001/XMLSchema#");

		BNode mappingNode = vf.createBNode();

		// Create logical source
		RmlUtils.createLogicalSourceNode(builder, mappingNode, "people.xml", "ql:XPath", null);

		// Create subject map
		RmlUtils.createSubjectMapNode(builder, mappingNode, baseUri + "person/{id}", baseUri + "Person");

		// Create object map for name
		ObjectMapDTO objectMapDTO = new ObjectMapDTO();
		objectMapDTO.setKey("rml:reference");
		objectMapDTO.setLiteralValue("people/person/name");

		List<ObjectMapDTO> objectMap = List.of(objectMapDTO);

		// Create name predicate-object map
		RmlUtils.createPredicateObjectMapNode(builder, mappingNode, baseUri + "hasName", objectMap);

		ObjectMapDTO objectMapAgeDTO = new ObjectMapDTO();
		objectMapAgeDTO.setKey("rml:reference");
		objectMapAgeDTO.setLiteralValue("people/person/age");

		ObjectMapDTO objectMapTypeAgeDTO = new ObjectMapDTO();
		objectMapTypeAgeDTO.setKey("rr:datatype");
		objectMapTypeAgeDTO.setLiteralValue("xsd:integer");

		List<ObjectMapDTO> objectMapAge = List.of(objectMapAgeDTO, objectMapTypeAgeDTO);

		// Create age predicate-object map
		RmlUtils.createPredicateObjectMapNode(builder, mappingNode, baseUri + "hasAge", objectMapAge);

		StringWriter out = new StringWriter();
		Rio.write(builder.build(), out, baseUri, RDFFormat.TURTLE);

		System.out.println(out);

		assertTrue(out.toString().contains("rml:source \"people.xml\""));
		assertTrue(out.toString().contains("rr:predicate ex:hasName"));
		assertTrue(out.toString().contains("rml:reference \"people/person/name\""));
		assertTrue(out.toString().contains("rr:predicate ex:hasAge"));
		assertTrue(out.toString().contains("rml:reference \"people/person/age\""));

	}

	@Test
	void testPredefinedPrefix() {
		assertEquals("http://www.w3.org/ns/r2rml#Literal", RmlUtils.literalValueToUri("rr:Literal"));
		assertEquals("http://www.w3.org/ns/r2rml#IRI", RmlUtils.literalValueToUri("rr:IRI"));
	}

	@Test
	void testDefaultPrefix() {
		String customPrefix = "custom:prefix";
		assertEquals(customPrefix, RmlUtils.literalValueToUri(customPrefix));
	}

}
